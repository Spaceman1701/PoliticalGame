package kml.feature;

import kml.schema.SimpleField;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ethan on 8/10/2016.
 */
public class Feature {

    private Map<String, SimpleField> dataMap;
    private Set<Polygon> polygons;

    public Feature() {
        dataMap = new HashMap<>();
        polygons = new HashSet<>();
    }

    public void addPolygon(Polygon p) {
        polygons.add(p);
    }

    public Set<Polygon> getPolygons() {
        return polygons;
    }

    public void addField(String name, SimpleField f) {
        dataMap.put(name, f);
    }

    public SimpleField getField(String name) {
        return dataMap.get(name);
    }

    public String getName() {
        return (String)getField("NAME").getValue();
    }
}
