package gui;

import java.util.AbstractMap.SimpleImmutableEntry;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import maps.Coordinate;
import maps.Line;
import maps.Stop;
import maps.Street;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import vehicles.Autobus;
import vehicles.Tram;
import vehicles.Trolley;
import vehicles.Vehicle;


public class InfoBoxController {
    
	public static void InfoBoxController(VBox infoBox, Line line) {
        
        // Clear VBox
        infoBox.getChildren().clear();
        
        // Set Label
        infoBox.getChildren().add(new Label("Line: " + line.getID()));

        // Set Forms
        Group group = new Group();
        Circle circle = new Circle();
        circle.setCenterX(110);
        circle.setCenterY(110);
        circle.setRadius(6);
        circle.setStyle("-fx-fill: " + line.getColor());
        Polyline polyline = new Polyline(200, 20, 20, 200);
        polyline.setStyle("-fx-stroke:" + line.getColor());
        polyline.setStrokeLineCap(StrokeLineCap.ROUND);
        polyline.setStrokeLineJoin(StrokeLineJoin.ROUND);
        polyline.setStrokeWidth(4);
        group.getChildren().addAll(polyline, circle);
        infoBox.getChildren().add(group);

        // Set Route
        Text text = new Text("Route (Street - Stop): \n\n");
        for (SimpleImmutableEntry<Street, Stop> tmp : line.getRoute()){
            text.setText(text.getText() + tmp.getKey().getId());
            if (tmp.getValue() != null) {
                text.setText(text.getText() + " - " + tmp.getValue().getId() + "\n");
            } else {
                text.setText(text.getText() + "\n");
            }
        }
        infoBox.getChildren().add(text);
    }
    
    public static void InfoBoxController(VBox infoBox, Street street) {
        
        // Clear VBox
        infoBox.getChildren().clear();
        
        // Set Label
        infoBox.getChildren().add(new Label("Street: " + street.getId()));

        // Set Forms
        infoBox.getChildren().add(new ImageView(new Image("street.png", 180, 180, false, false)));

        // Set Coordinates
        Text text = new Text("Coordinates: \n");
        for (Coordinate tmp : street.getCoordinates()){
            text.setText(text.getText() + tmp.toString() + "\n");
        }
        infoBox.getChildren().add(text);

        // Set Traffic situation
        infoBox.getChildren().add(new Text("Traffic difficulties: " + street.GetdrivingDifficulties()));
    }
    
    public static void InfoBoxController(VBox infoBox, Stop stop) {

        // Clear VBox
        infoBox.getChildren().clear();
        
        // Set Label
        infoBox.getChildren().add(new Label("Station: " + stop.getId()));

        // Set Forms
        infoBox.getChildren().add(new ImageView(new Image("stop.png", 180, 180, false, false)));

        // Set Street
        infoBox.getChildren().add(new Text("Located on the street: \n" + stop.getStreet().getId()));

        // Set Coordinates
        infoBox.getChildren().add(new Text("Coordinates: " + stop.getCoordinate().toString()));

    }
    
    public static void InfoBoxController(VBox infoBox, Vehicle vehicle) {

        // Clear VBox
        infoBox.getChildren().clear();
        
        // Set Label
        infoBox.getChildren().add(new Label("Vehicle: " + vehicle.getId()));

        // Set Forms
        if (vehicle instanceof  Tram) {
            infoBox.getChildren().add(new ImageView(new Image("tra_tra.png", 180, 180, false, false)));
        } else if (vehicle instanceof  Trolley) {
            infoBox.getChildren().add(new ImageView(new Image("tro_tra.png", 180, 180, false, false)));
        } else if (vehicle instanceof  Autobus) {
            infoBox.getChildren().add(new ImageView(new Image("bus_tra.png", 180, 180, false, false)));
        }

        // Set Line
        infoBox.getChildren().add(new Text("Line: " + vehicle.getLine().getID()));

        // Drow Line
        Group group = new Group();
        Circle circle = new Circle();
        circle.setCenterX(110);
        circle.setCenterY(0);
        circle.setRadius(6);
        circle.setStyle("-fx-fill: " + vehicle.getLine().getColor());
        Polyline polyline = new Polyline(20, 0, 200, 0);
        polyline.setStyle("-fx-stroke:" + vehicle.getLine().getColor());
        polyline.setStrokeLineCap(StrokeLineCap.ROUND);
        polyline.setStrokeLineJoin(StrokeLineJoin.ROUND);
        polyline.setStrokeWidth(4);
        group.getChildren().addAll(polyline, circle);
        infoBox.getChildren().add(group);
        
        // Set Coordinates
        Text text = new Text(vehicle.getPosition().toString());
        text.setId("Actual place: ");
        infoBox.getChildren().add(text);
        Timeline updatePos = new Timeline(new KeyFrame(Duration.millis(40), (ActionEvent event) -> {
            text.textProperty().set("Actual location: " + vehicle.getPosition().toString());
        }));
        updatePos.setCycleCount(Timeline.INDEFINITE);
        updatePos.play();
	}
}