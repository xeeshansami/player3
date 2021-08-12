package com.play.view.videoplayer.hd;//package com.example.azhar.playerapp;
//

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;


public class Main2Activity extends AppCompatActivity {

    private List<String> myList;
    private ListView listView;
    private Cursor audiocursor;
    ProgressDialog pd;
    Dialog dialog;
    private List<Integer> fileFolderTypeList = null;
    public static final int AUDIO_FILE = 3;
    private Handler handler,handlerLoader;
    private TextView pathTextView, emptyTextview;
    private String mediapath = new String(Environment.getExternalStorageDirectory().getAbsolutePath());
    MediaMetadataRetriever metaRetriver;
    private File file, file2,file3,file4,file5,file6,file7;
    String name,name2,name3,name4,name5,name6,name7;
    ProgressBar loadingIndicator;
    private final static String[] acceptedExtensions = {"mp3", "mp2", "mp4", "wav", "flac", "ogg", "au", "snd", "mid", "midi", "kar"
            , "mga", "aif", "aiff", "aifc", "m3u", "oga", "spx"};
    private String filename;
    private String path;
    File temp_file;
    List<String> folders_path;
    List<String> folders_path2;
    List<String> folders_List2;
    MediaPlayer mediaPlayer;
    private String album, albumid;
    private String title;
    private String id;
    private ImageView imgAlbum;
    FolderAdapter folderAdapter;
    ProductAdapter audioAdpater;
    RecyclerView recyclerView, audiofilesrecycler;
    ArrayList<String> folders_list;
    ArrayList<Songs> audioFilesList;
    private Bitmap bitmap;
    AudioManager audioManager;
    int currentVol;
    private Bitmap bm;
    private String root_sd;
    private AlertDialog alertDialog;
    private File list[] = null;
    public int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private AdView mAdView;
    private InterstitialAd mInterstitial;
    boolean isActivityIsVisible = true;
    private AdRequest adRequest,ar;
    Boolean a = true;
//    ProgressBar loadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dialog = new Dialog(Main2Activity.this);
        // dialog.setTitle("               Please Wait");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView( getLayoutInflater().inflate( R.layout.custom, null ) );
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mAdView = (AdView) findViewById(R.id.adView_banner);
          adRequest = new AdRequest.Builder()
////                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setVisibility(View.VISIBLE);

        folders_list = new ArrayList<String>();
        folders_List2 = new ArrayList<String>();
        folders_path = new ArrayList<String>();
        folders_path2 = new ArrayList<String>();
//        mInterstitial = new InterstitialAd(this);
//        mInterstitial.setAdUnitId(getResources().getString(R.string.intersitial_as_unit_id));
//        mInterstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//
//                if (mInterstitial.isLoaded()) {
//                    if(mInterstitial != null) {
//                        loadingIndicator.setVisibility(View.GONE);
//                        mInterstitial.show();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//
//                mInterstitial = null;
//
//            }
//        });
//        ar = new AdRequest.Builder()
////                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
//                .build();



//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(mInterstitial != null) {
//                    mInterstitial.loadAd(ar);
//                }
//            }
//        }, 4000);
//        Handler handlerLoader = new Handler();
//        handlerLoader.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mInterstitial != null) {
//                    loadingIndicator.setVisibility(View.VISIBLE);
//                }
//            }
//        }, 3000);
//        myList = new ArrayList<String>();
        pathTextView = (TextView) findViewById(R.id.path);
        emptyTextview = (TextView) findViewById(R.id.emptyTextView);

        audioFilesList = new ArrayList<Songs>();
        recyclerView = (RecyclerView) findViewById(R.id.audiofoldersrecycler);
        audiofilesrecycler = (RecyclerView) findViewById(R.id.audiofilesrecycler);
        audiofilesrecycler.setNestedScrollingEnabled(true);
        audiofilesrecycler.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        final LinearLayoutManager audioFileslm = new LinearLayoutManager(this);
        audioFileslm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        audiofilesrecycler.setLayoutManager(audioFileslm);


        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkStoragePermission()) {

                showData();
            } else {
                requestStoragePermission();
            }

