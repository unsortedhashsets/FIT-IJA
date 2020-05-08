package gui;

import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import maps.Coordinate;
import maps.Stop;

public class ViewStop extends Circle {
    private String id;
    private Coordinate coor;
    private static final DropShadow highlight = new DropShadow(20, Color.GOLDENROD);

    public ViewStop(Stop stop) {
        this.id = stop.getId();
        this.coor = stop.getCoordinate();

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

        drawStop();
    }

    private void drawStop() {
        setCenterX(coor.getX());
        setCenterY(coor.getY());
        setRadius(6);
        setFill(Color.WHITE);
        setStroke(Color.GREY);
        setStrokeWidth(2);
    }

}