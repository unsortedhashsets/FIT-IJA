package vehicles;

import maps.Line;

public class Trolley extends Vehicle {
    public Trolley(String id, Line line, String from, String to){
        super(id, line, from, to, 60);
    }
}
