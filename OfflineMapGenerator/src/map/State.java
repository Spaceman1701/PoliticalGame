package map;

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

    private List<Layer> layers;

    private Layer baseLayer;

    public State(String name, int stateId) {
        this.name = name;
        this.stateId = stateId;

        type = getTypeFromName(name);

        layers = new ArrayList<>();
    }

    public void addLayer(Layer l) {
        layers.add(l);
    }

    public void calculateBaseLayer() {
        if (layers.isEmpty()) {
            return;
        }
        if (layers.size() == 1) {
            baseLayer = layers.get(0);
            return;
        }

        //Maybe use linked list or a queue and do this:
        //Dequeue the top 2 layers on the list and compute intersections,
        //create a layer from the results and add it to the queue,
        //repeat until thr queue has only one layer, this ought to be the base layer

        LinkedList<Layer> layersList = new LinkedList<>(layers);

        while (layersList.size() > 1) {
            Layer l1 = layersList.poll();
            Layer l2 = layersList.poll();

            Layer resultLayer = new Layer("result");

            for (Region r1 : l1.getRegions()) {
                for (Region r2 : l2.getRegions()) {
                    if (r1.isRoughCollision(r2)) {
                        List<Region> result = r1.getFineCollision(r2);
                        if (result != null && result.size() != 0) {
                            resultLayer.addRegions(result);
                            //TODO: Handle associations: the main regions need to know what base regions they are made of
                        }
                    }
                }
            }

            if (resultLayer.getRegions().size() != 0) {
                layersList.add(resultLayer);
            }
        }

        baseLayer = layersList.get(0);
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