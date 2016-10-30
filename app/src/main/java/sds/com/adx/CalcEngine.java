package sds.com.adx;

import java.math.BigDecimal;
import java.util.Vector;

import android.util.Log;

public class CalcEngine {

	
	final String TAG = "CALCENGINE";
	
	private Vector emaRow = new Vector();
	private Vector emas = new Vector(); 
	
	private double yesterdaysEma;
	private double todaysEma;

	private Vector thisQuotesHist;
	
    final int QUOTE = 2;
    final int SYMBOL = 0;
    final int DATE = 1;
    final int EMA12 = 3;
    final int EMA26 = 4;
    final int MACD = 5;
    
    final int HIGH = 2;
    final int LOW = 3;
    final int CLOSE = 4;
    final int TR = 5;
    final int DM1_PLUS = 6;
    final int DM1_MINUS = 7;
    final int TR14 = 8;
    final int DM14_PLUS = 9; 
    final int DM14_MINUS = 10;
    final int DI14_PLUS = 11;
    final int DI14_MINUX = 12;
    final int DIFF = 13;
    final int SUM = 14;
    final int DX = 15;
    final int ADX = 16;
	
//	public CalcEngine() {
//		
//
//	}
	
	public double calcEMA(double todaysValue, double yesterdaysEMA, int timeFrame) {
		
		double ema = 0;
		double k = 0;
		double mTodaysValue = todaysValue;
		double mYesterdaysEMA = yesterdaysEMA;
		
		k = 2f/(timeFrame + 1);
		
	
		
		ema = mTodaysValue * k + mYesterdaysEMA * (1 - k);
		
		
		
		
		return round(ema,2);
	}
	
	public double calcSMA(Vector closingPrices, int timeFrame) {
		
		Vector mClosingPrices = closingPrices;
		int mTimeFrame = timeFrame;
		double sum = 0;
		double sma = 0;
		
		if ( mClosingPrices.size() < timeFrame ) {
			Log.d("tag","Days provided must be more than time frame! - " + mClosingPrices.size());
		} else {

			for ( int i=0; i<mTimeFrame; i++ ) {
				
				sum += (double) mClosingPrices.get(i);
				
			}

			sma = sum/mTimeFrame;

		}
		
		return round(sma,2);
	}
	
    public static double round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
	
	public double calcMACD(double todaysClose) {
		
		double macD = 0;
		
		
		
		return macD;
	}

	public double calcTR(Quote quote, double yesterdaysClose) {
		// TODO Auto-generated method stub
    	double hl = 0;
    	double hcp = 0;
    	double lcp = 0;
    	double tr = 0;
		
		double doubleHigh = quote.getHIGH();
		double doubleLow = quote.getLOW();
				
		hl = doubleHigh - doubleLow;
		
    	if ( yesterdaysClose == 0.0 ) { 
        	tr = hl;
    		return round(tr,2);
    	}
    	
    	hcp = Math.abs(doubleHigh - yesterdaysClose);
    	lcp = Math.abs(doubleLow - yesterdaysClose);
   
    	
    	if ( hl >= hcp && hl >= lcp ) {
    		tr = hl;		
    	} else if ( hcp >= hl && hcp >= lcp ) {
    		tr = hcp;
    	} else if ( lcp >= hl && lcp >= hcp ) {
    		tr = lcp;
    	} else {
//    		Log.d(TAG,"Yesterdays HCloseL: " + yesterdaysClose );
//    		Log.d(TAG,"Quote: " + quote.getSYMBOL() + ":" + quote.getDATE() + ":" + quote.getHIGH() + ":" + quote.getLOW() + ":" + quote.getCLOSE());
//    		tr = 0;
    		Log.d(TAG,"No TR set: " + quote.getDATE());
    	}
		return round(tr,2);
	}
	
	public DMResult calcDM(Quote quote, double yHigh, double yLow) {
		// TODO Auto-generated method stub
    	double yesterdaysHigh = yHigh;
    	double yesterdaysLow = yLow;
    	double todaysHigh = 0;
    	double todaysLow = 0;
    	double dmPlus = 0;
    	double dmMinus = 0;
    	
		todaysHigh = quote.getHIGH();
		todaysLow = quote.getLOW();
		
//		Log.d("tag","yHigh, YLow etc" + todaysHigh + todaysLow + yHigh + yLow);

//	    UpMove = today's high − yesterday's high
//	    DownMove = yesterday's low − today's low
//	    if UpMove > DownMove and UpMove > 0, then +DM = UpMove, else +DM = 0
//	    if DownMove > UpMove and DownMove > 0, then −DM = DownMove, else −DM = 0 
		
		double upMove = todaysHigh - yesterdaysHigh;
		double downMove = yesterdaysLow - todaysLow;
		

		
		if ( upMove > downMove && upMove > 0 ) {
			
			dmPlus = upMove;
		} else {
			dmPlus = 0;
		}
		
		if ( downMove > upMove && downMove > 0 ) {
			
			dmMinus = downMove;
		} else {
			dmMinus = 0;
		}
    	
		if ( dmMinus == 0 && dmPlus == 0 ) {
//			 Log.d(TAG,"Setting both to 0");
		}
		
		DMResult dmRes = new DMResult(round(dmPlus,2), round(dmMinus,2));
		

		
		return dmRes;
	}
	

}
