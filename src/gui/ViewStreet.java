package gui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import maps.Coordinate;
import maps.Street;

public class ViewStreet extends Polyline {
    private Street street;
    private Glow glow = new Glow();
    private Node node;
    Tooltip tooltip = new Tooltip();
    ContextMenu contextMenu = new ContextMenu();

    public ViewStreet(Street in_street, Node in_node) {
        this.node = in_node;
        this.street = in_street;
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
        setStrokeWidth(12);
    }

    EventHandler<MouseEvent> mouseEntered = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            tooltip.setText(
                "street: " + street.getId() +
                "\ntraffic situation: " + street.GetdrivingDifficulties()
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
                System.out.println("Mouse LEFT clicked on street: " + street.getId());
            } else if (e.getButton() == MouseButton.SECONDARY)
            {
                UserInputWindow window = new UserInputWindow(street);
                System.out.println("Mouse RIGHT clicked on street " + street.getId());
            }
        }
    };
}