package item;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.JsoupUtils;
import util.Lid;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class that all types of items inherit from. Combines all the attributes (and all their getters) that all
 * items can have.
 */
public abstract class Item {
	// Stats that all types of items have
    /**
     * Name of the item.
     */
	protected String name = null;
    /**
     * cat2 as per the Lodestone: Arms, Tools, Armor, Accessories, Medicine{@literal &}Meals, Materials or Others
     */
    protected String cat2 = null;
    /**
     * cat3 as per the Lodestone, varies by cat2
     */
	protected String cat3 = null;
    /**
     * Lodestone unique identifier, 11 alphanumeric characters
     */
	protected Lid lid = null;
    /**
     * Lodestone icon url for the 128x128 pixel icon (not high quality version of the item)
     */
	protected String licon = null;
    /**
     * Rarity of the item (common, uncommon, rare or epic)
     */
	protected String rarity;
    /**
     * Item level
     */
	protected Integer ilvl = null;
    /**
     * Player level required to equip the item
     */
	protected Integer requiredLevel = null;
    /**
     * Unique: can you have more than one in your inventory?
     */
	protected boolean unique;
    /**
     * Untradable: can you trade it with other players? (my understanding)
     */
	protected boolean untradable;
    /**
     * Is advanced melding (i.e. more materia than slots) allowed?
     */
	protected boolean advMelding;
    /**
     * Can you sell to a vendor? (my understanding)
     */
	protected boolean unsellable;
    /**
     * Can you sell on the Market Board? (my understanding)
     */
	protected boolean marketProhibited;
    /**
     * Vendor sell price
     */
	protected Integer sellPrice = null;

	// Stats that some types of items have; here rather than in sub-classes so all items have a getter
    /**
     * Small bit of flavor text accompanying some types of items like potions.
     */
    protected String note = null;
    /**
     * Recast time, i.e. cooldown
     */
    protected Integer recast = null;
    /**
     * Physical or magical damage (bonus to player damage per my understanding)
     */
	protected Integer damage = null;
    /**
     * Auto-attack damage
     */
	protected Float autoAttack = null;
    /**
     * Delay...?
     */
	protected Float delay = null;
    /**
     * Block Strength
     */
    protected Integer blockStrength = null;
    /**
     * Block Rate
     */
	protected Integer blockRate = null;
    /**
     * Physical Defense rating
     */
	protected Integer defense = null;
    /**
     * Magical Defense Rating
     */
    protected Integer magicDefense = null;
    /**
     * List of classes and jobs that may use this item
     */
	protected String disciplines = null;
    /**
     * List of stat bonuses for equipping this item
     */
	protected List<Bonus> bonuses = new ArrayList<>();
    /**
     * List of effects for using this item
     */
	protected List<String> effects = new ArrayList<>();
    /**
     * Number of materia slots
     */
	protected Integer materiaSlots = null;
    /**
     * Discipline of the Hand required to repair this item
     */
	protected String repairClass = null;
    /**
     * Discipline of the Hand level required to repair this item
     */
	protected Integer repairLevel = null;
    /**
     * Material (i.e. grade of dark matter) required to repair this item
     */
	protected String repairMaterial = null;
    /**
     * Discipline of the Hand required to meld materia to this item
     */
	protected String meldingClass = null;
    /**
     * Discipline of the Hand level required to meld materia to this item
     */
	protected Integer meldingLevel = null;
    /**
     * Can materia be extracted from this item?
     */
	protected Boolean extractable = null;
    /**
     * If desynthesizable, discipline of the Hand class required to desynthesize
     */
	protected String desynthClass = null;
    /**
     * If desynthesizable, discipline of the Hand desynth skill level required to desynthesize
     */
	protected Float desynthLevel = null;
    /**
     * Can you apply a dye?
     */
	protected Boolean dyeable = null;
    /**
     * Can you use this appearance as a glamor on something else? (my understanding per
     * <a href="https://na.finalfantasyxiv.com/lodestone/topics/detail/a1585905092f4cd0fcf95239489bb85da7227deb">Glamours and Gear Preview (03/20/2014)</a>)
     */
	protected Boolean projectable = null;
    /**
     * Can you apply your free company crest on this item?
     */
	protected boolean crestWorthy;
    /**
     * What item level must target item have to apply this materia?
     */
	protected Integer meldItemLevel = null;
    /**
     * Can you put this item in the glamour dresser?
     */
	protected boolean dresserAble;
    /**
     * Can you put this item in the armoire?
     */
	protected boolean armoireAble;
    /**
     * Set of vendors who will sell this item
     */
	protected Set<Lid> shopSources = new HashSet<>();
    /**
     * Set of enemies that may drop this item when killed
     */
    protected Set<Lid> enemySources = new HashSet<>();
    /**
     * Set of quests that may award this item as a reward
     */
	protected Set<Lid> questSources = new HashSet<>();
    /**
     * Set of duties that may provide this item
     */
	protected Set<Lid> dutySources = new HashSet<>();
    /**
     * List of botanist or miner gathering log entries that provide this item
     */
	protected Set<Lid> gatheringSources = new HashSet<>();
    /**
     * List of crafting log recipes making this item
     */
	protected Set<Lid> craftingSources = new HashSet<>();
    /**
     * Lit of crafting log recipes using this item as an ingredient
     */
	protected Set<Lid> craftingUses = new HashSet<>();

