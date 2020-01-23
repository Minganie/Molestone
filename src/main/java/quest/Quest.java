package quest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import util.*;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.Coords.parseCoords;

/**
 * A single quest in the game.
 */
public class Quest {
    private static class RewardsBag {
        private Elements completionRewards;
        private Elements optionalRewards;
        private RewardsBag(Elements completionRewards, Elements optionalRewards) {
            this.completionRewards = completionRewards;
            this.optionalRewards = optionalRewards;
        }
    }
    /**
     * Category per the Lodestone; next to the level in the quest description. For example: "Seventh
     * Umbral Era", "Seventh Astral Era", "Heavensward", "Dragonsong War", "Chronicles of a New Era - Primals", ...
     */
    protected String category;
    /**
     * Name of the quest
     */
    protected String title;
    /**
     * Type of the quest: MSQ, unlock, etc.
     */
    protected String type;
    /**
     * Area where the quest giver is located
     */
    protected String area = null;
    /**
     * Quest level
     */
    protected int level;
    /**
     * Lodestone unique identifier, 11 alphanumeric characters
     */
    protected Lid lid;
    /**
     * Url to the banner, the image representing the quest on the Lodestone, 376x120 pixels
     */
    protected String bannerUrl;
    /**
     * NPC you talk to to start the quest
     */
    protected NPC questGiver;
    /**
     * Class required to start the quest (from my understanding, only relevant for game-starting quests)
     */
    protected Job startingClass;
    /**
     * Class required for the quest (from my understanding, the actual restriction you need to worry about)
     */
    protected String classRequirement;
    /**
     * Level required for the quest
     */
    protected Integer levelRequirement;
    /**
     * List of actions (i.e. Blue Mage spell learned) required for this quest
     */
    protected List<String> actionRequirements = new ArrayList<>();
    /**
     * If present, Grand Company membership required to undertake the quest
     */
    protected GrandCompany grandCompany;
    /**
     * If present, Grand Company rank required to undertake the quest
     */
    protected GrandCompany.Rank grandCompanyRank;
    /**
     * List of names of the duties that must be completed before this quest is available
     */
    protected List<String> requiredDuties = new ArrayList<>();
    /**
     * Experience points awarded for completing this quest
     */
    protected Integer xp;
    /**
     * Gil awarded for completing this quest
     */
    protected Integer gil;
    /**
     * Grand company seals awarded for completing this quest
     */
    protected Integer grandCompanySeals;
    /**
     * Ventures (retainer currency) awarded for completing this quest
     */
    protected Integer ventures;
    /**
     * If it is a beast tribe quest, beast tribe granting the quest
     */
    protected String beastTribe;
    /**
     * Beast tribe currency (Steel Amalj'ok, Sylphic Goldleaf, etc.) awarded for completing this quest
     */
    protected StringCountBag beastTribeCurrency;
    /**
     * Number of tomestomes awarded for completing this quest
     */
    protected StringCountBag tomestones;
    /**
     * Amount of beast tribe reputation awarded for completing this quest
     */
    protected Integer beastTribeReputation;
    /**
     * Ordinary completion rewards: you get all these items for completing the quest
     */
    protected List<LidCountBag> completionRewards = new ArrayList<>();
    /**
     * Class-dependent rewards: you get a number of these items, determined by your class, for completing this quest
     */
    protected Map<String, LidCountBag> classRewards = new HashMap<>();
    /**
     * Gender-dependent rewards: you get either the rewards for the male or for the female character
     */
    protected Map<String, List<LidCountBag>> genderRewards = new TreeMap<>();
    /**
     * Other completion rewards: skills, actions and emotes
     */
    protected List<ActionIconBag> actionRewards = new ArrayList<>();
    /**
     * Optional rewards: you may choose one of these items to get in addition to the completion reward
     */
    protected List<LidCountBag> optionalRewards = new ArrayList<>();
    /**
     * Is this quest part of a season event?
     */
    protected boolean isSeasonal;

    private static final String[] allBeastTribeCurrencies = new String[]{"Ixali Oaknot", "Rainbowtide Psashp", "Steel Amalj", "Sylphic Goldleaf", "Titan Cobaltpiece", "Vanu Whitebone", "Black Copper Gil", "Carved Kupo Nut",
            "Ananta Dreamstaff", "Kojin Sango", "Namazu Koban"};

