package map;

import map.intersection.Vertex;
import math.Vector2d;

import java.util.Comparator;

/**
 * Created by Ethan on 8/20/2016.
 */
public class ClockwiseComparator implements Comparator<Vertex> {

    private Vector2d cent;

    public ClockwiseComparator(Vector2d cent) {
        this.cent = cent;
    }

    private static final int LESS = -1;
    private static final int GREATER = 1;
    @Override
    public int compare(Vertex va, Vertex vb) {
        Vector2d a = va.data;
        Vector2d b = vb.data;

        if (a.x - cent.x >= 0 && b.x - cent.x < 0) {
            return LESS;
        }
        if (a.x - cent.x < 0 && b.x - cent.x >= 0) {
            return GREATER;
        }
        if (a.x - cent.x == 0 && b.x - cent.x == 0) {
            if (a.y - cent.y >= 0 || b.y - cent.y >= 0) {
                return (int) Math.signum(a.y - b.y);
            }
            return (int) Math.signum(b.y - a.y);
        }

        double det = (a.x - cent.x) * (b.y - cent.y) - (b.x - cent.x) * (a.y - cent.y);
        if (det < 0) {
            return LESS;
        }
        if (det > 0) {
            return GREATER;
        }

        double d1 = Vector2d.sub(a, cent).mag2();
        double d2 = Vector2d.sub(b, cent).mag2();

        return (int) Math.signum(d1 - d2);
    }
}
