package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class used to represent a Lodestone id, useful for type checking (and some basic constraints like a Lid must
 * be 11 alpha-numeric characters)
 */
public class Lid {
    /**
     * Lodestone id; unique throughout all the Lodestone
     */
    private String lid;

    /**
     * Constructor; performs basic format checking on the given string
     * @param lid Lodestone id string
     * @throws Exception if the given string is not 11 alpha-numeric characters
     */
    public Lid(String lid) throws Exception {
        if(lid.length()!=11)
            throw new Exception("Invalid lodestone id: '" + lid + "'");
        this.lid = lid;
    }

    /**
     * Factory method to get a {@link Lid} from a url part
     * @param mumbo String like so "/lodestone/playguide/db/item/84ccf1c33bb/"
     * @return Instance of Lid
     * @throws Exception if the string doesn't match the regex
     */
    public static Lid parseLid(String mumbo) throws Exception {
        Pattern pattern = Pattern.compile("^/lodestone/playguide/db/.+?/(.+?)/(.+)?$");
        Matcher matcher = pattern.matcher(mumbo);
        if (matcher.find())
            return new Lid(matcher.group(1));
        else
            throw new Exception("Can't seem to find the lid in provided url part: " + mumbo);
    }

    /**
     * Getter for the Lodestone id string
     * @return Lodestone id string
     */
    public String get() {
        return lid;
    }

    /**
     * Utility toString method
     * @return String like so "84ccf1c33bb"
     */
    public String toString() {
        return lid;
    }

    /**
     * Utility equals method
     * @param obj Other object
     * @return Is is the same Lid?
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Lid) {
            Lid o = (Lid) obj;
            return this.lid.equals(o.lid);
        } else
            return false;
    }

    /**
     * Utility hashCode method
     * @return lid string's hashCode
     */
    @Override
    public int hashCode() {
        return lid.hashCode();
    }
}
