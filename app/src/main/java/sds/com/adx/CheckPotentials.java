package sds.com.adx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

//import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Pair;
//import android.view.View;
//import android.widget.Toast;

public class CheckPotentials {

    private static final String TAG = "CheckPotentials";
    private final String ALERT_NUMBER = "96557366";
    private final double TRIGGER_BUFFER = .01f; // Percentage of offer price to take
    private final double LIMIT_BUFFER = .008f; // Percentage of offer price to take
//	private boolean TESTRUN = false;

    private static final int MAX_DAYS_ACTIVE = 7;
    private static final int MAX_DAYS_ACTIVE_AFTER_TRIGGER = 7;
    private static final int HOT = 3;  // No of days of dip/rise
    private static final int MARGIN = 200;


    static DatabaseHandler db = null;
    DownloadObj dob = null;

    Context thisContext;
    Position newPosition = new Position();

    private int notificationCounter = 0;

    private boolean thisTestRun;

    public CheckPotentials(Context c, boolean testrun) {

        thisContext = c;
        thisTestRun = testrun;

        dob = new DownloadObj(thisContext);

        db = new DatabaseHandler(thisContext);

//        db.deleteAllHistory();
        //openWhatsApp("Chris");
        Log.d(TAG, "Starting checkPotentials");
        checkPotentials();
        Log.d(TAG, "Ending checkPotentials");
        Log.d(TAG, "Starting checkPositions");
        checkPositions();
        Log.d(TAG, "Ending checkPositions");
        sendMessage(notificationCounter++, "Completed check potentials");


    }

