package map;

import math.BoundingBox;
import math.Vector2d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 8/12/2016.
 */
public class Region {

    private Vector2d[] polygon;
    private BoundingBox boundingBox;
    private List<Region> associations;

    public Region(Vector2d[] polygon) {
        this.polygon = polygon;
        boundingBox = BoundingBox.generateBoundingBox(polygon);
        associations = new ArrayList<>();
    }

    public boolean isRoughCollision(Region r) {
        return r.boundingBox.intersects(boundingBox);
    }

    public void addAssociation(Region r) {
        associations.add(r);
    }

    public List<Region> getAssociations() {
        return associations;
    }

    public Vector2d[] getPolygon() {
        return polygon;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}
