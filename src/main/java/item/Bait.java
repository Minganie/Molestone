package item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.Lid;

/**
 * Class used to represent a Bait, cat3 "Fishing tackle" on the Lodestone
 */
public class Bait extends Item {

    Bait(String icat2, String icat3, Document doc, Lid lid) throws Exception {
        super(icat2, icat3, doc, lid);
    }

    @Override
    void parseSpecificDetails(Element details) {
        note = details.select(".db-view__help_text").text();
//        System.out.println("Bait " + name + " has a note of " + note);
    }
}
