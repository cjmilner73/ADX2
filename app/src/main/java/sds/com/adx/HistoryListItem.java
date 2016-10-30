package sds.com.adx;


public class HistoryListItem {

    private String positionSymbol;
    private String direction;
    private double triggered;
    private double profit;
    private String dateOpened;
    private String dateClosed;


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

    public String getHistorySymbol() {
        return positionSymbol;
    }

    public void setHistorySymbol(String HistorySymbol) {
        this.positionSymbol = HistorySymbol;
    }

    public String getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(String dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(String dateClosed) {
        this.dateClosed = dateClosed;
    }

    public HistoryListItem(String symbol, String direction, double triggered, double profit, String dateOpened, String dateClosed) {
        this.positionSymbol = symbol;
        this.direction = direction;
        this.triggered = triggered;
        this.profit = profit;
        this.dateOpened = dateOpened;
        this.dateClosed = dateClosed;
    }
}
