package gui;

import java.util.ArrayList;
import java.util.Collections;
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

/**
 * Graphic representation of the info block in the scene
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class InfoBoxController {

    /**
    * Infobox constructor
    * @param infoBox parent infoBox object
    * @param line line to provide information about
    */
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
        for (SimpleImmutableEntry<Street, Stop> tmp : line.getRoute()) {
            text.setText(text.getText() + tmp.getKey().getId());
            if (tmp.getValue() != null) {
                text.setText(text.getText() + " - " + tmp.getValue().getId() + "\n");
            } else {
                text.setText(text.getText() + "\n");
            }
        }
        infoBox.getChildren().add(text);
    }

    /**
    * Infobox constructor
    * @param infoBox parent infoBox object
    * @param street street to provide information about
    */
    public static void InfoBoxController(VBox infoBox, Street street) {

        // Clear VBox
        infoBox.getChildren().clear();

        // Set Label
        infoBox.getChildren().add(new Label("Street: " + street.getId()));

        // Set Forms
        infoBox.getChildren().add(new ImageView(new Image("street.png", 180, 180, false, false)));

        // Set Coordinates
        Text text = new Text("Coordinates: \n");
        for (Coordinate tmp : street.getCoordinates()) {
            text.setText(text.getText() + tmp.toString() + "\n");
        }
        infoBox.getChildren().add(text);

        // Set Traffic situation
        infoBox.getChildren().add(new Text("Traffic difficulties: " + street.GetdrivingDifficulties()));
    }

    /**
    * Infobox constructor
    * @param infoBox parent infoBox object
    * @param stop stop to provide information about
    */
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

    /**
    * Infobox constructor
    * @param infoBox parent infoBox object
    * @param vehicle vehicle to provide information about
    */
    public static void InfoBoxController(VBox infoBox, Vehicle vehicle) {

        // Clear VBox
        infoBox.getChildren().clear();

        // Set Label
        infoBox.getChildren().add(new Label("Vehicle: " + vehicle.getId()));

        // Set Forms
        if (vehicle instanceof Tram) {
            infoBox.getChildren().add(new ImageView(new Image("tra_tra.png", 180, 180, false, false)));
        } else if (vehicle instanceof Trolley) {
            infoBox.getChildren().add(new ImageView(new Image("tro_tra.png", 180, 180, false, false)));
        } else if (vehicle instanceof Autobus) {
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

        // Set Coordinates and // Set next STOPS
        Text textC = new Text(vehicle.getPosition().toString());
        Text textS = new Text(vehicle.getPosition().toString());

        infoBox.getChildren().addAll(textC, textS);

        Timeline updatePos = new Timeline(new KeyFrame(Duration.millis(100), (ActionEvent event) -> {
            textC.textProperty().set("Actual location: \n" + vehicle.getPosition().toString());
            textS.textProperty().set(vehicle.toString());
        }));
        updatePos.setCycleCount(Timeline.INDEFINITE);
        updatePos.play();

        // Set next STOPS
        Group group2 = new Group();

        Timeline updatePos2 = new Timeline(new KeyFrame(Duration.millis(1), (ActionEvent event) -> {

            group2.getChildren().clear();

            Text text2 = new Text("");
            text2.setText("Arrival time   (Stop - Arrival time in):\n\n   ");
            group2.getChildren().add(text2);

            Polyline polyline2 = new Polyline();
            polyline2.setStyle("-fx-stroke:" + vehicle.getLine().getColor());
            polyline2.setStrokeLineCap(StrokeLineCap.ROUND);
            polyline2.setStrokeLineJoin(StrokeLineJoin.ROUND);
            polyline2.setStrokeWidth(3);

            group2.getChildren().add(polyline2);
            
            ArrayList<SimpleImmutableEntry<Stop, Integer>> list = vehicle.getSchedule();
            Collections.reverse(list);

            if (list.size() >= 1) {
            
                for (SimpleImmutableEntry<Stop, Integer> tmp : list) {

                    int pointY = 25 + vehicle.getSchedule().indexOf(tmp) * 15;

                    polyline2.getPoints().addAll(new Double[] { (double) 0, (double) pointY });


                    Circle circle2 = new Circle();
                    circle2.setCenterX(0);
                    circle2.setCenterY(pointY);
                    circle2.setRadius(6);

                    if (vehicle.getSchedule().indexOf(tmp) == list.size()-1){
                        circle2.setStyle("-fx-fill: RED");
                        Circle circle3 = new Circle();
                        circle3.setCenterX(0);
                        circle3.setCenterY(pointY);
                        circle3.setStyle("-fx-fill: YELLOW");
                        circle3.setRadius(3);
                        circle3.setCenterX(0);
                        group2.getChildren().add(circle2);
                        group2.getChildren().add(circle3);
                    } else {
                        circle2.setStyle("-fx-fill: " + vehicle.getLine().getColor());
                        group2.getChildren().add(circle2);
                    }
                    int mins = (tmp.getValue() % 3600) / 60;
                    int secs = tmp.getValue() % 60;
                    
                    if (mins ==0) {
                        text2.setText(text2.getText() + tmp.getKey().getId() + " - in : " + secs + "s\n   ");
                    } else {
                        text2.setText(text2.getText() + tmp.getKey().getId() + " - in : " + mins + "m " + secs + "s\n   ");
                    }
                }
            }
        }));

        updatePos2.setCycleCount(Timeline.INDEFINITE);
        updatePos2.play();
        infoBox.getChildren().add(group2);
	}
}