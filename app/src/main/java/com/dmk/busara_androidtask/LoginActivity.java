package com.dmk.busara_androidtask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.ISyncModel;
import net.SyncModel;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText _emailText, _passwordText;

    private Button _loginButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        SyncModel syncModel = new SyncModel(this);
        syncModel.setLoginDelegate(new ISyncModel.LoginDelegate() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                startActivity(new Intent(LoginActivity.this, VideoActivity.class)
//                .putExtra("link","http://api.smartduka.busaracenterlab.org/media/TNS_SmartDuka-Module_1_Part_A.m4v").putExtra("title","Video"));

                finish();

            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                AlertDialog.Builder bd = new AlertDialog.Builder(LoginActivity.this);
                bd.setTitle("Alert");
                bd.setMessage(message);
                bd.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                bd.show();
            }
        });
        syncModel.login(email, password);

        // startActivity(new Intent(this,MainActivity.class));


    }




    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}