package item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;

import java.util.Collections;

/**
 * Class used to represent a soul crystal (equipped to get a job as opposed to a class)
 */
public class SoulCrystal extends Item {

    SoulCrystal(String icat2, String icat3, Document doc, Lid lid) throws Exception {
        super(icat2, icat3, doc, lid);
    }

    @Override
	void parseSpecificDetails(Element details) throws Exception {
		disciplines = details.select(".db-view__item_equipment__class").html().trim();

		Elements bss = details.select(".db-view__basic_bonus li");
        for(Element b : bss) {
            Bonus bonus;
            try {
                bonus = Equipable.parseBonus(b.html());
                bonuses.add(bonus);
            } catch (PlannedException e) {
                throw new Exception("Failed to get bonuses for " + name + " because " + e.getMessage());
            }
        }

		note = details.select(".db-view__help_text").html();
	}
}
