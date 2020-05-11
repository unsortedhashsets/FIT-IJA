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

        float time = 0.04f;

        while (time != 0){
            Object delayObject = coordinates.get(this.departure);
            float delay = 1.0f;
            if (delayObject.getClass().getName().equals("maps.Street")){
                Street street = (Street) delayObject;
                delay = 1 - street.GetdrivingDifficulties() * 0.01f;
            } else if (delayObject.getClass().getName().equals("maps.Stop")){
                Stop stop = (Stop) delayObject;
                delay = 1 - stop.getStreet().GetdrivingDifficulties() * 0.01f;
            }

            float distance_X = delay * acceleration * velocity_X * 0.04f; // 1.0f = 1 second
            float distance_Y = delay * acceleration * velocity_Y * 0.04f; // 1.0f = 1 second

            this.float_X += (velocity_X > 0) 
                          ? Math.min(distance_X, this.arrival.diffX(this.position))
                          : Math.max(distance_X, this.arrival.diffX(this.position));
            this.float_Y += (velocity_Y > 0) 
                          ? Math.min(distance_Y, this.arrival.diffY(this.position))
                          : Math.max(distance_Y, this.arrival.diffY(this.position));
 
            this.position = Coordinate.create(Math.round(float_X), Math.round(float_Y));
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

            float length = (float) Math.sqrt(Math.pow(distance_X, 2) + Math.pow(distance_Y, 2));
            time -= length / (acceleration * this.velocity * delay);
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
            if (InternalClock.isTime(from, InternalClock.MINUTE)){
                while (!InternalClock.isTime(to, InternalClock.MINUTE)){
                    try{
                        Thread.sleep(40);
                    } catch (InterruptedException exc){}
                    
                    boolean isStop = actualizePosition();
                    if (isStop){
                        String stopTime = InternalClock.getStopTime();
                        while(!InternalClock.isTime(stopTime, InternalClock.SECOND)){
                            try{
                                Thread.sleep(40);
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
