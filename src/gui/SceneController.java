
package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import javafx.event.EventHandler;

import java.net.URL;
import java.util.*;


/**
 * Scen controller (Scene.fxml)
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class SceneController implements Initializable {

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
            Coordinates.setText("Coordinates: " + (int) e.getX() + ":" + (int) e.getY());
        }
    };

    /**
     * closeClick
     */
    @FXML
    private void cleanClick() {
        System.out.println("TEST SceneController.cleanClick");
    }

    @FXML
    private void newClick() {
        System.out.println("TEST SceneController.newClick");
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
        System.out.println("TEST SceneController.zoomInClick");
    }

    @FXML
    private void zoomOutClick() {
        System.out.println("TEST SceneController.zoomOutClick");
    }

    @FXML
    private void aboutClick() {
        System.out.println("TEST SceneController.aboutClick");
    }
}
