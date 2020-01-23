package shop;

import org.jsoup.nodes.Element;
import util.StringCountBag;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionMerchandise extends Merchandise {
    private Action action;

    public ActionMerchandise(Element td) throws Exception {
        String name = td.select("div:nth-child(2) > h4:nth-child(1)").text().trim();
        String licon = td.select("div:first-child > div > img").first().attr("src");
        String helpText = td.select("div:nth-child(2) > p.db-shop__list__help").first().text();
        StringCountBag scb = parseHelpText(helpText);
        action = new Action(name, licon, scb.getThingy(), scb.getCount());
    }

    private static StringCountBag parseHelpText(String helpText) throws Exception {
        Pattern pattern = Pattern.compile("^(.+?)Duration:.*?(\\d+)h.*?$");
        Matcher matcher = pattern.matcher(helpText);
        if(matcher.find() && matcher.groupCount() == 2) {
            String effect = matcher.group(1).trim();
            int duration = Integer.parseInt(matcher.group(2));
            return new StringCountBag(effect, duration);
        } else
            throw new Exception("Unable to find FC Action's effect and duration from '" + helpText + "'");
    }

    public String getName() {
        return action.getName();
    }

    public String getLicon() {
        return action.getLicon();
    }

    public String getEffect() {
        return action.getEffect();
    }

    public int getDurationHours() {
        return action.getDurationHours();
    }

    @Override
    public String toString() {
        return "(Action) " + action.toString();
    }

    @Override
    public void setForSave(PreparedStatement addSales) throws SQLException {
        //good_type, venture, actionname, actionicon, actioneffect, actionduration
        //    5         6           7           8           9           10
        addSales.setString(5, "Action");
        addSales.setInt(6, 0);
        addSales.setString(7, action.getName());
        addSales.setString(8, action.getLicon());
        addSales.setString(9, action.getEffect());
        addSales.setInt(10, action.getDurationHours());
    }

    @Override
    public void saveMerchItems(int saleId, PreparedStatement addMerchItems) throws SQLException {
        // merchant_sale, item, hq, n
        // no items here, do nothing
    }
}