    /**
     * Default constructor used by {@link Materia} in one of its constructors used by {@link ItemFactory#getHack(String, Lid, String, String, String)}
     * because ...?
     */
    Item() {
    }

    /**
     * Factory method; loads the Lodestone page for this item from the given Lodestone id; deduces cat2 and cat3 from
     * the Lodestone page; delegates choice of child class to {@link ItemFactory}
     * @param lid Lodestone id
     * @return Instance of {@link Item}
     * @throws Exception for various parsing issues
     */
    public static Item get(Lid lid) throws Exception {
        URL url = new URL("https://na.finalfantasyxiv.com/lodestone/playguide/db/item/" + lid.toString());
        Document doc = Jsoup.connect(url.toString()).get();
        String icat3 = doc.select(".db-view__item__text__category").first().text().trim();
        String icat2 = doc.select(".db__menu__category__kind.active > span.db__menu__category__txt").first().text().trim();
        return ItemFactory.getItem(icat2, icat3, doc, lid);
    }

    /**
     * Generally used constructor
     * @param cat2 cat2 per Lodestone
     * @param cat3 cat3 per Lodestone
     * @param doc Jsoup document for the Lodestone page for this item
     * @param lid Lodestone id
     * @throws Exception for various parsing issues
     */
    Item(String cat2, String cat3, Document doc, Lid lid) throws Exception {
	    this.cat2 = cat2;
	    this.cat3 = cat3;
	    this.lid = lid;
        Element details = doc.selectFirst("div.db_cnts");
        this.name = JsoupUtils.firstNonEmptyTextNode(details.select(".db-view__item__text__name").first());
        this.licon = details.select(".db-view__item__icon__item_image").attr("src");
        this.ilvl = parseIlvl(details);
        this.requiredLevel = parseReqLvl(details);
        parseDetails(details);
    }

    /**
     * Parse item level from the "details" div
     * @param details Div with the item details
     * @return Item level as an {@link Integer}
     */
    private static Integer parseIlvl(Element details) {
	    Integer i = null;
	    try {
            String mumbo = details.select(".db-view__item_level").text();
            Pattern pattern = Pattern.compile("Item Level (\\d+)");
            Matcher matcher = pattern.matcher(mumbo);
            if (matcher.find())
                return JsoupUtils.parseInt(matcher.group(1));
        } catch (NullPointerException | ParseException e) {
            // Item may not have a ilvl
        }
        return i;
    }

    /**
     * Parse required level to equip this item from the "details" div
     * @param details Div with the item details
     * @return Required level as an {@link Integer}
     */
    private static Integer parseReqLvl(Element details) {
	    Integer i = null;
	    try {
	        String mumbo = details.select(".db-view__item_equipment__level").text();
	        Pattern pattern = Pattern.compile("Lv. (\\d+)");
	        Matcher matcher = pattern.matcher(mumbo);
	        if(matcher.find())
	            return JsoupUtils.parseInt(matcher.group(1));
        } catch (NullPointerException | ParseException e) {
	        // Item may not have a level requirement
        }
        return i;
    }

    abstract void parseSpecificDetails(Element details) throws Exception;
	
