package shop;

import org.jsoup.nodes.Element;
import util.GrandCompany;
import util.JsoupUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TokenRankPrice extends Price {
    private int n;
    private String tokens;
    private GrandCompany.Rank rank;
    private GrandCompany GC;

    public TokenRankPrice(Element element) throws Exception {
        String nString = element.select(":root > p:first-child > span:nth-child(2)").html();
        n = JsoupUtils.parseInt(nString);
        tokens = element.select(":root > p:nth-child(1) > span:nth-child(1) > img:nth-child(1)").attr("alt");
        String rankName = element.select(":root > p:nth-child(2) > span:nth-child(1) > span:nth-child(2)").first().html();
        GC = GrandCompany.get(rankName);
        rank = GC.getRank(rankName);
    }

    public int getN() {
        return n;
    }

    public String getTokens() {
        return tokens;
    }

    public GrandCompany.Rank getRank() {
        return rank;
    }

    public GrandCompany getGC() {
        return GC;
    }

    @Override
    public String toString() {
        return String.format("(GC) %d seals (%s with %s)", n, rank, GC.toString());
    }

    @Override
    public void setForSave(PreparedStatement addSales) throws SQLException {

        //price_type, gil, token_name, token_n, seals, rank, gc, fcc_rank, fcc_credits
        addSales.setString(11, "Seals");
        addSales.setInt(12, 0);
        addSales.setString(13, null);
        addSales.setInt(14, 0);
        addSales.setInt(15, n);
        addSales.setString(16, rank.toString());
        addSales.setString(17, GC.toString());
        addSales.setInt(18, 0);
        addSales.setInt(19, 0);
    }

    @Override
    public void savePriceItems(int saleId, PreparedStatement addPriceItems) throws SQLException {
        // merchant_sale, item, hq, n
        // no items here, do nothing
    }
}
