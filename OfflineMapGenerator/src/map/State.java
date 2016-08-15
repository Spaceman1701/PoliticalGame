package map;

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



    public State(String name, int stateId) {
        this.name = name;
        this.stateId = stateId;

        type = getTypeFromName(name);
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