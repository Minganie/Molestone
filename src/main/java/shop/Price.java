package shop;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;

public abstract class Price {
    public static Price get(Element el) throws Exception {
        if(isAFC(el)) {
            return new CreditRankPrice(el);
        }
        if(isAGC(el)) {
            return new TokenRankPrice(el.selectFirst(":root > td:nth-child(2)"));
        }
        if(isAGil(el)) {
            return new GilPrice(el.selectFirst(":root > td:nth-child(2)"));
        }
        // Here, it's multiple-payment-possible, either items or combination of items and tokens
        if(hasItems(el) && hasTokens(el))
            return new TokenAndItemPrice(el.selectFirst(":root > td:nth-child(2)"));
        if(hasItems(el))
            return new ItemPrice(el.selectFirst(":root > td:nth-child(2)"));
        if(hasTokens(el))
            return new TokenPrice(el.selectFirst(":root > td:nth-child(2)"));
        throw new Exception("Can't figure out price type...");
    }

    private static boolean isAGil(Element tr) {
        return tr.select(":root > td:nth-child(2) > ul").size() == 0;
    }
    private static boolean hasItems(Element tr) {
        return tr.select(":root > td:nth-child(2) a.db_popup").size() > 0;
    }
    private static boolean hasTokens(Element tr) {
        for(Element li : tr.select(":root > td:nth-child(2) > ul > li")) {
            if(li.select("a.db_popup").size() == 0)
                return true;
        }
        return false;
    }
    private static boolean isAGC(Element tr) {
        return tr.select(":root > td:nth-child(2) > p").size() == 2;
    }
    private static boolean isAFC(Element tr) {
        return tr.select(":root > td").size() == 3;
    }

    public abstract void setForSave(PreparedStatement addSales) throws SQLException;

    public abstract void savePriceItems(int saleId, PreparedStatement addPriceItems) throws SQLException;
}
