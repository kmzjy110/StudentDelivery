package ca.kapust.ics4u.myapplication;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kmzwg on 1/11/2018.
 */

public class BeADelivererFragment extends Fragment {
    public static BeADelivererFragment newInstance() {
        BeADelivererFragment fragment = new BeADelivererFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.be_a_deliverer_fragment, container, false);
    }
}
