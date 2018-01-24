package ca.kapust.ics4u.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import static ca.kapust.ics4u.myapplication.CommunicationIntentService.COST_INDICATOR;
import static ca.kapust.ics4u.myapplication.CommunicationIntentService.ID_INDICATOR;
import static ca.kapust.ics4u.myapplication.CommunicationIntentService.NAME_INDICATOR;
import static ca.kapust.ics4u.myapplication.CommunicationIntentService.ORDER_INDICATOR;
import static ca.kapust.ics4u.myapplication.CommunicationIntentService.PHONE_NUMBER_INDICATOR;
import static ca.kapust.ics4u.myapplication.CommunicationIntentService.RESTAURANT_LOCATION_INDICATOR;
import static ca.kapust.ics4u.myapplication.CommunicationIntentService.RESTAURANT_NAME_INDICATOR;
import static ca.kapust.ics4u.myapplication.CommunicationIntentService.TIP_INDICATOR;
import static ca.kapust.ics4u.myapplication.CommunicationIntentService.USER_LOCATION_INDICATOR;


/**
 * Created by kmzwg on 1/16/2018.
 */

public class ResponseReceiver extends BroadcastReceiver {
    public static final String ACTION= "order received";
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            //when data is received
            MainActivity current = (MainActivity) context;
            View v = current.deliveryRequestsFragment.mView;
            if(intent.getStringExtra(MainActivity.ACTION_INDICATOR).equals(MainActivity.DELIVERY_RECEIVED_ACTION)){
                String s=  "Name: "+intent.getStringExtra(NAME_INDICATOR);
                s+="\n";
                s+="Customer Location: "+intent.getStringExtra(USER_LOCATION_INDICATOR);
                s+="\n";
                s+="Restaurant Name: "+intent.getStringExtra(RESTAURANT_NAME_INDICATOR);
                s+="\n";
                s+="Restaurant Location: "+intent.getStringExtra(RESTAURANT_LOCATION_INDICATOR);
                s+="\n";
                s+="Order: "+intent.getStringExtra(ORDER_INDICATOR);
                s+="\n";
                s+="Cost of Order: "+intent.getStringExtra(COST_INDICATOR);
                s+="\n";
                s+="Tip:  "+intent.getStringExtra(TIP_INDICATOR);
                s+="\n";
                s+="Phone Number:  "+intent.getStringExtra(PHONE_NUMBER_INDICATOR);
                s+="\n";

                //update the data in all the delivery requests
                current.deliveryRequestsFragment.updateData(intent.getStringExtra(ID_INDICATOR),s);
                //send notification
                current.sendNotification(MainActivity.DELIVERY_RECEIVED_ACTION, "Enter app for details");

            }else{
                //if it's not receiving a delivery, it's that a delivery has been accepted
                current.sendNotification(MainActivity.DELIVERY_ACCEPTED_ACTION,intent.getStringExtra("Details"));


            }

        }catch(Exception e){//when restrictedReceiverContext comes in and the type cast has errors, just ignore.
        }
    }
}
