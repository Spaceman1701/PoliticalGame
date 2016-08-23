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

    private enum ClipState {
        NOT_CLIIPING, CLIPPING_ON_SUBJECT, CLIPPING_ON_CLIP
    }

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

    public static Intersection calculateIntersectionSM(Region r1, Region r2) {
        List<Vertex> subject = getVertexList(r1);
        List<Vertex> clip = getVertexList(r2);

        List<Vertex> segIntersections = getSegIntersectionsVertex(r1, r2);

        Vector2d subjectCenter = calculateCenter(subject);
        Vector2d clipCenter = calculateCenter(clip);

        subject.addAll(segIntersections);
        clip.addAll(segIntersections);

        subject.sort(new ClockwiseComparator2(subjectCenter));
        clip.sort(new ClockwiseComparator2(clipCenter));

        List<Vertex> entrentIntersects = getEntrentIntersects(subject, clip, r1, r2);

        List<List<Vertex>> outputLists = new LinkedList<>();
        for (Vertex v : entrentIntersects) {
            List<Vertex> clipped = new LinkedList<>();
            clipped.add(v);
            int vIndex = getIndex(v, subject) + 1;
            if (vIndex == 0) {
                throw new RuntimeException("vertex not found!");
            }
            if (vIndex >= subject.size()) { //Loop around
                vIndex = 0;
            }
            clipped = doClipping(subject, clip, vIndex);
            clipped.sort(new ClockwiseComparator2(calculateCenter(clipped)));
            outputLists.add(clipped);
        }

        System.out.println(outputLists.size());

        List<Region> shared = new LinkedList<>();
        for (List<Vertex> polygon : outputLists) {
            shared.add(vertexListToRegion(polygon));
        }

        return new Intersection(shared, null, null);
    }

    private static Region vertexListToRegion(List<Vertex>  v) {
        Vector2d[] output = new Vector2d[v.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = v.get(i).data;
        }
        return new Region(output);
    }

    private static List<Vertex> doClipping(List<Vertex> subject, List<Vertex> clip, int startIndex) {
        List<Vertex> output = new LinkedList<>();
        List<Vertex> clipOutput = null;
        Iterator<Vertex> iterator = subject.listIterator(startIndex);
        while(iterator.hasNext()) {
            Vertex v = iterator.next();
            output.add(v);
            if (v.isIntersection) {
                int vIndex = getIndex(v, clip) + 1;
                if (vIndex == 0) {
                    throw new RuntimeException("vertex not found!");
                }
                if (vIndex >= clip.size()) { //Loop around
                    vIndex = 0;
                }
                clipOutput = getClipVerticies(clip, vIndex);
                break; //done here
            }
        }
        if (clipOutput == null) { //didn't find a intersect, means we have to loop around
            for (Vertex v : subject) {
                if (v.isIntersection) {
                    int vIndex = getIndex(v, clip) + 1;
                    if (vIndex == 0) {
                        throw new RuntimeException("vertex not found!");
                    }
                    if (vIndex >= clip.size()) { //Loop around
                        vIndex = 0;
                    }
                    clipOutput = getClipVerticies(clip, vIndex);
                    break; //done here
                }
            }
        }
        if (clipOutput == null) {
            throw new RuntimeException("never found a second intersect");
        }
        output.addAll(clipOutput);
        return output;
    }

    private static List<Vertex> getClipVerticies(List<Vertex> clip, int startIndex) {
        List<Vertex> output = new LinkedList<>();
        Iterator<Vertex> iterator = clip.listIterator(startIndex);

        boolean found = false;
        while (iterator.hasNext()) {
            Vertex v = iterator.next();
            output.add(v);
            if (v.isIntersection) {
                found = true;
                return output;
            }
        }
        //Didn't end, loop back around
        for(Vertex v : clip) {
            output.add(v);
            if (v.isIntersection) {
                found = true;
                return output;
            }
        }
        if (!found) {
            throw new RuntimeException("never found a vertex");
        }

        return output;
    }

    private static int getIndex(Vertex v, List<Vertex> list) {
        int i = 0;
        for (Vertex vert : list) {
            if (vert == v) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private static Vector2d calculateCenter(List<Vertex> polygon) {
        Vector2d center = new Vector2d();

        for (Vertex v : polygon) {
            Vector2d vec = v.data;
            center = Vector2d.add(center, v.data);
        }

        center.mul(1.0d/polygon.size());
        return center;
    }

    private static List<Vertex> getEntrentIntersects(List<Vertex> subject, List<Vertex> clip, Region subjectRegion, Region clipRegion) {
        List<Vertex> output = new ArrayList<>();

        Vertex previous = subject.get(subject.size() - 1);
        for (int i = 0; i < subject.size(); i++) {
            Vertex v = subject.get(i);
            if (v.isIntersection) {
                Vector2d next = null;
                if (i != 0) {
                    previous = subject.get(i - 1);
                }
                if (i != subject.size() - 1) {
                    next = subject.get(i + 1).data;
                } else {
                    next = subject.get(0).data;
                }
                boolean startOut = Util.pointInsidePolygon(previous.data, clipRegion.getPolygon(), null);
                boolean endOut = Util.pointInsidePolygon(next, clipRegion.getPolygon(), null);

                if (!startOut && endOut) {
                    v.setEntrant(true);
                    output.add(v);
                }
            }
        }

        return output;
    }

    private static List<Vertex> getSegIntersectionsVertex(Region r1, Region r2) {
        List<Vector2d> segIntersects = getSegItersects(r1, r2);

        List<Vertex> output = new ArrayList<>();

        for (Vector2d v : segIntersects) {
            output.add(new Vertex(v, true));
        }

        return output;
    }

    private static List<Vertex> getVertexList(Region r) {
        List<Vertex> output = new ArrayList<>();

        for (Vector2d v : r.getPolygon()) {
            output.add(new Vertex(v, false));
        }

        return output;
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
