package gui;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import maps.Coordinate;
import maps.Street;

public class ViewStreet extends Polyline {
    private Street street;
    private Glow glow = new Glow();

    public ViewStreet(Street in_street) {
        this.street = in_street;
        glow.setLevel(0.9);
        setOnMouseEntered(mouseEntered);
        setOnMouseExited(mouseExited);
        setupToolTip();
        drawStreet();
    }

    private void setupToolTip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("street: " + street.getId());
        Tooltip.install(this, tooltip);
        tooltip.getStyleClass().add("street-tip");
    }

    private void drawStreet() {
        for (Coordinate tmp : street.getCoordinates()) {
            getPoints().addAll(new Double[] { (double) tmp.getX(), (double) tmp.getY() });
        }
        setStroke(Color.GREY);
        setStrokeWidth(8);
    }

    EventHandler<MouseEvent> mouseEntered = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
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
}