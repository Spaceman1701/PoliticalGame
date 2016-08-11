package kml.feature;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ethan on 8/10/2016.
 */
public class Polygon {

    private final Boundary outerBoundary;
    private Set<Boundary> innerBoundaries;

    public Polygon(Boundary outerBoundary) {
        this.outerBoundary = outerBoundary;

        innerBoundaries = new HashSet<>();
    }

    public Boundary getOuterBoundary() {
        return outerBoundary;
    }

    public Set<Boundary> getInnerBoundaries() {
        return innerBoundaries;
    }

    /**
     * @param b - does not check to ensure physical possible boundary
     */
    public void addInnerBoundary(Boundary b) {
        innerBoundaries.add(b);
    }
}
