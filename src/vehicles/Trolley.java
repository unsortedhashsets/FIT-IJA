package vehicles;

import java.time.LocalTime;

import maps.Line;

/**
 * The class Trolley represents a trolley on the map.
 * This class extends class Vehicle and has all methods 
 * that Vehicle has.
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Trolley extends Vehicle {
    /**
     * Consructs an Trolley object with given parameters.
     * @param id ID of this trolley.
     * @param line A line, where this trolley is driving.
     * @param from From what time this trolley is driving.
     * @param to Till what time this trolley is driving.
     * Default velocity of each trolley is 12 m/s. 
     */
    public Trolley(String id, Line line, LocalTime from, LocalTime to){
        super(id, line, from, to, 12);
    }
}
