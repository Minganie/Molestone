package item;

/**
 * Class to represent a bonus: size and stat (ex. +4 STR)
 */
public class Bonus {
	private String stat;
	private int size;
	
	Bonus(String stat, int size) {
		this.stat = stat;
		this.size = size;
	}

    /**
     * Getter for the stat that is augmented
     * @return Stat that is augmented
     */
	public String getStat() {
		return stat;
	}

    /**
     * Getter for the size of the bonus
     * @return Size of the bonus
     */
	public int getSize() {
		return size;
	}

    /**
     * Utility toString method
     * @return A string like so "+4 STR"
     */
	public String toString() {
		return "+" + size + " " + stat;
	}

}
