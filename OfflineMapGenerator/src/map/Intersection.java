package map;

import map.intersection.SortedVertexList;
import map.intersection.Vertex;
import math.Util;
import math.Vector2d;

import java.lang.reflect.Array;
import java.util.*;

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
        return sharedSubRegion.isEmpty();
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

   public static Intersection calculateIntersectionWE(Region r1, Region r2) {
       SortedVertexList clipping = new SortedVertexList();
       SortedVertexList subject = new SortedVertexList();

       subject.addVectors(r2.getPolygon());
       clipping.addVectors(r1.getPolygon());
       subject.updateCenter();
       clipping.updateCenter();

       for (int i = 0; i < r1.getPolygon().length; i++) {
           Segment seg1 = getSegment(r1.getPolygon(), i);
           for (int j = 0; j < r2.getPolygon().length; j++) {
               Segment seg2 = getSegment(r2.getPolygon(), j);
               Vector2d iPoint;
               //System.out.println("testing polygon intersect");
               if ((iPoint = seg1.getIntersection(seg2)) != null ) {
                   Vertex intersect = new Vertex(iPoint, true);
                   clipping.add(intersect);
                   subject.add(intersect);
                   //System.out.println("POLYONG INTERSECT AT: " + intersect.data);
               }
           }
       }

       subject.sortClockwise();
       clipping.sortClockwise();

       List<Region> shared = new ArrayList<Region>();

       for (int i = 0; i < subject.size(); i++) {
           Vertex suvjectV = subject.get(i);
           if (suvjectV.isIntersection) {
               List<Vector2d> output = new ArrayList<Vector2d>();
               output.add(suvjectV.data);

               boolean foundIntersection = false;
               for (int j = (i + 1) % (subject.size() - 1); !foundIntersection; j = ((j + 1) % (subject.size() - 1))) {
                   Vertex nextVert = subject.get(j);
                   output.add(nextVert.data);
                   foundIntersection = nextVert.isIntersection;
                   if (foundIntersection) {
                       int startClip = clipping.getIndex(nextVert);
                       boolean foundClipIntersection = false;
                       for (int k = startClip; !foundClipIntersection; k = (k + 1) % (clipping.size() - 1)) {
                           Vertex nextClipVert = clipping.get(k);
                           output.add(nextClipVert.data);
                           foundClipIntersection = true;
                       }
                       //Finished polygon
                       
                       shared.add(vectorListAsRegion(output));
                       
                       if (j <= i) {
                           i = subject.size();
                       } else {
                           i = j;
                       }
                       break;
                   }
               }
           }
       }

       return new Intersection(shared, null, null);



   }

    private static Region vectorListAsRegion(List<Vector2d> list) {
        Vector2d[] data = new Vector2d[list.size()];
        list.toArray(data);
        return new Region(data);
    }

    private static Segment getSegment(Vector2d[] polygon, int start) {
        Vector2d s = polygon[start];
        if (start + 1 < polygon.length) {
            return new Segment(s, polygon[start + 1]);
        }
        return new Segment(s, polygon[0]);
    }



}
