package vehicles;

import maps.Coordinate;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

public class Vehicle{
    private String id;
    
    private Coordinate position;
    private Coordinate departure;
    private Coordinate arrival;

    private float velocity;
    private float velocity_X;
    private float velocity_Y;

    protected Vehicle(String id, float velocity){
        this.id = id;
        this.velocity = velocity;
    }

    @Setter
    public void setId(String id){
        this.id = id;
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
        int X = this.position.getX() + (int)(velocity_X * 1.0f); // 1.0f = time
        int Y = this.position.getY() + (int)(velocity_Y * 1.0f); // 1.0f = time
        
        this.position = Coordinate.create(X, Y);
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
