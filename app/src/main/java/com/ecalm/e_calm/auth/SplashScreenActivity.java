package com.ecalm.e_calm.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ecalm.e_calm.IntroActivity;
import com.ecalm.e_calm.R;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView logo;
    private int waktu = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent first = new Intent(SplashScreenActivity.this, IntroActivity.class);
                startActivity(first);
                finish();
            }
        }, waktu);
    }
}
