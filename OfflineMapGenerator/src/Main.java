import gui.Window;
import map.MapGenerator;

/**
 * Created by Ethan on 8/9/2016.
 */
public class Main {
    public static final int RES_X = 700;
    public static final int RES_Y = 700;


    public static void main(String[] args) {
        MapGenerator mg = new MapGenerator();
        Window w = new Window(RES_X, RES_Y, mg);
    }
}
