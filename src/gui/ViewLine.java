package gui;

import java.util.List;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import maps.Coordinate;
import maps.Line;
import maps.Stop;
import maps.Street;

public class ViewLine extends Polyline {
    AnchorPane work_pane;
    Line line;

    private Glow glow = new Glow();

    public ViewLine(Line in_line, AnchorPane in_work_pane) {
        this.work_pane = in_work_pane;
        this.line = in_line;
        setId(line.getID());
        glow.setLevel(1.0);
        setOnMouseEntered(mouseEntered);
        setOnMouseExited(mouseExited);
        setupToolTip();
        drawLine();
    }

    private void setupToolTip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("line: " + line.getID() + "\n\nRoute:\n" + (line.getRoute().stream().map(entry -> entry.getKey().getId() + ":" + entry.getValue() + ";\n").collect(Collectors.joining())));
        Tooltip.install(this, tooltip);
        tooltip.setStyle("-fx-background-radius: 6 6 6 6; -fx-background-color:	linear-gradient( " + line.getColor() + " , #808080 );");

    }

    private void drawLine() {

        List<SimpleImmutableEntry<Street, Stop>> tmp = line.getRoute();

        System.out.println(line.getID());
        System.out.println(line.getRoute().stream().map(entry -> entry.getKey().getId() + ":" + entry.getValue() + ";").collect(Collectors.joining()));
        
        for (int i = 0; i < tmp.size(); i++) {
            // ZERO STOP
            if (i == 0) {
                drawStop(tmp.get(i).getValue().getCoordinate());
                System.out.println("0 POINT: " + tmp.get(i).getValue().getCoordinate().getX() + " - " + tmp.get(i).getValue().getCoordinate().getY());                
                line.addCoordinate(tmp.get(i).getValue().getCoordinate(), tmp.get(i).getValue());
                getPoints().addAll(new Double[] { (double) tmp.get(i).getValue().getCoordinate().getX(), (double) tmp.get(i).getValue().getCoordinate().getY() });
            } else {
                boolean setJ = false;
                int j = 0;
                // N STOPS ON ONE LINE 
                if (tmp.get(i-1).getKey().getId() == tmp.get(i).getKey().getId()){
                    // FIND J next street index of the previous stop
                    if (!(setJ)) {
                        for (int t = 0; t < tmp.get(i).getKey().getCoordinates().size(); t++){
                            if (checkIfStopIsBetweenCoords(tmp.get(i-1).getValue(), tmp.get(i-1).getKey().getCoordinates().get(t), tmp.get(i-1).getKey().getCoordinates().get(t + 1))) {
                                j = t+1;
                                break;
                            }
                        }
                        setJ = true;
                    }
                    // N STOPS IF ON ONE STRAIGHT LINE
                    if (tmp.get(i).getKey().getCoordinates().size() == 2){
                        drawStop(tmp.get(i).getValue().getCoordinate());
                        System.out.println(i + " POINT: " + tmp.get(i).getValue().getCoordinate().getX() + " - " + tmp.get(i).getValue().getCoordinate().getY());
                        line.addCoordinate(tmp.get(i).getValue().getCoordinate(), tmp.get(i).getValue());
                        getPoints().addAll(new Double[] { (double) tmp.get(i).getValue().getCoordinate().getX(), (double) tmp.get(i).getValue().getCoordinate().getY() });
                    // N STOPS IF ON ONE not STRAIGHT LINE
                    } else {
                        for (; j < tmp.get(i).getKey().getCoordinates().size() - 1; j++) {
                            if (checkIfStopIsBetweenCoords(tmp.get(i).getValue(), tmp.get(i).getKey().getCoordinates().get(j), tmp.get(i).getKey().getCoordinates().get(j + 1))) {
                                System.out.println(i + " CORNER: " + tmp.get(i).getKey().getCoordinates().get(j).getX() + " - " + tmp.get(i).getKey().getCoordinates().get(j).getY());
                                line.addCoordinate(tmp.get(i).getKey().getCoordinates().get(j), tmp.get(i).getKey());
                                getPoints().addAll(new Double[] { (double) tmp.get(i).getKey().getCoordinates().get(j).getX(), (double) tmp.get(i).getKey().getCoordinates().get(j).getY() }); 
                                drawStop(tmp.get(i).getValue().getCoordinate());
                                System.out.println(i + " POINT: " + tmp.get(i).getValue().getCoordinate().getX() + " - " + tmp.get(i).getValue().getCoordinate().getY());                  
                                getPoints().addAll(new Double[] { (double) tmp.get(i).getValue().getCoordinate().getX(), (double) tmp.get(i).getValue().getCoordinate().getY() });
                                line.addCoordinate(tmp.get(i).getValue().getCoordinate(), tmp.get(i).getValue());
                                break;
                            } else {
                                System.out.println(i + " CORNER: " + tmp.get(i).getKey().getCoordinates().get(j).getX() + " - " + tmp.get(i).getKey().getCoordinates().get(j).getY());
                                getPoints().addAll(new Double[] { (double) tmp.get(i).getKey().getCoordinates().get(j).getX(), (double) tmp.get(i).getKey().getCoordinates().get(j).getY() });
                                line.addCoordinate(tmp.get(i).getKey().getCoordinates().get(j), tmp.get(i).getKey());
                            }
                        }
                    }
                // ONE STOP ON ONE LINE
                } else {
                    // EMPTY STREET
                    // ONE STOP ON ONE LINE (STREETS CONNECTIONS: END-END or BEGIN-END)
                    int c;
                    // END-END or END-BEGIN
                    if (tmp.get(i-1).getKey().end().equals(tmp.get(i).getKey().end()) || tmp.get(i-1).getKey().end().equals(tmp.get(i).getKey().begin())) {
                        for (c = tmp.get(i-1).getKey().getCoordinates().size() - 1; c > 0; c--) {
                            if (tmp.get(i-1).getValue() == null){
                                // END-END
                                if (tmp.get(i-1).getKey().end().equals(tmp.get(i).getKey().begin())) {
                                    c = c - 1;
                                    System.out.println(i + " 1 CORNER: " + tmp.get(i-1).getKey().getCoordinates().get(tmp.get(i-1).getKey().getCoordinates().size()-1-c).getX() + " - " + tmp.get(i-1).getKey().getCoordinates().get(tmp.get(i-1).getKey().getCoordinates().size()-1-c).getY());                 // <----------- CORD
                                    getPoints().addAll(new Double[] { (double) tmp.get(i-1).getKey().getCoordinates().get(tmp.get(i-1).getKey().getCoordinates().size()-c-1).getX(), (double) tmp.get(i-1).getKey().getCoordinates().get(tmp.get(i-1).getKey().getCoordinates().size()-1-c).getY() });
                                    line.addCoordinate(tmp.get(i-1).getKey().getCoordinates().get(tmp.get(i-1).getKey().getCoordinates().size()-1-c), tmp.get(i-1).getKey());
                                // END-BEGIN     
                                } else {
                                    System.out.println(i + " 2 CORNER: " + tmp.get(i-1).getKey().getCoordinates().get(c).getX() + " - " + tmp.get(i-1).getKey().getCoordinates().get(c).getY());      
                                    getPoints().addAll(new Double[] { (double) tmp.get(i-1).getKey().getCoordinates().get(c).getX(), (double) tmp.get(i-1).getKey().getCoordinates().get(c).getY() });
                                    line.addCoordinate(tmp.get(i-1).getKey().getCoordinates().get(c), tmp.get(i-1).getKey());
                                }
                            } else if (checkIfStopIsBetweenCoords(tmp.get(i-1).getValue(), tmp.get(i-1).getKey().getCoordinates().get(c-1), tmp.get(i-1).getKey().getCoordinates().get(c))) { 
                                for (; c < tmp.get(i-1).getKey().getCoordinates().size(); c++){
                                    System.out.println(i + " 3 CORNER: " + tmp.get(i-1).getKey().getCoordinates().get(c).getX() + " - " + tmp.get(i-1).getKey().getCoordinates().get(c).getY());             
                                    getPoints().addAll(new Double[] { (double) tmp.get(i-1).getKey().getCoordinates().get(c).getX(), (double) tmp.get(i-1).getKey().getCoordinates().get(c).getY() });
                                    line.addCoordinate(tmp.get(i-1).getKey().getCoordinates().get(c), tmp.get(i-1).getKey());
                                }
                                break;
                            }
                        }
                    // BEGIN-END or BEGIN-BEGIN
                    } else {
                        for (c = 1; c < tmp.get(i-1).getKey().getCoordinates().size(); c++) {
                            if (tmp.get(i-1).getValue() == null){
                                // BEGIN-END
                                if (tmp.get(i-1).getKey().begin().equals(tmp.get(i).getKey().end())) {                                    
                                    System.out.println(i + " 4 CORNER: " + tmp.get(i-1).getKey().getCoordinates().get(tmp.get(i-1).getKey().getCoordinates().size()-1-c).getX() + " - " + tmp.get(i-1).getKey().getCoordinates().get(tmp.get(i-1).getKey().getCoordinates().size()-1-c).getY());   // <----------- CORD
                                    getPoints().addAll(new Double[] { (double) tmp.get(i-1).getKey().getCoordinates().get(tmp.get(i-1).getKey().getCoordinates().size()-1-c).getX(), (double) tmp.get(i-1).getKey().getCoordinates().get(tmp.get(i-1).getKey().getCoordinates().size()-1-c).getY() });
                                    line.addCoordinate(tmp.get(i-1).getKey().getCoordinates().get(tmp.get(i-1).getKey().getCoordinates().size()-1), tmp.get(i-1).getKey());
                                // BEGIN-BEGIN
                                } else {
                                    System.out.println(i + " 5 CORNER: " + tmp.get(i-1).getKey().getCoordinates().get(c).getX() + " - " + tmp.get(i-1).getKey().getCoordinates().get(c).getY());              
                                    getPoints().addAll(new Double[] { (double) tmp.get(i-1).getKey().getCoordinates().get(c).getX(), (double) tmp.get(i-1).getKey().getCoordinates().get(c).getY() });
                                    line.addCoordinate(tmp.get(i-1).getKey().getCoordinates().get(c), tmp.get(i-1).getKey());
                                }
                            } else if (checkIfStopIsBetweenCoords(tmp.get(i-1).getValue(), tmp.get(i-1).getKey().getCoordinates().get(c), tmp.get(i-1).getKey().getCoordinates().get(c+1))) { 
                                for (; c >= 0; c--){
                                    System.out.println(i + " 6 CORNER: " + tmp.get(i-1).getKey().getCoordinates().get(c).getX() + " - " + tmp.get(i-1).getKey().getCoordinates().get(c).getY());              
                                    getPoints().addAll(new Double[] { (double) tmp.get(i-1).getKey().getCoordinates().get(c).getX(), (double) tmp.get(i-1).getKey().getCoordinates().get(c).getY() });
                                    line.addCoordinate(tmp.get(i-1).getKey().getCoordinates().get(c), tmp.get(i-1).getKey());
                                }
                                break;
                            }
                        }
                    }
                    if (tmp.get(i).getValue() == null) {
                        continue;
                    }
                    if (tmp.get(i).getKey().getCoordinates().size() == 2) {
                        drawStop(tmp.get(i).getValue().getCoordinate());
                        System.out.println(i + " 0 POINT: " + tmp.get(i).getValue().getCoordinate().getX() + " - " + tmp.get(i).getValue().getCoordinate().getY());
                        line.addCoordinate(tmp.get(i).getValue().getCoordinate(), tmp.get(i).getValue());
                        getPoints().addAll(new Double[] { (double) tmp.get(i).getValue().getCoordinate().getX(), (double) tmp.get(i).getValue().getCoordinate().getY() });
                    } else {
                        if (tmp.get(i-1).getKey().end().equals(tmp.get(i).getKey().begin()) || tmp.get(i-1).getKey().begin().equals(tmp.get(i).getKey().begin())) {
                            for (c = 1; c < tmp.get(i).getKey().getCoordinates().size() - 1; c++) {
                                System.out.println(i + " 7 CORNER: " + tmp.get(i).getKey().getCoordinates().get(c).getX() + " - " + tmp.get(i).getKey().getCoordinates().get(c).getY());              
                                getPoints().addAll(new Double[] { (double) tmp.get(i).getKey().getCoordinates().get(c).getX(), (double) tmp.get(i).getKey().getCoordinates().get(c).getY() });
                                line.addCoordinate(tmp.get(i).getKey().getCoordinates().get(c), tmp.get(i).getKey());
                                if (checkIfStopIsBetweenCoords(tmp.get(i).getValue(), tmp.get(i).getKey().getCoordinates().get(c), tmp.get(i).getKey().getCoordinates().get(c+1))) {
                                    drawStop(tmp.get(i).getValue().getCoordinate());
                                    System.out.println(i + " 2 POINT: " + tmp.get(i).getValue().getCoordinate().getX() + " - " + tmp.get(i).getValue().getCoordinate().getY());
                                    line.addCoordinate(tmp.get(i).getValue().getCoordinate(), tmp.get(i).getValue());
                                    getPoints().addAll(new Double[] { (double) tmp.get(i).getValue().getCoordinate().getX(), (double) tmp.get(i).getValue().getCoordinate().getY() });
                                    break;
                                }
                            }
                        } else {
                            for (c = tmp.get(i).getKey().getCoordinates().size() - 1; c > 0; c--) {
                                System.out.println(i + " 8 CORNER: " + tmp.get(i).getKey().getCoordinates().get(c).getX() + " - " + tmp.get(i).getKey().getCoordinates().get(c).getY());              
                                getPoints().addAll(new Double[] { (double) tmp.get(i).getKey().getCoordinates().get(c).getX(), (double) tmp.get(i).getKey().getCoordinates().get(c).getY() });
                                line.addCoordinate(tmp.get(i).getKey().getCoordinates().get(c), tmp.get(i).getKey());
                                if (checkIfStopIsBetweenCoords(tmp.get(i).getValue(), tmp.get(i).getKey().getCoordinates().get(c), tmp.get(i).getKey().getCoordinates().get(c-1))) {
                                    drawStop(tmp.get(i).getValue().getCoordinate());
                                    System.out.println(i + " 1 POINT: " + tmp.get(i).getValue().getCoordinate().getX() + " - " + tmp.get(i).getValue().getCoordinate().getY());
                                    line.addCoordinate(tmp.get(i).getValue().getCoordinate(), tmp.get(i).getValue());
                                    getPoints().addAll(new Double[] { (double) tmp.get(i).getValue().getCoordinate().getX(), (double) tmp.get(i).getValue().getCoordinate().getY() });
                                    break;
                                }
                            }
                        }
                    }

                }

            }

        }
        setStyle("-fx-stroke:" + line.getColor());
        setStrokeLineCap(StrokeLineCap.ROUND);
        setStrokeLineJoin(StrokeLineJoin.ROUND);
        setStrokeWidth(4);
    }

    private void drawStop(Coordinate coor) {
        Circle circle = new Circle();
        circle.setCenterX(coor.getX());
        circle.setCenterY(coor.getY());
        circle.setRadius(6);
        circle.setStyle("-fx-fill: " + line.getColor());
        this.work_pane.getChildren().add(circle);
    }

    private Boolean checkIfStopIsBetweenCoords(Stop s, Coordinate first, Coordinate second) {
        // test point in x-range
        if ((first.getX() <= s.getCoordinate().getX() && s.getCoordinate().getX() <= second.getX())
                || (second.getX() <= s.getCoordinate().getX() && s.getCoordinate().getX() <= first.getX())) {
            // test point in y-range
            if ((first.getY() <= s.getCoordinate().getY() && s.getCoordinate().getY() <= second.getY())
                    || (second.getY() <= s.getCoordinate().getY() && s.getCoordinate().getY() <= first.getY())) {
                // test point is on the line
                if ((s.getCoordinate().diffX(first) * second.diffY(first)) == (second.diffX(first) * s.getCoordinate().diffY(first))) {
                    return true;
                }
            }
        }
        return false;
    }

    EventHandler<MouseEvent> mouseEntered = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            setEffect(glow);
            System.out.println("Mouse entered on Line: " + line.getID());
        }
    };

    EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            setEffect(null);
            System.out.println("Mouse exited from Line: " + line.getID());
        }
    };

}
