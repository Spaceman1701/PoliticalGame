package map;

import math.Triangle;
import math.Vector2d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 8/12/2016.
 */
public class MapArea {
    private List<Triangle> triangles;

    public MapArea(Vector2d[] verticies) {
        trianglulate(verticies);
    }

    private void trianglulate(Vector2d[] verticies) {
        triangles = new ArrayList<>();
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }
}
