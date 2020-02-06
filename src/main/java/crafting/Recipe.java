package crafting;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.JsoupUtils;
import util.Lid;
import util.LidCountBag;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A recipe: part of the "Crafting Log" on the Lodestone; something a disciple of the Hand can craft
 */
public class Recipe {
    /**
     *Lodestone unique identifier, 11 alphanumeric characters
     */
    protected Lid lid;
    /**
     * Lodestone icon url for the 128x128 pixel icon
     */
    protected String licon;
    /**
     * Discipline of the Hand required to execute this recipe
     */
    protected String doth;
    /**
     * Master book required for this recipe
     */
    protected String mastery = null;
    /**
     * Discipline level of this recipe
     */
    protected int lvl;
    /**
     * Number of stars of this recipe (number of stars next to the level)
     */
    protected int nStars;
    /**
     * Name of the recipe
     */
    protected String name;
    /**
     * Lodestone id of the item produced by this recipe
     */
    protected Lid product;
    /**
     * Category of the product of this recipe
     */
    protected String cat;
    /**
     * Materials required for this recipe (excludes crystals)
     */
    protected List<LidCountBag> materials = new ArrayList<>();
    /**
     * Crystals required to execute this recipe
     */
    protected List<LidCountBag> crystals = new ArrayList<>();
    /**
     * Number of items crafted upon one execution of this recipe (ex. for armor, typically 1; for potions, typically 3)
     */
    protected int nCrafted;
    /**
     * Difficulty rating of this recipe
     */
    protected int difficulty;
    /**
     * Durability available while crafting
     */
    protected int durability;
    /**
     * Maximum number attainable by increasing quality while crafting
     */
    protected int maxQuality;
    /**
     * Maximum initial quality from high quality materials, in percents
     */
    protected int quality;
    /**
     * Craftsmanship recommended to attempt this recipe
     */
    protected int recommendedCrafstmanship = 0;
    /**
     * Crafstmanship required to execute this recipe
     */
    protected int requiredCraftsmanship = 0;
    /**
     * Control required to execute this recipe
     */
    protected int requiredControl = 0;
    /**
     * Control required to use quick synthesis for this recipe
     */
    protected int requiredControlForQuickSynthesis = 0;
    /**
     * Craftsmanship required to use quick synthesis for this recipe
     */
    protected  int requiredCraftsmanshipForQuickSynthesis = 0;
    /**
     * Aspect (ex. Wind) required to craft this recipe
     */
    protected String requiredAspect = null;
    /**
     * Is quick synthesis possible for this item?
     */
    protected boolean quickSynthesisEnabled = true;
    /**
     * Is it possible to craft a high quality version of this product?
     */
    protected boolean highQualityEnabled = true;
    /**
     * Does this product have a collectable version?
     */
    protected boolean collectable = false;
    /**
     * Is this recipe limited to dscipline specialists?
     */
    protected boolean specialist = false;
    /**
     * Does executing this recipe grant no experience?
     */
    protected boolean noXp = false;
    /**
     * Does this recipe require access to a facility (ex. Ehcatl crafting station)?
     */
    protected String facility = null;
    /**
     * Does this recipe require wearing a specific piece of equipment (ex. Ehcatl gloves)?
     */
    protected String equipment = null;
    /**
     * Does this always craft a collectible, whether Collector's Glove is on or not?
     */
    protected boolean alwaysCollectible = false;

    /**
     * Fetch a recipe from its Lodestone id
     * @param lid Lodestone id
     * @return Recipe
     * @throws Exception for various parsing issues
     */
    public static Recipe get(Lid lid) throws Exception {
        return new Recipe(lid);
    }

    private Recipe(Lid lid) throws Exception {
        this.lid = lid;
        URL url = new URL("https://na.finalfantasyxiv.com/lodestone/playguide/db/recipe/" + lid.toString());
        Document doc = Jsoup.connect(url.toString()).get();
        licon = doc.select(".db-view__item__icon__item_image").attr("src");
        doth = doc.select(".db-view__item__text__job_name").first().text();
        try {
            mastery = doc.select(".db-view__recipe__text__book_name").first().text();
        } catch (NullPointerException e) { /* no mastery required */ }
        lvl = Integer.parseInt(doc.select(".db-view__item__text__level__num").first().text());
        nStars = doc.select("span.ic_star--wh15").size();
        name = JsoupUtils.firstNonEmptyTextNode(doc.select(".db-view__item__text__name").first());
        product = new Lid(Lid.parseLid(doc.selectFirst(".db-tooltip__bt_item_detail > a").attr("href")));
        cat = doc.select(".db-view__recipe__text__category").first().text();
        addLidCountBags(doc, "Materials");
        addLidCountBags(doc, "Crystals");
        Element craftData = doc.selectFirst(".db-view__recipe__craftdata");
        setCraftData(craftData);
        Element craftConditions = doc.selectFirst(".db-view__recipe__crafting_conditions");
        setCraftConditions(craftConditions);
    }

