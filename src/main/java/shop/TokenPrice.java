package shop;

import org.jsoup.nodes.Element;
import util.JsoupUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

public class TokenPrice extends Price {
    private int n;
    private String token;


    public TokenPrice(Element td) throws ParseException {
        token = td.selectFirst("h4").html().trim();
        String nString = td.selectFirst("span.db-view__data__number").html().trim();
        n = JsoupUtils.parseInt(nString);
    }

    public int getN() {
        return n;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return String.format("(Tok) %d %s", n, token);
    }

    @Override
    public void setForSave(PreparedStatement addSales) throws SQLException {

        //price_type, gil, token_name, token_n, seals, rank, gc, fcc_rank, fcc_credits
        addSales.setString(11, "Tokens");
        addSales.setInt(12, 0);
        addSales.setString(13, token);
        addSales.setInt(14, n);
        addSales.setInt(15, 0);
        addSales.setString(16, null);
        addSales.setString(17, null);
        addSales.setInt(18, 0);
        addSales.setInt(19, 0);
    }

    @Override
    public void savePriceItems(int saleId, PreparedStatement addPriceItems) throws SQLException {
        // merchant_sale, item, hq, n
        // no items here, do nothing
    }
}
