package gui;

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

import java.util.Set;

/**
 * Okno pro nastavovani hodnot atributu portu
 *
 */
public class UserInputWindow {
    private Stage userInput;
    private VBox layout;
    private Scene uiScene;
    private Button confirmButton;

    private Street street;

    /**
     * Konstruktor okna pro uživatelský vstup na port
     * 
     * @param Street Street, na který se zadávají hodnoty
     */
    public UserInputWindow(Street Street) {
        this.street = Street;

        this.userInput = new Stage();

        this.userInput.setTitle("User Input");
        this.userInput.setWidth(220);
        this.userInput.setHeight(80);
        this.userInput.initModality(Modality.APPLICATION_MODAL);

        initLayout();

        this.userInput.showAndWait();

    }

    /**
     * Inicializace okna pro uživatelský vstup
     */
    public void initLayout() {
        this.layout = new VBox();
        this.layout.setAlignment(Pos.CENTER);
        this.confirmButton = new Button("OK");

        HBox box = new HBox();
        box.setPadding(new Insets(10, 10, 10, 10));
        Label valueName = new Label();
        valueName.setText("Traffic level:");
        valueName.setPrefWidth(160);
        valueName.setFont(new Font(17));
        box.getChildren().add(valueName);
        TextField valueInput = new TextField();
        valueInput.setPrefWidth(60);
        valueInput.setId("Traffic level");
        valueInput.setText(Integer.toString(street.GetdrivingDifficulties()));
        box.getChildren().add(valueInput);
        /** Nastavení eventu - po stisknutí ENTERU se nataví hodnoty **/
        valueInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    setPortValues(valueInput);
                }
            }
        });

        this.layout.getChildren().add(box);
        confirmButton.setPrefWidth(50);
        confirmButton.setId("ConfirmButton");
        this.layout.getChildren().add(confirmButton);
        this.uiScene = new Scene(this.layout);
        this.userInput.setScene(this.uiScene);
    }

    /**
     * Nastavení za daných hodnot na porty
     * 
     * @param valueInput value from tbox
     */
    public void setPortValues(TextField valueInput) {
        int i = Integer.parseInt(valueInput.getText().trim());
        if (i >= 0 && i <= 100){
            street.SetdrivingDifficulties(i);
            System.out.println("New traffic level for street: " + street.getId() + " is - " + i);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Wrong input");
            alert.setTitle("Please, enter the integer value from 0 to 100");
            alert.show();
        }
        this.userInput.close();
    }
}