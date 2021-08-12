package com.play.view.videoplayer.hd;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sameer on 5/3/2018.
 */

public class Recent extends AppCompatActivity {
    VideoDetailActivityFliper videoDetailActivityFliper;
    int recentsize, recentpathsize;
    String name,path;
    RecyclerView recyclerView;
    FolderAdapter folderAdapter;
    //DataBaseManager db;
    private AdView mAdView;
    Cursor videoCursorActivity;
    VideoSongsAdapter videoSongsAdapter;
    private InterstitialAd mInterstitial;
    ProgressBar loadingIndicator;
    boolean isActivityIsVisible = true;
    List<String> arrayListfile;
    List<String> arrayListname;
    Cursor c;
    private String filename,title,album;
    Toolbar toolbar;
    Dialog dialog;
    private AdRequest adRequest, ar;
    ArrayList<VideoSongs> videoActivitySongsList2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_videos);
        dialog = new Dialog(Recent.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        dialog.setContentView( getLayoutInflater().inflate( R.layout.custom, null ) );
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
//                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                .build();
        mAdView.loadAd(adRequest);
        ads();
//        if (isOnline()) {
//            if (!Recent.this.isFinishing()) {
//                dialog.show();
//            }
//
//            mInterstitial = null;
//
//
//            Display display = ((WindowManager) getSystemService(getApplication().WINDOW_SERVICE)).getDefaultDisplay();
//            int width = display.getWidth();
//            int height = display.getHeight();
//            Log.v("width", width + "");
//            dialog.getWindow().setLayout((6 * width) / 6, (4 * height) / 11);
//
//            AdRequest adRequestInter = new AdRequest.Builder().build();
//            mInterstitial = new InterstitialAd(getApplicationContext());
//            mInterstitial.setAdUnitId(getResources().getString(R.string.intersitial_as_unit_id));
//
//            mInterstitial.setAdListener(new AdListener() {
//                @Override
//                public void onAdLoaded() {
//                    if (mInterstitial.isLoaded()) {
//                        if (mInterstitial != null) {
//                            loadingIndicator.setVisibility(View.GONE);
//                            dialog.dismiss();
//                            //loadingIndicator.draw();
//                            if (isActivityIsVisible) {
//                                mInterstitial.show();
//                            }
//                        }
//                    }
//                }
//            });
//            mInterstitial.loadAd(adRequestInter);
//        }
        arrayListfile = new ArrayList<String>();
        arrayListname = new ArrayList<String>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.hme8);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        db = new DataBaseManager(getApplicationContext());
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        videoActivitySongsList2 = new ArrayList<VideoSongs>();
        String query ="SELECT * FROM recent";
        /*
        c = db.selectQuery(query);
        Log.e("Insert query", query);
        if (c.getCount() > 0) {
            if (c.moveToFirst() && c != null) {
                do {
                    name = c.getString(c.getColumnIndex("name"));

                    //arrayListname.add(name);

                    path = c.getString(c.getColumnIndex("path"));
                    getFiles(path);
                   // arrayListfile.add(path);
                    {
                        // smsSender();
                        //Toast.makeText(Checkboxx.this,"Total Strength is"+Strength, Toast.LENGTH_SHORT).show();
                    }
                }
                while (c.moveToNext());
            }
        }

*/

        videoDetailActivityFliper = new VideoDetailActivityFliper();
        recyclerView = (RecyclerView) findViewById(R.id.videoRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(videoSongsAdapter);



        videoSongsAdapter = new VideoSongsAdapter(Recent.this, videoActivitySongsList2);
        videoSongsAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(videoSongsAdapter);






    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recent, menu);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (itemId == android.R.id.home) {
            finish();
//
        }
        else if (itemId ==  R.id.setting) {

            Intent i = new Intent(Recent.this,SettingActivity.class);
            i.putExtra("package","recent");
            startActivity(i);
            finish();

        }
        else if (itemId ==  R.id.clear) {
        String delete_query = "DELETE FROM recent";
            Log.e("Insert query", delete_query);
          //  db.delete(delete_query);
            videoSongsAdapter.notifyDataSetChanged();
            Intent i = new Intent(Recent.this,MainActivity_Front.class);
            startActivity(i);

        }
        else if (itemId == R.id.refresh) {
            Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
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
    public void ads() {
        if (isOnline()) {
            if (!Recent.this.isFinishing()){
                dialog.show();
            }

            mInterstitial = null;


            Display display = ((WindowManager) getSystemService(getApplication().WINDOW_SERVICE)).getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            Log.v("width", width + "");
            dialog.getWindow().setLayout((6 * width) / 8, (4 * height) / 15);

            AdRequest adRequestInter = new AdRequest.Builder().build();
            mInterstitial = new InterstitialAd(this);
            mInterstitial.setAdUnitId(getResources().getString(R.string.intersitial_as_unit_id));

            mInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (mInterstitial.isLoaded()) {
                        if (mInterstitial != null) {
                            loadingIndicator.setVisibility(View.GONE);
                            dialog.dismiss();
                            //loadingIndicator.draw();
                            if (isActivityIsVisible) {
                                mInterstitial.show();
                            }
                        }
                    }
                }
            });
            mInterstitial.loadAd(adRequestInter);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
            isActivityIsVisible = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
            isActivityIsVisible = true;
        }



    private void getFiles(String songsName) {


        String selection = MediaStore.Video.VideoColumns.DATA + " like?";
        String[] projection = {MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.ARTIST,
                MediaStore.Video.VideoColumns.RESOLUTION,
                MediaStore.Video.VideoColumns.ALBUM,
                MediaStore.Video.VideoColumns.DESCRIPTION,
                MediaStore.Video.VideoColumns.TITLE,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.SIZE};

        String[] selectionArgs = {"%" + songsName + "%"};
//        String[] selectionArgs=new String[]{"%Swag-Se-Swagat-Song--Tiger-Zinda-Hai--Salman-Khan--Katrina-Kaif.mp4%"};

        Log.i("Files", "Video files" + Arrays.toString(selectionArgs));
        videoCursorActivity = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
//        System.out.println("MAPing "+dirPath + "=media="+ MediaStore.Video.Media.EXTERNAL_CONTENT_URI );

        int totalvideoscount = videoCursorActivity.getCount();
//        folders_list.clear();
        while (videoCursorActivity.moveToNext()) {
            filename = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            title = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String dura = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            String artist = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
            album = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
            String desc = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION));
            String res = videoCursorActivity.getString(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
            int size = videoCursorActivity.getInt(
                    videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            int videoId = videoCursorActivity.getInt(videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            Uri albumArtUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);
            System.out.println(albumArtUri);

            System.out.println("Total SOngs" + totalvideoscount);
            System.out.println("Title" + title);


            VideoSongs songs = new VideoSongs();
            songs.setData(filename);
            songs.setImage(albumArtUri.toString());
            songs.setDuration(milliSecondsToTimer(Long.parseLong(dura)));
            songs.setName(title);
            songs.setArtist(artist);
            songs.setSize(getFileSize(size));
            videoActivitySongsList2.add(songs);
        }


    }
    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

// Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
// Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

// Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

// return timer string
        return finalTimerString;
    }
   /* public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }*/


    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


}
