package sds.com.adx;

final class DMResult {
    private final double dmPlus;
    private final double dmMinus;

    public DMResult(double dmPlus, double dmMinus) {
        this.dmPlus = dmPlus;
        this.dmMinus = dmMinus;
    }

    public double getDmPlus() {
        return dmPlus;
    }

    public double getDmMinus() {
        return dmMinus;
    }
}
