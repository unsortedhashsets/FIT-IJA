package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import internal.InternalClock;

/**
 * Graphic representation of the set-time window in the scene
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class SetTimeWindow {
    private Stage userInput;
    private VBox layout;
    private Scene uiScene;
    private Button confirmButton;

    /**
    * Window constructor for time input
    */
    public SetTimeWindow() {

        this.userInput = new Stage();
        this.userInput.setTitle("Set Time");
        this.userInput.setWidth(290);
        this.userInput.setHeight(80);
        this.userInput.initModality(Modality.APPLICATION_MODAL);

        Layout();

        this.userInput.showAndWait();

    }

    /**
     * Initialization of the user input window
     */
    public void Layout() {
        this.layout = new VBox();
        this.layout.setAlignment(Pos.CENTER);
        this.confirmButton = new Button("OK");

        HBox box = new HBox();
        box.setPadding(new Insets(10, 10, 10, 10));

        Label HHName = new Label();
        HHName.setText("  HH:");
        HHName.setPrefWidth(50);
        HHName.setFont(new Font(17));
        box.getChildren().add(HHName);
        TextField HHInput = new TextField();
        HHInput.setPrefWidth(40);
        HHInput.setId("HH");
        HHInput.setText("09");
        box.getChildren().add(HHInput);

        Label MMName = new Label();
        MMName.setText("  MM:");
        MMName.setPrefWidth(50);
        MMName.setFont(new Font(17));
        box.getChildren().add(MMName);
        TextField MMInput = new TextField();
        MMInput.setPrefWidth(40);
        MMInput.setId("MM");
        MMInput.setText("55");
        box.getChildren().add(MMInput);

        Label SSName = new Label();
        SSName.setText("  SS:");
        SSName.setPrefWidth(50);
        SSName.setFont(new Font(17));
        box.getChildren().add(SSName);
        TextField SSInput = new TextField();
        SSInput.setPrefWidth(40);
        SSInput.setId("SS");
        SSInput.setText("00");
        box.getChildren().add(SSInput);

        this.layout.getChildren().add(box);
        confirmButton.setPrefWidth(50);
        confirmButton.setId("ConfirmButton");
        this.layout.getChildren().add(confirmButton);
        this.uiScene = new Scene(this.layout);
        this.userInput.setScene(this.uiScene);

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                setTimeValues(HHInput, MMInput, SSInput);
            }
        });

        HHInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    setTimeValues(HHInput, MMInput, SSInput);
                }
            }
        });

        MMInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    setTimeValues(HHInput, MMInput, SSInput);
                }
            }
        });

        SSInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    setTimeValues(HHInput, MMInput, SSInput);
                }
            }
        });
    }

    /**
     * Setting of the given values
     * @param HHInput HH value from tbox
     * @param MMInput MM value from tbox
     * @param SSInput SS value from tbox
     */
    public void setTimeValues(TextField HHInput, TextField MMInput, TextField SSInput) {
       
        String s = HHInput.getText().trim() + ":" + MMInput.getText().trim() + ":" + SSInput.getText().trim();
        if (s.matches("(?:[01]\\d|2[0-3]):(?:[0-5]\\d):(?:[0-5]\\d)")) {
            InternalClock.setTime(s);
            System.out.println("New time is: " + s);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Please, enter the HH value from 0 to 100, MM and SS values from 0 to 59");
            alert.setTitle("Wrong input");
            alert.show();
        }
        this.userInput.close();
    }
}