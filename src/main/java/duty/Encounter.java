package duty;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * One boss encounter
 */
public class Encounter {
    /**
     * Set of boss names for this encounter (ex. "Diabolos" or "Annia Quo Soranus and Julia Quo Soranus");
     * it's a set because some "gauntlet" type encounters use the same mob twice
     */
    protected Set<Lid> bossList = new HashSet<>();
    /**
     * Combinations of (token, number) awarded for this encounter
     */
    protected Map<String, Integer> tokens = new HashMap<>();
    /**
     * Set of Lodestone ids for the items awarded for this encounter;
     * it's a set because some encounters will award the same item several times, or a normal and high quality
     * version of the same item
     */
    protected Set<Lid> loot;
    /**
     * Order of occurrence of this encounter in its duty
     */
    protected int index;

    /**
     * Constructor
     * @param encounterBox Div for this encounter in the Lodestone page for its duty
     * @param index Order of occurrence of this encounter in its duty
     * @throws Exception for various parsing issues
     */
    Encounter(Element encounterBox, int index) throws Exception {
        this.index = index;
        // One encounter may involve several mobs...
        parseBossList(encounterBox);

        // poetics, etc.
        parseTokens(encounterBox);

        // in the boss chest(s)
        loot = Chest.getLids(encounterBox);

        // cards, orchestrion, etc.
        parseNotChestedLoot(encounterBox);
    }

    /**
     * Parse information for loot that is not chested, typically Triple Triad Cards
     * @param encounterBox Div for this encounter in the Lodestone page for its duty
     * @throws Exception for various parsing issues
     */
    private void parseNotChestedLoot(Element encounterBox) throws Exception {
        if(encounterBox != null) {
            Elements rewards = encounterBox.select(".db-view__data__reward__item a.db_popup");
            for (Element reward : rewards) {
                Lid lid = Lid.parseLid(reward.attr("href"));
                loot.add(lid);
            }
        }
    }

    /**
     * Parse information for the tokens awarded for this encounter
     * @param encounterBox Div for this encounter in the Lodestone page for its duty
     * @throws ParseException for various parsing issues
     */
    private void parseTokens(Element encounterBox) throws ParseException {
        if(encounterBox != null) {
            Elements tokenRewards = encounterBox.select("div.db-view__data__reward__token");
            for (Element tokenLine : tokenRewards) {
                int n = Integer.parseInt(tokenLine.select(".db-view__data__reward__token--value").text());
                String t = tokenLine.select(".db-view__data__reward__token--name").text().trim();
                tokens.put(t, n);
            }
        }
    }

    /**
     * Parse information about the various mobs fighting in this encounter
     * @param encounterBox Div for this encounter in the Lodestone page for its duty
     * @throws Exception for various parsing issues
     */
    private void parseBossList(Element encounterBox) throws Exception {
        if(encounterBox != null) {
            Elements bosses = encounterBox.select("ul.db-view__data__boss_list li");
            for (Element boss : bosses) {
                bossList.add(Lid.parseLid(boss.select("a").attr("href")));
            }
        }
    }

    /**
     * Utility toString method
     * @return A string like so "[Annia Quo Soranus, Julia Quo Soranus] [40 Allagan Tomestone of Mendacity, 50 Allagan Tomestone of Genesis] and 16 items"
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if(tokens.size() > 0) {
            for (Map.Entry<String, Integer> e : tokens.entrySet()) {
                b.append(e.getValue() + " " + e.getKey() + ", ");
            }
            b.delete(b.length() - 2, b.length());
        }
        return bossList + " [" + b.toString() + "] and " + loot.size() + " items";
    }

    /**
     * Getter for the order of occurrence of this encounter in its duty
     * @return Order of occurrence of this encounter in its duty
     */
    public int getIndex() {
        return index;
    }

    /**
     * Getter for the list of mobs fighting in this encounter
     * @return Set of {@link Lid} fighting in this encounter
     */
    public Set<Lid> getBossList() {
        return bossList;
    }

    /**
     * Getter for the combinations of (token, number) awarded for this encounter
     * @return Map of (token, number) for this encounter
     */
    public Map<String, Integer> getTokens() {
        return tokens;
    }

    /**
     * Getter for the list of Lodestone ids for the items awarded for this encounter
     * @return Set of Lodestone ids for this encounter's loot
     */
    public Set<Lid> getLoot() {
        return loot;
    }

    /**
     * Print full information about this encounter to standard output
     */
    public void print() {
        StringBuilder b = new StringBuilder();
        if(tokens.size() > 0) {
            for (Map.Entry<String, Integer> e : tokens.entrySet()) {
                b.append(e.getValue() + " " + e.getKey() + ", ");
            }
            b.delete(b.length() - 2, b.length());
        }
        System.out.println("    Encounter " + bossList + " [" + b.toString() + "] with loot:");
        for(Lid lid : loot) {
            System.out.println("      " + lid.toString());
        }
    }

    /**
     * Get the pretty-formatted list of mobs fighting in this encounter
     * @return A string like so "Mumuepo The Beholden, Franz The Fair, Langloisiert The Lionheart, Narasimha, Silent Moss The Solemn, U'linbho The Sand Devil"
     */
    public String getBossNames() {
        StringBuilder b = new StringBuilder();
        if(bossList.size() > 0) {
            for(Lid boss : bossList) {
                b.append(boss.get() + ", ");
            }
            b.delete(b.length()-2, b.length());
        }
        return b.toString();
    }
}
