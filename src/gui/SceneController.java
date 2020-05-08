
package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import maps.Line;
import maps.Stop;
import maps.Street;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.net.URL;
import java.util.*;

import internal.InternalClock;
import internal.Parser;

/**
 * Scen controller (Scene.fxml)
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class SceneController implements Initializable {

    private List<ViewStreet> viewStreets;
    private List<ViewLine> viewLines;
    private List<ViewStop> viewStops;

    private int cordX;
    private int cordY;

    @FXML
    private MenuItem Start;

    @FXML
    private MenuItem SpeedIn;

    @FXML
    private MenuItem SpeedDe;

    @FXML
    private MenuItem SetTime;

    @FXML
    private MenuItem DefTime;

    @FXML
    private MenuItem zoomIn;

    @FXML
    private MenuItem zoomOut;

    @FXML
    private MenuItem zoomDef;

    @FXML
    private BorderPane scene;

    @FXML
    private MenuItem newButton;

    @FXML
    private MenuItem cleanButton;

    @FXML
    private Label Coordinates;

    @FXML
    private Label Clocks;

    @FXML
    private BorderPane main;

    private AnchorPane work_area;

    private ZoomableScrollPane scroll_work_area;

    /**
     * Inicializace sceny
     * 
     * @param location  lokace
     * @param resources zdroj
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitialScen();
        setHandlers();
        setHotKeys();
        setClockTimeLine();
    }

    private void setInitialScen() {
        this.work_area = new AnchorPane();
        // work_area.getChildren().add(new ImageView(new
        // Image("Paris_Revisited_preview.png")));
        this.scroll_work_area = new ZoomableScrollPane(work_area);
        scroll_work_area.setFitToWidth(true);
        scroll_work_area.setFitToHeight(true);
        main.setCenter(scroll_work_area);
    }

    private void setClockTimeLine() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), (ActionEvent event) -> {
            Clocks.textProperty().set(InternalClock.updateClock());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setHandlers() {
        work_area.setOnMouseEntered(mouseEnteredAndMove);
        work_area.setOnMouseMoved(mouseEnteredAndMove);
        work_area.setOnMouseExited(mouseExited);
        work_area.setOnScroll(mouseScroll);
    }

    private void setHotKeys() {
        newButton.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        cleanButton.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        Start.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        SpeedIn.setAccelerator(new KeyCodeCombination(KeyCode.A));
        SpeedDe.setAccelerator(new KeyCodeCombination(KeyCode.D));
        DefTime.setAccelerator(new KeyCodeCombination(KeyCode.T));
        SetTime.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        zoomIn.setAccelerator(new KeyCodeCombination(KeyCode.W));
        zoomOut.setAccelerator(new KeyCodeCombination(KeyCode.S));
        zoomDef.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
    }

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

        List<Street> streets = Parser.getStreets();
        this.viewStreets = new ArrayList<>();
        for (int i = 0; i < streets.size(); i++) {
            this.viewStreets.add(new ViewStreet(streets.get(i)));
            work_area.getChildren().add(this.viewStreets.get(i));
        }

        List<Stop> stops = Parser.getStops();
        this.viewStops = new ArrayList<>();
        for (int i = 0; i < stops.size(); i++) {
            this.viewStops.add(new ViewStop(stops.get(i)));
            work_area.getChildren().add(this.viewStops.get(i));
        }

        List<Line> lines = Parser.getLines();
        this.viewLines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            this.viewLines.add(new ViewLine(lines.get(i)));
            work_area.getChildren().add(this.viewLines.get(i));
        }
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
        InternalClock.increaseAccelerationLevel();
        System.out.println("TEST SceneController.speedIncrClick");
    }

    @FXML
    private void speedDecrClick() {
        InternalClock.decreaseAccelerationLevel();
        System.out.println("TEST SceneController.speedDecrClick");
    }

    @FXML
    private void speedDefaClick() {
        InternalClock.defaultAccelerationLevel();
        System.out.println("TEST SceneController.speedDefaClick");
    }

    @FXML
    private void zoomInClick() {
        scroll_work_area.zoomIn();
        System.out.println("TEST SceneController.zoomInClick");
    }

    @FXML
    private void zoomDefClick() {
        scroll_work_area.zoomToDefault();
        System.out.println("TEST SceneController.zoomDefClick");
    }

    @FXML
    private void zoomOutClick() {
        scroll_work_area.zoomOut();
        System.out.println("TEST SceneController.zoomOutClick");
    }

    @FXML
    private void aboutClick() {
        System.out.println("TEST SceneController.aboutClick");
    }

    EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            Coordinates.setText("");
        }
    };

    EventHandler<MouseEvent> mouseEnteredAndMove = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            cordX = (int) e.getX();
            cordY = (int) e.getY();
            Coordinates.setText("Coordinates: " + cordX + ":" + cordY);
        }
    };

    EventHandler<ScrollEvent> mouseScroll = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            if (scrollEvent.isControlDown()) {
                if (scrollEvent.getDeltaY() < 0) {
                    scroll_work_area.zoomOut();
                } else {
                    scroll_work_area.zoomIn();
                }
                scrollEvent.consume();
            }
        }
    };
}
