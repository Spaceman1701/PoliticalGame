package map;

import gui.DrawPanel;
import gui.Window;
import kml.KMLLoader;
import math.Line;
import math.Vector2i;

import java.io.File;
import java.io.FileNotFoundException;

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
        if (!file.exists()) {
            throw new FileNotFoundException(location);
        }
        KMLLoader.loadKML(file);
    }

    public void generateMap() {
        System.out.println("generating map");
    }

    public static void main(String[] args) {
        MapGenerator mg = new MapGenerator();
    }
}