//            String root_sd = Environment.getExternalStorageDirectory().toString();
//            Log.e("Root", root_sd);
//
//
//            File list[] = null;
//    /* if ( Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {  // we can read the External Storage...
//        list=getAllFilesOfDir(Environment.getExternalStorageDirectory());
//    }*/
//
//            pathTextView.setText("Folders");
//
//            file = new File(root_sd);
//            list = file.listFiles(new AudioFilter());
//
//            Log.e("Size of list ", "" + list.length);
//            //LoadDirectory(root_sd);
//
//            folders_list.clear();
//            for (int i = 0; i < list.length; i++) {
//
////            if (list[i].isHidden()) {
////                System.out.println("hidden path files.." + list[i].getAbsolutePath());
////            }
//
//                String name = list[i].getName();
//                int count = getAudioFileCount(list[i].getAbsolutePath());
//                Log.e("Count : " + count, list[i].getAbsolutePath());
//                Log.i("Counts of Data", String.valueOf(count));
////            if (count != 0)
////                myList.add(name);
//
//                Products products = new Products();
//                products.setFolders_name(name);
//                products.setTotal_count(String.valueOf(count));
//                if (count != 0)
////                myList.add(String.valueOf(products));
//                    folders_list.add(name);
//        /*int count=getAllFilesOfDir(list[i]);
//        Log.e("Songs count ",""+count);
//
//        */
//
//            }
//            folderAdapter = new FolderAdapter(VideoFolder.this, folders_list);
//            recyclerView.setAdapter(folderAdapter);


        } else {
//            if (checkStoragePermission()) {

            showData();
//            } else {
//                requestStoragePermission();
//            }
        }
//        listView = (ListView) findViewById(com.azhar.azhar.videoplayer.R.id.pathlist);
//        pathTextView = (TextView) findViewById(com.azhar.azhar.videoplayer.R.id.path);
//
//
//
//        String root_sd = Environment.getExternalStorageDirectory().toString();
//        Log.e("Root", root_sd);
//
//
//        File list[] = null;
//    /* if ( Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {  // we can read the External Storage...
//        list=getAllFilesOfDir(Environment.getExternalStorageDirectory());
//    }*/
//
//        pathTextView.setText("Folders");
//
//        file = new File(root_sd);
//        list = file.listFiles(new AudioFilter());
//        Log.e("Size of list ", "" + list.length);
//        //LoadDirectory(root_sd);
//        folders_list.clear();
//        for (int i = 0; i < list.length; i++) {
//
////            if (list[i].isHidden()) {
////                System.out.println("hidden path files.." + list[i].getAbsolutePath());
////            }
//
//            String name = list[i].getName();
//            int count = getAudioFileCount(list[i].getAbsolutePath());
//            Log.e("Count : " + count, list[i].getAbsolutePath());
//            Log.i("Counts of Data", String.valueOf(count));
////            if (count != 0)
////                myList.add(name);
//
//            Products products = new Products();
//            products.setFolders_name(name);
//            products.setTotal_count(String.valueOf(count));
//            if (count != 0)
////                myList.add(String.valueOf(products));
//                folders_list.add(name);
//        /*int count=getAllFilesOfDir(list[i]);
//        Log.e("Songs count ",""+count);
//
//        */
//
//        }
//        folderAdapter = new FolderAdapter(Main2Activity.this, folders_list);
//        recyclerView.setAdapter(folderAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                temp_file = new File(file, folders_path.get(position));
//                                if (!temp_file.isFile()) {
                                System.out.println("I am not a file " + temp_file);
                                folders_list.clear();

                                //                                    List<File> files = getListFiles(file);