    /**
     * Static factory method to instantiate a quest from a Lodestone id
     * @param lid Lodestone id
     * @return Instance of {@link Quest}
     * @throws Exception for various parsing issues
     */
    public static Quest get(Lid lid) throws Exception {
        return new Quest(lid);
    }

    private Quest(Lid lid) throws Exception {
        this.lid = lid;
        genderRewards.put("Male", new ArrayList<>());
        genderRewards.put("Female", new ArrayList<>());
        URL url = new URL("https://na.finalfantasyxiv.com/lodestone/playguide/db/quest/" + lid.toString());
        Document doc = Jsoup.connect(url.toString()).get();
        category = doc.select(".db-view__detail__content_type").first().text();
        type = findQuestType(doc.select(".db-view__detail__lname_name").first().parent().className());
        title = doc.select(".db-view__detail__lname_name").first().text();
        level = parseLevel(doc.select(".db-view__detail__level").text());
        bannerUrl = doc.select(".db-view__detail__visual > img:nth-child(1)").attr("src");
        Elements season = doc.select(".db-view__quest__past_season_event");
        isSeasonal = (season != null && season.first() != null);

        if(!isSeasonal) {
            String npcName = doc.select("div.db-table__wrapper--npc_content a.db_popup > strong:nth-child(1)").first().text();
            String npcLid = Lid.parseLid(doc.select("div.db-table__wrapper--npc_content a.db_popup").first().attr("href"));
            Coords coords = parseCoords(doc.select(".db-view__npc__location__list > li > ul > li").first().text());
            Element areaEl = doc.select(".db-view__npc__location__list > li:nth-child(1)").first();
            area = "";
            for(Node n : areaEl.childNodes()) {
                if(n.nodeName().equals("#text"))
                    area += n.toString();
                else if (n.nodeName().equals("i"))
                    area += ((Element) n).text();
                else if (n.nodeName().equals("ul"))
                    break;
                else
                 throw new Exception("Problem parsing area name...");
            }
            area = area.trim();
            questGiver = new NPC(npcName, new Lid(npcLid), area, coords);
        }
        String jobString = doc.select("dl.db-view__data__detail_list:nth-child(1) > dd:nth-child(2)").first().text();
        if(!jobString.equals("Not specified"))
            startingClass = Job.getFromName(jobString);
        Element restrictionEl = doc.select("dl.db-view__data__detail_list:nth-child(2) > dd:nth-child(2)").first();
        setClassRequirement(restrictionEl);
        setGC(doc.select("dl.db-view__data__detail_list:nth-child(3) > dd:nth-child(2)").first().text());
        List<TextNode> reqDutiesNodes = doc.select(".db-view__data__detail_list__wrapper > dl:nth-child(4) > dd:nth-child(2)").first().textNodes();
        for(TextNode node : reqDutiesNodes) {
            if(!node.text().trim().equals("Not specified") && !node.text().trim().equals("All of the above quests complete") && !node.text().equals(" ")) {
                requiredDuties.add(node.text());
            }
        }
        xp = parseNullableInt(doc.select("li.db-view__quest__reward__quest_exp > div:nth-child(2)"));
        gil = parseNullableInt(doc.select("li.db-view__quest__reward__quest_gil > div:nth-child(2)"));
        grandCompanySeals = parseNullableInt(doc.select("li.db-view__quest__reward__seals_common > div:nth-child(2)"));
        ventures = parseNullableInt(doc.select("ul.db-view__quest__reward__list:nth-child(6) > li:nth-child(1) > div.db-view__quest__reward__name:containsOwn(Venture) + div.db-view__quest__reward__value"));
        beastTribe = parseBeastTribeName(doc.select("ul.db-view__quest__reward__list > li:nth-child(1) > div.db-view__quest__reward__name:containsOwn(Relations)"));
        beastTribeCurrency = findBeastTribeCurrencies(doc);
        tomestones = findTomestones(doc);
        beastTribeReputation = parseNullableInt(doc.select("ul.db-view__quest__reward__list > li:nth-child(1) > div.db-view__quest__reward__name:containsOwn(Relations) + div.db-view__quest__reward__value"));
        Element rewardsParent = doc.selectFirst(".db-view__data");
        RewardsBag rb = findRewardsElements(rewardsParent);
        setCompletionRewards(rb.completionRewards);
        setItemRewards(rb.optionalRewards, optionalRewards);
    }

