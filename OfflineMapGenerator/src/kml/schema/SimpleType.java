package kml.schema;

/**
 * Created by Ethan on 8/10/2016.
 */
public enum SimpleType {
    DOUBLE, STRING;


    public Object parseString(String string) {
        switch (this) {
            case DOUBLE:
                return Double.parseDouble(string);
            case STRING:
                return string;
        }
        //Unreachable code...
        return null;
    }
}
