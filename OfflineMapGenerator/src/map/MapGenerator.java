package map;

import gui.Window;
import kml.KMLData;
import kml.feature.Boundary;
import kml.feature.Feature;
import kml.feature.Polygon;
import math.Line;
import math.Vector2d;
import math.Vector2i;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ethan on 8/10/2016.
 */
public class MapGenerator {
    public static final int RES_X = 1280;
    public static final int RES_Y = 720;

    private static final String STATE_MAP_LOCATION = "res/state_map.kml";

    private Window win;

    private ArrayList<KMLData> kmlMaps;

    private FullMap nation;

    public MapGenerator() {
        win = new Window(RES_X, RES_Y, this);
        kmlMaps = new ArrayList<>();
        loadStateMap();
    }

    private void loadStateMap() {
        try {
            KMLData stateMap = KMLData.loadKML(new File(STATE_MAP_LOCATION));
            ArrayList<State> states = new ArrayList<>();
            int state = 0;
            int dc = 0;
            int territory = 0;
            for (Feature f : stateMap.getFeatures()) {
                states.add(new State(f.getName(), Integer.parseInt((String)f.getField("STATEFP").getValue())));
                StateType type = State.getTypeFromName(f.getName());
                if (type == StateType.STATE) {
                    state++;
                } else if (type == StateType.UNINCORPORATED) {
                    territory++;
                } else {
                    dc++;
                }
            }

            System.out.println(state +" States, " + territory +" Territories, " + dc +" Washington DCs");
            drawKMl(stateMap);

            nation = new FullMap(states);

        } catch (Exception e) {
            System.err.println("problem with state map!");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void drawKMl(KMLData kml) {
        for (Feature f : kml.getFeatures()) {
            for (Polygon p : f.getPolygons()) {
                Boundary b = p.getOuterBoundary();
                for (int i = 0; i < b.getVerticies().length - 1; i+=1) {
                    Vector2i start = projectGeography(b.getVerticies()[i]);

                    Vector2i end = null;

                    if (i + 1 < b.getVerticies().length) {
                        end = projectGeography(b.getVerticies()[i + 1]);
                    } else {
                        end = projectGeography(b.getVerticies()[0]);
                    }
                    Line l = new Line(start, Vector2i.add(end, new Vector2i(0, 0)));
                    win.getDrawPanel().drawLine(l);
                }
            }
        }
        win.getDrawPanel().update();
    }

    public void loadNewKMLMap(File file) {
        System.out.println("Selected file as kml: " + file.getAbsolutePath());
        try {
            KMLData d = KMLData.loadKML(file);
            kmlMaps.add(d);
            drawKMl(d);
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

        double xRangeNew = 1280;
        double yRangeNew = 640;

        double newX = (((point.x - xMin) * xRangeNew) / xRangeOld);
        double newY = (-(((point.y - yMin) * yRangeNew) / yRangeOld)) + yRangeNew;

        newY += 40;
        newX += 150;
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
