package com.play.view.player.videoplayer4k.CursorUtils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.play.view.player.videoplayer4k.R;
import com.play.view.player.videoplayer4k.VideoFolder;
import com.google.android.gms.ads.MobileAds;


public class SplashScreen extends AppCompatActivity {
    private int MY_PERMISSIONS_REQUEST_READANDWRITE_EXTERNAL_STORAGE = 1230;
    public View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(SplashScreen.this, getResources().getString(R.string.app_id));
        //   --- Admob ---
        view=getWindow().getDecorView().getRootView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (checkStoragePermission() && checkWritePermission()) {
                try {
                    if (isOnline()) {
                        Admob.createLoadInterstitial(SplashScreen.this,null);
                    } else {
                        doFunc();
                    }
                } catch (Exception d) {
                }
            } else {
                requestStoragePermission();
            }
        } else {
            if (isOnline()) {
                Admob.createLoadInterstitial(SplashScreen.this,null);
            } else {
                doFunc();
            }
        }
    }
    public void doFunc() {
        /* Create an Intent that will start the Menu-Activity. */
        Intent mainIntent = new Intent(SplashScreen.this, VideoFolder.class);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    startActivity(mainIntent);
                    finish();
                }
            }
        };
        thread.start();
        SplashScreen.this.startActivity(mainIntent);
        SplashScreen.this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READANDWRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isOnline()) {
                    Admob.createLoadInterstitial(SplashScreen.this,null);
                } else {
                    doFunc();
                }
            } else {
                checkStoragePermission();
                checkWritePermission();
            }
        }
    }

    private boolean checkStoragePermission() {
        return ActivityCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkWritePermission() {
        return ActivityCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(SplashScreen.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READANDWRITE_EXTERNAL_STORAGE);


    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected() && wifiNetwork.isAvailable()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected() && mobileNetwork.isAvailable()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable()) {
            return true;
        }

        return false;
    }
}