    public void checkPotentials() {

        double current = 0;
        double low = 0;
        boolean isRising = false;
        boolean balanced;


//        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        List<Potential> potentials = db.getAllPotentials();

        notificationCounter++;
        if (potentials.size() > 0) {

//            for (int i = 0; i < potentials.size(); i++) {
//                Potential p = potentials.get(i);
////                Log.d(TAG,"Potential days active: " + p.daysActive + " for " + p.getSymbol());
//                if (p.daysActive >= MAX_DAYS_ACTIVE && p.getTrigger() == 0) {
//                    db.deletePotential(p);
////                    Log.i(TAG,"Just deleted expired Potential from DB " + p.getSymbol() + ":" + p.getDaysActive());
//                } else if (p.daysActive >= (MAX_DAYS_ACTIVE_AFTER_TRIGGER + MAX_DAYS_ACTIVE)) {
//                    db.deletePotential(p);
//                    Log.i(TAG,"Just deleted expired Potential from DB AFTER TRIG " + p.getSymbol() + ":" + p.getDaysActive());
//                }
//            }


            for (int i = 0; i < potentials.size(); i++) {
                Potential p = potentials.get(i);
                Log.d(TAG,p.getSymbol() + " : Checking daysActive first");
                if (p.daysActive >= MAX_DAYS_ACTIVE && p.getTrigger() == 0) {

                    Log.i(TAG,"About to delete Potential from array " + p.getSymbol() + ":" + p.getDaysActive());
                    db.deletePotential(p);
                    potentials.remove(i);
                } else if (p.daysActive >= (4*MAX_DAYS_ACTIVE)) {

                    Log.i(TAG,"About to delete expired Potential from array AFTER TRIG " + p.getSymbol() + ":" + p.getDaysActive());
                    db.deletePotential(p);
                    potentials.remove(i);
                }
            }


            for (int i = 0; i < potentials.size(); i++) {
                Potential p = potentials.get(i);

//                showPotentials();

                ArrayList<Quote> listQuotesP = getAllPotQuotes(p);

                if (listQuotesP.size() > 1) {


                    p.setDaysActive(setDaysActive(listQuotesP));
                    p.setDirection(getDirection(listQuotesP));

                    int absExposedWeight = Math.abs(getOpenBuys() - getOpenSells());
                    if (absExposedWeight < 4) {
                        balanced = true;
                    } else {
                        balanced = true;
                    }

                    if (!thisTestRun) {
                        Pair quotePair = dob.getQuotes((listQuotesP.get(listQuotesP.size() - 1)).getSYMBOL());
                        current = (Double) quotePair.first;
                        low = (Double) quotePair.second;
                        if (current >= low) {
                            isRising = true;
                        }

//				Log.d(TAG,"Current for NOT TESTRUN: " + p.getSymbol() + " is " + current);
                    } else {
                        current = listQuotesP.get(listQuotesP.size() - 1).getCLOSE();
                        Log.d(TAG, "listQuotesP.siz = " + listQuotesP.size());
                        double last = 0;
                        if (listQuotesP.size() > 1) {
                            last = listQuotesP.get(listQuotesP.size() - 2).getCLOSE();
                        }
                        if (current >= last) {
                            isRising = true;
                        } else {
                            isRising = false;
                        }
//				Log.d(TAG,"Current for TESTRUN: " + p.getSymbol() + " is " + current);
                    }

                    int currentInt = (int) current;

                    if (current != -1) {
                        p.setCurrent(round(current, 2));
//                Log.d(TAG,"Just set current to: " + round(current,2));
                    }

                    String raisedDate = listQuotesP.get(listQuotesP.size() - 1).getDATE();

//			double stop=0;
//			double limit=0;
//			int ppp=0;
                    if (p.getTrigger() == 0 && listQuotesP.size() > 2) {
                        Log.d(TAG, "Calling setHotOrNot");
                        double trigger = setHotOrNot(listQuotesP);


                        if (trigger != 0) {
                            p.setTrigger(trigger);
                            Log.d(TAG, "Tracking: Setting potential trigger to : " + p.getTrigger() + " for " + p.getSymbol() + " after " + p.getDaysActive());

                            if (p.getDirection().equals("BUY")) {
                                p.setStop(setStop(listQuotesP, p.getDirection(), p.getStartId()));
                                p.setLimit(setLimit(listQuotesP, p.getDirection(), p.getStartId()));
                                p.setPpp(setPPP(p.getStop(), p.getLimit(), current, p.getSymbol()));
                                Log.d(TAG, "PRE-TRIG Buy: " + p.symbol + " : " + raisedDate + " : " + trigger + " : " + p.getStop() + " : " + p.getLimit() + " : " + p.getPpp());
                                sendMessage(notificationCounter++, "PRE-TRIG Buy: " + p.symbol + " : " + trigger + " : " + p.getStop() + " : " + p.getLimit() + " : " + p.getPpp());
                            } else if (p.getDirection().equals("SELL")) {
                                p.setStop(setStop(listQuotesP, p.getDirection(), p.getStartId()));
                                p.setLimit(setLimit(listQuotesP, p.getDirection(), p.getStartId()));
                                p.setPpp(setPPP(p.getStop(), p.getLimit(), current, p.getSymbol()));
                                sendMessage(notificationCounter++, "PRE-TRIG Sell: " + p.symbol + " : " + trigger + " : " + p.getStop() + " : " + p.getLimit() + " : " + p.getPpp());
                                Log.d(TAG, "PRE-TRIG Sell: " + p.symbol + " : " + trigger + " : " + p.getStop() + " : " + p.getLimit() + " : " + p.getPpp());

                            }

                        }
                    }

                    db.updatePotential(p);

                    // Below IF needs cleaning up...what's that current stuff?


                    if (p.getTrigger() != 0 && p.direction.equals("BUY") && (currentInt != 0 || current != -1)) {
                        Log.d(TAG,p.getSymbol() + " : Now checking if we can trigger a Position, current: " + current + " and trigger: " + p.getTrigger());
                        if (p.current >= p.trigger && p.current < (p.trigger + (p.trigger * TRIGGER_BUFFER)) && p.current < p.getHigh() && isRising && balanced) {

//					checkStopLimit(stop, limit);
//					Log.d(TAG,"Buy: " + p.symbol +  " : " + current + " : " + stop + " : " + limit + " : " + ppp);
// 					newPosition = new Position(0, p.symbol, p.direction, stop, limit, ppp, p.current, 0, "Open", p.getTrigger());

//					Log.d(TAG,"Raised date is " + raisedDate);
                            newPosition = new Position(0, p.symbol, p.direction, p.getStop(), p.getLimit(), p.getPpp(), p.current, 0, "Open", p.current, raisedDate, 0);

                            db.addPosition(newPosition);
                            db.deletePotential(p);
                            Log.i(TAG, "Just deleted Potential from (from DB) as raising Position " + p.getSymbol() + ":" + p.getDaysActive());


                            double potential_profit = (p.getLimit() - current) * p.getPpp();
                            double potential_loss = (current - p.getStop()) * p.getPpp();

                            sendMessage(notificationCounter++, "Buy: " + p.symbol + " : " + current + " : " + p.getStop() + " : " + p.getLimit() + " : " + p.getPpp() + " : " + potential_loss + " : " + potential_profit);
                            Log.d(TAG, "Buy: " + p.symbol + " : " + current + " : " + p.getStop() + " : " + p.getLimit() + " : " + p.getPpp() + " : " + potential_loss + " : " + potential_profit);
//                            potentials.remove(i);

                        } else {
                            Log.d(TAG, "Didn't raise Position because balanced maybe = " + balanced + " or isRising " + isRising + " " + p.getSymbol());
                        }
                    } else if (p.getTrigger() != 1 && p.direction.equals("SELL") && (currentInt != 0 || current != -1)) {


                        // Change this so that trigger is inside Low/High when Potential set, or p(0)
                        if (p.current <= p.trigger && p.current > (p.trigger - (p.trigger * TRIGGER_BUFFER)) && p.current > p.getLow() && !isRising && balanced) {
//					Log.d(TAG,"Raising position: trigger = " + p.trigger + " and current is " + p.current);


//					newPosition = new Position(0, p.symbol, p.direction, stop, limit, ppp, p.current, 0, "Open", p.getTrigger());
                            newPosition = new Position(0, p.symbol, p.direction, p.getStop(), p.getLimit(), p.getPpp(), p.current, 0, "Open", p.current, raisedDate, 0);

                            db.addPosition(newPosition);
                            db.deletePotential(p);
                            Log.i(TAG, "Just deleted Potential from (DB) as raising Position " + p.getSymbol() + ":" + p.getDaysActive());

                            sendMessage(notificationCounter++, "Sell: " + p.symbol + " : " + current + " : " + p.getStop() + " : " + p.getLimit() + " : " + p.getPpp());
                            Log.d(TAG, "Sell: " + p.symbol + " : " + current + " : " + p.getStop() + " : " + p.getLimit() + " : " + p.getPpp());
//                            potentials.remove(i);

                        }
                    } else {
				            Log.i(TAG,p.getSymbol() + " Doing nothing, current: " + current + ", trigger: " + p.getTrigger() + ", currentInt: " + currentInt);
                    }
                    // Why here and not before for loop
                }
            }
        } else {
            Log.d(TAG, "No potentials to process");
        }
//		showPositions();

        db.setLastPotUpdateDate();
//		Log.d(TAG,"Ending checkPotentials");

//		Log.d(TAG,"Starting checkPositions");

    }

