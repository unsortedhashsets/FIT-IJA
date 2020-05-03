import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.Test;

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

        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("gui/Scene.fxml")), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("IJA-JA-JA-JA");
        scene.getStylesheets().add("gui/Scene.css");
        primaryStage.show();

        Test.HelloWorld();
        
    }
}