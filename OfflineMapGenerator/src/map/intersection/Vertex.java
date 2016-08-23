package map.intersection;

import math.Vector2d;

/**
 * Created by Ethan on 8/19/2016.
 */
public class Vertex {
    public final Vector2d data;
    public final boolean isIntersection;
    private boolean isEntrant;

    public Vertex(Vector2d data, boolean isIntersection) {
        this.data = data;
        this.isIntersection = isIntersection;
    }

    public boolean equals(Object other) {
        if (other.getClass() == Vertex.class) {
            return data.equals(((Vertex)other).data);
        }

        return false;
    }

    public boolean isEntrant() {
        return isEntrant;
    }

    public void setEntrant(boolean value) {
        this.isEntrant = value;
    }
}
