package util;

/**
 * Utility class used to hold a an action name and its icon
 */
public class ActionIconBag {
    /**
     * Action name
     */
    private String name;
    /**
     * Action icon
     */
    private String icon;

    /**
     * Constructor
     * @param name Name of the action
     * @param icon Icon of the action
     */
    public ActionIconBag(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    /**
     * Getter for the name
     * @return Name of the action
     */
    public String getName() {
        return name;
    }

    /** Getter for the icon
     * @return Icon url
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Utility toString method
     * @return String like so "Cast Glamour @https://img.fin..."
     */
    @Override
    public String toString() {
        return name + " @" + icon.substring(0,15) + "...";
    }

    /**
     * Utility equals method
     * @param obj Other object
     * @return Is it the same ActionIconBag?
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ActionIconBag))
            return false;
        ActionIconBag other = (ActionIconBag) obj;
        return name.equals(other.name) && icon.equals(other.icon);
    }

    /**
     * Utility hashCode method
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return name.hashCode() + icon.hashCode();
    }
}