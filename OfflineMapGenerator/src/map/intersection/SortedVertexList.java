package map.intersection;

import math.Vector2d;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Ethan on 8/19/2016.
 */
public class SortedVertexList extends LinkedList<Vertex>{

    private Vertex center;

    public int getIndex(Vertex vertex) { //Not implemented using equals
        int i = 0;
        for (Vertex v : this) {
            if (v == vertex) {
                return i;
            }
        }

        return -1;
    }

    public void updateCenter() {
        Vector2d center = new Vector2d();

        for (Vertex v : this) {
            center = Vector2d.add(center, v.data);
        }

        center.mul(1.0d/size());

        this.center = new Vertex(center, false);
    }

    public void addVectors(Vector2d[] vectors) {
        for (Vector2d v : vectors) {
            add(new Vertex(v, false));
        }
    }

    public void sortClockwise() {
        Collections.sort(this, new Comparator<Vertex>() {
            private static final int LESS = -1;
            private static final int GREATER = 1;
            @Override
            public int compare(Vertex va, Vertex vb) {
                Vector2d a = va.data;
                Vector2d b = vb.data;
                Vector2d cent = center.data;

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
        });
    }
}
