package vehicles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.AbstractMap.SimpleImmutableEntry;
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
    private boolean isStopped;

    private ArrayList<SimpleImmutableEntry<Coordinate, Object>> listOfCoors;
    private Iterator<SimpleImmutableEntry<Coordinate, Object>> iter;
    private boolean isReversed;

    private Coordinate position;
    private SimpleImmutableEntry<Coordinate, Object> departure;
    private SimpleImmutableEntry<Coordinate, Object> arrival;

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
        int length_X = arrival.getKey().diffX(departure.getKey());
        int length_Y = arrival.getKey().diffY(departure.getKey());
        float length = arrival.getKey().length(departure.getKey());

        this.velocity_X = (length != 0) ? (this.velocity * length_X) / length : 0;
        this.velocity_Y = (length != 0) ? (this.velocity * length_Y) / length : 0;

    }

    private void setMovingParameters(){
        this.listOfCoors = new ArrayList<SimpleImmutableEntry<Coordinate, Object>>(this.line.getCoordinates());
        this.iter = listOfCoors.iterator();
        this.isReversed = false;

        this.departure = iter.next();
        this.arrival = iter.next();
        this.position = this.departure.getKey();

        this.float_X = this.position.getX();
        this.float_Y = this.position.getY();

        setAxisVelocities();
    }

    public boolean actualizePosition(){
        float time = 0.04f;

        while (!(0 <= Math.abs(time) && Math.abs(time) <= 1.0E-7)){  // float accuracy
            float acceleration = InternalClock.getAccelerationLevel();
            Object delayObject = (isReversed) ? this.arrival.getValue() : this.departure.getValue();
            float delay = 1.0f;
            if (delayObject.getClass().getName().equals("maps.Street")){
                Street street = (Street) delayObject;
                delay = 1 - (street.GetdrivingDifficulties() * 0.01f);
            } else if (delayObject.getClass().getName().equals("maps.Stop")){
                Stop stop = (Stop) delayObject;
                delay = 1 - (stop.getStreet().GetdrivingDifficulties() * 0.01f);
            }

            float distance_X = delay * acceleration * velocity_X * 0.04f; // 1.0f = 1 second
            float distance_Y = delay * acceleration * velocity_Y * 0.04f; // 1.0f = 1 second

            this.float_X += (velocity_X > 0) 
                          ? Math.min(distance_X, arrival.getKey().diffX(this.position))
                          : Math.max(distance_X, arrival.getKey().diffX(this.position));
            this.float_Y += (velocity_Y > 0) 
                          ? Math.min(distance_Y, arrival.getKey().diffY(this.position))
                          : Math.max(distance_Y, arrival.getKey().diffY(this.position));

            this.position = Coordinate.create(Math.round(float_X), Math.round(float_Y));
            if (this.position.equals(this.arrival.getKey())){
                this.departure = this.arrival;
                if (!iter.hasNext()){
                    Collections.reverse(listOfCoors);
                    iter = listOfCoors.iterator();
                    iter.next();

                    this.isReversed = !this.isReversed;
                }

                this.arrival = iter.next();
                setAxisVelocities();

                Object object = this.departure.getValue();
                if (object.getClass().getName().equals("maps.Stop")){
                    return true;
                }
            }
            float length = (float) Math.sqrt(Math.pow(distance_X, 2) + Math.pow(distance_Y, 2));
            if (delay != 0){
                time -= length / (acceleration * this.velocity * delay);
            }
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

        boolean afterToTime = false;
        boolean hasReversed = this.isReversed;

        while (!isStopped){
            if (InternalClock.isTime(from, InternalClock.MINUTE)){
                while (!isStopped){
                    try{
                        Thread.sleep(40);
                    } catch (InterruptedException exc){}
                    
                    boolean isStop = actualizePosition();
                    if (isStop){
                        String stopTime = InternalClock.getStopTime();
                        int accuracy = InternalClock.SECOND; 
                        while(!InternalClock.isTime(stopTime, accuracy)){
                            try{
                                Thread.sleep(40);
                            } catch (InterruptedException exc){}
                        }
                    }

                    if (InternalClock.isTime(to, InternalClock.MINUTE)){
                        afterToTime = true;
                    }

                    if (hasReversed != this.isReversed && afterToTime){
                        afterToTime = true;
                        break;
                    }

                    hasReversed = this.isReversed;
                }
            }
        }
    }

    public void start() {
        this.isStopped = false;
        if (this.thread == null) {
            this.thread = new Thread(this, id);
            this.thread.start();
        }
    }

    public void stop() {
        this.isStopped = true;
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
