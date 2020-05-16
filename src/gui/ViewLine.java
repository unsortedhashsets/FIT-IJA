package gui;

import java.util.List;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import maps.Coordinate;
import maps.Line;
import maps.Stop;
import maps.Street;

/**
 * Graphic representation of Line the scene
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class ViewLine extends Polyline {
    AnchorPane work_pane;
    VBox infoBox;
    Line line;
    Group stopGroup = new Group();
    Group f_group = new Group();
    private Glow glow = new Glow();

    /**
    * Constructor of the graphic Stop representation
    * @param in_line input line object
    * @param Front_Group  front objects group
    * @param in_work_pane parent pane
    * @param in_infoBox  input infoBox object
    */
    public ViewLine(Line in_line,Group Front_Group, AnchorPane in_work_pane, VBox in_infoBox) {
        this.work_pane = in_work_pane;
        this.infoBox = in_infoBox;
        this.line = in_line;
        this.f_group = Front_Group;
        setId(line.getID());
        glow.setLevel(1.0);
        this.stopGroup.setOnMouseEntered(mouseEntered);
        this.stopGroup.setOnMouseExited(mouseExited);
        this.stopGroup.setOnMouseClicked(mouseClicked);
        setupToolTip();
        drawLine();
        this.stopGroup.getChildren().add(this);
        this.work_pane.getChildren().add(this.stopGroup);
    }

    /**
    * Set base settings of toolTip box 
    */
    private void setupToolTip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("line: " + line.getID() + "\n\nRoute:\n" + (line.getRoute().stream().map(entry -> entry.getKey().getId() + ":" + entry.getValue() + ";\n").collect(Collectors.joining())));
        Tooltip.install(this, tooltip);
        tooltip.setStyle("-fx-background-radius: 6 6 6 6; -fx-background-color:	linear-gradient( " + line.getColor() + " , #808080 );");

    }

    /**
    * General Draw the line object  
    */
    private void drawLine() {

        List<SimpleImmutableEntry<Street, Stop>> tmp = line.getRoute();
        
        for (int i = 0; i < tmp.size(); i++) {
            Street previousStreet = tmp.get(i-1).getKey();
            Stop previousStop = tmp.get(i-1).getValue();

            Street currentStreet = tmp.get(i).getKey();
            Stop currentStop = tmp.get(i).getValue();
            
            // ZERO STOP
            if (i == 0) {
                drawStop(currentStop.getCoordinate());
                getPoints().addAll(new Double[] { (double) currentStop.getCoordinate().getX(), 
                                                  (double) currentStop.getCoordinate().getY() });

                line.addCoordinate(currentStop.getCoordinate(), currentStop);
                
            } else {
                boolean setJ = false;
                int j = 0;
                // N STOPS ON ONE LINE 
                if (previousStreet.getId() == currentStreet.getId()){
                    // FIND J next street index of the previous stop
                    if (!(setJ)) {
                        for (int t = 0; t < currentStreet.getCoordinates().size(); t++){
                            if (checkIfStopIsBetweenCoords(previousStop, previousStreet.getCoordinates().get(t), previousStreet.getCoordinates().get(t + 1))) {
                                j = t+1;
                                break;
                            }
                        }
                        setJ = true;
                    }
                    // N STOPS IF ON ONE STRAIGHT LINE
                    if (currentStreet.getCoordinates().size() == 2){
                        drawStop(currentStop.getCoordinate());
                        getPoints().addAll(new Double[] { (double) currentStop.getCoordinate().getX(), 
                                                          (double) currentStop.getCoordinate().getY() });

                        line.addCoordinate(currentStop.getCoordinate(), currentStop);
                        
                    // N STOPS IF ON ONE not STRAIGHT LINE
                    } else {
                        for (; j < currentStreet.getCoordinates().size() - 1; j++) {
                            if (checkIfStopIsBetweenCoords(currentStop, currentStreet.getCoordinates().get(j), currentStreet.getCoordinates().get(j + 1))) {
                                drawStop(currentStop.getCoordinate());

                                getPoints().addAll(new Double[] { (double) currentStreet.getCoordinates().get(j).getX(), 
                                                                  (double) currentStreet.getCoordinates().get(j).getY() }); 
                                line.addCoordinate(currentStreet.getCoordinates().get(j), currentStreet);

                                getPoints().addAll(new Double[] { (double) currentStop.getCoordinate().getX(), 
                                                                  (double) currentStop.getCoordinate().getY() });
                                line.addCoordinate(currentStop.getCoordinate(), currentStop);

                                break;
                            } else {
                                getPoints().addAll(new Double[] { (double) currentStreet.getCoordinates().get(j).getX(), 
                                                                  (double) currentStreet.getCoordinates().get(j).getY()});
                                
                                line.addCoordinate(currentStreet.getCoordinates().get(j), currentStreet);
                            }
                        }
                    }
                // ONE STOP ON ONE LINE
                } else {
                    // BEGIN - BEGIN 
                    if (previousStreet.begin().equals(currentStreet.begin())){
                        BEGIN_something(tmp, i);
                        something_BEGIN(tmp, i);
                    // BEGIN - END
                    } else if (previousStreet.begin().equals(currentStreet.end())){
                        BEGIN_something(tmp, i);
                        something_END(tmp, i);
                    // END - BEGIN
                    } else if (previousStreet.end().equals(currentStreet.begin())){
                        END_something(tmp, i);
                        something_BEGIN(tmp, i);
                    // END - END
                    } else if (previousStreet.end().equals(currentStreet.end())){
                        END_something(tmp, i);
                        something_END(tmp, i);
                    }
                }

            }

        }
        setStyle("-fx-opacity: 0.8; -fx-stroke:" + line.getColor());
        setStrokeLineCap(StrokeLineCap.ROUND);
        setStrokeLineJoin(StrokeLineJoin.ROUND);
        setStrokeWidth(4);
    }

    /**
    * Draw the line object if connection ...-END exists
    * @param tmp street-stop SimpleImmutableEntry
    * @param i number in route
    */
    private void something_END(List<SimpleImmutableEntry<Street, Stop>> tmp, int i){
        // ... - END
        Street currentStreet = tmp.get(i).getKey();
        Stop currentStop = tmp.get(i).getValue();

        for (int c = currentStreet.getCoordinates().size() - 1; c >= 0; c--) {
            if (currentStop == null) {
                break;
            }

            getPoints().addAll(new Double[] { (double) currentStreet.getCoordinates().get(c).getX(), 
                                              (double) currentStreet.getCoordinates().get(c).getY() });
            line.addCoordinate(currentStreet.getCoordinates().get(c), currentStreet);
                
            if (checkIfStopIsBetweenCoords(currentStop, currentStreet.getCoordinates().get(c), currentStreet.getCoordinates().get(c-1))) { 
                drawStop(currentStop.getCoordinate());
                getPoints().addAll(new Double[] { (double) currentStop.getCoordinate().getX(), 
                                                  (double) currentStop.getCoordinate().getY() });
                line.addCoordinate(currentStop.getCoordinate(), currentStop);

                break;
            }

        }
        // ... - END
    }

    /**
    * Draw the line object if connection ...-BEGIN exists
    * @param tmp street-stop SimpleImmutableEntry
    * @param i number in route
    */
    private void something_BEGIN(List<SimpleImmutableEntry<Street, Stop>> tmp, int i){
        // ... - BEGIN
        Street currentStreet = tmp.get(i).getKey();
        Stop currentStop = tmp.get(i).getValue();

        for (int c = 0; c < currentStreet.getCoordinates().size() - 1; c++) {
            if (currentStop == null) {
                break;
            }

            getPoints().addAll(new Double[] { (double) currentStreet.getCoordinates().get(c).getX(), 
                                              (double) currentStreet.getCoordinates().get(c).getY() });
            line.addCoordinate(currentStreet.getCoordinates().get(c), currentStreet); 

            if (checkIfStopIsBetweenCoords(currentStop, currentStreet.getCoordinates().get(c), currentStreet.getCoordinates().get(c+1))) { 
                drawStop(currentStop.getCoordinate());
                getPoints().addAll(new Double[] { (double) currentStop.getCoordinate().getX(), 
                                                  (double) currentStop.getCoordinate().getY() });
                line.addCoordinate(currentStop.getCoordinate(), currentStop);
                break;
            }
        }
        // ... - BEGIN
    }

    /**
    * Draw the line object if connection BEGIN-.. exists
    * @param tmp street-stop SimpleImmutableEntry
    * @param i number in route
    */
    private void BEGIN_something(List<SimpleImmutableEntry<Street, Stop>> tmp, int i){
        // BEGIN - ... 
        Street previousStreet = tmp.get(i-1).getKey();
        Stop previousStop = tmp.get(i-1).getValue();

        for (int c = previousStreet.getCoordinates().size() - 1; c > 0; c--) {
            if (previousStop == null) {
                getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(c).getX(), 
                                                  (double) previousStreet.getCoordinates().get(c).getY() });
                line.addCoordinate(previousStreet.getCoordinates().get(c), previousStreet);

                continue;
            } else if (checkIfStopIsBetweenCoords(previousStop, previousStreet.getCoordinates().get(c), previousStreet.getCoordinates().get(c-1))) { 
                for (int j = c - 1; j >= 0; j--) {
                    getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(j).getX(), 
                                                      (double) previousStreet.getCoordinates().get(j).getY() });
                    line.addCoordinate(previousStreet.getCoordinates().get(j), previousStreet);
                } 

                break;
            }
        }

        getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(0).getX(), 
                                          (double) previousStreet.getCoordinates().get(0).getY() });
        line.addCoordinate(previousStreet.getCoordinates().get(0), previousStreet);
        // BEGIN - ... 
    }

    /**
    * Draw the line object if connection END-.. exists
    * @param tmp street-stop SimpleImmutableEntry
    * @param i number in route
    */
    private void END_something(List<SimpleImmutableEntry<Street, Stop>> tmp, int i){
        // END - ...
        Street previousStreet = tmp.get(i-1).getKey();
        Stop previousStop = tmp.get(i-1).getValue();

        Street currentStreet = tmp.get(i).getKey();

        for (int c = 0; c < currentStreet.getCoordinates().size() - 1; c++) {        
            if (previousStop == null) {
                getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(c).getX(), 
                                                  (double) previousStreet.getCoordinates().get(c).getY() });
                line.addCoordinate(previousStreet.getCoordinates().get(c), previousStreet);

                continue;

            } else if (checkIfStopIsBetweenCoords(previousStop, previousStreet.getCoordinates().get(c), previousStreet.getCoordinates().get(c+1))) { 
                for (int j = c + 1; j < currentStreet.getCoordinates().size() - 1; j++) {
                    getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(j).getX(), 
                                                      (double) previousStreet.getCoordinates().get(j).getY() });
                    line.addCoordinate(previousStreet.getCoordinates().get(j), previousStreet);
                } 

                break;
            }
        }
        
        getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(previousStreet.getCoordinates().size()-1).getX(), 
                                          (double) previousStreet.getCoordinates().get(previousStreet.getCoordinates().size()-1).getY() });
        line.addCoordinate(previousStreet.getCoordinates().get(previousStreet.getCoordinates().size()-1), previousStreet);
        // END - ...
    }

    /**
    * Draw the stop  
    */
    private void drawStop(Coordinate coor) {
        Circle circle = new Circle();
        circle.setCenterX(coor.getX());
        circle.setCenterY(coor.getY());
        circle.setRadius(6);
        circle.setStyle("-fx-opacity: 0.8; -fx-fill: " + line.getColor());
        this.stopGroup.getChildren().add(circle);
    } 

    /**
    * Control if between two coordinates exists stop
    * @param s Stop
    * @param first first coordinate
    * @param second second coordinate
    */
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

    /**
    * EventHandler - mouse entered the line graphic representation
    */
    EventHandler<MouseEvent> mouseEntered = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            stopGroup.setEffect(glow);
        }
    };

    /**
    * EventHandler - mouse exited the line graphic representation
    */
    EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            stopGroup.setEffect(null);
        }
    };

    /**
    * EventHandler - mouse clicked the line graphic representation
    */
    EventHandler<MouseEvent> mouseClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (e.getButton() == MouseButton.PRIMARY)
            {   
                InfoBoxController.InfoBoxController(infoBox, line);
            } else if (e.getButton() == MouseButton.SECONDARY)
            {                
                stopGroup.toFront();
                f_group.toFront();
            }
        }
    };
    

}
