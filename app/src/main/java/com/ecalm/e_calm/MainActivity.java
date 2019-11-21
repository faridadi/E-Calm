package com.ecalm.e_calm;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecalm.e_calm.math.Status;
import com.ecalm.e_calm.sqlite.DatabaseHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    RelativeLayout remainder;
    RelativeLayout edu;
    RelativeLayout calorie;
    RelativeLayout profile;
    TextView date, calorieCounter, status;
    DatabaseHelper db;
    SharedPreferences sharedPreferences;
    String counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).onSameThread().check();


        sharedPreferences = this.getSharedPreferences("fanregisterlogin", Context.MODE_PRIVATE);

        counter =  sharedPreferences.getString(SharedPrefManager.KEY_CALORIE, null)+"kcal / "+sharedPreferences.getString(SharedPrefManager.KEY_LIMIT, null)+"kcal";

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db = new DatabaseHelper(this);

        calorieCounter = findViewById(R.id.calorieCounter);
        calorieCounter.setText(counter);


        remainder = findViewById(R.id.menuRemainder);
        edu = findViewById(R.id.menuEdu);
        calorie = findViewById(R.id.menuCalorie);
        date = findViewById(R.id.dateTextView);
        profile = findViewById(R.id.menuProfile);

        //setting status Healthy or Not healthy
        status = findViewById(R.id.statusText);
        Status sts = new Status();
        status.setText(sts.cekStatus(Float.parseFloat(sharedPreferences.getString(SharedPrefManager.KEY_CALORIE, null)),Float.parseFloat(sharedPreferences.getString(SharedPrefManager.KEY_LIMIT, null))));

        Date dateNow = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        date.setText(df.format(dateNow));

        remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(MainActivity.this, "Comming Soon (:").show();
            }
        });

        edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(MainActivity.this, "Comming Soon (:").show();
            }
        });

        calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(i);
                finish();
                Toasty.normal(MainActivity.this, "Scan your food").show();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(MainActivity.this, "My Profile").show();
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
