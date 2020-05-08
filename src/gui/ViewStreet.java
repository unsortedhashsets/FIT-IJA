package gui;

import java.util.List;

import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import maps.Coordinate;
import maps.Street;

public class ViewStreet extends Polyline {
    private String id;
    private List<Coordinate> coordinates;
    private static final DropShadow highlight = new DropShadow(20, Color.GOLDENROD);

    public ViewStreet(Street street) {
        this.id = street.getId();
        this.coordinates = street.getCoordinates();

        Tooltip tooltip = new Tooltip();
        tooltip.setText("\n"+this.id+"\n ");
        Tooltip.install(this, tooltip);


        setOnMouseEntered(e -> {
            setEffect(highlight);
            System.out.println("Mouse entered on " + this.id);
        });

        setOnMouseExited(e -> {
            setEffect(null);
            System.out.println("Mouse exited on " + this.id);
        });

        drawStreet();
    }

    private void drawStreet() {
        for (Coordinate tmp : this.coordinates) {
            getPoints().addAll(new Double[] { (double) tmp.getX(), (double) tmp.getY() });
        }
        setStroke(Color.GREY);
        setStrokeWidth(8);
    }

}