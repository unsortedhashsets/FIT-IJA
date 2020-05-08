import internal.InternalClock;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main program class - Provides window and scene creation
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Main extends Application {

    public static void main(String[] args) {
        InternalClock.setDefaultClock();
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("gui/Scene.fxml"));
        
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("I");
        scene.getStylesheets().add("gui/Scene.css");
        primaryStage.show();
    }


}