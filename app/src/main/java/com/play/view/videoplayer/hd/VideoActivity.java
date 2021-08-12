package com.play.view.videoplayer.hd;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.os.Build.VERSION_CODES;

@TargetApi(VERSION_CODES.M)
public class VideoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , SharedPreferences.OnSharedPreferenceChangeListener {

    public int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private AdView mAdView;
    private int video_column_index;
    ListView videolist;
    int count;
    static String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID};
    Cursor videoCursorActivity;
    RecyclerView recyclerView;
    VideoSongsAdapter videoSongsAdapter;
    ArrayList<VideoSongs> videoActivitySongsList = new ArrayList<VideoSongs>();
    private String filename, title, id, albu, artist;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    ProgressDialog prodialog;
    private boolean auto_scan;
    DrawerLayout drawer;
    String pathofvideo;
    boolean doubleBackToExitPressedOnce = false;
    String[] DayOfWeek = {"Music", "Videos", "Audios"};
    ExpandedMenuModel item1, item2;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    boolean mVisible;
    private boolean mVisibleVideo;
    String listPref;
    StringBuilder builder;
    private AlertDialog alertDialog;
    private String resolu;
    Dialog dialog;
    private InterstitialAd mInterstitial;
    private AdRequest adRequest, ar;
    ProgressBar loadingIndicator;
    boolean isActivityIsVisible = true;
    SharedPreferences mSharedPrefs = null;
    private  int brightnessValue;
    private String value1;
    private ContentResolver cResolver;
    private Handler handler,handlerLoader;
    String insert_query,select_query,delete_query;
    Cursor c;
    //DataBaseManager db;
    LinearLayout linearLayout;
    Boolean start = true;
    String dbfilename,dbimage,dbduraton,dbtitle,dbalbum,dbartish,dbsize,dbresol;
    // Get the screen current brightness
