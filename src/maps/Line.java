package maps;

import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

public class Line {
    private String id;
    private String color;
    private String type;

    private ArrayList<SimpleImmutableEntry<Street, Stop>> route;
    private List<String> times;
    private List<Street> streets;
    private List<Stop> stops;

    public Line(String id, String color, String type) {
        this.id = id;
        this.color = color;
        this.type = type;
        this.route = new ArrayList<SimpleImmutableEntry<Street, Stop>>();

        this.times = new ArrayList<String>();
        this.streets = new ArrayList<Street>();
        this.stops = new ArrayList<Stop>();
    }

    public boolean addStop(Stop stop) {
        if (this.route.isEmpty() && stop.getStreet() != null) {
            this.route.add(new SimpleImmutableEntry<>(stop.getStreet(), stop));
            return true;
        }
        if (!this.route.isEmpty() && stop.getStreet() != null) {
            if (stop.getStreet().follows(this.route.get(this.route.size() - 1).getKey())) {
                this.route.add(new SimpleImmutableEntry<>(stop.getStreet(), stop));
                return true;
            }
        }
        return false;
    }

    public boolean addStreet(Street street) {
        if (this.route.isEmpty() || !street.follows(this.route.get(this.route.size() - 1).getKey())) {
            return false;
        }
        this.route.add(new SimpleImmutableEntry<>(street, null));
        return true;
    }

    public java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street, Stop>> getRoute() {
        return new ArrayList<>(this.route);
    }

    public void addTime(String time) {
        this.times.add(time);
    }

    public String getColor() {
        return this.color;
    }

    public String getID() {
        return this.id;
    }
}