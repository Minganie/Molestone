package shop;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Coords;
import util.Lid;
import util.ZonedCoords;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shop {
    protected Lid lid;
    protected String name;
    protected List<ZonedCoords> locations = new ArrayList<>();
    protected Tabs gilTabs = new Tabs();
    protected Tabs currencyTabs = new Tabs();
    protected Tabs sealsTabs = new Tabs();
    protected Tabs creditsTabs = new Tabs();
    protected List<Sale> sales = new ArrayList<>();


    public static Shop get(Lid lid) throws Exception {
        return new Shop(lid);
    }


    private Shop(Lid lid) throws Exception {
        this.lid = lid;
        URL url = new URL("https://na.finalfantasyxiv.com/lodestone/playguide/db/shop/" + lid.toString());
        Document doc = Jsoup.connect(url.toString()).maxBodySize(0).get();
        name = doc.select(".db-shop__name").first().text().trim();
        Elements locs = doc.select(".db-shop__place > li");
        for(Element location : locs) {
            String areaText = location.text().trim();
            locations.add(parseAreaAndCoords(areaText));
        }
        parseInventory(doc, SaleType.Gil);
        parseInventory(doc, SaleType.Currency);
        parseInventory(doc, SaleType.Seals);
        parseInventory(doc, SaleType.Credits);
    }

    private String getRegularTabId(String tabDivId, int i) {
        return String.format(tabDivId, i);
    }

    private String getCreditTabId(String tabDivId, String tabName) throws Exception {
        String idName;
        switch (tabName) {
            case "Items":
                idName = "item";
                break;
            case "Actions":
                idName = "action";
                break;
            default:
                throw new Exception("Unknown credit shop tab name '" + tabName + "'");
        }
        return String.format(tabDivId, idName);
    }

    private void parseInventory(Document doc, SaleType type) throws Exception {
        String allDivId;
        String tabDivId;
        String subTabDivId;
        Tabs tabs;
        switch (type) {
            case Gil:
                allDivId = "#sys_shop_type_gil";
                tabDivId = "#sys_gil_shop_no_%d";
                subTabDivId = "#sys_gil_shop_%d_no_%d";
                tabs = gilTabs;
                break;
            case Currency:
                allDivId = "#sys_shop_type_currency";
                tabDivId = "#sys_shop_currency_no_%d";
                subTabDivId = "#sys_shop_currency_%d_no_%d";
                tabs = currencyTabs;
                break;
            case Seals:
                allDivId = "#sys_shop_type_cs";
                tabDivId = "#sys_shop_cs_no_%d";
                subTabDivId = "#sys_shop_cs_no_%d_%d";
                tabs = sealsTabs;
                break;
            case Credits:
                allDivId = "#sys_shop_type_fcc";
                tabDivId = "#sys_fcc_shop_%s";
                subTabDivId = "";
                tabs = creditsTabs;
                break;
            default:
                throw new Exception("Unknown sale type?");
        }

        Element el = doc.select(allDivId).first();
        Elements tabNames = doc.select(allDivId+ " > div > div > ul.sys_shop_tab > li");
        if(el != null) {
            // ZERO TABS
            if (tabNames.size() == 0) {  // There is zero tabs
                Elements sales = el.select("div > div > div > table > tbody > tr");
                for (Element sale : sales) {
                    Merchandise m = Merchandise.get(sale);
                    Price p = Price.get(sale);
                    this.sales.add(new Sale(type, m, p));
                }

                // THERE ARE TABS
            } else {
                int i = 0;
                for (Element tabNameEl : tabNames) {
                    String tabName = shortenName(tabNameEl.text().trim());
                    if(tabName.equals("") && !tabNameEl.attr("data-tooltip").equals("")) {
                        String tt = tabNameEl.attr("data-tooltip");
                        if(tt.contains("Private"))
                            tabName = "Private";
                        else if(tt.contains("Sergeant"))
                            tabName = "Sergeant";
                        else
                            tabName = "Lieutenant";
                    }
                    tabs.addTab(tabName);
                    String tabId;
                    if(type.equals(SaleType.Credits))
                        tabId = getCreditTabId(tabDivId, tabName);
                    else
                        tabId = getRegularTabId(tabDivId, i);
                    Element tab = el.select(tabId).first();
                    Elements subTabNames = tab.select("ul.sys_shop_tab > li");

                    // THIS tab doesn't have subtabs
                    if (subTabNames.size() == 0) {
                        Elements sales = tab.select("table > tbody > tr");
                        int mycpt = 0;
                        for (Element sale : sales) {
                            Merchandise m = Merchandise.get(sale);
                            Price p = Price.get(sale);
                            this.sales.add(new Sale(type, tabName, m, p));
                            mycpt++;
                        }

                        // THIS tab has subtabs
                    } else {
                        int j = 0;
                        for (Element subTabNameEl : subTabNames) {
                            String subTabName = shortenName(subTabNameEl.text().trim());
                            tabs.addSubTab(tabName, subTabName);
                            Element subTabEl = tab.selectFirst(String.format(subTabDivId, i, j));
                            Elements sales = subTabEl.select("table > tbody > tr");
                            for (Element sale : sales) {
                                Merchandise m = Merchandise.get(sale);
                                Price p = Price.get(sale);
                                this.sales.add(new Sale(type, tabName, subTabName, m, p));
                            }
                            j++;
                        }
                    }
                    i++;
                }
            }
        }
    }

    private ZonedCoords parseAreaAndCoords(String mumbo) throws ParseException {
        Pattern pattern = Pattern.compile("^(.+?)\\s\\(X:(.+)\\sY:(.+)\\)$");
        Matcher matcher = pattern.matcher(mumbo);
        if(matcher.find() && matcher.groupCount()==3) {
            String area = matcher.group(1);
            Double x = Double.parseDouble(matcher.group(2));
            Double y = Double.parseDouble(matcher.group(3));
            Coords coords = new Coords(x, y);
            return new ZonedCoords(coords, area);
        } else
            throw new ParseException("Can't find area, x and y from '" + mumbo + "'", 0);
    }

    private String shortenName(String mumbo) {
        mumbo = removePurchase(mumbo);
        return removeNumbers(mumbo);
    }

    private String removeNumbers(String mumbo) {
        Pattern pattern = Pattern.compile("(.+?)\\(\\d+\\)$");
        Matcher matcher = pattern.matcher(mumbo);
        if(matcher.find()) {
            return matcher.group(1);
        } else
            return mumbo;
    }

    private String removePurchase(String mumbo) {
        if(mumbo.indexOf("Purchase ") == 0)
            return mumbo.substring(9, mumbo.length());
        if(mumbo.indexOf("Repurchase ") == 0)
            return mumbo.substring(11, mumbo.length());
        return mumbo;
    }

    public Lid getLid() {
        return lid;
    }

    public String getName() {
        return name;
    }

    public List<ZonedCoords> getLocations() {
        return locations;
    }

    public Tabs getGilTabs() {
        return gilTabs;
    }

    public Tabs getCurrencyTabs() {
        return currencyTabs;
    }

    public Tabs getSealsTabs() {
        return sealsTabs;
    }

    public Tabs getCreditsTabs() {
        return creditsTabs;
    }

    public List<Sale> getSales() {
        return sales;
    }

    @Override
    public String toString() {
        String location = locations.get(0).getZone();
        String otherLocations = "";
        if(locations.size() > 1) {
            otherLocations = " (others)";
        }
        boolean gil = false;
        boolean cur = false;
        boolean sea = false;
        boolean fcc = false;
        for(Sale sale : sales) {
            switch (sale.getType()) {
                case Gil:
                    gil = true;
                    break;
                case Currency:
                    cur = true;
                    break;
                case Seals:
                    sea = true;
                    break;
                case Credits:
                    fcc = false;
                    break;
            }
        }
        StringBuilder sb = new StringBuilder();
        if(gil)
            sb.append("gil, ");
        if(cur)
            sb.append("currency, ");
        if(sea)
            sb.append("seals, ");
        if(fcc)
            sb.append("credits, ");
        sb.substring(0, sb.length()-2);
        return String.format("[SHP] %s @ %s in %s%s, sells for %s", name, lid.get(), location, otherLocations, sb.toString());
    }
}
