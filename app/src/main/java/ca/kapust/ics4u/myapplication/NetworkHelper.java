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
    static String defaultIp = "http://www.kapust.ca";
    int reason;
    Socket socket;
    public NetworkHelper(int reason){
        this.reason = reason;
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        //opts.reconnection = true;
        //opts.reconnectionAttempts=5;
        //opts.reconnectionDelay = 5;
        //opts.query = "auth_token=" + authToken;
        try {
            socket = IO.socket(defaultIp, opts);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    JSONObject data = new JSONObject();
                    try {
                        data.put("email","");
                        socket.emit("register", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {}

            });
            socket.on("success", new Emitter.Listener() {
                @Override
                public void call(Object... args) {


                    //Run login code here

                    socket.disconnect();
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
                    //mPasswordView.setError(getString(R.string.error_incorrect_password));
                    //mPasswordView.requestFocus();

                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }



}


