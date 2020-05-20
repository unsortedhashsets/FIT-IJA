package gui;

import java.util.ArrayList;
import java.util.List;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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
import vehicles.Vehicle;

/**
 * Graphic representation of Line the scene
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class ViewLine extends Polyline {
    AnchorPane work_pane;
    VBox infoBox;
    Line line;
    ContextMenu contextMenu = new ContextMenu();
    MenuItem newLine = new MenuItem("Draw new line");
    MenuItem defaultLine = new MenuItem("Return Default line");
    Group stopGroup = new Group();
    Group f_group = new Group();
    private Glow glow = new Glow();
    List<ViewStreet> viewStreets;

    /**
     * Constructor of the graphic Stop representation
     * 
     * @param in_line      input line object
     * @param Front_Group  front objects group
     * @param in_work_pane parent pane
     * @param in_infoBox   input infoBox object
     */
    public ViewLine(Line in_line, Group Front_Group, AnchorPane in_work_pane, VBox in_infoBox,
            List<ViewStreet> in_viewStreets) {
        this.work_pane = in_work_pane;
        this.infoBox = in_infoBox;
        this.line = in_line;
        this.f_group = Front_Group;
        this.viewStreets = in_viewStreets;
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
        tooltip.setText("line: " + line.getID() + "\n\n" + "Route:\n" + (line.getRoute().stream()
                .map(entry -> entry.getKey().getId() + ":" + entry.getValue() + ";\n").collect(Collectors.joining())));
        Tooltip.install(this, tooltip);
        tooltip.setStyle("-fx-background-radius: 6 6 6 6; -fx-background-color:	linear-gradient( " + line.getColor()
                + " , #808080 );");

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
                Street previousStreet = tmp.get(i - 1).getKey();
                Stop previousStop = tmp.get(i - 1).getValue();

                boolean setJ = false;
                int j = 0;
                // N STOPS ON ONE LINE
                if (previousStreet.getId() == currentStreet.getId()) {
                    // FIND J next street index of the previous stop
                    if (!(setJ)) {
                        for (int t = 0; t < currentStreet.getCoordinates().size(); t++) {
                            if (Street.isInStreet(previousStop.getCoordinate(), previousStreet.getCoordinates().get(t),
                                    previousStreet.getCoordinates().get(t + 1))) {
                                j = t + 1;
                                break;
                            }
                        }
                        setJ = true;
                    }
                    // N STOPS IF ON ONE STRAIGHT LINE
                    if (currentStreet.getCoordinates().size() == 2) {
                        drawStop(currentStop.getCoordinate());
                        getPoints().addAll(new Double[] { (double) currentStop.getCoordinate().getX(),
                                (double) currentStop.getCoordinate().getY() });

                        line.addCoordinate(currentStop.getCoordinate(), currentStop);

                        // N STOPS IF ON ONE not STRAIGHT LINE
                    } else {
                        for (; j < currentStreet.getCoordinates().size() - 1; j++) {
                            if (Street.isInStreet(currentStop.getCoordinate(), currentStreet.getCoordinates().get(j),
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
                                        (double) currentStreet.getCoordinates().get(j).getY() });

                                line.addCoordinate(currentStreet.getCoordinates().get(j), currentStreet);
                            }
                        }
                    }
                    // ONE STOP ON ONE LINE
                } else {
                    // BEGIN - BEGIN
                    if (previousStreet.begin().equals(currentStreet.begin())) {
                        BEGIN_something(previousStreet, previousStop, i);
                        something_BEGIN(currentStreet, currentStop, i);
                        // BEGIN - END
                    } else if (previousStreet.begin().equals(currentStreet.end())) {
                        BEGIN_something(previousStreet, previousStop, i);
                        something_END(currentStreet, currentStop, i);
                        // END - BEGIN
                    } else if (previousStreet.end().equals(currentStreet.begin())) {
                        END_something(previousStreet, previousStop, i);
                        something_BEGIN(currentStreet, currentStop, i);
                        // END - END
                    } else if (previousStreet.end().equals(currentStreet.end())) {
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
     * 
     * @param currentStreet current street
     * @param currentStop   current stop
     * @param i             number in route
     */
    private void something_END(Street currentStreet, Stop currentStop, int i) {

        for (int c = currentStreet.getCoordinates().size() - 1; c >= 0; c--) {
            if (currentStop == null) {
                break;
            }

            getPoints().addAll(new Double[] { (double) currentStreet.getCoordinates().get(c).getX(),
                    (double) currentStreet.getCoordinates().get(c).getY() });
            line.addCoordinate(currentStreet.getCoordinates().get(c), currentStreet);

            if (Street.isInStreet(currentStop.getCoordinate(), currentStreet.getCoordinates().get(c),
                    currentStreet.getCoordinates().get(c - 1))) {
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
     * 
     * @param currentStreet current street
     * @param currentStop   current stop
     * @param i             number in route
     */
    private void something_BEGIN(Street currentStreet, Stop currentStop, int i) {

        for (int c = 0; c < currentStreet.getCoordinates().size() - 1; c++) {
            if (currentStop == null) {
                break;
            }

            getPoints().addAll(new Double[] { (double) currentStreet.getCoordinates().get(c).getX(),
                    (double) currentStreet.getCoordinates().get(c).getY() });
            line.addCoordinate(currentStreet.getCoordinates().get(c), currentStreet);

            if (Street.isInStreet(currentStop.getCoordinate(), currentStreet.getCoordinates().get(c),
                    currentStreet.getCoordinates().get(c + 1))) {
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
     * 
     * @param previousStreet previous street
     * @param previousStop   previous stop
     * @param i              number in route
     */
    private void BEGIN_something(Street previousStreet, Stop previousStop, int i) {

        for (int c = previousStreet.getCoordinates().size() - 1; c > 0; c--) {
            if (previousStop == null) {
                getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(c).getX(),
                        (double) previousStreet.getCoordinates().get(c).getY() });
                line.addCoordinate(previousStreet.getCoordinates().get(c), previousStreet);

                continue;
            } else if (Street.isInStreet(previousStop.getCoordinate(), previousStreet.getCoordinates().get(c),
                    previousStreet.getCoordinates().get(c - 1))) {
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
     * 
     * @param previousStreet previous street
     * @param previousStop   previous stop
     * @param i              number in route
     */
    private void END_something(Street previousStreet, Stop previousStop, int i) {

        for (int c = 0; c < previousStreet.getCoordinates().size() - 1; c++) {
            if (previousStop == null) {
                getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(c).getX(),
                        (double) previousStreet.getCoordinates().get(c).getY() });
                line.addCoordinate(previousStreet.getCoordinates().get(c), previousStreet);

                continue;

            } else if (Street.isInStreet(previousStop.getCoordinate(), previousStreet.getCoordinates().get(c),
                    previousStreet.getCoordinates().get(c + 1))) {
                for (int j = c + 1; j < previousStreet.getCoordinates().size() - 1; j++) {
                    getPoints().addAll(new Double[] { (double) previousStreet.getCoordinates().get(j).getX(),
                            (double) previousStreet.getCoordinates().get(j).getY() });
                    line.addCoordinate(previousStreet.getCoordinates().get(j), previousStreet);
                }

                break;
            }
        }

        getPoints().addAll(new Double[] {
                (double) previousStreet.getCoordinates().get(previousStreet.getCoordinates().size() - 1).getX(),
                (double) previousStreet.getCoordinates().get(previousStreet.getCoordinates().size() - 1).getY() });
        line.addCoordinate(previousStreet.getCoordinates().get(previousStreet.getCoordinates().size() - 1),
                previousStreet);

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
            if (e.getButton() == MouseButton.PRIMARY) {
                new InfoBoxController(infoBox, line);
                stopGroup.toFront();
                f_group.toFront();
            } else if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.getItems().add(newLine);
                contextMenu.getItems().add(defaultLine);
                newLine.setOnAction(event1 -> {

                    if (line.OLD_route == null && line.OLD_coordinates == null){
                        line.OLD_route = new ArrayList<SimpleImmutableEntry<Street, Stop>>(line.route);
                        line.OLD_coordinates = new ArrayList<SimpleImmutableEntry<Coordinate, Object>>(line.coordinates);
                    }

                    line.coordinates = new ArrayList<SimpleImmutableEntry<Coordinate, Object>>();
                    ArrayList<SimpleImmutableEntry<Street, Stop>> NEW_route = new ArrayList<SimpleImmutableEntry<Street, Stop>>();
                    ArrayList<SimpleImmutableEntry<Street, Stop>> TMP_route = new ArrayList<SimpleImmutableEntry<Street, Stop>>();

                    for (ViewStreet tmp : viewStreets) {
                        tmp.oldHandler = (EventHandler<MouseEvent>) tmp.getOnMouseClicked();
                        tmp.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent ev) {
                                if (ev.getButton() == MouseButton.PRIMARY) {
                                    String picked_street_id = ev.getPickResult().getIntersectedNode().getId();

                                    for (ViewStreet vStreet : viewStreets){
                                        if (vStreet.street.getId() ==  picked_street_id){
                                            TMP_route.add(new SimpleImmutableEntry<>(vStreet.street,null));
                                        }
                                    }
                                } else if (ev.getButton() == MouseButton.SECONDARY) {
                                    for (ViewStreet tmp : viewStreets) {
                                        tmp.setOnMouseClicked(tmp.oldHandler);
                                    }

                                    SimpleImmutableEntry<Street, Stop> first = TMP_route.get(0);
                                    SimpleImmutableEntry<Street, Stop> last = TMP_route.get(TMP_route.size() - 1);

                                    ArrayList<Integer> first_index = new ArrayList<Integer>();
                                    ArrayList<Integer> last_index = new ArrayList<Integer>();

                                    for (SimpleImmutableEntry<Street, Stop> step : line.route){
                                        if (step.getKey() == first.getKey()) {
                                            first_index.add(line.route.indexOf(step));
                                        } else if (step.getKey() == last.getKey()) {
                                            last_index.add(line.route.indexOf(step));
                                        }
                                    }

                                    for (int i = last_index.get(0)-1; i >= first_index.get(first_index.size()-1)+1 ; i--){
                                        line.route.remove(i);
                                    }

                                    TMP_route.remove(0);
                                    TMP_route.remove(TMP_route.size()-1);

                                    int size = TMP_route.size();
                                    for (int i = first_index.get(first_index.size()-1)+1; i <= size+first_index.get(first_index.size()-1); i++){
                                        line.route.add(i, TMP_route.get(0));
                                        TMP_route.remove(0);
                                    }

                                    work_pane.getChildren().remove(stopGroup);

                                    new ViewLine(line, f_group , work_pane, infoBox, viewStreets);

                                    for (Vehicle i : line.getVehicles()){
                                        i.updateKeyPositions();
                                    }
                                }
                            }
                        });
                    }
                });
                defaultLine.setOnAction(event1 -> {

                    line.route = new ArrayList<SimpleImmutableEntry<Street, Stop>>(line.OLD_route);
                    line.coordinates = new ArrayList<SimpleImmutableEntry<Coordinate, Object>>();

                    work_pane.getChildren().remove(stopGroup);

                    new ViewLine(line, f_group , work_pane, infoBox, viewStreets);

                    for (Vehicle i : line.getVehicles()){
                        i.updateKeyPositions();
                    }
                });
                contextMenu.show(work_pane, e.getScreenX(), e.getScreenY());
            }
        }
    };
    

}