//                                    System.out.println("Filename or DirectoryName" + filesarray.addAll(files));
                                /// file = new File(file, folders_list.get(position1));
                                file = new File(folders_path.get(position));
                                //Uri uri = Uri.fromFile(file);

                                //String fullPAth1 = uri.toString();
                                Log.i("", "OnItemClick");

                                AsyncTaskRunner runner = new AsyncTaskRunner();
//                String sleepTime = time.getText().toString();
                                runner.execute();

//                                File listFilesSongs[] = file.listFiles();
//                                folders_list.clear();
//                                if (listFilesSongs != null && listFilesSongs.length > 0) {
//
//                                    for (int i = 0; i < listFilesSongs.length; i++) {
//
//                                        if (listFilesSongs[i].isDirectory()) {
//
//                                        String name = listFilesSongs[i].getName();
//
//                                        int count = getAudioFileCount(listFilesSongs[i].getAbsolutePath());
//                                        Log.e("Count : " + count, listFilesSongs[i].getAbsolutePath());
//                                        Products products = new Products();
//                                        products.setFolders_name(name);
////                                        products.setTotal_count(String.valueOf(count));
//                                        if (count != 0) {
//                                            folders_list.add(name);
//                                        }
//                                        System.out.println("Filename" + name);
//                                        String fullPAth = file.toString();
//                                        String[] str = fullPAth.split("/");
//                                        String lastOne = str[str.length - 1];
//                                        System.out.println(lastOne);
//                                        pathTextView.setText(lastOne);
//////                                    System.out.println(Arrays.toString(separated));
////
////
//////                                    Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_LONG).show();
////
////
////                                    }
//                                    folderAdapter = new FolderAdapter(VideoFolder.this, folders_list);
//                                    recyclerView.setAdapter(folderAdapter);
//                                        } else {
//                                            if (listFilesSongs[i].getName().toLowerCase().endsWith(".mp4")
//                                                || listFilesSongs[i].getName().toUpperCase().endsWith(".mp4")) {
//
//                                            System.out.println("SOngs Name = " + listFilesSongs[i].getName());
//                                            int countc = getAudioFileCount(listFilesSongs[i].getName());
//                                            System.out.println(countc);
//                                            getFiles(listFilesSongs[i].getName());
//
//
//                                        }
//                                        }
//                                    videoSongsAdapter = new VideoSongsAdapter(VideoFolder.this, videoActivitySongsList);
//                                    recyclerViewFiles.setAdapter(videoSongsAdapter);
//                                        }
//
//                                    }
////                                }
                                final Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Do something after 100ms
                                        ads();
                                    }
                                }, 00);


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                // do whatever
                            }
                        })
        );



    }


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        folders_list.clear();


