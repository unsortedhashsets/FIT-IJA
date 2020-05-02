package maps;

public class MyStop implements Stop{
    private String id;
    private Coordinate coor;
    private Street street;

    public MyStop(String id, Coordinate coordinate){
        this.id = id;
        this.coor = coordinate;
        this.street = null;
    }

    public MyStop(String id){
        this.id = id;
        this.coor = null;
        this.street = null;
    }

    @Override
    public String getId(){
        return this.id;
    }

    @Override
    public Coordinate getCoordinate(){
        return this.coor;
    }

    @Override
    public void setStreet(Street s){
        this.street = s;
    }

    @Override
    public Street getStreet(){
        return this.street;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        
        MyStop that = (MyStop)obj;
        return that.id.equals(this.id);
    }

    @Override
    public String toString(){
        return "stop(" + this.id + ")";
    }
}