    private void showPositions() {

        List<Position> allPos = db.getAllPositions();
        Position p = new Position();

        for (int i = 0; i < allPos.size(); i++) {

            p = allPos.get(i);
            Log.d(TAG, "POS: symb: " + p.getSymbol());
            Log.d(TAG, "POS: date opened: " + p.getDateOpened());
            Log.d(TAG, "POS: triggered: " + p.getTriggered());
            Log.d(TAG, "POS: current: " + p.getCurrent());
            Log.d(TAG, "POS: ppp: " + p.getPPP());
            Log.d(TAG, "POS: profit: " + p.getProfit());
            Log.d(TAG, "POS: daysActive: " + p.getDaysActive());
            Log.d(TAG, "POS: limit: " + p.getLimit());
            Log.d(TAG, "POS: stop: " + p.getStop());
            Log.d(TAG, "POS: ------------------");

        }
    }

    private void showPotentials() {

        List<Potential> allPot = db.getAllPotentials();
        Potential p;

        for (int i = 0; i < allPot.size(); i++) {

            p = allPot.get(i);
            Log.d(TAG, "POT: " + p.getSymbol());
//            Log.d(TAG, "POT: stop: " + p.getStop());
//            Log.d(TAG, "POT: ------------------");

        }
        Log.d(TAG,"POT: " + allPot.size());
    }