finish();

        //folders_List2.clear();

        // folders_path.clear();

        if (doubleBackToExitPressedOnce) {
           finish();
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



    public void ads() {
        if (isOnline()) {
            if (!Main2Activity.this.isFinishing()){
                dialog.show();
            }
            mInterstitial = null;


            Display display = ((WindowManager) getSystemService(getApplication().WINDOW_SERVICE)).getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            Log.v("width", width + "");
            dialog.getWindow().setLayout((6 * width) / 8, (4 * height) / 20);

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
            mAdView.setVisibility(View.VISIBLE);
        }
    }
    private void showData() {


        if (root_sd == null) {
            root_sd = Environment.getExternalStorageDirectory().getAbsolutePath();

//            root_sd = "storage/emulated/0/WhatsApp/media";
            Log.i("", "System Memory:" + root_sd);
        }
        pathTextView.setText("Folders");
        file = new File(String.valueOf(root_sd));
        list = file.listFiles(new AudioFilter());
        if (list != null) {
            Log.e("Size of list ", "" + list.length);
            folders_list.clear();
            for (int i = 0; i < list.length; i++) {
                //dialog1.show();
//            if (list[i].isHidden()) {
//                System.out.println("hidden path files.." + list[i].getAbsolutePath());
//            }
//           if(list[i].isDirectory()) {

                name = list[i].getName();
                file = new File(String.valueOf(list[i]));
                String filenameArray[] = name.split("\\.");
                String extension = filenameArray[filenameArray.length - 1];
                int count = getAudioFileCount(String.valueOf(list[i].getAbsolutePath()));
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
                        || name.endsWith(".mpeg")
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

                    i=list.length;
                    // condition = true;
                }

                if (count > 0 && extension.equals(name))
                {

                    File[] aaa = list[i].listFiles(new AudioFilter());
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

                            x=aaa.length;
                            // condition = true;
                        }
                        if (count2 >= 1 && extension2.equals(name2))
                        {


                            File[] bbb = aaa[x].listFiles(new AudioFilter());
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

                                    y=bbb.length;
                                    //condition = true;
                                }
                                if (count3 >= 1 && extension3.equals(name3)) {
                                    File[] ccc = bbb[y].listFiles(new AudioFilter());
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

                                            z=ccc.length;
                                            //condition = true;
                                        }


                                        if (count4 >= 1 && extension4.equals(name4)) {
                                            File[] ddd = ccc[z].listFiles(new AudioFilter());
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

                                                    a=ddd.length;
                                                    //condition = true;
                                                }
                                                if (count5 > 1 && extension5.equals(name5)) {
                                                    File[] eee = ddd[a].listFiles(new AudioFilter());
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
                                        }
                                        else if (zz == ccc.length) {

                                            recyclerviewdata(name4, name3, count3, file3);
                                        }
                                        //recyclerviewdata(name4, name3, count3, file3);
                                    }
                                } else if (yy == bbb.length) {
                                    recyclerviewdata(name3, name2, count2, file2);
                                }
                                //recyclerviewdata(name3, name2, count2, file2);
                            }

                        }

                        else if (xx == aaa.length ){
                            recyclerviewdata(name2, name, count, file);
                        }
                        //recyclerviewdata(name2, name, count, file);
                    }
                }
                else{
                    recyclerviewdata(name, name, count, file);
                }

            }

            emptyTextview.setVisibility(View.GONE);
        } else {
            Log.i("", "There is no view folders");
            emptyTextview.setText("Sorry ! There is no media folders or files");
            emptyTextview.setVisibility(View.VISIBLE);
        }

    }

    public void recyclerviewdata(String name , String foldername,int count,File file) {

        a = true;
//        if (name.endsWith(".mp4")
//                || name.endsWith(".mp3")
//                || name.endsWith(".mpeg")
//                || name.endsWith(".3gpp")
//                || name.endsWith(".avi")
//                || name.endsWith(".rm")
//                || name.endsWith(".au")
//                || name.endsWith(".aac")
//                || name.endsWith(".asf")
//                || name.endsWith(".aif")
//                || name.endsWith(".aiff")
//                || name.endsWith(".aifc")
//                || name.endsWith(".rmvb")
//                || name.endsWith(".amv")
//                || name.endsWith(".vob")
//                || name.endsWith(".yuv")
//                || name.endsWith(".qt")
//                || name.endsWith(".midi")
//                || name.endsWith(".mid")
//                || name.endsWith(".m4p")
//                || name.endsWith(".m4v")
//                || name.endsWith(".mpg")
//                || name.endsWith(".mpeg")
//                || name.endsWith(".mxf")
//                || name.endsWith(".m2v")
//                || name.endsWith(".kar")
//                || name.endsWith(".mkv")
//                || name.endsWith(".oga")
//                || name.endsWith(".ogg")
//                || name.endsWith(".ogv")
//                || name.endsWith(".f4b")
//                || name.endsWith(".f4a")
//                || name.endsWith(".f4p")
//                || name.endsWith(".f4v")
//                || name.endsWith(".flac")
//                || name.endsWith(".flv")
//                || name.endsWith(".webm")
//                || name.endsWith(".wmv")
//                || name.endsWith(".wav")
//                || name.endsWith(".3g2")
//                || name.endsWith(".3gpp")
//                || name.endsWith(".3gp")
//                || name.endsWith(".mov")
//                || name.endsWith(".m4a")
//                || name.endsWith(".mng")
//                || name.endsWith(".mp2")
//                || name.endsWith(".mga")
//                || name.endsWith(".spx")
//                || name.endsWith(".svi")
//                || name.endsWith(".nsv")
//                || name.endsWith(".snd")
//                || name.endsWith(".MP4")
//                || name.endsWith(".mkv")) {


        //            if (count != 0)
//                myList.add(name);
        Products products = new Products();
        products.setFolders_name(name);
          if (name.endsWith(".mp3")
                || name.endsWith(".MP3")
                  || name.endsWith(".kar")
                  || name.endsWith(".ogg")
                  || name.endsWith(".oga")
                  || name.endsWith(".m3u")
                  || name.endsWith(".spx")
                  || name.endsWith(".aif")
                  || name.endsWith(".mga")
                  || name.endsWith(".aifc")
                  || name.endsWith(".midi")
                  || name.endsWith(".mid")
                  || name.endsWith(".aac")
                  || name.endsWith(".wav")
                  || name.endsWith(".flac")
                  || name.endsWith(".au"))  {



            //products.setTotal_count(String.valueOf(count));
            if (count != 0) {
                {
                    for (int i = 0; i < folders_list.size(); i++) {
                        if (foldername.equalsIgnoreCase(folders_list.get(i))) {
                            a = false;
                        }
                    }
                    if (a.equals(true)) {
                        folders_list.add(foldername);
                        folders_List2.add(foldername);
                        folders_path.add(file.toString());
                        folders_path2.add(file.toString());
                       // folderAdapter = new FolderAdapter(Main2Activity.this, folders_list);
                        recyclerView.setAdapter(folderAdapter);
                    }

                }
            }
        }
    }

    private void showData1() {
        if (root_sd == null) {
            root_sd = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.i("", "System Memory:" + root_sd);
        }


        pathTextView.setText("Folders");
        file = new File(root_sd);
        list = file.listFiles(new AudioFilter());
        if (list != null) {

            Log.e("Size of list ", "" + list.length);

            folders_list.clear();
            for (int i = 0; i < list.length; i++) {

//            if (!list[i].isHidden()) {
//                System.out.println("hidden path files.." + list[i].getAbsolutePath());
//            }

                String name = list[i].getName();
                String filenameArray[] = name.split("\\.");
                String extension = filenameArray[filenameArray.length - 1];
                Log.i("", "Extensions of :" + extension);


                if (!name.endsWith(".mp3")
                        && !name.endsWith(".MP3")
                        && !name.endsWith(".3gp")
                        && !name.endsWith(".kar")
                        && !name.endsWith(".ogg")
                        && !name.endsWith(".oga")
                        && !name.endsWith(".m3u")
                        && !name.endsWith(".spx")
                        && !name.endsWith(".aif")
                        && !name.endsWith(".mga")
                        && !name.endsWith(".aifc")
                        && !name.endsWith(".midi")
                        && !name.endsWith(".mid")
                        && !name.endsWith(".aac")
                        && !name.endsWith(".wav")
                        && !name.endsWith(".flac")
                        && !name.endsWith(".au")) {

                    int count = getAudioFileCount(list[i].getAbsolutePath());
                    Log.e("Count : " + count, String.valueOf(list[i].getParentFile()));
                    Log.i("Counts of Data", String.valueOf(count));
                    Products products = new Products();
                    products.setFolders_name(name);
                    products.setTotal_count(String.valueOf(count));
                    if (count != 0)
                        folders_list.add(name);

                  //  folderAdapter = new FolderAdapter(Main2Activity.this, folders_list);
                    recyclerView.setAdapter(folderAdapter);
                } else if (list[i].getName().toLowerCase().endsWith("mp3") ||
                        list[i].getName().endsWith("MP3") ||
                        list[i].getName().endsWith("3gp") ||
                        list[i].getName().toLowerCase().endsWith("m4a") ||
                        list[i].getName().toLowerCase().endsWith("ogg") ||
                        list[i].getName().toLowerCase().endsWith("kar") ||
                        list[i].getName().toLowerCase().endsWith("oga") ||
                        list[i].getName().toLowerCase().endsWith("spx") ||
                        list[i].getName().toLowerCase().endsWith("m3u") ||
                        list[i].getName().toLowerCase().endsWith("aifc") ||
                        list[i].getName().toLowerCase().endsWith("aif") ||
                        list[i].getName().toLowerCase().endsWith("mga") ||
                        list[i].getName().toLowerCase().endsWith("midi") ||
                        list[i].getName().toLowerCase().endsWith("mid") ||
                        list[i].getName().toLowerCase().endsWith("snd") ||
                        list[i].getName().toLowerCase().endsWith("au") ||
                        list[i].getName().toLowerCase().endsWith("flac") ||
                        list[i].getName().toLowerCase().endsWith("wav") ||
                        list[i].getName().toLowerCase().endsWith("aac")
                        ) {

                    System.out.println("SOngs Name = " + list[i].getName());
                    int countc = getAudioFileCount(list[i].getName());
                    System.out.println(countc);

                    new GetAudioListAsynkTask(getApplicationContext()).execute(list[i].getName());
//                                            getFiles(list[i].getName());
//                                            initLayout(list[i].getName());


//                                            audioAdpater = new ProductAdapter(Main2Activity.this, audioFilesList);
//                                            audiofilesrecycler.setAdapter(audioAdpater);
                    if(folderAdapter != null){

                        folderAdapter.notifyDataSetChanged();

                    }
                }

            }

            emptyTextview.setVisibility(View.GONE);
        } else {
            Log.i("", "There is no view folders");
            emptyTextview.setText("Sorry ! There is no media folders or files");
            emptyTextview.setVisibility(View.VISIBLE);
        }
//        folderAdapter.notifyDataSetChanged();
    }

    private class GetAudioListAsynkTask extends AsyncTask<String, Void, Void> {
        private Context context;


        @Override
        protected void onPreExecute() {


        }

        public GetAudioListAsynkTask(Context context) {

            this.context = context;
        }


        @Override
        protected Void doInBackground(String... params) {

            try {

                initLayout(params[0]);


            } catch (Exception e) {

                return null;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            audioAdpater = new ProductAdapter(Main2Activity.this, audioFilesList);
            audiofilesrecycler.setAdapter(audioAdpater);
            if(audioAdpater != null){
                audioAdpater.notifyDataSetChanged();
            }


        }


    }


    public void adds(){
        if (isOnline()) {
            dialog.show();
            Display display =((WindowManager)getSystemService(getApplication().WINDOW_SERVICE)).getDefaultDisplay();
            int width = display.getWidth();
            int height=display.getHeight();
            Log.v("width", width+"");
            dialog.getWindow().setLayout((6*width)/8,(4*height)/20);

            MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));

//            mAdView = (AdView) findViewById(R.id.adView_banner);
            adRequest = new AdRequest.Builder()
//                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                    .build();
            mAdView.loadAd(adRequest);
            mAdView.setVisibility(View.GONE);


            mInterstitial = new InterstitialAd(this);
            mInterstitial.setAdUnitId(getResources().getString(R.string.intersitial_as_unit_id));


            mInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {

                    if (mInterstitial.isLoaded()) {
                        if (mInterstitial != null) {
                            loadingIndicator.setVisibility(View.GONE);
                            dialog.dismiss();
                            mInterstitial.show();
                        }
                    }

                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();

                    mInterstitial = null;
                }
            });
            ar = new AdRequest.Builder()
