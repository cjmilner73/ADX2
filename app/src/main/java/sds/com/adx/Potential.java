package sds.com.adx;

import java.util.Date;

import android.util.Log;

public class Potential {

	final String TAG = "Potential";
	
	int id;
	String symbol;
	String direction;
	int daysActive;
	double trigger;
	double current;
	int startId;
	int hot;
	double high;
	double stop;
	double limit;
	double ppp;
	double low;

	Potential() {

		super();
	}
	Potential(int id, String symbol, String direction, int startId, double current, int trigger, int daysActive, int hot, double high, double stop, double limit, double ppp, double low) {
		
		this.id = id;
		this.symbol = symbol;
		this.direction = direction;
		this.startId = startId;
		this.current = current;
		this.trigger = trigger;
		this.daysActive = daysActive;
		this.hot = hot;
		this.high = high;

	}
	
	// ID
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	// SYMBOL
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return this.symbol;
	}

	// SYMBOL
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public String getDirection() {
		return this.direction;
	}
	
	// DATE
	public void setStartId(int startId) {
		this.startId = startId;
	}
	
	public int getStartId() {
		return this.startId;
	}
	
	// CURRENT
	public void setCurrent(double current) {
		
		this.current = current;
	}
	
	public double getCurrent() {
		return current;
	}

	// TRIGGER
	public void setTrigger(double f) {
		
		this.trigger = f;
	}
	
	public double getTrigger() {
		return trigger;
	}

	// DAYS ACTIVE
	public void setDaysActive(int daysActive) {
		
		this.daysActive = daysActive;
	}
	
	public int getDaysActive() {
		return daysActive;
	}
	
	public void setHot(int hot) {
		
		this.hot = hot;;
	}
	
	public int getHot() {
		return hot;
	}

	public void setHigh(double high) {

		this.high = high;
	}

	public double getHigh() {
		return high;
	}

	public void setLow(double low) {

		this.low = low;
	}

	public double getLow() {
		return 0;
	}


	public void setStop(double stop) {

		this.stop = stop;
	}

	public double getStop() { return stop; };

	public void setLimit(double limit) {

		this.limit = limit;
	}

	public double getLimit() {
		return limit;
	}


	public void setPpp(double ppp) {

		this.ppp = ppp;
	}

	public double getPpp() {
		return ppp;
	}



}




