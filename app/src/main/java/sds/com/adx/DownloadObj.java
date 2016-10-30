package sds.com.adx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import java.net.URL;

public class DownloadObj  {

	private static final String TEMP_FILENAME = "test.txt";

	private final String TAG = "DownloadObj";
	private String lineHolder[] = null; 
	private int startLine;

    private TextView finalResult;

	Context thisContext;
	
	static DatabaseHandler db;
	
	Vector<Quote> allQuotesVector = new Vector<Quote>();


	boolean dbExists = false;
	boolean donotReverse = false;
	
	String quoteType;
	
	public DownloadObj(Context c) {
		
		thisContext = c;
		
		db = new DatabaseHandler(thisContext);
		
	}

	
//	void getTickFromURL(ArrayList<String> tickerList, String urlTicker, String indexURLString, String index, String appendage) {
//
//		boolean alreadyAdded = false;
//
////		Log.d(TAG,"urlTicker: " + urlTicker);
////		Log.d(TAG,"indexURLString: " + indexURLString);
////		Log.d(TAG,"index: " + index);
////		Log.d(TAG,"appendage: " + appendage);
//
//
//		String startPatternString = "nameCode";
//		int patternOffSet = 9;
//		String endPatternString = "\"";
//
//		try {
//			String result = CSVFunctions.getStringfromURL(thisContext, urlTicker);
//			//Log.d(TAG,"FTSE 100 download wiki : " + result);
//			lineHolder = result.split("\\r?\\n");
//			for (int y = 0; y < lineHolder.length; y++) {
//				if (lineHolder[y].contains(indexURLString)) {
//						//																		Log.d(TAG,lineHolder[y]);
//						//	String delims = "[[nameCode]]";
//						int startStr = lineHolder[y]
//								.lastIndexOf(startPatternString);
//						String tmpString = lineHolder[y].substring(
//								startStr + patternOffSet, lineHolder[y].length());
//						int endStr = tmpString.indexOf(endPatternString);
//						String tickSymbol = tmpString.substring(0, endStr);
//						  if (tickSymbol.length() > 0 && tickSymbol.charAt(tickSymbol.length()-1)=='.') {
//							    tickSymbol = tickSymbol.substring(0, tickSymbol.length()-1);
//							  }
//						  if (tickSymbol.length() > 0 && tickSymbol.charAt(tickSymbol.length()-2)=='.') {
//							    tickSymbol = tickSymbol.replace('.', '-');
//							  }
//
//
//
//						  for (int i1=0; i1<tickerList.size(); i1++) {
//							  if (tickerList.get(i1).equals(tickSymbol)) {
//								  alreadyAdded = true;
//							  }
//							  ArrayList<Tick> ticks = db.getAllTicks();
//							  for (int i2=0; i2<ticks.size(); i2++) {
////								  Log.d(TAG,"tick from DB: " + ticks.get(i2).getTick() + " with " + tickSymbol);
//								  if (ticks.get(i2).getTick().equals(tickSymbol + appendage)) {
//									  alreadyAdded = true;
////									  Log.d(TAG,"Setting to TRUE!");
//								  }
//							  }
//						  }
//						  if ( ! alreadyAdded ) {
//							  tickerList.add(tickSymbol);
////							  Log.d(TAG,"AddTick: " + tickSymbol + appendage);
//							  Tick ticker = new Tick(0,tickSymbol + appendage, index,0,9999999);
//							  db.addTick(ticker);
//						  } else {
////							  Log.d(TAG,"Not adding Tick: " + tickSymbol + " as alreadyAdded is true");
//						  }
//						  alreadyAdded=false;
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.e(TAG, "Unable to connect to internet for FTSE");
//		}
//
//	}
	
//	public boolean loadTickers(String index) {
//
//		String pageTitle = null;
//		String appendage = null;
//		ArrayList<String> tickerList = new ArrayList<String>();
//
//		if ( index.equals("FTSE") ) {
//			pageTitle = "FTSE_100_Index";
//			appendage = ".L";
//		}
//
//		if ( index.equals("NASDAQ")) {
//			pageTitle = "NASDAQ-100";
//
//		}
//
//		String url = "http://en.wikipedia.org/wiki/" + pageTitle;
//		ArrayList<String> tickerURLs = new ArrayList<String>();
//
//		try {
//			String result = CSVFunctions.getStringfromURL(thisContext, url);
//			lineHolder = result.split("\\r?\\n");
//			for (int y = 0; y < lineHolder.length; y++) {
////				Log.v(TAG,"linHolder[y]= " + lineHolder[y]);
////				if ( (lineHolder[y].contains("<td><a href=\"/wiki/") && ! lineHolder[y].contains("Help")) && lineHolder[y].contains("Aggreko")) {
//				if ( (lineHolder[y].contains("<td><a href=\"/wiki/") && ! lineHolder[y].contains("Help")) ) {
//
//					String delims = "[\"]+";
//					List<String> cols = Arrays.asList(lineHolder[y]
//							.split(delims));
//
//					tickerURLs.add("http://en.wikipedia.org" + cols.get(1));
////					Log.v(TAG,"Found ADN: " + cols.get(1));
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.e(TAG, "Unable to connect to internet for FTSE");
//			return false;
//		}
//
////		 Log.d(TAG,"urlTicker size: " + tickerURLs.size());
//
//		for (int i=0; i<tickerURLs.size(); i++) {
//
//			String urlTicker = tickerURLs.get(i);
////			Log.d(TAG,"urlTicker: " + urlTicker);
//
//			String indexURLString = "stock-prices-search.html?nameCode=";
//
//			getTickFromURL(tickerList, urlTicker, indexURLString, index, appendage);
//
//
//
//		}
//
//		return true;
//
//	}
	
