package map;

import gui.DrawPanel;
import math.Line;
import math.Vector2d;
import math.Vector2i;

import java.util.*;

/**
 * Created by Ethan on 8/11/2016.
 */
public class State {
    private static final String WASHINGTON_DC = "District of Columbia";

    private static final String[] TERRITORIES = {
            "Commonwealth of the Northern Mariana Islands",
            "Guam",
            "United States Virgin Islands",
            "American Samoa",
            "Puerto Rico"
    };

    private final String name;
    private final int stateId;

    private final StateType type;

    private Map<String, Layer> layers;

    private Layer baseLayer;

    public State(String name, int stateId) {
        this.name = name;
        this.stateId = stateId;

        type = getTypeFromName(name);

        layers = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Collection<Layer> getLayers() {
        return layers.values();
    }

    public boolean hasLayer(String name) {
        return layers.containsKey(name);
    }

    public Layer getLayer(String name) {
        return layers.get(name);
    }

    public void addLayer(String name, Layer l) {
        layers.put(name, l);
    }

    public void calculateBaseLayer() {
        System.out.println("Calculating base layer");
        if (layers.isEmpty()) {
            return;
        }
        if (layers.values().size() == 1) {
            baseLayer = (Layer)layers.values().toArray()[0];
            return;
        }

        //Maybe use linked list or a queue and do this:
        //Dequeue the top 2 layers on the list and compute intersections,
        //create a layer from the results and add it to the queue,
        //repeat until thr queue has only one layer, this ought to be the base layer

        LinkedList<Layer> layersList = new LinkedList<>(layers.values());
        //System.out.println(layersList.size());

        while (layersList.size() > 1) {
            Layer l1 = layersList.poll();
            Layer l2 = layersList.poll();
            if (l1 == null || l2 == null) {
                System.out.println("layers are null");
            }
            //System.out.println(layersList.size());

            Layer resultLayer = new Layer("result");
            int regionNum = 0;
            int regionNum2 = 0;
            for (Region r1 : l1.getRegions()) {
                //System.out.println(regionNum + " / " + l1.getRegions().size());
                regionNum++;
                for (Region r2 : l2.getRegions()) {
                    //System.out.println(regionNum2 + " / " + l2.getRegions().size());
                    regionNum2++;
                    if (true) {
                        //System.out.println("rough collision found");
                        Intersection intersection = Intersection.calculateIntersectionSM(r2, r1);
                        if (intersection != null && !intersection.isEmpty()) {
                            //System.out.println("Intersection found!");
                            for (Region sr : intersection.getSharedSubRegion()) {
                                sr.addAssociation(r1);
                                sr.addAssociation(r2);
                            }
                            /*
                            for (Region r1sr : intersection.getR1SubRegion()) {
                                r1sr.addAssociation(r1);
                            }
                            for (Region r2sr : intersection.getR2SubRegion()) {
                                r2sr.addAssociation(r2);
                            }
                            */
                            resultLayer.addRegions(intersection.getSharedSubRegion());
                        }
                    }
                }
            }

            //if (resultLayer.getRegions().size() != 0) {
                //System.out.println("Results layer has regions!");
                layersList.add(resultLayer);
            //}
        }

        baseLayer = layersList.get(0);
        System.out.println(getName() +" done with " + baseLayer.getRegions().size() + " subregions!");
    }

    public void drawState(DrawPanel drawPanel) {
        if (baseLayer == null) {
            return;
        }
        for (Region r : baseLayer.getRegions()) {
            for (int i = 0; i < r.getPolygon().length; i+=2) {
                Segment s = Segment.getSegment(r.getPolygon(), i);
                Vector2i start = MapGenerator.projectGeography(s.getStart());
                Vector2i end = MapGenerator.projectGeography(s.getEnd());
                drawPanel.drawLine(new Line(start, Vector2i.add(start, new Vector2i(1,1))));
            }
        }
    }

    public int getStateId() {
        return stateId;
    }


    public static StateType getTypeFromName(String name) {
        if (name.equals(WASHINGTON_DC)) {
            return StateType.WASHINGTON_DC;
        }

        for (String territory : TERRITORIES) {
            if (name.equals(territory)) {
                return StateType.UNINCORPORATED;
            }
        }

        return StateType.STATE;
    }
}