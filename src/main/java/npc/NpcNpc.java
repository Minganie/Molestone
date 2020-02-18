package npc;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Coords;
import util.JsoupUtils;
import util.Lid;
import util.ZonedCoords;

public class NpcNpc extends Npc {
    protected NpcNpc(String cat, Lid lid) throws Exception {
        super(cat, lid);
    }

    @Override
    void parseDetails(Document doc) throws Exception {
        Element loc = doc.selectFirst("tr:matches(^Location$) + tr");
        String z = JsoupUtils.firstNonEmptyTextNode(loc.selectFirst("td > ul > li"));
        String c = JsoupUtils.firstNonEmptyTextNode(loc.selectFirst("td > ul > li > ul > li"));
        location = new ZonedCoords(Coords.parseCoords(c), z);

        Elements avQuests = doc.select("table:matches(^Available Quests$) + table > tbody > tr a.db_popup");
        for(Element avQuest : avQuests) {
            availableQuests.add(Lid.parseLid(avQuest.attr("href")));
        }

        Elements relQuests = doc.select("table:matches(^Related Quests$) + table > tbody > tr a.db_popup");
        for(Element relQuest : relQuests) {
            relatedQuests.add(Lid.parseLid(relQuest.attr("href")));
        }
    }
}
