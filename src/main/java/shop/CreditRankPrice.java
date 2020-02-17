package shop;

import org.jsoup.nodes.Element;
import util.JsoupUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Subclass of {@link Price} where a certain Free Company rank is required and the currency is Free Company Credits
 */
public class CreditRankPrice extends Price {
    private int rank;
    private int credits;


    /**
     * Constructor
     * @param el Html element containing the price
     * @throws ParseException for various parsing issues
     */
    public CreditRankPrice(Element el) throws ParseException {
        rank = Integer.parseInt(el.select("td:nth-child(2)").html());
        credits = JsoupUtils.parseInt(el.select("td:nth-child(3)").html());
    }

    /**
     * Getter for the required Free Company rank
     * @return Required Free Company rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Getter for the number of Free Company credits
     * @return Number of Free Company credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Utility toString method
     * @return A pretty string like "(FCC) 1500 credits (rank 6)"
     */
    @Override
    public String toString() {
        return String.format("(FCC) %d credits (rank %d)", credits, rank);
    }
}
