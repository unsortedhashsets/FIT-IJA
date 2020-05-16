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
import maps.Street;

/**
 * Graphic representation of the set-traffic situation window in the scene
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class SetTrafficWindow {
    private Stage userInput;
    private VBox layout;
    private Scene uiScene;
    private Button confirmButton;

    private Street street;

    /**
    * Window constructor for traffic input
    * @param Street Street to set traffic situation
    */
    public SetTrafficWindow(Street Street) {
        this.street = Street;

        this.userInput = new Stage();

        this.userInput.setTitle("Set Traffic Situation");
        this.userInput.setWidth(250);
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
        Label valueName = new Label();
        valueName.setText("Traffic level (0-100):");
        valueName.setPrefWidth(180);
        valueName.setFont(new Font(17));
        box.getChildren().add(valueName);
        TextField valueInput = new TextField();
        valueInput.setPrefWidth(60);
        valueInput.setId("Traffic level");
        valueInput.setText(Integer.toString(street.getDrivingDifficulties()));
        box.getChildren().add(valueInput);

        this.layout.getChildren().add(box);
        confirmButton.setPrefWidth(50);
        confirmButton.setId("ConfirmButton");
        this.layout.getChildren().add(confirmButton);
        this.uiScene = new Scene(this.layout);
        this.userInput.setScene(this.uiScene);

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                setTrafficSituation(valueInput);
            }
        });
        valueInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    setTrafficSituation(valueInput);
                }
            }
        });
    }

    /**
     * Setting of the given values on street
     * @param valueInput value from tbox
     */
    public void setTrafficSituation(TextField valueInput) {
        int i = Integer.parseInt(valueInput.getText().trim());
        if (i >= 0 && i <= 100){
            street.setDrivingDifficulties(i);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Please, enter the integer value from 0 to 100");
            alert.setTitle("Wrong input");
            alert.show();
        }
        this.userInput.close();
    }
}