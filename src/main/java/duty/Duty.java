package duty;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.Lid;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to represent a duty you may undertake in the game
 */
public abstract class Duty {
    /**
     * Name of the duty (without (Hard))
     */
    protected String name;
    /**
     * Url of the banner used on the Lodestone to represent this duty
     */
    protected String banner;
    /**
     * Level of the duty as seen in the duty finder
     */
    protected int level;
    /**
     * Lodestone unique identifier, 11 alphanumeric characters
     */
    protected Lid lid;
    /**
     * Difficulty level: Regular, Hard, Extreme, Savage, Ultimate
     */
    protected Difficulty difficulty = null;
    /**
     * Experience usually awarded for completing this duty
     */
    protected Integer completionXp = null;
    /**
     * Gil usually awarded for completing this duty
     */
    protected Integer completionGil = null;
    /**
     * Bonus experience awarded for completing this duty
     */
    protected Integer bonusXp = null;
    /**
     * Bonus gil awarded for completing this duty
     */
    protected Integer bonusGil = null;
    /**
     * Free chests found somewhere in this duty (dungeons and raids only)
     */
    protected List<Chest> chests = new ArrayList<>();
    /**
     * Boss encounters in this duty (dungeons, trials and raids)
     */
    protected List<Encounter> encounters = new ArrayList<>();
    /**
     * Tokens awarded for completing this duty (ex. Allagan Tomestone of Poetics)
     */
    protected Map<String, Integer> tokens = new HashMap<>();
    /**
     * Experience awarded for finishing this pvp duty in 1st place
     */
    protected Integer pvpXpRank1 = null;
    /**
     * Wolf marks awarded for finishing this pvp duty in 1st place
     */
    protected Integer wolfMarksRank1 = null;
    /**
     * Experience awarded for finishing this pvp duty in 2nd place
     */
    protected Integer pvpXpRank2 = null;
    /**
     * Wolf marks awarded for finishing this pvp duty in 2nd place
     */
    protected Integer wolfMarksRank2 = null;
    /**
     * Experience awarded for finishing this pvp duty in 3rd place
     */
    protected Integer pvpXpRank3 = null;
    /**
     * Wolf marks awarded for finishing this pvp duty in 3rd place
     */
    protected Integer wolfMarksRank3 = null;

    /**
     * Factory method to get a {@link Duty} from a {@link Lid} (lodestone id)
     * @param lid Lodestone id
     * @return Duty
     * @throws Exception for various parsing issues
     */
    public static Duty get(Lid lid) throws Exception {
        URL url = new URL("https://na.finalfantasyxiv.com/lodestone/playguide/db/duty/" + lid.toString());
        Document doc = Jsoup.connect(url.toString()).get();
        return DutyFactory.getDuty(doc, lid);
    }

    /**
     * Duty constructor; package-private
     * @param doc Jsoup document for the Lodestone page of this duty
     * @param lid Lodestone id
     * @throws Exception for various parsing issues
     */
    Duty(Document doc, Lid lid) throws Exception {
        this.lid = lid;
        String nameAndDifficulty = doc.select(".db-view__detail__lname_name").text();
        parseNameAndDifficulty(nameAndDifficulty);
        level = parseLevel(doc);
        banner = parseBanner(doc);
        parseDetails(doc);
    }

    /**
     * Get the duty level from the Lodestone page of this duty
     * @param doc Jsoup document for the Lodestone page of this duty
     * @return Duty level as an int
     * @throws ParseException for various parsing issues
     */
    private int parseLevel(Document doc) throws ParseException {
        String lvlTxt = doc.select(".db-view__detail__level").first().text();
        Pattern pattern = Pattern.compile("Lv..(\\d+)");
        Matcher matcher = pattern.matcher(lvlTxt);
        if(matcher.find() && matcher.groupCount() == 1)
            return Integer.parseInt(matcher.group(1));
        else
            throw new ParseException("Can't find duty level in '" + lvlTxt + "'", 0);
    }