//    protected int getScreenBrightness() {
//        /*
//            public static int getInt (ContentResolver cr, String name, int def)
//                Convenience function for retrieving a single system settings value as an integer.
//                Note that internally setting values are always stored as strings; this function
//                converts the string to an integer for you. The default value will be returned
//                if the setting is not defined or not an integer.
//
//            Parameters
//                cr : The ContentResolver to access.
//                name : The name of the setting to retrieve.
//                def : Value to return if the setting is not defined.
//            Returns
//                The setting's current value, or 'def' if it is not defined or not a valid integer.
//        */
//        brightnessValue = Settings.System.getInt(
//                getContentResolver(),
//                Settings.System.SCREEN_BRIGHTNESS, 0);
//        Log.i("", "Sysytem bright" + brightnessValue);
//        return brightnessValue;
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mSharedPrefs = getSharedPreferences("com.azhar.azhar.videoplayer", Context.MODE_PRIVATE);
        //brightnessValue = mSharedPrefs.getInt("ScreenBrightness", 0);
        //setBrightness(brightnessValue);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            //mAdView.pause();
            isActivityIsVisible = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            //mAdView.resume();
            isActivityIsVisible = true;
        }
        mSharedPrefs = getSharedPreferences("com.azhar.azhar.videoplayer", Context.MODE_PRIVATE);
       // brightnessValue = mSharedPrefs.getInt("ScreenBrightness", 0);
       // setBrightness(brightnessValue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            //mAdView.destroy();

        }
        if (mInterstitial != null) {
            mInterstitial = null;
            if(handlerLoader != null && handler != null){
                handler.removeCallbacksAndMessages(null);
                handlerLoader.removeCallbacksAndMessages(null);
            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        linearLayout=findViewById(R.id.main_content);

        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
                //.addTestDevice("190af34e322acedf")
                .build();
        mAdView.loadAd(adRequest);
        //showprogress();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        prodialog = new ProgressDialog(this);
        prodialog = new ProgressDialog(this);
        prodialog.setIndeterminate(false);
        prodialog.setCancelable(false);
        //prodialog = prodialog.show(VideoActivity.this, "", "Loading....", false);

        /*
        db = new DataBaseManager(getApplicationContext());
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        */



        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        dialog = new Dialog(VideoActivity.this);
        // dialog.setTitle(" Please Wait");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView( getLayoutInflater().inflate( R.layout.custom, null ) );

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        auto_scan = sharedPreferences.getBoolean(getResources().getString(com.play.view.videoplayer.hd.R.string.auto_scan_key), true);
        System.out.println("MainActivity Switch State" + auto_scan);
        ;

        toolbar = (Toolbar) findViewById(com.play.view.videoplayer.hd.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.hme8);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Videos");

        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        value1 = mSharedPrefs.getString("value1", "true");



        if (checkStoragePermission()) {
            if (auto_scan) {
                {
                    if (value1.equals("true")) {
                    new GetAudioListAsynkTask(getApplicationContext()).execute((Void) null);
                    mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
                    mEditor1.putString("value1", "false");
                    mEditor1.apply();

                        value1 = mSharedPrefs.getString("value1", "true");
                }
                else {
                        int i = 0;
                        videoActivitySongsList.clear();
                        select_query = "select * from videos";
                        Log.e("select query", select_query);

                        /*
                        c = db.selectQuery(select_query);
                        if (c.getCount() > 0) {
                            if (c.moveToFirst() && c != null) {
                                do {
                                    i++;
                                    if (isOnline()) {
                                        if (i % 10 == 0 || i ==1) {
                                            //linearLayout.setVisibility(View.VISIBLE);
                                            videoActivitySongsList.add(null);
                                        }
                                        else {
                                            dbfilename = c.getString(c.getColumnIndex("filename"));
                                            dbimage = c.getString(c.getColumnIndex("image"));
                                            dbduraton = c.getString(c.getColumnIndex("duration"));
                                            dbtitle = c.getString(c.getColumnIndex("title"));
                                            dbalbum = c.getString(c.getColumnIndex("album"));
                                            dbartish = c.getString(c.getColumnIndex("artist"));
                                            dbsize = c.getString(c.getColumnIndex("size"));
                                            dbresol = c.getString(c.getColumnIndex("resol"));
                                            {
                                                VideoSongs songs = new VideoSongs();
                                                songs.setData(dbfilename);
                                                songs.setImage(dbimage);
                                                songs.setDuration(dbduraton);
                                                songs.setName(dbtitle);
                                                songs.setAlbum(dbalbum);
                                                songs.setArtist(dbartish);
                                                songs.setSize((dbsize));
                                                videoActivitySongsList.add(songs);


                                                //VideoSongs videoSongs = new VideoSongs(dbfilename,dbimage,dbduraton,dbtitle,dbalbum,dbartish,dbsize,dbresol);
                                            }
                                        }
                                    }
                                     else {
                                        dbfilename = c.getString(c.getColumnIndex("filename"));
                                        dbimage = c.getString(c.getColumnIndex("image"));
                                        dbduraton = c.getString(c.getColumnIndex("duration"));
                                        dbtitle = c.getString(c.getColumnIndex("title"));
                                        dbalbum = c.getString(c.getColumnIndex("album"));
                                        dbartish = c.getString(c.getColumnIndex("artist"));
                                        dbsize = c.getString(c.getColumnIndex("size"));
                                        dbresol = c.getString(c.getColumnIndex("resol"));
                                        {
                                            VideoSongs songs = new VideoSongs();
                                            songs.setData(dbfilename);
                                            songs.setImage(dbimage);
                                            songs.setDuration(dbduraton);
                                            songs.setName(dbtitle);
                                            songs.setAlbum(dbalbum);
                                            songs.setArtist(dbartish);
                                            songs.setSize((dbsize));
                                            videoActivitySongsList.add(songs);


                                            //VideoSongs videoSongs = new VideoSongs(dbfilename,dbimage,dbduraton,dbtitle,dbalbum,dbartish,dbsize,dbresol);
                                        }
                                    }
                                }

                                while (c.moveToNext());
                            }

                        }
                        */
                        videoSongsAdapter = new VideoSongsAdapter(this, videoActivitySongsList);
                        recyclerView = (RecyclerView) findViewById(R.id.videoRecycler);
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setHasFixedSize(true);
                        final LinearLayoutManager lm = new LinearLayoutManager(this);
                        lm.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(lm);
                        recyclerView.setAdapter(videoSongsAdapter);
                        recyclerView.getRecycledViewPool().clear();
                        videoSongsAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        else {


            requestStoragePermission();
        }


    }

    private class GetAudioListAsynkTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;

        @Override
        protected void onPreExecute() {

        }

        public GetAudioListAsynkTask(Context context) {

            this.context = context;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            try {

                init_phone_video_grid();
                return true;
            } catch (Exception e) {
                return false;

            }

        }

        @Override
        protected void onPostExecute(Boolean result) {

//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }

        }
    }

    public  void loadvideos()
    {
        {
            int i = 0;
            videoActivitySongsList.clear();
            select_query = "select * from videos";
            Log.e("select query", select_query);

            /*
            c = db.selectQuery(select_query);
            if (c.getCount() > 0) {
                if (c.moveToFirst() && c != null) {
                    do {

                        if (isOnline()) {
                            i++;
                            if (i % 10 == 0 || i ==1) {
                                videoActivitySongsList.add(null);
                            }
                            else {
                                dbfilename = c.getString(c.getColumnIndex("filename"));
                                dbimage = c.getString(c.getColumnIndex("image"));
                                dbduraton = c.getString(c.getColumnIndex("duration"));
                                dbtitle = c.getString(c.getColumnIndex("title"));
                                dbalbum = c.getString(c.getColumnIndex("album"));
                                dbartish = c.getString(c.getColumnIndex("artist"));
                                dbsize = c.getString(c.getColumnIndex("size"));
                                dbresol = c.getString(c.getColumnIndex("resol"));
                                {
                                    VideoSongs songs = new VideoSongs();
                                    songs.setData(dbfilename);
                                    songs.setImage(dbimage);
                                    songs.setDuration(dbduraton);
                                    songs.setName(dbtitle);
                                    songs.setAlbum(dbalbum);
                                    songs.setArtist(dbartish);
                                    songs.setSize((dbsize));
                                    videoActivitySongsList.add(songs);


                                    //VideoSongs videoSongs = new VideoSongs(dbfilename,dbimage,dbduraton,dbtitle,dbalbum,dbartish,dbsize,dbresol);
                                }
                            }
                        }
                        else {
                            dbfilename = c.getString(c.getColumnIndex("filename"));
                            dbimage = c.getString(c.getColumnIndex("image"));
                            dbduraton = c.getString(c.getColumnIndex("duration"));
                            dbtitle = c.getString(c.getColumnIndex("title"));
                            dbalbum = c.getString(c.getColumnIndex("album"));
                            dbartish = c.getString(c.getColumnIndex("artist"));
                            dbsize = c.getString(c.getColumnIndex("size"));
                            dbresol = c.getString(c.getColumnIndex("resol"));
                            {
                                VideoSongs songs = new VideoSongs();
                                songs.setData(dbfilename);
                                songs.setImage(dbimage);
                                songs.setDuration(dbduraton);
                                songs.setName(dbtitle);
                                songs.setAlbum(dbalbum);
                                songs.setArtist(dbartish);
                                songs.setSize((dbsize));
                                videoActivitySongsList.add(songs);


                                //VideoSongs videoSongs = new VideoSongs(dbfilename,dbimage,dbduraton,dbtitle,dbalbum,dbartish,dbsize,dbresol);
                            }
                        }
                    }
                    while (c.moveToNext());
                }
            }
            */

            videoSongsAdapter = new VideoSongsAdapter(this, videoActivitySongsList);
            recyclerView = (RecyclerView) findViewById(R.id.videoRecycler);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(true);
            final LinearLayoutManager lm = new LinearLayoutManager(this);
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(lm);
            recyclerView.setAdapter(videoSongsAdapter);
            recyclerView.getRecycledViewPool().clear();
            videoSongsAdapter.notifyDataSetChanged();

        }
    }

    @SuppressWarnings("deprecation")
    private void init_phone_video_grid() {
        delete_query = "DELETE FROM videos";
        Log.e("delete query", delete_query);
        //db.delete(delete_query);

        String[] proj = {MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.ARTIST,
                MediaStore.Video.VideoColumns.RESOLUTION,
                MediaStore.Video.VideoColumns.ALBUM,
                MediaStore.Video.VideoColumns.DESCRIPTION,
                MediaStore.Video.VideoColumns.TITLE,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.SIZE};
        String sortOrder = MediaStore.Video.VideoColumns.TITLE + " ASC";

        videoCursorActivity = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, null, null, sortOrder);
        count = videoCursorActivity.getCount();
        videoActivitySongsList.clear();

        if (videoCursorActivity != null) {
            while (videoCursorActivity.moveToNext()) {
                filename = videoCursorActivity.getString(
                        videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                title = videoCursorActivity.getString(
                        videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                final String dura = videoCursorActivity.getString(
                        videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                artist = videoCursorActivity.getString(
                        videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                albu = videoCursorActivity.getString(
                        videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                String desc = videoCursorActivity.getString(
                        videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.DESCRIPTION));
                resolu = videoCursorActivity.getString(
                        videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                final long size = videoCursorActivity.getLong(
                        videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

//            long ids = videoCursor.getLong(videoCursor
//                    .getColumnIndex(MediaStore.Video.Media._ID));
//            ContentResolver crThumb = getContentResolver();
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 1;
//            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(
//                    crThumb, ids, MediaStore.Video.Thumbnails.MINI_KIND,
//                    options);

                int videoId = videoCursorActivity.getInt(videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.VideoColumns._ID));
                System.out.println(videoId);
//        getVideoThumbnail(getApplicationContext(),videoId);
//            Uri sArtworkUri = Uri
//                    .parse("content://media/external/view/media");
                final Uri albumArtUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);
                System.out.println(albumArtUri);
//            String time = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//                time = String.format("%02d:%02d",
//                        TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(dura)),
//                        TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(dura)) -
//                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(dura)))
//                );
//            }
                File file = new File(filename);
//            Date lastModDate = new Date(file.lastModified());
//            System.out.println("File last modified @ : " + (lastModDate.toString()));
//            Uri uri = Uri.fromFile(file);
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
//            sendBroadcast(intent);
                System.out.println("FileName" + filename);
                System.out.println(albumArtUri.toString());
                System.out.println("Res" + resolu);
                System.out.println("Total SOngs" + count);
                System.out.println("Title" + title);
                System.out.println("dura" + milliSecondsToTimer(Long.parseLong(dura)));
                System.out.println("artist" + artist);
                System.out.println("album" + albu);
                System.out.println("desc" + desc);

                System.out.println("size" + getFileSize(size));
//            System.out.println("size in MB" + hrSize);


                VideoSongs songs = new VideoSongs();
                songs.setData(filename);
                songs.setImage(albumArtUri.toString());
                songs.setDuration(milliSecondsToTimer(Long.parseLong(dura)));
                songs.setName(title);
                songs.setAlbum(albu);
                songs.setArtist(artist);
                songs.setSize(getFileSize(size));
                videoActivitySongsList.add(songs);
                final String abc = "a";
                {
                    insert_query = " Insert into videos (filename,image,duration,title,album,artist,size,resol) values("+'"'+""+filename+""+'"'+","+'"'+"" +albumArtUri.toString()+ ""+'"'+","+'"'+"" +milliSecondsToTimer(Long.parseLong(dura))+ ""+'"'+","+'"'+"" +title+ ""+'"'+","+'"'+"" +albu+ ""+'"'+","+'"'+"" +artist+ ""+'"'+","+'"'+"" +getFileSize(size)+ ""+'"'+","+'"'+""+abc+""+'"'+")";
                    Log.e("Insert query", insert_query);
                 //   db.insert_update(insert_query);
                }

            }
        }
        videoCursorActivity.close();


    }

    /*public static String getFileSize(long size) {
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

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    private boolean checkStoragePermission() {
        return ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (auto_scan) {
                    {
                        if (value1.equals("true")) {
                            new GetAudioListAsynkTask(getApplicationContext()).execute((Void) null);

                            mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
                            mEditor1.putString("value1", "false");
                            mEditor1.apply();

                            value1 = mSharedPrefs.getString("value1", "true");
                        }
                        else {

                            select_query = "select * from videos";
                            Log.e("select query", select_query);

                            /*
                            c = db.selectQuery(select_query);
                            if (c.getCount() > 0) {
                                if (c.moveToFirst() && c != null) {
                                    do {
                                        dbfilename = c.getString(c.getColumnIndex("filename"));
                                        dbimage = c.getString(c.getColumnIndex("image"));
                                        dbduraton = c.getString(c.getColumnIndex("duration"));
                                        dbtitle = c.getString(c.getColumnIndex("title"));
                                        dbalbum = c.getString(c.getColumnIndex("album"));
                                        dbartish = c.getString(c.getColumnIndex("artist"));
                                        dbsize = c.getString(c.getColumnIndex("size"));
                                        dbresol = c.getString(c.getColumnIndex("resol"));
                                        {
                                            VideoSongs songs = new VideoSongs();
                                            songs.setData(dbfilename);
                                            songs.setImage(dbimage);
                                            songs.setDuration(dbduraton);
                                            songs.setName(dbtitle);
                                            songs.setAlbum(dbalbum);
                                            songs.setArtist(dbartish);
                                            songs.setSize((dbsize));
                                            videoActivitySongsList.add(songs);


                                            //VideoSongs videoSongs = new VideoSongs(dbfilename,dbimage,dbduraton,dbtitle,dbalbum,dbartish,dbsize,dbresol);
                                        }
                                    }
                                    while (c.moveToNext());
                                }
                            }

                            */




                        }
                    }

                } else {
//            if (checkStoragePermission()) {

                    {
                        if (value1.equals("true")) {
                            new GetAudioListAsynkTask(getApplicationContext()).execute((Void) null);
                            mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
                            mEditor1.putString("value1", "false");
                            mEditor1.apply();

                            value1 = mSharedPrefs.getString("value1", "true");
                        }
                        else {

                            select_query = "select * from videos";
                            Log.e("select query", select_query);
                           // c = db.selectQuery(select_query);
                            if (c.getCount() > 0) {
                                if (c.moveToFirst() && c != null) {
                                    do {
                                        dbfilename = c.getString(c.getColumnIndex("filename"));
                                        dbimage = c.getString(c.getColumnIndex("image"));
                                        dbduraton = c.getString(c.getColumnIndex("duration"));
                                        dbtitle = c.getString(c.getColumnIndex("title"));
                                        dbalbum = c.getString(c.getColumnIndex("album"));
                                        dbartish = c.getString(c.getColumnIndex("artist"));
                                        dbsize = c.getString(c.getColumnIndex("size"));
                                        dbresol = c.getString(c.getColumnIndex("resol"));
                                        {
                                            VideoSongs songs = new VideoSongs();
                                            songs.setData(dbfilename);
                                            songs.setImage(dbimage);
                                            songs.setDuration(dbduraton);
                                            songs.setName(dbtitle);
                                            songs.setAlbum(dbalbum);
                                            songs.setArtist(dbartish);
                                            songs.setSize((dbsize));
                                            videoActivitySongsList.add(songs);


                                            //VideoSongs videoSongs = new VideoSongs(dbfilename,dbimage,dbduraton,dbtitle,dbalbum,dbartish,dbsize,dbresol);
                                        }
                                    }
                                    while (c.moveToNext());
                                }
                            }




                        }
                    }
//            } else {
//                requestStoragePermission();
//            }
                }
            } else {
                checkStoragePermission();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
//                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(VideoActivity.this,VideoFolder.class);
                startActivity(i);
                finish();

        }        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            videoSongsAdapter = new VideoSongsAdapter(this, videoActivitySongsList);
            recyclerView.setAdapter(videoSongsAdapter);
            videoSongsAdapter.notifyDataSetChanged();
        }
        else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, VideoSearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else if (id ==  R.id.setting) {

            Intent i = new Intent(VideoActivity.this,SettingActivity.class);
            i.putExtra("package","videoactivity");
            startActivity(i);
            finish();

        }
        else if (id == R.id.refresh) {
            Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.sycn) {
//            delete_query = "DELETE from videos";
//            Log.e("delete query", delete_query);
//            db.delete(delete_query);
           // new GetAudioListAsynkTask(getApplicationContext()).execute((Void) null);
            loadvideos();
            Toast.makeText(getApplicationContext(), "Synchronized", Toast.LENGTH_SHORT).show();
        }

        else if (id ==R.id.delete) {
            select_query = "select * from videopath";
            Log.e("select query", select_query);
            /*
            c = db.selectQuery(select_query);
            if (c.getCount() > 0) {

                if (c.moveToFirst() && c != null) {
                    do {
                        pathofvideo = c.getString(c.getColumnIndex("path"));
                        MediaFileFunctions mediaFileFunctions = new MediaFileFunctions();
                        mediaFileFunctions.deleteViaContentProvider(getApplicationContext(), pathofvideo);
                    }
                    while (c.moveToNext());
                }
                delete_query = "DELETE from videos";
                Log.e("delete query", delete_query);
                db.delete(delete_query);
                new GetAudioListAsynkTask(getApplicationContext()).execute((Void) null);

            }
            */

        }


        return super.onOptionsItemSelected(item);
    }

    void network_stream() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(VideoActivity.this);
        dialogBuilder.setCancelable(false);

// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rate_us_demo, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();


        Button cancel = (Button) dialogView.findViewById(R.id.quit);
        Button ok = (Button) dialogView.findViewById(R.id.rateUsOk);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mazhar.player")));
                alertDialog.dismiss();

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                finish();
            }
        });


        alertDialog.show();

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(VideoActivity.this,VideoFolder.class);
        startActivity(i);
        finish();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_eq) {

        } else if (id == R.id.nav_folders) {

            startActivity(new Intent(this, Main2Activity.class));
            return true;
        } else if (id == R.id.nav_eq) {

//            viewPager.setCurrentItem(0, true);
            FragmentManager fmst = getSupportFragmentManager();
            FragmentTransaction ftst = fmst.beginTransaction();
            AudioFragment st = new AudioFragment();

            ftst.add(R.id.viewpager, st, "Azhar");
            ftst.commit();
            drawer.closeDrawers();
            return true;
        } else if (id == R.id.nav_album) {

//            viewPager.setCurrentItem(2, true);
            FragmentManager fmst = getSupportFragmentManager();
            FragmentTransaction ftst = fmst.beginTransaction();
            AlbumFragment st = new AlbumFragment();

            ftst.add(R.id.viewpager, st, "Azhar");
            ftst.commit();
            drawer.closeDrawers();
            return true;
        } else if (id == R.id.nav_artist) {

//            viewPager.setCurrentItem(3, true);
            FragmentManager fmst = getSupportFragmentManager();
            FragmentTransaction ftst = fmst.beginTransaction();
            ArtistsFragment st = new ArtistsFragment();

            ftst.add(R.id.viewpager, st, "Azhar");
            ftst.commit();
            drawer.closeDrawers();
            return true;
        } else if (id == R.id.nav_genre) {

//            viewPager.setCurrentItem(1, true);
            FragmentManager fmst = getSupportFragmentManager();
            FragmentTransaction ftst = fmst.beginTransaction();
            GenreFragment st = new GenreFragment();
            ftst.add(R.id.viewpager, st, "Azhar");
            ftst.commit();
            drawer.closeDrawers();
            return true;
        } else if (id == R.id.nav_video) {

            startActivity(new Intent(this, VideoActivity.class));
            return true;
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String title = "Share Video  with Freinds";
            String link = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
            intent.putExtra(Intent.EXTRA_SUBJECT, title);
            intent.putExtra(Intent.EXTRA_TEXT, link);
            startActivity(Intent.createChooser(intent, "Share XM videoplayer using"));
        } else if (id == R.id.nav_rate) {
            network_stream();
            return true;
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, About.class));
            return true;
        }
