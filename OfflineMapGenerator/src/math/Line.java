package math;

/**
 * Created by Ethan on 8/10/2016.
 */
public class Line {
    private Vector2i start;
    private Vector2i end;

    public Line(Vector2i start, Vector2i end) {
        this.start = start;
        this.end = end;
    }

    public Vector2i getStart() {
        return new Vector2i(start);
    }

    public Vector2i getEnd() {
        return new Vector2i(end);
    }
}
