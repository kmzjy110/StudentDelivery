package ca.kapust.ics4u.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
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

        Switch switch1 = (Switch)getActivity().findViewById(R.id.switch1);


        switch1.isChecked();

        Switch.OnCheckedChangeListener listener = new Switch.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                // TODO Auto-generated method stub

            }

        };

        switch1.setOnCheckedChangeListener(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.be_a_deliverer_fragment, container, false);
    }
}
