package sds.com.adx;

import sds.com.adx.adapter.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import static android.widget.Toast.*;


public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener, TrackingFragment.OnFragmentInteractionListener, PositionFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener {

    private final String TAG = "MainActivity";

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    private String[] tabs = { "SUMMARY", "TRACKING", "POSITIONS", "HISTORY" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


        new StartIntervalCheck(this);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
        int position = tab.getPosition();

        List<Fragment> allFragments = getSupportFragmentManager().getFragments();

        if (allFragments != null) {
            if (position == 0) {
                SummaryFragment f = (SummaryFragment) allFragments.get(0);
                f.updateFragmentData();
            }
            if (position == 1) {
                TrackingFragment f = (TrackingFragment) allFragments.get(1);
                f.updateFragmentData();
            }
            if (position == 2) {
                PositionFragment f = (PositionFragment) allFragments.get(2);
                f.updateFragmentData();
            }
            if (position == 3) {
                HistoryFragment f = (HistoryFragment) allFragments.get(3);
                f.updateFragmentData();
            }
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onFragmentInteraction(String id) {

    }


}
