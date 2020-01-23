package duty;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.Lid;

/**
 * Class used to represent a Guildhest
 */
public class Guildhest extends Duty {

    /**
     * Constructor
     * @param doc Jsoup document for the Lodestone page for this duty
     * @param lid Lodestone id
     * @throws Exception for various parsing issues
     */
    Guildhest(Document doc, Lid lid) throws Exception {
        super(doc, lid);
    }

    /**
     * Guildhest-specific parsing of name and difficulty: there is only the name; assigns the appropriate value to
     * the field {@link Guildhest#name}
     * @param nameAndDifficulty String like so "Amdapor Keep (Hard)" or "Solemn Trinity"
     */
    @Override
    void parseNameAndDifficulty(String nameAndDifficulty) {
        name = nameAndDifficulty;
    }

    /**
     * Guildhest-specific parsing of details; assigns appropriate values to the fields {@link Guildhest#completionXp},
     * {@link Guildhest#completionGil}, {@link Guildhest#bonusXp} and {@link Guildhest#bonusGil}
     * @param doc Jsoup document for the Lodestone page of this duty
     */
    @Override
    void parseDetails(Document doc) {
        String completionXpString = doc.select("div.db-view__data__inner:nth-child(3) > div:nth-child(1) > ul:nth-child(2) > li:nth-child(1) > div:nth-child(2)").text();
        String completionGilString = doc.select("div.db-view__data__inner:nth-child(3) > div:nth-child(1) > ul:nth-child(2) > li:nth-child(2) > div:nth-child(2)").text();
        String bonusXpString = doc.select("div.db-view__data__inner:nth-child(4) > div:nth-child(1) > ul:nth-child(2) > li:nth-child(1) > div:nth-child(2)").text();
        String bonusGilString = doc.select("div.db-view__data__inner:nth-child(4) > div:nth-child(1) > ul:nth-child(2) > li:nth-child(2) > div:nth-child(2)").text();
        completionXp = Integer.parseInt(completionXpString);
        completionGil = Integer.parseInt(completionGilString);
        bonusXp = Integer.parseInt(bonusXpString);
        bonusGil = Integer.parseInt(bonusGilString);
    }

    /**
     * Printing of the full details for this Guildhest to standard output
     */
    @Override
    public void print() {
        System.out.println(name + " @ " + lid.toString());
        System.out.println("  Completion: " + completionXp + " xp + " + completionGil + " gil" );
        System.out.println("  Bonus     : " + bonusXp + " xp + " + bonusGil + " gil" );
    }

    /**
     * Utility toString method
     * @return A string like so "[HES] Solemn Trinity @ 7a556805107"
     */
    @Override
    public String toString() {
        return "[HES] " + name + " @ " + lid.toString();
    }
}
