public class Segment {
    public Point p1;
    public Point p2;

    public Segment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public double length(){
        System.out.println("Dlugosc odcinka między punktami p1 i p2 wynosi: " );
        return Math.sqrt(Math.pow((p2.x - p1.x),2) + Math.pow((p2.y - p1.y),2));
    }
}
