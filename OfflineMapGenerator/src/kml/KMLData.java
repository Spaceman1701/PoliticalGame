package kml;


import kml.feature.Boundary;
import kml.feature.Feature;
import kml.feature.Polygon;
import kml.schema.SchemaMap;
import kml.schema.SimpleField;
import kml.schema.SimpleType;
import math.Vector2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan on 8/10/2016.
 */
public class KMLData {
    private static final String DOCUMENT_TAG = "Document";
    private static final String STRING_NAME = "xsd:string";
    private static final String DOUBLE_NAME = "xsd:double";
    private static final String FIELD_TAG = "SimpleField";
    private static final String SCHEMA_TAG = "Schema";
    private static final String FOLDER_TAG = "Folder";
    private static final String PLACEMARK_TAG = "Placemark";
    private static final String DATA_TAG = "ExtendedData";
    private static final String SCHEMA_DATA_TAG = "SchemaData";
    private static final String SIMPLE_DATA_TAG = "SimpleData";
    private static final String GEOMETRY_TAG = "MultiGeometry";
    private static final String POLYGON_TAG = "Polygon";
    private static final String OUTER_BOUNDARY_TAG = "outerBoundaryIs";
    private static final String RING_TAG = "LinearRing";
    private static final String COORD_TAG = "coordinates";

    private ArrayList<Feature> features;

    private KMLData() {
        features = new ArrayList<>();
    }

    private void addFeature(Feature feature) {
        features.add(feature);
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public static KMLData loadKML(File file) throws IOException, SAXException, ParserConfigurationException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);

        Element root = doc.getDocumentElement();
        System.out.println("root: " + root.getTagName());

        NodeList docNode = root.getElementsByTagName(DOCUMENT_TAG);
        Element docElement = (Element)docNode.item(0);

        System.out.println(docElement.getTagName());

        Element schemaData = (Element)docElement.getElementsByTagName(SCHEMA_TAG).item(0);
        SchemaMap schemaMap = loadSchema(schemaData);

        Element dataFolder = (Element)docElement.getElementsByTagName(FOLDER_TAG).item(0);
        System.out.println("Folder found: " + dataFolder.getAttribute("id"));

        KMLData kml = new KMLData();

        NodeList placemarks = dataFolder.getElementsByTagName(PLACEMARK_TAG);

        for (int i = 0; i < placemarks.getLength(); i++) {
            Element placemark = (Element)placemarks.item(i);
            kml.addFeature(createFeature(placemark, schemaMap));
        }

        System.out.println("Finished loading kml... found " + kml.getFeatures().size() + " features");

        return kml;
    }

    private static Feature createFeature(Element placemark, SchemaMap schemaMap) {
        Feature feature = new Feature();

        NodeList fieldNodes = parseFieldNodes(placemark);

        for (int i = 0; i < fieldNodes.getLength(); i++) {
            Element field = (Element)fieldNodes.item(i);

            String name = field.getAttribute("name");
            SimpleType type = schemaMap.getType(name);

            feature.addField(name, new SimpleField(type, type.parseString(field.getTextContent())));
        }

        Element geometry = (Element)placemark.getElementsByTagName(GEOMETRY_TAG).item(0);

        NodeList polygons = null;

        if (geometry != null) {
            polygons = geometry.getElementsByTagName(POLYGON_TAG);
        } else { //No MuliGeometry present... means polygons will always be length 1...
            polygons = placemark.getElementsByTagName(POLYGON_TAG);
        }



        for (int i = 0; i < polygons.getLength(); i++) {
            feature.addPolygon(parsePolygon((Element)polygons.item(i)));
        }

        return feature;
    }

    private static Polygon parsePolygon(Element polygonData) {
        Element outerBounds = (Element)((Element)polygonData.getElementsByTagName(OUTER_BOUNDARY_TAG)
                .item(0)).getElementsByTagName(RING_TAG).item(0);
        Element outerCoordList = (Element)outerBounds.getElementsByTagName(COORD_TAG).item(0);

        Vector2d[] outerBoundData = parseCoordList(outerCoordList);

        Boundary outerbounds = new Boundary(outerBoundData);
        Polygon polygon = new Polygon(outerbounds);

        return polygon;
    }

    private static Vector2d[] parseCoordList(Element coords) {
        String data = coords.getTextContent();
        String[] vertsData = data.split(" ");
        Vector2d[] verts = new Vector2d[vertsData.length];

        for (int i = 0; i < vertsData.length; i++) {
            String vertex = vertsData[i];
            String[] component = vertex.split(",");

            verts[i] = new Vector2d(Double.parseDouble(component[0]), Double.parseDouble(component[1]));
        }

        return verts;
    }

    private static NodeList parseFieldNodes(Element placemark) {
        Element data = (Element)placemark.getElementsByTagName(DATA_TAG).item(0);
        Element schemaData = (Element)placemark.getElementsByTagName(SCHEMA_DATA_TAG).item(0);

        return schemaData.getElementsByTagName(SIMPLE_DATA_TAG);
    }

    private static SchemaMap loadSchema(Element schemaData) {
        SchemaMap schemaMap = new SchemaMap();

        NodeList fields = schemaData.getElementsByTagName(FIELD_TAG);

        for (int i = 0; i < fields.getLength(); i++) {
            Element fieldElement = (Element)fields.item(i);
            String type = fieldElement.getAttribute("type");
            String name = fieldElement.getAttribute("name");

            System.out.println("Var: " + name +" is " + type);

            schemaMap.put(name, parseSchemaType(type));
        }

        return schemaMap;
    }

    private static SimpleType parseSchemaType(String typeData) {
        switch (typeData) {
            case STRING_NAME:
                return SimpleType.STRING;
            case DOUBLE_NAME:
                return SimpleType.DOUBLE;
        }

        throw new RuntimeException("Bad schema type!");
    }
}
