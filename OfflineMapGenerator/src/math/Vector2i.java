package math;

/**
 * Created by Ethan on 8/10/2016.
 */
public class Vector2i {

    public int x;
    public int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i(Vector2i vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vector2i() {
        this(0, 0);
    }

    public static Vector2i add(Vector2i a, Vector2i b) {
        return new Vector2i(a.x + b.x, a.y + b.y);
    }

    public static Vector2i sub(Vector2i a, Vector2i b) {
        return new Vector2i(a.x - b.x, a.y - b.y);
    }

    public static int dot(Vector2i a, Vector2i b) {
        return a.x * b.x + a.y * b.y;
    }

    public int mag2() {
        return Vector2i.dot(this, this);
    }

    public float mag() {
        return (float)Math.sqrt(mag2());
    }
}
