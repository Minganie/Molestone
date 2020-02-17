package item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to represent a material, for example things you use for crafting
 */
public class Material extends Item {

    Material(String icat2, String icat3, Document doc, Lid lid) throws Exception {
        super(icat2, icat3, doc, lid);
    }

    /**
     * Hack constructor
     * @param name Name of the item
     * @param lid Lodestone id
     * @param cat2 cat2 as per the Lodestone
     * @param cat3 cat3 as per the Lodestone
     * @param licon Url of the icon on the Lodestone
     */
    Material(String name, Lid lid, String cat2, String cat3, String licon) {
        this.name = name;
        this.lid = lid;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.licon = licon;
    }

    @Override
	void parseSpecificDetails(Element details) {
        Elements texts = details.select(".db-view__info_text");

        for(Element txtEl : texts) {
            Elements effectsEl = txtEl.select("ul.sys_nq_element > li");
            Elements desynthEl = txtEl.select(".db-view__desynthesizable");

            // Effects node
            if (!effectsEl.isEmpty()) {
                for (Element li : effectsEl) {
                    effects.add(li.html());
                }

            // Desynth node
            } else if(!desynthEl.isEmpty()) {
                parseDesynth(txtEl);

            // Otherwise it's a note
            } else {
                note = extractText(txtEl);
            }
        }

//        System.out.println("Item " + name + " has a note of " + note);
//        System.out.println("Item " + name + " has effects: " + effects);
//        System.out.println("Item " + name + " is desynth " + desynthLevel + " by " + desynthClass);
	}

    private void parseDesynth(Element txtEl) {
	    String mumbo = txtEl.html();
        Pattern pattern = Pattern.compile("<span.+</span>(\\d+\\.\\d+).\\((.+)\\)");
        Matcher matcher = pattern.matcher(mumbo);
        if (matcher.find())
        {
            desynthLevel = Float.parseFloat(matcher.group(1));
            desynthClass = matcher.group(2);
        }
    }

    private String extractText(Element mumbo) {
//	    StringBuilder sb = new StringBuilder();
//	    for(TextNode txt : mumbo.textNodes()) {
//	        sb.append(txt.text());
//        }
//	    return sb.toString();
        return mumbo.text();
    }

}
