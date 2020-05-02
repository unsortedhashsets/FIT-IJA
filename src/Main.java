import javafx.application.Application;

//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import gui.Test;
import clock.InternalClock;

/**
 * Main program class - Provides window and scene creation
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        // Parent root = FXMLLoader.load(getClass().getResource("gui/scene.fxml")); some
        // example
        Test.HelloWorld();
        
        Group root = new Group();

        Scene scene = new Scene(root, 300, 300, Color.BLACK);
        // scene.getStylesheets().add("gui/mainScene.css");

        Rectangle r = new Rectangle(25, 25, 250, 250);
        r.setFill(Color.BLUE);

        root.getChildren().add(r);

        primaryStage.setScene(scene);
        primaryStage.setTitle("IJA-JA-JA-JA");

        primaryStage.show();

    }

    public int getInt() {
        return 1;
    }
}