	private void parseCommonDetails(Element details) throws Exception {
	    rarity = parseRarity(details);
		unique = !details.select("div.db-view__item__header span.rare").isEmpty();
		untradable = !details.select("div.db-view__item__header span.ex_bind").isEmpty();
		advMelding = !details.select("div.db-view__item_footer .db-view__cannot_materia_prohibition").isEmpty();
		unsellable = !details.select("div.db-view__item_footer .db-view__unsellable").isEmpty();
		marketProhibited = !details.select("div.db-view__item_footer .db-view__market_notsell").isEmpty();
		if(!details.select("span.sys_nq_element > span.db-view__sells").isEmpty())
		    sellPrice = parseSellPrice(details.select("span.sys_nq_element").html());
//		System.out.println("Item " + name + " has a sell price of " + sellPrice + " gil");
        Elements imgs = details.select(".db-view__item__storage>li>img");
        if(imgs.size() != 3) {
            throw new Exception("Didn't find three images for Crest/Dresser/Armoire for " + this.name);
        } else {
            crestWorthy = !imgs.get(0).attr("alt").contains("Cannot");
            dresserAble = !imgs.get(1).attr("alt").contains("Cannot");
            armoireAble = !imgs.get(2).attr("alt").contains("Cannot");
        }

		
		// HEADER
		Elements spans = details.select("div.db-view__item__header .db-view__item__text span");
		for(Element span : spans) {
			if(!(span.hasClass("db-view__item__text__category") ||
					span.hasClass("rare") ||
					span.hasClass("ex_bind") ||
                    span.hasClass("sys_hq_element")))
				throw new Exception("found new span in header: " + span);
		}
		
		// FOOTER
        // Seems to have changed the p's ? spans look more reliable
//		Elements ps = details.select(".db-view__item_footer p");
//		for(Element p : ps) {
//			if(!p.hasClass("db-view__cannot_materia_prohibition"))
//				throw new Exception("found a new p in footer: " + p);
//		}
		Elements fspans = details.select(".db-view__item_footer span");
		for(Element span : fspans) {
			if(!(span.hasClass("db-view__unsellable") ||
					span.hasClass("db-view__market_notsell") ||
					span.hasClass("db-view__sells") ||
					span.hasClass("sys_nq_element") ||
					span.hasClass("sys_hq_element")))
				throw new Exception("found a new span in footer: " + span);
		}
		findShopSources(details);
		findEnemySources(details);
		findQuestSources(details);
		findDutySources(details);
		findGatheringSources(details);
		findCraftingSources(details);
		findCraftingUses(details);
	}

    private void findEnemySources(Element details) throws Exception {
        Elements trs = details.select("h3:matchesOwn(^Dropped By$) + div.db-table__wrapper > table > tbody > tr");
        for(Element tr : trs) {
            Lid enemy = Lid.parseLid(tr.selectFirst("a.db_popup").attr("href"));
            enemySources.add(enemy);
        }
    }

    private void findCraftingUses(Element details) throws Exception {
        Elements trs = details.select("h3:matchesOwn(Related Crafting Log) + div.db-table__wrapper > table > tbody > tr");
        for(Element tr : trs) {
            Lid recipe = Lid.parseLid(tr.selectFirst("a.db_popup").attr("href"));
            craftingUses.add(recipe);
        }
    }

    private void findCraftingSources(Element details) throws Exception {
        Elements trs = details.select("h3:matchesOwn(^Crafting Log$) + div.db-table__wrapper > table > tbody > tr");
        for(Element tr : trs) {
            Lid recipe = Lid.parseLid(tr.selectFirst("a.db_popup").attr("href"));
            craftingSources.add(recipe);
        }
    }

    private void findGatheringSources(Element details) throws Exception {
        Elements trs = details.select("h3:matchesOwn(Gathering Log) + div.db-table__wrapper > table > tbody > tr");
        for(Element tr : trs) {
            Lid gathering = Lid.parseLid(tr.selectFirst("a.db_popup").attr("href"));
            gatheringSources.add(gathering);
        }
    }

    private void findDutySources(Element details) throws Exception {
        Elements trs = details.select("h3:matchesOwn(Related Duties) + div.db-table__wrapper > table > tbody > tr");
        for(Element tr : trs) {
            Lid duty = Lid.parseLid(tr.selectFirst("a.db_popup").attr("href"));
            dutySources.add(duty);
        }
    }

