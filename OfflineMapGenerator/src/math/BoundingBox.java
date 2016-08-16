package math;

/**
 * Created by Ethan on 8/15/2016.
 */
public class BoundingBox {
    private Vector2d min;
    private Vector2d max;
    private Vector2d center;
    private Vector2d extent;

    private BoundingBox(double minX, double maxX, double minY, double maxY) {
        min = new Vector2d(minX, minY);
        max = new Vector2d(maxX, maxY);
        extent = new Vector2d((maxX - minX) / 2.0d, (maxY - minY) / 2.0d);
        center = new Vector2d((maxX + minX) / 2.0d, (maxY + minY) / 2.0d);
    }

    public static BoundingBox generateBoundingBox(Vector2d[] polygon) {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Vector2d vec : polygon) {
            if (vec.x < minX) {
                minX = vec.x;
            } else if (vec.x > maxX) {
                maxX = vec.x;
            }
            if (vec.y < minY) {
                minY = vec.y;
            } else if (vec.y < maxY) {
                maxY = vec.y;
            }
        }

        return new BoundingBox(minX, maxX, minY, maxY);
    }

    public Vector2d getMin() {
        return min;
    }

    public Vector2d getMax() { return max; }

    public boolean intersects(BoundingBox bb) {
        Vector2d delta = Vector2d.sub(bb.center, center);

        return (delta.x < bb.extent.x + extent.x) && (delta.y < bb.extent.y + extent.y);
    }
}
