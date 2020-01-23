package item;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import util.Lid;

/**
 * Class used to represent a piece of armor: head, chest, etc. (excludes shield)
 */
public class Armor extends Equipable {

    Armor(String icat2, String icat3, Document doc, Lid lid) throws Exception {
        super(icat2, icat3, doc, lid);
    }

    @Override
	void parseSpecificDetails(Elements details) throws Exception {
		// SPEC
		Elements specs = details.select(".db-view__item_spec .sys_nq_element");

        String def = specs.select(".db-view__item_spec__value--armor strong").html();
        String mdef = specs.select(".db-view__item_spec__value--last strong").html();

        defense = Integer.parseInt(def);
        magicDefense = Integer.parseInt(mdef);

//        System.out.println(String.format("Item %40s has defense of %4d def at %4d mdef",
//                name,
//				defense,
//				magicDefense));

		super.parseSpecificDetails(details);
		
	}
}
