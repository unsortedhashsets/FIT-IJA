package gui;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.transform.Scale;

public class ZoomableScrollPane extends ScrollPane {
    private Group zoomGroup;
    private Scale scaleTransform;
    private Node content;
    public double scaleValue = 1.0;
    public double delta = 0.05;

    public ZoomableScrollPane(Node content) {
        super();
        setPannable(true);
        this.content = content;
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(content);
        setContent(contentGroup);

        scaleTransform = new Scale(scaleValue, scaleValue);

        zoomGroup.getTransforms().add(scaleTransform);

    }

    public void zoomToDefault() {
        scaleTransform.setX(1.0);
        scaleTransform.setY(1.0);
    }

    public void zoomTo(double scaleValue) {

        // do the resizing
        this.scaleValue = scaleValue;
        scaleTransform.setX(scaleValue);
        scaleTransform.setY(scaleValue);

    }

    public void zoomOut() {
        scaleValue -= delta;

        if (Double.compare(scaleValue, 0.1) < 0) {
            scaleValue = 0.1;
        }
        zoomTo(scaleValue);
    }

    public void zoomIn() {

        scaleValue += delta;

        if (Double.compare(scaleValue, 10) > 0) {
            scaleValue = 10;
        }
        zoomTo(scaleValue);

    }

}