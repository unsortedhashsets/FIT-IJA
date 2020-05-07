package vehicles;

import maps.Coordinate;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

public class Vehicle{
    private String id;
    
    private float float_X;
    private float float_Y;

    private Coordinate position;
    private Coordinate departure;
    private Coordinate arrival;

    private float velocity;
    private float velocity_X;
    private float velocity_Y;

    protected Vehicle(String id, float velocity){
        this.id = id;
        this.velocity = (velocity * 1000)/3600; // transform from km/h to m/s
    }

    @Setter
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

    @Setter
    public void actualizePosition(){
        float_X += velocity_X * 1.0f; // 1.0f = time
        float_Y += velocity_Y * 1.0f; // 1.0f = time
        
        this.position = Coordinate.create((int)float_X, (int)float_Y);
    }

    @Getter
    public String getId(){
        return this.id;
    }

    @Getter
    public float getVelocity(){
        return this.velocity;
    }

    @Getter
    public Coordinate getPosition(){
        return this.position;
    }   
}
