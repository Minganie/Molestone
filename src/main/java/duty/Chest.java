package duty;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Coords;
import util.Lid;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to represent a free chest (i.e. a chest not dropped by a boss encounter)
 */
public class Chest {
    private int index;
    /**
     * {@link Coords} : x and y coordinates of the chest location in the duty map
     */
    private Coords coords;
    /**
     * Set of Lodestone ids of the items possibly in this chest
     */
    private Set<Lid> items;

    /**
     * Constructor
     * @param box Div on the Lodestone holding this chest and its content
     * @throws Exception for various parsing issues
     */
    Chest(Element box) throws Exception {
        String coordsText = box.select(".db-view__treasure_box_popup__map_coordinates").text();
        coords = Coords.parseCoords(coordsText);
        items = getLids(box);
        index = parseIndex(box.select("h1.db-view__treasure_box_popup__title").text().trim());
    }

    private int parseIndex(String title) throws Exception {
        if(title.trim().equals("Treasure Coffer"))
            return 1;
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(title);
        if(matcher.find() && matcher.groupCount() == 1)
            return Integer.parseInt(matcher.group(1));
        else
            throw new Exception("Unable to find chest index from text '" + title + "'");
    }

    /**
     * Static method to get Lodestone ids for the contents of this chest
     * @param box Div on the Lodestone holding this chest and its content
     * @return Set of Lodestone ids
     * @throws Exception for various parsing issues
     */
    static Set<Lid> getLids(Element box) throws Exception {
        Set<Lid> lids = new HashSet<>();
        if(box != null) {
            Elements items = box.select("div.sys_treasure_box a.db_popup");
            for (Element item : items) {
                lids.add(Lid.parseLid(item.attr("href")));
            }
        }
        return lids;
    }

    /**
     * Getter for the index (i.e. Treasure Chest #idx)
     * @return index of this chest
     */
    public int getIndex() {
        return index;
    }

    /**
     * Getter for the coordinates of this chest
     * @return coordinates of this chest
     */
    public Coords getCoords() {
        return coords;
    }

    /**
     * Getter for the contents of this chest
     * @return Set of Lodestone ids for the contents of this chest
     */
    public Set<Lid> getItems() {
        return items;
    }

    /**
     * Utility toString method
     * @return A string like so "@[13.8, 25.4] containing 3 items"
     */
    @Override
    public String toString() {
        return "#" + index + " @" + coords.toString() + " containing " + items.size() + " items";
    }
}