//


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Intent createShareForecastIntent() {
        String textThatYouWantToShare =
                "Lovely Music:";
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle("Share via")
                .setType("text/plain")
                .setText(textThatYouWantToShare)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.hardware_key))) {
            if (listPref.equals(getString(R.string.automatic_value))) {
                System.out.println(getString(R.string.automatic_value));
            } else if (listPref.equals(getString(R.string.disable_value))) {
                System.out.println(getString(R.string.disable_value));
            } else if (listPref.equals(getString(R.string.decoding_value))) {
                System.out.println(getString(R.string.decoding_value));
            } else if (listPref.equals(getString(R.string.full_value))) {
                System.out.println(getString(R.string.full_value));
            }
        }
    }

    public class MediaFileFunctions
    {
        @TargetApi(VERSION_CODES.HONEYCOMB)
        public  boolean deleteViaContentProvider(Context context, String fullname)
        {
            Uri uri=getFileUri(context,fullname);

            if (uri==null)
            {
                return false;
            }

            try
            {
                ContentResolver resolver=context.getContentResolver();

                // change type to image, otherwise nothing will be deleted
                ContentValues contentValues = new ContentValues();
                int media_type = 1;
                contentValues.put("media_type", media_type);
                resolver.update(uri, contentValues, null, null);

                return resolver.delete(uri, null, null) > 0;
            }
            catch (Throwable e)
            {
                return false;
            }
        }

        @TargetApi(VERSION_CODES.HONEYCOMB)
        private  Uri getFileUri(Context context, String fullname)
        {
            // Note: check outside this class whether the OS version is >= 11
            Uri uri = null;
            Cursor cursor = null;
            ContentResolver contentResolver = null;

            try
            {
                contentResolver=context.getContentResolver();
                if (contentResolver == null)
                    return null;

                uri=MediaStore.Files.getContentUri("external");
                String[] projection = new String[2];
                projection[0] = "_id";
                projection[1] = "_data";
                String selection = "_data = ? ";    // this avoids SQL injection
                String[] selectionParams = new String[1];
                selectionParams[0] = fullname;
                String sortOrder = "_id";
                cursor=contentResolver.query(uri, projection, selection, selectionParams, sortOrder);

                if (cursor!=null)
                {
                    try
                    {
                        if (cursor.getCount() > 0) // file present!
                        {
                            cursor.moveToFirst();
                            int dataColumn=cursor.getColumnIndex("_data");
                            String s = cursor.getString(dataColumn);
                            if (!s.equals(fullname))
                                return null;
                            int idColumn = cursor.getColumnIndex("_id");
                            long id = cursor.getLong(idColumn);
                            uri= MediaStore.Files.getContentUri("external",id);
                        }
                        else // file isn't in the media database!
                        {
                            ContentValues contentValues=new ContentValues();
                            contentValues.put("_data",fullname);
                            uri = MediaStore.Files.getContentUri("external");
                            uri = contentResolver.insert(uri,contentValues);
                        }
                    }
                    catch (Throwable e)
                    {
                        uri = null;
                    }
                    finally
                    {
                        cursor.close();
                    }
                }
            }
            catch (Throwable e)
            {
                uri=null;
            }
            return uri;
        }
    }
    public void ads() {
        if (isOnline()) {
            if (!VideoActivity.this.isFinishing()){
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
                    dialog.dismiss();
                    if (mInterstitial != null) {
                        if (mInterstitial.isLoaded()) {

                            loadingIndicator.setVisibility(View.GONE);

                            //loadingIndicator.draw();
                            if (isActivityIsVisible) {
                                {
                                    mInterstitial.show();
                                }
                            }
                        }
                    }
                }
            });
            mInterstitial.loadAd(adRequestInter);
        }
    }

}