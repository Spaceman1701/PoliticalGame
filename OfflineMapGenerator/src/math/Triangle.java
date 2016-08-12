package math;

/**
 * Created by Ethan on 8/12/2016.
 */
public class Triangle {
    private Vector2d p1;
    private Vector2d p2;
    private Vector2d p3;

    public Triangle(Vector2d p1, Vector2d p2, Vector2d p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Vector2d getP1() {
        return p1;
    }

    public Vector2d getP2() {
        return p2;
    }

    public Vector2d getP3() {
        return p3;
    }
}
