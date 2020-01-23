package util;

public class LidQCountBag {
    private LidQBag lqb;
    private int n;

    public LidQCountBag(Lid lid, boolean hq, int n) {
        lqb = new LidQBag(lid, hq);
        this.n = n;
    }

    public Lid getLid() {
        return lqb.getLid();
    }

    public boolean isHq() {
        return lqb.isHq();
    }

    public int getN() {
        return n;
    }
}