//                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                    .build();

            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (mInterstitial != null) {
                        mInterstitial.loadAd(ar);
                        loadingIndicator.setVisibility(View.GONE);
                        dialog.dismiss();

                    }
                }
            }, 0000);

            handlerLoader = new Handler();
            handlerLoader.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mInterstitial != null) {
                        loadingIndicator.setVisibility(View.VISIBLE);
                    }
                }
            }, 0000);
        } else {
            loadingIndicator.setVisibility(View.GONE);}
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

    private int getAudioFileCount(String dirPath) {

        String selection = MediaStore.Audio.AudioColumns.DATA + " like ?";
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM};
        String[] selectionArgs = {dirPath + "%"};
        String sortOrder = MediaStore.Video.VideoColumns.TITLE + " ASC";
        Cursor cursor = managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder);
        return cursor.getCount();
    }

    private void initLayout(String songsName) {
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.AudioColumns.DATA + " like ?";


        String[] selectionArgs = {"%" + songsName + "%"};

        final String[] cursor_cols = {MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.SIZE,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.DURATION};


        audiocursor = getContentResolver().query(uri,
                cursor_cols, selection, selectionArgs, null);
        int count = audiocursor.getCount();
        System.out.println("Total songs " + count);

//        folders_list.clear();
        while (audiocursor.moveToNext()) {

            String s = audiocursor.getString(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            System.out.println("Sizes" + s);
            String artist = audiocursor.getString(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = audiocursor.getString(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            title = audiocursor.getString(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String data = audiocursor.getString(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            long albumId = audiocursor.getLong(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            int duration = audiocursor.getInt(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

//            Log.i("Album art", albumArtUri.toString());
//            Log.i("Duration", milliSecondsToTimer(duration));
//            Log.i("Title", title);
//            Log.i("Artist", artist);
//            Log.i("Album", album);
//            Log.i("Data", data);

            audioFilesList.add(new Songs(albumArtUri.toString(), title, artist, data, album, milliSecondsToTimer(duration)));


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
    void network_stream() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Main2Activity.this);
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

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.dhdvideo.player")));
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


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping...");
//                                    t.forEachFile(file);
            // String a = "///storage/emulated/0/FacebookVideos";
            // @SuppressLint("NewApi") Path p3 = Paths.get(URI.create("///storage/emulated/0/FacebookVideos"));
//                                    try {
//                                        nioRun(p3);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//
//                int ii = 1;
//                int maxFiles =10;
//                System.out.println( "IO run" );
//                long start = System.currentTimeMillis();
//                //File[] listOfFiles = folder.listFiles(new AudioFilter());
//                 list = temp_file.listFiles(new AudioFilter());
//                // System.out.println("Total : " + listOfFiles.length);
//                for (File file : list) {
//                    System.out.println( "" + ii + ": " + file.getName() );
//                    if (++ii > maxFiles) break;
//                }
//                String abc = file.getAbsolutePath();
//                long stop = System.currentTimeMillis();
//                System.out.println( "Elapsed: " + (stop - start) + " ms" );
            list = file.listFiles(new AudioFilter());
//                                    File list[] = file.listFiles(new AudioFilter());
            folders_list.clear();
            //videoActivitySongsList.clear();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i < list.length; i++) {
                        if (list[i].getName().toLowerCase().endsWith("mp3") ||
                                list[i].getName().endsWith("MP3") ||
                                list[i].getName().endsWith(".3gp") ||
                                list[i].getName().toLowerCase().endsWith("m4a") ||
                                list[i].getName().toLowerCase().endsWith("ogg") ||
                                list[i].getName().toLowerCase().endsWith("kar") ||
                                list[i].getName().toLowerCase().endsWith("oga") ||
                                list[i].getName().toLowerCase().endsWith("spx") ||
                                list[i].getName().toLowerCase().endsWith("m3u") ||
                                list[i].getName().toLowerCase().endsWith("aifc") ||
                                list[i].getName().toLowerCase().endsWith("aif") ||
                                list[i].getName().toLowerCase().endsWith("mga") ||
                                list[i].getName().toLowerCase().endsWith("midi") ||
                                list[i].getName().toLowerCase().endsWith("mid") ||
                                list[i].getName().toLowerCase().endsWith("snd") ||
                                list[i].getName().toLowerCase().endsWith("au") ||
                                list[i].getName().toLowerCase().endsWith("flac") ||
                                list[i].getName().toLowerCase().endsWith("wav") ||
                                list[i].getName().toLowerCase().endsWith("aac"))

                        {

                            System.out.println("SOngs Name = " + list[i].getName());
                            int countc = getAudioFileCount(list[i].getName());
                            System.out.println(countc);

                            new GetAudioListAsynkTask(getApplicationContext()).execute(list[i].getName());

//                                            getFiles(list[i].getName());


                            folderAdapter.notifyDataSetChanged();

                        } else {
//                                String name = list[i].getName();
//                                path = list[i].getPath();
//                                int count = getAudioFileCount(list[i].getAbsolutePath());
//                                Log.e("Count : " + count, list[i].getAbsolutePath());
//                                Products products = new Products();
//                                products.setFolders_name(name);
//                                products.setTotal_count("Total :" + String.valueOf(count));
//                                if (count != 0) {
//                                    folders_list.add(name);
//                                }
//                                System.out.println("Filename" + name);
//                                String fullPAth = file.toString();
//                                String[] str = fullPAth.split("/");
//                                String lastOne = str[str.length - 1];
//                                System.out.println(lastOne);
//                                pathTextView.setText(lastOne);
//stuff that updates ui
                        }
                    }
                }
            });
            //                                    System.out.println(Arrays.toString(separated));


//                                    Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_LONG).show();

//                                            if (pd.isShowing())
//                                            {
//                                                pd.dismiss();
//                                            }
//                                            else {
//                                                Toast.makeText(VideoFolder.this, "", Toast.LENGTH_SHORT).show();
//                                            }





//                                    videoSongsAdapter = new VideoSongsAdapter(VideoFolder.this, videoActivitySongsList);
//                                    recyclerViewFiles.setAdapter(videoSongsAdapter);



            //          Toast.makeText(MainActivity.this, "onprogress"+list.length, Toast.LENGTH_SHORT).show();
            //finalResult.setText(text[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog

            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
//            progressDialog.dismiss();
           // folderAdapter = new FolderAdapter(Main2Activity.this, folders_list);
            recyclerView.setAdapter(folderAdapter);
            folderAdapter.notifyDataSetChanged();
            pd.dismiss();
            //finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
            showprogress();
//            progressDialog = ProgressDialog.show(MainActivity.this, "ProgressDialog", "Wait for "+time.getText().toString()+ " seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
    public void showprogress() {
        pd = new ProgressDialog(this);
        pd.setTitle("Please wait");
        pd.setMessage("Loading dictionary file...");
        pd.setCancelable(false);
        pd.show();


    }


    // class to limit the choices shown when browsing to SD card to media files
    public class AudioFilter implements FileFilter {

        // only want to see the following audio file types
        private String[] extension = {"mp3", "mp2", "mp4", "wav", "flac", "ogg", "au", "snd", "mid", "midi", "kar"
                , "mga", "aif", "aiff", "aifc", "m3u", "oga", "spx"};

        @Override
        public boolean accept(File pathname) {

            // if we are looking at a directory/file that's not hidden we want to see it so return TRUE
            if ((pathname.isDirectory() || pathname.isFile()) && !pathname.isHidden()) {
                return true;
            }

            // loops through and determines the extension of all files in the directory
            // returns TRUE to only show the audio files defined in the String[] extension array
            for (String ext : extension) {
                if (pathname.getName().toLowerCase().endsWith(ext)) {
                    return true;
                }
            }

            return false;
        }
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

                showData();
                Log.d("After Permission", "Should be read");
            } else {
                checkStoragePermission();

                Log.d("After Permission", "Not reading");
            }
        }

    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
            isActivityIsVisible = false;
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
            isActivityIsVisible = true;
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        if(mInterstitial != null){
            mInterstitial = null;
        }
        super.onDestroy();
    }

}