    private String getDirection(ArrayList<Quote> quotes) {


        double diPlus = quotes.get(0).getDI14_PLUS();
        double diMinus = quotes.get(0).getDI14_MINUS();

        if (diPlus > diMinus) {
            return "BUY";
        } else {
            return "SELL";
        }

    }

    private double getPosQuote(Position p) {

        List<Quote> dbQuotes = db.getAllQuotesByTick(p.getSymbol());

//		Log.d(TAG,"Potential: " + p.getSymbol());

        Quote potQuote = dbQuotes.get(dbQuotes.size() - 1);

        return potQuote.getCLOSE();
    }

    private ArrayList<Quote> getAllPotQuotes(Potential p) {

        ArrayList<Quote> potQuotes = new ArrayList<Quote>();

        List<Quote> dbQuotes = db.getAllQuotesByTick(p.getSymbol());

//		Log.d(TAG,"Potential: " + p.getSymbol());

        String symb = p.getSymbol();
        int startId = p.getStartId();
        for (int y = 0; y < dbQuotes.size(); y++) {
            Quote thisQuote = dbQuotes.get(y);
            if (thisQuote.getSYMBOL().equals(symb) && thisQuote.getID() >= (startId)) {
                potQuotes.add(thisQuote);
                Log.d(TAG, "Adding potQuote " + thisQuote.getSYMBOL() + " for date " + thisQuote.getDATE() + " : CLOSE: " + thisQuote.getCLOSE() + " : ADX: " + thisQuote.getADX());
            }
        }

        if (potQuotes.size()==0) {
            Log.d(TAG,"WARNING, returning 0 quotes for " + p.getSymbol());
        }
        return potQuotes;
    }

//	public int getPotCount() {
//		db = new DatabaseHandler(thisContext);
//		int count = db.getPotentialsCount();
//		return count;
//	}

    private double getHigh(ArrayList<Quote> quotes) {
        double quotesHigh = 0;
        for (int k = 0; k < quotes.size(); k++) {
            Quote q = quotes.get(k);
            if (quotes.get(k).getHIGH() > quotesHigh) {
                quotesHigh = quotes.get(k).getHIGH();
//				Log.d(TAG,"Quotes for High. DATE: " + q.getDATE() + " HIGH: " + q.getHIGH() + " CLOSE: " + q.getCLOSE());
//				Log.d(TAG,quotes.get(k).getDATE() + " New high: " + k + " : " + quotesHigh);
            }

        }
//		Log.d(TAG,"Finished call and returned high: " + quotesHigh);
        return quotesHigh;
    }

    private double getLow(ArrayList<Quote> quotes) {
        double quotesHigh = 999999;

        for (int k = 0; k < quotes.size(); k++) {

            if (quotes.get(k).getLOW() < quotesHigh) {
                quotesHigh = quotes.get(k).getLOW();
            }

        }

        return quotesHigh;

    }

    private double setStop(ArrayList<Quote> quotes, String type, int startID) {

        double stop = 0;

//		db = new DatabaseHandler(thisContext);

        if (type.equals("BUY")) {
            stop = getLow(quotes);
        } else if (type.equals("SELL")) {
            stop = getHigh(quotes);
        } else {
            Log.e(TAG, "STOP NOT SET");
        }

        return stop;

    }

