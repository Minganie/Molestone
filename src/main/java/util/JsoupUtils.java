package util;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class JsoupUtils {
    public static List<String> actualTextCommaTrimmed(Element el) {
        List<String> atct = new ArrayList<>();
        for(TextNode tn : el.textNodes()) {
            if(!tn.text().trim().equals(""))
                atct.add(tn.text().trim());
        }
        return atct;
    }

    public static int parseInt(String intString) throws ParseException {
        return NumberFormat.getNumberInstance(java.util.Locale.US).parse(intString).intValue();
    }

    public static String firstNonEmptyTextNode (Element el) {
        List<String> atct = actualTextCommaTrimmed(el);
        return atct.get(0);
    }
}
