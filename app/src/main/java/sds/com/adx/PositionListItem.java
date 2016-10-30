package sds.com.adx;


public class PositionListItem {

    private String positionSymbol;
    private String direction;
    private double triggered;
    private double profit;
    private int daysActive;


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getTriggered() {
        return triggered;
    }

    public void setTriggered(double Triggered) {
        this.triggered = triggered;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getPositionSymbol() {
        return positionSymbol;
    }

    public void setPositionSymbol(String PositionSymbol) {
        this.positionSymbol = PositionSymbol;
    }

    public int getDaysActive() {
        return daysActive;
    }

    public void setDaysActive(int daysActive) {
        this.daysActive = daysActive;
    }

    public PositionListItem(String symbol, String direction, double triggered, double profit, int daysActive) {
        this.positionSymbol = symbol;
        this.direction = direction;
        this.triggered = triggered;
        this.profit = profit;
        this.daysActive = daysActive;
    }
}
