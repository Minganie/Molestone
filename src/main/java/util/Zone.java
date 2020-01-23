package util;

public class Zone {
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