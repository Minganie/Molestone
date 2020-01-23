package shop;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Merchandise {
    public static Merchandise get(Element el) throws Exception {
        String itemName = el.selectFirst("td:first-child").text();
        Elements itemLinks = el.select("td:first-child a.db_popup");
        if(itemName.trim().contains("Venture"))
            return new VentureMerchandise(el.selectFirst("td"));
        if(itemLinks.size() == 0)
            return new ActionMerchandise(el.selectFirst("td"));
        else
            return new ItemMerchandise(el.selectFirst("td"));
    }

    public abstract void setForSave(PreparedStatement addSales) throws SQLException;

    public abstract void saveMerchItems(int saleId, PreparedStatement addMerchItems) throws SQLException;

}
