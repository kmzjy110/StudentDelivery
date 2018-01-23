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
    //reason = 1: Sent a Request await delivery
    //reason = 2: Is a deliverer
    int reason;
    Socket socket;
    public NetworkHelper(){
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
                        String error = data.getString("error");
                        //Run code for when there is a delivery available

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Run login code here

                    //socket.disconnect();
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
    public NetworkHelper(final String ordererLocation ,final String restaurantLocation,final String restaurantName,final String order,final String cost,final String tip){
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

    public void Destroy(){
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


