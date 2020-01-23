package shop;

public class Sale {

    private SaleType type;
    private String tab;
    private String subTab;
    private Merchandise merchandise;
    private Price price;

    public Sale(SaleType type, Merchandise good, Price price) {
        this.type = type;
        tab = null;
        subTab = null;
        this.merchandise = good;
        this.price = price;
    }
    public Sale(SaleType type, String tab, Merchandise good, Price price) {
        this.type = type;
        this.tab = tab;
        subTab = null;
        this.merchandise = good;
        this.price = price;
    }
    public Sale(SaleType type, String tab, String subtab, Merchandise good, Price price) {
        this.type = type;
        this.tab = tab;
        this.subTab = subtab;
        this.merchandise = good;
        this.price = price;
    }

    public SaleType getType() {
        return type;
    }

    public String getTab() {
        return tab;
    }

    public String getSubTab() {
        return subTab;
    }

    public Merchandise getMerchandise() {
        return merchandise;
    }

    public Price getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return price.toString() + " =for= " + merchandise.toString();
    }

}
