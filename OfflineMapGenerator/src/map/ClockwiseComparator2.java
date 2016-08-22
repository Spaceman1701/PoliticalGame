package map;

import map.intersection.Vertex;
import math.Util;
import math.Vector2d;

import java.util.Comparator;

/**
 * Created by Ethan on 8/21/2016.
 */
public class ClockwiseComparator2 implements Comparator<Vertex> {
    private Vector2d cent;

    public ClockwiseComparator2(Vector2d cent) {
        this.cent = cent;
    }
    @Override
    public int compare(Vertex o1, Vertex o2) {
        Vector2d v1 = Vector2d.sub(o1.data, cent);
        Vector2d v2 = Vector2d.sub(o2.data, cent);

        Vector2d v1Polar = Util.toPolar(v1);
        Vector2d v2Polar = Util.toPolar(v2);

        if (v1Polar.y == v2Polar.y) {
            return (int) Math.signum(v1Polar.x - v2Polar.x);
        }

        return (int) Math.signum(v1Polar.y - v2Polar.y);
    }
}
