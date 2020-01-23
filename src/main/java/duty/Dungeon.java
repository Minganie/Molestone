package duty;

import org.jsoup.nodes.Document;
import util.Lid;

/**
 * Subclass of {@link Duty} used to represent dungeons
 */
public class Dungeon extends DutyWithFreeChests {
    /**
     * Constructor
     * @param doc Jsoup document of the Lodestone page for this dungeon
     * @param lid Lodestone id for this dungeon
     * @throws Exception for various parsing issues
     */
    Dungeon(Document doc, Lid lid) throws Exception {
        super(doc, lid);
    }

    /**
     * Utility toString method
     * @return A string like so "[DUN] Amdapor Keep (Regular) @ ae8a92122ec"
     */
    @Override
    public String toString() {
        return "[DUN] " + name + " (" + difficulty.toString() + ") @ " + lid.toString();
    }
}
