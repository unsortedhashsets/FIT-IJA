package maps;

public class Coordinate{
    private final int X;
    private final int Y;

    public Coordinate(int x, int y){
        this.X = x;
        this.Y = y;
    }

    public static Coordinate create(int x, int y){
        return (x >= 0 && y >= 0) ? new Coordinate(x, y) : null;
    }

    public int getX(){
        return this.X;
    }

    public int getY(){
        return this.Y;
    }

    public int diffX(Coordinate c) {
        return this.X - c.getX();
    }

    public int diffY(Coordinate c) {
        return this.Y - c.getY();
    }

    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        
        Coordinate that = (Coordinate)obj;
        return that.X == this.X && that.Y == this.Y;
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = 31*hash + this.X;
        hash = 31*hash + this.Y;
        
        return hash;
    }
}
