package util;

/**
 * Holder for a zone and a set of coordinates
 */
public class ZonedCoords {
    private Coords coords;
    private String zone;

    /**
     * Constructor
     * @param coords Coordinates
     * @param zone Name of the zone
     */
    public ZonedCoords(Coords coords, String zone) {
        this.coords = coords;
        this.zone = Zone.getActualZone(zone);
    }

    /**
     * Getter for the coordinates
     * @return {@link Coords}
     */
    public Coords getCoords() {
        return coords;
    }

    /**
     * Getter for the name of the zone
     * @return Name of the zone
     */
    public String getZone() {
        return zone;
    }

    /**
     * Utility toString method
     * @return A pretty string like "[11.5, 13.6] in Central Thanalan"
     */
    @Override
    public String toString() {
        return coords.toString() + " in " + zone;
    }

    /**
     * Utility equals method
     * @param o other object
     * @return Is it the same zone and coords?
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof ZonedCoords) {
            ZonedCoords oc = (ZonedCoords) o;
            return zone.equals(oc.zone) && coords.equals(oc.coords);
        } else
            return false;
    }


    /**
     * Utility hashCode method
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return coords.hashCode() * zone.hashCode();
    }
}
