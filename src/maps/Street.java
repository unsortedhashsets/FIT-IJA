package maps;

import java.util.List;
import java.util.ArrayList;

public class Street {
    private String id;
    private List<Stop> stops;
    private List<Coordinate> coordinates;
    private int drivingDifficulties = 0;
    private Boolean status = true;

    private Street(String id, Coordinate... coordinates) {
        this.id = id;
        this.stops = new ArrayList<Stop>();
        this.coordinates = new ArrayList<Coordinate>();

        for (Coordinate coor : coordinates) {
            this.coordinates.add(coor);
        }
    }

    public static Street create(String id, Coordinate... coordinates) {
        return new Street(id, coordinates);
    }

    public String getId() {
        return this.id;
    }

    public List<Coordinate> getCoordinates() {
        return this.coordinates;
    }

    public List<Stop> getStops() {
        return this.stops;
    }

    public boolean addStop(Stop stop) {
        for (int i = this.coordinates.size() - 1; i > 0; i--) {
            if (isInStreet(this.coordinates.get(i), stop.getCoordinate(), this.coordinates.get(i-1))) {
                this.stops.add(stop);
                stop.setStreet(this);
                return true;
            }
        }

        return false;
    }

    private boolean isInStreet(Coordinate previous, Coordinate stop, Coordinate next){
        int diff_X = stop.diffX(previous);
        int diff_Y = stop.diffY(previous);

        int length_X = next.diffX(previous);
        int length_Y = next.diffY(previous);

        int cross = (diff_X * length_Y) - (diff_Y * length_X);

        if (cross != 0){
            return false;
        }
        if (Math.abs(length_X) >= Math.abs(length_Y))
            return length_X > 0 ? 
                previous.getX() <= stop.getX() && stop.getX() <= next.getX() :
                next.getX() <= stop.getX() && stop.getX() <= previous.getX();
        else
            return length_Y > 0 ? 
                previous.getY() <= stop.getY() && stop.getY() <= next.getY() :
                next.getY() <= stop.getY() && stop.getY() <= previous.getY();
    }

    public Coordinate begin() {
        return coordinates.get(0);
    }

    public Coordinate end() {
        return coordinates.get(coordinates.size() - 1);
    }

    public boolean follows(Street s) {
        return this.begin().equals(s.end()) || this.end().equals(s.begin()) || this.begin().equals(s.begin())
                || this.end().equals(s.end());
    }

    public int GetdrivingDifficulties() {
        return this.drivingDifficulties;
    }

    public void SetdrivingDifficulties(int DD) {
        this.drivingDifficulties = DD;
    }

    public void SetStatus(boolean status) {
        this.status = status;
    }

    public boolean GetStatus() {
        return this.status;
    }

    public String GetStatusString() {
        if (this.status) {
            return "Open";
        } else {
            return "Closed";
        }
    }
}