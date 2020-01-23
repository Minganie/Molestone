package util;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to hold game coordinates (x and y)
 */
public class Coords {
    /**
     * Coordinate x (left to right)
     */
    private Double x;
    /**
     * Coordinate y (top to bottom)
     */
    private Double y;

    /**
     * Factory method to extract x and y from a string like "Map Coordinates X: 13.8 Y: 25.4"
     * @param mumbo String like "Map Coordinates X: 13.8 Y: 25.4"
     * @return coordinates
     * @throws ParseException if the mumbo doesn't match the expected pattern
     */
    public static Coords parseCoords(String mumbo) throws ParseException {
        Pattern pattern = Pattern.compile("^(?:Map.Coordinates.)?X:.(.+).Y:.(.+)$");
        Matcher matcher = pattern.matcher(mumbo);
        if(matcher.find() && matcher.groupCount()==2) {
            Double x = Double.parseDouble(matcher.group(1));
            Double y = Double.parseDouble(matcher.group(2));
            return new Coords(x, y);
        } else
            throw new ParseException("Can't find x and y from '" + mumbo + "'", 0);
    }

    /**
     * Generic constructor
     * @param x left-right coordinate
     * @param y top-bottom coordinate
     */
    public Coords(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x
     * @return x, left-right coordinate
     */
    public Double getX() {
        return x;
    }

    /**
     * Getter for y
     * @return y, top-bottom coordinate
     */
    public Double getY() {
        return y;
    }

    /**
     * Prettily prints the coordinates like so "[13.8, 25.4]"
     * @return pretty string like "[13.8, 25.4]"
     */
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    /**
     * Utility equals method
     * @param o Other object
     * @return Is is the same Coords?
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof Coords) {
            Coords c = (Coords) o;
            return this.toString().equals(c.toString());
        } else
            return false;
    }

    /**
     * Utility hashCode method
     * @return Hashcode
     */
    @Override
    public int hashCode() {
        return x.hashCode() - y.hashCode();
    }
}
