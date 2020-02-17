package shop;

import org.jsoup.nodes.Element;
import util.GrandCompany;
import util.JsoupUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Subclass of {@link Price} where the payment will be a number of Grand Company seals and a certain rank is required
 */
public class TokenRankPrice extends Price {
    private int n;
    private String tokens;
    private GrandCompany.Rank rank;
    private GrandCompany GC;

    /**
     * Constructor
     * @param element Html element containing the price
     * @throws Exception for various parsing issues
     */
    public TokenRankPrice(Element element) throws Exception {
        String nString = element.select(":root > p:first-child > span:nth-child(2)").html();
        n = JsoupUtils.parseInt(nString);
        tokens = element.select(":root > p:nth-child(1) > span:nth-child(1) > img:nth-child(1)").attr("alt");
        String rankName = element.select(":root > p:nth-child(2) > span:nth-child(1) > span:nth-child(2)").first().html();
        GC = GrandCompany.get(rankName);
        rank = GC.getRank(rankName);
    }

    /**
     * Getter for the number of Grand Company Seals
     * @return Number of Grand Company Seals
     */
    public int getN() {
        return n;
    }

    /**
     * Getter for the name of the Grand Company seal
     * @return Name of the Grand Company seal
     */
    public String getTokens() {
        return tokens;
    }

    /**
     * Getter for the required rank with the Grand Company
     * @return Required rank with the Grand Company
     */
    public GrandCompany.Rank getRank() {
        return rank;
    }

    /**
     * Getter for the name of the Grand Company
     * @return Name of the Grand Company
     */
    public GrandCompany getGC() {
        return GC;
    }

    /**
     * Utility toString method
     * @return A pretty String like "(GC) 1060 Serpent Seals (Private Third Class with The Twin Adder)"
     */
    @Override
    public String toString() {
        return String.format("(GC) %d seals (%s with %s)", n, rank, GC.toString());
    }

}