    private RewardsBag findRewardsElements(Element rewardsParent) {
        Elements els = rewardsParent.select("div.db-view__data__inner--quest_reward");
        Elements crels = new Elements();
        Elements orls = rewardsParent.select(".db-view__data__inner--select_reward ul.db-view__data__item_list div.db-view__data__reward__item__name");
        for(Element el : els) {
            crels.addAll(el.select("div.db-view__data__inner__wrapper > ul"));
        }
        return new RewardsBag(crels, orls);
    }

    private static String findQuestType(String className) throws Exception {
        switch (className) {
            case "db-view__icon db-view__icon--common_repeat":
                return "Side Quest, Repeatable";
            case "db-view__icon db-view__icon--common_normal":
                return "Side Quest";
            case "db-view__icon db-view__icon--main":
                return "Main Story Quest";
            case "db-view__icon db-view__icon--function_normal":
                return "Feature Quest";
            case "db-view__icon db-view__icon--function_repeat":
                return "Feature Quest, Repeatable";
            default:
                throw new Exception("Unknown class on html element for quest type: '" + className + "'");
        }
    }

    /**
     * Utility toString method
     * @return A string like so "A Fiendish Likeness @40020a8a0b5 in The Rising Stones (lvl 60) [Heavensward Primal Quests]"
     */
    public String toString() {
        return title + " @" + lid.toString() + " in " + area + " (lvl " + level + ") [" + category + "]";
    }

    /**
     * Utility method to print the full information about this quest to standard output
     */
    public void print() {
        StringBuilder actualCompletionRewards = new StringBuilder();
        actualCompletionRewards.append("[");
        if(completionRewards.size() > 0)
            for(LidCountBag lcb : completionRewards)
                actualCompletionRewards.append(lcb.toString()).append(", ");
        if(classRewards.size() > 0)
            for(Map.Entry<String, LidCountBag> en : classRewards.entrySet())
                actualCompletionRewards.append(en.getKey()).append("=").append(en.getValue().toString()).append(", ");
        if(genderRewards.get("Male").size() > 0 || genderRewards.get("Female").size() > 0)
            for(Map.Entry<String, List<LidCountBag>> gl : genderRewards.entrySet()) {
                actualCompletionRewards.append(gl.getKey()).append(": ");
                for (LidCountBag lcb : gl.getValue())
                    actualCompletionRewards.append(lcb.toString()).append(", ");
            }
        if(actionRewards.size() > 0)
            for(ActionIconBag rew : actionRewards)
                actualCompletionRewards.append(rew.toString()).append(", ");
        actualCompletionRewards.append("]");

        System.out.println("QUEST " + title + " @ " + lid);
        System.out.println("\t" + category + " in " + area + " (lvl " + level + ")");
        System.out.println("\tBanner: " + bannerUrl);
        System.out.println("\tBy: " + (questGiver != null ? questGiver.getName() : "Unavailable") + " for " + startingClass);
        System.out.println("\tMust be: " + classRequirement + " (lvl " + levelRequirement + ")");
        System.out.println("\tGC limited to: " + grandCompanyRank + " in " + grandCompany);
        System.out.println("\tDuty limited to: " + requiredDuties.toString());
        System.out.println("\t" + String.format("%5d xp   %5d gil  %5d seals  %5d ventures", xp, gil, grandCompanySeals, ventures));
        System.out.println("\t" + String.format("%5d btc  %5d rep  %5d tome", beastTribeCurrency.getCount(), beastTribeReputation, tomestones.getCount()));
        System.out.println("\tCompletion rewards: " + actualCompletionRewards.toString());
        System.out.println("\tOptional rewards: " + optionalRewards.toString());
    }

    private int parseLevel(String mumbo) throws Exception {
        Pattern pattern = Pattern.compile("Lv\\.\\s(\\d+)");
        Matcher matcher = pattern.matcher(mumbo);
        if(matcher.find() && matcher.groupCount()==1)
            return Integer.parseInt(matcher.group(1));
        else throw new Exception("Can't figure out quest level from '" + mumbo + "'");
    }

