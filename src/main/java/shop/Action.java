package shop;

/**
 * Class for a Free Company action you might buy
 */
public class Action {
    private String name;
    private String licon;
    private String effect;
    private int durationHours;

    /**
     * Constructor
     * @param name Name of the action
     * @param licon Lodestone url for the icon of this action
     * @param effect Description of the effect of this action
     * @param durationHours Number of hours this action will last
     */
    public Action(String name, String licon, String effect, int durationHours) {
        this.name = name;
        this.licon = licon;
        this.effect = effect;
        this.durationHours = durationHours;
    }

    /**
     * Getter for the name of the action
     * @return Name of the action
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the Lodestone url for the icon of this action
     * @return Lodestone url for the icon of this action
     */
    public String getLicon() {
        return licon;
    }

    /**
     * Getter for the description of the effect of this action
     * @return Description of the effect of this action
     */
    public String getEffect() {
        return effect;
    }

    /**
     * Getter for the number of hours this action will last
     * @return Number of hours this action will last
     */
    public int getDurationHours() {
        return durationHours;
    }

    /**
     * Utility toString method
     * @return A pretty string like "The Heat of Battle"
     */
    @Override
    public String toString() {
        return name;
    }
}
