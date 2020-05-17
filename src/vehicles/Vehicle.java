package vehicles;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Iterator;

import internal.InternalClock;
import maps.Coordinate;
import maps.Line;
import maps.Stop;
import maps.Street;

/**
 * The class Vehicle represents a vehicle on the map.
 * An instance of this class has unique ID, 
 * position on the map,
 * line, where this vehicle is driving,
 * a period of time, when this vehicle is driving,
 * list of key position in the line, 
 * and vehicle's velocity. 
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Vehicle implements Runnable{
    private String id;
    private Line line;
    private LocalTime from;
    private LocalTime to;

    private Thread thread;
    private boolean isStopped;

    private ArrayList<SimpleImmutableEntry<Coordinate, Object>> listOfCoors;
    private Iterator<SimpleImmutableEntry<Coordinate, Object>> iter;
    private boolean isReversed;

    private Coordinate position;
    private SimpleImmutableEntry<Coordinate, Object> departure;
    private SimpleImmutableEntry<Coordinate, Object> arrival;

    private double velocity;
    private double velocity_X;
    private double velocity_Y;

    private double double_X;
    private double double_Y;

    /**
     * Consructs a Vehicle object with given parameters.
     * @param id ID of this vehicle.
     * @param line A line, where this vehicle is driving.
     * @param from From what time this vehicle is driving.
     * @param to Till what time this vehicle is driving.
     * @param velocity The velocity of this vehicle.
     */ 
    protected Vehicle(String id, Line line, LocalTime from, LocalTime to, double velocity){
        this.id = id;
        this.line = line;
        this.from = from;
        this.to = to;
        this.velocity = velocity;
    }

    private void setAxisVelocities() {
        int length_X = arrival.getKey().diffX(departure.getKey());
        int length_Y = arrival.getKey().diffY(departure.getKey());
        double length = arrival.getKey().length(departure.getKey());

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

        this.double_X = this.position.getX();
        this.double_Y = this.position.getY();

        setAxisVelocities();
    }

    private double getDelay(Object delayObject){
        double delay = 1.0;
        
        if (delayObject.getClass().getName().equals("maps.Street")){
            Street street = (Street) delayObject;
            delay = 1 - (street.getDrivingDifficulties() * 0.01);
        } else if (delayObject.getClass().getName().equals("maps.Stop")){
            Stop stop = (Stop) delayObject;
            delay = 1 - (stop.getStreet().getDrivingDifficulties() * 0.01);
        }

        return delay;
    }

    /**
     * Actualizes the position of this vehicle every 1 ms. 
     * @return true, if this vehicle is in a stop, otherwise false
    */
    public boolean actualizePosition(){
        double time = 0.001;

        while (!(0 <= Math.abs(time) && Math.abs(time) <= 1.0E-7)){  // double accuracy
            double acceleration = InternalClock.getAccelerationLevel();

            Object delayObject = (isReversed) ? this.arrival.getValue() : this.departure.getValue();
            double delay = getDelay(delayObject);

            double distance_X = delay * acceleration * velocity_X * 0.001; // 1.0 = 1 second
            double distance_Y = delay * acceleration * velocity_Y * 0.001; // 1.0 = 1 second

            this.double_X += (velocity_X > 0) 
                          ? Math.min(distance_X, arrival.getKey().diffX(this.position))
                          : Math.max(distance_X, arrival.getKey().diffX(this.position));
            this.double_Y += (velocity_Y > 0) 
                          ? Math.min(distance_Y, arrival.getKey().diffY(this.position))
                          : Math.max(distance_Y, arrival.getKey().diffY(this.position));

            this.position = Coordinate.create((int)Math.round(double_X), (int)Math.round(double_Y));
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

            double length = Math.sqrt(Math.pow(distance_X, 2) + Math.pow(distance_Y, 2));
            if (delay != 0) {
                time -= length / (acceleration * this.velocity * delay);
            }
        } 

        return false;
    }

    /**
     * Returns the identifier of this vehicle.
     * @return The identifier of this vehicle.
    */
    public String getId(){
        return this.id;
    }
    
    /**
     * Returns the line, where this vehicle is driving.
     * @return The line, where this vehicle is driving.
    */
    public Line getLine() {
        return this.line;
    }

    /**
     * Returns the position of this vehicle.
     * @return The position of this vehicle.
    */
    public Coordinate getPosition(){
        return this.position;
    }   

    /**
     * Creates the schedule for this vehicle and return that.
     * @return The current schedule of this vehicle.
    */
    public ArrayList<SimpleImmutableEntry<Stop, Integer>> getSchedule(){
        ArrayList<SimpleImmutableEntry<Stop, Integer>> schedule = new ArrayList<>();
        
        Iterator<SimpleImmutableEntry<Coordinate, Object>> iterator = listOfCoors.iterator();
        SimpleImmutableEntry<Coordinate, Object> nextPair = iterator.next();
        
        boolean inCurrentLine = false;

        Coordinate previous;
        Coordinate next = this.position;
        double time = 0;

        while (iterator.hasNext()){
            nextPair = iterator.next();

            if (nextPair.equals(this.arrival)){
                inCurrentLine = true;
            }

            if (inCurrentLine){
                previous = next;
                next = nextPair.getKey();

                Object object = nextPair.getValue();
                double delay = getDelay(object);
                time += next.length(previous) / (delay * velocity);
                
                if (object.getClass().getName().equals("maps.Stop")){
                    schedule.add(new SimpleImmutableEntry<>((Stop) object, (int) time));
                    time += 10;
                } 
            }
        }

        return schedule;
    }

    /**
     * This method involves continuous driving of this vehicle.
     * Also this method handles the behavior of this vehicle at stops
     * or if this vehicle is driving on a certain period of the time.
    */
    @Override
    public void run() {
        setMovingParameters();
        boolean hasReversed = !this.isReversed;

        while (!isStopped){
            try{
                Thread.sleep(1);
            } catch (InterruptedException exc){}

            while ((InternalClock.afterTime(from) && InternalClock.beforeTime(to)) || (hasReversed == this.isReversed)){
                hasReversed = this.isReversed;

                try{
                    Thread.sleep(1);
                } catch (InterruptedException exc){}
                
                boolean isStop = actualizePosition();
                if (isStop){
                    LocalTime stopTime = InternalClock.getStopTime();
                    while(!InternalClock.afterTime(stopTime)){
                        try{
                            Thread.sleep(1);
                        } catch (InterruptedException exc){}
                    }
                }
                
                if (isStopped){
                    break;
                }
            }
        }
    }

    /**
     * Creates a new thread for this instance and start it.
    */
    public void start() {
        this.isStopped = false;
        if (this.thread == null) {
            this.thread = new Thread(this, id);
            this.thread.start();
        }
    }

    /**
     * Stops created thread of this vehicle.
    */
    public void stop() {
        this.isStopped = true;
        if (this.thread != null) {
            this.thread.interrupt();
        }
    }

    /**
     * Converts this Vehicle object to a String. 
     * @return A string representation of this vehicle.
    */
    @Override
    public String toString(){
        return "\nis running from : " + this.from +
               "\n                     to : " + this.to;

    }
}