    private void setGC(String mumbo) throws Exception {
        if(mumbo.equals("Not specified")) {
            grandCompany = null;
            grandCompanyRank = null;
        } else {
            Pattern pattern = Pattern.compile("(.+)\\s/\\sOver\\s(.+)$");
            Matcher matcher = pattern.matcher(mumbo);
            if (matcher.find() && matcher.groupCount() == 2) {
                grandCompany = GrandCompany.get(matcher.group(1));
                grandCompanyRank = grandCompany.getRank(matcher.group(2));
            } else {
                grandCompany = GrandCompany.get(mumbo);
                grandCompanyRank = null;
            }
        }
    }

    private Integer parseNullableInt(Elements el) {
        try {
            Integer a = Integer.parseInt(el.first().text());
            return a;
        } catch (Exception e) {
            return 0;
        }
    }

    private void setClassRequirement(Element restrictionEl) throws Exception {
        String jobText;
        if(restrictionEl.select("ul.db-view__data__action").first() != null) {
            jobText = restrictionEl.textNodes().get(0).text().trim();
            String actionText = restrictionEl.select("ul.db-view__data__action li div.db-view__data__reward__item__name").text().trim();
            actionRequirements.add(actionText);
        } else if(restrictionEl.textNodes().size() > 2) { // cause some dailies have two lines...
            jobText = restrictionEl.textNodes().get(1).text().trim();
        } else {
            jobText = restrictionEl.textNodes().get(0).text().trim();
        }
        Pattern pattern = Pattern.compile("^(.+)\\sLv\\.\\s(\\d+)$");
        Matcher matcher = pattern.matcher(jobText);
        if (matcher.find() && matcher.groupCount() == 2) {
            classRequirement = matcher.group(1);
            levelRequirement = Integer.parseInt(matcher.group(2));
        } else throw new Exception("Can't figure out class and level restrictions from " + restrictionEl.text());
    }

    private String parseBeastTribeName(Elements els) {
        try {
            String mumbo = els.first().text();
            mumbo = mumbo.substring(0, mumbo.indexOf(" Relations"));
            switch (mumbo) {
                case "Sylphic":
                    return "Sylphs";
                case "Kobold":
                    return "Kobolds";
                case "Ixali":
                    return "Ixal";
                case "Vanu":
                    return "Vanu Vanu";
                case "Moogle":
                    return "Moogles";
                default:
                    return mumbo;
            }
        } catch (NullPointerException e){
            return null;
        }
    }

    private StringCountBag findBeastTribeCurrencies(Document doc) throws Exception {
        int currenciesFound = 0;
        int value = 0;
        String name = null;
        for(String btc : allBeastTribeCurrencies) {
            try {
                name = doc.select("div.db-view__quest__reward__name:containsOwn(" + btc + ")").first().text();
                String n = doc.select("div.db-view__quest__reward__name:containsOwn(" + btc + ") + div.db-view__quest__reward__value").first().text();
                ++currenciesFound;
                value = Integer.parseInt(n);
            } catch (NullPointerException e) {}
        }
        if(currenciesFound == 1)
            return new StringCountBag(name, value);
        else if(currenciesFound == 0)
            return new StringCountBag(null, 0);
        else
            throw new Exception("More than one beast tribe currency found? at lid " + lid);
    }

    private StringCountBag findTomestones(Document doc) {
        String name = null;
        try {
            name = doc.select("ul.db-view__quest__reward__list:nth-child(7) > li:nth-child(1) > div.db-view__quest__reward__name:containsOwn(Tomestone)").first().text();
        } catch (NullPointerException e) {}
        Integer value = parseNullableInt(doc.select("ul.db-view__quest__reward__list:nth-child(7) > li:nth-child(1) > div.db-view__quest__reward__name:containsOwn(Tomestone) + div.db-view__quest__reward__value"));
        return new StringCountBag(name, value);
    }

    /**
     * Getter for quest type
     * @return Type (MSQ, unlock, etc.)
     */
    public String getType() {
        return type;
    }



