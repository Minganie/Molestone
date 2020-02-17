package shop;

import org.jsoup.nodes.Element;
import util.JsoupUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Subclass of {@link Price} where the payment will be some type of token, i.e. scips, tomestones, etc.
 */
public class TokenPrice extends Price {
    private int n;
    private String token;

    /**
     * Constructor
     * @param td Html element containing the price
     * @throws ParseException for various parsing issues
     */
    public TokenPrice(Element td) throws ParseException {
        token = td.selectFirst("h4").html().trim();
        String nString = td.selectFirst("span.db-view__data__number").html().trim();
        n = JsoupUtils.parseInt(nString);
    }

    /**
     * Getter for the number of tokens
     * @return Number of tokens
     */
    public int getN() {
        return n;
    }

    /**
     * Getter for the name of the token
     * @return Name of the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Utility toString method
     * @return A pretty String like "(Tok) 325 Yellow Gatherers' Scrip"
     */
    @Override
    public String toString() {
        return String.format("(Tok) %d %s", n, token);
    }

}
