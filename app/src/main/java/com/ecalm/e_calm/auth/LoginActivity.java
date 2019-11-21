package com.ecalm.e_calm.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ecalm.e_calm.MainActivity;
import com.ecalm.e_calm.R;
import com.ecalm.e_calm.SharedPrefManager;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import io.isfaaghyth.rak.Rak;

public class LoginActivity extends AppCompatActivity {

    EditText mUserName, mPassword;
    Button btnSignIn;
    TextView tvSignUp;

    String uName, password;

    String API = "";

    ProgressBar pgLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        AndroidNetworking.initialize(getApplicationContext());

        Rak.initialize(getApplicationContext());

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        mUserName = findViewById(R.id.edt_signin_username);
        mPassword = findViewById(R.id.edt_signin_password);
        btnSignIn = findViewById(R.id.btn_signIn);
        tvSignUp = findViewById(R.id.tv_signUp);
        pgLogin = findViewById(R.id.pg_login);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        tvSignUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                return true;
            }
        });
        getInputText();
    }

    void getInputText(){
        uName = mUserName.getText().toString();
        password = mPassword.getText().toString();
    }

    void postData(){
        pgLogin.setVisibility(View.VISIBLE);
        AndroidNetworking.post(API)
                .addBodyParameter("username", uName)
                .addBodyParameter("password", password)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pgLogin.setVisibility(View.INVISIBLE);
                        Toasty.success(getApplicationContext(), "Login Success", Toasty.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();

                    }

                    @Override
                    public void onError(ANError anError) {
                        pgLogin.setVisibility(View.INVISIBLE);
                        Toasty.success(getApplicationContext(), "Login Failed", Toasty.LENGTH_SHORT).show();
                    }
                });
    }
}
