package util;

/**
 * Utility class used to represent a NPC: a mob with a name and a Lodestone id
 */
public class NPC {
    /**
     * Name
     */
    private String name;
    /**
     * Lodestone id
     */
    private Lid lid;
    /**
     * Name of the area where the NPC is (area, not zone as in the db; for instance "Ul'dah - Steps of Nald" rather
     * than "Ul'dah")
     */
    private String area;
    /**
     * Coordinates of the NPC
     */
    private Coords coords;

    /**
     * Constructor
     * @param name Name of the NPC
     * @param lid Lodestone id of the NPC
     */
    public NPC(String name, Lid lid) {
        this.name = name;
        this.lid = lid;
    }

    /**
     * Constructor for a NPC with coordinates, i.e. a quest giver
     * @param name Name of the NPC
     * @param lid Lodestone id
     * @param area Area of the NPC
     * @param coords Coordinates of the NPC
     */
    public NPC(String name, Lid lid, String area, Coords coords) {
        this.name = name;
        this.lid = lid;
        this.area = area;
        this.coords = coords;
    }

    /**
     * Utility toString method
     * @return String like so "Fraeloef @ 811a94693d5"
     */
    @Override
    public String toString() {
        return name + " @ " + lid.toString();
    }

    /**
     * Getter for the name
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the Lodestone id
     * @return Lodestone id
     */
    public Lid getLid() {
        return lid;
    }

    /**
     * Getter for the area where the NPC is (area, not zone as in the db; for instance "Ul'dah - Steps of Nald" rather
     * than "Ul'dah")
     * @return Area
     */
    public String getArea() {
        return area;
    }

    /**
     * Getter for the coordinates of the NPC
     * @return Coordinates
     */
    public Coords getCoords() {
        return coords;
    }

    /**
     * Utility equals method
     * @param o Other object
     * @return Is it the same NPC?
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof NPC) {
            NPC n = (NPC) o;
            boolean ar = (this.area == null && n.area == null) || (this.area != null && n.area != null);
            boolean co = (this.coords== null && n.coords == null) || (this.coords != null && n.coords != null);
            return this.name.equals(n.name)
                    && this.lid.equals(n.lid)
                    && ar
                    && co;
        } else
            return false;
    }

    /**
     * Utility hashCode method
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return name.hashCode() + lid.hashCode();
    }
}