	public Vector<Quote> getQuotes(ArrayList<Tick> tickSymbols, Date to, String testDate) {

		
		String getPrefix = null;
		String getPrefixIndex = null;
		String getSuffix = null;

		Log.d(TAG,"Getting Quotes");

//		SimpleDateFormat year = new SimpleDateFormat("yyyy"); 
//		SimpleDateFormat month = new SimpleDateFormat("MM");
//		SimpleDateFormat day = new SimpleDateFormat("dd");

		String tickSymbol = null;

		String date = null;

//		Log.d(TAG,"tickSymbols.size() " + tickSymbols.size());
		String url = null;
		Log.d(TAG,"TickSymbolSize is " + tickSymbols.size());
		for (int i=0; i<tickSymbols.size(); i++) {
			tickSymbol  = tickSymbols.get(i).getTick();
			if ( tickSymbol.equals("FTSE") ) {
				url = (getPrefixIndex + tickSymbol + getSuffix);
			} else {
				url = (getPrefix + tickSymbol + getSuffix);
			}
			Date fromNewDate = null;
//			String fromDate = tickSymbols.get(i).getLastCloseDate();
			String fromDate = db.getMaxQuoteDate(tickSymbol);
			if (fromDate == null) {
				fromDate = testDate;
			}
//			Log.d(TAG,"Got max date for " + tickSymbol  + " as " + fromDate);
//			Log.d(TAG,"Got LastCloseDate from tick: " + tickSymbols.get(i).getTick() + " : " + fromDate); 
			Calendar c1;
			try {
				//				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				c1 = Calendar.getInstance();
				c1.setTime(sdf.parse(fromDate));
				fromNewDate = c1.getTime(); 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.d(TAG,"Error here");
				e.printStackTrace(); 
			}

			url = formURL(tickSymbol, fromNewDate, to);
			Log.d(TAG,"url: " + url);
			try {
//				Log.d(TAG,"Searching HTTP for URL: " + url);
				String result = CSVFunctions.getStringfromURL(thisContext, url, tickSymbol, "Inter");
//				db.showAllRaw();
//				Log.d(TAG,"result: " + result);
				if (quoteType.equals("InterDay")) {
//					Log.d(TAG,"Getting InterDay");
					if (result.contains("Date")) {
						String lines[] = result.split("\\r?\\n");
						for (int y = 0; y < lines.length; y++) {
							List<String> cols = Arrays.asList(lines[y].split("\\s*,\\s*"));
							if (y != 0) {
								int id = y;
								date = cols.get(0); // Date
								String highStr = (String) cols.get(2);
								double high = Double.parseDouble(highStr);
								String lowStr = (String) cols.get(3);
								double low = Double.parseDouble(lowStr);
								String closeStr = (String) cols.get(4);
								double close = Double.parseDouble(closeStr);
								double adx = 0;
								double tr14 = 0;
								double dm14_plus = 0;
								double dm14_minus = 0;
								double twelveHigh = 0;

								Quote quote = new Quote(id, tickSymbol, date,
										high, low, close, adx, tr14, dm14_plus,
										dm14_minus, twelveHigh);
								allQuotesVector.add(quote);
//								Log.d(TAG,"DATE: " + quote.getDATE() + " HIGH: " + quote.getHIGH() + " LOW: " + quote.getLOW() + " CLOSE: " + quote.getCLOSE());
							}
						}
//					} else { Log.d(TAG,"String does not contain Date:");
				
					}
				} else if (quoteType.equals("IntraDay")) { 
//					Log.d(TAG,"Getting IntraDay");
					//if (result.contains("Date")) {
						String lines[] = result.split("\\r?\\n");
						//											Log.d(TAG,"Lines count is: " + lines.length);
						for (int y = 0; y < lines.length; y++) {
							List<String> cols = Arrays.asList(lines[y]
									.split("\\s*,\\s*"));

							if (y != 0) {
								int id = y;
								date = cols.get(0); // Date
								String highStr = (String) cols.get(1);
								double high = Double.parseDouble(highStr);
								String lowStr = (String) cols.get(2);
								double low = Double.parseDouble(lowStr);
								String closeStr = (String) cols.get(3);
								double close = Double.parseDouble(closeStr);
								String twelveHighStr = (String) cols.get(4);
								double twelveHigh = Double.parseDouble(twelveHighStr);
								double adx = 0;
								double tr14 = 0;
								double dm14_plus = 0;
								double dm14_minus = 0;

								Quote quote = new Quote(id, tickSymbol, date,
										high, low, close, adx, tr14, dm14_plus,
										dm14_minus, twelveHigh);
								
								allQuotesVector.add(quote);
//								Log.d(TAG,"Got IntraDay Quote: " + close + " for " + tickSymbol);
//								ArrayList<Tick> ticks = db.getAllTicks();
//								for (int i1=0; i1<ticks.size(); i1++) {
//								}
							}
						}
					//}
				}
			} catch (Exception e) {
//									Log.e("Error", e.getMessage());
									e.printStackTrace();
				Log.e(TAG, "getQuotes: Unable to connect to internet");
			}
		} 
//		Log.v(TAG, "Returning allQuotesVector size: " + allQuotesVector.size());
		return allQuotesVector;
		// Load manual values using getQuoteStatic, remember to not reverse

		//			showDB();


	}
	
