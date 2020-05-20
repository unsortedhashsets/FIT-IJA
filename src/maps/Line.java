package maps;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleImmutableEntry;

import vehicles.Autobus;
import vehicles.Tram;
import vehicles.Trolley;
import vehicles.Vehicle;

/**
 * The class Line represents a one line of public transport.
 * Each line has unique ID, color, type of vehicles, 
 * list of stops and route.
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Line {
    private String id;
    private String color;

    private String type;
    private ArrayList<Vehicle> lineVehicles;
    private int counterID;

    public ArrayList<SimpleImmutableEntry<Street, Stop>> route;
    public ArrayList<SimpleImmutableEntry<Coordinate, Object>> coordinates;

    public ArrayList<SimpleImmutableEntry<Street, Stop>> OLD_route = null;
    public ArrayList<SimpleImmutableEntry<Coordinate, Object>> OLD_coordinates = null;

    /**
     * Consructs a Line object with given parameters.
     * @param id Name of this line.
     * @param color This line will be represented on the map by this color.
     * @param type Type of vehicles on this line.
     */ 
    public Line(String id, String color, String type) {
        this.id = id;
        this.color = color;

        this.type = type;
        this.counterID = 0;

        this.lineVehicles = new ArrayList<Vehicle>();
        this.route = new ArrayList<SimpleImmutableEntry<Street, Stop>>();
        this.coordinates = new ArrayList<SimpleImmutableEntry<Coordinate, Object>>();
    }

    /**
     * This method inserts a given stop in this line.
     * The insertion order determines the order of stops, the first inserted is the first stop of the line. 
     * The stops are located on the street, if the street of the newly inserted stop does not follow the last inserted one,
     * it cannot be inserted. The first (default) stop can always be inserted.
     * Simultaneously with the stop, the street where the line goes is also inserted.
     * @param stop Inserted stop.
     * @return true, if the stop was successfully added, otherwise false.
     */ 
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

    /**
     * Inserts a street without a stop into the line. The insertion order determines the passage order.
     * If the inserted stop does not follow the last inserted one, it cannot be inserted.
     * A street without a stop cannot be inserted first (the first is always the default stop).
     * @param street Inserted street without stops.
     * @return true, if the street was successfully added, otherwise false.  
     */ 
    public boolean addStreet(Street street) {
        if (this.route.isEmpty() || !street.follows(this.route.get(this.route.size() - 1).getKey())) {
            return false;
        }
        this.route.add(new SimpleImmutableEntry<>(street, null));
        return true;
    }

    /**
     * Defines a pair Coordinate-(Street || Stop), 
     * where coordinate is key position on the line for vehicles
     * and object is type of key position (street end, corner or stop).
     * @param coordinate Given position.
     * @param object Type of key position (Street or Stop).
     */ 
    public void addCoordinate(Coordinate coordinate, Object object){
        this.coordinates.add(new SimpleImmutableEntry<>(coordinate, object));
    }

    /**
     * Returns the route of the line (list of streets through which the line passes and its stops). 
     * Each pass is always a pair saved using AbstractMap.SimpleImmutableEntry. 
     * If the line is just passing through the street, the value in the respective pair is null.
     * @return Line route. Returns a defensive copy of the route or an unmodifiable list.
     * Modifications to the returned list have no effect on internal line information, or are not possible.
     */ 
    public java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street, Stop>> getRoute() {
        return new ArrayList<>(this.route);
    }

    /**
     * Creates a new vehicle with a unique ID according to the line type of vehicles.
     * Each vehicle has a period of time, when it's driving.
     * @param from From what time the vehicle is driving.
     * @param to Till what time vehicle is driving.
     * @return Vehicle's created instance.
     */ 
    public Vehicle createVehicle(LocalTime from, LocalTime to) {
        String vehicleID = "(" + this.id + ")_" + (++counterID);
        Vehicle vehicle;

        switch (this.type) {
            case "tram":
                vehicle = new Tram(vehicleID, this, from, to);
                break;
            case "trolley":
                vehicle = new Trolley(vehicleID, this, from, to);
                break;
            case "autobus":
                vehicle = new Autobus(vehicleID, this, from, to);
                break;
            default:
                vehicle = null;
                break;
        }

        this.lineVehicles.add(vehicle);
        return vehicle;
    }

    /**
     * Returns the identifier of this line.
     * @return The identifier of this line.
     */ 
    public String getID() {
        return this.id;
    }

    /**
     * Returns color of this line.
     * @return Color of this line.
     */ 
    public String getColor() {
        return this.color;
    }

    /**
     * Returns list of keys position on this line.
     * @return List of keys position on this line.
     */ 
    public ArrayList<SimpleImmutableEntry<Coordinate, Object>> getCoordinates(){
        return this.coordinates;
    }

    /**
     * Returns list of vehicles on this line.
     * @return List of vehicles on this line.
     */ 
    public ArrayList<Vehicle> getVehicles(){
        return this.lineVehicles;
    }
}