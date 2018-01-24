package ca.kapust.ics4u.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kmzwg on 1/15/2018.
 */

public class CommunicationIntentService extends IntentService {
    public static final String NAME_INDICATOR= "name";
    public static final String USER_LOCATION_INDICATOR = "user location";
    public static final String RESTAURANT_NAME_INDICATOR ="restaurant name";
    public static final String RESTAURANT_LOCATION_INDICATOR="restaurant location";
    public static final String ORDER_INDICATOR = "order";
    public static final String COST_INDICATOR="cost";
    public static final String TIP_INDICATOR = "tip";
    public static final String PHONE_NUMBER_INDICATOR="phone number";
    public static final String ID_INDICATOR="id";
    public CommunicationIntentService(){
        this(CommunicationIntentService.class.getName());

    }
    public CommunicationIntentService(String name){
        super(name);
    }
    @Override
    protected void onHandleIntent(Intent intent) {

            if (MainActivity.isDeliverer) {
                NetworkHelper.hell = new NetworkHelper(this);
                //TODO: GET PHONE NUMBER OF THE PERSON MAKING THE REQUEST

            } else {
                String restaurantLocation = intent.getStringExtra(CommunicationIntentService.RESTAURANT_LOCATION_INDICATOR);
                String ordererLocation = intent.getStringExtra(CommunicationIntentService.USER_LOCATION_INDICATOR);
                String restaurantName = intent.getStringExtra(CommunicationIntentService.RESTAURANT_NAME_INDICATOR);
                String order = intent.getStringExtra(CommunicationIntentService.ORDER_INDICATOR);
                String cost = intent.getStringExtra(CommunicationIntentService.COST_INDICATOR);
                String tip = intent.getStringExtra(CommunicationIntentService.TIP_INDICATOR);
                NetworkHelper.hell = new NetworkHelper(this, ordererLocation, restaurantLocation, restaurantName, order, cost, tip);
            }


    }
    public void createDeli(JSONObject data) {
        try {

            //put all the data in the intent and send the data to the receiver
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(ResponseReceiver.ACTION);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(MainActivity.ACTION_INDICATOR, MainActivity.DELIVERY_RECEIVED_ACTION);
            broadcastIntent.putExtra(ID_INDICATOR, data.getString("orderId"));
            broadcastIntent.putExtra(NAME_INDICATOR, data.getString("requesterFName")+" "+data.getString("requesterLName"));
            broadcastIntent.putExtra(USER_LOCATION_INDICATOR, data.getString("ordererLocation"));
            broadcastIntent.putExtra(RESTAURANT_NAME_INDICATOR, data.getString("restaurantName"));
            broadcastIntent.putExtra(RESTAURANT_LOCATION_INDICATOR, data.getString("restaurantLocation"));
            broadcastIntent.putExtra(ORDER_INDICATOR, data.getString("order"));
            broadcastIntent.putExtra(COST_INDICATOR, data.getString("cost"));
            broadcastIntent.putExtra(TIP_INDICATOR, data.getString("tip"));
            broadcastIntent.putExtra(PHONE_NUMBER_INDICATOR, data.getString("phoneNumber"));
            sendBroadcast(broadcastIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
