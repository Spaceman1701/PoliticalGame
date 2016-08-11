package kml.schema;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan on 8/10/2016.
 */
public class SchemaMap {
    private Map<String, SimpleType> schema;


    public SchemaMap() {
        schema = new HashMap<>();
    }

    public void put(String name, SimpleType type) {
        schema.put(name, type);
    }

    public SimpleType getType(String name) {
        return schema.get(name);
    }
}
