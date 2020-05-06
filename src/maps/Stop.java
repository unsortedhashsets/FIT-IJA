package maps;

public class Stop {
    private String id;
    private Coordinate coor;
    private Street street;

    public Stop(String id, Coordinate coordinate){
        this.id = id;
        this.coor = coordinate;
        this.street = null;
    }

    public Stop(String id){
        this.id = id;
        this.coor = null;
        this.street = null;
    }

    public String getId(){
        return this.id;
    }

    public Coordinate getCoordinate(){
        return this.coor;
    }

    public void setStreet(Street s){
        this.street = s;
    }

    public Street getStreet(){
        return this.street;
    }

    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        
        Stop that = (Stop)obj;
        return that.id.equals(this.id);
    }

    public String toString(){
        return "stop(" + this.id + ")";
    }
}