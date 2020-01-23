package util;

/**
 * Utility class used to hold a Lid and a number (for example, 3 x 84ccf1c33bb)
 */
public class LidCountBag {
    /**
     * Lodestone id
     */
    private Lid lid;
    /**
     * Number
     */
    private int count;

    /**
     * Constructor
     * @param lid Lodestone id
     * @param count Number
     */
    public LidCountBag(Lid lid, int count) {
        this.lid = lid;
        this.count = count;
    }

    /**
     * Getter for the Lodestone id
     * @return Lodestone id
     */
    public Lid getLid() {
        return lid;
    }

    /**
     * Getter for the count
     * @return Count
     */
    public int getCount() {
        return count;
    }

    /**
     * Utility toString method
     * @return String like so "3x84ccf1c33bb"
     */
    @Override
    public String toString() {
        return count + "x" + lid;
    }

    /**
     * Utility equals method
     * @param obj Other object
     * @return Is it the same LidCountBag?
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LidCountBag))
            return false;
        LidCountBag other = (LidCountBag) obj;
        return lid.equals(other.lid) && count == other.count;
    }

    /**
     * Utility hashCode method
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return lid.hashCode()*count;
    }
}
