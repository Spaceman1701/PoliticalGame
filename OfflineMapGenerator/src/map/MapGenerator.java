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

            for (Feature f : d.getFeatures()) {
                System.out.println(f.getName());

                for (Polygon p : f.getPolygons()) {
                    Boundary b = p.getOuterBoundary();

                    System.out.println(b.getVerticies().length);
                    for (int i = 0; i < b.getVerticies().length - 1; i+=2) {
                        Vector2i start = Vector2i.add(new Vector2i(b.getVerticies()[i]), new Vector2i(0, 0));
                        Vector2i end = Vector2i.add(new Vector2i(b.getVerticies()[i + 1]), new Vector2i(0, 0));
                        start.scale(1, -1);
                        end.scale(1, -1);
                        win.getDrawPanel().drawLine(new Line(start, end));
                    }
                }

            }

            win.getDrawPanel().update();
        } catch (Exception e) {
            System.err.println("Error reading KML");
            e.printStackTrace();
        }
    }

    public void generateMap() {
        System.out.println("generating map");
    }

    public static void main(String[] args) {
        MapGenerator mg = new MapGenerator();
    }
}
