package util;

/**
 * Holder for a number and a {@link LidQBag}
 */
public class LidQCountBag {
    private LidQBag lqb;
    private int n;

    /**
     * Constructor
     * @param lid Lodestone id
     * @param hq Is it high quality?
     * @param n Number
     */
    public LidQCountBag(Lid lid, boolean hq, int n) {
        lqb = new LidQBag(lid, hq);
        this.n = n;
    }

    /**
     * Getter for the Lodestone id
     * @return Lodestone id
     */
    public Lid getLid() {
        return lqb.getLid();
    }
    /**
     * Getter for whether it is high quality
     * @return Is it high quality?
     */
    public boolean isHq() {
        return lqb.isHq();
    }

    /**
     * Getter for the number
     * @return Number
     */
    public int getN() {
        return n;
    }
}
