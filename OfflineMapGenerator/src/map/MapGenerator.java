package map;

import gui.Window;
import kml.KMLData;
import kml.feature.Boundary;
import kml.feature.Feature;
import kml.feature.Polygon;
import math.Line;
import math.Vector2d;
import math.Vector2i;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Ethan on 8/10/2016.
 */
public class MapGenerator {
    public static final int RES_X = 800;
    public static final int RES_Y = 800;

    private Window win;

    public MapGenerator() {
        win = new Window(RES_X, RES_Y, this);
    }

    public void loadNewKMLMap(String location) throws FileNotFoundException {
        File file = new File(location);
        System.out.println("Selected file as kml: " + location);
        if (!file.exists()) {
            throw new FileNotFoundException(location);
        }
        try {
            KMLData d = KMLData.loadKML(file);

            int a = 0;
            for (Feature f : d.getFeatures()) {

                for (Polygon p : f.getPolygons()) {

                    Boundary b = p.getOuterBoundary();
                    System.out.println(b.getVerticies().length);
                    for (int i = 0; i < b.getVerticies().length; i+=2) {
                        Vector2i start = projectGeography(b.getVerticies()[i]);

                        Vector2i end = null;

                        if (i + 1 < b.getVerticies().length) {
                            end = projectGeography(b.getVerticies()[i + 1]);
                        } else {
                            end = projectGeography(b.getVerticies()[0]);
                        }
                        if (i < 5000000) {
                            Line l = new Line(start, Vector2i.add(end, new Vector2i(0, 0)));
                            Line l2 = new Line(start, Vector2i.add(start, new Vector2i(1, 1)));
                            //win.getDrawPanel().drawLine(l);
                            win.getDrawPanel().drawLine(l);
                        }
                    }
                }
                a++;
            }

            win.getDrawPanel().update();
        } catch (Exception e) {
            System.err.println("Error reading KML");
            e.printStackTrace();
        }
    }

    private Vector2i projectGeography(Vector2d point) {
        double xMin = -130;
        double xMax = -50;

        double yMin = 20;
        double yMax = 50;

        double xRangeOld = -50 + 130;
        double yRangeOld = 50 - 20;

        double xRangeNew = 800;
        double yRangeNew = 400;

        double newX = (((point.x - xMin) * xRangeNew) / xRangeOld);
        double newY = (-(((point.y - yMin) * yRangeNew) / yRangeOld)) + yRangeNew;

        //newX = point.x + 400;
        //newY = point.y + 400;

        return new Vector2i(newX, newY);
    }

    public void generateMap() {
        System.out.println("generating map");
    }

    public static void main(String[] args) {
        MapGenerator mg = new MapGenerator();
    }
}
