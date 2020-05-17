package maps;

import java.util.List;
import java.util.ArrayList;

/**
 * The class Street represents a street on the map.
 * Each street has unique ID, 
 * list of coordinates which this street is defined,
 * list of stops on this street,
 * driving difficulty on this street
 * and status of this street (open || closed).
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Street {
    private String id;
    private List<Stop> stops;
    private List<Coordinate> coordinates;
    private int drivingDifficulties = 0;
    private Boolean status = true;

    /**
     * Consructs a Street object with given parameters.
     * @param id Name of this street.
     * @param coordinates The list of coordinates which this street is defined.
     */ 
    public Street(String id, Coordinate... coordinates) {
        this.id = id;
        this.stops = new ArrayList<Stop>();
        this.coordinates = new ArrayList<Coordinate>();

        for (Coordinate coor : coordinates) {
            this.coordinates.add(coor);
        }
    }

    /**
    * Controls if between two coordinates exists stop.
    * @param previous Previous coordinate.
    * @param stop Coordinate of the given stop.
    * @param next Next coordinate.
    * @return true, if the stop is in street, otherwise false.
    */
    public static boolean isInStreet(Coordinate stop, Coordinate previous, Coordinate next){
        int diff_X = stop.diffX(previous);
        int diff_Y = stop.diffY(previous);

        int length_X = next.diffX(previous);
        int length_Y = next.diffY(previous);

        int cross = (diff_X * length_Y) - (diff_Y * length_X);

        if (cross != 0){
            return false;
        }
        if (Math.abs(length_X) >= Math.abs(length_Y))
            return length_X > 0 ? 
                previous.getX() <= stop.getX() && stop.getX() <= next.getX() :
                next.getX() <= stop.getX() && stop.getX() <= previous.getX();
        else
            return length_Y > 0 ? 
                previous.getY() <= stop.getY() && stop.getY() <= next.getY() :
                next.getY() <= stop.getY() && stop.getY() <= previous.getY();
    }

    /**
     * Adds a given stop to this street.
     * @param stop The given stop.
     * @return true, if the stop was added on this street, otherwise false
    */
    public boolean addStop(Stop stop) {
        for (int i = this.coordinates.size() - 1; i > 0; i--) {
            if (isInStreet(stop.getCoordinate(), this.coordinates.get(i), this.coordinates.get(i-1))) {
                this.stops.add(stop);
                stop.setStreet(this);
                return true;
            }
        }

        return false;
    }

    /**
    * Sets the driving difficulty of this street.
    * @param DD The driving difficulty of this street.
    */
    public void setDrivingDifficulties(int DD) {
        this.drivingDifficulties = DD;
    }

    /**
    * Sets the status of this street.
    * @param status The status of this street.
    */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Returns the identifier of this street.
     * @return The identifier of this street.
    */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the list of stops of this street.
     * @return The list of stops of this street.
    */
    public List<Stop> getStops() {
        return this.stops;
    }

    /**
     * Returns the list of coordinates of this street.
     * @return The list of coordinates of this street.
    */
    public List<Coordinate> getCoordinates() {
        return this.coordinates;
    }

    /**
    * Returns the begin of this street.
    * @return The begin of this street.
    */
    public Coordinate begin() {
        return coordinates.get(0);
    }

    /**
    * Returns the end of this street.
    * @return The end of this street.
    */
    public Coordinate end() {
        return coordinates.get(coordinates.size() - 1);
    }

    /**
    * Checks if a given street follows this street.
    * @param s The given street.
    * @return true, if it follows, otherwise false.
    */
    public boolean follows(Street s) {
        return this.begin().equals(s.begin()) ||
               this.begin().equals(s.end()) || 
               this.end().equals(s.begin()) || 
               this.end().equals(s.end());
    }

    /**
    * Returns the driving difficulty of this street.
    * @return The driving difficulty of this street.
    */
    public int getDrivingDifficulties() {
        return this.drivingDifficulties;
    }

    /**
    * Returns the status of this street.
    * @return true, is the street is opened, otherwise false.
    */
    public boolean getStatus() {
        return this.status;
    }

    /**
    * Returns the status of this street as a String.
    * @return String representation of the street status.
    */
    public String getStatusString() {
        return (this.status) ? "Open" : "Closed";
    }
}