package util;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection of small quality of life tools to use with Jsoup
 */
public class JsoupUtils {
    /**
     * Get the list of actual text, no empty strings, both ends trimmed
     * @param el Jsoup element
     * @return List of strings
     */
    public static List<String> actualTextCommaTrimmed(Element el) {
        List<String> atct = new ArrayList<>();
        for(TextNode tn : el.textNodes()) {
            if(!tn.text().trim().equals(""))
                atct.add(tn.text().trim());
        }
        return atct;
    }

    /**
     * Locale-smart parser for ints, 'cause 1060 is written 1,060
     * @param intString Input string
     * @return int
     * @throws ParseException if the string can't be parsed as a EN-US number
     */
    public static int parseInt(String intString) throws ParseException {
        return NumberFormat.getNumberInstance(java.util.Locale.US).parse(intString).intValue();
    }

    /**
     * Get the first not empty text node child
     * @param el Jsoup element
     * @return First not empty text node child
     */
    public static String firstNonEmptyTextNode (Element el) {
        List<String> atct = actualTextCommaTrimmed(el);
        return atct.get(0);
    }
}
