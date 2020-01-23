package util;

public class ZonedCoords {
    private Coords coords;
    private String zone;

    public ZonedCoords(Coords coords, String zone) {
        this.coords = coords;
        this.zone = Zone.getActualZone(zone);
    }

    public Coords getCoords() {
        return coords;
    }

    public String getZone() {
        return zone;
    }

    @Override
    public String toString() {
        return coords.toString() + " in " + zone;
    }

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
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return coords.hashCode() * zone.hashCode();
    }
}
