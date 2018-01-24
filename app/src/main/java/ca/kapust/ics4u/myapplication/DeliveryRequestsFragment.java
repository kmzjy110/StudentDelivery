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
        mView=view;
        ConstraintLayout layout = (ConstraintLayout)mView.findViewById(R.id.container);
        
        for(int i=minIndex;i<maxIndex;i++){
            
            TextView orderText = new TextView(getActivity());
            //assuming there is less than 100 orders at a time
            final int declineButtonId=i*declineButtonComputationFactor;
            int acceptButtonId=i*acceptButtonComputationFactor;
            
            orderText.setId(i);
            orderText.setText(data.get(i-minIndex));

            Button declineButton = new Button(getActivity());
            declineButton.setText("decline");
            declineButton.setId(declineButtonId);

            Button acceptButton = new Button(getActivity());
            acceptButton.setText("accept");
            acceptButton.setId(acceptButtonId);

            final int temp=i-minIndex;
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

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int buttonIndex=view.getId();
                    buttonIndex/=acceptButtonComputationFactor;
                    String id = ids.get(buttonIndex-minIndex);
                    //TODO:establish socket connection, send in user name and phone number
                    NetworkHelper.hell.accept(Integer.parseInt(id));
                }
            });

            layout.addView(orderText);
            layout.addView(declineButton);
            layout.addView(acceptButton);

            ConstraintLayout.LayoutParams orderTextLayoutParam = new ConstraintLayout.LayoutParams( ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

            if(i==minIndex){//the very first text
                orderTextLayoutParam.topToBottom=ConstraintSet.PARENT_ID;



            }else{
                orderTextLayoutParam.topToBottom=(i-1)*acceptButtonComputationFactor;//below the previous button

            }
            orderTextLayoutParam.rightToRight=ConstraintSet.PARENT_ID;
            orderTextLayoutParam.leftToLeft=ConstraintSet.PARENT_ID;

            orderText.setLayoutParams(orderTextLayoutParam);

            ConstraintLayout.LayoutParams declineButtonLayoutParam = new ConstraintLayout.LayoutParams( ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
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
    public void updateData(String orderId,String text){
        ids.add(orderId);
        data.add(text);
        maxIndex++;
    }
    public void removeData(String orderId, String text){
        ids.remove(orderId);
        data.remove(text);
        maxIndex--;

    }
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
