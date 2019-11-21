package com.ecalm.e_calm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecalm.e_calm.auth.SplashScreenActivity;

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView name, age, tall, weight, gender, hobby;
    EditText editUrl;
    LinearLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        sharedPreferences = this.getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        name = findViewById(R.id.profileNamaText);
        age = findViewById(R.id.profileUmurText);
        tall = findViewById(R.id.profileTbText);
        weight = findViewById(R.id.profileBbText);
        logout = findViewById(R.id.profileLogout);
        editUrl = findViewById(R.id.editUrlText);

        name.setText(sharedPreferences.getString(SharedPrefManager.KEY_NAME, null));
        age.setText(sharedPreferences.getString(SharedPrefManager.KEY_AGE, null)+" year");
        tall.setText(sharedPreferences.getString(SharedPrefManager.KEY_TALL, null)+" cm");
        weight.setText(sharedPreferences.getString(SharedPrefManager.KEY_WEIGHT, null)+" kg");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });
        editUrl.setText(sharedPreferences.getString(SharedPrefManager.KEY_URL, null));

        editUrl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor = sharedPreferences.edit().putString(SharedPrefManager.KEY_URL, editUrl.getText().toString());
                editor.apply();

                String tmp = sharedPreferences.getString(SharedPrefManager.KEY_URL, null);

                Toasty.success(ProfileActivity.this, "Link Updated = \""+tmp+"\"").show();

                editUrl.setText(tmp);
                return false;
            }
        });
    }

    private  void showPopUp(){
        AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
        alert.setMessage("Are you Sure ?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPreferences.edit().clear().commit();
                        Intent i = new Intent(ProfileActivity.this, SplashScreenActivity.class);
                        startActivity(i);
                        finish();
                    }
                }).setNegativeButton("Cancel", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
