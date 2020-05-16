
package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import maps.Line;
import maps.Stop;
import maps.Street;
import vehicles.Vehicle;
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

    private Boolean NewPushed = false;

    private List<ViewStreet> viewStreets;
    private List<ViewLine> viewLines;
    private List<ViewStop> viewStops;
    private List<ViewVehicle> viewVehicles;

    private Group vehiclesGroup;

    private Timeline updatePos;

    private int cordX;
    private int cordY;

    @FXML
    private MenuItem Start;
    
    @FXML
    private MenuItem Stop;

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
    
    @FXML
    private VBox infoBox;

    private AnchorPane work_area;

    private ZoomableScrollPane scroll_work_area;

    /**
     * Scene initialization
     * 
     * @param location  location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitialScen();
        setHandlers();
        setHotKeys();
        setClockTimeLine();
    }

    /**
     * Set initial scen settings
     */
    private void setInitialScen() {
        this.work_area = new AnchorPane();
        // work_area.getChildren().add(new ImageView(new Image("Paris_Revisited_preview.png")));
        this.scroll_work_area = new ZoomableScrollPane(work_area);
        scroll_work_area.setFitToWidth(true);
        scroll_work_area.setFitToHeight(true);
        main.setCenter(scroll_work_area);
    }

    /**
    * Set clocks
    */
    private void setClockTimeLine() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), (ActionEvent event) -> {
            Clocks.textProperty().set(InternalClock.updateClock());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
    * Set event handlers
    */
    private void setHandlers() {
        work_area.setOnMouseEntered(mouseEnteredAndMove);
        work_area.setOnMouseMoved(mouseEnteredAndMove);
        work_area.setOnMouseExited(mouseExited);
        work_area.setOnScroll(mouseScroll);
    }

    /**
    * Set event hot keys
    */
    private void setHotKeys() {
        newButton.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        cleanButton.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        Start.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        Stop.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN));
        SpeedIn.setAccelerator(new KeyCodeCombination(KeyCode.A));
        SpeedDe.setAccelerator(new KeyCodeCombination(KeyCode.D));
        DefTime.setAccelerator(new KeyCodeCombination(KeyCode.T));
        SetTime.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        zoomIn.setAccelerator(new KeyCodeCombination(KeyCode.W));
        zoomOut.setAccelerator(new KeyCodeCombination(KeyCode.S));
        zoomDef.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
    }

    /**
     * CLEAN click 
     */
    @FXML
    private void cleanClick() {
        work_area.getChildren().clear();
        this.NewPushed = false;
        // work_area.getChildren().add(new ImageView(new Image("pngwing.com.png")));
    }

    /**
    * NEW click 
    */
    @FXML
    private void newClick() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Parser.parse(file);
            work_area.getChildren().clear();

            try {
                String background = Parser.getBackgroundPath();
                int width = Parser.getWidth();
                int height = Parser.getHeight();
                work_area.getChildren().add(new ImageView(new Image(background, width, height, false, true)));
            } catch (java.lang.IllegalArgumentException err) {
                work_area.setBackground(new Background(new BackgroundImage(new Image("ground.png", 375, 216, false, true),
                BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT))); 
            }
            // FIX!

            List<Street> streets = Parser.getStreets();
            this.viewStreets = new ArrayList<>();
            for (int i = 0; i < streets.size(); i++) {
                this.viewStreets.add(new ViewStreet(streets.get(i), work_area, infoBox));
            }

            List<Stop> stops = Parser.getStops();
            this.viewStops = new ArrayList<>();
            for (int i = 0; i < stops.size(); i++) {
                this.viewStops.add(new ViewStop(stops.get(i), work_area, infoBox));
            }

            vehiclesGroup = new Group();

            List<Vehicle> vehicles = Parser.getVehicles();
            this.viewVehicles = new ArrayList<>();
            for (int i = vehicles.size() -1; i >= 0; i--) {
                this.viewVehicles.add(new ViewVehicle(vehicles.get(i),vehiclesGroup , infoBox));
            }

            List<Line> lines = Parser.getLines();
            this.viewLines = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                this.viewLines.add(new ViewLine(lines.get(i),vehiclesGroup , work_area, infoBox));
            }

            work_area.getChildren().add(this.vehiclesGroup);


            this.NewPushed = true;

        } else {
            System.out.println("No file choosed");
        }

    }

    /**
    * START click 
    */
    @FXML
    private void startClick() {
        if (this.NewPushed) {
            for (ViewVehicle tmp : viewVehicles) {
                tmp.GetVehicle().start();
            }
            updatePos = new Timeline(new KeyFrame(Duration.millis(40), (ActionEvent event) -> {
                for (ViewVehicle tmp : viewVehicles) {
                    tmp.UpdatePosition();
                }
            }));
            Timeline timeline = new Timeline();

            timeline.setCycleCount(1);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1)));
            timeline.play();

            updatePos.setCycleCount(Timeline.INDEFINITE);
            updatePos.play();
        }
    }

    /**
    * STOP click 
    */
    @FXML
    private void stopClick() {
        if (this.NewPushed) {
            updatePos.stop();
            for (ViewVehicle tmp : viewVehicles) {
                tmp.GetVehicle().stop();
            }
        }
    }

    /**
    * SET TIME click 
    */
    @FXML
    private void setTimeClick() {
        new SetTimeWindow();
    }

    /**
    * SPEED INCREASE click 
    */
    @FXML
    private void speedIncrClick() {
        InternalClock.increaseAccelerationLevel();
    }

    /**
    * SPEED DECREASE click 
    */
    @FXML
    private void speedDecrClick() {
        InternalClock.decreaseAccelerationLevel();
    }

    /**
    * SPEED DEFAULT click 
    */
    @FXML
    private void speedDefaClick() {
        InternalClock.defaultAccelerationLevel();
    }

    /**
    * ZOOM IN click 
    */
    @FXML
    private void zoomInClick() {
        scroll_work_area.zoomIn();
    }

    /**
    * ZOOM DEFAULT click 
    */
    @FXML
    private void zoomDefClick() {
        scroll_work_area.zoomToDefault();
    }

    /**
    * ZOOM OUT click 
    */
    @FXML
    private void zoomOutClick() {
        scroll_work_area.zoomOut();
    }

    /**
    * ABOUT click 
    */
    @FXML
    private void aboutClick() {
        new AboutController();
    }

    /**
    * EventHandler - mouse exited from work area
    */
    EventHandler<MouseEvent> mouseExited = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            Coordinates.setText("");
        }
    };

    /**
    * EventHandler - mouse entered and move in work area
    */
    EventHandler<MouseEvent> mouseEnteredAndMove = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            cordX = (int) e.getX();
            cordY = (int) e.getY();
            Coordinates.setText("Coordinates: " + cordX + ":" + cordY);
        }
    };

    /**
    * EventHandler - mouse scroll in work area with cntrl down
    */
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
