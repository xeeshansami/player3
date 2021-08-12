package com.play.view.videoplayer.hd.CursorUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.play.view.videoplayer.hd.R;
import com.play.view.videoplayer.hd.VideoFolder;

import com.google.android.gms.ads.InterstitialAd;

public class Splash extends AppCompatActivity {

    public ProgressBar progressBar;
    android.os.Handler Handler;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.my_progressBar);
        Handler handler
                =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doFunc();
            }
        },1000);
    }


    public void doFunc() {
        /* Create an Intent that will start the Menu-Activity. */
        Intent mainIntent = new Intent(Splash.this, VideoFolder.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }
}
