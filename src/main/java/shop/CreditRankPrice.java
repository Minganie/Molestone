package shop;

import org.jsoup.nodes.Element;
import util.JsoupUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

public class CreditRankPrice extends Price {
    private int rank;
    private int credits;


    public CreditRankPrice(Element el) throws ParseException {
        rank = Integer.parseInt(el.select("td:nth-child(2)").html());
        credits = JsoupUtils.parseInt(el.select("td:nth-child(3)").html());
    }

    public int getRank() {
        return rank;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return String.format("(FCC) %d credits (rank %d)", credits, rank);
    }

    @Override
    public void setForSave(PreparedStatement addSales) throws SQLException {
        //price_type, gil, token_name, token_n, seals, rank, gc, fcc_rank, fcc_credits
        addSales.setString(11, "FCC");
        addSales.setInt(12, 0);
        addSales.setString(13, null);
        addSales.setInt(14, 0);
        addSales.setInt(15, 0);
        addSales.setString(16, null);
        addSales.setString(17, null);
        addSales.setInt(18, rank);
        addSales.setInt(19, credits);
    }

    @Override
    public void savePriceItems(int saleId, PreparedStatement addPriceItems) {
        // merchant_sale, item, hq, n
        // no items here, do nothing
    }
}
