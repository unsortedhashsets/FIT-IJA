package gui;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polyline;
import maps.Line;

public class ViewLine extends Polyline {
    Line line;
    private Glow glow = new Glow();

    public ViewLine(Line in_line) {
        this.line = in_line;
        glow.setLevel(0.9);
        setOnMouseEntered(mouseEntered);
        setOnMouseExited(mouseExited);
        setupToolTip();
        drawLine();
    }

    private void setupToolTip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("line: " + line.getID());
        Tooltip.install(this, tooltip);
        tooltip.getStyleClass().add("line-tip");
    }

    private void drawLine() {
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
