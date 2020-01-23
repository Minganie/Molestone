package duty;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.JsoupUtils;
import util.Lid;

import java.text.ParseException;

/**
 * Class used to represent a PvP duty
 */
public class PvP extends Duty {
    /**
     * Constructor
     * @param doc Jsoup document of the Lodestone page for this duty
     * @param lid Lodestone id
     * @throws Exception for various parsing issues
     */
    PvP(Document doc, Lid lid) throws Exception {
        super(doc, lid);
    }

    /**
     * PvP-specific parsing of name and difficulty: there is only the name; assigns the appropriate value to
     * the field {@link PvP#name}
     * @param nameAndDifficulty String like so "Amdapor Keep (Hard)" or "Solemn Trinity"
     */
    @Override
    void parseNameAndDifficulty(String nameAndDifficulty) {
        name = nameAndDifficulty;
    }

    /**
     * PvP-specific parsing of details; assigns the appropriate values to the fields
     * {@link PvP#pvpXpRank1}, {@link PvP#pvpXpRank2}, {@link PvP#pvpXpRank3},
     * {@link PvP#wolfMarksRank1}, {@link PvP#wolfMarksRank2}, {@link PvP#wolfMarksRank3}
     * @param doc Jsoup document for the Lodestone page of this duty
     * @throws ParseException for various parsing issues
     */
    @Override
    void parseDetails(Document doc) throws Exception {
        parseTokens(doc);
        Elements blocks = doc.select("div.db-view__data__inner");
        if(blocks.size() > 3 || blocks.size() < 2)
            throw new Exception("Can't figure out pvp rewards: it's neither winner/loser nor rank 1/2/3");
        int i = 1;
        for(Element block : blocks) {
            int xp = JsoupUtils.parseInt(block.selectFirst(".db-view__data__reward__list--pvp_exp .db-view__data__reward__list--value").text().trim());
            int marks = JsoupUtils.parseInt(block.selectFirst(".db-view__data__reward__list--pvp_point .db-view__data__reward__list--value").text().trim());
            switch (i) {
                case 1:
                    pvpXpRank1 = xp;
                    wolfMarksRank1 = marks;
                    break;
                case 2:
                    pvpXpRank2 = xp;
                    wolfMarksRank2 = marks;
                    break;
                default:
                    pvpXpRank3 = xp;
                    wolfMarksRank3 = marks;
                    break;
            }
            i++;
        }
    }

    /**
     * PvP-specific parsing of full details to standard output
     */
    @Override
    public void print() {
        System.out.println(name + " @ " + lid.toString());
        System.out.println("  Rank 1: " + pvpXpRank1 + " pvp xp + " + wolfMarksRank1 + " wolf marks");
        System.out.println("  Rank 2: " + pvpXpRank2 + " pvp xp + " + wolfMarksRank2 + " wolf marks");
        System.out.println("  Rank 3: " + pvpXpRank3 + " pvp xp + " + wolfMarksRank3 + " wolf marks");
    }

    /**
     * Parsing the number and type of tokens awarded for this duty; adds appropriate values to {@link PvP#tokens}
     * @param doc Jsoup document of the Lodestone page for this duty
     * @throws ParseException for various parsing issues
     */
    private void parseTokens(Document doc) throws ParseException {
        Elements tokenElements = doc.select("div.allagan_clear > div.allagan_box > div.value_box");
        for(Element tokenElement : tokenElements) {
            String tokenName = tokenElement.select(".txt_yellow.highlight").text();
            String tokenNumber = tokenElement.select(".value").text();
            tokens.put(tokenName, Integer.parseInt(tokenNumber));
        }
    }

    /**
     * Utility toString method
     * @return A string like so "[PVP] Astragalos @ b821bd06180"
     */
    @Override
    public String toString() {
        return "[PVP] " + name + " @ " + lid.toString();
    }
}
