package gui;

import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import maps.Coordinate;
import maps.Street;

/**
 * Graphic representation of the Street in the scene
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class ViewStreet extends Polyline {
    public Street street;
    private Glow glow = new Glow();
    Tooltip tooltip = new Tooltip();
    ContextMenu contextMenu = new ContextMenu();
    MenuItem open_close = new MenuItem("Close/Open Street");
    MenuItem changeTraffic = new MenuItem("Set up traffic situation");
    AnchorPane work_pane;
    VBox infoBox;
    public EventHandler<MouseEvent> oldHandler;

    /**
    * Constructor of the graphic Street representation 
    * @param in_street  input street object 
    * @param in_work_pane parent pane
    * @param in_infoBox  input infoBox object 
    */
    public ViewStreet(Street in_street, AnchorPane in_work_pane, VBox in_infoBox) {
        this.work_pane = in_work_pane;
        this.infoBox = in_infoBox;
        this.street = in_street;
        setId(street.getId());
        glow.setLevel(0.9);
        setOnMouseEntered(mouseEntered);
        setOnMouseExited(mouseExited);
        setOnMouseClicked(mouseClicked);
        setupToolTip();
        drawStreet();
        this.work_pane.getChildren().add(this);
    }

    /**
    * Set base settings of toolTip box 
    */
    private void setupToolTip() {
        tooltip.setText(
            "street: " + street.getId() +
            "\ntraffic situation: " + street.getDrivingDifficulties()
        );
        Tooltip.install(this, tooltip);
        tooltip.getStyleClass().add("street-tip");
    }

    /**
    * Draw the street object  
    */
    private void drawStreet() {
        for (Coordinate tmp : street.getCoordinates()) {
            getPoints().addAll(new Double[] { (double) tmp.getX(), (double) tmp.getY() });
        }
        setStroke(Color.BLACK);
        setStrokeLineCap(StrokeLineCap.ROUND);
        setStrokeLineJoin(StrokeLineJoin.ROUND);
        setStrokeWidth(16);
    }

    /**
    * EventHandler - mouse entered the street graphic representation
    */
    EventHandler<MouseEvent> mouseEntered = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            tooltip.setText(
                "street: " + street.getId() +
                "\n\nTraffic situation: " + street.getDrivingDifficulties() +
                "\nStatus: " + street.getStatusString()
            );
            setEffect(glow);
        }
    };

    /**
    * EventHandler - mouse exited the street graphic representation
    */
    EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            setEffect(null);
        }
    };

    /**
    * EventHandler - mouse clicked on the street graphic representation
    */
    EventHandler<MouseEvent> mouseClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (e.getButton() == MouseButton.PRIMARY)
            {   
                new InfoBoxController(infoBox, street);
            } else if (e.getButton() == MouseButton.SECONDARY)
            {
                contextMenu.getItems().add(open_close);
                contextMenu.getItems().add(changeTraffic);
                open_close.setOnAction(event1 -> {
                    if (street.getStatus()) {
                        setStroke(Color.RED);
                        getStrokeDashArray().addAll(20d,20d);
                        street.setDrivingDifficulties(100);
                        street.setStatus(false);
                    } else {
                        setStroke(Color.BLACK);
                        getStrokeDashArray().clear();
                        street.setDrivingDifficulties(0);
                        street.setStatus(true);     
                    }
                });
                changeTraffic.setOnAction(event1 -> {
                    new SetTrafficWindow(street);
                });
                contextMenu.show(work_pane, e.getScreenX(), e.getScreenY());
            }
        }
    };
}



                
                