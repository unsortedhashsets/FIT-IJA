package gui;

import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Graphic representation of the about window in the scene
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class AboutController {
    private Stage about;

    /**
    * About Window constructor
    */
    public AboutController() {

        this.about = new Stage();
        this.about.setTitle("About");
        this.about.setWidth(400);
        this.about.setHeight(400);
        this.about.initModality(Modality.APPLICATION_MODAL);

        layout();
        
        this.about.showAndWait();

    }

    /**
    * Initialization of the About window
    */
    private void layout() {
        
    }
}