    private double setLimit(ArrayList<Quote> quotes, String type, int startID) {

        double limit = 0;

        db = new DatabaseHandler(thisContext);

        if (type.equals("BUY")) {
            limit = getHigh(quotes);
            limit = limit + (limit * LIMIT_BUFFER);
            Log.d(TAG, "POS: Setting limit : " + limit + " + " + limit + " * " + LIMIT_BUFFER);
        } else if (type.equals("SELL")) {
            limit = getLow(quotes);
            Log.d(TAG, "Limit set to: getLow: " + limit);
            limit = limit - (limit * LIMIT_BUFFER);
            Log.d(TAG, "Limit now set to: " + limit);
        } else {
            Log.e(TAG, "TRIGGER NOT SET");
        }

        return round(limit, 2);

    }

//	private double setTrigger(ArrayList<Quote> quotes, String dir) {
//		double trigger = 0;
//
//		db = new DatabaseHandler(thisContext);
//
//		Quote yesterdayQuote = quotes.get(quotes.size()-2);
//		int yesterdayID = yesterdayQuote.getID();
//
//		if ( dir.equals("BUY") ) {
//			trigger = db.getQuote(yesterdayID).getHIGH();
//		} else if ( dir.equals("SELL") ) {
//			trigger = db.getQuote(yesterdayID).getLOW();
//		} else {
//			Log.e(TAG,"TRIGGER NOT SET");
//		}
//		return trigger;
//	}

    private int setDaysActive(ArrayList<Quote> p) {
        int days = 0;

        days = p.size() - 1;

        return days;
    }

    private double setHotOrNot(ArrayList<Quote> p) {
        int counter = 0;

        double trigger = 0;

        double newHigh = p.get(0).getCLOSE();
        double newLow = p.get(0).getCLOSE();

//		double high = p.get(0).getCLOSE();
//		double low = p.get(0).getCLOSE();

        double high = getHigh(p);
        double low = getLow(p);
//		for (int i=0; i<p.size(); i++) {
//			Log.d(TAG,"Hot quotes: " + p.get(i).getDATE() + " HIGH: " + p.get(i).getHIGH() + " CLOSE: " + p.get(i).getCLOSE());
//		}

        String direction = getDirection(p);

        if (direction.equals("BUY")) {

            for (int i = 0; i < p.size() - 1; i++) {

//				double closeToday = p.get(i).getCLOSE();
                double closeTomorrow = p.get(i + 1).getCLOSE();
//                Log.d(TAG,p.get(i).getDATE() + " X1:Close today is " + p.get(i).getCLOSE() + " and Close tomorrow is " + p.get(i+1).getCLOSE());
//				if ( closeTomorrow > closeToday ) {
//					counter--;
//
//				}

                if (closeTomorrow < newLow) {
                    counter++;
//					Log.d(TAG,p.get(i).getDATE() + " Found a new Low tomorrow: " + closeTomorrow + " as todays Low is: " + newLow);
                    newLow = closeTomorrow;
                    if (counter == HOT) {
//                        Log.d(TAG,"Breaking out at " + p.get(i+1).getDATE() + " for " + p.get(i+1).getSYMBOL() + " : " + p.get(i+1).getCLOSE());
                        // Trigger should be HIGH - CLOSE / 2
						trigger = p.get(i+1).getCLOSE() + ((high - p.get(i+1).getCLOSE()) / 2);
//                        trigger = low + ((high - low) / 2);

                        Log.d(TAG, "Buy Trigger=" + p.get(i).getSYMBOL() + " : " + p.get(i + 1).getCLOSE() + " high - that / 2: " + " high: " + high + " and " + (high - p.get(i + 1).getCLOSE()) / 2);
//                        trigger = p.get(i+1).getHIGH();
//                        if ( trigger > p.get(i).getCLOSE() ) {
//                            trigger = p.get(i).getCLOSE();
//							Log.d(TAG,"Setting trigger to CLOSE price");
//                        } else {
//							Log.d(TAG,"Should never get here, how can getHIGH < getCLOSE?");
//						}
                        Log.d(TAG, "returning trigger: " + trigger);
                        return round(trigger, 2);
                    }
                }
            }
        } else if (direction.equals("SELL")) {

            for (int i = 0; i < p.size() - 1; i++) {

                double closeToday = p.get(i).getCLOSE();
                double closeTomorrow = p.get(i + 1).getCLOSE();


                if (closeTomorrow > newHigh) {
                    counter++;
//                    Log.d(TAG,p.get(i+1).getSYMBOL() + "newHigh " + newHigh + " : closeTomorrow : " + closeTomorrow + " : " + p.get(i+1).getDATE() + " : " + p.get(i+1).getDATE());
                    newHigh = closeTomorrow;
                    if (counter == HOT) {
//						Log.d(TAG,"Breaking out at " + p.get(i+1).getDATE() + " for " + p.get(i).getSYMBOL() + " : " + p.get(i+1).getCLOSE());
						trigger = p.get(i+1).getCLOSE() - ((p.get(i+1).getCLOSE() - low) / 2);
//                        trigger = low + ((high - low) / 2);

                        Log.d(TAG, "Sell trigger (close)= " + p.get(i).getSYMBOL() + " : " + p.get(i + 1).getCLOSE() + " : " + ((p.get(i + 1).getCLOSE() - low) / 2) + " : " + low + " : " + high);
                        Log.d(TAG, "Sell trigger (trigger): " + p.get(i).getSYMBOL() + " : " + trigger);
                        Log.d(TAG, "trigger=low + (high - low): " + low + " + " + high + " -  " + low);

//  trigger = p.get(i).getLOW();
// Do not understand this logic
//                        if ( trigger < p.get(0).getCLOSE() ) {
//                            trigger = p.get(0).getCLOSE();
//                        }

                        Log.d(TAG, "returning trigger: " + trigger);
                        return round(trigger, 2);
                    }
                }
//				else if ( closeTomorrow < closeToday ) {
//
//					counter--;
//
//				}
            }

        }

        return 0;
    }

