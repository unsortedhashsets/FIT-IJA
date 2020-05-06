
package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;

import java.io.File;
import java.net.URL;
import java.util.*;

import internal.Parser;
/**
 * Scen controller (Scene.fxml)
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class SceneController implements Initializable {

    private Scale scale = new Scale();
    private int cordX;
    private int cordY;
    @FXML
    private MenuItem Start;

    @FXML
    private MenuItem SpeedIn;

    @FXML
    private MenuItem SpeeDe;

    @FXML
    private MenuItem SetTime;

    @FXML
    private MenuItem zoomIn;

    @FXML
    private MenuItem zoomOut;

    @FXML
    private BorderPane scene;

    @FXML
    private MenuItem newButton;

    @FXML
    private MenuItem cleanButton;

    @FXML
    private AnchorPane work_area;

    @FXML
    private ScrollPane scroll_work_area;

    @FXML
    private Label Coordinates;

    @FXML
    private Label Clocks;

    /**
     * Inicializace sceny
     * 
     * @param location  lokace
     * @param resources zdroj
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scroll_work_area.setPannable(true);
        work_area.getChildren().add(new ImageView(new Image("Paris_Revisited_preview.png")));

        newButton.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        cleanButton.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        Start.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        SpeedIn.setAccelerator(new KeyCodeCombination(KeyCode.I));
        SpeeDe.setAccelerator(new KeyCodeCombination(KeyCode.D));
        SetTime.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        zoomIn.setAccelerator(new KeyCodeCombination(KeyCode.PLUS));
        zoomOut.setAccelerator(new KeyCodeCombination(KeyCode.MINUS));

        work_area.setOnMouseMoved(mouseMove);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames()
                .add(new KeyFrame(Duration.millis(10), new KeyValue(Clocks.textProperty(), "ТУТ НАДО ВЕРНУТЬ СТРИНГ")));

        timeline.play();
        System.out.println("TEST SceneController.initialize");
    }

    EventHandler<MouseEvent> mouseMove = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            cordX = (int) e.getX();
            cordY = (int) e.getY();
            Coordinates.setText("Coordinates: " + cordX + ":" + cordY);
        }
    };

    /**
     * closeClick
     */
    @FXML
    private void cleanClick() {
        work_area.getChildren().clear();
        work_area.getChildren().add(new ImageView(new Image("pngwing.com.png")));
    }

    @FXML
    private void newClick() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null)
            Parser.parse(file);
        else
            System.out.println("No file choosed");

    }

    @FXML
    private void startClick() {
        System.out.println("TEST SceneController.startClick");
    }

    @FXML
    private void setTimeClick() {
        System.out.println("TEST SceneController.setTimeClick");
    }

    @FXML
    private void speedIncrClick() {
        System.out.println("TEST SceneController.speedIncrClick");
    }

    @FXML
    private void speedDecrClick() {
        System.out.println("TEST SceneController.speedDecrClick");
    }

    @FXML
    private void zoomInClick() {
        scale.setPivotX(cordX);
        scale.setPivotY(cordY);

        scale.setX(work_area.getScaleX() * 1.1);
        scale.setY(work_area.getScaleY() * 1.1);

        work_area.getTransforms().add(scale);

    }

    @FXML
    private void zoomOutClick() {
        scale.setPivotX(cordX);
        scale.setPivotY(cordY);

        scale.setX(work_area.getScaleX() * 0.9);
        scale.setY(work_area.getScaleY() * 0.9);

        work_area.getTransforms().add(scale);

    }

    @FXML
    private void aboutClick() {
        System.out.println("TEST SceneController.aboutClick");
    }
}