    private void setCraftConditions(Element craftConditions) throws Exception {
        Elements dds = craftConditions.select("dd");
        for(Element dd : dds) {
            String t = dd.text().trim();
            if (t.contains("Quick Synthesis Craftsmanship Required"))
                requiredCraftsmanshipForQuickSynthesis = extractInt(t);
            else if(t.contains("Always Synthesized as Collectable"))
                alwaysCollectible = true;
            else if(t.contains("Quick Synthesis Control Required")) // FIRST CAUSE CONTROL REQUIRED INCLUDED so not enough
                requiredControlForQuickSynthesis = extractInt(t);
            else if(t.contains("Craftsmanship Recommended"))
                recommendedCrafstmanship = extractInt(t);
            else if(t.contains("Craftsmanship Required"))
                requiredCraftsmanship = extractInt(t);
            else if(t.contains("Control Required"))
                requiredControl = extractInt(t);
            else if(t.contains("Quick Synthesis Unavailable"))
                quickSynthesisEnabled = false;
            else if(t.contains("HQ Uncraftable"))
                highQualityEnabled = false;
            else if(t.contains("Collectable Synthesis Available"))
                collectable = true;
            else if(t.contains("Aspect"))
                requiredAspect = t.substring(8, t.length());
            else if(t.contains("Specialist"))
                specialist = true;
            else if(t.contains("No EXP"))
                noXp = true;
            else if(t.contains("Facility Access"))
                facility = t;
            else if(t.contains("Equipment Required"))
                equipment = getEquipmentLid(t);
            else
                throw new Exception("Unknown craft condition! '" + t + "'");
        }
    }

    private String getEquipmentLid(String t) throws Exception {
        switch (t.trim()) {
            case "Equipment Required: Ehcatl Wristgloves":
                return "4902e88281a";
            default:
                throw new Exception("Unknown required equipment: '" + t + "'?");
        }
    }

    private int extractInt(String mumbo) throws ParseException {
        Pattern p = Pattern.compile(".+?(\\d+).*");
        Matcher m = p.matcher(mumbo);
        if(m.find() && m.groupCount() == 1)
            return Integer.parseInt(m.group(1));
        else
            throw new ParseException("Unable to find control/craftsmanship/etc required from '" + mumbo + "'", 0);
    }

    private void setCraftData(Element craftData) throws Exception {
        Elements lis = craftData.select("li");
        if(lis.size() != 5)
            throw new Exception("Unexpected number of craft data items: expected 5, got: " + lis.size());
        nCrafted = getIntFromSpanText(craftData, "Total Crafted");
        difficulty = getIntFromSpanText(craftData, "Difficulty");
        durability = getIntFromSpanText(craftData, "Durability");
        maxQuality = getIntFromSpanText(craftData, "Maximum Quality");
        quality = getQualityPercent(craftData);
    }

    private int getQualityPercent(Element craftData) throws ParseException {
        String upToString = craftData.selectFirst("li > span:matchesOwn(^Quality$)").parent().textNodes().get(0).text().trim();
        Pattern pattern = Pattern.compile("Up to (\\d+)\\%");
        Matcher matcher = pattern.matcher(upToString);
        if(matcher.find() && matcher.groupCount() == 1) {
            return Integer.parseInt(matcher.group(1));
        } else
            throw new ParseException("Can't find quality percentage from '" + upToString + "'", 0);
    }

    private int getIntFromSpanText(Element el, String spanText) {
        String n = el.select("li > span:matchesOwn(^" + spanText + "$)").first().parent().textNodes().get(0).text().trim();
        return Integer.parseInt(n);
    }

    private void addLidCountBags(Document doc, String h4) throws Exception {
        Elements els = doc.select("div.recipe_detail > div.db-view__data > div.db-view__data__inner > div.db-view__data__inner__wrapper > h4:containsOwn(" + h4 + ") ~ div.js__material.db-tree > div.db-view__data__reward__item__name");
        List<LidCountBag> list;
        switch (h4) {
            case "Materials":
                list = materials;
                break;
            case "Crystals":
                list = crystals;
                break;
            default:
                throw new Exception("Unknown ingredient type? '" + h4 + "'");
        }
        for(Element el : els) {
            Lid l = new Lid(Lid.parseLid(el.select("a.db_popup").first().attr("href")));
            int n = Integer.parseInt(el.select(".db-view__item_num").first().text());
            list.add(new LidCountBag(l, n));
        }
    }

    /**
     * Getter for the Lodestone id
     * @return Lodestone id
     */
    public Lid getLid() {
        return lid;
    }

    /**
     * Getter for the Lodestone icon
     * @return Lodestone icon url
     */
    public String getLicon() {
        return licon;
    }

    /**
     * Getter for the Discipline of the Hand required to execute this recipe
     * @return Discipline of the Hand
     */
    public String getDoth() {
        return doth;
    }