    private void findQuestSources(Element details) throws Exception {
        Elements trs = details.select("h3:matchesOwn(Related Quests) + div.db-table__wrapper > table > tbody > tr");
        for(Element tr : trs) {
            Lid quest = Lid.parseLid(tr.selectFirst("a.db_popup").attr("href"));
            questSources.add(quest);
        }
    }

    private void findShopSources(Element details) throws Exception {
	    // Regular vendors
        Elements trs = details.select(".db-shop__npc__space > table > tbody > tr");
        for(Element tr : trs) {
            Lid shop = Lid.parseLid(tr.selectFirst("a.db_popup").attr("href"));
            shopSources.add(shop);
        }
        // Currency vendors
        Elements divs = details.select("h3:matchesOwn(Required Items) ~ div.db-shop__item__wrapper");
        for(Element div : divs) {
            Lid shop = Lid.parseLid(div.selectFirst("p.db-shop__item__npc > a.db_popup").attr("href"));
            shopSources.add(shop);
        }
        // GC QM
        Elements gcDivs = details.select("h3:matchesOwn(Required Company Seals and Rank) ~ div.db-shop__item__wrapper");
        for(Element div : gcDivs ) {
            Lid shop = Lid.parseLid(div.selectFirst("a.db_popup").attr("href"));
            shopSources.add(shop);
        }
    }

    private static String parseRarity(Element details) throws Exception {
	    String nameClass = details.select(".db-view__item__text__name").first().className();
	    if(nameClass.contains("_uncommon"))  // LEAVE IT FIRST just in case because "uncommon" contains "common"...
	        return "Uncommon";
        if(nameClass.contains("_common"))
            return "Common";
        if(nameClass.contains("_rare"))
            return "Rare";
        if(nameClass.contains("_epic"))
            return "Epic";
        if(nameClass.contains("_magic"))
            return "Magic";
        throw new Exception("Can't figure out item rarity from '" + nameClass + "'");
    }

    private Integer parseSellPrice(String mumbo) throws Exception {
	    try {
            Pattern pattern = Pattern.compile("<span.+</span>(\\d+(,\\d+)?).gil");
            Matcher matcher = pattern.matcher(mumbo);
            if (matcher.find())
                return JsoupUtils.parseInt(matcher.group(1));
            else
                throw new Exception("Can't seem to parse sell price for item " + name + " with price string " + mumbo);
        } catch (Exception e) {
            throw new Exception("Can't seem to parse sell price for item " + name + " with price string " + mumbo);
        }
    }

    private void parseDetails(Element details) throws Exception {
		parseCommonDetails(details);
		parseSpecificDetails(details);
	}

    /**
     * Getter for the name of the item
     * @return Name of the item
     */
	public String getName() {
		return name;
	}

    /**
     * Getter for the Lodestone id
     * @return Lodestone id
     */
	public Lid getLid() {
		return lid;
	}

    /**
     * Getter for the cat2
     * @return cat2
     */
    public String getCat2() {
        return cat2;
    }

    /**
     * Getter for the cat3
     * @return cat3
     */
    public String getCat3() {
        return cat3;
    }

    /**
     * Getter for the url of the icon on the Lodestone
     * @return Url of the icon
     */
	public String getLicon() {
		return licon;
	}

    /**
     * Getter for the item level
     * @return Item level
     */
	public Integer getIlvl() {
		return ilvl;
	}

    /**
     * Getter for the player level required to equip the item
     * @return Required player level
     */
	public Integer getRequiredLevel() {
		return requiredLevel;
	}

    /**
     * Getter for the uniqueness of the item
     * @return Uniqueness of the item
     */
	public Boolean isUnique() {
		return unique;
	}

    /**
     * Getter for the untradeable-ness of the item
     * @return Is it unique?
     */
	public Boolean isUntradable() {
		return untradable;
	}

    /**
     * Getter for the possibility of advanced melding for this item
     * @return Is advanced melding possible?
     */
	public Boolean isAdvMelding() {
		return advMelding;
	}

    /**
     * Getter for the unsellability of this item
     * @return Is is unsellable?
     */
	public Boolean isUnsellable() {
		return unsellable;
	}

    /**
     * Getter for the market prohibition for this item
     * @return Is it market prohibited?
     */
	public Boolean isMarketProhibited() {
	    return marketProhibited;
    }

