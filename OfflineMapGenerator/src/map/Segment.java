package map;

import kml.feature.Polygon;
import math.Util;
import math.Vector2d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 8/17/2016.
 */
public class Segment {
    private Vector2d start;
    private Vector2d end;

    public Segment(Vector2d start, Vector2d end) {
        this.start = start;
        this.end = end;
    }

    public Vector2d getIntersection(Segment s) {
        return Util.getIntersection(start, end, s.start, s.end);
    }

    public Vector2d getStart() {
        return start;
    }

    public Vector2d getEnd() {
        return end;
    }

    public List<Vector2d> getRegionIntersection(Region r) {
        List<Vector2d> intersections = new ArrayList<>();

        for (int i = 0; i < r.getPolygon().length; i++) {
            Vector2d pStart = r.getPolygon()[i];

            Vector2d pEnd = null;

            if (i + 1 < r.getPolygon().length - 1) {
                pEnd = r.getPolygon()[i + 1];
            } else {
                pEnd = r.getPolygon()[0];
            }

            Vector2d intersect = Util.getIntersection(start, end, pStart, pEnd);

            if (intersect != null) {
                intersections.add(intersect);
            }
        }

        return intersections;
    }
}
