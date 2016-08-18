package map;

import math.Util;
import math.Vector2d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 8/17/2016.
 */
public class Intersection {

    private List<Region> sharedSubRegion;
    private List<Region> r1SubRegion;
    private List<Region> r2SubRegion;

    private Intersection(List<Region> sharedSubRegion, List<Region> r1SubRegion, List<Region> r2SubRegion) {
        this.sharedSubRegion = sharedSubRegion;
        this.r1SubRegion = r1SubRegion;
        this.r2SubRegion = r2SubRegion;
    }

    public List<Region> getSharedSubRegion() {
        return sharedSubRegion;
    }

    public List<Region> getR1SubRegion() {
        return r1SubRegion;
    }

    public List<Region> getR2SubRegion() {
        return r2SubRegion;
    }

    public List<Region> getAllSubRegions() {
        List<Region> output = new ArrayList<>();
        output.addAll(sharedSubRegion);
        output.addAll(r1SubRegion);
        output.addAll(r2SubRegion);

        return output;
    }

    public boolean isEmpty() {
        return sharedSubRegion.isEmpty() || r2SubRegion.isEmpty() || r2SubRegion.isEmpty();
    }

    public static Intersection calculateIntersection(Region r1, Region r2) {
        Vector2d[] p1 = r1.getPolygon();
        Vector2d[] p2 = r2.getPolygon();

        for (int i = 0; i < p1.length; i++) {
            Vector2d vertex1 = p1[i];
            boolean inside = Util.pointInsidePolygon(vertex1, p2, r2.getBoundingBox());

            Vector2d nextVert = null;
            if (i + 1 < p1.length) {
                nextVert = p1[i + 1];
            } else {
                nextVert = p1[0]; //Wrap around. I should really just make a class for this...
            }

            Segment clockwiseSeg = new Segment(vertex1, nextVert);
            List<Vector2d> segIntersections = clockwiseSeg.getRegionIntersection(r2);

            if (inside) {
                if (segIntersections.isEmpty()) {
                    //Mean's next vertex is also inside
                    continue;
                } else if (segIntersections.size() == 1){
                    //Seg exits r2 and does not re-enter

                } else {
                    //seg exits and re-enters... this really sucks
                }
            } else {
                if (segIntersections.isEmpty()) {
                    //No intersection here
                    continue;
                } else if (segIntersections.size() == 1) {
                    //Seg enters r2
                } else {
                    //this really sucks
                }
            }
        }

        return new Intersection(null, null, null);
    }

    public static Intersection getIntersectionClipper(Region r1, Region r2) {
        return null;
    }



}
