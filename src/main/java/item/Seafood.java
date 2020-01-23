package item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;

/**
 * Class used to represent something you fished; not currently used, but here just in case Melody decided to parse
 * "Can be placed in aquarium" info later
 */
public class Seafood extends Material {
    Seafood(String cat2, String cat3, Document doc, Lid lid) throws Exception {
        super(cat2, cat3, doc, lid);
    }


    @Override
    void parseSpecificDetails(Elements details) {
//        Elements info = details.select(".div.db-view__info_text:nth-child(2)");
//        if(!info.isEmpty()) {
//            for (Element li : info) {
//                String s = li.html();
//                if (s.contains("Convertible"))
//                    convertible = s.contains("Yes");
//                if (s.contains("Desynthesizable")) // this is wrong, need to parse "Culinarian too"
//                    desynthLevel = (s.contains("No") ? null : Float.parseFloat(li.select("span").html()));
//            }
//        }
        super.parseSpecificDetails(details);
    }
}
