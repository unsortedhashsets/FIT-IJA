package vehicles;

import java.time.LocalTime;

import maps.Line;

public class Trolley extends Vehicle {
    public Trolley(String id, Line line, LocalTime from, LocalTime to){
        super(id, line, from, to, 12);
    }
}