    private int setPPP(double stop, double limit, double current, String symbol) {

        int ppp = 0;
        int stop_check = 0;
//
//		ppp = (int) (MARGIN/(close * 0.1));

        double profit_distance = limit - current;
        double loss_distance = limit - stop;

        Log.d(TAG, "limit_check, limit: " + limit);
        Log.d(TAG, "limit_check, stop: " + stop);

        Log.d(TAG, "limit_check, current: " + current);
        Log.d(TAG, "limit_check, profit_distance: " + profit_distance);

//		ppp = Math.round(MARGIN / profit_distance);
        ppp = (int) Math.round(MARGIN / loss_distance);


        if (stop_check > MARGIN) {
            Log.d(TAG, "WARNING: Stop Loss is over 200: " + stop_check + " : " + symbol);
            sendMessage(notificationCounter++, "WARNING: Stop Loss is over 200: " + stop_check + " : " + symbol);
        }

//		Log.d(TAG,"ppp: " + ppp);
        return Math.abs(ppp);
    }

    public void checkPositions() {

        List<Position> positions = db.getAllPositions();
        //   List<History> closedHist = db.getAllClosedHist();

        double current;

        if (positions.size() > 0) {
            for (int i = 0; i < positions.size(); i++) {

                Position p = positions.get(i);

                if (!thisTestRun) {
                    Pair quotePair = dob.getQuotes(p.getSymbol());
                    current = (Double) quotePair.first;
//					Log.d(TAG,"Current for NOT TESTRUN: " + p.getSymbol() + " is " + current);
                } else {
//					Log.d(TAG,"Current for TESTRUN: " + p.getSymbol() + " is " + current);
                    current = getPosQuote(p);
                }

//				current = dob.getQuotes(p.getSymbol());
                double profit = 0;
                p.setDaysActive(posGetDaysActive(p));

                p.setCurrent(current);
                if (p.getDirection().equals("BUY")) {
                    profit = (current - p.getTriggered()) * p.getPPP();
                } else {
                    profit = (p.getTriggered() - current) * p.getPPP();
                }

                Log.d(TAG, "POS: proffit = current - triggerd = " + p.getSymbol() + " : " + current + " - " + p.getTriggered() + " ppp : " + p.getPPP() + " daysActive: " + p.getDaysActive());
                Log.d(TAG, "POS: Limit: " + p.getLimit());
                Log.d(TAG, "POS: Dir: " + p.getDirection());

                p.setProfit(round(profit, 2));

                if (p.getDaysActive() > (2 * MAX_DAYS_ACTIVE)) {
                    sendMessage(notificationCounter++, "Closing expired position: " + p.symbol + " : " + p.getProfit());
                    Log.d(TAG, "Tracking: Closing expired position: " + p.symbol + " on date " + p.getDaysActive() + " from Position open" + " close " + p.getCurrent());
                    db.addClosedHist(p);
                    db.deletePosition(p.getSymbol());

                } else {

                    if (current != 0) {
//						Log.d(TAG,"Did we get here?");
                        if (p.getDirection().equals("BUY") && !p.getStatus().equals("CLOSED")) {
//							Log.d(TAG,"Did we get here2?");
                            Log.d(TAG, "POS: On date: " + p.getDateOpened() + " : " + p.getDaysActive() + " limit is " + p.getLimit() + " and current is " + current);
                            if (current > p.getLimit()) {
//								Log.d(TAG,"Did we get here3?");
                                p.setStatus("CLOSED");
                                Log.d(TAG, "Tracking: callng addClosedHist with daysActive for Position = " + p.getDaysActive() + " Limit: " + p.getLimit() + " and current: " + p.getCurrent() + " and PPP: " + p.getPPP());
                                db.addClosedHist(p);
                                db.deletePosition(p.getSymbol());
//								Log.i(TAG, "CLOSE: " + p.symbol + " : " + " LIMIT: " + p.getLimit());
                                sendMessage(notificationCounter++, "Buy Close Profit: " + p.symbol + " : LIMIT: " + p.getLimit() + " : CURRENT: " + current);
                            } else
//								Log.d(TAG, "CLOSING: p.getStop() = " + p.getStop() + " current = " + current);
                                if (current < p.getStop() && !p.getStatus().equals("CLOSED")) {
//							profit = current - p.getTriggered();
//							p.setProfit(profit);
                                    p.setStatus("CLOSED");
//								Log.d(TAG,"calling addClosedHist1");
                                    Log.d(TAG, "Tracking: callng addClosedHist with daysActive for Position = " + p.getDaysActive() + " Stop: " + p.getStop() + " and current: " + p.getCurrent() + " and PPP: " + p.getPPP());
                                    db.addClosedHist(p);
                                    db.deletePosition(p.getSymbol());
                                    sendMessage(notificationCounter++, "Buy Close Loss: " + p.symbol + " : STOP: " + p.getStop() + " : CURRENT: " + current);
                                } else {
//								Log.e(TAG, "Not closing Position");
//							profit = current - p.getTriggered();
//							Log.d(TAG, "Profit: Current - Triggered = " + current + " - " + p.getTriggered());

//							p.setProfit(round(profit, 2));
                                    db.updatePosition(p);

//							Log.d(TAG, "profit set to (current - triggered): " + current + " - " + p.getTriggered() + " = " + p.profit);
                                }
                        } else if (p.getDirection().equals("SELL") && !p.getStatus().equals("CLOSED")) {

                            if (current < p.getLimit()) {

                                p.setStatus("CLOSED");
                                Log.d(TAG, "called addClosedHist2 as current: " + current + " is < limit: " + p.getLimit());
                                Log.d(TAG, "called addClosedHist2 as current: " + current + " profit: " + p.getProfit());
                                Log.d(TAG, "called addClosedHist2 as current: " + current + " opened: " + p.getDateOpened());
                                Log.d(TAG, "called addClosedHist2 as current: " + current + " triggered: " + p.getTriggered());


                                db.addClosedHist(p);
                                db.deletePosition(p.getSymbol());
                                sendMessage(notificationCounter++, "Sell Close Profit: " + p.symbol + " : " + p.getLimit() + " : " + current);
                            } else if (current > p.getStop() && !p.getStatus().equals("CLOSED")) {
//							profit = p.getLimit() - p.getTriggered();
//							p.setProfit(profit);
                                p.setStatus("CLOSED");
                                db.addClosedHist(p);
                                db.deletePosition(p.getSymbol());
                                Log.d(TAG, "Buying a SELL at a loss: " + p.getSymbol() + " current: " + current + " and p.getStop() " + p.getStop() + " date: " + p.getDaysActive());
                                sendMessage(notificationCounter++, "Sell Close Loss: " + p.symbol + " : " + p.getStop() + " : " + current);

                            } else {
//								Log.e(TAG, "Not closing Position");
//							profit = current - p.getTriggered();
//							Log.d(TAG, "Profit: Current - Triggered = " + current + " - " + p.getTriggered());
//
//							p.setProfit(round(profit, 2));
//								db.updatePosition(p);

//							Log.d(TAG, "profit set to (current - triggered): " + current + " - " + p.getTriggered() + " = " + p.profit);
                            }
                        }
                    }
                    db.updatePosition(p);
//					Log.d(TAG, "NXT: should have just updated DB with " + p.getSymbol() + " and current " + p.getCurrent() + " and daysActive " + p.getDaysActive());
//					showPositions();
                }
            }
        }
    }

