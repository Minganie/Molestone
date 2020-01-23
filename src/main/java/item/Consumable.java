package item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to represent a consumable (ex. potion, food)
 */
public class Consumable extends Item {

    Consumable(String icat2, String icat3, Document doc, Lid lid) throws Exception {
        super(icat2, icat3, doc, lid);
    }

    @Override
	void parseSpecificDetails(Elements details) throws Exception {
		note = details.select(".db-view__info_text").html();
        Elements effectsEl = details.select("ul.sys_nq_element > li");
        for(Element li : effectsEl) {
            effects.add(li.html());
        }

        String recastString = details.select("div.sys_nq_element:nth-child(2) > div:nth-child(1) > strong:nth-child(1)").html();
        recast = parseRecast(recastString);

//        System.out.println("Item " + name + " has a note of " + note.substring(0, 20) + "...");
//        System.out.println("Item " + name + " has an effects of " + effects);
//        System.out.println("Item " + name + " has an recast of " + recast + "s");
	}

	private Integer parseRecast(String mumbo) throws Exception {
	    int h = 0;
	    int m = 0;
	    int s = 0;
	    try {
            Pattern pattern = Pattern.compile("(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?");
            Matcher matcher = pattern.matcher(mumbo);
            if (matcher.find())
            {
                if(matcher.group(3) != null)
                    s = Integer.parseInt(matcher.group(3));
                if(matcher.group(2) != null)
                    m = Integer.parseInt(matcher.group(2));
                if(matcher.group(1) != null)
                    h = Integer.parseInt(matcher.group(1));
            }
        } catch (Exception e) {
	        throw new Exception("Can't seem to parse recast time for item " + name + " with timer string " + mumbo);
        }
        return h * 60 * 60 + m * 60 + s;
    }

}
