package gui;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import maps.Stop;
import vehicles.Autobus;
import vehicles.Tram;
import vehicles.Trolley;
import vehicles.Vehicle;

public class ViewVehicle extends Circle {
    private Vehicle vehicle;
    private Glow glow = new Glow();

    public ViewVehicle(Vehicle in_vehicle) {
        this.vehicle = in_vehicle;
        setId(in_vehicle.getId());
        glow.setLevel(0.9);
        setOnMouseEntered(mouseEntered);
        setOnMouseExited(mouseExited);
        setupToolTip();
        drawStop();
    }

    private void setupToolTip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText("stop: " + vehicle.getId());
        Tooltip.install(this, tooltip);
        tooltip.getStyleClass().add("stop-tip");
    }

    private void drawStop() {
        setRadius(0);
        if (vehicle instanceof  Tram) {
            setFill(new ImagePattern(new Image("tra.png")));
        } else if (vehicle instanceof  Trolley) {
            setFill(new ImagePattern(new Image("tro.png")));
        } else if (vehicle instanceof  Autobus) {
            setFill(new ImagePattern(new Image("bus.png")));
        }
        setStyle("-fx-stroke: " + vehicle.getLine().getColor());
    }        

    public void UpdatePosition() {
        setCenterX(vehicle.getPosition().getX());
        setCenterY(vehicle.getPosition().getY());
        setRadius(14);
        setStrokeWidth(1);
    }

    public Vehicle GetVehicle() {
        return vehicle;
    }  

    EventHandler<MouseEvent> mouseEntered = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            setEffect(glow);  
            System.out.println("Mouse entered on stop: " + vehicle.getId());
        }
    };

    EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            setEffect(null);
            System.out.println("Mouse exited from stop: " + vehicle.getId());
        }
    };

}