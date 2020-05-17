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
        tooltip.setText("line: " + line.getID() + "\n\n" + 
                        "Route:\n" + (line.getRoute().stream()
                                          .map(entry -> entry.getKey().getId() 
                                                      + ":" 
                                                      + entry.getValue() 
                                                      + ";\n").collect(Collectors.joining())));
        Tooltip.install(this, tooltip);
        tooltip.setStyle("-fx-background-radius: 6 6 6 6; -fx-background-color:	linear-gradient( " + line.getColor() + " , #808080 );");

    }

    /**
    * General Draw the line object  
    */
    private void drawLine() {

        List<SimpleImmutableEntry<Street, Stop>> tmp = line.getRoute();
        
        for (int i = 0; i < tmp.size(); i++) {
            Street currentStreet = tmp.get(i).getKey();
            Stop currentStop = tmp.get(i).getValue();
            
            // ZERO STOP
            if (i == 0) {
                drawStop(currentStop.getCoordinate());
                getPoints().addAll(new Double[] { (double) currentStop.getCoordinate().getX(), 
                                                  (double) currentStop.getCoordinate().getY() });

                line.addCoordinate(currentStop.getCoordinate(), currentStop);
                
            } else {
                Street previousStreet = tmp.get(i-1).getKey();
                Stop previousStop = tmp.get(i-1).getValue();

                boolean setJ = false;
                int j = 0;
                // N STOPS ON ONE LINE 
                if (previousStreet.getId() == currentStreet.getId()){
                    // FIND J next street index of the previous stop
                    if (!(setJ)) {
                        for (int t = 0; t < currentStreet.getCoordinates().size(); t++){
                            if (Street.isInStreet(previousStop.getCoordinate(), 
                                                  previousStreet.getCoordinates().get(t), 
                                                  previousStreet.getCoordinates().get(t + 1))) {
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
                            if (Street.isInStreet(currentStop.getCoordinate(), 
                                                  currentStreet.getCoordinates().get(j), 
                                                  currentStreet.getCoordinates().get(j + 1))) {
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
                        BEGIN_something(previousStreet, previousStop, i);
                        something_BEGIN(currentStreet, currentStop, i);
                    // BEGIN - END
                    } else if (previousStreet.begin().equals(currentStreet.end())){
                        BEGIN_something(previousStreet, previousStop, i);
                        something_END(currentStreet, currentStop, i);
                    // END - BEGIN
                    } else if (previousStreet.end().equals(currentStreet.begin())){
                        END_something(previousStreet, previousStop, i);
                        something_BEGIN(currentStreet, currentStop, i);
                    // END - END
                    } else if (previousStreet.end().equals(currentStreet.end())){
                        END_something(previousStreet, previousStop, i);
                        something_END(currentStreet, currentStop, i);
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
    * @param currentStreet current street
    * @param currentStop current stop
    * @param i number in route
    */
    private void something_END(Street currentStreet, Stop currentStop, int i){

        for (int c = currentStreet.getCoordinates().size() - 1; c >= 0; c--) {
            if (currentStop == null) {
                break;
            }

            getPoints().addAll(new Double[] { (double) currentStreet.getCoordinates().get(c).getX(), 
                                              (double) currentStreet.getCoordinates().get(c).getY() });
            line.addCoordinate(currentStreet.getCoordinates().get(c), currentStreet);
                
            if (Street.isInStreet(currentStop.getCoordinate(),
                                  currentStreet.getCoordinates().get(c),
                                  currentStreet.getCoordinates().get(c-1))) { 
                drawStop(currentStop.getCoordinate());
                getPoints().addAll(new Double[] { (double) currentStop.getCoordinate().getX(), 
                                                  (double) currentStop.getCoordinate().getY() });
                line.addCoordinate(currentStop.getCoordinate(), currentStop);

                break;
            }

        }

    }

    /**
    * Draw the line object if connection ...-BEGIN exists
    * @param currentStreet current street
    * @param currentStop current stop
    * @param i number in route
    */
    private void something_BEGIN(Street currentStreet, Stop currentStop, int i){

        for (int c = 0; c < currentStreet.getCoordinates().size() - 1; c++) {
            if (currentStop == null) {
                break;
            }

            getPoints().addAll(new Double[] { (double) currentStreet.getCoordinates().get(c).getX(), 
                                              (double) currentStreet.getCoordinates().get(c).getY() });
            line.addCoordinate(currentStreet.getCoordinates().get(c), currentStreet); 

            if (Street.isInStreet(currentStop.getCoordinate(),
                                  currentStreet.getCoordinates().get(c),
                                  currentStreet.getCoordinates().get(c+1))) { 
                drawStop(currentStop.getCoordinate());
                getPoints().addAll(new Double[] { (double) currentStop.getCoordinate().getX(), 
                                                  (double) currentStop.getCoordinate().getY() });
                line.addCoordinate(currentStop.getCoordinate(), currentStop);
                break;
            }
        }
    }

    /**
    * Draw the line object if connection BEGIN-.. exists
    * @param previousStreet previous street
    * @param previousStop previous stop
    * @param i number in route
    */
    private void BEGIN_something(Street previousStreet, Stop previousStop, int i){

        for (int c = previousStreet.getCoordinates().size() - 1; c > 0; c--) {
            if (previousStop == null) {
                getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(c).getX(), 
                                                  (double) previousStreet.getCoordinates().get(c).getY() });
                line.addCoordinate(previousStreet.getCoordinates().get(c), previousStreet);

                continue;
            } else if (Street.isInStreet(previousStop.getCoordinate(),
                                         previousStreet.getCoordinates().get(c),
                                         previousStreet.getCoordinates().get(c-1))) { 
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

    }

    /**
    * Draw the line object if connection END-.. exists
    * @param previousStreet previous street
    * @param previousStop previous stop
    * @param i number in route
    */
    private void END_something(Street previousStreet, Stop previousStop, int i){

        for (int c = 0; c < previousStreet.getCoordinates().size() - 1; c++) {        
            if (previousStop == null) {
                getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(c).getX(), 
                                                  (double) previousStreet.getCoordinates().get(c).getY() });
                line.addCoordinate(previousStreet.getCoordinates().get(c), previousStreet);

                continue;

            } else if (Street.isInStreet(previousStop.getCoordinate(),
                                         previousStreet.getCoordinates().get(c),
                                         previousStreet.getCoordinates().get(c+1))) { 
                for (int j = c + 1; j < previousStreet.getCoordinates().size() - 1; j++) {
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
                new InfoBoxController(infoBox, line);
            } else if (e.getButton() == MouseButton.SECONDARY)
            {                
                stopGroup.toFront();
                f_group.toFront();
            }
        }
    };
    

}