    /**
     * Get the duty banner (the image used on the Lodestone to illustrate this duty, 376x120 pixels) from the
     * Lodestone page for this duty
     * @param doc Jsoup document for the Lodestone page of this duty
     * @return Duty banner url
     */
    private String parseBanner(Document doc) {
        Element img = doc.select(".db-view__detail__visual > img:nth-child(1)").first();
        return img.attr("src");
    }

    /**
     * Method to parse name and difficulty from the duty name; varies by duty type and must be implemented by subclasses;
     * assigns appropriate values to the fields {@link Duty#name} and {@link Duty#difficulty}
     * @param nameAndDifficulty String like so "Amdapor Keep (Hard)" or "Solemn Trinity"
     */
    abstract void parseNameAndDifficulty(String nameAndDifficulty);

    /**
     * Method to parse duty type-specific details; varies by duty type and must be implemented by subclasses;
     * assigns values to the appropriate fields
     * @param doc Jsoup document for the Lodestone page of this duty
     * @throws Exception for various parsing issues
     */
    abstract void parseDetails(Document doc) throws Exception;

    /**
     * Utility method to print duty type-specific full information on this duty to standard output;
     * must be implemented by subclasses
     */
    public abstract void print();

    /**
     * Getter for the name of this duty
     * @return Duty name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the level of this duty
     * @return Duty level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Getter for the Lodestone id of this duty
     * @return Lodestone id
     */
    public Lid getLid() {
        return lid;
    }

    /**
     * Getter for the difficulty of this duty
     * @return Duty difficulty
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Getter for the experience points awarded for completing this duty
     * @return Completion experience points
     */
    public Integer getCompletionXp() {
        return completionXp;
    }

    /**
     * Getter for the gil awarded for completing this duty
     * @return Completion gil reward
     */
    public Integer getCompletionGil() {
        return completionGil;
    }

    /**
     * Getter for the experience points awarded as a bonus for this duty
     * @return Bonus experience points
     */
    public Integer getBonusXp() {
        return bonusXp;
    }

    /**
     * Getter for the gil awarded as a bonus for this duty
     * @return Bonus gil
     */
    public Integer getBonusGil() {
        return bonusGil;
    }

    /**
     * Getter for the list of free chests in this duty
     * @return List of free chests in this duty
     */
    public List<Chest> getChests() {
        return chests;
    }

    /**
     * Getter for the list of boss encounters in this duty
     * @return List of encounters in this duty
     */
    public List<Encounter> getEncounters() {
        return encounters;
    }

    /**
     * Getter for the duty banner url
     * @return Duty banner url
     */
    public String getBanner() {
        return banner;
    }

    /**
     * Getter for the combinations of (token, number) awarded for this duty
     * @return Map of (token, number)
     */
    public Map<String, Integer> getTokens() {
        return tokens;
    }

    /**
     * Getter for the pvp experience points awarded for completing this duty in 1st place
     * @return pvp experience for 1st place
     */
    public Integer getPvpXpRank1() {
        return pvpXpRank1;
    }

    /**
     * Getter for the wolf marks awarded for completing this duty in 1st place
     * @return wolf marks for 1st place
     */
    public Integer getWolfMarksRank1() {
        return wolfMarksRank1;
    }

    /**
     * Getter for the pvp experience points awarded for completing this duty in 2nd place
     * @return pvp experience for 2nd place
     */
    public Integer getPvpXpRank2() {
        return pvpXpRank2;
    }

    /**
     * Getter for the wolf marks awarded for completing this duty in 2nd place
     * @return wolf marks for 2nd place
     */
    public Integer getWolfMarksRank2() {
        return wolfMarksRank2;
    }

    /**
     * Getter for the pvp experience points awarded for completing this duty in 3rd place
     * @return pvp experience for 3rd place
     */
    public Integer getPvpXpRank3() {
        return pvpXpRank3;
    }

    /**
     * Getter for the wolf marks awarded for completing this duty in 3rd place
     * @return wolf marks for 3rd place
     */
    public Integer getWolfMarksRank3() {
        return wolfMarksRank3;
    }
}
