package ca.kapust.ics4u.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

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
        try{
            //Establish Socket Connection
            NetworkHelper hell;
            if(MainActivity.isDeliverer){
                    hell = new NetworkHelper();
                    //TODO: GET PHONE NUMBER OF THE PERSON MAKING THE REQUEST
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(ResponseReceiver.ACTION);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent.putExtra(MainActivity.ACTION_INDICATOR,MainActivity.DELIVERY_RECEIVED_ACTION);
                    broadcastIntent.putExtra(ID_INDICATOR,"GUIDGUIDGUID");
                    broadcastIntent.putExtra(NAME_INDICATOR, "Harry Zhao");
                    broadcastIntent.putExtra(USER_LOCATION_INDICATOR, "505 Pickering Crescent");
                    broadcastIntent.putExtra(RESTAURANT_NAME_INDICATOR, "Golden Dragon");
                    broadcastIntent.putExtra(RESTAURANT_LOCATION_INDICATOR, "504 Pickering Crescent");
                    broadcastIntent.putExtra(ORDER_INDICATOR, "Hunan Beef");
                    broadcastIntent.putExtra(COST_INDICATOR, "$10");
                    broadcastIntent.putExtra(TIP_INDICATOR, "$1");
                    broadcastIntent.putExtra(PHONE_NUMBER_INDICATOR,"4165555555");
                    //

                    sendBroadcast(broadcastIntent);

                }else{
                    hell = new NetworkHelper();
                }




            if(true){//order taken

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(ResponseReceiver.ACTION);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(MainActivity.ACTION_INDICATOR,MainActivity.DELIVERY_ACCEPTED_ACTION);
                //TODO:get the phone number and name of the person taking the request and push that into notification
            }

        }catch(Exception e){

        }


    }



}
