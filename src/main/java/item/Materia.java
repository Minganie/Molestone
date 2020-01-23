package item;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to represent a materia, i.e. the thing you meld on equipment
 */
public class Materia extends Item {

    Materia(String icat2, String icat3, Document doc, Lid lid) throws Exception {
        super(icat2, icat3, doc, lid);
    }

    @Override
    void parseSpecificDetails(Elements details) throws Exception {
        Elements bss = details.select(".basic_bonus li");
        for(Element b : bss) {
            try {
                Bonus bonus = parseBonus(b.html());
                bonuses.add(bonus);
            } catch (PlannedException e) {
                throw new Exception("Can't seem to find the bonus for " + name + " with provided string " + b.html() + " because " + e.getMessage());
            }
        }

        Elements reqs = details.select("ul.db-view__info_text li");
        int i=0;
        for(Element req : reqs) {
            String s = req.text();
            try {
                meldItemLevel = parseItemLevel(req.text());
                ++i;
                if(i>1)
                    throw new Exception("More than one requirement for one materia: " + name);
            } catch (PlannedException e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    private Bonus parseBonus(String mumbo) throws PlannedException {
        Pattern pattern = Pattern.compile("^(.+)\\s\\+(\\d+)$");
        Matcher matcher = pattern.matcher(mumbo);
        String stat = null;
        Integer size = null;
        if (matcher.find())
        {
            stat = matcher.group(1);
            size = Integer.parseInt(matcher.group(2));
        }
        if(stat == null || size == null)
            throw new PlannedException("Can't seem to find the bonus for materia " + name + " with provided string '" + mumbo + "'");
        return new Bonus(stat, size);
    }

    private Integer parseItemLevel(String mumbo) throws PlannedException {
        Pattern pattern = Pattern.compile("^.+Item\\sLevel\\s(\\d+)$");
        Matcher matcher = pattern.matcher(mumbo);
        Integer lvl = null;
        if (matcher.find())
            lvl = Integer.parseInt(matcher.group(1));
        else
            throw new PlannedException("Can't seem to find the bonus for materia " + name + " with provided string '" + mumbo + "'");
        return lvl;
    }
}
