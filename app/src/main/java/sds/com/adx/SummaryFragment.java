package sds.com.adx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class SummaryFragment extends Fragment {

    private static final String TAG = "SummaryFragment";

    private TextView openMarketsText;
    private String lastPotUpdatedDate;
    private TextView lastUpdatedText;

    private String openMarkets;
    private String noPotentials;
    private String openPositions;
    private String profitAndLoss;

    TextView securitiesTrackedText;
    TextView openPositionsText;
    TextView profitAndLossText;

    static DatabaseHandler db;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        db = StartIntervalCheck.getDB();

        lastPotUpdatedDate = db.getLastPotUpdate();
        openMarkets = getOpenMarkets();
        noPotentials = Integer.toString(db.getPotentialsCount());
        openPositions = Integer.toString(db.getPositionsCount());
        profitAndLoss = getProfit();

        rootView = inflater.inflate(R.layout.fragment_summary, container, false);

        lastUpdatedText = (TextView)rootView.findViewById(R.id.last_updated_v);
        openMarketsText = (TextView)rootView.findViewById(R.id.open_markets_v);
        securitiesTrackedText = (TextView)rootView.findViewById(R.id.securities_tracked_v);
        openPositionsText = (TextView)rootView.findViewById(R.id.open_positions_v);
        profitAndLossText = (TextView)rootView.findViewById(R.id.profit_and_loss_v);

        lastUpdatedText.setText(lastPotUpdatedDate);
//        openMarketsText.setText(openMarkets);
//        openMarketsText.setText(String.valueOf(i1));

        securitiesTrackedText.setText(noPotentials);
        openPositionsText.setText(openPositions);
        profitAndLossText.setText(profitAndLoss);
        openMarketsText.setText(openMarkets);

        return rootView;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    public void updateFragmentData() {

        db = StartIntervalCheck.getDB();

        if (db == null) {
            Log.d(TAG, "db is null!");
        } else {

//            lastPotUpdatedDate = db.getLastPotUpdate();
            lastPotUpdatedDate = db.getMaxQuoteDate("MS");
            openMarkets = String.valueOf(0);
            noPotentials = Integer.toString(db.getPotentialsCount());
            openPositions = Integer.toString(db.getPositionsCount());
            profitAndLoss = "0";
            if (rootView != null) {


                openMarketsText = (TextView) rootView.findViewById(R.id.open_markets_v);
                securitiesTrackedText = (TextView) rootView.findViewById(R.id.securities_tracked_v);
                openPositionsText = (TextView) rootView.findViewById(R.id.open_positions_v);
                profitAndLossText = (TextView) rootView.findViewById(R.id.profit_and_loss_v);

                lastUpdatedText.setText(lastPotUpdatedDate);
//        openMarketsText.setText(openMarkets);
//        openMarketsText.setText(String.valueOf(i1));

                securitiesTrackedText.setText(noPotentials);
                openPositionsText.setText(openPositions);
                profitAndLossText.setText(getProfit());
            } else {
                Log.e(TAG, "rootView is null, gonna fail");

            }
        }
    }

    private String getOpenMarkets() {
        int openMarkets = 0;

        int NYSE_open = (21*60) + 30;
        int NYSE_close = (5*60);
        int FTSE_open = (16*60) + 30;
        int FTSE_close = (23*60) + 30;


        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        int min = calendar.get(Calendar.MINUTE);        // gets hour in 12h format

        int minsSinceMidnight = (hour*60) + min;

        if (minsSinceMidnight > NYSE_open && minsSinceMidnight < NYSE_close) {
            openMarkets++;
        }
        if (minsSinceMidnight > FTSE_open && minsSinceMidnight < FTSE_close) {
            openMarkets++;
        }

        return String.valueOf(openMarkets);
    }

    private String getProfit() {

        String profit = "";

        int profitInt = 0;

        List<Position> positions = db.getAllPositions();
        List<History> histories = db.getAllClosedHist();

        for (int i=0; i<histories.size(); i++) {
            History p = histories.get(i);
            profitInt += p.getProfit();
        }

//        for (int i=0; i<positions.size(); i++) {
//            Position p = positions.get(i);
//            profitInt += p.getProfit();
//        }

        return String.valueOf(profitInt);
    }

}