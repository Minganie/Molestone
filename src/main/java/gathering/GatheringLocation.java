package gathering;

import java.util.Objects;

/**
 * Class used to represent one gathering location, i.e. a group of gathering nodes in the game
 */
public class GatheringLocation {
    private String region;
    private String zone;
    private String area;
    private int level;
    private boolean timeLimited;

    /**
     * Constructor
     * @param region Region (i.e. Thanalan)
     * @param zone Zone (i.e. Central Thanalan)
     * @param area Area (i.e. Spineless Basin)
     * @param level Level of the gathering nodes
     * @param timeLimited Is this location only present at certain times of the day?
     */
    public GatheringLocation(String region, String zone, String area, int level, boolean timeLimited) {
        this.region = region;
        this.zone = zone;
        this.area = area;
        this.level = level;
        this.timeLimited = timeLimited;
    }

    /**
     * Getter for the region (i.e. Thanalan)
     * @return Region (i.e. Thanalan)
     */
    public String getRegion() {
        return region;
    }

    /**
     * Getter for the zone (i.e. Central Thanalan)
     * @return Zone (i.e. Central Thanalan)
     */
    public String getZone() {
        return zone;
    }

    /**
     * Getter for the area (i.e. Spineless Basin)
     * @return Area (i.e. Spineless Basin)
     */
    public String getArea() {
        return area;
    }

    /**
     * Getter for the level of this gathering location
     * @return Level of this gathering location
     */
    public int getLevel() {
        return level;
    }

    /**
     * Getter for whether this location is only present at certain times of the day?
     * @return Is this location only present at certain times of the day?
     */
    public boolean isTimeLimited() {
        return timeLimited;
    }

    /**
     * Utility equals method
     * @param o Other object
     * @return Is it the same gathering location?
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof GatheringLocation) {
            GatheringLocation other = (GatheringLocation) o;
            return region.equals(other.region)
                    && zone.equals(other.zone)
                    && area.equals(other.area)
                    && level == other.level
                    && timeLimited == other.timeLimited;
        } else
            return false;
    }

    /**
     * Utility hashCode method
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(region, zone, area, level, timeLimited);
    }

    /**
     * Utility toString method
     * @return A pretty String like "Lv. 5 Hammerlea (Western Thanalan - Thanalan)"
     */
    @Override
    public String toString() {
        return "Lv. " + level + " " + area + (timeLimited ? "*" : "") + " (" + zone + " - " + region + ")";
    }
}
