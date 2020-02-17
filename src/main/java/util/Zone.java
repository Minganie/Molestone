package util;

/**
 * Class for a zone in the game, i.e. Central Thanalan, etc. Will also rename "Steps of Nald/Thal" to Ul'dah and
 * "Upper/Lower Decks" to Limsa Lominsa. Finally, will correct "Eastern Shroud" present in some fan sites to "East Shroud"
 */
public class Zone {
    /**
     * Factory method
     * @param name Name of the zone
     * @return Zone
     */
    static public String getActualZone(String name) {
        if(name != null && name.contains("Ul'dah"))
            return "Ul'dah";
        if(name != null && name.contains("Limsa Lominsa"))
            return "Limsa Lominsa";
        if(name != null && name.equals("Eastern Shroud"))   // that's you, consolegameswiki
            return "East Shroud";
        return name;
    }
}