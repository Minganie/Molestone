package gathering;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.JsoupUtils;
import util.Lid;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GatheredItem {
    private Lid lid;
    private String cat2;
    private String cat3;
    private Lid item;
    private int level;
    private int nStars;
    private boolean hidden;
    private List<GatheringLocation> locations;

    private GatheredItem(Lid lid, Document doc) throws Exception {
        this.lid = lid;
        cat2 = JsoupUtils.firstNonEmptyTextNode(doc.selectFirst("p.db-view__item__text__job_name"));
        cat3 = JsoupUtils.firstNonEmptyTextNode(doc.selectFirst("p.db-view__gathering__text__category"));
        item = Lid.parseLid(doc.selectFirst(".db-tooltip__bt_item_detail > a:nth-child(1)").attr("href"));
        level = JsoupUtils.parseInt(JsoupUtils.firstNonEmptyTextNode(doc.selectFirst("span.db-view__item__text__level__num")));
        nStars = doc.select("span.ic_star--wh15").size();
        hidden = (doc.selectFirst(".db-view__item__text > strong") != null);
        locations = new ArrayList<>();
        dealWithLocations(doc.selectFirst(".db-view__gathering__data"));
    }

    private void dealWithLocations(Element locsEl) throws ParseException {
        Pattern pattern = Pattern.compile("Lv\\.\\s(\\d+)\\s(.+)");
        String currentRegion = null;
        String currentZone = null;
        for(Element el : locsEl.children()) {
            if(el.tagName().equals("h5")) {
                currentRegion = el.text();
            }
            if(el.tagName().equals("dl")) {
                currentZone = el.selectFirst("dt").text();
                for(Element areaEl : el.select("dd")) {
                    Matcher matcher = pattern.matcher(areaEl.text());
                    if(matcher.find() && matcher.groupCount() == 2) {
                        int lvl = JsoupUtils.parseInt(matcher.group(1));
                        String areaName = matcher.group(2).trim();
                        boolean timeLimited = (el.selectFirst(".db-view__gathering__point__01") != null);
                        locations.add(new GatheringLocation(currentRegion, currentZone, areaName, lvl, timeLimited));
                    } else
                        throw new ParseException("Unable to parse level and area info for " + areaEl.text(), 0);
                }
            }
        }
    }

    public static GatheredItem get(Lid lid) throws Exception {
        URL url = new URL("https://na.finalfantasyxiv.com/lodestone/playguide/db/gathering/" + lid.toString());
        Document doc = Jsoup.connect(url.toString()).get();
        return new GatheredItem(lid, doc);
    }

    public Lid getLid() {
        return lid;
    }

    public String getCat2() {
        return cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public Lid getItem() {
        return item;
    }

    public int getLevel() {
        return level;
    }

    public int getnStars() {
        return nStars;
    }

    public boolean isHidden() {
        return hidden;
    }

    public List<GatheringLocation> getLocations() {
        return locations;
    }

    @Override
    public String toString() {
        StringBuilder stars = new StringBuilder();
        for(int i = 0; i < nStars; i++)
            stars.append('*');
        StringBuilder locString = new StringBuilder();
        for(GatheringLocation gl : locations) {
            locString.append(gl.toString());
            locString.append(", ");
        }
        locString.delete(locString.length()-2, locString.length());
        return lid + " | Gathering lv." + level + stars.toString() + " " + item + " (" + cat2 + "/" + cat3 + ") "
                + (hidden ? "hidden " : "") + "in [" + locString.toString() + "]";
    }
}
