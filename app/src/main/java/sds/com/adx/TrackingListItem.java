package sds.com.adx;


public class TrackingListItem {

    private String trackingSymbol;
    private String direction;
    private double latestPrice;
    private double trigger;
    private int daysActive;


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public String getTrackingSymbol() {
        return trackingSymbol;
    }

    public void setTrackingSymbol(String trackingSymbol) {
        this.trackingSymbol = trackingSymbol;
    }

    public int getDaysActive() {
        return daysActive;
    }

    public void setDaysActive(int daysActive) {
        this.daysActive = daysActive;
    }

    public double getTrigger() {
        return trigger;
    }

    public void setTrigger(double trigger) {
        this.trigger = trigger;
    }

    public TrackingListItem(String symbol, String direction, double latestPrice, double trigger, int daysActive) {
        this.trackingSymbol = symbol;
        this.direction = direction;
        this.latestPrice = latestPrice;
        this.trigger = trigger;
        this.daysActive = daysActive;
    }
}