    private void setCompletionRewards(Elements rels) throws Exception {
        if(rels.size() > 0) {
            for (Element el : rels) {
                Elements wrapper = el.select(".db-view__data__reward__item__name__wrapper");
                if (wrapper != null && wrapper.size() > 0 && wrapper.first() != null) {
                    if (isAGenderReward(wrapper)) {
                        addGenderReward(wrapper.first());
                    } else if(isAClassReward(wrapper)) {
                        addClassReward(wrapper.first());
                    } else {
                        addItemReward(wrapper.first(), completionRewards);
                    }
                } else {
                    addActionReward(el);
                }
            }
        }
    }
    private static boolean isAGenderReward(Elements wrapper) {
        if(wrapper.size() > 0) {
            return hasGender(JsoupUtils.actualTextCommaTrimmed(wrapper.first()));
        } else
            return false;
    }
    private static boolean hasGender(List<String> strings) {
        for(String n : strings) {
            if(n.equals("Male") || n.equals("Female"))
                return true;
        }
        return false;
    }
    private static boolean isAClassReward(Elements wrapper) {
        if(wrapper.size() > 0) {
            return hasClass(JsoupUtils.actualTextCommaTrimmed(wrapper.first()));
        } else
            return false;
    }
    private static boolean hasClass(List<String> strings) {
        for(String n : strings) {
            if(!isInteger(n))
                return true;
        }
        return false;
    }

