package sds.com.adx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by chrismilner on 26/09/16.
 */
public class CalcADX {

    private String TAG = "CalcADX";

    private static final int TIMEFRAME = 14;
    private static final int MIN_QUOTE_SIZE = 30;

    static DatabaseHandler db;

    public void calcADX(Vector<Quote> allQuotesVector, Context context) {

        Context thisContext = context;

        db = new DatabaseHandler(thisContext);

        if ( allQuotesVector.size() < 1) {
            Log.e(TAG,"allQuotesVector is 0 - no days to process");
        } else {
            Log.d(TAG,"allQuotesVector is " + allQuotesVector.size());
        }
        String date = null;

        Vector<Quote> oneSymbolVector = new Vector<>();

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

    private boolean quoteExists(String symbol) {

        Quote quotes = db.getQuote(symbol);

        if ( quotes == null ) {
            return false;
        } else {
            return true;
        }

    }

    void calculateALL(Vector<Quote> allQuotes, boolean calcSMA, String date) {

//        Log.i(TAG,"Calculating ADX: " + allQuotes.get(0).getSYMBOL() + " and size " + allQuotes.size() + " SMA: " + calcSMA);

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
        } else {
//            Log.w(TAG,"noDuplicateQuotes is 0, possible duplicate quotes");
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

    void addToDB(Vector<Quote> noDupsQuote) {

        db.addQuote(noDupsQuote);
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

}
