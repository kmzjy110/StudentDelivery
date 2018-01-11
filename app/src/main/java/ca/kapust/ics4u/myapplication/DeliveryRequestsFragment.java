package ca.kapust.ics4u.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kmzwg on 1/11/2018.
 */

public class DeliveryRequestsFragment extends Fragment {
    public static DeliveryRequestsFragment newInstance() {
        DeliveryRequestsFragment fragment = new DeliveryRequestsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.delivery_requests_fragment, container, false);
    }
}
