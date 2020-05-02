package maps;

import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

public interface Line{
    public boolean addStop(Stop stop);

    public boolean addStreet(Street street);

    public List<SimpleImmutableEntry<Street, Stop>> getRoute();

    public static Line defaultLine(String id){
        return new MyLine(id);
    }
}