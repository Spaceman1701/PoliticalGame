package math;

/**
 * Created by Ethan on 8/10/2016.
 */
public class Vector2d {
    public double x;
    public double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d vec) {
        this(vec.x, vec.y);
    }

    public Vector2d() {
        this(0, 0);
    }

    public static Vector2d add(Vector2d a, Vector2d b) {
        return new Vector2d(a.x + b.x, a.y + b.y);
    }

    public static Vector2d sub(Vector2d a, Vector2d b) {
        return new Vector2d(a.x - b.x, a.y - b.y);
    }

    public static double dot(Vector2d a, Vector2d b) {
        return a.x * b.x + a.y * b.y;
    }

    public double mag2() {
        return Vector2d.dot(this, this);
    }

    public double mag() {
        return Math.sqrt(mag2());
    }

    public String toString() {
        return "Vector[" + x +", " + y + "]";
    }

    public Vector2d mul(double mul) {
        x *= mul;
        y *= mul;

        return this;
    }

    //No epsilon R/N because I don't really need this method to work in instances when the vector is acctually a different instance...
    //I know I suck
    public boolean equals(Object other) {
        if (other.getClass() == Vector2d.class) {
            Vector2d vec = (Vector2d) other;
            return vec.x == x && vec.y == y;
        }

        return false;
    }

}
