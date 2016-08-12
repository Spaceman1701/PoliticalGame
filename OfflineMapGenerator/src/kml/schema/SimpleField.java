package kml.schema;

/**
 * Created by Ethan on 8/10/2016.
 */
public class SimpleField {

    private final Object value;
    private final SimpleType type;

    public SimpleField(SimpleType type, Object value) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public SimpleType getType() {
        return type;
    }

    public String toString() {
        return value.toString();
    }
}
