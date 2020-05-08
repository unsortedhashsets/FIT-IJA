package gui;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import maps.Stop;

public class ViewStop extends Circle {
    private Stop stop;
    private Glow glow = new Glow();

    public ViewStop(Stop in_stop) {
        this.stop = in_stop;
        glow.setLevel(0.9);
        setOnMouseEntered(mouseEntered);
        setOnMouseExited(mouseExited);
        setupToolTip();
        drawStop();
    }

    private void setupToolTip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("stop: " + stop.getId());
        Tooltip.install(this, tooltip);
        tooltip.getStyleClass().add("stop-tip");
    }

    private void drawStop() {
        setCenterX(stop.getCoordinate().getX());
        setCenterY(stop.getCoordinate().getY());
        setRadius(8);
        setFill(Color.WHITE);
        setStroke(Color.GREY);
        setStrokeWidth(4);
    }

    EventHandler<MouseEvent> mouseEntered = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            setEffect(glow);
            System.out.println("Mouse entered on stop: " + stop.getId());
        }
    };

    EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            setEffect(null);
            System.out.println("Mouse exited from stop: " + stop.getId());
        }
    };

}