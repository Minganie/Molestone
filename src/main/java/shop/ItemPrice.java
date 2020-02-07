package shop;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Lid;
import util.LidQCountBag;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemPrice extends Price {
    private List<LidQCountBag> items = new ArrayList<>();

    public ItemPrice(Element td) throws Exception {
        Elements items = td.select(":root > ul > li");
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

    private void addItem(int n, Lid lid, boolean hq) {
        items.add(new LidQCountBag(lid, hq, n));
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
        //price_type, gil, token_name, token_n, seals, rank, gc, fcc_rank, fcc_credits
        addSales.setString(11, "Items");
        addSales.setInt(12, 0);
        addSales.setString(13, null);
        addSales.setInt(14, 0);
        addSales.setInt(15, 0);
        addSales.setString(16, null);
        addSales.setString(17, null);
        addSales.setInt(18, 0);
        addSales.setInt(19, 0);
    }

    @Override
    public void savePriceItems(int saleId, PreparedStatement addPriceItems) throws SQLException {
        // merchant_sale, item, hq, n
        addPriceItems.setInt(1, saleId);
        for(LidQCountBag bag : items) {
            addPriceItems.setString(2, bag.getLid().get());
            addPriceItems.setBoolean(3, bag.isHq());
            addPriceItems.setInt(4, bag.getN());
            addPriceItems.execute();
        }
    }
}
