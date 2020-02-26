package npc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Coords;
import util.JsoupUtils;
import util.Lid;
import util.ZonedCoords;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Npc {
    protected Lid lid;
    protected String name;
    protected ZonedCoords location = null;
    protected List<Lid> availableQuests = new ArrayList<>();
    protected List<Lid> relatedQuests = new ArrayList<>();

    private static String urlPart = "npc";

    private Npc(Lid lid, Document doc) throws Exception {
        this.lid = lid;
        name = JsoupUtils.firstNonEmptyTextNode(doc.selectFirst(".db-view__detail__npc_name"));
        parseDetails(doc);
    }

    public static Npc get(Lid lid) throws Exception {
        URL url = new URL("https://na.finalfantasyxiv.com/lodestone/playguide/db/npc/" + urlPart + "/" + lid.toString());
        Document doc = Jsoup.connect(url.toString()).get();
        return new Npc(lid, doc);
    }

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

    public Lid getLid() {
        return lid;
    }

    public String getName() {
        return name;
    }

    public ZonedCoords getLocation() {
        return location;
    }

    public List<Lid> getAvailableQuests() {
        return availableQuests;
    }

    public List<Lid> getRelatedQuests() {
        return relatedQuests;
    }
}
