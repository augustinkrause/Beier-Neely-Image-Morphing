import java.util.Vector;

public class Feature {

    private Vector2d start;
    private Vector2d end;

    public Feature(Vector2d start, Vector2d end) {
        this.start = start;
        this.end = end;
    }

    public Vector2d getStart() {
        return start;
    }

    public void setStart(Vector2d start) {
        this.start = start;
    }

    public Vector2d getEnd() {
        return end;
    }

    public void setEnd(Vector2d end) {
        this.end = end;
    }

    public double distOfPoint(Vector2d point){
        /*double px = this.end.getX() - this.start.getX();
        double py = this.end.getY() - this.start.getY();

        double norm = px*px + py*py;

        double u =  ((point.getX() - this.start.getX()) * px + (point.getY() - this.start.getY()) * py) / norm;

        if (u > 1) {
            u = 1;
        }else if (u < 0) {
            u = 0;
        }

        double x = this.start.getX() + u * px;
        double y = this.start.getY() + u * py;

        double dx = x - point.getX();
        double dy = y - point.getY();

        double dist = Math.sqrt(dx*dx + dy*dy);

        return dist;*/

        // Return minimum distance between line segment vw and point p
        double l2 = Math.pow(this.end.subtract(this.start).magnitude(), 2);  // i.e. |w-v|^2 -  avoid a sqrt
        if (l2 == 0.0){ return point.distOfVector(this.start);}   // v == w case
        // Consider the line extending the segment, parameterized as v + t (w - v).
        // We find projection of point p onto the line.
        // It falls where t = [(p-v) . (w-v)] / |w-v|^2
        // We clamp t from [0,1] to handle points outside the segment vw.
        double t = Math.max(0, Math.min(1, point.subtract(this.start).dotProduct(this.end.subtract(this.start)) / l2));
        Vector2d projection = new Vector2d( this.start.getX() + t * (this.end.getX() - this.start.getX()),
                this.start.getY() + t * (this.end.getY() - this.start.getY()));  // Projection falls on the segment
        return point.distOfVector(projection);
    }

    @Override
    public String toString() {
        return "Feature{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
