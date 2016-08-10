package math;

/**
 * Created by Ethan on 8/10/2016.
 */
public class Vector2f {
    public float x;
    public float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f vec) {
        this(vec.x, vec.y);
    }

    public Vector2f() {
        this(0, 0);
    }

    public static Vector2f add(Vector2f a, Vector2f b) {
        return new Vector2f(a.x + b.x, a.y + b.y);
    }

    public static Vector2f sub(Vector2f a, Vector2f b) {
        return new Vector2f(a.x - b.x, a.y - b.y);
    }

    public static float dot(Vector2f a, Vector2f b) {
        return a.x * b.x + a.y * b.y;
    }

    public float mag2() {
        return Vector2f.dot(this, this);
    }

    public float mag() {
        return (float)Math.sqrt(mag2());
    }
}
