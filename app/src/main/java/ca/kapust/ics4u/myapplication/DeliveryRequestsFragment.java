package ca.kapust.ics4u.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by kmzwg on 1/11/2018.
 */

public class DeliveryRequestsFragment extends Fragment {
    public ArrayList<String> data = new ArrayList<String>();
    public ArrayList<String> ids= new ArrayList<String>();
    int minIndex=1000;//avoid conflict with android system UI id
    int maxIndex=1000;
    protected View mView;
    public final static int acceptButtonComputationFactor = 1000;
    public final static int declineButtonComputationFactor=100;
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
        View view = inflater.inflate(R.layout.delivery_requests_fragment, container, false);
        //set the view for data access purposes
        mView=view;
        ConstraintLayout layout = (ConstraintLayout)mView.findViewById(R.id.container);
        //dynamically create UI based on the data (which is updated in the response receiver
        for(int i=minIndex;i<maxIndex;i++){
            
            TextView orderText = new TextView(getActivity());
            //assuming there is less than 100 orders at a time
            //using a computation factor for Id to avoid conflicting with Java system UI id
            final int declineButtonId=i*declineButtonComputationFactor;
            int acceptButtonId=i*acceptButtonComputationFactor;
            //setting id (and text) for each element
            orderText.setId(i);
            orderText.setText(data.get(i-minIndex));

            Button declineButton = new Button(getActivity());
            declineButton.setText("decline");
            declineButton.setId(declineButtonId);

            Button acceptButton = new Button(getActivity());
            acceptButton.setText("accept");
            acceptButton.setId(acceptButtonId);

           //set the listener for each of the decline buttons, which deletes the data and updates the UI
            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int buttonIndex=view.getId();
                    buttonIndex/=declineButtonComputationFactor;
                   ids.remove(buttonIndex-minIndex);
                   data.remove(buttonIndex-minIndex);
                   maxIndex--;
                    transactFragment(newInstance(),true);


                }
            });
            //set the listener for each of the accept buttons, which gets the Id of the order and sends it to the server
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int buttonIndex=view.getId();
                    buttonIndex/=acceptButtonComputationFactor;
                    String id = ids.get(buttonIndex-minIndex);
                    //TODO:establish socket connection, send in user name and phone number
                }
            });
            //add the Ui elements
            layout.addView(orderText);
            layout.addView(declineButton);
            layout.addView(acceptButton);

            ConstraintLayout.LayoutParams orderTextLayoutParam = new ConstraintLayout.LayoutParams( ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

            if(i==minIndex){//the very first text
                //connect the top of the text to the button of the very top block
                orderTextLayoutParam.topToBottom=ConstraintSet.PARENT_ID;



            }else{
                //connect the top of the text with a previous accept button
                orderTextLayoutParam.topToBottom=(i-1)*acceptButtonComputationFactor;//below the previous button

            }
            //connect the textbox to the container
            orderTextLayoutParam.rightToRight=ConstraintSet.PARENT_ID;
            orderTextLayoutParam.leftToLeft=ConstraintSet.PARENT_ID;

            orderText.setLayoutParams(orderTextLayoutParam);

            ConstraintLayout.LayoutParams declineButtonLayoutParam = new ConstraintLayout.LayoutParams( ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
           //connect the button to the textbox and the parent container
            declineButtonLayoutParam.topToBottom=orderText.getId();
            declineButtonLayoutParam.rightToRight=ConstraintSet.PARENT_ID;
            declineButtonLayoutParam.leftToRight=acceptButton.getId();
            declineButton.setLayoutParams(declineButtonLayoutParam);

            ConstraintLayout.LayoutParams acceptButtonLayoutParam = new ConstraintLayout.LayoutParams( ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            acceptButtonLayoutParam.topToBottom=orderText.getId();
            acceptButtonLayoutParam.leftToLeft=ConstraintSet.PARENT_ID;
            acceptButtonLayoutParam.rightToLeft=declineButton.getId();
            acceptButton.setLayoutParams(acceptButtonLayoutParam);
            
            
        }
       




        return view;
    }

    /**
     * update the data for all delivery requests
     * @param orderId the id of the request
     * @param text the details of the request
     */
    public void updateData(String orderId,String text){
        ids.add(orderId);
        data.add(text);
        maxIndex++;
    }

    /**
     * remove a piece of data from the delivery requests
     * @param orderId the id of the request
     * @param text the details of the request
     */
    public void removeData(String orderId, String text){
        ids.remove(orderId);
        data.remove(text);
        maxIndex--;

    }

    /**
     * update the UI
     * @param fragment the fragment to be updated
     * @param reload if the user wants to reload
     */
    public void transactFragment(Fragment fragment, boolean reload) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (reload) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
