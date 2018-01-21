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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RegisterActivity extends AppCompatActivity {

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
        final String phoneNum = mPhoneNum.getText().toString();
        final String birthday = mBirthday.getText().toString();

        //NEED TO DO CHECKS HERE HARRY








        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        //opts.reconnection = true;
        //opts.reconnectionAttempts=5;
        //opts.reconnectionDelay = 5;
        //opts.query = "auth_token=" + authToken;
        try {
            socket = IO.socket(LoginActivity.defaultIp, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                JSONObject data = new JSONObject();
                try {
                    data.put("email",email);
                    data.put("password",password);
                    data.put("firstName",fName);
                    data.put("lastName",lName);
                    data.put("phoneNumber",phoneNum);
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
                //startActivity(new Intent(LoginActivity.this,MainActivity.class));
                
                socket.disconnect();
            }

        });
        socket.on("fail", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //Run login code here
                //showProgress(false);
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
                socket.disconnect();
            }

        });
        socket.connect();
    }

}
