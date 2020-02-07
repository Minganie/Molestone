package shop;

import org.jsoup.nodes.Element;
import util.JsoupUtils;
import util.Lid;
import util.LidQCountBag;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

public class TokenAndItemPrice extends Price {
    private LidQCountBag item;
    private int collectability;

    private String tokenName;
    private int nTokens;

    public TokenAndItemPrice(Element td) throws Exception {
        int nItemLis = 0;
        int nTokenLis = 0;
        for(Element li : td.select(":root > ul > li")) {
            if(isItem(li)) {
                Lid itemLid = Lid.parseLid(li.selectFirst("a.db_popup").attr("href"));
                int nItem = Integer.parseInt(li.selectFirst("span.db-view__data__number").html());
                boolean hq = li.select("a.db_popup").first().attr("href").contains("hq=1");
                collectability = getCollectability(li);
                item = new LidQCountBag(itemLid, hq, nItem);
                nItemLis++;
            } else if(isToken(li)) {
                tokenName = td.selectFirst("h4").html().trim();
                String nString = td.selectFirst("span.db-view__data__number").html().trim();
                nTokens = JsoupUtils.parseInt(nString);
                nTokenLis++;
            } else throw new Exception("Can't seem to figure out subpayment type for token and item price");
        }
        if(nItemLis != 1)
            throw new Exception("Not exactly one item li in token and item price?");
        if(nTokenLis != 1)
            throw new Exception("Not exactly one token li in token and item price?");
    }
    private static boolean isItem(Element li) {
        try {
            Lid lid = Lid.parseLid(li.selectFirst("a.db_popup").attr("href"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private static boolean isToken(Element li) {
        return li.selectFirst("h4").childNodeSize() == 1;
    }

    private static int getCollectability(Element itemLi) throws ParseException {
        int n = 0;
        for(String s : JsoupUtils.actualTextCommaTrimmed(itemLi.selectFirst("h4"))) {
            if(s.contains("Collectability Rating"))
                return JsoupUtils.parseInt(s.substring(24, s.length()-2));
        }
        return n;
    }

    public Lid getItemLid() {
        return item.getLid();
    }

    public int getnItem() {
        return item.getN();
    }

    public boolean isItemHq() {
        return item.isHq();
    }

    public int getItemCollectability() {
        return collectability;
    }

    public String getTokenName() {
        return tokenName;
    }

    public int getnTokens() {
        return nTokens;
    }

    @Override
    public String toString() {
        return String.format("(Tok + Item) %d %s + %d %s", nTokens, tokenName, item.getN(), item.getLid());
    }

    @Override
    public void setForSave(PreparedStatement addSales) throws SQLException {

        //price_type, gil, token_name, token_n, seals, rank, gc, fcc_rank, fcc_credits
        addSales.setString(11, "Tokens and Items");
        addSales.setInt(12, 0);
        addSales.setString(13, tokenName);
        addSales.setInt(14, nTokens);
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
        addPriceItems.setString(2, item.getLid().get());
        addPriceItems.setBoolean(3, item.isHq());
        addPriceItems.setInt(4, item.getN());
        addPriceItems.execute();
    }
}
