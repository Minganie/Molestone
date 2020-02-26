package npc;

import org.jsoup.nodes.Element;
import util.JsoupUtils;

import java.text.ParseException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpawnLocation {
    private String location;
    private int minLevel;
    private int maxLevel;
    private boolean conditional;

    public SpawnLocation(Element el) throws ParseException {
        String mumbo = JsoupUtils.firstNonEmptyTextNode(el);
        Pattern pattern = Pattern.compile("^(.+)\\sLv\\.\\s(\\d+)(?:\\-(\\d+))?$");
        Matcher matcher = pattern.matcher(mumbo);
        String tmpLocation = null;
        String tmpMinLvl = null;
        String tmpMaxLvl = null;
        if(matcher.find()) {
            tmpLocation = matcher.group(1);
            tmpMinLvl = matcher.group(2);
            tmpMaxLvl = matcher.group(3);
        } else {
            throw new ParseException("Can't figure out spawn location from '" + mumbo + "'", 0);
        }

        location = tmpLocation;
        minLevel = JsoupUtils.parseInt(tmpMinLvl);
        if(tmpMaxLvl == null)
            maxLevel = minLevel;
        else
            maxLevel = JsoupUtils.parseInt(tmpMaxLvl);

        conditional = (el.selectFirst("p:matchesOwn(certain circumstances)") != null);
    }

    public SpawnLocation(String location, int minLevel, int maxLevel, boolean conditional) {
        this.location = location;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.conditional = conditional;
    }

    public String getLocation() {
        return location;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public boolean isConditional() {
        return conditional;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof SpawnLocation) {
            SpawnLocation other = (SpawnLocation) o;
            return this.location.equals(other.location)
                    && this.minLevel == other.minLevel
                    && this.maxLevel == other.maxLevel
                    && this.conditional == other.conditional;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, minLevel, maxLevel, conditional);
    }

    @Override
    public String toString() {
        return location + " Lv. " + (minLevel == maxLevel ? minLevel : minLevel + "-" + maxLevel + (conditional ? "*" : ""));
    }
}
