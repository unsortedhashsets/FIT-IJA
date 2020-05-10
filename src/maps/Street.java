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
        int length = coordinates.length;

        if (length > 2) {
            for (int i = 1; i < length - 1; i++) {
                int vector1_X = coordinates[i].diffX(coordinates[i - 1]);
                int vector1_Y = coordinates[i].diffY(coordinates[i - 1]);

                int vector2_X = coordinates[i].diffX(coordinates[i + 1]);
                int vector2_Y = coordinates[i].diffY(coordinates[i + 1]);

                int scalar = vector1_X * vector2_X + vector1_Y * vector2_Y;

                if (scalar != 0) {
                    return null;
                }
            }
        }

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
        int dot_stop_X = stop.getCoordinate().getX();
        int dot_stop_Y = stop.getCoordinate().getY();

        for (int i = 0; i < this.coordinates.size() - 1; i++) {
            int dot1_X = coordinates.get(i).getX();
            int dot1_Y = coordinates.get(i).getY();

            int dot2_X = coordinates.get(i + 1).getX();
            int dot2_Y = coordinates.get(i + 1).getY();

            float x = (dot_stop_X - dot1_X) * (dot2_Y - dot1_Y);
            float y = (dot_stop_Y - dot1_Y) * (dot2_X - dot1_X);

            if (x == y && (dot1_X <= dot_stop_X && dot_stop_X <= dot2_X && dot1_Y <= dot_stop_Y && dot_stop_Y <= dot2_Y)
                    || (dot2_X <= dot_stop_X && dot_stop_X <= dot1_X && dot2_Y <= dot_stop_Y && dot_stop_Y <= dot1_Y)) {
                this.stops.add(stop);
                stop.setStreet(this);
                return true;
            }
        }

        return false;
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