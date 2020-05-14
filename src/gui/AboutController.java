package gui;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class AboutController {
    private Stage about;

    public AboutController() {

        this.about = new Stage();
        this.about.setTitle("");
        this.about.setWidth(400);
        this.about.setHeight(400);
        this.about.initModality(Modality.APPLICATION_MODAL);

        layout();
        
        this.about.showAndWait();

    }

    private void layout() {
        
    }
}