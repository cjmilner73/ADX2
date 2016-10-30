package sds.com.adx;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */


public class HistoryFragment extends Fragment implements AbsListView.OnItemClickListener {


    private final String TAG = "HistoryFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List historyList; // at the top of your fragment list

    static DatabaseHandler db;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistoryFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db = new DatabaseHandler(this.getActivity().getApplicationContext());

        List<History> allHis;
        allHis = db.getAllClosedHist();



        historyList = new ArrayList();
//        exampleListItemList.add(new ExampleListItem("Example 1"));
//        exampleListItemList.add(new ExampleListItem("Example 2"));
//        exampleListItemList.add(new ExampleListItem("Example 3"));
        for (int i=0; i<allHis.size(); i++) {
            String symbol = allHis.get(i).getSymbol();
            String direction = allHis.get(i).getDirection();
            double triggered = allHis.get(i).getTriggered();
            double profit = allHis.get(i).getProfit();
            String dateOpened = allHis.get(i).getDateOpened();
            String dateClosed = allHis.get(i).getDateClosed();
            historyList.add(new HistoryListItem(symbol, direction, triggered, profit, dateOpened, dateClosed));
        }
        mAdapter = new HistoryListAdapter(getActivity(), historyList);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<History> allHis;
        allHis = db.getAllClosedHist();

        historyList = new ArrayList();
//        exampleListItemList.add(new ExampleListItem("Example 1"));
//        exampleListItemList.add(new ExampleListItem("Example 2"));
//        exampleListItemList.add(new ExampleListItem("Example 3"));
        for (int i=0; i<allHis.size(); i++) {
            String symbol = allHis.get(i).getSymbol();
            String direction = allHis.get(i).getDirection();
            double triggered = allHis.get(i).getTriggered();
            double profit = allHis.get(i).getProfit();
            String dateOpened = allHis.get(i).getDateOpened();
            String dateClosed = allHis.get(i).getDateClosed();
            historyList.add(new HistoryListItem(symbol, direction, triggered, profit, dateOpened, dateClosed));
//            Log.d(TAG,"History: " + symbol + " : " + direction + " : " + triggered + " : " + profit + " : " + dateOpened + " : " + dateClosed);
        }
        mAdapter = new HistoryListAdapter(getActivity(), historyList);

        View view = inflater.inflate(R.layout.fragment_history, container, false);

//        TextView textView = new TextView(context);
//        textView.setText("Hello. I'm a header view");
//
//        mListView.addHeaderView(textView);
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int history, long id) {
        HistoryListItem item = (HistoryListItem) this.historyList.get(history);
        Toast.makeText(getActivity(), item.getHistorySymbol() + " Clicked!"
                , Toast.LENGTH_SHORT).show();
    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    public void updateFragmentData() {

        List<History> allHis;
        allHis = db.getAllClosedHist();

        historyList = new ArrayList();
//        exampleListItemList.add(new ExampleListItem("Example 1"));
//        exampleListItemList.add(new ExampleListItem("Example 2"));
//        exampleListItemList.add(new ExampleListItem("Example 3"));
        for (int i=0; i<allHis.size(); i++) {
            String symbol = allHis.get(i).getSymbol();
            String direction = allHis.get(i).getDirection();
            double triggered = allHis.get(i).getTriggered();
            double profit = allHis.get(i).getProfit();
            String dateOpened = allHis.get(i).getDateOpened();
            String dateClosed = allHis.get(i).getDateClosed();
            historyList.add(new HistoryListItem(symbol, direction, triggered, profit, dateOpened, dateClosed));
        }

        mAdapter = new HistoryListAdapter(getActivity(), historyList);

        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

    }

}

