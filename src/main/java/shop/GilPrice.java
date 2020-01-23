package shop;

import org.jsoup.nodes.Element;
import util.JsoupUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;

public class GilPrice extends Price {
    private int n;

    public GilPrice(Element td) throws ParseException {
        n = JsoupUtils.parseInt(td.html());
    }

    public int getN() {
        return n;
    }

    @Override
    public String toString() {
        return String.format("(Gil) %d credits", n);
    }

    @Override
    public void setForSave(PreparedStatement addSales) throws SQLException {

        //price_type, gil, token_name, token_n, seals, rank, gc, fcc_rank, fcc_credits
        addSales.setString(11, "Gil");
        addSales.setInt(12, n);
        addSales.setString(13, null);
        addSales.setInt(14, 0);
        addSales.setInt(15, 0);
        addSales.setString(16, null);
        addSales.setString(17, null);
        addSales.setInt(18, 0);
        addSales.setInt(19, 0);
    }

    @Override
    public void savePriceItems(int saleId, PreparedStatement addPriceItems) {
        // merchant_sale, item, hq, n
        // no items here, do nothing
    }
}
