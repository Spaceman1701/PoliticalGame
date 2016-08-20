package map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 8/11/2016.
 */
public class FullMap {

    private ArrayList<State> states;

    public FullMap(ArrayList<State> states) {
        this.states = new ArrayList<>(states);
    }

    public State getState(int id) {
        for (State s : states) {
            if (s.getStateId() == id) {
                return s;
            }
        }
        return null;
    }

    public List<State> getStates() {
        return states;
    }
}
