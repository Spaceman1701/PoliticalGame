package map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ethan on 8/17/2016.
 */
public class Layer {
    private final String name;
    private List<Region> regions;

    public Layer(String name) {
        this.name = name;
        regions = new ArrayList<>();
    }

    public void addRegion(Region r) {
        regions.add(r);
    }

    public void addRegions(Collection<Region> r) {
        regions.addAll(r);
    }

    public List<Region> getRegions() {
        return regions;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }
}
