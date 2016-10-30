package sds.com.adx;

import android.util.Log;

public class History {

    final String TAG = "History";

    int id;
    String symbol;
    String direction;

    double triggered;
    double closed;
    double profit;
    String dateOpened;
    String dateClosed;

    History() {

        super();
    }

    History(int id, String symbol, String direction, double triggered, double closed, double profit, String dateOpened, String dateClosed) {

        this.id = id;
        this.symbol = symbol;
        this.direction = direction;
        this.profit = profit;
        this.triggered = triggered;
        this.dateOpened = dateOpened;
        this.dateClosed = dateClosed;


    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getProfit() {
        return profit;
    }

    public void setTriggered(double triggered) {
        this.triggered = triggered;
    }

    public double getTriggered() {
        return triggered;
    }

    public void setDateOpened(String dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getDateOpened() {
        return dateOpened;
    }

    public void setDateClosed(String dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getDateClosed() {
        return dateClosed;
    }
}



