package duty;

import java.util.Arrays;
import java.util.List;

/**
 * Simple class mostly to perform a type check on duty difficulty
 */
public class Difficulty {
    /**
     * Name of the difficulty (i.e. "Regular", "Hard", ...
     */
    private String name;
    /**
     * Sorting order of the difficulty; Regular {@literal <} Hard {@literal <} Extreme {@literal <} Savage {@literal <} Ultimate
     */
    private int order;

    /**
     * "Singleton" of difficulties; package-private
     */
    static final List<Difficulty> difficulties = Arrays.asList(
            new Difficulty("Regular", 0),
            new Difficulty("Hard", 1),
            new Difficulty("Extreme", 2),
            new Difficulty("Savage", 3),
            new Difficulty("Ultimate", 4)
    );

    /**
     * Static factory method to get a difficulty from the name of a duty
     * @param dutyName Name of the duty
     * @return Instance of {@link Difficulty}
     */
    public static Difficulty get(String dutyName) {
        if(dutyName.contains("Hard") || dutyName.contains("hard"))
            return difficulties.get(1);
        if(dutyName.contains("Extreme") || dutyName.contains("extreme"))
            return difficulties.get(2);
        if(dutyName.contains("Savage") || dutyName.contains("savage"))
            return difficulties.get(3);
        if(dutyName.contains("Ultimate") || dutyName.contains("ultimate"))
            return difficulties.get(4);
        else return difficulties.get(0);
    }

    /**
     * Private constructor of Difficulty
     * @param name Name of the difficulty
     * @param order Sort order of the difficulty
     */
    private Difficulty(String name, int order) {
        this.name = name;
        this.order = order;
    }

    /**
     * Utility toString method
     * @return A string like so "Hard"
     */
    @Override
    public String toString() {
        return name;
    }
}
