package shop;

import org.jsoup.nodes.Element;
import util.JsoupUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;

public class GilPrice extends Price {
    private int n;

    public GilPrice(Element td) throws ParseException {
        n = JsoupUtils.parseInt(td.html());
    }

    public int getN() {
        return n;
    }

    @Override
    public String toString() {
        return String.format("(Gil) %d credits", n);
    }


}
