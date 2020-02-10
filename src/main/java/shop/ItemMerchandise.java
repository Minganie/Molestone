package shop;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;
import util.LidQCountBag;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Subclass of {@link Merchandise} where what you buy is one or several items of normal or high quality
 */
public class ItemMerchandise extends Merchandise {
    private List<LidQCountBag> items = new ArrayList<>();

    /**
     * Constructor
     * @param td Html element containing the item merchandise
     * @throws Exception for various parsing issues
     */
    public ItemMerchandise(Element td) throws Exception {
        Elements items;
        if(hasMoreThanOne(td))
            items = td.select("ul > li");
        else
            items = new Elements(td);
        for(Element item : items) {
            Lid lid = Lid.parseLid(item.select("a.db_popup").first().attr("href"));
            int n = 1;
            try {
                n = Integer.parseInt(item.select("span.db-view__data__number").first().html());
            } catch (NullPointerException npe) {}
            boolean q = item.select("a.db_popup").first().attr("href").contains("hq=1");
            addItem(n, lid, q);
        }
    }

    private static boolean hasMoreThanOne(Element td) {
        return td.select(":root > ul > li").size() > 0;
    }

    private void addItem(int n, Lid lid, boolean hq) {
        items.add(new LidQCountBag(lid, hq, n));
    }

    /**
     * Getter for the items you will get for this transaction
     * @return List of {@link LidQCountBag} i.e. (number, quality, item's Lodestone id)
     */
    public List<LidQCountBag> getItems() {
        return items;
    }

    /**
     * Utility toString method
     * @return A pretty string like "3x58af1380e89*"
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(LidQCountBag bag : items) {
            sb.append(String.format("%dx%s%s, ", bag.getN(), bag.getLid(), (bag.isHq() ? "*" : "")));
        }
        return sb.toString();
    }


}
