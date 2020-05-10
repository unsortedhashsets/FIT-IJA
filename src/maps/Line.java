package maps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.AbstractMap.SimpleImmutableEntry;

import vehicles.Autobus;
import vehicles.Tram;
import vehicles.Trolley;
import vehicles.Vehicle;

public class Line {
    private String id;
    private String color;

    private String type;
    private int counterID;

    private ArrayList<SimpleImmutableEntry<Street, Stop>> route;
    private LinkedHashMap<Coordinate, Object> coordinates;

    public Line(String id, String color, String type) {
        this.id = id;
        this.color = color;

        this.type = type;
        this.counterID = 0;

        this.route = new ArrayList<SimpleImmutableEntry<Street, Stop>>();
        this.coordinates = new LinkedHashMap<Coordinate, Object>();
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

    public void addCoordinate(Coordinate coordinate, Object object){
        this.coordinates.put(coordinate, object);
    }

    public java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street, Stop>> getRoute() {
        return new ArrayList<>(this.route);
    }

    public Vehicle createVehicle(String from, String to) {
        String vehicleID = "(" + this.id + ")_" + (++counterID);

        switch (this.type) {
            case "tram":
                return new Tram(vehicleID, this, from, to);
            case "trolley":
                return new Trolley(vehicleID, this, from, to);
            case "autobus":
                return new Autobus(vehicleID, this, from, to);
            default:
                return null;
        }
    }

    public String getColor() {
        return this.color;
    }

    public String getID() {
        return this.id;
    }

    public LinkedHashMap<Coordinate, Object> getCoordinates(){
        return this.coordinates;
    }
}