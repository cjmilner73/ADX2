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
public class TrackingFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "TrackingFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List trackingList; // at the top of your fragment list

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
    public static TrackingFragment newInstance(String param1, String param2) {
        TrackingFragment fragment = new TrackingFragment();
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
    public TrackingFragment() {
        Log.d(TAG,"Tracking");
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHandler(this.getActivity().getApplicationContext());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<Potential> allPots;
        allPots = db.getAllPotentials();



//        TrackingListAdapter.notifyDataSetChanged();
        trackingList = new ArrayList();
//        exampleListItemList.add(new ExampleListItem("Example 1"));
//        exampleListItemList.add(new ExampleListItem("Example 2"));
//        exampleListItemList.add(new ExampleListItem("Example 3"));
        for (int i=0; i<allPots.size(); i++) {

            String symbol = allPots.get(i).getSymbol();
            String direction = allPots.get(i).getDirection();
            double latestPrice = allPots.get(i).getCurrent();
            double trigger = allPots.get(i).getTrigger();
            int daysActive = allPots.get(i).getDaysActive();
            trackingList.add(new TrackingListItem(symbol, direction, latestPrice, trigger, daysActive));
        }



        mAdapter = new TrackingListAdapter(getActivity(), trackingList);



        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TrackingListItem item = (TrackingListItem) this.trackingList.get(position);
        Toast.makeText(getActivity(), item.getTrackingSymbol() + " Clicked!"
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

        List<Potential> allPots;
        allPots = db.getAllPotentials();

//        TrackingListAdapter.notifyDataSetChanged();
        trackingList = new ArrayList();
//        exampleListItemList.add(new ExampleListItem("Example 1"));
//        exampleListItemList.add(new ExampleListItem("Example 2"));
//        exampleListItemList.add(new ExampleListItem("Example 3"));
        for (int i=0; i<allPots.size(); i++) {

            String symbol = allPots.get(i).getSymbol();
            String direction = allPots.get(i).getDirection();
            double latestPrice = allPots.get(i).getCurrent();
            double trigger = allPots.get(i).getTrigger();
            int daysActive = allPots.get(i).getDaysActive();
            trackingList.add(new TrackingListItem(symbol, direction, latestPrice, trigger, daysActive));
        }



        mAdapter = new TrackingListAdapter(getActivity(), trackingList);



//        TextView textView = new TextView(context);
//        textView.setText("Hello. I'm a header view");
//
//        mListView.addHeaderView(textView);

        // Set the adapter
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

    }

}