    /**
     * Getter for the vendor sell price
     * @return Vendor sell price
     */
    public Integer getSellPrice() {
        return sellPrice;
    }

    /**
     * Getter for the note
     * @return Note
     */
    public String getNote() {
        return note;
    }

    /**
     * Getter for the recast time
     * @return Recast time
     */
    public Integer getRecast() {
        return recast;
    }

    /**
     * Getter for the damage of this item
     * @return Damage
     */
    public Integer getDamage() {
        return damage;
    }

    /**
     * Getter for the auto-attack damage of this item
     * @return Auto-attack damage
     */
    public Float getAutoAttack() {
        return autoAttack;
    }

    /**
     * Getter for the delay of this item
     * @return Delay
     */
    public Float getDelay() {
        return delay;
    }

    /**
     * Getter for the block strength
     * @return Block strength
     */
    public Integer getBlockStrength() {
        return blockStrength;
    }

    /**
     * Getter for the block rate
     * @return Block rate
     */
    public Integer getBlockRate() {
        return blockRate;
    }

    /**
     * Getter for the physical defense
     * @return Physical defense
     */
    public Integer getDefense() {
        return defense;
    }

    /**
     * Getter for the magical defense
     * @return Magical defense
     */
    public Integer getMagicDefense() {
        return magicDefense;
    }

    /**
     * Getter for the list of classes/jobs that can use this item
     * @return String with all classes/jobs
     */
    public String getDisciplines() {
        return disciplines;
    }

    /**
     * Getter for the list of {@link Bonus} for equipping this item
     * @return List of {@link Bonus}
     */
    public List<Bonus> getBonuses() {
        return bonuses;
    }

    /**
     * Getter for the list of effects for consuming this item
     * @return List of effects
     */
    public List<String> getEffects() {
        return effects;
    }

    /**
     * Getter for the number of materia slots in this item
     * @return Number of materia slots
     */
    public Integer getMateriaSlots() {
        return materiaSlots;
    }

    /**
     * Getter for the Discipline of the Hand required to repair this item
     * @return Discipline of the Hand class required to repair
     */
    public String getRepairClass() {
        return repairClass;
    }

    /**
     * Getter for the Discipline of the Hand level required to repair this item
     * @return Discipline of the Hand level required to repair
     */
    public Integer getRepairLevel() {
        return repairLevel;
    }

    /**
     * Getter for the material required to repair this item (i.e. Dark Matter grade)
     * @return Dark matter consumed to repair
     */
    public String getRepairMaterial() {
        return repairMaterial;
    }

    /**
     * Getter for the Discipline of the Hand required to meld materia to this item
     * @return Discipline of the Hand required to meld
     */
    public String getMeldingClass() {
        return meldingClass;
    }

    /**
     * Getter for the Discipline of the Hand level required to meld materia to this item
     * @return Discipline of the Hand level required to meld
     */
    public Integer getMeldingLevel() {
        return meldingLevel;
    }

    /**
     * Getter for the convertibility to materia
     * @return Is it convertible to materia?
     */
    public Boolean isConvertible() {
        return extractable;
    }

    /**
     * Getter for the Discipline of the Hand required to desynthesize this item, if desynthesizable
     * @return Discipline of the Hand required to desynthesize
     */
    public String getDesynthClass() {
        return desynthClass;
    }

    /**
     * Getter for the desynthesis skill level required to desynthesize this item, if desynthesizable
     * @return Desynthesis skill required
     */
    public Float getDesynthLevel() {
        return desynthLevel;
    }

    /**
     * Getter for the dyeability of this item
     * @return Can it be dyed?
     */
    public Boolean isDyeable() {
        return dyeable;
    }

    /**
     * Getter for whether or not this item's appearance can be used as a glamor on another item
     * @return Is it projectable?
     */
    public Boolean isProjectable() {
        return projectable;
    }

    /**
     * Getter for the item level required for this materia to be melded on it
     * @return Item level required to meld on
     */
    public Integer getMeldItemLevel() {
        return meldItemLevel;
    }

    /**
     * Getter for the Free Company crest worthiness of this item
     * @return Is it crest worthy?
     */
    public Boolean isCrestWorthy() {
        return crestWorthy;
    }

    /**
     * Getter for whether you can place this item in the glamor dresser
     * @return Can it be put in the dresser?
     */
    public Boolean isDresserAble() {
        return dresserAble;
    }

