package duty;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.Lid;

/**
 * Class used to represent a Trial
 */
public class Trial extends DifficultyDuty {

    /**
     * Constructor
     * @param doc Jsoup document for the Lodestone page for this duty
     * @param lid Lodestone id
     * @throws Exception for various parsing issues
     */
    Trial(Document doc, Lid lid) throws Exception {
        super(doc, lid);
    }

    /**
     * Trial-specific parsing name and difficulty; assigns the appropriate value to fields {@link Trial#name} and {@link Trial#difficulty}
     * @param nameAndDifficulty String like so "Amdapor Keep (Hard)" or "Solemn Trinity"
     */
    @Override
    void parseNameAndDifficulty(String nameAndDifficulty) {
        difficulty = Difficulty.get(nameAndDifficulty);
        name = trimName(nameAndDifficulty);
    }

    /**
     * Trial-specific parsing of details; encounters
     * @param doc Jsoup document for the Lodestone page of this duty
     * @throws Exception for various parsing issues
     */
    @Override
    void parseDetails(Document doc) throws Exception {
        // I assume a trial can only have *one* encounter... until Square proves me wrong
        Element box = doc.select("div.db-view__data__inner").first();
        encounters.add(new Encounter(box, 0));
    }

    /**
     * Utility toString method
     * @return A string like so "[TRI] The Howling Eye (Hard) @ b74b4328a17"
     */
    @Override
    public String toString() {
        return "[TRI] " + name + " (" + difficulty.toString() + ") @ " + lid.toString();
    }

    /**
     * Trial-specific printing of full details to standard output
     */
    @Override
    public void print() {
        System.out.println(name + " (" + difficulty.toString() + ") @ " + lid.toString());
        System.out.println("  Bosses:");
        for(Encounter encounter : encounters) {
            encounter.print();
        }
    }
}
