package util;

/**
 * Holder for a Lodestone id and a quality
 */
public class LidQBag {
    private Lid lid;
    private boolean hq;

    /**
     * Constructor
     * @param lid Lodestone id
     * @param hq Is it high quality?
     */
    public LidQBag(Lid lid, boolean hq) {
        this.lid = lid;
        this.hq = hq;
    }

    /**
     * Getter for the Lodestone id
     * @return Lodestone id
     */
    public Lid getLid() {
        return lid;
    }

    /**
     * Getter for whether it is high quality
     * @return Is it high quality?
     */
    public boolean isHq() {
        return hq;
    }
}
