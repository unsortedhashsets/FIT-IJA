package gui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import maps.Coordinate;
import maps.Street;

public class ViewStreet extends Polyline {
    private Street street;
    private Glow glow = new Glow();
    private Node node;
    Tooltip tooltip = new Tooltip();
    ContextMenu contextMenu = new ContextMenu();
    MenuItem open = new MenuItem("Open street");
    MenuItem close = new MenuItem("Close street");
    AnchorPane work_pane;

    public ViewStreet(Street in_street, Node in_node, AnchorPane work_pane) {
        this.node = in_node;
        this.street = in_street;
        this.work_pane = work_pane;
        setId(street.getId());
        glow.setLevel(0.9);
        setOnMouseEntered(mouseEntered);
        setOnMouseExited(mouseExited);
        setOnMouseClicked(mouseClicked);
        setupToolTip();
        drawStreet();
    }

    private void setupToolTip() {
        tooltip.setText(
            "street: " + street.getId() +
            "\ntraffic situation: " + street.GetdrivingDifficulties()
        );
        Tooltip.install(this, tooltip);
        tooltip.getStyleClass().add("street-tip");
    }

    private void drawStreet() {
        for (Coordinate tmp : street.getCoordinates()) {
            getPoints().addAll(new Double[] { (double) tmp.getX(), (double) tmp.getY() });
        }
        setStroke(Color.BLACK);
        setStrokeLineCap(StrokeLineCap.ROUND);
        setStrokeLineJoin(StrokeLineJoin.ROUND);
        setStrokeWidth(16);
    }

    EventHandler<MouseEvent> mouseEntered = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            tooltip.setText(
                "street: " + street.getId() +
                "\n\nTraffic situation: " + street.GetdrivingDifficulties() +
                "\nStatus: " + street.GetStatusString()
            );
            setEffect(glow);
            System.out.println("Mouse entered on street: " + street.getId());
        }
    };

    EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            setEffect(null);
            System.out.println("Mouse exited from street: " + street.getId());
        }
    };

    EventHandler<MouseEvent> mouseClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (e.getButton() == MouseButton.PRIMARY)
            {   
                contextMenu.getItems().add(close);
                contextMenu.getItems().add(open);
                close.setOnAction(event1 -> {
                    System.out.println("SIGNAL on close click " + street.getId());
                    System.out.println("STREET: " + street.getId() + " was closed on coords: " + street.getCoordinates().get(0).getX() + " - " + street.getCoordinates().get(0).getX() + " | " + street.getCoordinates().get(street.getCoordinates().size()-1).getX() + " - " + street.getCoordinates().get(street.getCoordinates().size()-1).getX());
                    setStroke(Color.RED);
                    getStrokeDashArray().addAll(20d,20d);
                    street.SetStatus(false);
                });
                open.setOnAction(event1 -> {
                    System.out.println("SIGNAL on open click " + street.getId());
                    System.out.println("STREET: " + street.getId() + " was opened on coords: " + street.getCoordinates().get(0).getX() + " - " + street.getCoordinates().get(0).getX() + " | " + street.getCoordinates().get(street.getCoordinates().size()-1).getX() + " - " + street.getCoordinates().get(street.getCoordinates().size()-1).getX());
                    setStroke(Color.BLACK);
                    getStrokeDashArray().clear();
                    street.SetStatus(true);     
                });
                contextMenu.show(node, e.getScreenX(), e.getScreenY());
                System.out.println("Mouse LEFT clicked on street: " + street.getId());
            } else if (e.getButton() == MouseButton.SECONDARY)
            {
                UserInputWindow window = new UserInputWindow(street);
                System.out.println("Mouse RIGHT clicked on street " + street.getId());
            }
        }
    };
}



                
                