package kml;

import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ethan on 8/10/2016.
 */
public class KMLLoader {


    public static void loadKML(File file) {
        Kml[] kml = null;
        try {
            kml = Kml.unmarshalFromKmz(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Feature f = kml[0].getFeature();
        System.out.println(f.getDescription());
    }
}
