package duty;

import org.jsoup.nodes.Document;
import util.Lid;

/**
 * Simple factory for duties
 */
public class DutyFactory {
    /**
     * Factory method for getting a {@link Duty}
     * @param doc Jsoup document for the Lodestone page for this duty
     * @param lid Lodestone id for this duty
     * @return Appropriate instance of {@link Duty}
     * @throws Exception for various parsing issues
     */
    public static Duty getDuty(Document doc, Lid lid) throws Exception {
        String type = doc.select(".db-view__detail__content_type").text();
        if(type.equalsIgnoreCase("Dungeons"))
            return new Dungeon(doc, lid);
        if(type.equalsIgnoreCase("Guildhests"))
            return new Guildhest(doc, lid);
        if(type.equalsIgnoreCase("Trials"))
            return new Trial(doc, lid);
        if(type.equalsIgnoreCase("Raids"))
            return new Raid(doc, lid);
        if(type.equalsIgnoreCase("PvP"))
            return new PvP(doc, lid);
        else throw new Exception("Can't seem to figure out which type of duty '" + type + "' is");
    }
}
