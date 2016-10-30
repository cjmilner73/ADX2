package sds.com.adx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PullbacksFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG", "PullbacksFragment updated");


        View rootView = inflater.inflate(R.layout.fragment_tracking, container, false);

        return rootView;
    }

}
