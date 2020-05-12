package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import maps.Line;
import maps.Stop;
import maps.Street;
import vehicles.Vehicle;


public class InfoBoxController {
    
	public static void InfoBoxController(VBox infoBox, Line line) {
        
        infoBox.getChildren().clear();

        infoBox.getChildren().add(new ImageView(new Image("pngwing.com.png", 150, 150, false, false)));
        infoBox.getChildren().add(new Label("A label: " + line.getID()));
    }
    
    public static void InfoBoxController(VBox infoBox, Street street) {
        
        infoBox.getChildren().clear();

        infoBox.getChildren().add(new ImageView(new Image("pngwing.com.png", 150, 150, false, false)));
        infoBox.getChildren().add(new Label("A label: " + street.getId()));
    }
    
    public static void InfoBoxController(VBox infoBox, Stop stop) {
        
        infoBox.getChildren().clear();

        infoBox.getChildren().add(new ImageView(new Image("pngwing.com.png", 150, 150, false, false)));
        infoBox.getChildren().add(new Label("A label: " + stop.getId()));
    }
    
    public static void InfoBoxController(VBox infoBox, Vehicle vehicle) {
        
        infoBox.getChildren().clear();

        infoBox.getChildren().add(new ImageView(new Image("pngwing.com.png", 150, 150, false, false)));
        infoBox.getChildren().add(new Label("A label: " + vehicle.getId()));
        infoBox.getChildren().add(new Text(vehicle.toString()));
        Text text = new Text(vehicle.getPosition().toString());
        text.setId("TEXT");
        infoBox.getChildren().add(text);
        Timeline updatePos = new Timeline(new KeyFrame(Duration.millis(40), (ActionEvent event) -> {
            text.textProperty().set(vehicle.getPosition().toString());
        }));
        updatePos.setCycleCount(Timeline.INDEFINITE);
        updatePos.play();
	}
}