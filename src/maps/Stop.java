package maps;

/**
 * The class Stop represents a stop on a street.
 * Each stop has unique ID, position on the map and 
 * a street on which the stop is located.
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Stop {
    private String id;
    private Coordinate coor;
    private Street street;

    /**
     * Consructs a Stop object with given parameters.
     * @param id Name of this stop.
     * @param coordinate The position of this stop.
     */ 
    public Stop(String id, Coordinate coordinate){
        this.id = id;
        this.coor = coordinate;
        this.street = null;
    }

    /**
     * Sets the street on which the stop is located.
     * @param s the street on which the stop is located.
    */
    public void setStreet(Street s){
        this.street = s;
    }

    /**
     * Returns the identifier of this stop.
     * @return The identifier of this stop.
    */
    public String getId(){
        return this.id;
    }

    /**
     * Returns the position of this stop.
     * @return The position of this stop.
    */
    public Coordinate getCoordinate(){
        return this.coor;
    }

    /**
     * Returns the street on which the stop is located.
     * @return The street on which the stop is located.
    */
    public Street getStreet(){
        return this.street;
    }

    /**
     * Compares two stops for equality. 
     * @param obj The object to compare with.
     * @return true, if they're equal, otherwise false.
    */
    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        
        Stop that = (Stop)obj;
        return that.id.equals(this.id);
    }

    /**
     * Converts this Stop object to a String. 
     * @return A string representation of this stop.
    */
    @Override
    public String toString(){
        return "stop(" + this.id + ")";
    }
}