    /**
     * Getter for the master book required to craft this recipe
     * @return Master book name
     */
    public String getMastery() {
        return mastery;
    }

    /**
     * Getter for the discipline level of this recipe
     * @return Level
     */
    public int getLvl() {
        return lvl;
    }

    /**
     * Getter for the number of stars of this recipe
     * @return Number of stars
     */
    public int getnStars() {
        return nStars;
    }

    /**
     * Getter for the name of the recipe
     * @return Name of the recipe
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the category of this recipe's product
     * @return Category of the product
     */
    public String getCat() {
        return cat;
    }

    /**
     * Getter for the materials required to execute this query (excludes crystals)
     * @return List of (number, Lodestone id)
     */
    public List<LidCountBag> getMaterials() {
        return materials;
    }

    /**
     * Getter for the crystals required to execute this query
     * @return List of (number, Lodestone id)
     */
    public List<LidCountBag> getCrystals() {
        return crystals;
    }

    /**
     * Getter for the number of products crafted for one execution of this recipe
     * @return Number of crafted items
     */
    public int getnCrafted() {
        return nCrafted;
    }

    /**
     * Getter for the difficulty of this recipe
     * @return Difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Getter for the durability available while crafting this recipe
     * @return Durability
     */
    public int getDurability() {
        return durability;
    }

    /**
     * Getter for the maximum number attainable by increasing quality while crafting this recipe
     * @return Maximum Quality
     */
    public int getMaxQuality() {
        return maxQuality;
    }

    /**
     * Getter for the quality
     * @return Quality
     */
    public int getQuality() {
        return quality;
    }

    /**
     * Getter for the recommended amount of craftsmanship to attempt this recipe
     * @return Recommended craftsmanship
     */
    public int getRecommendedCrafstmanship() {
        return recommendedCrafstmanship;
    }

    /**
     * Getter for the amount of craftsmanship required to execute this recipe
     * @return Required craftsmanship
     */
    public int getRequiredCraftsmanship() {
        return requiredCraftsmanship;
    }

    /**
     * Getter for the amount of control required to execute this recipe
     * @return Required control
     */
    public int getRequiredControl() {
        return requiredControl;
    }

    /**
     * Getter for the amount of control required to use quick synthesis for this recipe
     * @return Required control for quick synthesis
     */
    public int getRequiredControlForQuickSynthesis() {
        return requiredControlForQuickSynthesis;
    }

    /**
     * Getter for the mount of craftsmanship required to use quick synthesis for this recipe
     * @return Required craftsmanship for quick synthesis
     */
    public int getRequiredCraftsmanshipForQuickSynthesis() {
        return requiredCraftsmanshipForQuickSynthesis;
    }

    /**
     * Getter for whether quick synthesis is possible
     * @return Is quick synthesis possible?
     */
    public boolean isQuickSynthesisEnabled() {
        return quickSynthesisEnabled;
    }

    /**
     * Getter for whether a high quality version of this product can be crafted
     * @return Is high quality possible?
     */
    public boolean isHighQualityEnabled() {
        return highQualityEnabled;
    }

    /**
     * Getter for whether this product has a collectable version
     * @return Is collectability possible?
     */
    public boolean isCollectable() {
        return collectable;
    }

    /**
     * Getter for the Lodestone id of the product resulting from this recipe
     * @return Product's Lodestone id
     */
    public Lid getProduct() {
        return product;
    }

    /**
     * Getter for the aspect (ex. Wind) required to execute this recipe, if any
     * @return Aspect
     */
    public String getRequiredAspect() {
        return requiredAspect;
    }

    /**
     * Getter for whether this recipe is limited to discipline specialists
     * @return Is it limited to specialists?
     */
    public boolean isSpecialist() {
        return specialist;
    }

    /**
     * Getter for whether crafting this recipe grants no experience
     * @return Is is void of xp?
     */
    public boolean isNoXp() {
        return noXp;
    }

    /**
     * Getter for the facility (ex. Ehcatl crafting station) required to execute this recipe, if any
     * @return Facility
     */
    public String getFacility() {
        return facility;
    }

    /**
     * Getter for the equipment (ex. Ehcatl gloves) required to execute this recipe, if any
     * @return Equipment
     */
    public String getEquipment() {
        return equipment;
    }

    /**
     * Getter for whether this recipe always crafts a collectible
     * @return Does it always make a collectible?
     */
    public boolean isAlwaysCollectible() {
        return alwaysCollectible;
    }

    /**
     * Utility toString method
     * @return a pretty string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name)
                .append(" (").append(doth).append(" Lv.").append(lvl);
        for(int i = 0; i < nStars; i++)
            sb.append('*');
        sb.append(")")
                .append(" mats:[");
        for(LidCountBag lcb : materials)
            sb.append(lcb.toString()).append(", ");
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append("] + crys:[");
        for(LidCountBag lcb : crystals)
            sb.append(lcb.toString()).append(", ");
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }
}
