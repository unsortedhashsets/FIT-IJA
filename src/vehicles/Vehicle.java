package vehicles;

import maps.Coordinate;
import maps.Line;

public class Vehicle{
    private String id;
    private Line line;
    private String from;
    private String to;

    private float float_X;
    private float float_Y;

    private Coordinate position;
    private Coordinate departure;
    private Coordinate arrival;

    private float velocity;
    private float velocity_X;
    private float velocity_Y;

    protected Vehicle(String id, Line line, String from, String to, float velocity){
        this.id = id;
        this.line = line;

        this.from = from;
        this.to = to;

        this.velocity = (velocity * 1000)/3600; // transform from km/h to m/s
    }

    public void setVelocity(float velocity){
        this.velocity = velocity;
    }

    public void setAxisVelocities() {
        int length_X = arrival.diffX(departure);
        int length_Y = arrival.diffY(departure);
        float length = arrival.length(departure);

        this.velocity_X = (this.velocity * length_X) / length;
        this.velocity_Y = (this.velocity * length_Y) / length;
    }

    public void actualizePosition(){
        float_X += velocity_X * 1.0f; // 1.0f = time
        float_Y += velocity_Y * 1.0f; // 1.0f = time
        
        this.position = Coordinate.create((int)float_X, (int)float_Y);
    }

    public String getId(){
        return this.id;
    }

    public float getVelocity(){
        return this.velocity;
    }

    public Coordinate getPosition(){
        return this.position;
    }   

    @Override
    public String toString(){
        return "Vehicle " + this.id + ": " +
               "is running from " + this.from +
               " to " + this.to;

    }
}