	public Pair getQuotes(String symbol) {

        //new AsyncTaskRunner().execute(symbol);

		String getPrefix = null;
		String getSuffix = null;
		getPrefix = "http://finance.yahoo.com/d/quotes.csv?s=";
		getSuffix = "&f=" + "d1hgl1";
		quoteType = "IntraDay";
		String url = (getPrefix + symbol + getSuffix);
//		Log.d(TAG,"Intra day url: " + url);
		double midDayPrice = 0;
		double highPrice = 0;
		double lowPrice = 0;
		try {
			String result = CSVFunctions.getStringfromURL(thisContext, url, symbol, "Intra");
			Log.d(TAG,"url: " + url);
			Log.d(TAG,"result: " + result);
			String[] elephantList = result.split(",");
			try {
				midDayPrice = Double.parseDouble(elephantList[3]);
				highPrice = Double.parseDouble(elephantList[2]);
				lowPrice = Double.parseDouble(elephantList[1]);
			} catch (Exception e) {
				Log.d(TAG,"EXCEPTION " + e.getMessage());
				Log.d(TAG,"EXCEPTION result: " + result);

			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "getQuotes: Unable to connect to internet");
			Pair dummyPair  = new Pair(-1,-1);
			return dummyPair;
		}
//		Log.d(TAG,"Returning midDayPrice: " + midDayPrice + " and not high or [2] which is: " + highPrice + " or low: " + lowPrice);
		Pair quotePair = new Pair(midDayPrice, lowPrice);

		return quotePair;
	}

