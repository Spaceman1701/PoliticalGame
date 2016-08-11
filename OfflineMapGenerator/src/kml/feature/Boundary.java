package kml.feature;

import math.Vector2d;

/**
 * Created by Ethan on 8/10/2016.
 */
public class Boundary {

    private Vector2d[] verticies;

    public Boundary(Vector2d[] verticies) {
        this.verticies = verticies;
    }

    public Vector2d[] getVerticies() {
        return verticies;
    }
}
