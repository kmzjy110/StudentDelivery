package ca.kapust.ics4u.myapplication;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.UUID;

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
        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.request_order_fragment, container, false);

        Button requestButton = (Button)layout.findViewById(R.id.submit_button);
        if(MainActivity.isDeliverer){
            requestButton.setText("You cannot request orders when you are a deliverer.");
            return layout;
        }
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ordererName=(String)((TextView)layout.findViewById(R.id.nameText)).getText();
                String ordererLocation=(String)((TextView)layout.findViewById(R.id.locationText)).getText();
                String restaurantLocation=(String)((TextView)layout.findViewById(R.id.restLocText)).getText();
                String restaurantName=(String)((TextView)layout.findViewById(R.id.restNameText)).getText();
                String order=(String)((TextView)layout.findViewById(R.id.orderText)).getText();
                String cost=(String)((TextView)layout.findViewById(R.id.costText)).getText();
                String tip=(String)((TextView)layout.findViewById(R.id.tipText)).getText();

                //generate GUID for order

                //socket connection
                if(!MainActivity.executedService){

                    Intent communication_service_intent = new Intent(getActivity(), CommunicationIntentService.class);
                    getActivity().startService(communication_service_intent);
                }
            }
        });
        return layout;
    }
}