	private String formURL(String tickSymbol, Date from, Date to) {
		// TODO Auto-generated method stub
		String getPrefix = null;
		String getPrefixIndex = null;
		String getSuffix = null;
		
		SimpleDateFormat year = new SimpleDateFormat("yyyy"); 
		SimpleDateFormat month = new SimpleDateFormat("MM"); 
		SimpleDateFormat day = new SimpleDateFormat("dd");
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(from); 
		c.add(Calendar.DATE, 1);
		from = c.getTime();
		
		String fromYear = year.format(from);
		String fromMonthTmp = month.format(from);
		String fromDay = day.format(from);
		String toYear = year.format(to);
		String toMonthTmp = month.format(to);
		String toDay = day.format(to);	

		int fromMonthInt = Integer.parseInt(fromMonthTmp.toString());
		int toMonthInt = Integer.parseInt(toMonthTmp.toString());
		
		
		fromMonthInt--;
		toMonthInt--;
		
		String fromMonth = String.valueOf(fromMonthInt);
		String toMonth = String.valueOf(toMonthInt);
		
//		if (  ! (fromYear.equals(toYear) && fromMonth.equals(toMonth) && fromDay.equals(toDay)) ) {
			getPrefix = "http://ichart.finance.yahoo.com/table.csv?s=";
			getPrefixIndex = "http://ichart.finance.yahoo.com/table.csv?s=%5E";
			getSuffix = "&a=" + fromMonth + "&b=" + fromDay + "&c=" + fromYear + "&d=" + toMonth + "&e=" + toDay + "&f=" + toYear + "&g=d&ignore=.csv";
			quoteType = "InterDay";
//		} else  {
//			getPrefix = "http://finance.yahoo.com/d/quotes.csv?s=";
//			getSuffix = "&f=" + "d1hgl1";
//			quoteType = "IntraDay";
//
//		}
		
//		String url = (getPrefixIndex + tickSymbol + getSuffix);
		String url = (getPrefix + tickSymbol + getSuffix);

//		Log.d(TAG,"Returning url: " + url);
		return url;
	}

	public Quote getSingleQuote(String tickSymbol) {
		
		String getPrefix = "http://finance.yahoo.com/d/quotes.csv?s="; 
		String getSuffix = "&f=" + "d1hgl1";
		String url = (getPrefix + tickSymbol + getSuffix);
		String date = null;

//		Log.d(TAG,"URL: " + url);		
		Quote quote = null;
		
		try {
			String result = CSVFunctions.getStringfromURL(thisContext, url, tickSymbol,"Intra");
					String lines[] = result.split("\\r?\\n");
					for (int y = 0; y < lines.length; y++) {
						List<String> cols = Arrays.asList(lines[y]
								.split("\\s*,\\s*"));
							int id = y;
							date = cols.get(0); // Date

							String highStr = (String) cols.get(1);
							double high = Double.parseDouble(highStr);

							String lowStr = (String) cols.get(2);
							double low = Double.parseDouble(lowStr);
							String closeStr = (String) cols.get(3);
							double close = Double.parseDouble(closeStr);

							double adx = 0;
							double tr14 = 0;
							double dm14_plus = 0;
							double dm14_minus = 0;
							double twelveHigh = 0;
							
							quote = new Quote(id, tickSymbol, date,
									high, low, close, adx, tr14, dm14_plus,
									dm14_minus,twelveHigh);


					}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "getSingleQuote: Unable to connect to internet");
		}
		
		return quote;
		
	}


//    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
//
//        private String resp;
//
//        @Override
//        protected String doInBackground(String... params) {
//            publishProgress("Sleeping..."); // Calls onProgressUpdate()
//            String getPrefix = null;
//            String getSuffix = null;
//            getPrefix = "http://finance.yahoo.com/d/quotes.csv?s=";
//            getSuffix = "&f=" + "d1hgl1";
//            quoteType = "IntraDay";
//            String url = (getPrefix + params[0] + getSuffix);
//            Log.d(TAG,"Intra day url: " + url);
//            double midDayPrice = 0;
//            try {
//                String result = CSVFunctions.getStringfromURL(url);
//                Log.d(TAG,"result: " + result);
//                String[] elephantList = result.split(",");
//                midDayPrice = Double.parseDouble(elephantList[3]);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e(TAG, "getQuotes: Unable to connect to internet");
//              //  return "Price";
//                return "-1";
//            }
//            //return "Price";
//            return double.toString(midDayPrice);
//        }
//
//        /*
//         * (non-Javadoc)
//         *
//         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
//         */
//        @Override
//        protected void onPostExecute(String result) {
//            // execution of result of Long time consuming operation
//            //finalResult.setText(result);
//
//        }
//
//        /*
//         * (non-Javadoc)
//         *
//         * @see android.os.AsyncTask#onPreExecute()
//         */
//        @Override
//        protected void onPreExecute() {
//            // Things to be done before execution of long running operation. For
//            // example showing ProgessDialog
//        }
//
//        /*
//         * (non-Javadoc)
//         *
//         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
//         */
//        @Override
//        protected void onProgressUpdate(String... text) {
//            //finalResult.setText(text[0]);
//            Log.d(TAG,"Doing onPogressUpdate");
//            // Things to be done while execution of long running operation is in
//            // progress. For example updating ProgessDialog
//        }
//    }


}


