package npc;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;

public class Enemy extends Npc {

    protected Enemy(String cat, Lid lid) throws Exception {
        super(cat, lid);
    }

    @Override
    void parseDetails(Document doc) throws Exception {
        Elements els = doc.select("tr:matches(^Spawn Location) + tr > td > ul > li");
        for(Element el : els) {
            spawnLocations.add(new SpawnLocation(el));
        }

        Element h3 = doc.selectFirst("h3:matchesOwn(Dropped Item)");
        Elements dItems = h3.parent().parent().select("table > tbody > tr > td a.db_popup");
        for (Element dItem : dItems) {
            droppedItems.add(Lid.parseLid(dItem.attr("href")));
        }

        h3 = doc.selectFirst("h3:matchesOwn(Related Duties)");
        Elements relDuties = h3.parent().parent().select("table.db-table > tbody > tr > td:first-child a.db_popup");
        for(Element relDuty : relDuties) {
            relatedDuties.add(Lid.parseLid(relDuty.attr("href")));
        }

        conditionalSpawner = (doc.selectFirst("span.db-view__npc__header_text:matchesOwn(Spawn Location)").parent().select("tr p:matchesOwn(Will only appear under)").size() > 0);
    }
}
