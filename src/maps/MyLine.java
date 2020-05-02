package maps;

import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

public class MyLine implements Line{
    private String id;
    private List<Street> streets;
    private List<Stop> stops;

    public MyLine(String id){
        this.id = id;
        this.streets = new ArrayList<Street>();
        this.stops = new ArrayList<Stop>();
    }

    @Override
    public boolean addStop(Stop stop) {
        if (this.stops.isEmpty()){
            this.stops.add(stop);
            this.streets.add(stop.getStreet());
            return true;
        }
        else{
            Street last_street = this.streets.get(streets.size() - 1);

            if(last_street.follows(stop.getStreet())){
                this.stops.add(stop);
                this.streets.add(stop.getStreet());
                return true;
            }
            else{
                return false;
            }
        }
        
    }

    @Override
    public boolean addStreet(Street street) {
        if (this.stops.isEmpty() || !street.getStops().isEmpty()){
            return false;
        }
        else if (!this.streets.get(streets.size() - 1).follows(street)){
            return false;
        }
        else{
            this.streets.add(street);
            return true;
        }
    }
 
    @Override
    public List<SimpleImmutableEntry<Street, Stop>> getRoute() {
        List<SimpleImmutableEntry<Street, Stop>> route = new ArrayList<SimpleImmutableEntry<Street, Stop>>();
        
        for (Street street : streets){
            boolean notInList = true;
            for (Stop stop : street.getStops()){
                if (this.stops.contains(stop)){
                    route.add(new SimpleImmutableEntry<>(street, stop));
                    notInList = false;
                }
            }
            
            if (notInList){
                route.add(new SimpleImmutableEntry<>(street, null));
            }
        }

        return route;
    }
}