    public double round(double d, int decimalPlace) {

        double newD = (double) (Math.round(d * 100.0) / 100.0);

        return newD;
    }

    private void sendMessage(int id, String message) {

//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(ALERT_NUMBER, null, message, null, null);
//
//        Vibrator v = (Vibrator) thisContext.getSystemService(Context.VIBRATOR_SERVICE);
//        // Vibrate for 500 milliseconds
//        v.vibrate(500);

        // Send Notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(thisContext);

        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("ADXcite");
        mBuilder.setContentText(message);

        NotificationManager mNotificationManager = (NotificationManager) thisContext.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = id;
        mNotificationManager.notify(notificationID, mBuilder.build());
//
//        Toast.makeText(thisContext.getApplicationContext(), message,
//                Toast.LENGTH_LONG).show();
//        Log.d(TAG,"Did we sendMessage?");
    }

    private String getTodaysDate() {

        Date date;

        Calendar cal = Calendar.getInstance();

        date = cal.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // (3) create a new String using the date format we want
        String formattedDate = formatter.format(date);

        return formattedDate;

    }

    private void openWhatsApp(String id) {

        Cursor c = thisContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Contacts.Data._ID}, ContactsContract.Data.DATA1 + "=?",
                new String[]{id}, null);
        c.moveToFirst();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.contacts/data/" + c.getString(0)));

        thisContext.startActivity(i);
