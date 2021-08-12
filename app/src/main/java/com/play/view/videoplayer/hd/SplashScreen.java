package com.play.view.videoplayer.hd;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SplashScreen extends AppCompatActivity {
    String insert_query, select_query, delete_query;
    Cursor c;
    private String filename, title, id, albu, artist, vidid, resolu;
    ;
    String dbfilename, dbimage, dbduraton, dbtitle, dbalbum, dbartish, dbsize, dbresol;
    private boolean auto_scan;
    private String album, albumid;
    //DataBaseManager db;
    private String value1;
    String pos;
    String sdcard;
    boolean intentcheck = false;
    private Cursor cursor;
    public static int height = 0;
    public static int width = 0;
    private TextView pathTextView, emptyTextview;
    public static List<String> list2 = null;
    SharedPreferences mSharedPrefs = null;
    ArrayList<VideoSongs> videoActivitySongsList = new ArrayList<VideoSongs>();
    Cursor videoCursorActivity;
    File createFolder;
    InterstitialAd mInterstitialAd;

    List<String> folders_list;
    List<String> folders_path;
    List<String> folders_path2;

    static Context contex;

    List<String> folders_List2;
    int count;
    Boolean a = true;
    String name, name2, name3, name4, name5, name6, name7;
    private File list[] = null;
    private String root_sd;
    private File file, file2, file3, file4, file5, file6, file7;
    public int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    boolean marshmelo;

    /*Check Ads Location Runtime Permission*/
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SplashScreen.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    /**/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        folders_list = new ArrayList<String>();
        folders_List2 = new ArrayList<String>();
        folders_path = new ArrayList<String>();
        folders_path2 = new ArrayList<String>();

        contex = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        marshmelo = mSharedPrefs.getBoolean("marsh", true);


       /*
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            if (marshmelo == true) {
            //    Toast.makeText(this, "Wait A Second", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //BigComputationTask t = new BigComputationTask();
                        //t.execute();
                        new GetAudioListAsynkTask(getApplicationContext()).execute((Void) null);
                        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
                        mEditor1.putString("value1", "false");
                        mEditor1.putBoolean("marsh", false);
                        mEditor1.apply();
                        value1 = mSharedPrefs.getString("value1", "true");
                        Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(SplashScreen.this, VideoFolder.class);
                                startActivity(i);
                                finish();
                            }
                        }, 100);
                    }
                }, 1000);
            }
            else {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Intent i = new Intent(SplashScreen.this, VideoFolder.class);
                        startActivity(i);
                        finish();
                    }
                }, 500);

            }
        }
        else
        {


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            auto_scan = sharedPreferences.getBoolean(getResources().getString(R.string.auto_scan_key), true);
            System.out.println("MainActivity Switch State" + auto_scan);
            mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            value1 = mSharedPrefs.getString("value1", "true");

            if (checkStoragePermission() && checkStoragePermission1()) {

//            BigComputationTask t = new BigComputationTask();
//            t.execute();
////            if (auto_scan)
//            {
//                {
////                    if (value1.equals("true"))
//                    {
//                        new GetAudioListAsynkTask(getApplicationContext()).execute((Void) null);
//                        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
//                        SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
//                        mEditor1.putString("value1", "false");
//                        mEditor1.apply();
//                        value1 = mSharedPrefs.getString("value1", "true");
//                        Intent i = new Intent(SplashScreen.this, MainActivity_Front.class);
//                        startActivity(i);
//                        finish();
//                    }
//                }
//            }
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Intent i = new Intent(SplashScreen.this, VideoFolder.class);
                        startActivity(i);
                        finish();
                    }
                }, 500);

            } else {
                requestStoragePermission();
            }
        }

        */

        try {


            createFolder = new File(Environment.getExternalStorageDirectory(), ".thumbs");
            if (!createFolder.exists()) {
                createFolder.mkdir();
            }

            System.out.println("create directory " + createFolder.getPath());

        } catch (Exception g) {

            System.out.println("create directory error " + g.getMessage());

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (checkStoragePermission() && checkStoragePermission1()) {

                System.out.println("onRequestPermissionsResult before " + AppClass.mVideoPathCache.size());

                mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);


                try {


                    new BigComputationTask3().execute();


                } catch (Exception d) {

                }
            } else {

                requestStoragePermission();
            }
        } else {
            new BigComputationTask3().execute();
        }


    }


    @Override
    protected void onResume() {


        super.onResume();
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


    @Override
    protected void onDestroy() {


        super.onDestroy();
    }


    private boolean checkStoragePermission() {

        return ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


    }

    private boolean checkStoragePermission1() {

        return ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


    }

    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


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

                //init_phone_video_grid();


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

    @SuppressWarnings("deprecation")
    private void init_phone_video_grid() {
        delete_query = "DELETE FROM videos";
        Log.e("delete query", delete_query);
        //  db.delete(delete_query);

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
                    insert_query = " Insert into videos (filename,image,duration,title,album,artist,size,resol) values(" + '"' + "" + filename + "" + '"' + "," + '"' + "" + albumArtUri.toString() + "" + '"' + "," + '"' + "" + milliSecondsToTimer(Long.parseLong(dura)) + "" + '"' + "," + '"' + "" + title + "" + '"' + "," + '"' + "" + albu + "" + '"' + "," + '"' + "" + artist + "" + '"' + "," + '"' + "" + getFileSize(size) + "" + '"' + "," + '"' + "" + abc + "" + '"' + ")";
                    Log.e("Insert query", insert_query);
                    //db.insert_update(insert_query);
                }
            }
        }
        videoCursorActivity.close();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            intentcheck = true;
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                {
                    {
                        {
                            System.out.println("onRequestPermissionsResult real ");
                            new GetAudioListAsynkTask(getApplicationContext()).execute((Void) null);
                            mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
                            mEditor1.putString("value1", "false");
                            mEditor1.putString("firstitemp", "true");
                            mEditor1.apply();
                            value1 = mSharedPrefs.getString("value1", "true");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    root_sd = Environment.getExternalStorageDirectory().getAbsolutePath();
                                    getStoragePath();
                                    File tt = getStoragePath();
                                    sdcard = tt.toString();
                                    if (sdcard.equalsIgnoreCase(root_sd)) {
                                        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                                        SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
                                        mEditor1.putBoolean("checksdcard", false);
                                        mEditor1.apply();
                                    } else {
                                        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                                        SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
                                        mEditor1.putBoolean("checksdcard", true);
                                        mEditor1.apply();
                                    }
                                }
                            }, 500);
                            new BigComputationTask3().execute();
                        }
                    }

                }

            } else {
                checkStoragePermission();
                checkStoragePermission1();
            }
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /*public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }*/

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


    private int getAudioFileCount(String dirPath) {


        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] projection = {MediaStore.Video.Media.DATA,};
        String[] selectionArgs = {dirPath + "%"};
        Log.i("Folders", "Video Folders" + Arrays.toString(selectionArgs));
        String sortOrder = MediaStore.Video.Media.TITLE + " ASC";
        try {


            cursor = getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder);

            if (cursor != null) {
                return cursor.getCount();
            }


        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor.getInt(0);
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
            videoActivitySongsList.add(songs);
            final String abc = "a";

            insert_query = " Insert into diffvideos (filename,image,duration,title,album,artist,size,resol,position) values('" + filename + "','" + albumArtUri.toString() + "','" + milliSecondsToTimer(Long.parseLong(dura)) + "','" + title + "','" + abc + "','" + artist + "','" + getFileSize(size) + "','" + abc + "','" + pos + "')";
            Log.e("Insert query", insert_query);
            // db.insert_update(insert_query);


        }


    }


    private void showData() {
        getStoragePath();
        File tt = getStoragePath();
        if (tt != null) {

            File s1 = tt;
            sdcard = s1.toString();
            root_sd = Environment.getExternalStorageDirectory().getAbsolutePath();

//            root_sd = "storage/emulated/0/WhatsApp/media";
            Log.i("", "System Memory:" + root_sd);
        }
        //pathTextView.setText("Folders");
        file = new File(String.valueOf(sdcard));
        list = file.listFiles(new VideoFolder.AudioFilter());
        if (sdcard.equalsIgnoreCase(root_sd)) {
            //Toast.makeText(this, "No SD Card", Toast.LENGTH_SHORT).show();
        } else {

            if (list != null) {
                Log.e("Size of list ", "" + list.length);
                //folders_list.clear();
                for (int i = 0; i < list.length; i++) {
                    //dialog1.show();
//            if (list[i].isHidden()) {
//                System.out.println("hidden path files.." + list[i].getAbsolutePath());
//            }
//           if(list[i].isDirectory()) {
                    int count = getAudioFileCount(String.valueOf(list[i].getAbsolutePath()));
                    Log.e("Count : " + count, list[i].getAbsolutePath());
                    Log.i("Counts of Data", String.valueOf(count));

                    name = list[i].getName();
                    file = new File(String.valueOf(list[i]));
                    if (count > 0) {
                        File[] aaa = list[i].listFiles(new VideoFolder.AudioFilter());
                        int xx = aaa.length;
                        for (int x = 0; x < aaa.length; x++) {
                            // xx--;
                            name2 = aaa[x].getName();
                            file2 = new File(String.valueOf(aaa[x]));
                            String filenameArray2[] = name2.split("\\.");
                            String extension2 = filenameArray2[filenameArray2.length - 1];
                            int count2 = getAudioFileCount(String.valueOf(aaa[x].getAbsolutePath()));
                            if (name2.endsWith(".mp4")
                                    || name2.endsWith(".mp3")
                                    || name2.endsWith(".mpeg")
                                    || name2.endsWith(".3gpp")
                                    || name2.endsWith(".avi")
                                    || name2.endsWith(".rm")
                                    || name2.endsWith(".au")
                                    || name2.endsWith(".aac")
                                    || name2.endsWith(".asf")
                                    || name2.endsWith(".aif")
                                    || name2.endsWith(".aiff")
                                    || name2.endsWith(".aifc")
                                    || name2.endsWith(".rmvb")
                                    || name2.endsWith(".amv")
                                    || name2.endsWith(".vob")
                                    || name2.endsWith(".yuv")
                                    || name2.endsWith(".qt")
                                    || name2.endsWith(".midi")
                                    || name2.endsWith(".mid")
                                    || name2.endsWith(".m4p")
                                    || name2.endsWith(".m4v")
                                    || name2.endsWith(".mpg")
                                    || name2.endsWith(".mpeg")
                                    || name2.endsWith(".mxf")
                                    || name2.endsWith(".m2v")
                                    || name2.endsWith(".kar")
                                    || name2.endsWith(".mkv")
                                    || name2.endsWith(".oga")
                                    || name2.endsWith(".ogg")
                                    || name2.endsWith(".ogv")
                                    || name2.endsWith(".f4b")
                                    || name2.endsWith(".f4a")
                                    || name2.endsWith(".f4p")
                                    || name2.endsWith(".f4v")
                                    || name2.endsWith(".flac")
                                    || name2.endsWith(".flv")
                                    || name2.endsWith(".webm")
                                    || name2.endsWith(".wmv")
                                    || name2.endsWith(".wav")
                                    || name2.endsWith(".3g2")
                                    || name2.endsWith(".3gpp")
                                    || name2.endsWith(".3gp")
                                    || name2.endsWith(".mov")
                                    || name2.endsWith(".m4a")
                                    || name2.endsWith(".mng")
                                    || name2.endsWith(".mp2")
                                    || name2.endsWith(".mga")
                                    || name2.endsWith(".spx")
                                    || name2.endsWith(".svi")
                                    || name2.endsWith(".nsv")
                                    || name2.endsWith(".snd")
                                    || name2.endsWith(".MP4")
                                    || name2.endsWith(".mkv")) {

                                x = aaa.length;
                                // condition = true;
                            }
                            if (count2 >= 1 && extension2.equals(name2)) {


                                File[] bbb = aaa[x].listFiles(new VideoFolder.AudioFilter());
                                int yy = bbb.length;
                                for (int y = 0; y < bbb.length; y++) {
                                    //yy--;
                                    name3 = bbb[y].getName();
                                    file3 = new File(String.valueOf(bbb[y]));
                                    String filenameArray3[] = name3.split("\\.");
                                    String extension3 = filenameArray3[filenameArray3.length - 1];
                                    int count3 = getAudioFileCount(String.valueOf(bbb[y].getAbsolutePath()));
                                    if (name3.endsWith(".mp4")
                                            || name3.endsWith(".mp3")
                                            || name3.endsWith(".mpeg")
                                            || name3.endsWith(".3gpp")
                                            || name3.endsWith(".avi")
                                            || name3.endsWith(".rm")
                                            || name3.endsWith(".au")
                                            || name3.endsWith(".aac")
                                            || name3.endsWith(".asf")
                                            || name3.endsWith(".aif")
                                            || name3.endsWith(".aiff")
                                            || name3.endsWith(".aifc")
                                            || name3.endsWith(".rmvb")
                                            || name3.endsWith(".amv")
                                            || name3.endsWith(".vob")
                                            || name3.endsWith(".yuv")
                                            || name3.endsWith(".qt")
                                            || name3.endsWith(".midi")
                                            || name3.endsWith(".mid")
                                            || name3.endsWith(".m4p")
                                            || name3.endsWith(".m4v")
                                            || name3.endsWith(".mpg")
                                            || name3.endsWith(".mpeg")
                                            || name3.endsWith(".mxf")
                                            || name3.endsWith(".m2v")
                                            || name3.endsWith(".kar")
                                            || name3.endsWith(".mkv")
                                            || name3.endsWith(".oga")
                                            || name3.endsWith(".ogg")
                                            || name3.endsWith(".ogv")
                                            || name3.endsWith(".f4b")
                                            || name3.endsWith(".f4a")
                                            || name3.endsWith(".f4p")
                                            || name3.endsWith(".f4v")
                                            || name3.endsWith(".flac")
                                            || name3.endsWith(".flv")
                                            || name3.endsWith(".webm")
                                            || name3.endsWith(".wmv")
                                            || name3.endsWith(".wav")
                                            || name3.endsWith(".3g2")
                                            || name3.endsWith(".3gpp")
                                            || name3.endsWith(".3gp")
                                            || name3.endsWith(".mov")
                                            || name3.endsWith(".m4a")
                                            || name3.endsWith(".mng")
                                            || name3.endsWith(".mp2")
                                            || name3.endsWith(".mga")
                                            || name3.endsWith(".spx")
                                            || name3.endsWith(".svi")
                                            || name3.endsWith(".nsv")
                                            || name3.endsWith(".snd")
                                            || name3.endsWith(".MP4")
                                            || name3.endsWith(".mkv")) {

                                        y = bbb.length;
                                        //condition = true;
                                    }
                                    if (count3 >= 1 && extension3.equals(name3)) {
                                        File[] ccc = bbb[y].listFiles(new VideoFolder.AudioFilter());
                                        int zz = ccc.length;
                                        for (int z = 0; z < ccc.length; z++) {
                                            //zz--;
                                            name4 = ccc[z].getName();
                                            file4 = new File(String.valueOf(ccc[z]));
                                            String filenameArray4[] = name4.split("\\.");
                                            String extension4 = filenameArray4[filenameArray4.length - 1];
                                            int count4 = getAudioFileCount(String.valueOf(ccc[z].getAbsolutePath()));

                                            if (name4.endsWith(".mp4")
                                                    || name4.endsWith(".mp3")
                                                    || name4.endsWith(".mpeg")
                                                    || name4.endsWith(".3gpp")
                                                    || name4.endsWith(".avi")
                                                    || name4.endsWith(".rm")
                                                    || name4.endsWith(".au")
                                                    || name4.endsWith(".aac")
                                                    || name4.endsWith(".asf")
                                                    || name4.endsWith(".aif")
                                                    || name4.endsWith(".aiff")
                                                    || name4.endsWith(".aifc")
                                                    || name4.endsWith(".rmvb")
                                                    || name4.endsWith(".amv")
                                                    || name4.endsWith(".vob")
                                                    || name4.endsWith(".yuv")
                                                    || name4.endsWith(".qt")
                                                    || name4.endsWith(".midi")
                                                    || name4.endsWith(".mid")
                                                    || name4.endsWith(".m4p")
                                                    || name4.endsWith(".m4v")
                                                    || name4.endsWith(".mpg")
                                                    || name4.endsWith(".mpeg")
                                                    || name4.endsWith(".mxf")
                                                    || name4.endsWith(".m2v")
                                                    || name4.endsWith(".kar")
                                                    || name4.endsWith(".mkv")
                                                    || name4.endsWith(".oga")
                                                    || name4.endsWith(".ogg")
                                                    || name4.endsWith(".ogv")
                                                    || name4.endsWith(".f4b")
                                                    || name4.endsWith(".f4a")
                                                    || name4.endsWith(".f4p")
                                                    || name4.endsWith(".f4v")
                                                    || name4.endsWith(".flac")
                                                    || name4.endsWith(".flv")
                                                    || name4.endsWith(".webm")
                                                    || name4.endsWith(".wmv")
                                                    || name4.endsWith(".wav")
                                                    || name4.endsWith(".3g2")
                                                    || name4.endsWith(".3gpp")
                                                    || name4.endsWith(".3gp")
                                                    || name4.endsWith(".mov")
                                                    || name4.endsWith(".m4a")
                                                    || name4.endsWith(".mng")
                                                    || name4.endsWith(".mp2")
                                                    || name4.endsWith(".mga")
                                                    || name4.endsWith(".spx")
                                                    || name4.endsWith(".svi")
                                                    || name4.endsWith(".nsv")
                                                    || name4.endsWith(".snd")
                                                    || name4.endsWith(".MP4")
                                                    || name4.endsWith(".mkv")) {

                                                z = ccc.length;
                                                //condition = true;
                                            }


                                            if (count4 >= 1 && extension4.equals(name4)) {
                                                File[] ddd = ccc[z].listFiles(new VideoFolder.AudioFilter());
                                                int aa = ddd.length;
                                                for (int a = 0; a < ddd.length; a++) {
                                                    //aa--;
                                                    name5 = ddd[z].getName();
                                                    file5 = new File(String.valueOf(ddd[a]));
                                                    String filenameArray5[] = name5.split("\\.");
                                                    String extension5 = filenameArray5[filenameArray5.length - 1];
                                                    int count5 = getAudioFileCount(String.valueOf(ddd[a].getAbsolutePath()));
                                                    if (name5.endsWith(".mp4")
                                                            || name5.endsWith(".mp3")
                                                            || name5.endsWith(".mpeg")
                                                            || name5.endsWith(".3gpp")
                                                            || name5.endsWith(".avi")
                                                            || name5.endsWith(".rm")
                                                            || name5.endsWith(".au")
                                                            || name5.endsWith(".aac")
                                                            || name5.endsWith(".asf")
                                                            || name5.endsWith(".aif")
                                                            || name5.endsWith(".aiff")
                                                            || name5.endsWith(".aifc")
                                                            || name5.endsWith(".rmvb")
                                                            || name5.endsWith(".amv")
                                                            || name5.endsWith(".vob")
                                                            || name5.endsWith(".yuv")
                                                            || name5.endsWith(".qt")
                                                            || name5.endsWith(".midi")
                                                            || name5.endsWith(".mid")
                                                            || name5.endsWith(".m4p")
                                                            || name5.endsWith(".m4v")
                                                            || name5.endsWith(".mpg")
                                                            || name5.endsWith(".mpeg")
                                                            || name5.endsWith(".mxf")
                                                            || name5.endsWith(".m2v")
                                                            || name5.endsWith(".kar")
                                                            || name5.endsWith(".mkv")
                                                            || name5.endsWith(".oga")
                                                            || name5.endsWith(".ogg")
                                                            || name5.endsWith(".ogv")
                                                            || name5.endsWith(".f4b")
                                                            || name5.endsWith(".f4a")
                                                            || name5.endsWith(".f4p")
                                                            || name5.endsWith(".f4v")
                                                            || name5.endsWith(".flac")
                                                            || name5.endsWith(".flv")
                                                            || name5.endsWith(".webm")
                                                            || name5.endsWith(".wmv")
                                                            || name5.endsWith(".wav")
                                                            || name5.endsWith(".3g2")
                                                            || name5.endsWith(".3gpp")
                                                            || name5.endsWith(".3gp")
                                                            || name5.endsWith(".mov")
                                                            || name5.endsWith(".m4a")
                                                            || name5.endsWith(".mng")
                                                            || name5.endsWith(".mp2")
                                                            || name5.endsWith(".mga")
                                                            || name5.endsWith(".spx")
                                                            || name5.endsWith(".svi")
                                                            || name5.endsWith(".nsv")
                                                            || name5.endsWith(".snd")
                                                            || name5.endsWith(".MP4")
                                                            || name5.endsWith(".mkv")) {

                                                        a = ddd.length;
                                                        //condition = true;
                                                    }
                                                    if (count5 > 1 && extension5.equals(name5)) {
                                                        File[] eee = ddd[a].listFiles(new VideoFolder.AudioFilter());
                                                        int bb = eee.length;
                                                        for (int b = 0; b < eee.length; b++) {
                                                            // bb--;
                                                            name6 = eee[b].getName();
                                                            file6 = new File(String.valueOf(eee[b]));
                                                            int count6 = getAudioFileCount(String.valueOf(eee[b].getAbsolutePath()));

                                                            //recyclerviewdata(name5, name4, count4, file4);
                                                        }
                                                    } else if (aa == ddd.length) {


                                                        recyclerviewdata(name5, name4, count4, file4);
                                                    }

                                                    //recyclerviewdata(name5, name4, count4, file4);
                                                }
                                            } else if (zz == ccc.length) {

                                                recyclerviewdata(name4, name3, count3, file3);
                                            }
                                            //recyclerviewdata(name4, name3, count3, file3);
                                        }
                                    } else if (yy == bbb.length) {
                                        recyclerviewdata(name3, name2, count2, file2);
                                    }
                                    //recyclerviewdata(name3, name2, count2, file2);
                                }

                            } else if (xx == aaa.length) {
                                recyclerviewdata(name2, name, count, file);
                            }
                            //recyclerviewdata(name2, name, count, file);
                        }
                    } else {
                        // recyclerviewdata(name, name, count, file);
                    }

                }

                // emptyTextview.setVisibility(View.GONE);
            }
        }


    }

    private void showData2() {
        delete_query = "DELETE FROM folders";
        Log.e("delete query", delete_query);
        //db.delete(delete_query);


        if (root_sd == null) {

            root_sd = Environment.getExternalStorageDirectory().getAbsolutePath();

//            root_sd = "storage/emulated/0/WhatsApp/media";
            Log.i("", "System Memory:" + root_sd);
        }
        //pathTextView.setText("Folders");
        file = new File(String.valueOf(root_sd));
        list = file.listFiles(new VideoFolder.AudioFilter());
        if (list != null) {
            Log.e("Size of list ", "" + list.length);
            folders_list.clear();
            for (int i = 0; i < list.length; i++) {
                //dialog1.show();
//            if (list[i].isHidden()) {
//                System.out.println("hidden path files.." + list[i].getAbsolutePath());
//            }
//           if(list[i].isDirectory()) {
                int count = getAudioFileCount(String.valueOf(list[i].getAbsolutePath()));
                name = list[i].getName();
                String filenameArray[] = name.split("\\.");
                String extension = filenameArray[filenameArray.length - 1];
                file = new File(String.valueOf(list[i]));
                if (count > 0 && extension.equals(name)) {
                    File[] aaa = list[i].listFiles(new VideoFolder.AudioFilter());
                    int xx = aaa.length;
                    for (int x = 0; x < aaa.length; x++) {
                        // xx--;
                        name2 = aaa[x].getName();
                        file2 = new File(String.valueOf(aaa[x]));
                        String filenameArray2[] = name2.split("\\.");
                        String extension2 = filenameArray2[filenameArray2.length - 1];
                        int count2 = getAudioFileCount(String.valueOf(aaa[x].getAbsolutePath()));
                        if (name2.endsWith(".mp4")
                                || name2.endsWith(".mp3")
                                || name2.endsWith(".mpeg")
                                || name2.endsWith(".3gpp")
                                || name2.endsWith(".avi")
                                || name2.endsWith(".rm")
                                || name2.endsWith(".au")
                                || name2.endsWith(".aac")
                                || name2.endsWith(".asf")
                                || name2.endsWith(".aif")
                                || name2.endsWith(".aiff")
                                || name2.endsWith(".aifc")
                                || name2.endsWith(".rmvb")
                                || name2.endsWith(".amv")
                                || name2.endsWith(".vob")
                                || name2.endsWith(".yuv")
                                || name2.endsWith(".qt")
                                || name2.endsWith(".midi")
                                || name2.endsWith(".mid")
                                || name2.endsWith(".m4p")
                                || name2.endsWith(".m4v")
                                || name2.endsWith(".mpg")
                                || name2.endsWith(".mpeg")
                                || name2.endsWith(".mxf")
                                || name2.endsWith(".m2v")
                                || name2.endsWith(".kar")
                                || name2.endsWith(".mkv")
                                || name2.endsWith(".oga")
                                || name2.endsWith(".ogg")
                                || name2.endsWith(".ogv")
                                || name2.endsWith(".f4b")
                                || name2.endsWith(".f4a")
                                || name2.endsWith(".f4p")
                                || name2.endsWith(".f4v")
                                || name2.endsWith(".flac")
                                || name2.endsWith(".flv")
                                || name2.endsWith(".webm")
                                || name2.endsWith(".wmv")
                                || name2.endsWith(".wav")
                                || name2.endsWith(".3g2")
                                || name2.endsWith(".3gpp")
                                || name2.endsWith(".3gp")
                                || name2.endsWith(".mov")
                                || name2.endsWith(".m4a")
                                || name2.endsWith(".mng")
                                || name2.endsWith(".mp2")
                                || name2.endsWith(".mga")
                                || name2.endsWith(".spx")
                                || name2.endsWith(".svi")
                                || name2.endsWith(".nsv")
                                || name2.endsWith(".snd")
                                || name2.endsWith(".MP4")
                                || name2.endsWith(".mkv")) {

                            x = aaa.length;
                            // condition = true;
                        }
                        if (count2 >= 1 && extension2.equals(name2)) {


                            File[] bbb = aaa[x].listFiles(new VideoFolder.AudioFilter());
                            int yy = bbb.length;
                            for (int y = 0; y < bbb.length; y++) {
                                //yy--;
                                name3 = bbb[y].getName();
                                file3 = new File(String.valueOf(bbb[y]));
                                String filenameArray3[] = name3.split("\\.");
                                String extension3 = filenameArray3[filenameArray3.length - 1];
                                int count3 = getAudioFileCount(String.valueOf(bbb[y].getAbsolutePath()));
                                if (name3.endsWith(".mp4")
                                        || name3.endsWith(".mp3")
                                        || name3.endsWith(".mpeg")
                                        || name3.endsWith(".3gpp")
                                        || name3.endsWith(".avi")
                                        || name3.endsWith(".rm")
                                        || name3.endsWith(".au")
                                        || name3.endsWith(".aac")
                                        || name3.endsWith(".asf")
                                        || name3.endsWith(".aif")
                                        || name3.endsWith(".aiff")
                                        || name3.endsWith(".aifc")
                                        || name3.endsWith(".rmvb")
                                        || name3.endsWith(".amv")
                                        || name3.endsWith(".vob")
                                        || name3.endsWith(".yuv")
                                        || name3.endsWith(".qt")
                                        || name3.endsWith(".midi")
                                        || name3.endsWith(".mid")
                                        || name3.endsWith(".m4p")
                                        || name3.endsWith(".m4v")
                                        || name3.endsWith(".mpg")
                                        || name3.endsWith(".mpeg")
                                        || name3.endsWith(".mxf")
                                        || name3.endsWith(".m2v")
                                        || name3.endsWith(".kar")
                                        || name3.endsWith(".mkv")
                                        || name3.endsWith(".oga")
                                        || name3.endsWith(".ogg")
                                        || name3.endsWith(".ogv")
                                        || name3.endsWith(".f4b")
                                        || name3.endsWith(".f4a")
                                        || name3.endsWith(".f4p")
                                        || name3.endsWith(".f4v")
                                        || name3.endsWith(".flac")
                                        || name3.endsWith(".flv")
                                        || name3.endsWith(".webm")
                                        || name3.endsWith(".wmv")
                                        || name3.endsWith(".wav")
                                        || name3.endsWith(".3g2")
                                        || name3.endsWith(".3gpp")
                                        || name3.endsWith(".3gp")
                                        || name3.endsWith(".mov")
                                        || name3.endsWith(".m4a")
                                        || name3.endsWith(".mng")
                                        || name3.endsWith(".mp2")
                                        || name3.endsWith(".mga")
                                        || name3.endsWith(".spx")
                                        || name3.endsWith(".svi")
                                        || name3.endsWith(".nsv")
                                        || name3.endsWith(".snd")
                                        || name3.endsWith(".MP4")
                                        || name3.endsWith(".mkv")) {

                                    y = bbb.length;
                                    //condition = true;
                                }
                                if (count3 >= 1 && extension3.equals(name3)) {
                                    File[] ccc = bbb[y].listFiles(new VideoFolder.AudioFilter());
                                    int zz = ccc.length;
                                    for (int z = 0; z < ccc.length; z++) {
                                        //zz--;
                                        name4 = ccc[z].getName();
                                        file4 = new File(String.valueOf(ccc[z]));
                                        String filenameArray4[] = name4.split("\\.");
                                        String extension4 = filenameArray4[filenameArray4.length - 1];
                                        int count4 = getAudioFileCount(String.valueOf(ccc[z].getAbsolutePath()));

                                        if (name4.endsWith(".mp4")
                                                || name4.endsWith(".mp3")
                                                || name4.endsWith(".mpeg")
                                                || name4.endsWith(".3gpp")
                                                || name4.endsWith(".avi")
                                                || name4.endsWith(".rm")
                                                || name4.endsWith(".au")
                                                || name4.endsWith(".aac")
                                                || name4.endsWith(".asf")
                                                || name4.endsWith(".aif")
                                                || name4.endsWith(".aiff")
                                                || name4.endsWith(".aifc")
                                                || name4.endsWith(".rmvb")
                                                || name4.endsWith(".amv")
                                                || name4.endsWith(".vob")
                                                || name4.endsWith(".yuv")
                                                || name4.endsWith(".qt")
                                                || name4.endsWith(".midi")
                                                || name4.endsWith(".mid")
                                                || name4.endsWith(".m4p")
                                                || name4.endsWith(".m4v")
                                                || name4.endsWith(".mpg")
                                                || name4.endsWith(".mpeg")
                                                || name4.endsWith(".mxf")
                                                || name4.endsWith(".m2v")
                                                || name4.endsWith(".kar")
                                                || name4.endsWith(".mkv")
                                                || name4.endsWith(".oga")
                                                || name4.endsWith(".ogg")
                                                || name4.endsWith(".ogv")
                                                || name4.endsWith(".f4b")
                                                || name4.endsWith(".f4a")
                                                || name4.endsWith(".f4p")
                                                || name4.endsWith(".f4v")
                                                || name4.endsWith(".flac")
                                                || name4.endsWith(".flv")
                                                || name4.endsWith(".webm")
                                                || name4.endsWith(".wmv")
                                                || name4.endsWith(".wav")
                                                || name4.endsWith(".3g2")
                                                || name4.endsWith(".3gpp")
                                                || name4.endsWith(".3gp")
                                                || name4.endsWith(".mov")
                                                || name4.endsWith(".m4a")
                                                || name4.endsWith(".mng")
                                                || name4.endsWith(".mp2")
                                                || name4.endsWith(".mga")
                                                || name4.endsWith(".spx")
                                                || name4.endsWith(".svi")
                                                || name4.endsWith(".nsv")
                                                || name4.endsWith(".snd")
                                                || name4.endsWith(".MP4")
                                                || name4.endsWith(".mkv")) {

                                            z = ccc.length;
                                            //condition = true;
                                        }


                                        if (count4 >= 1 && extension4.equals(name4)) {
                                            File[] ddd = ccc[z].listFiles(new VideoFolder.AudioFilter());
                                            int aa = ddd.length;
                                            for (int a = 0; a < ddd.length; a++) {
                                                //aa--;
                                                name5 = ddd[z].getName();
                                                file5 = new File(String.valueOf(ddd[a]));
                                                String filenameArray5[] = name5.split("\\.");
                                                String extension5 = filenameArray5[filenameArray5.length - 1];
                                                int count5 = getAudioFileCount(String.valueOf(ddd[a].getAbsolutePath()));
                                                if (name5.endsWith(".mp4")
                                                        || name5.endsWith(".mp3")
                                                        || name5.endsWith(".mpeg")
                                                        || name5.endsWith(".3gpp")
                                                        || name5.endsWith(".avi")
                                                        || name5.endsWith(".rm")
                                                        || name5.endsWith(".au")
                                                        || name5.endsWith(".aac")
                                                        || name5.endsWith(".asf")
                                                        || name5.endsWith(".aif")
                                                        || name5.endsWith(".aiff")
                                                        || name5.endsWith(".aifc")
                                                        || name5.endsWith(".rmvb")
                                                        || name5.endsWith(".amv")
                                                        || name5.endsWith(".vob")
                                                        || name5.endsWith(".yuv")
                                                        || name5.endsWith(".qt")
                                                        || name5.endsWith(".midi")
                                                        || name5.endsWith(".mid")
                                                        || name5.endsWith(".m4p")
                                                        || name5.endsWith(".m4v")
                                                        || name5.endsWith(".mpg")
                                                        || name5.endsWith(".mpeg")
                                                        || name5.endsWith(".mxf")
                                                        || name5.endsWith(".m2v")
                                                        || name5.endsWith(".kar")
                                                        || name5.endsWith(".mkv")
                                                        || name5.endsWith(".oga")
                                                        || name5.endsWith(".ogg")
                                                        || name5.endsWith(".ogv")
                                                        || name5.endsWith(".f4b")
                                                        || name5.endsWith(".f4a")
                                                        || name5.endsWith(".f4p")
                                                        || name5.endsWith(".f4v")
                                                        || name5.endsWith(".flac")
                                                        || name5.endsWith(".flv")
                                                        || name5.endsWith(".webm")
                                                        || name5.endsWith(".wmv")
                                                        || name5.endsWith(".wav")
                                                        || name5.endsWith(".3g2")
                                                        || name5.endsWith(".3gpp")
                                                        || name5.endsWith(".3gp")
                                                        || name5.endsWith(".mov")
                                                        || name5.endsWith(".m4a")
                                                        || name5.endsWith(".mng")
                                                        || name5.endsWith(".mp2")
                                                        || name5.endsWith(".mga")
                                                        || name5.endsWith(".spx")
                                                        || name5.endsWith(".svi")
                                                        || name5.endsWith(".nsv")
                                                        || name5.endsWith(".snd")
                                                        || name5.endsWith(".MP4")
                                                        || name5.endsWith(".mkv")) {

                                                    a = ddd.length;
                                                    //condition = true;
                                                }
                                                if (count5 > 1 && extension5.equals(name5)) {
                                                    File[] eee = ddd[a].listFiles(new VideoFolder.AudioFilter());
                                                    int bb = eee.length;
                                                    for (int b = 0; b < eee.length; b++) {
                                                        // bb--;
                                                        name6 = eee[b].getName();
                                                        file6 = new File(String.valueOf(eee[b]));
                                                        int count6 = getAudioFileCount(String.valueOf(eee[b].getAbsolutePath()));

                                                        //recyclerviewdata(name5, name4, count4, file4);
                                                    }
                                                } else if (aa == ddd.length) {


                                                    recyclerviewdata(name5, name4, count4, file4);
                                                }

                                                //recyclerviewdata(name5, name4, count4, file4);
                                            }
                                        } else if (zz == ccc.length) {

                                            recyclerviewdata(name4, name3, count3, file3);
                                        }
                                        //recyclerviewdata(name4, name3, count3, file3);
                                    }
                                } else if (yy == bbb.length) {
                                    recyclerviewdata(name3, name2, count2, file2);
                                }
                                //recyclerviewdata(name3, name2, count2, file2);
                            }

                        } else if (xx == aaa.length) {
                            recyclerviewdata(name2, name, count, file);
                        }
                        //recyclerviewdata(name2, name, count, file);
                    }
                } else {
                    // recyclerviewdata(name, name, count, file);
                }

            }

            // emptyTextview.setVisibility(View.GONE);
        } else {
            Log.i("", "There is no view folders");
//            emptyTextview.setText("Sorry ! There is no media folders or files");
//            emptyTextview.setVisibility(View.VISIBLE);
        }

    }

    public void recyclerviewdata(String name, String foldername, int count, File file) {
        a = true;
        Products products = new Products();
        products.setFolders_name(name);
        if (name.endsWith(".mp4")
                || name.endsWith(".mp3")
                || name.endsWith(".mpeg")
                || name.endsWith(".3gpp")
                || name.endsWith(".avi")
                || name.endsWith(".rm")
                || name.endsWith(".au")
                || name.endsWith(".aac")
                || name.endsWith(".asf")
                || name.endsWith(".aif")
                || name.endsWith(".aiff")
                || name.endsWith(".aifc")
                || name.endsWith(".rmvb")
                || name.endsWith(".amv")
                || name.endsWith(".vob")
                || name.endsWith(".yuv")
                || name.endsWith(".qt")
                || name.endsWith(".midi")
                || name.endsWith(".mid")
                || name.endsWith(".m4p")
                || name.endsWith(".m4v")
                || name.endsWith(".mpg")
                || name.endsWith(".mxf")
                || name.endsWith(".m2v")
                || name.endsWith(".kar")
                || name.endsWith(".mkv")
                || name.endsWith(".oga")
                || name.endsWith(".ogg")
                || name.endsWith(".ogv")
                || name.endsWith(".f4b")
                || name.endsWith(".f4a")
                || name.endsWith(".f4p")
                || name.endsWith(".f4v")
                || name.endsWith(".flac")
                || name.endsWith(".flv")
                || name.endsWith(".webm")
                || name.endsWith(".wmv")
                || name.endsWith(".wav")
                || name.endsWith(".3g2")
                || name.endsWith(".3gpp")
                || name.endsWith(".3gp")
                || name.endsWith(".mov")
                || name.endsWith(".m4a")
                || name.endsWith(".mng")
                || name.endsWith(".mp2")
                || name.endsWith(".mga")
                || name.endsWith(".spx")
                || name.endsWith(".svi")
                || name.endsWith(".nsv")
                || name.endsWith(".snd")
                || name.endsWith(".MP4")
                || name.endsWith(".mkv")) {


            //products.setTotal_count(String.valueOf(count));
            if (count != 0) {
                {

                    {
                        folders_list.add(foldername);
                        folders_List2.add(foldername);
                        folders_path.add(file.toString());
                        folders_path2.add(file.toString());
                        // insert_query = " Insert into folders (name,path,count) values("+'"'+""+foldername+""+'"'+","+'"'+"" +file.toString()+ ""+'"'+",'"+count+"')";
                        Log.e("Insert query", insert_query);
                        //db.insert_update(insert_query);
                    }
                }
            }
        }
    }

    File getStoragePath() {
        String removableStoragePath;
        File fileList[] = new File("/storage/").listFiles();
        for (File file : fileList) {
            if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && file.isDirectory() && file.canRead()) {
                File name = file;

                return file;
            }
        }
        return Environment.getExternalStorageDirectory();
    }


    private class BigComputationTask3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            try {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            } catch (Exception ff) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(SplashScreen.this, VideoFolder.class);
                        startActivity(i);
                        finish();
                    }
                }, 600000);


            }


        }

        @Override
        protected Void doInBackground(Void... params) {
            // Runs on the background thread
            //doBigComputation();


            try {
                list2 = getSdCardPaths(getApplicationContext(), true);


                File[] file = Environment.getExternalStorageDirectory().listFiles(new FileExplorer(getApplicationContext(), 0));


                try {


                    File file2 = new File(list2.get(1));
                    file2.listFiles(new FileExplorer(getApplicationContext(), 1));

                } catch (Exception g) {

                }
                // folderAdapter.notifyDataSetChanged();


            } catch (Exception g) {
                //   System.out.println("Error on extantions  "+g.getMessage());
                File[] file = Environment.getExternalStorageDirectory().listFiles(new FileExplorer(getApplicationContext(), 0));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {



            /*
            if(!isOnline()) {

                Intent i = new Intent(SplashScreen.this, VideoFolder.class);
                startActivity(i);
                finish();
            }
            */


        }

    }


    public void doFunc() {
        /* Create an Intent that will start the Menu-Activity. */
        Intent mainIntent = new Intent(SplashScreen.this, VideoFolder.class);
        SplashScreen.this.startActivity(mainIntent);
        SplashScreen.this.finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static List<String> getSdCardPaths(final Context context, final boolean includePrimaryExternalStorage) {
        String storageState = null;
        final File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
        if (externalCacheDirs == null || externalCacheDirs.length == 0)
            return null;
        if (externalCacheDirs.length == 1) {
            if (externalCacheDirs[0] == null)
                return null;
            storageState = EnvironmentCompat.getStorageState(externalCacheDirs[0]);
            if (!Environment.MEDIA_MOUNTED.equals(storageState))
                return null;
            if (!includePrimaryExternalStorage && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Environment.isExternalStorageEmulated())
                return null;
        }
        final List<String> result = new ArrayList<>();
        if (includePrimaryExternalStorage || externalCacheDirs.length == 1)
            result.add(getRootOfInnerSdCardFolder(externalCacheDirs[0]));


        try {
            for (int i = 1; i < externalCacheDirs.length; ++i) {
                final File file = externalCacheDirs[i];
                if (file == null)
                    continue;
                storageState = EnvironmentCompat.getStorageState(file);
                if (Environment.MEDIA_MOUNTED.equals(storageState))
                    result.add(getRootOfInnerSdCardFolder(externalCacheDirs[i]));
            }
        } catch (Exception gg) {
            result.add("/storage/sdcard1");
        }

        //
        //  System.out.println("sdcard "+Environment.getExternalStoragePublicDirectory());


        try {
            Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

            if (isSDPresent) {
                if (externalCacheDirs.length == 1) {

                    result.add("/storage/sdcard1/");
                }


            }
        } catch (Exception ss) {

        }


        if (result.isEmpty())
            return null;
        return result;
    }


    public static String[] getStorageDirectories() {
        String[] storageDirectories;
        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            List<String> results = new ArrayList<String>();
            File[] externalDirs = contex.getExternalFilesDirs(null);
            for (File file : externalDirs) {
                String path = null;
                try {
                    path = file.getPath().split("/Android")[0];
                } catch (Exception e) {
                    e.printStackTrace();
                    path = null;
                }
                if (path != null) {
                    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Environment.isExternalStorageRemovable(file))
                            || rawSecondaryStoragesStr != null && rawSecondaryStoragesStr.contains(path)) {
                        results.add(path);
                    }
                }
            }
            storageDirectories = results.toArray(new String[0]);
        } else {
            final Set<String> rv = new HashSet<String>();

            if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
                final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
                Collections.addAll(rv, rawSecondaryStorages);
            }
            storageDirectories = rv.toArray(new String[rv.size()]);
        }
        return storageDirectories;
    }


    /**
     * Given any file/folder inside an sd card, this will return the path of the sd card
     */
    private static String getRootOfInnerSdCardFolder(File file) {
        if (file == null)
            return null;
        final long totalSpace = file.getTotalSpace();
        while (true) {
            final File parentFile = file.getParentFile();
            if (parentFile == null || parentFile.getTotalSpace() != totalSpace)
                return file.getAbsolutePath();
            file = parentFile;
        }
    }


}
