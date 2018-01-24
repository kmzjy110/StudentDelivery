package ca.kapust.ics4u.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
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



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.be_a_deliverer_fragment, container, false);

        Switch switch1 = (Switch)relativeLayout.findViewById(R.id.switch1);
        //set listener for the switch
        Switch.OnCheckedChangeListener listener = new Switch.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(!isChecked){
                    NetworkHelper.hell.destroy();
                    NetworkHelper.hell=null;
                }
                if(isChecked){
                    //launch the service if it has not been launched
                    if(!MainActivity.executedService){
                       MainActivity.executedService=true;
                       MainActivity.isDeliverer=isChecked;
                    Intent communication_service_intent = new Intent(getActivity(), CommunicationIntentService.class);

                    getActivity().startService(communication_service_intent);}
                }

            }

        };

        switch1.setOnCheckedChangeListener(listener);

        return relativeLayout;
    }


}