    private static boolean isInteger(String text) {
        try {
            Integer i = Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void addGenderReward(Element el) throws Exception {
        try {
            Lid rewardLid = new Lid(Lid.parseLid(el.select("a.db_popup").first().attr("href")));
            Elements wrapper = el.select(".db-view__data__reward__item__name__wrapper");
            String gender = wrapper.first().textNodes().get(0).text();
            genderRewards.get(gender).add(new LidCountBag(rewardLid, 1));
        } catch (NullPointerException e) {
            // No reward
        }
    }

    private void addActionReward(Element el) {
        List<TextNode> els = el.select("div.db-view__data__reward__item__name").first().textNodes();
        String name = null;
        for(int i = 0; i < els.size(); i++) {
            if (!els.get(i).text().equals(" "))
                name = els.get(i).text().trim();
        }
        String url = el.select("img").first().attr("src");
        actionRewards.add(new ActionIconBag(name, url));
    }

    private void addClassReward(Element el) throws Exception {
        try {
            String classAbbrev;
            int n = 1;
            Lid rewardLid = new Lid(Lid.parseLid(el.select("a.db_popup").first().attr("href")));
            Element wrapper = el.select(".db-view__data__reward__item__name__wrapper").first();
            if(wrapper.textNodes().size() == 2) {
                classAbbrev = wrapper.textNodes().get(1).text();
                n = Integer.parseInt(wrapper.textNodes().get(0).text());
            } else {
                classAbbrev = wrapper.textNodes().get(0).text();
            }
            classRewards.put(classAbbrev, new LidCountBag(rewardLid, n));
        } catch (NullPointerException e) {
            // No reward
        }
    }

    private void addItemReward(Element el, List<LidCountBag> rewards) throws Exception {
        try {
            Lid rewardLid = new Lid(Lid.parseLid(el.select("a.db_popup").first().attr("href")));
            Elements wrapper = el.select(".db-view__data__reward__item__name__wrapper");
            int n = findNumberIfTheresOne(wrapper);
            rewards.add(new LidCountBag(rewardLid, n));
        } catch (NullPointerException e) {
            // No reward
        }
    }

    private static int findNumberIfTheresOne(Elements wrapper) {
        if(wrapper != null) {
            if(wrapper.size() > 0) {
                List<String> tns = JsoupUtils.actualTextCommaTrimmed(wrapper.first());
                for(String n : tns) {
                    try {
                        int i = Integer.parseInt(n);
                        return i;
                    } catch (NumberFormatException e) {}
                }
                return 1;
            } else {
                return 1;
            }
        } else
            return 1;
    }

    private void setItemRewards(Elements els, List<LidCountBag> rewards) throws Exception {
        for(Element el : els) {
            addItemReward(el, rewards);
        }
    }

    /**
     * Getter for the quest category (for ex. Heavensward Primal Quests)
     * @return Quest category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Getter for the quest title
     * @return Quest title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for the area where you can pick up the quest
     * @return Area where you can pick up the quest
     */
    public String getArea() {
        return area;
    }

    /**
     * Getter for the quest level
     * @return Quest level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Getter for the Lodestone id
     * @return Lodestone id
     */
    public Lid getLid() {
        return lid;
    }

    /**
     * Getter for the url of the banner used to illustrate this quest on the Lodestone
     * @return Url for the banner
     */
    public String getBannerUrl() {
        return bannerUrl;
    }

    /**
     * Getter for the {@link NPC} granting this quest
     * @return NPC granting this quest
     */
    public NPC getQuestGiver() {
        return questGiver;
    }

    /**
     * Getter for the starting class of this quest
     * @return Starting class
     */
    public Job getStartingClass() {
        return startingClass;
    }

    /**
     * Getter for the class/job requirement for this quest
     * @return Class/job requirement
     */
    public String getClassRequirement() {
        return classRequirement;
    }

    /**
     * Getter for the level requirement for this quest
     * @return Level requirement
     */
    public Integer getLevelRequirement() {
        return levelRequirement;
    }

    /**
     * Getter for the Grand Company requirement or this quest
     * @return Grand Company required
     */
    public GrandCompany getGrandCompany() {
        return grandCompany;
    }

    /**
     * Getter for the Grand Company rank requirement for this quest
     * @return Grand Company rank
     */
    public GrandCompany.Rank getGrandCompanyRank() {
        return grandCompanyRank;
    }

    /**
     * Getter for the list of completed duties or quests required for this quest
     * @return List of required duties or quests
     */
    public List<String> getRequiredDuties() {
        return requiredDuties;
    }

    /**
     * Getter for the experience points rewarded for this quest
     * @return Experience points
     */
    public Integer getXp() {
        return xp;
    }

    /**
     * Getter for the gil rewarded for this quest
     * @return Gil
     */
    public Integer getGil() {
        return gil;
    }

    /**
     * Getter for the number of Grand Company seals awarded for this quest
     * @return Grand Company seals
     */
    public Integer getGrandCompanySeals() {
        return grandCompanySeals;
    }

    /**
     * Getter for the number of ventures awarded for this quest
     * @return Ventures
     */
    public Integer getVentures() {
        return ventures;
    }

    /**
     * Getter for the number of beast tribe currency (ex. Sylphic Goldleaf) awarded for this quest
     * @return Beast tribe currency amount
     */
    public StringCountBag getBeastTribeCurrency() {
        return beastTribeCurrency;
    }

    /**
     * Getter for the number of tomestones awarded for this quest
     * @return Tomestones
     */
    public StringCountBag getTomestones() {
        return tomestones;
    }

    /**
     * Getter for the amount of Beast tribe reputation awarded for this quest
     * @return Beast tribe reputation
     */
    public Integer getBeastTribeReputation() {
        return beastTribeReputation;
    }

    /**
     * Getter for the list of regular rewards (item, number) awarded for completing this quest
     * @return List of (item, number) completion rewards
     */
    public List<LidCountBag> getCompletionRewards() {
        return completionRewards;
    }

    /**
     * Getter for the combinations of (class/job, (item, number)) awarded for completing this quest
     * @return Map of class-dependent completion rewards
     */
    public Map<String, LidCountBag> getClassRewards() {
        return classRewards;
    }


    /**
     * Getter for the other rewards (skills, actions, emotes) awarded for completing this quest
     * @return List of actions
     */
    public List<ActionIconBag> getActionRewards() {
        return actionRewards;
    }

    /**
     * Getter for the list of optional rewards
     * @return List of optional rewards
     */
    public List<LidCountBag> getOptionalRewards() {
        return optionalRewards;
    }

    /**
     * Getter for the combinations of (gender, (item, count)) awarded for completing this quest
     * @return Map of (gender, (item, count))
     */
    public Map<String, List<LidCountBag>> getGenderRewards() {
        return genderRewards;
    }

    /**
     * Getter for the beast tribe (if any) granting this quest
     * @return Beast tribe name
     */
    public String getBeastTribe() {
        return beastTribe;
    }

    /**
     * Getter for whether this quest is part of a seasonal event
     * @return Is the quest seasonal?
     */
    public boolean isSeasonal() {
        return isSeasonal;
    }

    /**
     * Getter for the list of actions (i.e. Blue Mage spells learned) required for this quest
     * @return List of spell names
     */
    public List<String> getActionRequirements() {
        return actionRequirements;
    }
}
