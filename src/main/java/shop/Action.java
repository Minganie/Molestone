package shop;

public class Action {
    private String name;
    private String licon;
    private String effect;
    private int durationHours;

    public Action(String name, String licon, String effect, int durationHours) {
        this.name = name;
        this.licon = licon;
        this.effect = effect;
        this.durationHours = durationHours;
    }

    public String getName() {
        return name;
    }

    public String getLicon() {
        return licon;
    }

    public String getEffect() {
        return effect;
    }

    public int getDurationHours() {
        return durationHours;
    }

    @Override
    public String toString() {
        return name;
    }
}
