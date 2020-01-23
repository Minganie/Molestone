package util;

public class LidQBag {
    private Lid lid;
    private boolean hq;

    public LidQBag(Lid lid, boolean hq) {
        this.lid = lid;
        this.hq = hq;
    }

    public Lid getLid() {
        return lid;
    }

    public boolean isHq() {
        return hq;
    }
}
