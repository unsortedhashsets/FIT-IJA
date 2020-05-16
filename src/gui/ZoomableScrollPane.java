package gui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.transform.Scale;

/**
 * Extension of the scrollPane with the zoom ability
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class ZoomableScrollPane extends ScrollPane {
    private Group zoomGroup;
    private Scale scaleTransform;
    public double scaleValue = 1.0;
    public double delta = 0.05;

    /**
    * Constructor of the Zoomable Scroll Pane (ZSP)
    * @param content Content node of the zoomable scroll pane
    */
    public ZoomableScrollPane(Node content) {
        super();
        setPannable(true);
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(content);
        setContent(contentGroup);
        scaleTransform = new Scale(scaleValue, scaleValue);
        zoomGroup.getTransforms().add(scaleTransform);

    }

    /**
    * Set Zoom coefficient to default size of the content in the ZSP
    */
    public void zoomToDefault() {
        zoomTo(1.0);
    }

    /**
    * Set Zoom out coefficient of the content in the ZSP
    */
    public void zoomOut() {
        scaleValue -= delta;
        if (Double.compare(scaleValue, 0.1) < 0) {
            scaleValue = 0.1;
        }
        zoomTo(scaleValue);
    }

    /**
    * Set Zoom in coefficient of the content in the ZSP
    */
    public void zoomIn() {
        scaleValue += delta;
        if (Double.compare(scaleValue, 10) > 0) {
            scaleValue = 10;
        }
        zoomTo(scaleValue);
    }

    /**
    * Zoom in/out with provided coefficient from zoomin(), zoomOut(), zoomToDefault() 
    * @param scaleValue coefficient coefficient of the scale transformation
    */
    public void zoomTo(double scaleValue) {
        this.scaleValue = scaleValue;
        scaleTransform.setX(scaleValue);
        scaleTransform.setY(scaleValue);
    }
}