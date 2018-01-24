package ca.kapust.ics4u.myapplication;


import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by V on 1/22/2018.
 */

public class NetworkHelper {
    static NetworkHelper hell;
    static String defaultIp = "http://www.kapust.ca";
    CommunicationIntentService view;
    //reason = 1: Sent a Request await delivery
    //reason = 2: Is a deliverer
    int reason;
    Socket socket;

    public NetworkHelper(final CommunicationIntentService view){
        this.view = view;
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = false;
        //opts.reconnectionAttempts=5;
        //opts.reconnectionDelay = 5;
        //opts.query = "auth_token=" + authToken;
        try {
            socket = IO.socket(defaultIp, opts);
            reason =2;
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject data = new JSONObject();

                    try {
                        data.put("id",MainActivity.currentUser);
                        socket.emit("startDelivering", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {}

            });
            socket.on("found", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    JSONObject data = (JSONObject)args[0];
                    try {
                        view.createDeli(data.getJSONObject("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
            socket.on("orderConfirmed", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        if(data.getString("id").equals(""+MainActivity.currentUser)){
                            //TODO When show a notification

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            });
            socket.on("orderAlreadyTaken", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        //TODO demonstrate that there is no more order


                        data.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            });
            socket.on("success", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        String error = data.getString("error");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //socket.disconnect();
                    //mPasswordView.setError(getString(R.string.error_incorrect_password));
                    //mPasswordView.requestFocus();

                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
    public NetworkHelper(final CommunicationIntentService view, final String ordererLocation , final String restaurantLocation, final String restaurantName, final String order, final String cost, final String tip){
        this.view = view;
        reason=2;
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        //opts.reconnection = true;
        //opts.reconnectionAttempts=5;
        //opts.reconnectionDelay = 5;
        //opts.query = "auth_token=" + authToken;
        try {
            socket = IO.socket(defaultIp, opts);
            reason =2;
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("id",MainActivity.currentUser);
                        data.put("ordererLocation",ordererLocation);
                        data.put("restaurantLocation",restaurantLocation);
                        data.put("restaurantName",restaurantName);
                        data.put("order",order);
                        data.put("cost",cost);
                        data.put("tip",tip);
                        data.put("currentLocation","PLACE");
                        socket.emit("createOrder", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {}

            });
            socket.on("foundDeliverer", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    JSONObject data = (JSONObject)args[0];
                    try {
                        String phoneNumber = data.getString("phoneNumber");
                        String name = data.getString("name");
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(ResponseReceiver.ACTION);
                        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        //indicate that a delivery has been accepted
                        broadcastIntent.putExtra(MainActivity.ACTION_INDICATOR,MainActivity.DELIVERY_ACCEPTED_ACTION);
                        //broadcast the information and send the data back to the main activity
                        broadcastIntent.putExtra("Details","Name:"+name+" Phone:"+phoneNumber);
                        view.sendBroadcast(broadcastIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Run login code here

                    socket.disconnect();
                }

            });
            socket.on("success", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        String error = data.getString("error");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //socket.disconnect();
                }
            });
            socket.on("fail", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        String error = data.getString("error");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    socket.disconnect();
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void accept(int orderId){
        JSONObject data = new JSONObject();
        try {
            data.put("id",MainActivity.currentUser);
            data.put("orderId",orderId);
            socket.emit("acceptDelivery", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void destroy(){
        JSONObject data = new JSONObject();
        try {
            data.put("id",MainActivity.currentUser);
            socket.emit("stopDelivering", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.disconnect();
    }

}


