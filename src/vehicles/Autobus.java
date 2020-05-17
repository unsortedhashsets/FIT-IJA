package vehicles;

import java.time.LocalTime;

import maps.Line;

/**
 * The class Autobus represents an autobus on the map.
 * This class extends class Vehicle and has all methods 
 * that Vehicle has.
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Autobus extends Vehicle{    
    /**
     * Consructs an Autobus object with given parameters.
     * @param id ID of this autobus.
     * @param line A line, where this autobus is driving.
     * @param from From what time this autobus is driving.
     * @param to Till what time this autobus is driving.
     * Default velocity of each autobus is 15 m/s. 
     */
    public Autobus(String id, Line line, LocalTime from, LocalTime to){
        super(id, line, from, to, 15);
    }
}
