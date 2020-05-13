package vehicles;

import java.time.LocalTime;

import maps.Line;

public class Autobus extends Vehicle{    
    public Autobus(String id, Line line, LocalTime from, LocalTime to){
        super(id, line, from, to, 15);
    }
}
