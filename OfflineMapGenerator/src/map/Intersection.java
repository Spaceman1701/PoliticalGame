package map;

import map.intersection.ClippingLinkedList;
import map.intersection.SortedVertexList;
import map.intersection.Vertex;
import math.Util;
import math.Vector2d;

import java.lang.reflect.Array;
import java.time.Clock;
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
                       subject.remove(nextVert);
                       int startClip = clipping.getIndex(nextVert);
                       if (startClip == -1) {
                           continue;
                       }
                       boolean foundClipIntersection = false;
                       for (int k = startClip; !foundClipIntersection; k = (k + 1) % (clipping.size() - 1)) {
                           Vertex nextClipVert = clipping.get(k);
                           output.add(nextClipVert.data);
                           foundClipIntersection = nextClipVert.isIntersection;
                           if (foundClipIntersection) {
                               clipping.remove(nextClipVert);
                           }
                       }
                       //Finished polygon

                       shared.add(vectorListAsRegion(output));

                       if (j <= i) {
                           i = subject.size();
                           System.out.println("loop should break");
                       } else {
                           i = j;
                           System.out.println("end clip");
                       }
                       break;
                   }
               }
           }
       }

       return new Intersection(shared, null, null);



   }

    public static Intersection calculateIntersectionWE2(Region r1, Region r2) {
        ClippingLinkedList<Vertex> vertices = new ClippingLinkedList<>();

        Vector2d r1Center = new Vector2d();
        for (Vector2d vec : r1.getPolygon()) {
            r1Center = Vector2d.add(r1Center, vec);
            vertices.addToClip(new Vertex(vec, false));
        }
        r1Center.mul(1.0d / r1.getPolygon().length);

        ClockwiseComparator2 clipComparator = new ClockwiseComparator2(r1Center);

        Vector2d r2Center = new Vector2d();
        for (Vector2d vec : r2.getPolygon()) {
            r2Center = Vector2d.add(r2Center, vec);
            vertices.addToSubject(new Vertex(vec, false));
        }

        r2Center.mul(1.0d / r2.getPolygon().length);

        ClockwiseComparator2 subjectComparator = new ClockwiseComparator2(r2Center);

        List<Vector2d> segIntersections = getSegItersects(r1, r2);

        for (Vector2d point : segIntersections) {
            vertices.addToBothSorted(new Vertex(point, true));
            System.out.println("Intersects at: " + point);
        }

        System.out.println("There are " + segIntersections.size() + " intersections");


        vertices.addSubjectComparator(subjectComparator);
        vertices.addClipComparator(clipComparator);


        Iterator<Vertex> iter = vertices.getSubjectOnlyIterator();
        while (iter.hasNext()) {
            Vertex v = iter.next();
            System.out.print(v.data + ", " + v.isIntersection + ", ");
        }
        System.out.println();



        boolean clipping = false;
        List<List<Vector2d>> clippingList =  new LinkedList<>();

        List<Vector2d> currentList = null;
        ClippingLinkedList.DoubleIterator verts = (ClippingLinkedList.DoubleIterator)vertices.iterator();
        Vertex previous = vertices.getSubjectEnd(); //Lists are loops
        while(verts.hasNext()) {
            Vertex v = (Vertex)verts.next();
            if (v.isIntersection) {
                //System.out.println(v.data);
                //System.out.println("INTERSECTION");
                if (clipping) {
                    clipping = false;
                    clippingList.add(currentList);
                } else if (Util.pointInsidePolygon(previous.data, r1.getPolygon(), r2.getBoundingBox()) &&
                Util.pointInsidePolygon(((Vertex)verts.peekNext()).data, r1.getPolygon(), r2.getBoundingBox())){
                    clipping = true;
                    currentList = new LinkedList<>();
                    verts.setSwitchOnNextLink(true);
                }
            } else if(clipping) {
                //System.out.println("adding vert not intersect");
            }
            if (clipping) {
                currentList.add(v.data);
                //System.out.println("adding vert");
            }
            previous = v;
        }

        List<Region> shared = new LinkedList<>();
        for (List<Vector2d> polygon : clippingList) {
            Vector2d[] data = new Vector2d[polygon.size()];
            polygon.toArray(data);
            shared.add(new Region(data));
        }

        return new Intersection(shared, null, null);

    }

    private static List<Vector2d> getSegItersects(Region r1, Region r2) {
        List<Vector2d> output = new LinkedList<>();
        for (int i = 0; i < r1.getPolygon().length; i++) {
            Segment seg1 = getSegment(r1.getPolygon(), i);
            for (int j = 0; j < r2.getPolygon().length; j++) {
                Segment seg2 = getSegment(r2.getPolygon(), j);
                Vector2d result = seg1.getIntersection(seg2);
                if (result != null) {
                    output.add(result);
                }
            }
        }
        return output;
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
