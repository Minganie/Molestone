package shop;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;
import util.LidQCountBag;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemMerchandise extends Merchandise {
    private List<LidQCountBag> items = new ArrayList<>();

    public ItemMerchandise(Element td) throws Exception {
        Elements items;
        if(hasMoreThanOne(td))
            items = td.select("ul > li");
        else
            items = new Elements(td);
        for(Element item : items) {
            Lid lid = Lid.parseLid(item.select("a.db_popup").first().attr("href"));
            int n = 1;
            try {
                n = Integer.parseInt(item.select("span.db-view__data__number").first().html());
            } catch (NullPointerException npe) {}
            boolean q = item.select("a.db_popup").first().attr("href").contains("hq=1");
            addItem(n, lid, q);
        }
    }

    private static boolean hasMoreThanOne(Element td) {
        return td.select(":root > ul > li").size() > 0;
    }

    private void addItem(int n, Lid lid, boolean hq) {
        items.add(new LidQCountBag(lid, hq, n));
    }

    public List<LidQCountBag> getItems() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(LidQCountBag bag : items) {
            sb.append(String.format("%dx%s%s, ", bag.getN(), bag.getLid(), (bag.isHq() ? "*" : "")));
        }
        return sb.toString();
    }

    @Override
    public void setForSave(PreparedStatement addSales) throws SQLException {
        //good_type, venture, actionname, actionicon, actioneffect, actionduration
        //    5         6           7           8           9           10
        addSales.setString(5, "Items");
        addSales.setInt(6, 0);
        addSales.setString(7, null);
        addSales.setString(8, null);
        addSales.setString(9, null);
        addSales.setInt(10, 0);
    }

    @Override
    public void saveMerchItems(int saleId, PreparedStatement addMerchItems) throws SQLException {
        // merchant_sale, item, hq, n
        addMerchItems.setInt(1, saleId);
        for(LidQCountBag bag : items) {
            addMerchItems.setString(2, bag.getLid().get());
            addMerchItems.setBoolean(3, bag.isHq());
            addMerchItems.setInt(4, bag.getN());
            addMerchItems.execute();
        }
    }
}
