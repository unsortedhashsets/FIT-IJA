import internal.InternalClock;
import javafx.application.Application;
import javafx.application.Platform;
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
        Platform.setImplicitExit(true);

        Parent root = FXMLLoader.load(getClass().getResource("gui/Scene.fxml"));
        

        
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("IJA PROJECT 2020");
        scene.getStylesheets().add("gui/Scene.css");

        primaryStage.setOnCloseRequest((ae) -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }


}