package duty;

import org.jsoup.nodes.Document;
import util.Lid;

/**
 * Subclass of {@link Duty} for duties that can have different difficulties, i.e. {@link Trial}, {@link Dungeon} and {@link Raid}
 */
public abstract class DifficultyDuty extends Duty {

    /**
     * Constructor for a duty with difficulty; package-private
     * @param doc Jsoup document of the Lodestone page of the duty
     * @param lid Lodestone id of the duty
     * @throws Exception for various parsing issues
     */
    DifficultyDuty(Document doc, Lid lid) throws Exception {
        super(doc, lid);
    }

    /**
     * Static method to trim difficulty off of a duty name; package-private
     * @param nameAndDifficulty Name and difficulty like so "Amdapor Keep (Hard)"
     * @return Name of the duty only, like so "Amdapor Keep"
     */
    static String trimName(String nameAndDifficulty) {
        String[] diffs = new String[Difficulty.difficulties.size()];
        for(int i = 0; i < diffs.length; i++)
            diffs[i] = " (" + Difficulty.difficulties.get(i) + ")";
        for(String diff : diffs) {
            if(nameAndDifficulty.contains(diff)) {
                int l = diff.length();
                int beg = nameAndDifficulty.indexOf(diff);
                int end = beg+l;
                return nameAndDifficulty.substring(0, beg) + nameAndDifficulty.substring(end, nameAndDifficulty.length());
            }
        }
        return nameAndDifficulty;
    }
}
