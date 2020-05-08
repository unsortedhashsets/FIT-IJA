package gui;

import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.stream.Collectors;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import maps.Coordinate;
import maps.Line;
import maps.Street;
import maps.Stop;

public class ViewLine extends Polyline {
    Line line;

    public ViewLine(Line line) {
        this.line = line;

        Tooltip tooltip = new Tooltip();
        tooltip.setText("\n"+this.line.getID()+"\n ");
        Tooltip.install(this, tooltip);

        
        drawLine();
    }

    private void drawLine() {
        List<SimpleImmutableEntry<Street, Stop>> tmpRoute = this.line.getRoute();

        System.out.println("------------");
        String res = tmpRoute.stream().map(entry -> entry.getKey().getId() + ":" + entry.getValue() + ";")
        .collect(Collectors.joining());
        System.out.println(res);

    }

}