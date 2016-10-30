package sds.com.adx;

import android.util.Log;

public class Position {

	final String TAG = "Position";
	
	int id;
	String symbol;
	String direction;
	double stop;
	double limit;
	double pPP;
	double current;
	double profit;
	String status;
	int open;
	double triggered;
    String dateOpened;
	int daysActive;
	
	Position() {
		
		super();
	}
	
	Position(int id, String symbol, String direction, double stop, double limit, double pPP, double current, double profit, String status, double triggered, String dateOpened, int daysActive) {
		
		this.id = id;
		this.symbol = symbol;
		this.direction = direction;
		this.stop = stop;
		this.limit = limit;
		this.pPP = pPP;
		this.current = current;
		this.profit = profit;
		this.status = status;
		this.triggered = triggered;
        this.dateOpened = dateOpened;
		this.daysActive = daysActive;
		
		

	}
	
	private void raiseError(String message) {
		Log.d(TAG, message);
	}

	private void raiseWarning(String message) {
		Log.d(TAG,message);
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
	
	public void setStop(double f) {
		
//		if (f > limit) {
//			raiseError("Stop cannot be above limit." + f + " is above limit of " + limit);
//		} else {
//			this.stop = f;
//		}
//		if (f == limit) {
////			raiseError("Stop same as Limit");
//		}
		this.stop = f;
		
	}
	
	public double getStop() {
		return stop;
	}
	
	public void setLimit(double limit) {
		
//		if (limit <= stop) {
//			raiseError("Limit cannot be below limit.");
//		} else {
//			this.limit = limit;
//		}
		this.limit = limit;
	}
	
	public double getLimit() {
		return limit;
	}
	
	public void setPPP(double pPP) {
		
		double exposure = Math.abs(open - stop) * pPP;
		
//		if ( exposure > 100) {
//			raiseWarning("Exposure above 100");
//		}
		
		this.pPP = pPP;
	}
	
	
	public double getPPP() {
		return pPP;
	}
	public void setCurrent(double current) {
		this.current = current;
	}
	
	public double getCurrent() {
		return current;
	}
	
	public void setProfit(double profit) {
		this.profit = profit;
	}
	
	public double getProfit() {
		return profit;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
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

	public void setDaysActive(int daysActive) {
		this.daysActive = daysActive;
	}

	public int getDaysActive() {
		return daysActive;
	}
}



