package vehicles;

import java.time.LocalTime;

import maps.Line;

/**
 * The class Tram represents a tram on the map.
 * This class extends class Vehicle and has all methods 
 * that Vehicle has.
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Tram extends Vehicle{
    /**
     * Consructs a Tram object with given parameters.
     * @param id ID of this tram.
     * @param line A line, where this tram is driving.
     * @param from From what time this tram is driving.
     * @param to Till what time this tram is driving.
     * Default velocity of each tram is 10 m/s. 
     */
    public Tram(String id, Line line, LocalTime from, LocalTime to){
        super(id, line, from, to, 10);
    }
}
