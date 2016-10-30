package sds.com.adx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class CheckQuotes extends BroadcastReceiver {

	private static final int LOWHIGH_RANGE = 60;
	//private static final int OFFSET = 0;
	private final boolean LOADING_FROM_URL = false;

	private static final int TIMEFRAME = 14;

	private static final int MIN_QUOTE_SIZE = 30;


	final String TAG = "CheckQuotes";


	static DatabaseHandler db;
	static DownloadObj dob = null;

	private int OFFSET = 0;

	boolean testRun = true;
	private int testOffSet;
	private String start_date = "2016-07-01";

	Context thisContext;
		
    @Override
    public void onReceive(Context context, Intent intent) {
        
    	thisContext = context;
    	
		dob = new DownloadObj(thisContext);
    	
		db = new DatabaseHandler(thisContext);

		Log.i(TAG,"Starting CheckQuotes");

    	if (thisContext == null) {
    		Log.d(TAG,"thisContext is null");
    	}

    	if ( wifiEnabled(thisContext) ) {
            Toast.makeText(thisContext.getApplicationContext(), "Started AysncTaskRunner('Chris')", Toast.LENGTH_LONG).show();
			Log.d(TAG,"Calling background job (checkQuotesAll)");
            new AsyncTaskRunner().execute("Chris");

    	} else {
            Toast.makeText(thisContext.getApplicationContext(), "WIFI Not Enabled", Toast.LENGTH_LONG).show();
        }

		Log.i(TAG,"Ending CheckQuotes");
        
    }

    public void checkQuotes_All(boolean testRun, int i) {
				
				 loadTicks();
				 ArrayList<Tick> tickList= db.getAllTicks();
			
				 tickList = removeBadTicks(tickList);

				if ( tickList == null ) {	
					Log.i(TAG,"tickList is null");
				}
				
				Date toDate = setToDate(testRun, i);
				
				if ( tickList.size() > 0 ) {

                   // Toast.makeText(thisContext.getApplicationContext(), "Downloading latest quotes", Toast.LENGTH_LONG).show();

					Vector<Quote> allQuotes;
					String tickSymbol;
					Date fromNewDate;


					// NEW
					// TAKING THIS OUT AS WE ARE NOW LOADING ALL QUOTES IN TO DB

					allQuotes = dob.getQuotes(tickList, toDate, start_date);


					// Populating vector from DB
					Log.d(TAG,"Calling getQuotes from database");
					allQuotes = getQuotes(tickList, toDate);


                    Log.i(TAG,"Processing Quotes with toDate of: " + toDate + " and allQuotes.size() of " + allQuotes.size());
					if ( allQuotes.size() != 0 ) {
						Log.d(TAG,"Calling loadADX from Class CalcADX");
						CalcADX loadADX = new CalcADX();
						loadADX.calcADX(allQuotes, thisContext);

						setTwoMonthHighLow();

						checkADX();
					}

					Log.i(TAG,"Ending Load and Check of Quotes");

                } else {
					Log.w(TAG, "Ticklist is 0");
				}


		db.setLastQuoteUpdateDate();
    }

	private void showAllQuotes(Vector<Quote> all) {

		Log.d(TAG,"Showing all quotes size" + all.size());

		if ( all.size() == 0 ) {
			Log.d(TAG,"allQuotes is ZERO");
		}

		for (int i=1;i<all.size(); i++) {
			Quote q = all.get(i);
			Log.d(TAG,"allQuotes: DATE: " + q.getSYMBOL() + " : " + q.getDATE() + " HIGH: " + q.getHIGH() + " LOW: " + q.getLOW() + " CLOSE: " + q.getCLOSE());

		}
	}

    private void checkADX() {
    	
		ArrayList<Tick> ticks = db.getAllTicks();

		int counter = 0;

		String symbol = null;
		Tick thisTick = new Tick(0,null,null,(double)0, (double)9999999);
		for (int m=0; m<ticks.size(); m++) {
			counter++;
			thisTick = ticks.get(m);
			symbol = thisTick.getTick();

			String lastDate = db.getMaxQuoteDate(symbol);
			if (lastDate == null) {
				lastDate = start_date;
			}
			db.checkQuotesADX(symbol, lastDate);

		}
		counter = 0;
    }
    
	private void calcADX(Vector<Quote> allQuotesVector) {
		if ( allQuotesVector.size() < 1) {
			Log.e(TAG,"allQuotesVector is 0 - no days to process");
		} else {
			Log.d(TAG,"allQuotesVector is " + allQuotesVector.size());
		}
		String date = null;

		Vector<Quote> oneSymbolVector = new Vector<>();

//		showAllQuotes(oneSymbolVector);

		for (int y=0; y<allQuotesVector.size(); y++) {

			Quote thisQuote = allQuotesVector.get(y); 
			String currSymbol = thisQuote.getSYMBOL();
			if ( y != (allQuotesVector.size() - 1) ) {

				Quote nextQuote = allQuotesVector.get(y+1); 
				String nextSymbol = nextQuote.getSYMBOL();
				if ( nextSymbol.equals(currSymbol) ) {
					oneSymbolVector.add(thisQuote);
				} else {
					oneSymbolVector.add(thisQuote);

					Collections.reverse(oneSymbolVector);
					if ( ! quoteExists(thisQuote.getSYMBOL()) ) {
						calculateALL(oneSymbolVector, true, null);

					} else {
						date = oneSymbolVector.get(oneSymbolVector.size()-1).getDATE();
						calculateALL(oneSymbolVector, false, date);
					}
					oneSymbolVector.clear();
				
				}
			} else {
				oneSymbolVector.add(thisQuote); 
				Collections.reverse(oneSymbolVector);
				
				if ( ! quoteExists(thisQuote.getSYMBOL()) ) {
					calculateALL(oneSymbolVector, true, null);
				} else {
					date = oneSymbolVector.get(0).getDATE();
					calculateALL(oneSymbolVector, false, date); 
				}
				oneSymbolVector.clear();
			}

		}

	}
    
	Vector<Quote> removeDups(Vector<Quote> quotes) {

		for (int i=0; i<quotes.size(); i++) {

			Quote thisQuote = (Quote) quotes.get(i);

			if ( (double)thisQuote.getLOW() == (double)thisQuote.getHIGH()  ) {
				quotes.remove(i);
				i--;
			}

		}

		return quotes;
	}
	
	void calculateALL(Vector<Quote> allQuotes, boolean calcSMA, String date) {	
		
//		Log.i(TAG,"Calculating ADX: " + allQuotes.get(0).getSYMBOL() + " and size " + allQuotes.size() + " SMA: " + calcSMA);

		Vector<Quote> noDuplicateQuotes = removeDups(allQuotes);

		if (noDuplicateQuotes.size() > 0) {

			if ( noDuplicateQuotes.size() > MIN_QUOTE_SIZE || calcSMA == false) {
				calculateTR(noDuplicateQuotes, date);
				calculateDM(noDuplicateQuotes, date);
				calculateTR14(noDuplicateQuotes, calcSMA, date);
				calculateDM14_PLUS(noDuplicateQuotes, calcSMA, date);
				calculateDM14_MINUS(noDuplicateQuotes, calcSMA, date);
				calculateDI14(noDuplicateQuotes);
				calculateDX(noDuplicateQuotes);
				calculateADX(noDuplicateQuotes, calcSMA, date);
				addToDB(noDuplicateQuotes);
			}
		} else { Log.w(TAG,"noDuplicateQuotes is 0, possible duplicate quotes"); }
	}
	
	private boolean quoteExists(String symbol) {

		Quote quotes = db.getQuote(symbol);
		
		if ( quotes == null ) {
			return false;
		} else {
			return true;
		}
			
	}
	

	void addToDB(Vector<Quote> noDupsQuote) {

		db.addQuote(noDupsQuote);
	}

	String goBackOneDay(String date) {
	
		Calendar c1;
		Date tmpDate = null;

				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
 
					c1 = Calendar.getInstance();
					c1.setTime(sdf.parse(date));
					c1.add(Calendar.DATE, -1);  // number of days to add
					tmpDate = c1.getTime(); 
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				date = formatter.format(tmpDate);

		return date;
	}
	
	Quote getYesterdayQuote(String date, String symbol) {
		Quote yQuote = null;
		yQuote = db.getQuoteByDate3(symbol);

		if ( yQuote == null ) {
			Log.i(TAG,"yQuote is null");
		}
		
		return yQuote;
	}

	void calculateTR(Vector<Quote> quotes, String date) {
		CalcEngine calcEngine = new CalcEngine(); 

		Quote yesterdayQuote = null;
		Quote todayQuote;
		double yesterdaysClose;

		String symbol = quotes.get(0).getSYMBOL();
		int start;

		if ( date != null ) {
			start = 0; 
		} else {
			start = 1;
		}
//		Log.i(TAG,"quote.size: " + quotes.size());
		for (int i=start; i<quotes.size(); i++) {

			if ( date != null ) {
				yesterdayQuote = getYesterdayQuote(date, symbol);
				date = null;
			} else {
				yesterdayQuote = quotes.get(i-1);
			}

			todayQuote = quotes.get(i);
			
			if ( yesterdayQuote == null ) {
				Log.i(TAG,"YesterdayQuote is null");	
			}
			
			yesterdaysClose = yesterdayQuote.getCLOSE();
		
			double tr = calcEngine.calcTR(todayQuote, yesterdaysClose);
			todayQuote.setTR(tr);
			double doubleClose = todayQuote.getCLOSE();
			yesterdaysClose = doubleClose;
			
		}
	}

	void calculateDM(Vector<Quote> quotes, String date) {

		CalcEngine calcEngine = new CalcEngine(); 

		double yesterdaysHigh;
		double yesterdaysLow;

		Quote yesterdayQuote;
		Quote todayQuote;

		String symbol = quotes.get(0).getSYMBOL();
		int start;

		if ( date != null ) {
			start = 0; 
		} else {
			start = 1;
		}


		for (int i=start; i<quotes.size(); i++) {

			if ( date != null ) { 

				yesterdayQuote = getYesterdayQuote(date, symbol);
			
				date = null;
			} else { 
				yesterdayQuote = quotes.get(i-1);
			}

			todayQuote = quotes.get(i);

			yesterdaysHigh = yesterdayQuote.getHIGH();
			yesterdaysLow = yesterdayQuote.getLOW();

			DMResult dmRes = new DMResult(1,1);
			dmRes = calcEngine.calcDM(todayQuote, yesterdaysHigh, yesterdaysLow);

			todayQuote.setDM1_PLUS(dmRes.getDmPlus());
			todayQuote.setDM1_MINUS(dmRes.getDmMinus());

			yesterdaysHigh = todayQuote.getHIGH();
			yesterdaysLow = todayQuote.getLOW();

		}
	}

	void calculateDM14_PLUS(Vector<Quote> quotes, boolean calcSMA, String date) {

		Vector<Quote> firstQuotesForSMA = new Vector<Quote>();

		int start;

		if ( date != null ) {
			start = 0; 
		} else {
			start = 1;
		}

		for (int i=start; i<quotes.size(); i++) {


			double firstSum = 0;

			if ( calcSMA ) {

				for ( int i1=i; i1<i+TIMEFRAME; i1++ ) {

					Quote tempVector =  quotes.get(i1);

					double tmpdoubleDM = tempVector.getDM1_PLUS();
					firstSum = firstSum + tmpdoubleDM;
				}

				double sma = firstSum;

				Quote thisQuote1 = quotes.get(i+TIMEFRAME-1);

				thisQuote1.setDM14_PLUS(round(sma,2));
				firstQuotesForSMA.clear();
				firstSum = 0;

				calcSMA = false;

				i = i + TIMEFRAME;	
			}

			Quote todaysQuote = quotes.get(i);
			Quote yesterdaysQuote;

			String symbol = quotes.get(0).getSYMBOL(); 

			if ( date != null ) {
				yesterdaysQuote = getYesterdayQuote(date, symbol);

				date = null;
			} else {
				yesterdaysQuote = quotes.get(i-1);
			}


			double todaysDM1_PLUS = todaysQuote.getDM1_PLUS();
			double yesterdaysDM14_PLUS = yesterdaysQuote.getDM14_PLUS();
			double emaNext = yesterdaysDM14_PLUS - (yesterdaysDM14_PLUS/TIMEFRAME) + todaysDM1_PLUS;
			todaysQuote.setDM14_PLUS(round(emaNext,2));
		}

	}

	@SuppressLint("SimpleDateFormat") void calculateDM14_MINUS(Vector<Quote> quotes, boolean calcSMA, String date) {

		Vector<Quote> firstQuotesForSMA = new Vector<Quote>();

		int start;

		if ( date != null ) {
			start = 0; 
		} else {
			start = 1;
		}

		for (int i=start; i<quotes.size(); i++) {

			double firstSum = 0;

			if ( calcSMA ) {

				for ( int i1=i; i1<i+TIMEFRAME; i1++ ) {

					Quote tempVector =  quotes.get(i1);

					double tmpdoubleDM = tempVector.getDM1_MINUS();
					firstSum = firstSum + tmpdoubleDM;
				}

				double sma = firstSum;

				Quote thisQuote1 = quotes.get(i+TIMEFRAME-1);

				thisQuote1.setDM14_MINUS(round(sma, 2));
				firstQuotesForSMA.clear();
				firstSum = 0;

				calcSMA = false;

				i = i + TIMEFRAME;	
			}

			Quote todaysQuote = quotes.get(i);
			Quote yesterdaysQuote;

			String symbol = quotes.get(0).getSYMBOL();

			if ( date != null ) {
				yesterdaysQuote = getYesterdayQuote(date, symbol);
				date = null;
			} else {
				yesterdaysQuote = quotes.get(i-1);
			}


			double todaysDM1_MINUS = todaysQuote.getDM1_MINUS();
			double yesterdaysDM14_MINUS = yesterdaysQuote.getDM14_MINUS();

			double emaNext = yesterdaysDM14_MINUS - (yesterdaysDM14_MINUS/TIMEFRAME) + todaysDM1_MINUS;
			todaysQuote.setDM14_MINUS(round(emaNext, 2));
		}

	}

	void calculateTR14(Vector<Quote> quotes, boolean calcSMA, String date) {

		Vector<Quote> firstQuotesForSMA = new Vector<Quote>();

		int start;

		if ( date != null ) {
			start = 0; 
		} else {
			start = 1;
		}

		for (int i=start; i<quotes.size(); i++) {

			double firstSum = 0;

			if ( calcSMA ) {

				for ( int i1=i; i1<i+TIMEFRAME; i1++ ) {

					Quote tempVector = quotes.get(i1);
					double tmpdoubleTR = tempVector.getTR();
					firstSum = firstSum + tmpdoubleTR;
				}

				double sma = firstSum;

				Quote thisQuote1 = (Quote) quotes.get(i+TIMEFRAME-1);

				thisQuote1.setTR14(round(sma, 2));

				firstQuotesForSMA.clear();
				firstSum = 0;

				calcSMA = false;
				i = i + TIMEFRAME;	
			}

			Quote todaysQuote = quotes.get(i); 
			Quote yesterdaysQuote;

			String symbol = quotes.get(0).getSYMBOL(); 

			if ( date != null ) {
				yesterdaysQuote = getYesterdayQuote(date, symbol);
				date = null;
			} else {
				yesterdaysQuote = quotes.get(i-1);
			}

			double todaysTR = todaysQuote.getTR();
			double yesterdaysTR14 = yesterdaysQuote.getTR14();
			double emaNext = yesterdaysTR14 - (yesterdaysTR14/TIMEFRAME) + todaysTR;

			todaysQuote.setTR14(round(emaNext, 2));

		}
	}

	public void calculateDI14(Vector<Quote> quotes) {

		Quote thisQuote = null;

		double di14_plus = 0;
		double di14_minus = 0;

		for (int i=0; i<quotes.size(); i++) {

			thisQuote = quotes.get(i);

			if ( thisQuote.getTR14() != 0 ) {

				di14_plus = thisQuote.getDM14_PLUS() / thisQuote.getTR14() * 100;
				di14_minus = thisQuote.getDM14_MINUS() / thisQuote.getTR14() * 100;

				thisQuote.setDI14_PLUS(round(di14_plus,2));
				thisQuote.setDI14_MINUS(round(di14_minus,2));
			}
		}
	}

	public void calculateDX(Vector<Quote> quotes) {

		double dX = 0;

		for (int i=0; i<quotes.size(); i++ ) {

			Quote thisQuote = (Quote) quotes.get(i);
			double absValDI14_MINUS = Math.abs(thisQuote.getDI14_MINUS());
			double absValDI14_PLUS = Math.abs(thisQuote.getDI14_PLUS());
			double sumDI14 = absValDI14_MINUS + absValDI14_PLUS;

			dX = Math.abs(((absValDI14_PLUS - absValDI14_MINUS) / sumDI14)) * 100;

			thisQuote.setDX(dX);
		}
	}

	void calculateADX(Vector<Quote> quotes, boolean calcSMA, String date) {

		Vector<Quote> firstQuotesForSMA = new Vector<Quote>();
		int start;

		if ( date != null ) {
			start = 0; 
		} else {
			start = 14;
		}

		for (int i=start; i<quotes.size(); i++) {

			double firstSum = 0;

			if ( calcSMA ) {

				for ( int i1=i; i1<i+TIMEFRAME; i1++ ) {
					Quote tempVector =  (Quote) quotes.get(i1);

					double tmpdoubleDX = tempVector.getDX();

					firstSum = firstSum + tmpdoubleDX;
				}
				double sma = firstSum;

				Quote thisQuote1 = (Quote) quotes.get(i+TIMEFRAME-1);

				thisQuote1.setADX(round((sma/TIMEFRAME),2));

				firstQuotesForSMA.clear();
				firstSum = 0;

				calcSMA = false;
				i = i + TIMEFRAME;	

			}

			Quote todaysQuote = quotes.get(i);

			Quote yesterdaysQuote;

			String symbol = quotes.get(0).getSYMBOL();

			if ( date != null ) {
				yesterdaysQuote = getYesterdayQuote(date, symbol);
				date = null;
			} else {
				yesterdaysQuote = quotes.get(i-1);
			}


			double todaysDX = todaysQuote.getDX();
			double yesterdaysADX = yesterdaysQuote.getADX();

			double emaNext = ((yesterdaysADX * 13) + todaysDX) / TIMEFRAME;

			todaysQuote.setADX(round(emaNext, 2));

		}
	}
	
	public double round(double d, int decimalPlace) {
		
		double newD = (double) (Math.round(d*100.0)/100.0);
		
		return newD;
	}

	void showAll() {

		List<Quote> allQuotesDB = db.getAllQuotes(); 
		Log.d(TAG,"showAll quotes for allQuotesDB.size(): " + allQuotesDB.size());
		for (int i=0; i<allQuotesDB.size(); i++) {
			Quote q = allQuotesDB.get(i);
//			if ( q.getSYMBOL().equals("SRCL")) {
//			Log.i(TAG,"showAll: " + q.getSYMBOL() + ":" + q.getDATE() + ":" + q.getHIGH() + ":" + q.getLOW() + ":" + q.getCLOSE() + ":" + q.getTR() + ":" + q.getDM1_PLUS() + ":" + q.getDM1_MINUS() + ":" + q.getTR14() + ":" + q.getDM14_PLUS() + ":" + q.getDM14_MINUS() + ":" + q.getDI14_PLUS() + ":" + q.getDI14_MINUS() + ":" + q.getDIFF() + ":" + q.getSUM() + ":" + q.getDX() + ":" + q.getADX());
//			Log.i(TAG,"showAll: " + q.getSYMBOL() + ":" + q.getDATE() + " : ADX - "  + q.getADX() + " : DI14_PLUS - " + q.getDI14_PLUS() + " : DI14_MINUX - " + q.getDI14_MINUS());
			Log.i(TAG,"showAll: " + q.getSYMBOL() + ":" + q.getDATE() + " : ADX - "  + q.getADX() + " : LOW - " + q.getLOW() + " : CLOSE - " + q.getCLOSE());

//			}
		}

		int count = allQuotesDB.size();
		Log.d(TAG,"Count is: " + count);

		List<History> histList = db.getAllClosedHist();

	}

	void showADX(Vector<Quote> quotes) {
		for (int i=0; i<quotes.size(); i++) {

			Quote q = quotes.get(i);
			String date = q.getDATE();
			double close = q.getCLOSE();
			double adx = q.getADX();
			String symbol = q.getSYMBOL();

			Log.i(TAG,"Show ADX: " + symbol + " " + date + " " + close + " " + adx);

		}
	}

	void showDB(String sym) {

		Log.i(TAG,"Showing DB Quotes: " + sym);
		
		List<Quote> quotesAll = db.getAllQuotes();
		for (int i=0; i<quotesAll.size(); i++) {
			Quote q = quotesAll.get(i);
			String s = quotesAll.get(i).getSYMBOL();
			
			if ( s.equals(sym) ) { 
				Log.i(TAG,"Quotes from DB: " + q.getID() + ", "+ q.getDATE() + ", " + q.getSYMBOL() + ", " + q.getLOW() + ", " + q.getHIGH()  + ", " + q.getCLOSE() + " : " + q.getADX());

			}
		}

	}
	
	void showDB() {

		List<Quote> quotesAll = db.getAllQuotes();
		for (int i=0; i<quotesAll.size(); i++) {
			Quote q = (Quote) quotesAll.get(i);
			Log.i(TAG,"showDB: " + q.getID() + ", "+ q.getSYMBOL() + ", " + q.getDATE() + ", " + q.getLOW() + ", " + q.getHIGH() + ", " + q.getCLOSE() + ", " + q.getADX());
		}

	}

	void showDM(Vector<Quote> quotes) {

		for (int i=0; i<quotes.size(); i++) {

			Quote q = (Quote)quotes.get(i);
			Log.i(TAG,"DM14+: " + q.getDM14_PLUS()); 
			Log.i(TAG,"DM14-: " + q.getDM14_MINUS()); 

		}

	}

	void showTR14(Vector<Quote> quotes) {

		for (int i=0; i<quotes.size(); i++) {

			Quote q = (Quote)quotes.get(i);
			Log.i(TAG,"TR14: " + q.getTR14()); 
		}

	}

	void showQuote(Quote todaysQuote) {
		Quote q = todaysQuote;
		
		Log.i(TAG,"Quote: " + q.getSYMBOL() + " : " + q.getDATE() + "" + q.getHIGH() + "-LOW-" + q.getLOW() + "-CLOSE-" + q.getCLOSE() + "-TR-" + q.getTR() + "-DM1+-" + q.getDM1_PLUS() + "-DM1--" + q.getDM1_MINUS() + "-TR14-" + q.getTR14() + "-DM14+-" + q.getDM14_PLUS() + "-DM14--" + q.getDM14_MINUS() + "-DI14+-" + q.getDI14_PLUS() + "-DI14--" + q.getDI14_MINUS() + "-DIFF-" + q.getDIFF() + "-SUM-" + q.getSUM() + "-DX-" + q.getDX() + "-ADX-" + q.getADX());


	}
	
	private void setTwoMonthHighLow() {
		ArrayList<Tick> tickList = db.getAllTicks();

		for (int i=0; i<tickList.size(); i++) {

			String symbol = tickList.get(i).getTick();
			ArrayList<Quote> oneQuoteList = (ArrayList<Quote>) db.getAllQuotesByTick(symbol);

			List<Quote> lastSixty = new ArrayList<Quote>();
	
			Tick t = tickList.get(i);

			double tickHigh = t.getTwoMonthHigh();
			double tickLow = t.getTwoMonthLow();
			
			if ( oneQuoteList.size() > 0 ) {
				lastSixty = oneQuoteList;

				if ( lastSixty.size() > LOWHIGH_RANGE ) {
					lastSixty = oneQuoteList.subList(lastSixty.size()-LOWHIGH_RANGE, lastSixty.size());
				} 
			
				tickHigh = t.getTwoMonthHigh();
				tickLow = t.getTwoMonthLow();
				
				for (int k=0; k<lastSixty.size(); k++) {
					if (lastSixty.get(k).getCLOSE() > tickHigh) {
						tickHigh = lastSixty.get(k).getCLOSE();
					} else if (lastSixty.get(k).getCLOSE() < tickLow) {
						tickLow = lastSixty.get(k).getCLOSE();
					}
				}

				t.setTwelveMonthHigh(tickHigh);
				t.setTwelveMonthLow(tickLow);
				db.updateTick(t);
			}
		}
		
		
	}
	
	private Date setToDate(boolean testRun, int i) {

		Date toDate = null;

		if (!testRun) {
			toDate = getToDate(OFFSET);
		} else {
			toDate = getToDate(i);
		}
		return toDate;

	}


	private void loadTicks() {


		IOStream iOS = new IOStream(thisContext);
		ArrayList<Tick> allTicksFromFile = iOS.readData();

		ArrayList<Tick> allTicksFromDB = db.getAllTicks();
		
		boolean tickExists = false;
		
		for (int i=0; i<allTicksFromFile.size(); i++) {
			
			for (int y=0; y<allTicksFromDB.size(); y++) {
				if ( allTicksFromFile.get(i).getTick().equals(allTicksFromDB.get(y).getTick()) ) {
					tickExists = true;
				} 
				
			}
			 
			if ( tickExists ) {
//				Log.d(TAG,"Not adding Tick, already exists in Database");
			} else {
				Log.d(TAG,"Adding Tick, it's new!");
				db.addTick(allTicksFromFile.get(i));
			}
			tickExists = false;
		}
		
		

	}
	
	private ArrayList<Tick> removeBadTicks(ArrayList<Tick> tickList) {
		// TODO Auto-generated method stub
		
		ArrayList<String> badList = new ArrayList<String>();
		
		badList.add("TLW.L");
		badList.add("BA.L");
		badList.add("IMI.L");
		badList.add("RSA.L");
		badList.add("ENRC.L");
		badList.add("SAB.L");
		
		for (int i=0; i<tickList.size(); i++) {
			
			for (int j=0; j<badList.size(); j++) {
				String tickListString = tickList.get(i).getTick();
				String badListString = badList.get(j);
			 
				if (tickListString.equals(badListString)) {
					tickList.remove(i);
//					Log.d(TAG,"Removing bad tick: " + tickListString);
				}
			}
			
		}
		
		return tickList;
	}


	
	
	public Date getToDate(int offset) {

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -offset);

		return cal.getTime();	
	}	
	
	private boolean wifiEnabled(Context c) {

	    ConnectivityManager cm =
	            (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo netInfo = cm.getActiveNetworkInfo();
		 	if (netInfo != null) {
//				Log.d(TAG, netInfo.toString());
//	        if (netInfo != null && netInfo.isConnectedOrConnecting() && netInfo.getTypeName().equals("WIFI") ) {

//				Log.d(TAG, "netInfo: " + netInfo.isConnectedOrConnecting() + " : " + netInfo.getTypeName());

				if (netInfo != null && netInfo.isConnectedOrConnecting() && (netInfo.getTypeName().equals("WIFI") || netInfo.getTypeName().equals("MOBILE"))) {
//					Log.d(TAG, "Wifi is enabled");
					return true;
				} else {
					Log.i(TAG, "Wifi is not enabled");
				}
			} else {
//				Log.d(TAG, "netInfo is null! Definitely no WiFi");
			}
//	        Log.i(TAG,"netInfo: " + netInfo);
	        return false;

	}

	private Vector<Quote> getQuotes(List<Tick> tickList, Date toDate) {
//		private Vector<Quote> getQuotes(List<Tick> tickList, Date fromDate, Date toDate) {


			Vector<Quote> oneTickQuotes = null;
		Vector<Quote> allQuotes = new Vector<Quote>();


		String tickSymbol;
		String toNewDate = null;



		for (int i=0; i<tickList.size(); i++) {
//			Log.d(TAG, "Loading from DB");
			tickSymbol  = tickList.get(i).getTick();
			String fromDate = db.getMaxQuoteDate(tickSymbol);
//			String fromDate = getFromDateForTest();


			if (fromDate == null) {
				fromDate = start_date;
			}

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");


			toNewDate = df.format(toDate);


//			db.getMaxQuoteDate(tickSymbol);
			oneTickQuotes = db.getQuotesFromDB(tickSymbol, fromDate, toNewDate);
//			for (int k=0; k<oneTickQuotes.size(); k++) {
//				Quote q = oneTickQuotes.get(k);
//			}


			allQuotes.addAll(oneTickQuotes);
		}

//		db.showAllRaw();

		return allQuotes;

	}


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        private ProgressDialog prgDialog;

        @Override
        protected String doInBackground(String... params) {

			// For TEST, start a loop here
			if ( !testRun ) {
				Log.d(TAG, "Starting checkQuotes_All");
				checkQuotes_All(testRun, 0);
				Log.d(TAG, "Calling CheckPotentials");
				new CheckPotentials(thisContext, testRun);
			} else {
				db.truncateDatabase();

				// NEW
//				loadAllQuotes();


				int counter=0;
				testOffSet = getOffSetFromDate(start_date);
				for (int i=testOffSet; i>=0; i--) {
					Log.d(TAG, "Starting checkQuotes_All: TESTRUN: " + counter);
					counter++;
					checkQuotes_All(testRun, i);

					new CheckPotentials(thisContext, testRun);


				}
			}
			// End the TEST loop here
//			showAll();
            return null;
        }

		private void loadAllQuotes() {

			List<Quote> allQuotes;

			loadTicks();

			ArrayList<Tick> tickList= db.getAllTicks();
			Date toDate = getToDate(0);

			Log.d(TAG,"Loaded ticklist - " + db.getAllTicks().size());


			allQuotes = dob.getQuotes(tickList, toDate, start_date);


			Log.d(TAG,"Loaded all RAW tick quotes into database: " + db.getRawCount());
		}
        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            //finalResult.setText(result);

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            super.onPreExecute();
            //prgDialog = new ProgressDialog(AlarmReceiver.thisContext);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);
//            Log.d(TAG,"Doing onPogressUpdate");
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }

		int getOffSetFromDate (String date) {

			Calendar c1;
			Calendar now;
			Date tmpDate = null;
			long msBetween = 0;
			int daysBetween = 0;

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				c1 = Calendar.getInstance();
				c1.setTime(sdf.parse(date));
				c1.add(Calendar.DATE, 60);  // number of days to add
				tmpDate = c1.getTime();

				now = Calendar.getInstance();
				int millisecondsBetweeen = now.compareTo(c1);


				msBetween = now.getTimeInMillis() - c1.getTimeInMillis();



			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			date = formatter.format(tmpDate);

			daysBetween = (int) (msBetween / (1000*60*60*24));

			return (int) daysBetween;
		}
    }


}
