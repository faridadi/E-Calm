package com.ecalm.e_calm.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ecalm.e_calm.MainActivity;
import com.ecalm.e_calm.R;
import com.ecalm.e_calm.SharedPrefManager;
import com.ecalm.e_calm.math.BMR;
import com.ecalm.e_calm.model.User;
import com.ecalm.e_calm.network.ConnectivityHelper;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    EditText mUserName, mGender, mAge, mBodyWeight, mBodyTall;
    Button btnRegister;
    TextView tvSignIn;

    String username;
    int gender = 0;
    int age;
    float weight, height;
    ProgressBar pgRegister;
    RadioGroup radioGroup;

    String API = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        AndroidNetworking.initialize(getApplicationContext());

        //if the user is already logged in we will directly start the MainActivity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        mUserName = findViewById(R.id.edt_username);
        mAge = findViewById(R.id.edt_age);
        mBodyWeight = findViewById(R.id.edt_bodyweight);
        mBodyTall = findViewById(R.id.edt_bodytall);
        btnRegister = findViewById(R.id.btn_signUp);
        pgRegister = findViewById(R.id.pg_register);
        radioGroup = findViewById(R.id.radioGroup);

        tvSignIn = findViewById(R.id.tv_signIn);

        //getInputData();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    getInputData();
                    User model = new User(username, age, height, weight, gender);
                    SharedPrefManager.getInstance(RegisterActivity.this).userRegister(model);
                    Intent i =  new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Pastikan semua terisi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.maleRadio:
                        gender = 0;
                        Toasty.normal(RegisterActivity.this, "Male").show();
                        break;
                    case R.id.female:
                        gender = 1;
                        Toasty.normal(RegisterActivity.this, "Female").show();
                        break;
                }
            }
        });
    }

    void getInputData(){
        username = mUserName.getText().toString();
        age = Integer.parseInt(mAge.getText().toString());
        //gender sudah di handling di radio button

        height = Float.parseFloat(mBodyTall.getText().toString());
        weight = Float.parseFloat(mBodyWeight.getText().toString());
    }

//    void postData(){
//        pgRegister.setVisibility(View.VISIBLE);
//        AndroidNetworking.post(API)
//                .addBodyParameter("username", username)
//                .addBodyParameter("email", email)
//                .addBodyParameter("password", password)
//                .addBodyParameter("bodyWeight", Integer.toString(bodyWeight))
//                .addBodyParameter("bodyTall", Integer.toString(bodyTall))
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        pgRegister.setVisibility(View.INVISIBLE);
//                        Toasty.success(getApplicationContext(), "Register Sucessfull", Toast.LENGTH_SHORT).show();
//
//                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        pgRegister.setVisibility(View.INVISIBLE);
//                        Toasty.error(getApplicationContext(), "Register Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private boolean checkForm() {
        if (isEmpty(mUserName)) {
            mUserName.setError("Silahkan isi username");
        } else if (mUserName.getText().length() < 4) {
            showErrorName();
        }  else if (isEmpty(mAge)) {
            mAge.setError("Silahkan isi umur anda");
        } else if (isEmpty(mBodyWeight)) {
            mBodyWeight.setError("Silahkan isi berat badan anda");
        } else if (isEmpty(mBodyTall)){
            mBodyTall.setError("Silahkan isi tinggi badan anda");
        }else {
            return true;
        }
        return false;
    }

    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void showErrorName() {
        Toasty.error(getApplicationContext(), "The Nama field must be at least 4 characters in length ", Toasty.LENGTH_SHORT).show();
    }
}
