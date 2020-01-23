package duty;

import org.jsoup.nodes.Document;
import util.Lid;

/**
 * Class used to represent a raid
 */
public class Raid extends DutyWithFreeChests {
    /**
     * Constructor
     * @param doc Jsoup document of the Lodestone page for this duty
     * @param lid Lodestone id
     * @throws Exception for various parsing issues
     */
    Raid(Document doc, Lid lid) throws Exception {
        super(doc, lid);
    }

    /**
     * Utility toString method
     * @return A string like so "[RAI] The Ridorana Lighthouse (Regular) @ 390fb10fd68"
     */
    @Override
    public String toString() {
        return "[RAI] " + name + " (" + difficulty.toString() + ") @ " + lid.toString();
    }
}
