package gathering;

import java.util.Objects;

public class GatheringLocation {
    private String region;
    private String zone;
    private String area;
    private int level;
    private boolean timeLimited;

    public GatheringLocation(String region, String zone, String area, int level, boolean timeLimited) {
        this.region = region;
        this.zone = zone;
        this.area = area;
        this.level = level;
        this.timeLimited = timeLimited;
    }

    public String getRegion() {
        return region;
    }

    public String getZone() {
        return zone;
    }

    public String getArea() {
        return area;
    }

    public int getLevel() {
        return level;
    }

    public boolean isTimeLimited() {
        return timeLimited;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof GatheringLocation) {
            GatheringLocation other = (GatheringLocation) o;
            return region.equals(other.region)
                    && zone.equals(other.zone)
                    && area.equals(other.area)
                    && level == other.level
                    && timeLimited == other.timeLimited;
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, zone, area, level, timeLimited);
    }

    @Override
    public String toString() {
        return "Lv. " + level + " " + area + (timeLimited ? "*" : "") + " (" + zone + " - " + region + ")";
    }
}
