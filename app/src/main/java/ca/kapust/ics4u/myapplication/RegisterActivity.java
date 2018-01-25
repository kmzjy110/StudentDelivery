package ca.kapust.ics4u.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RegisterActivity extends AppCompatActivity {

    static RegisterActivity current;
    //create a new socket
    private Socket socket;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mEmail;
    private EditText mPhoneNum;
    private EditText mBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        current=this;
        //initialize all variables
        mEmail = (EditText) findViewById(R.id.email);
        mFirstName = (EditText) findViewById(R.id.fName);
        mLastName = (EditText) findViewById(R.id.lName);
        mPhoneNum = (EditText) findViewById(R.id.phoneNum);
        mBirthday = (EditText) findViewById(R.id.birthday);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirmPassword);

        //set the listener to attmpt registration
        Button mEmailRegisterButton = (Button)findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
    }

    protected void attemptRegistration(){
        //get all values
        final String fName = mFirstName.getText().toString();
        final String lName = mLastName.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String confirmPassword = mConfirmPasswordView.getText().toString();
        final String email = mEmail.getText().toString();
        String phoneNum = mPhoneNum.getText().toString();
        String bday = mBirthday.getText().toString();
        //checking for validity of all data
        boolean reg = false;
        int counter =0;
        String temp="";
        //validity of the phone number
        for(int i =0;i<phoneNum.length();i++){
            for(int j=0;j<10;j++){
                String let = ""+phoneNum.charAt(i);
                if(let.equals(""+j)){
                    temp+=let;
                    counter++;
                }
            }
        }

        final String phoneNumber = temp;
        if(counter!=10&&counter!=11){
            mPhoneNum.setError("Not a valid phone number");
            mPhoneNum.requestFocus();
            reg=true;
        }
        counter =0;
        temp="";
        //validity of the birthday
        for(int i =0;i<bday.length();i++){
            for(int j=0;j<10;j++){
                String let = ""+bday.charAt(i);
                if(let.equals(""+j)){
                    temp+=let;
                    counter++;
                }
            }
        }
        if(counter!=8){
            mBirthday.setError("Not a valid birthday");
            mBirthday.requestFocus();
            reg=true;
        }
        final String birthday = temp;
        //validity of the first name
        if(fName.length()<1){
            mFirstName.setError("Field is required");
            mFirstName.requestFocus();
            reg=true;
        }
        //validity of the last name
        if(lName.length()<1){
            mLastName.setError("Field is required");
            mLastName.requestFocus();
            reg=true;
        }
        //validity of the password
        if(!isPasswordValid(password)){
            reg=true;
            mPasswordView.setError("Password doesn't meet requirements");
            mPasswordView.requestFocus();
        }else{
            //validity of the confirmation password
        if(!password.equals(confirmPassword)){
            mConfirmPasswordView.setError("Passwords don't match");
            mConfirmPasswordView.requestFocus();
            reg=true;
        }}
        //validity of the email
        if(!(email.contains("@")&&email.contains("."))){
            mEmail.setError("Not a valid email");
            mEmail.requestFocus();
            reg=true;
        }
        //if all the data is valid
        if(!reg){
            //set the setting for the connection
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        try {
            //try set the connection and include the ip
            socket = IO.socket(NetworkHelper.defaultIp, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //when the socket connects
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                //create a JSON object
                JSONObject data = new JSONObject();
                try {
                    //place all the info into the object
                    data.put("email",email);
                    data.put("password",LoginActivity.encryptPwd(password));
                    data.put("firstName",fName);
                    data.put("lastName",lName);
                    data.put("phoneNumber",phoneNumber);
                    data.put("birthday",birthday);
                    //send the registration data
                    socket.emit("register", data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            // when the socket disconnects
            @Override
            public void call(Object... args) {}

        });
        //when the registration is a success
        socket.on("success", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //Run go the the login page
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                //disconnect from the server
                socket.disconnect();
            }

        });
        //when the registration fails
        socket.on("fail", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //get the reason for the error
                JSONObject data = (JSONObject)args[0];
                try {
                    String error = data.getString("error");
                    //if the reason for the error is that the email is already used
                    if(error.equals("users.email")){
                       RegisterActivity.this.runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                //inform the user that the email is already in use
                                mEmail.setError("Email is already used");
                                mEmail.requestFocus();
                            }});
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //disconnect from the server
                socket.disconnect();


            }

        });
        //establish a connection with the server
        socket.connect();
        }
    }

    /**
     * check if the password matches the standard for the app
     * @param password the password to be checked
     * @return
     */
    private boolean isPasswordValid(String password) {

        boolean containsUpperCase=false;
        boolean containsLowerCase=false;
        boolean containsNumber=false;
        boolean lengthReq=password.length() >= 6;
        for(int i=0;i<password.length();i++){
            for (char u=65;u<=90;u++){
                if(password.charAt(i)==u){
                    containsUpperCase=true;
                    break;

                }
            }
            for (char l=97;l<=122;l++){
                if(password.charAt(i)==l){
                    containsLowerCase=true;
                    break;

                }
            }
            for (char n=48;n<=57;n++){
                if(password.charAt(i)==n){
                    containsNumber=true;
                    break;

                }
            }

        }

        return containsUpperCase &&containsLowerCase &&containsNumber &&lengthReq;
    }


}
