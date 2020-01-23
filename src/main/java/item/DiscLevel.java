package item;

/**
 * Class used to represent a class/job and a level (ex. WAR lvl 40)
 */
public class DiscLevel {
	private String discipline;
	private int level;
	
	DiscLevel(String discipline, int level) {
		this.discipline = discipline;
		this.level = level;
	}

    /**
     * Getter for the class/job
     * @return Class or job
     */
	public String getDiscipline() {
		return discipline;
	}

    /**
     * Getter for the level of the class/job
     * @return Level
     */
	public int getLevel() {
		return level;
	}

    /**
     * Utility toString method
     * @return A string like so "WAR Lv. 40"
     */
	public String toString() {
		return discipline + " Lv. " + level;
	}
}
