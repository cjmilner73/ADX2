package sds.com.adx.adapter;

import sds.com.adx.SummaryFragment;
import sds.com.adx.TrackingFragment;
import sds.com.adx.PositionFragment;
import sds.com.adx.HistoryFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Summary fragment activity
                 return new SummaryFragment();

            case 1:
                // Pullbacks fragment activity
//                return new PullbacksFragment();
                  return new TrackingFragment();

            case 2:
                // Positions fragment activity
                return new PositionFragment();

            case 3:
                // History fragment activity
                return new HistoryFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }

}