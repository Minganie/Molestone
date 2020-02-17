package shop;

import org.jsoup.nodes.Element;
import util.StringCountBag;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Subclass of {@link Merchandise} to represent an instance where you buy a Free Company action (see {@link Action})
 */
public class ActionMerchandise extends Merchandise {
    private Action action;

    /**
     * Constructor
     * @param td Html element containing this merchandise
     * @throws Exception for various parsing issues
     */
    public ActionMerchandise(Element td) throws Exception {
        String name = td.select("div:nth-child(2) > h4:nth-child(1)").text().trim();
        String licon = td.select("div:first-child > div > img").first().attr("src");
        String helpText = td.select("div:nth-child(2) > p.db-shop__list__help").first().text();
        StringCountBag scb = parseHelpText(helpText);
        action = new Action(name, licon, scb.getThingy(), scb.getCount());
    }

    private static StringCountBag parseHelpText(String helpText) throws Exception {
        Pattern pattern = Pattern.compile("^(.+?)Duration:.*?(\\d+)h.*?$");
        Matcher matcher = pattern.matcher(helpText);
        if(matcher.find() && matcher.groupCount() == 2) {
            String effect = matcher.group(1).trim();
            int duration = Integer.parseInt(matcher.group(2));
            return new StringCountBag(effect, duration);
        } else
            throw new Exception("Unable to find FC Action's effect and duration from '" + helpText + "'");
    }

    /**
     * Getter for the name of the action
     * @return Name of the action
     */
    public String getName() {
        return action.getName();
    }

    /**
     * Getter for the Lodestone url of the icon of this action
     * @return Lodestone url of the icon of this action
     */
    public String getLicon() {
        return action.getLicon();
    }

    /**
     * Getter for the description of the effect of this action
     * @return Description of the effect of this action
     */
    public String getEffect() {
        return action.getEffect();
    }

    /**
     * Getter for the number of hours this action will last
     * @return Number of hours this action will last
     */
    public int getDurationHours() {
        return action.getDurationHours();
    }

    /**
     * Utility toString method
     * @return A prettty String like "(Action) The Heat of Battle"
     */
    @Override
    public String toString() {
        return "(Action) " + action.toString();
    }
}
