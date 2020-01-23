package achievement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.JsoupUtils;
import util.Lid;

import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Achievement {
    /**
     *Lodestone unique identifier, 11 alphanumeric characters
     */
    protected Lid lid;
    /**
     * cat2 as per the Lodestone: Battle, PvP, etc
     */
    protected String category2;
    /**
     * cat3 as per the Lodestone, for example for Battle: Battle, Dungeons, etc
     */
    protected String category3;
    /**
     * Name of the achievement
     */
    protected String title;
    /**
     * Number of achievement points awarded for getting this achievement
     */
    protected int points;
    /**
     * URL of the achievement's icon on the Lodestone
     */
    protected URL icon;
    /**
     * Description of the achievement, i.e. what do you need to do to get it?
     */
    protected String description;
    /**
     * When applicable, title awarded for this achievement, as a Map{@literal <}Gender, Title{@literal >}
     */
    protected Map<String, String> titleReward = new HashMap<>();
    /**
     * When applicable, Lodestone id of the item awarded for this achievement
     */
    protected Lid itemReward = null;

    /**
     * Parsing constructor
     * @param lid Lodestone id
     * @throws Exception
     */
    private Achievement(Lid lid) throws Exception {
        URL url = new URL("https://na.finalfantasyxiv.com/lodestone/playguide/db/achievement/" + lid.get());
        Document doc = Jsoup.connect(url.toString()).get();
        Element e = doc.select(".db-view__achievement__text").first();
        this.category2 = e.textNodes().get(0).text().trim();
        this.category3 = e.textNodes().get(1).text().trim();
        this.title = e.select("span").text();
        this.points = JsoupUtils.parseInt(doc.select(".db-view__achievement__point").text());
        this.icon = new URL(doc.select(".db-view__achievement__icon__image").first().attr("src"));
        this.description = doc.select("div.db-view__data__content_info:nth-child(2)").text();
        // find reward that's a title
        if(doc.select("p.db-view__achievement__title_name").first() != null) {
            // is it gender specific? sigh
            String t = doc.select("p.db-view__achievement__title_name").text();
            if(t.contains("Male") && t.contains("Female")) {
                Pattern pattern = Pattern.compile("^Male:\\s(.+)\\sFemale:\\s(.+)$");
                Matcher matcher = pattern.matcher(t);
                if(matcher.find() && matcher.groupCount() == 2) {
                    titleReward.put("Male", matcher.group(1));
                    titleReward.put("Female", matcher.group(2));
                } else {
                    throw new ParseException("Unable to parse gender-specific title rewards for achievement " + title + " @ " + lid.get(), 0);
                }
            } else {
                titleReward.put("Male", t);
                titleReward.put("Female", t);
            }
        }
        // find reward that's an item
        if(doc.select("a.db_popup").first() != null)
            this.itemReward = new Lid(Lid.parseLid(doc.select("a.db_popup").attr("href")));
    }

    /**
     * Utility toString method
     * @return a pretty string like "Achievement 'To Crush Your Enemies III' @ dd27ac2924c (10 points)"
     */
    public String toString() {
        return "Achievement '" + title + "' @ " + lid + " (" + points + " points)";
    }

    /**
     * Utility equals method
     * @param obj Other object
     * @return Is is the same Achievement?
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Achievement) {
            Achievement o = (Achievement) obj;
            return o.lid.equals(this.lid)
                    && o.title.equals(this.title)
                    && o.points == this.points
                    && o.icon.equals(this.icon)
                    && o.description.equals(this.description)
                    && (Objects.equals(this.itemReward, o.itemReward))
                    && (Objects.equals(this.titleReward, o.titleReward));
        } else
            return false;
    }

    /**
     * Public factory method
     * @param lid Lodestone id
     * @return Achievement
     * @throws Exception for various parsing issues
     */
    public static Achievement get(Lid lid) throws Exception {
        return new Achievement(lid);
    }

    /**
     * Getter for the Lodestone id
     * @return Lodestone id
     */
    public Lid getLid() {
        return lid;
    }

    /**
     * Getter for the category 2 as per the Lodestone: Battle, PvP, etc
     * @return Category 2
     */
    public String getCategory2() {
        return category2;
    }

    /**
     * Getter for the category 3 as per the Lodestone, for example for Battle: Battle, Dungeons, etc
     * @return Category 3
     */
    public String getCategory3() {
        return category3;
    }

    /**
     * Getter for the title (or name) of the Achievement
     * @return Name of the achievement
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the number of achievement points awarded for completing this achievement
     * @return Number of achievement points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter for the URL of the achievement icon on the Lodestone
     * @return URL of the icon
     */
    public URL getIcon() {
        return icon;
    }

    /**
     * Getter for the description of the achievement, i.e. what do you need to do to get it?
     * @return Description of the achievement
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the title awarded for completing this achievement, when applicable
     * @return Map of (Gender, Title)
     */
    public Map<String, String> getTitleReward() {
        return titleReward;
    }

    /**
     * Getter for the item awarded for completing this achievement, when applicable
     * @return Lodestone id of the item
     */
    public Lid getItemReward() {
        return itemReward;
    }
}
