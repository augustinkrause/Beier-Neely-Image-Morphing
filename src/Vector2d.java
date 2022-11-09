public class Vector2d {

    private double x;
    private double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector2d add(Vector2d vector){
        return new Vector2d(this.x + vector.getX(), this.y + vector.getY());
    }

    public Vector2d subtract(Vector2d vector){
        return new Vector2d(this.x - vector.getX(), this.y - vector.getY());
    }

    public Vector2d scale(int scalar){
        return new Vector2d(this.x * scalar, this.y * scalar);
    }

    public Vector2d scale(double scalar){
        return new Vector2d(this.x * scalar, this.y * scalar);
    }

    public double magnitude(){
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public double dotProduct(Vector2d vector){
        return this.x * vector.getX() + this.y * vector.getY();
    }

    public Vector2d perpendicular(){
        return new Vector2d(1.0, -this.x/this.y);
    }

    public double distOfVector(Vector2d vec){
        return(Math.sqrt(Math.pow(vec.getX() - this.x, 2) + Math.pow(vec.getY() - this.y, 2)));
    }

    @Override
    public String toString() {
        return "Vector2d{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
