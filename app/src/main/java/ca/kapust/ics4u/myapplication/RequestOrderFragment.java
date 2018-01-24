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
                String ordererLocation= ((TextView)layout.findViewById(R.id.locationText)).getText().toString();
                String restaurantLocation=((TextView)layout.findViewById(R.id.restLocText)).getText().toString();
                String restaurantName=((TextView)layout.findViewById(R.id.restNameText)).getText().toString();
                String order=((TextView)layout.findViewById(R.id.orderText)).getText().toString();
                String cost=((TextView)layout.findViewById(R.id.costText)).getText().toString();
                String tip=((TextView)layout.findViewById(R.id.tipText)).getText().toString();


                //socket connection
                if(!MainActivity.executedService){

                    Intent communication_service_intent = new Intent(getActivity(), CommunicationIntentService.class);
                    communication_service_intent.putExtra(CommunicationIntentService.USER_LOCATION_INDICATOR,ordererLocation);
                    communication_service_intent.putExtra(CommunicationIntentService.RESTAURANT_LOCATION_INDICATOR,restaurantLocation);
                    communication_service_intent.putExtra(CommunicationIntentService.RESTAURANT_NAME_INDICATOR,restaurantName);
                    communication_service_intent.putExtra(CommunicationIntentService.ORDER_INDICATOR,order);
                    communication_service_intent.putExtra(CommunicationIntentService.COST_INDICATOR,cost);
                    communication_service_intent.putExtra(CommunicationIntentService.TIP_INDICATOR,tip);

                    getActivity().startService(communication_service_intent);
                }
            }
        });
        return layout;
    }
}
