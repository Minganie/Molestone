package shop;

/**
 * A single transaction at a shop, i.e. you will pay x, and will get y
 */
public class Sale {

    private SaleType type;
    private String tab;
    private String subTab;
    private Merchandise merchandise;
    private Price price;

    /**
     * Constructor for a no-tabs shop
     * @param type Type of the sale
     * @param good Merchandise you will acquire
     * @param price Price you will pay
     */
    public Sale(SaleType type, Merchandise good, Price price) {
        this.type = type;
        tab = null;
        subTab = null;
        this.merchandise = good;
        this.price = price;
    }

    /**
     * Constructor for a one-level-of-tabs shop
     * @param type Type of the sale
     * @param tab Name of the tab
     * @param good Merchandise you will acquire
     * @param price Price you will pay
     */
    public Sale(SaleType type, String tab, Merchandise good, Price price) {
        this.type = type;
        this.tab = tab;
        subTab = null;
        this.merchandise = good;
        this.price = price;
    }

    /**
     * Constructor for a two-levels-of-tabs shop
     * @param type Type of the sale
     * @param tab Name of the parent tab
     * @param subtab Name of the child tab
     * @param good Merchandise you will acquire
     * @param price Price you will pay
     */
    public Sale(SaleType type, String tab, String subtab, Merchandise good, Price price) {
        this.type = type;
        this.tab = tab;
        this.subTab = subtab;
        this.merchandise = good;
        this.price = price;
    }

    /**
     * Getter for the type of sale
     * @return SaleType
     */
    public SaleType getType() {
        return type;
    }

    /**
     * Getter for the parent tab, if present
     * @return tab
     */
    public String getTab() {
        return tab;
    }

    /**
     * Getter for the child tab, if present
     * @return subtab
     */
    public String getSubTab() {
        return subTab;
    }

    /**
     * Getter for the merchandise
     * @return Merchandise
     */
    public Merchandise getMerchandise() {
        return merchandise;
    }

    /**
     * Getter for the price
     * @return Price
     */
    public Price getPrice() {
        return price;
    }

    /**
     * Utility toString
     * @return A pretty string like "3x58af1380e89* =for= (Action) The Heat of Battle"
     */
    @Override
    public String toString() {
        return price.toString() + " =for= " + merchandise.toString();
    }

}
