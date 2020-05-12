package gui;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import maps.Stop;

public class ViewStop extends Circle {
    private Stop stop;
    private Glow glow = new Glow();
    AnchorPane work_pane;
    VBox infoBox;

    public ViewStop(Stop in_stop, AnchorPane in_work_pane, VBox in_infoBox) {
        this.work_pane = in_work_pane;
        this.infoBox = in_infoBox;
        this.stop = in_stop;
        this.infoBox = in_infoBox;
        setId(stop.getId());
        glow.setLevel(0.9);
        setOnMouseEntered(mouseEntered);
        setOnMouseExited(mouseExited);
        setOnMouseClicked(mouseClicked);
        setupToolTip();
        drawStop();
        this.work_pane.getChildren().add(this);
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
        setRadius(14);
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        setStrokeWidth(6);
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

    EventHandler<MouseEvent> mouseClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (e.getButton() == MouseButton.PRIMARY)
            {   
                System.out.println("Mouse LEFT clicked on stop: " + stop.getId());
                InfoBoxController.InfoBoxController(infoBox, stop);
            } else if (e.getButton() == MouseButton.SECONDARY)
            {                
                System.out.println("Mouse RIGHT clicked on stop " + stop.getId());
            }
        }
    };

}