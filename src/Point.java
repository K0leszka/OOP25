 public class Point {
        float x;
        float y;

        public String toString(){
            System.out.println();
            return "Punkty (x i y) wynoszą: " + x + " " + y;
        }

        public String translate(float dxx, float dxy){
            x -= dxx;
            y -= dxy;
            System.out.println();
            return "Punkty (x i y) po przesunięciu wynoszą: " + x + " " + y;
        }
        public Point translated(float dxx, float dxy){
            Point newPoint = new Point();
            newPoint.x = this.x + dxx;
            newPoint.y = this.y + dxy;
            return newPoint;
        }
//    public String toSvg(){
//        System.out.println();
//        return "<svg height=\"100\" width=\"100\" xmlns=\"http://www.w3.org/2000/svg\"> " +
//                "<circle r=\"45\" cx=" + x + " cy=" + y + " fill=\"red\" /> " + "</svg>";
//    }
    }


