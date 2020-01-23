package item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;

/**
 * Class used to represent a weapon
 */
public class Weapon extends Equipable {

    Weapon(String icat2, String icat3, Document doc, Lid lid) throws Exception {
        super(icat2, icat3, doc, lid);
    }

    @Override
    void parseSpecificDetails(Elements details) throws Exception {
        // SPEC
        Elements specs = details.select(".db-view__item_spec .sys_nq_element .db-view__item_spec__value");
        if (!cat3.contains("Secondary")) {
            Element phys = specs.get(0);
            Element aa = specs.get(1);
            Element del = specs.get(2);
            damage = Integer.parseInt(phys.select("strong").html());
            autoAttack = Float.parseFloat(aa.select("strong").html());
            delay = Float.parseFloat(del.select("strong").html());
        }
        if (!cat3.contains("Secondary") && specs.isEmpty())
            throw new Exception("Something funky happened to " + name + " about his dmg specs");

        super.parseSpecificDetails(details);
    }
}