    /**
     * Getter for whether you can place this item in the glamor armoire
     * @return Can it be put in the armoire?
     */
    public Boolean isArmoireAble() {
        return armoireAble;
    }
	
	private String formatLicon() {
		if(licon.length()<20)
			return licon;
		return licon.substring(0, 19) + "...";
	}

    /**
     * Utility toString method
     * @return A string like so "Vintage Hora                             i  48 r  45  f88470aaa49 https://img.finalfa..."
     */
	public String toString() {
		return String.format("%-40s i %3d r %3d %12s %s", name, ilvl, requiredLevel, lid.toString(), formatLicon());
	}

    /**
     * Getter for the item's rarity
     * @return Rarity (common, uncommon, rare or epic)
     */
    public String getRarity() {
        return rarity;
    }

    /**
     * Getter for whether this item can be converted into materia
     * @return Is it convertible into materia?
     */
    public Boolean getExtractable() {
        return extractable;
    }

    /**
     * Getter for whether this item can be dyed
     * @return Can it be dyed?
     */
    public Boolean getDyeable() {
        return dyeable;
    }

    /**
     * Getter for whether this item can be projected as a glamour
     * @return Can it be projected?
     */
    public Boolean getProjectable() {
        return projectable;
    }

    /**
     * Getter for the vendors who will sell this item
     * @return Set of Lodestone ids of shops
     */
    public Set<Lid> getShopSources() {
        return shopSources;
    }

    /**
     * Getter for the enemies that may drop this item
     * @return Set of Lodestone ids of enemies
     */
    public Set<Lid> getEnemySources() {
        return enemySources;
    }

    /**
     * Getter for the quests that will award this item as a reward
     * @return Set of Lodestone ids of quests
     */
    public Set<Lid> getQuestSources() {
        return questSources;
    }

    /**
     * Getter for the duties that may provide this item
     * @return Set of Lodestone ids of duties
     */
    public Set<Lid> getDutySources() {
        return dutySources;
    }

    /**
     * Getter for the botanist or miner gathering log entries that provide this item
     * @return Set of Lodestone ids of gathering log entries
     */
    public Set<Lid> getGatheringSources() {
        return gatheringSources;
    }

    /**
     * Getter for the crafting recipes making this item
     * @return Set of Lodestone ids for crafting log entries making this item
     */
    public Set<Lid> getCraftingSources() {
        return craftingSources;
    }

    /**
     * Getter for the crafting recipes using this item as an ingredient
     * @return Set of Lodestone ids for crafting log entries using this item
     */
    public Set<Lid> getCraftingUses() {
        return craftingUses;
    }

    /**
     * Utility method to get a Lodestone id for an item name; requires a connection to Melody's ffxiv db
     * @param conn Database connection to Melody's ffxiv db
     * @param name Item name
     * @return Lodestone id
     * @throws Exception if the item is not in Melody's ffxiv db
     */
    public static Lid getLid(Connection conn, String name) throws Exception {
	    if(name.contains("%27"))
	        name = name.replace("%27", "'");
	    if(name.equalsIgnoreCase("Blunt Goblin Gladius"))
	        name = "Nicked Viking Sword";
	    if(name.contains(" (Item)"))
	        name = name.replace(" (Item)", "");
	    if(name.equals("Attendee 777"))
	        name = "Attendee #777";

        PreparedStatement obvious = conn.prepareStatement("SELECT lid FROM items WHERE lower(name) = lower(?)");
        obvious.setString(1, name);
        ResultSet obviousrs = obvious.executeQuery();
        Lid lid = null;
        if(obviousrs.next()) {
            lid = new Lid(obviousrs.getString(1));
            if(obviousrs.next())
                throw new Exception("More than one item with name '" + name + "'");
        } else {
            PreparedStatement bu = conn.prepareStatement("SELECT lid FROM items WHERE lower(?) like lower( '%' || name || '%') ");
            bu.setString(1, name);
            ResultSet burs = bu.executeQuery();
            if(burs.next()) {
                lid = new Lid(burs.getString(1));
                if(burs.next())
                    throw new Exception("More than one item with name like '%" + name + "%'");
            } else {
                throw new Exception("Can't find item with name '" + name + "'");
            }
        }
        return lid;
    }
}