//		Log.d(TAG,"startActivity called");
        c.close();
    }

    private int posGetDaysActive(Position p) {
        long daysActiveLong = 0;

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inputString1 = p.getDateOpened();

        String inputString2 = db.getMaxQuoteDate(p.getSymbol());

//		Log.d(TAG,"Parse inputString1: dateOpened: " + inputString1);
//		Log.d(TAG,"Parse inputString2: maxDBDate: " + inputString2);

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            long diff = date2.getTime() - date1.getTime();
            daysActiveLong = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//			Log.d(TAG, "Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
//			Log.d(TAG, "date error: " + e.getMessage());
        }
//		Log.d(TAG,"Returning: " + (int)daysActiveLong);
        return (int) daysActiveLong;
    }

    private int getOpenBuys() {

        int openBuys = 0;

        List<Position> allPos = db.getAllPositions();

        for (int i = 0; i < allPos.size(); i++) {
            Position p = allPos.get(i);
            if (p.getDirection().equals("BUY")) {
                openBuys++;
            }
        }
        Log.d(TAG, "openBuys: " + openBuys);
        return openBuys;

    }

    private int getOpenSells() {

        int openSells = 0;

        List<Position> allPos = db.getAllPositions();

        for (int i = 0; i < allPos.size(); i++) {
            Position p = allPos.get(i);
            if (p.getDirection().equals("SELL")) {
                openSells++;
            }
        }
        Log.d(TAG, "openSells: " + openSells);

        return openSells;

    }

}
