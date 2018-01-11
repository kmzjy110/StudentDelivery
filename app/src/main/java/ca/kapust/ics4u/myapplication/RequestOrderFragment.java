package ca.kapust.ics4u.myapplication;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kmzwg on 1/11/2018.
 */

public class RequestOrderFragment extends Fragment {
    public static RequestOrderFragment newInstance() {
        RequestOrderFragment fragment = new RequestOrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.request_order_fragment, container, false);
    }
}
