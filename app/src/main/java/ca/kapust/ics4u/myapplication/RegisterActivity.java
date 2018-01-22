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
        mEmail = (EditText) findViewById(R.id.email);
        mFirstName = (EditText) findViewById(R.id.fName);
        mLastName = (EditText) findViewById(R.id.lName);
        mPhoneNum = (EditText) findViewById(R.id.phoneNum);
        mBirthday = (EditText) findViewById(R.id.birthday);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirmPassword);

        Button mEmailRegisterButton = (Button)findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
    }

    protected void attemptRegistration(){
        final String fName = mFirstName.getText().toString();
        final String lName = mLastName.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String confirmPassword = mConfirmPasswordView.getText().toString();
        final String email = mEmail.getText().toString();
        String phoneNum = mPhoneNum.getText().toString();
        String bday = mBirthday.getText().toString();

        //NEED TO DO CHECKS HERE HARRY
        boolean reg = false;
        int counter =0;
        String temp="";
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
        if(fName.length()<1){
            mFirstName.setError("Field is required");
            mFirstName.requestFocus();
            reg=true;
        }
        if(lName.length()<1){
            mLastName.setError("Field is required");
            mLastName.requestFocus();
            reg=true;
        }
        if(!isPasswordValid(password)){
            reg=true;
            mPasswordView.setError("Password doesn't meet requirements");
            mPasswordView.requestFocus();
        }else{
        if(!password.equals(confirmPassword)){
            mConfirmPasswordView.setError("Passwords don't match");
            mConfirmPasswordView.requestFocus();
            reg=true;
        }}
        if(!(email.contains("@")&&email.contains("."))){
            mEmail.setError("Not a valid email");
            mEmail.requestFocus();
            reg=true;
        }



        if(!reg){

        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        try {
            socket = IO.socket(NetworkHelper.defaultIp, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                JSONObject data = new JSONObject();
                try {
                    data.put("email",email);
                    data.put("password",LoginActivity.encryptPwd(password));
                    data.put("firstName",fName);
                    data.put("lastName",lName);
                    data.put("phoneNumber",phoneNumber);
                    data.put("birthday",birthday);
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
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                
                socket.disconnect();
            }

        });
        socket.on("fail", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject)args[0];
                try {
                    String error = data.getString("error");
                    if(error.equals("users.email")){
                       RegisterActivity.this.runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                mEmail.setError("Email is already used");
                                mEmail.requestFocus();
                            }});
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.disconnect();
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();

            }

        });
        socket.connect();
        }
    }
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
