package util;

/**
 * Utility class to hold a count of "something"
 */
public class StringCountBag {
    private String thingy;
    private int count;

    /**
     * Constructor
     * @param thingy "Something"
     * @param count Count
     */
    public StringCountBag(String thingy, int count) {
        this.thingy = thingy;
        this.count = count;
    }

    /**
     * Getter for your "something"
     * @return "Something"
     */
    public String getThingy() {
        return thingy;
    }

    /**
     * Getter for the count of "something"
     * @return Count
     */
    public int getCount() {
        return count;
    }
}
