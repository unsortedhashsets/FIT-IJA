package vehicles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Iterator;

import internal.InternalClock;
import maps.Coordinate;
import maps.Line;
import maps.Stop;
import maps.Street;

public class Vehicle implements Runnable{
    private String id;
    private Line line;
    private String from;
    private String to;

    private Thread thread;

    private LinkedHashMap<Coordinate, Object> coordinates;
    private ArrayList<Coordinate> listOfCoors;
    private Iterator<Coordinate> iter;

    private Coordinate position;
    private Coordinate departure;
    private Coordinate arrival;

    private float velocity;
    private float velocity_X;
    private float velocity_Y;

    private float float_X;
    private float float_Y;

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

    private void setAxisVelocities() {
        int length_X = arrival.diffX(departure);
        int length_Y = arrival.diffY(departure);
        float length = arrival.length(departure);

        this.velocity_X = (this.velocity * length_X) / length;
        this.velocity_Y = (this.velocity * length_Y) / length;
    }

    private void setMovingParameters(){
        this.coordinates = this.line.getCoordinates();
        this.listOfCoors = new ArrayList<Coordinate>(coordinates.keySet());
        this.iter = listOfCoors.iterator();

        this.position = this.departure = (Coordinate) iter.next();
        this.arrival = (Coordinate) iter.next();

        this.float_X = this.position.getX();
        this.float_Y = this.position.getY();

        setAxisVelocities();
    }

    public boolean actualizePosition(){
        float acceleration = InternalClock.getAccelerationLevel();

        float distance = acceleration * velocity * 0.01f;

        while (!(0 <= distance && distance <= 0.0001)){
            float distance_X = acceleration * velocity_X * 0.01f; // 1.0f = 1 second
            float distance_Y = acceleration * velocity_Y * 0.01f; // 1.0f = 1 second

            
            this.float_X += (velocity_X > 0) 
                          ? Math.min(distance_X, this.arrival.diffX(this.position))
                          : Math.max(distance_X, this.arrival.diffX(this.position));
            this.float_Y += (velocity_Y > 0) 
                          ? Math.min(distance_Y, this.arrival.diffY(this.position))
                          : Math.max(distance_Y, this.arrival.diffY(this.position));

            this.position = Coordinate.create((int)float_X, (int)float_Y);
            if (this.position.equals(this.arrival)){
                this.departure = this.arrival;
                if (!iter.hasNext()){
                    Collections.reverse(listOfCoors);
                    iter = listOfCoors.iterator();
                    iter.next();
                }
                this.arrival = (Coordinate) iter.next();
                setAxisVelocities();

                Object object = coordinates.get(this.departure);
                if (object.getClass().getName().equals("maps.Stop")){
                    return true;
                }
            }

            float length = (float) Math.sqrt((distance_X * distance_X) + (distance_Y * distance_Y));
            distance -= length;
        } 

        return false;
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
    public void run() {
        setMovingParameters();

        while (true){
            if (InternalClock.isTime(from)){
                while (!InternalClock.isTime(to)){
                    try{
                        Thread.sleep(10);
                    } catch (InterruptedException exc){}
                    
                    System.out.println("position: " + this.position);
                    boolean isStop = actualizePosition();
                    if (isStop){
                        String stopTime = InternalClock.getStopTime().substring(0,5);
                        while(!InternalClock.isTime(stopTime)){
                            try{
                                Thread.sleep(10);
                            } catch (InterruptedException exc){}
                        }
                    }
                }
            }
        }
    }

    public void start() {
        if (this.thread == null) {
            this.thread = new Thread(this, id);
            this.thread.start();
        }
    }

    public void stop() {
        if (this.thread != null) {
            this.thread.interrupt();
        }
    }

    public Line getLine() {
        return this.line;
    }

    @Override
    public String toString(){
        return "Vehicle " + this.id + ": " +
               "is running from " + this.from +
               " to " + this.to;

    }
}
