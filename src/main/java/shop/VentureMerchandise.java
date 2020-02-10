package shop;

import org.jsoup.nodes.Element;
import util.JsoupUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Subclass of {@link Merchandise} where the item you will get is a number of ventures for retainers
 */
public class VentureMerchandise extends Merchandise {
    private int n;

    /**
     * Constructor
     * @param td Html element containing the merchandise
     * @throws ParseException for various parsing issues
     */
    public VentureMerchandise(Element td) throws ParseException {
        this.n = 1;
        if(td.selectFirst("span.db-view__data__number") != null) {
            this.n = JsoupUtils.parseInt(td.selectFirst("span.db-view__data__number").text().trim());
        }
    }

    /**
     * Getter for the number of ventures
     * @return Number of ventures
     */
    public int getN() {
        return n;
    }

    /**
     * Utility toString method
     * @return A pretty string like "(Vent) 3"
     */
    @Override
    public String toString() {
        return String.format("(Vent) %d", n);
    }
}
