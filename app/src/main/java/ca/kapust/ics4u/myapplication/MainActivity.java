package ca.kapust.ics4u.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ca.kapust.ics4u.myapplication.BeADelivererFragment;
import ca.kapust.ics4u.myapplication.DeliveryRequestsFragment;
import ca.kapust.ics4u.myapplication.R;
import ca.kapust.ics4u.myapplication.RequestOrderFragment;

public class MainActivity extends AppCompatActivity {
    private ResponseReceiver receiver;
    public static final String CURRENT_USER_INDICATOR = "current_user";

    public static final String ACTION_INDICATOR = "Action";
    public static int currentUser;
    public  RequestOrderFragment requestOrderFragment = RequestOrderFragment.newInstance();
    public  BeADelivererFragment beADelivererFragment = BeADelivererFragment.newInstance();
    public  DeliveryRequestsFragment deliveryRequestsFragment = DeliveryRequestsFragment.newInstance();
    public static boolean executedService=false;
    public static boolean isDeliverer=false;
    public static final String DELIVERY_ACCEPTED_ACTION = "Delivery Accepted";
    public static final String DELIVERY_RECEIVED_ACTION = "Delivery Received";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        //register broadcast receiver for the background location update service
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, requestOrderFragment);
        transaction.commit();




        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                selectedFragment = requestOrderFragment;
                                break;
                            case R.id.navigation_dashboard:
                                selectedFragment = beADelivererFragment;
                                break;
                            case R.id.navigation_notifications:
                                selectedFragment = deliveryRequestsFragment;
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });



    }
    public void sendNotification(String action) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        //Create the intent that’ll fire when the user taps the notification//
/*
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(pendingIntent);
*/
        mBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        if(action.equals(DELIVERY_ACCEPTED_ACTION)){

            mBuilder.setContentTitle("Your delivery request have been accepted");

        }else{

            mBuilder.setContentTitle("You have received a delivery order.");

        }
        mBuilder.setContentText("Enter app for details.");


        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());

    }
}