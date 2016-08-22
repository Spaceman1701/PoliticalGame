import map.Intersection;
import map.Region;
import map.intersection.SortedVertexList;
import map.intersection.Vertex;
import math.Vector2d;

/**
 * Created by Ethan on 8/20/2016.
 */
public class TestPolygonIntersection {

    private static Vector2d[] p1 = {new Vector2d(0.0d, 1.0d), new Vector2d(1.0d, 1.0d), new Vector2d(1.0d, 0.0d),
                                        new Vector2d(0.0d, 0.0d)};

    public static void main(String[] args) {
        Vector2d[] p2 = new Vector2d[p1.length];

        for (int i = 0; i < p2.length; i++) {
            p2[i] = Vector2d.add(new Vector2d(0.5d, 0.5d), p1[i]);
        }

        SortedVertexList sv = new SortedVertexList();
        sv.addVectors(p1);
        sv.updateCenter();
        sv.sortClockwise();
        for (Vertex v : sv) {
            System.out.print(v.data + ", ");
        }
        System.out.println();


        Intersection intersection = Intersection.calculateIntersectionWE2(new Region(p1), new Region(p2));

        System.out.println(intersection.getSharedSubRegion().size());
        for (Region r : intersection.getSharedSubRegion()) {
            System.out.println(r);
        }
    }
}
