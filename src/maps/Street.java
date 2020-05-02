/*
 * Zdrojové kódy josu součástí zadání 1. úkolu pro předmětu IJA v ak. roce 2019/2020.
 * (C) Radek Kočí
 */
package maps;

import java.util.List;

/**
 * Reprezentuje jednu ulici v mapě. Ulice má svůj identifikátor (název) a je definována souřadnicemi. Pro 1. úkol
 * předpokládejte pouze souřadnice začátku a konce ulice.
 * Na ulici se mohou nacházet zastávky.
 * @author koci
 */
public interface Street {
    /**
     * Vrátí identifikátor ulice.
     * @return Identifikátor ulice.
     */
    public String getId();
    
    /**
     * Vrátí seznam souřadnic definujících ulici. První v seznamu je vždy počátek a poslední v seznamu konec ulice.
     * @return Seznam souřadnic ulice.
     */
    
    public List<Coordinate> getCoordinates();
    
    /**
     * Vrátí seznam zastávek na ulici.
     * @return Seznam zastávek na ulici. Pokud ulize nemá žádnou zastávku, je seznam prázdný.
     */
    public List<Stop> getStops();
    
    /**
     * Přidá do seznamu zastávek novou zastávku.
     * @param stop Nově přidávaná zastávka.
     */
    public boolean addStop(Stop stop);

    public boolean follows(Street s);

    public Coordinate begin();

    public Coordinate end();

    public static Street defaultStreet(String id, Coordinate... coordinates) {
        int length = coordinates.length;

        if (length > 2){
            for (int i = 1; i < length - 1; i++){
                int vector1_X = coordinates[i].diffX(coordinates[i-1]);
                int vector1_Y = coordinates[i].diffY(coordinates[i-1]);
                
                int vector2_X = coordinates[i].diffX(coordinates[i+1]);
                int vector2_Y = coordinates[i].diffY(coordinates[i+1]);

                int scalar = vector1_X*vector2_X + vector1_Y*vector2_Y;

                if (scalar != 0){
                    return null;
                }
            }
        }

        return new MyStreet(id, coordinates);
    }
}
