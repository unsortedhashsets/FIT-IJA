package vehicles;

import java.time.LocalTime;

import maps.Line;

public class Tram extends Vehicle{
    public Tram(String id, Line line, LocalTime from, LocalTime to){
        super(id, line, from, to, 10);
    }
}
