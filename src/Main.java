public class Main {
    public static void main(String[] args) {
        Point p1 = new Point();
        p1.x = 0;
        p1.y = 0;
        Point p2 = p1.translated(3,4);
        Point p3 = p2.translated(6,8);
        Point p4 = p3.translated(9,12);
        Point p5 = p4.translated(11,16);
        Point p6 = p5.translated(14,20);
        System.out.println(p1.toString());
        // System.out.println(p1.toSvg());;
        // System.out.println(p1.translate(15,20));
        System.out.println(p2.toString());
        System.out.println(p3.toString());;
        System.out.println(p4.toString());
        System.out.println(p5.toString());
        System.out.println(p6.toString());

        Segment s1 = new Segment(p1,p2);
        Segment s2 = new Segment(p3,p4);
        Segment s3 = new Segment(p5,p6);

        System.out.println("s1 length: " + s1.length());
        System.out.println("s2 length: " + s2.length());
        System.out.println("s3 length: " + s3.length());

    }

}