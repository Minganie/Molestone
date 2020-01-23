package item;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import util.Lid;

/**
 * Class used to represent a shield (the piece of armor gladiators and paladins use)
 */
public class Shield extends Equipable {

    Shield(String icat2, String icat3, Document doc, Lid lid) throws Exception {
        super(icat2, icat3, doc, lid);
    }

    @Override
	void parseSpecificDetails(Elements details) throws Exception {
		// SPEC
		Elements specs = details.select(".db-view__item_spec .sys_nq_element");

        String strength = specs.select(".db-view__item_spec__value--shield strong").html();
        String rate = specs.select(".db-view__item_spec__value--last strong").html();

        blockStrength = Integer.parseInt(strength);
        blockRate = Integer.parseInt(rate);

		super.parseSpecificDetails(details);
	}
}
