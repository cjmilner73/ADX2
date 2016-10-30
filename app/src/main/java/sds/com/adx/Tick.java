package sds.com.adx;


import android.util.Log;

public class Tick {

	final String TAG = "Tick";
	
	int id;
	String tick;
	String index;
	double twelveMonthHigh;
	double twelveMonthLow;


	
	Tick(int id, String tick, String index, double twelveMonthHigh, double twelveMonthLow) {
		
		this.id = id;
		this.tick = tick;
		this.index = index;
		this.twelveMonthHigh = twelveMonthHigh;
		this.twelveMonthLow = twelveMonthLow;


	}
	
	// ID
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	// SYMBOL
	public void setTick(String tick) {
		this.tick = tick;
	}
	
	public String getTick() {
		return this.tick;
	}

	// SYMBOL
	public void setIndex(String index) {
		this.index = index;
	}
	
	public String getIndex() {
		return this.index;
	}
	
	public double getTwoMonthHigh() {
		return this.twelveMonthHigh;
	}
	
	public void setTwelveMonthHigh(double twelveMonthHigh) {
		this.twelveMonthHigh = twelveMonthHigh;
	}
	
	public double getTwoMonthLow() {
		return this.twelveMonthLow;
	}
	
	public void setTwelveMonthLow(double twelveMonthLow) {
		this.twelveMonthLow = twelveMonthLow;
	}
}





