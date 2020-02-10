package shop;

import org.jsoup.nodes.Element;
import util.JsoupUtils;
import util.Lid;
import util.LidQCountBag;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Subclass of {@link Price} where the payment will be an item and a number of Allagan tomestones
 */
public class TokenAndItemPrice extends Price {
    private LidQCountBag item;
    private int collectability;

    private String tokenName;
    private int nTokens;

    /**
     * Constructor
     * @param td Html element containing the price
     * @throws Exception for various parsing issues
     */
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

    /**
     * Getter for the item's Lodestone id
     * @return Lodestone id of the item
     */
    public Lid getItemLid() {
        return item.getLid();
    }

    /**
     * Getter for the number of items
     * @return Number of items
     */
    public int getnItem() {
        return item.getN();
    }

    /**
     * Getter for the item's quality
     * @return Is it a high quality item?
     */
    public boolean isItemHq() {
        return item.isHq();
    }

    /**
     * Getter for whether the item is collectable
     * @return Is it collectable?
     */
    public int getItemCollectability() {
        return collectability;
    }

    /**
     * Getter for the name of the token, i.e. Allagan Tomestones of Poetics
     * @return Name of the token
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * Getter for the number of tokens
     * @return Number of tokens
     */
    public int getnTokens() {
        return nTokens;
    }

    /**
     * Utility toString method
     * @return A pretty string like "(Tok + Item) 325 Yellow Crafter's Scrips + 1 15ef9749703"
     */
    @Override
    public String toString() {
        return String.format("(Tok + Item) %d %s + %d %s", nTokens, tokenName, item.getN(), item.getLid());
    }
}
