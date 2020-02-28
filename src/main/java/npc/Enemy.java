package npc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.JsoupUtils;
import util.Lid;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Enemy {
    protected Lid lid;
    protected String name;
    protected List<SpawnLocation> spawnLocations = new ArrayList<>();
    protected List<Lid> droppedItems = new ArrayList<>();
    protected List<Lid> relatedDuties = new ArrayList<>();

    private static String urlPart = "enemy";

    private Enemy(Lid lid, Document doc) throws Exception {
        this.lid = lid;
        this.name = JsoupUtils.firstNonEmptyTextNode(doc.selectFirst(".db-view__detail__npc_name"));
        parseDetails(doc);

    }

    public static Enemy get(Lid lid) throws Exception {
        URL url = new URL("https://na.finalfantasyxiv.com/lodestone/playguide/db/npc/" + urlPart + "/" + lid.toString());
        Document doc = Jsoup.connect(url.toString()).get();
        return new Enemy(lid, doc);
    }

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
        if(h3 != null) {
            Elements relDuties = h3.parent().parent().select("table.db-table > tbody > tr > td:first-child a.db_popup");
            for (Element relDuty : relDuties) {
                relatedDuties.add(Lid.parseLid(relDuty.attr("href")));
            }
        }
    }

    public Lid getLid() {
        return lid;
    }

    public String getName() {
        return name;
    }

    public List<SpawnLocation> getSpawnLocations() {
        return spawnLocations;
    }

    public List<Lid> getDroppedItems() {
        return droppedItems;
    }

    public List<Lid> getRelatedDuties() {
        return relatedDuties;
    }
}
