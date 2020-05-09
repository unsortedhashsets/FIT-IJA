package vehicles;

import maps.Line;

public class Autobus extends Vehicle{    
    public Autobus(String id, Line line, String from, String to){
        super(id, line, from, to, 70);
    }
}
