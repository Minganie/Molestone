package shop;

import org.jsoup.nodes.Element;
import util.JsoupUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

public class VentureMerchandise extends Merchandise {
    private int n;

    public VentureMerchandise(Element td) throws ParseException {
        this.n = 1;
        if(td.selectFirst("span.db-view__data__number") != null) {
            this.n = JsoupUtils.parseInt(td.selectFirst("span.db-view__data__number").text().trim());
        }
    }

    public int getN() {
        return n;
    }

    @Override
    public String toString() {
        return String.format("(Vent) %d", n);
    }

    @Override
    public void setForSave(PreparedStatement addSales) throws SQLException {
        //good_type, venture, actionname, actionicon, actioneffect, actionduration
        //    5         6           7           8           9           10
        addSales.setString(5, "Venture");
        addSales.setInt(6, n);
        addSales.setString(7, null);
        addSales.setString(8, null);
        addSales.setString(9, null);
        addSales.setInt(10, 0);
    }

    @Override
    public void saveMerchItems(int saleId, PreparedStatement addMerchItems) throws SQLException {
        // merchant_sale, item, hq, n
        // no items here, do nothing
    }
}
