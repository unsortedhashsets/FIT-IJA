package maps;

import java.util.List;
import java.util.ArrayList;

public class MyStreet implements Street{
    private String id;
    private List<Stop> stops;
    private List<Coordinate> coordinates;
    
    public MyStreet(String id, Coordinate... coordinates){
        this.id = id;
        this.stops = new ArrayList<Stop>();
        this.coordinates = new ArrayList<Coordinate>();

        for(Coordinate coor : coordinates){
            this.coordinates.add(coor);
        }
    }
    
    @Override
    public String getId(){
        return this.id;
    }

    @Override
    public List<Coordinate> getCoordinates(){
        return this.coordinates;
    }

    @Override
    public List<Stop> getStops(){
        return this.stops;
    }

    @Override
    public boolean addStop(Stop stop){
        int dot_stop_X = stop.getCoordinate().getX();
        int dot_stop_Y = stop.getCoordinate().getY();

        for (int i = 0; i < this.coordinates.size() - 1; i++){
            int dot1_X = coordinates.get(i).getX();
            int dot1_Y = coordinates.get(i).getY();

            int dot2_X = coordinates.get(i+1).getX();
            int dot2_Y = coordinates.get(i+1).getY();

            float x = (dot_stop_X - dot1_X)*(dot2_Y - dot1_Y);
            float y = (dot_stop_Y - dot1_Y)*(dot2_X - dot1_X);

            if (x == y && (dot1_X <= dot_stop_X && dot_stop_X <= dot2_X
                       &&  dot1_Y <= dot_stop_Y && dot_stop_Y <= dot2_Y)
                       || (dot2_X <= dot_stop_X && dot_stop_X <= dot1_X
                       &&  dot2_Y <= dot_stop_Y && dot_stop_Y <= dot1_Y)
                       ){
                this.stops.add(stop);
                stop.setStreet(this);
                return true;
            }
        }

        return false;
    }

    @Override
    public Coordinate begin() {
        return coordinates.get(0);
    }

    @Override
    public Coordinate end() {
        return coordinates.get(coordinates.size() - 1);
    }

    @Override
    public boolean follows(Street s) {
        return this.begin().equals(s.end()) 
            || this.end().equals(s.begin())
            || this.begin().equals(s.begin())
            || this.end().equals(s.end());
    }
}