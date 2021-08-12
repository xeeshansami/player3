package com.play.view.videoplayer.hd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity_Front extends AppCompatActivity {
    Dialog dialog;
    private AlertDialog alertDialog;
    FragmentManager fragmentManager;
    AudioFragment downloaded_video_fragment;
    Button videofolder, video, rateus, player,recent;
    FragmentTransaction fragmentTransaction;
    private boolean doubleBackToExitPressedOnce = false;
    private int brightnessValue;
    SharedPreferences mSharedPrefs = null;
    private ContentResolver cResolver;
    Toolbar toolbar;
    private AdView mAdView;
    private AdRequest adRequest, ar;
    ProgressBar loadingIndicator;

    @Override
    protected void onResume() {
        super.onResume();


        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        //brightnessValue = mSharedPrefs.getInt("ScreenBrightness", 0);
          //setBrightness(brightnessValue);
    }
//
//    protected int getScreenBrightness() {
//
//        brightnessValue = Settings.System.getInt(
//                getContentResolver(),
//                Settings.System.SCREEN_BRIGHTNESS, 0);
//        Log.i("", "Sysytem bright" + brightnessValue);
//        return brightnessValue;
//    }
    @Override
    protected void onRestart() {
        super.onRestart();
        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        //brightnessValue = mSharedPrefs.getInt("ScreenBrightness", 0);
         //setBrightness(brightnessValue);
    }

//
//    public void setBrightness(int brightness) {
////
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.System.canWrite(getApplicationContext())) {
//                if (brightness >= 0 || brightness <= 255)
//                    cResolver = this.getApplicationContext().getContentResolver();
//                Settings.System.putInt(cResolver,
//                        Settings.System.SCREEN_BRIGHTNESS_MODE,
//                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
//
//                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
//
//            } else
//
//            {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                intent.setData(Uri.parse("package:" + this.getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        } else {
//            try {
//                //sets manual mode and brightnes 255
//                if (brightness >= 0 || brightness <= 255)
//                    cResolver = getApplicationContext().getContentResolver();
////                Settings.System.putInt(cResolver,
////                        Settings.System.SCREEN_BRIGHTNESS_MODE,
////                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
////                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness); //this will set the brightness to maximum (255)
////
////                //refreshes the screen
//////            int br = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
//////            Log.d("Brightness :", String.valueOf(br));
////                WindowManager.LayoutParams lp = getWindow().getAttributes();
////                lp.screenBrightness = (float) brightness / 255;
////                getWindow().setAttributes(lp);
//                //sets manual mode and brightnes 255
//                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);  //this will set the manual mode (set the automatic mode off)
//                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);  //this will set the brightness to maximum (255)
//
//                //refreshes the screen
//                int br = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.screenBrightness = (float) br / 255;
//                getWindow().setAttributes(lp);
//            } catch (Exception e) {
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_front);


//        getScreenBrightness();
//        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
//        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
//
////        System.out.println("onPause Last saved Progress" + mProgress);
//        mEditor.putInt("ScreenBrightness", brightnessValue);
////        mEditor.putInt("SongPosition", songPosn);
//        mEditor.apply();

        video = findViewById(R.id.allvideo);
        videofolder = findViewById(R.id.videofolder);
        player = findViewById(R.id.player);
        rateus = findViewById(R.id.rateus);
        recent = findViewById(R.id.recent);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(com.play.view.videoplayer.hd.R.drawable.abc);
        getSupportActionBar().setTitle("Full HD Video Player");
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mAdView = (AdView) findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
////                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                .build();
        mAdView.loadAd(adRequest);



        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(MainActivity_Front.this, "Fetching Data Wait a Second", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity_Front.this, VideoActivity.class);
                startActivity(i);


                //dialog.show();
                //dialog.dismiss();
            }
        });
        videofolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity_Front.this, "Fetching Data Wait a Second", Toast.LENGTH_SHORT).show();
                Intent audiosFolders = new Intent(MainActivity_Front.this, VideoFolder.class);
                startActivity(audiosFolders);



            }
        });
        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.root_view, new AudioFragment()).commit();

//
//                FragmentManager fmst = getSupportFragmentManager();
//                FragmentTransaction ftst = fmst.beginTransaction();
//                AudioFragment st = new AudioFragment();
//                ftst.commit();
//                Intent i = new Intent(MainActivity_Front.this,Main2Activity.class);
//                startActivity(i);
            }
        });
        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network_stream();
            }
        });


        recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity_Front.this,Recent.class);
                startActivity(i);
                //Toast.makeText(MainActivity_Front.this, "Under Construction", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        menu.findItem(R.id.delete).setVisible(false);
        // Associate searchable configuration with the SearchView
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (itemId == android.R.id.home) {

//
        }
        else if (itemId ==  R.id.setting) {
            Intent i = new Intent(MainActivity_Front.this,SettingActivity.class);
            i.putExtra("package","main");
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    void network_stream() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity_Front.this);
        dialogBuilder.setCancelable(false);

// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rate_us_demo, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();


      //  Button cancel = (Button) dialogView.findViewById(R.id.rateUsCancel);
        Button ok = (Button) dialogView.findViewById(R.id.rateUsOk);
        Button quit = (Button) dialogView.findViewById(R.id.quit);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dhdvideo.player")));
                alertDialog.dismiss();

            }
        });





        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                android.provider.Settings.System.putInt(getApplicationContext().getContentResolver(),
//                        android.provider.Settings.System.SCREEN_BRIGHTNESS,brightnessValue );
                alertDialog.dismiss();
                finishAffinity();

            }
        });


        alertDialog.show();

    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if(getSupportFragmentManager().findFragmentById(R.id.root_view) != null) {
            getSupportFragmentManager()
                    .beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.root_view)).commit();
        }
        // network_stream();
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();



        if (doubleBackToExitPressedOnce) {
            network_stream();
            // super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1000);

    }
}