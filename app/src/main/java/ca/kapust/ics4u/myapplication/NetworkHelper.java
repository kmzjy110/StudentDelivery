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
    //static networkHelper using in the background service
    static NetworkHelper hell;
    //set the defaultIp of the server (yes this is my domain)
    // NOTE this sends requests to port 80 but the server runs on port 8000 because of local port forwarding
    static String defaultIp = "http://www.kapust.ca";
    //get the context
    CommunicationIntentService view;
    //reason = 1: Sent a Request await delivery
    //reason = 2: Is a deliverer
    int reason;
    //store the socket
    Socket socket;

    /**Initialize the network helper for when the user wants to be a deliverer
     * Create the socket, connect to the server and be ready to receive orders
     * @param view the service from which the network helper interacts with the app
     */
    public NetworkHelper(final CommunicationIntentService view){
        this.view = view;
        //set the connection settings
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = false;
        try {
            //initialize the socket
            socket = IO.socket(defaultIp, opts);
            reason =2;
            //when the socket connects
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //set the JSON data
                    JSONObject data = new JSONObject();
                    try {
                        data.put("id",MainActivity.currentUser);
                        //tell the server that the client wants to start delivering
                        socket.emit("startDelivering", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                //when the socket disconnects
                @Override
                public void call(Object... args) {}

            });
            //when the was a order found place it in the delivery requests section
            socket.on("found", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    JSONObject data = (JSONObject)args[0];
                    try {
                        //create the delivery requests section
                        view.createDeli(data.getJSONObject("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
            //if the order was already taken reject the order
            socket.on("orderAlreadyTaken", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject)args[0];
                    try {
                        //This a non functioning method bc of a lack of time
                        data.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            });
            //connect to the server
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**Initialize the network help when the client is requesting an order
     * establish socket, connect to the sever and send in the data
     * @param view the service where the network helper interfaces with the rest of the app
     * @param ordererLocation location of the user
     * @param restaurantLocation location of the restaurant
     * @param restaurantName name of the restaurant
     * @param order the users order for food
     * @param cost the desired cost
     * @param tip the amount the deliverer will be tipped
     */
    public NetworkHelper(final CommunicationIntentService view, final String ordererLocation , final String restaurantLocation, final String restaurantName, final String order, final String cost, final String tip){
        this.view = view;
        reason=2;
        //set the connection options
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = false;
        try {
            //initialize the socket
            socket = IO.socket(defaultIp, opts);
            reason =2;
            //on the sockets connection
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
                        //send in all the data for the order
                        socket.emit("createOrder", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                //when the socket disconnects
                @Override
                public void call(Object... args) {}

            });
            //when a deliverer is found for the client order
            socket.on("foundDeliverer", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    //create a notification for the deliver
                    JSONObject data = (JSONObject)args[0];
                    try {
                        if(data.getString("id").equals(""+MainActivity.currentUser)){
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //disconnect from the server
                    socket.disconnect();
                }

            });
            //when the order is submitted clear the fields and create a notification
            socket.on("success", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    view.sentOrder();
                }
            });
            //connect to the server
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**accept the an order when you're a deliverer
     * send the id of the order to know when on the client accepted
     * @param orderId the id of the order which the user accepted
     */
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

    /**Destroy the network helper
     * clear the database then disconnect from the server
     */
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


