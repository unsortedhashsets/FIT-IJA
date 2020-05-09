package vehicles;

import maps.Line;

public class Tram extends Vehicle{
    public Tram(String id, Line line, String from, String to){
        super(id, line, from, to, 45);
    }
}
