package com.play.view.videoplayer.hd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
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
import androidx.annotation.RequiresApi;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.play.view.videoplayer.hd.CursorUtils.Admob;
import com.play.view.videoplayer.hd.CursorUtils.SelectedFolderVideoActivity;
import com.play.view.videoplayer.hd.CursorUtils.VideosAndFoldersUtility;
import com.play.view.videoplayer.hd.Model.Folder;
import com.play.view.videoplayer.hd.Model.Video;

import com.play.view.videoplayer.hd.equalizer.CustomFragment2;
import com.play.view.videoplayer.hd.equalizer.SessionStorage;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class VideoFolder extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private File file, file2, file3, file4, file5, file6, file7;
    ArrayList<File> filesarray = new ArrayList<File>();
    private List<String> myList;
    private ListView listView;
    Video songs;
    int size;
    boolean listGird = true;
    CustomFragment2 equalizerFragment;
    public FrameLayout equiliazerScreen;
    int sortVariable = 0;
    boolean isActivityIsVisible = true;
    public int equValue = 0;
    public static List<Folder> folders = new ArrayList();
    public static List<Video> videos = new ArrayList();
    String pos;
    //    private TransparentProgressDialog pd1;
    int totalcount, recount;
    private List<Integer> fileFolderTypeList = null;
    public static final int AUDIO_FILE = 3;
    public static final String[] titles = new String[]{"Strawberry",
            "Banana", "Orange", "Mixed",
            "Banana", "Orange", "Mixed",
            "Banana", "Orange", "Mixed",
            "Banana", "Orange", "Mixed",
            "Banana", "Orange", "Mixed",
            "Banana", "Orange", "Mixed",
            "Banana", "Orange", "Mixed",
            "Banana", "Orange", "Mixed",
            "Banana", "Orange", "Mixed",
            "Banana", "Orange", "Mixed",
            "Banana",
    };

    public static final String[] descriptions = new String[]{
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "It is an aggregate accessory fruit",
            "Mixed Fruits"};

    public static final Integer[] images = {R.drawable.anim_drawable1, R.drawable.video_icon, R.drawable.anim_drawable1, R.drawable.video_icon,
            R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon,
            R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon,
            R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon,
            R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon,
            R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon,
            R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon,
            R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon, R.drawable.video_icon,
    };
    DrawerLayout drawer;
    InterstitialAd mInterstitialAd;
    File temp_file;
    Boolean backcheck = false;
    public int mState = 1, mState2 = 1, k;
    Boolean b;
    ArrayList<Integer> ints = new ArrayList<>();
    Dialog dialog, dialog1, dialog2;
    String name, name2, name3, name4, name5, name6, name7;
    public static TextView pathTextView, emptyTextview;
    private String mediapath = new String(Environment.getExternalStorageDirectory().getAbsolutePath());
    MediaMetadataRetriever metaRetriver;
    private final static String[] acceptedExtensions = {"mp3", "mp2", "mp4", "wav", "flac", "ogg", "au", "snd", "mid", "midi", "kar"
            , "mga", "aif", "aiff", "aifc", "m3u", "oga", "spx"};
    private String filename;
    private String path;
    VideoSongsAdapter videoSongsAdapter5;
    Boolean a = true;
    String sSDpath = null;
    String similar = "";
    MediaMetadataRetriever retriever;
    private File root;
    private ArrayList<File> fileList = new ArrayList<File>();
    private LinearLayout view1;
    public static int firsttime = 0;
    private Handler handler, handlerLoader;
    File fileCur = null;
    private File list[] = null;
    MediaPlayer mediaPlayer;
    Boolean condition = false, condition2 = false, condition3 = false, condition4 = false, condition5 = false;
    private String album, albumid;
    private String title;
    private String id, folderclick;
    private ImageView imgAlbum;
    int folderid;
    private String foldername, folderpath;
    static ArrayList<Integer> strings;
    Thread thread;
    FolderAdapter folderAdapter;
    RecyclerView recyclerViewFolders;

    String insert_query, select_query, delete_query;
    Cursor c;
    String adding = "false";
    String pathofvideo;
    SharedPreferences sharedPreferences = null;
    Boolean adcheck;
    String dbfilename, dbimage, dbduraton, dbtitle, dbalbum, dbartish, dbsize, dbresol;
    int dbposition;
    //  DataBaseManager db=null;
    String dbname, dbpath, dbpath2, dbname2;
    List<String> folders_list;
    List<String> folders_path;
    List<String> folders_path2;
    List<String> folders_List2;
    WindowManager windowManager2;
    WindowManager.LayoutParams layoutParams;
    private Bitmap bitmap;
    ArrayList<Video> videoActivitySongsList;
    Cursor videoCursorActivity;
    //RecyclerView recyclerViewVideos;
    RecyclerView recyclerViewVideos;
//    ListView recyclerViewVideos;

    VideoSongsAdapter videoSongsAdapter;
    private Cursor videoCursor;
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
    Toolbar toolbar;
    ProgressBar progressDialog;
    ProgressDialog prodialog;
    public int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private String root_sd;
    String sdcard;
    private Cursor cursor;
    private int mTreeSteps = 0;
    int vidid;
    public static AdView mAdView;
    private AdRequest adRequest, ar;
    ProgressBar loadingIndicator;
    private InterstitialAd mInterstitial;
    int folderposition;
    int position1;
    CustomListAdapter itemAdapter;
    Boolean checksdcard, recnfrm = true;
    SharedPreferences mSharedPrefs = null;
    private int APP_PERMISSION_REQUEST = 1220;
    private String value;
    private String value2;
    private int brightnessValue;
    private ContentResolver cResolver;
    ProgressDialog pd;
    private File directory;
    ArrayList<File> inFiles;
    InputStream in = null;
    Button button;
    OutputStream out = null;
    private String parent;
    Intent animationIntent;
    File createFolder, creaderFolder2;
    SettingFragment fragmentB;
    View view;
    AudioManager audioManager;
    int currentVol;
    private static final String ACTION_MEDIA_REMOVED = "android.intent.action.MEDIA_REMOVED";
    private static final String ACTION_MEDIA_MOUNTED = "android.intent.action.MEDIA_MOUNTED";
    private static final String MEDIA_BAD_REMOVAL = "android.intent.action.MEDIA_BAD_REMOVAL";
    private static final String MEDIA_EJECT = "android.intent.action.MEDIA_EJECT";
    private static final String TAG = "SDCardBroadcastReceiver";
    static MaterialStyledDialog.Builder dialogHeader_3, dialogHeader_2;

    @Override
    protected void onRestart() {
        super.onRestart();
        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        //brightnessValue = mSharedPrefs.getInt("ScreenBrightness", 0);
        // setBrightness(brightnessValue);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {

            try {
                mAdView.pause();
            } catch (Exception f) {

            }

            isActivityIsVisible = false;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("resume call");
        if (mAdView != null) {
            try {
                mAdView.resume();
            } catch (Exception f) {

            }
            isActivityIsVisible = true;
        }


        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);


        try {
//            mAdView.setVisibility(View.GONE);
        } catch (Exception f) {
            System.out.println("resume call error");

        }


        // if(firsttime==1)
        // {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                onResumeLoad();
//                // localdata(true);
//                //  loadDataAll();
//            }
//        }, 3000);

        // }

//        new BigComputationTask3().execute();

        try {
//            mAdView.setVisibility(View.GONE);
        } catch (Exception g) {
            System.out.println("resume call error " + g.getMessage());
        }
        // brightnessValue = mSharedPrefs.getInt("ScreenBrightness", 0);
        //  setBrightness(brightnessValue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mAdView != null) {
//
//            try {
//                //  mAdView.destroy();
//            } catch (Exception f) {
//
//            }
//
//        }
//        if (mInterstitial != null) {
//            mInterstitial = null;
//        }
//
//        if (mInterstitialAd != null) {
//            mInterstitialAd = null;
//        }

    }

    ProgressDialog getProgressDialog;
    SwipeRefreshLayout swipeRefreshLayoutl;
    LinearLayout col;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.play.view.videoplayer.hd.R.layout.video_folder);
        equiliazerScreen = (FrameLayout) findViewById(R.id.root_view);
        col = findViewById(R.id.col);
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.app_id));
        //   --- Admob ---
        view = getWindow().getDecorView().getRootView();
        Admob.createLoadBanner(VideoFolder.this, col);
        try {
            swipeRefreshLayoutl = findViewById(R.id.refresh);
            swipeRefreshLayoutl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    sycn();
                    swipeRefreshLayoutl.setRefreshing(false);
                }
            });
            androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            pathTextView = (TextView) toolbar.findViewById(R.id.path);
            new FontContm(this, pathTextView);
            audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            adcheck = true;
            strings = new ArrayList<>();

            fragmentB = new SettingFragment();
            loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

            dialog = new Dialog(VideoFolder.this);
            // dialog.setTitle("               Please Wait");
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(getLayoutInflater().inflate(R.layout.custom, null));

            if (isOnline()) {
                //   dialog.show();
            }

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            dialog1 = new Dialog(VideoFolder.this);
            // dialog.setTitle("               Please Wait");
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(getLayoutInflater().inflate(R.layout.custom4, null));

            dialog2 = new Dialog(this);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setContentView(getLayoutInflater().inflate(R.layout.custom3, null));
            final Display display = ((WindowManager) getSystemService(getApplication().WINDOW_SERVICE)).getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            Log.v("width", width + "");

            dialog1.getWindow().setLayout((6 * width) / 6, (4 * height) / 19);
            // dialog.getWindow().setLayout((6 * width) / 6, (4 * height) / 19);

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            progressDialog = findViewById(R.id.progressbar_video_folder);
            progressDialog.setIndeterminate(true);
            MobileAds.initialize(VideoFolder.this, getResources().getString(R.string.app_id));


//            mAdView = (AdView) findViewById(R.id.adView_banner);
//            adRequest = new AdRequest.Builder()
//                    .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
//                    .build();
//            mAdView.loadAd(adRequest);
//
//            mAdView.setAdListener(new AdListener() {
//
//                @Override
//                public void onAdLoaded() {
//                    mAdView.setVisibility(View.VISIBLE);
//                    Log.i("adLoaded", "yes");
//                }
//                // Implement AdListener
//            });

            //  ads();


            //MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
//        mAdView = (AdView) findViewById(R.id.adView_banner);
//        adRequest = new AdRequest.Builder()
//                //.addTestDevice("190af34e322acedf")
//                .build();
//
//        mAdView.loadAd(adRequest);
//
//        mAdView.setAdListener(new AdListener() {
//
//            @Override
//            public void onAdLoaded() {
//
//
//                if (isOnline()) {
//                    // mAdView.setVisibility(View.VISIBLE);
//
//                }
//
//
//            }
//        });


            try {
                createFolder = new File(Environment.getExternalStorageDirectory(), ".thumbs");
                if (!createFolder.exists()) {
                    createFolder.mkdir();
                }
            } catch (Exception g) {

                System.out.println("create directory error " + g.getMessage());

            }


            AppClass.subDirsSize = AppClass.subDirs.size();

            new ImageDownloaderTask().execute("");


            emptyTextview = (TextView) findViewById(R.id.emptyTextView);
            new FontContm(this, emptyTextview);
            videoActivitySongsList = new ArrayList<Video>();
            folders_list = new ArrayList<String>();
            folders_List2 = new ArrayList<String>();
            folders_path = new ArrayList<String>();
            folders_path2 = new ArrayList<String>();
//        myList = new ArrayList<String>();
//
            try {
                recyclerViewFolders = (RecyclerView) findViewById(R.id.foldersrecycler);
//            recyclerViewVideos = findViewById(R.id.videoRecycler);

//         recyclerViewVideos.setScrollingCacheEnabled(false);
                final LinearLayoutManager lm = new LinearLayoutManager(this);
                lm.setOrientation(LinearLayoutManager.VERTICAL);
                final LinearLayoutManager filesLm = new LinearLayoutManager(this);
                filesLm.setOrientation(LinearLayoutManager.VERTICAL);
//                recyclerViewFolders.setLayoutManager(lm);
//            recyclerViewVideos.setLayoutManager(filesLm
                recyclerViewFolders.setLayoutManager(new GridLayoutManager(this, 1));
// );
            } catch (IndexOutOfBoundsException e) {
            } catch (RuntimeException e) {
            } catch (Exception e) {
            }
            mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            value2 = mSharedPrefs.getString("value2", "true");
            mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            value = mSharedPrefs.getString("value", "true");


            //File[] file = Environment.getExternalStorageDirectory().listFiles(new FileExplorer(getApplicationContext()));

            /// String root_sd ="/storage/extSdCarcd";


            System.out.println("sdcard external " + System.getenv("EXTERNAL_STORAGE"));

//        File f_secs = new File(secStore);

            //      System.out.println("sdcard external "+f_secs.getName());


        /*
        Map<String, File> externalLocations = ExternalStorage.getAllStorageLocations();
        File sdCard = externalLocations.get(ExternalStorage.SD_CARD);
        File externalSdCard = externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD);

*/
        /*




        if(StorageUtil.isExternalStorageAvailable()) {

            file2.listFiles(new FileExplorer(getApplicationContext()));
        }
        */


//        int MyVersion = Build.VERSION.SDK_INT;
//        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
//            recyclerViewVideos.setNestedScrollingEnabled(false);
//            //recyclerViewVideos.setHasFixedSize(true);
//            recyclerViewFolders.setNestedScrollingEnabled(false);
//            recyclerViewFolders.setHasFixedSize(true);
//            if (checkStoragePermission()) {
//                {

            if (checkStoragePermission()) {
                AsyncLoadVideosAndFolder asyncLoadVideosAndFolder = new AsyncLoadVideosAndFolder("name");
                asyncLoadVideosAndFolder.execute();
            } else {
                requestStoragePermission();
            }
//            new AsyncLoadVideosAndFolder().execute();
//                }
//            } else {
//                requestStoragePermission();
//            }

//        } else {
//            //  recyclerViewVideos.setHasFixedSize(true);
//            recyclerViewFolders.setHasFixedSize(true);
//
//            {
//
//                localdata(true);
//
//            }
//        }

            final Intent i = getIntent();
            Bundle extras = i.getExtras();

            if (extras != null) {
                vidid = extras.getInt("vidid");
                System.out.println("vidid" + vidid);


            }

        /*
        if (extras != null) {
            vidid = extras.getInt("vidid");

            select_query = "select * from folders where id='"+vidid+"'";
            Log.e("select query", select_query);
            c = db.selectQuery(select_query);
            if (c.getCount() > 0) {
                if (c.moveToFirst() && c != null) {
                    do {
                        dbpath2 = c.getString(c.getColumnIndex("path"));
                        dbname2 = c.getString(c.getColumnIndex("name"));
                    }
                    while (c.moveToNext());
                }
            }
            File file321 = new File(dbpath2);
            String text = file321.toString();
            text = text.substring(0, text.lastIndexOf('/'));
            text = text+"/"+dbname2;

            if (file321.isDirectory())
            {
                String[] children = file321.list();
                for (int o = 0; o < children.length; o++)
                {
                    text = text+"/"+children[o];
                    new File(file321, text+"/"+children[o]).delete();
                }
            }
        }




        dialogHeader_3 = new MaterialStyledDialog.Builder(VideoFolder.this)
                .setHeaderDrawable(R.drawable.header)
                //.setIcon(new IconicsDrawable(context).icon(MaterialDesignIconic.Icon.gmi_github).color(Color.WHITE))
                .withDialogAnimation(true)
                .setTitle("Need Synchronization")
                .setDescription("System identify that new Videos are added in your phone, So need to synchronize it")
                .setPositiveText("Yes")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog1.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                delete_query = "DELETE FROM folders";
                                Log.e("delete query", delete_query);
                                db.delete(delete_query);
                                delete_query = "DELETE FROM positions";
                                Log.e("delete query", delete_query);
                                db.delete(delete_query);
                                delete_query = "DELETE FROM diffvideos";
                                Log.e("delete query", delete_query);
                                db.delete(delete_query);
                                mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                                SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
                                mEditor1.putString("value", "true");
                                mEditor1.apply();
                                value = mSharedPrefs.getString("value", "true");
                                ints.clear();
                                showData();
                                select_query = "select * from videos ORDER BY id DESC LIMIT 1";
                                Log.e("select query", select_query);
                                c = db.selectQuery(select_query);
                                if (c.getCount() > 0) {
                                    if (c.moveToFirst() && c != null) {
                                        do {
                                            totalcount = c.getInt(c.getColumnIndex("id"));
                                        }
                                        while (c.moveToNext());
                                    }
                                }
                                mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                                SharedPreferences.Editor mEditor111 = mSharedPrefs.edit();
                                mEditor111.putInt("totalcount", totalcount);
                                mEditor111.putString("firstitemp", "false");
                                mEditor111.apply();
                                dialog1.dismiss();

                            }
                        }, 500);

                        Toast.makeText(getApplicationContext(), "Synchronizing", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeText("Not now");

        dialogHeader_2 = new MaterialStyledDialog.Builder(VideoFolder.this)
                .setHeaderDrawable(R.drawable.header)
                //.setIcon(new IconicsDrawable(context).icon(MaterialDesignIconic.Icon.gmi_github).color(Color.WHITE))
                .withDialogAnimation(true)
                .setTitle("Need Synchronization")
                .setDescription("System identify that new SD Card is inserted in your phone, So need to synchronize it")
                .setPositiveText("Yes")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog1.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Sync();         }
                        }, 500);


                        Toast.makeText(getApplicationContext(), "Synchronizing", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeText("Not now");
*/


            recyclerViewFolders.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, recyclerViewFolders,
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onItemClick(View view, final int position) {
                                    int newposition = position + 1;
                                    adcheck = false;


                                    System.out.println("selected item is " + position + " " + view.toString());
                                    Intent intent = new Intent(VideoFolder.this, SelectedFolderVideoActivity.class);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
//                                loadvideos(position);


                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    // do whatever
                                }
                            })
            );


        /*
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
                //.addTestDevice("190af34e322acedf")
                .build();
        mAdView.loadAd(adRequest);




        mAdView.setVisibility(View.VISIBLE);

        */

            root_sd = Environment.getExternalStorageDirectory().getAbsolutePath();
            getStoragePath();
            File tt = getStoragePath();
            sdcard = tt.toString();
            if (sdcard.equalsIgnoreCase(root_sd)) {
                recnfrm = false;
            }

            mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            checksdcard = mSharedPrefs.getBoolean("checksdcard", false);
            if (checksdcard == false && recnfrm == false) {
                //Toast.makeText(this, "No need To Sync", Toast.LENGTH_LONG).show();
//                mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
//                SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
//                mEditor1.putBoolean("checksdcard", false);
//                mEditor1.apply();
            } else if (checksdcard == true && recnfrm == true) {
                //Toast.makeText(this, "No need To Sync", Toast.LENGTH_LONG).show();
//
            } else {
                //dialog1.show();
                mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
                mEditor1.putBoolean("checksdcard", recnfrm);
                mEditor1.apply();
//            Sync();

            }


            // recyclerViewVideos.setLayoutManager(new WrapContentLinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        } catch (IndexOutOfBoundsException e) {
        } catch (RuntimeException e) {
        } catch (Exception e) {
        }
    }

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= TRIM_MEMORY_COMPLETE) {
            AppClass.mVideoPathCache.evictAll();
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


    public void compare() {

    }


    public void ads() {

     /*
        if (isOnline())
    {
    */

        if (!VideoFolder.this.isFinishing()) {
            //   dialog.show();
        }

        mInterstitial = null;


//        Display display = ((WindowManager) getSystemService(getApplication().WINDOW_SERVICE)).getDefaultDisplay();
//        int width = display.getWidth();
//        int height = display.getHeight();
//        Log.v("width", width + "");
//        dialog.getWindow().setLayout((6 * width) / 8, (4 * height) / 15);


        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.intersitial_as_unit_id));

        AdRequest adRequestInter = new AdRequest.Builder().build();
        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(getResources().getString(R.string.intersitial_as_unit_id));

        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {


                System.out.println("mInterstitial add loaded");
               /*
                if (mInterstitial != null)
                {
                    if (mInterstitial.isLoaded())
                    {



                    }
                }

                */

                //loadingIndicator.setVisibility(View.GONE);


                //loadingIndicator.draw();
                // if (isActivityIsVisible) {
                // if (adcheck.equals(Boolean.TRUE)) {


                if (mInterstitial.isLoaded()) {

                    mInterstitial.show();
                }
                //   }
                //}
            }
        });

        mInterstitial.loadAd(adRequestInter);
//        mAdView.setVisibility(View.VISIBLE);
        //}
    }

 /*   public void loadvideos(int posi) {


        int i = 0;
        backcheck = true;
        recyclerViewFolders.setVisibility(View.GONE);
        recyclerViewVideos.setVisibility(View.VISIBLE);
        folders_list.clear();

        videoActivitySongsList.clear();
        int posi1 = posi;

             *//*
             select_query = "select * from diffvideos where position ='"+posi1+"'";
    Log.e("select query", select_query);



    c = db.selectQuery(select_query);
    if (c.getCount() > 0) {
        if (c.moveToFirst() && c != null) {
            do {
                i++;
                if (isOnline()) {
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
                    videoSongsAdapter = new VideoSongsAdapter5(this, videoActivitySongsList);
                    recyclerViewVideos.setAdapter(videoSongsAdapter);
                    recyclerViewVideos.getRecycledViewPool().clear();
                    videoSongsAdapter.notifyDataSetChanged();


            }
            while (c.moveToNext());
        }
    }
    *//*

        AppClass appClass = AppClass.getInstance();
        String folderName = appClass.folder.get(posi).name;

        Map<String, String> snapshot = null;
        snapshot = appClass.mVideoPathCache.snapshot();
        *//*synchronized (appClass.mVideoPathCache) {

        }*//*

        for (String id : snapshot.keySet()) {

            Object myObject = appClass.mVideoPathCache.get(id);

//            System.out.println("Values from catche "+StorageUtil.folder.get(posi).getPath()+" "+  myObject.toString()+" "+id.substring(id.lastIndexOf("/")+1,id.length()-4)+" "+id);
            songs = new Vio();
            if (appClass.folder.get(posi).getPath().equalsIgnoreCase(myObject.toString())) {
                songs.setData(id);
                //  songs.setImage(albumArtUri.toString());
                //songs.setDuration(milliSecondsToTimer(getVideoDuration(id)));
                songs.setName(id.substring(id.lastIndexOf("/") + 1, id.length() - 4));
                // songs.setArtist(artist);
                songs.setSize(getFileSize(new File(id).length()));
                videoActivitySongsList.add(songs);
                //   getFiles(id);
            }
        }

        List<RowItem> rowItems;
        RowItem item;
        rowItems = new ArrayList<RowItem>();
        for (int j = 0; j < titles.length; j++) {
            item = new RowItem(images[j], titles[j], descriptions[j]);
            rowItems.add(item);
            System.out.println("Values from catche " + rowItems.size());
        }
        try {
            CustomListViewAdapter adapter = new CustomListViewAdapter(this, R.layout.list_item, rowItems);
            videoSongsAdapter5 = new VideoSongsAdapter(this, videoActivitySongsList, "", 0);
            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.activity_list_item, android.R.id.text1, getResources().getStringArray(R.array.hardware_values));
//        itemAdapter = new CustomListAdapter(getApplicationContext(), videoActivitySongsList, "");
            pathTextView.setText("" + folderName);
            recyclerViewVideos.setAdapter(videoSongsAdapter5);
//        recyclerViewVideos.setAdapter(videoSongsAdapter);
            itemAdapter.notifyDataSetChanged();

        } catch (IndexOutOfBoundsException e) {
        }
//        itemAdapter.notifyDataSetChanged();
//      videoSongsAdapter.notifyDataSetChanged();
    }*/


    /*public long getVideoDuration(String path) {

        retriever = new MediaMetadataRetriever();

//use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(getApplicationContext(), Uri.fromFile(new File(path)));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        long timeInMillisec = Long.parseLong(time);

        retriever.release();

        return timeInMillisec;


        *//*

        MediaPlayer mp = MediaPlayer.create(context, Uri.parse(path));
        int duration = mp.getDuration();
        mp.release();

        System.out.println("duration of view "+duration);

        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );


*//*


    }*/


    /*private void loadDataAll() {

        backcheck = true;
        recyclerViewFolders.setVisibility(View.GONE);
        recyclerViewVideos.setVisibility(View.VISIBLE);
        folders_list.clear();

        videoActivitySongsList.clear();

        Map<String, String> snapshot = null;
        synchronized (AppClass.mVideoPathCache) {

            snapshot = AppClass.mVideoPathCache.snapshot();
        }

        snapshot = AppClass.mVideoPathCache.snapshot();


        for (String id : snapshot.keySet()) {
            Object myObject = AppClass.mVideoPathCache.get(id);

            VideoSongs songs = new VideoSongs();
            songs.setData(id);
            //  songs.setImage(albumArtUri.toString());
            //    songs.setDuration(milliSecondsToTimer(getVideoDuration(id)));
            songs.setName(id.substring(id.lastIndexOf("/") + 1, id.length() - 4));
            // songs.setArtist(artist);
            songs.setSize(getFileSize(new File(id).length()));
            videoActivitySongsList.add(songs);

        }


        itemAdapter = new CustomListAdapter(getApplicationContext(), videoActivitySongsList, "");
        recyclerViewVideos.setAdapter(itemAdapter);
        // recyclerViewVideos.getRecycledViewPool().clear();
        itemAdapter.notifyDataSetChanged();

    }*/

    /*private double getFileSize(String path) {

        File file = new File(path);


        long sizeInBytes = file.length();
//transform in MB
        double sizeInMb = sizeInBytes / (1024 * 1024);


        System.out.println("size in mbs " + sizeInMb);
        return sizeInMb;
    }*/


    /*public static String getStringSizeLengthFile(long size) {

        DecimalFormat df = new DecimalFormat("0.00");

        float sizeKb = 1024.0f;
        float sizeMo = sizeKb * sizeKb;
        float sizeGo = sizeMo * sizeKb;
        float sizeTerra = sizeGo * sizeKb;


        if (size < sizeMo)
            return df.format(size / sizeKb) + " Kb";
        else if (size < sizeGo)
            return df.format(size / sizeMo) + " Mo";
        else if (size < sizeTerra)
            return df.format(size / sizeGo) + " Go";

        return "";
    }*/

/*
    public void loadvideos2(String posi){
        int i = 0;
        backcheck = true;
        folders_list.clear();

        videoActivitySongsList.clear();
        int posi1 = Integer.parseInt(posi);

        select_query = "select * from diffvideos where position ='"+posi1+"'";
        Log.e("select query", select_query);
        c = db.selectQuery(select_query);
        if (c.getCount() > 0) {
            if (c.moveToFirst() && c != null) {
                do {
                    i++;
                    if (isOnline()) {
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
                        videoSongsAdapter = new VideoSongsAdapter5(this, videoActivitySongsList);
                        recyclerViewVideos.setAdapter(videoSongsAdapter);
                        recyclerViewVideos.getRecycledViewPool().clear();
                        videoSongsAdapter.notifyDataSetChanged();


                }
                while (c.moveToNext());
            }
        }

    }
    */

    /*private class AsyncTaskRunnershowdata extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            //   showData();
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // dialog1.dismiss();
        }

        @Override
        protected void onPreExecute() {
            // dialog1.show();
        }


        @Override
        protected void onProgressUpdate(String... text) {
        }
    }*/


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


    private void onResumeLoad() {

        AppClass.folder.clear();

        Iterator it = AppClass.dirList.entrySet().iterator();

        System.out.println("if condition true view " + AppClass.dirList.size());

        while (it.hasNext()) {

            Map.Entry pairs = (Map.Entry) it.next();


            System.out.println("if condition true video2 " + pairs.getValue().toString() + " " + pairs.getKey().toString());

            FolderEntity entity = new FolderEntity();

            entity.setName(pairs.getValue().toString());
            entity.setPath(pairs.getKey().toString());
            AppClass.folder.add(entity);
        }


        // Collections.sort(StorageUtil.folder,null);
        Collections.sort(AppClass.folder, new Comparator<FolderEntity>() {
            public int compare(FolderEntity obj1, FolderEntity obj2) {
                // ## Ascending order
                return obj1.name.compareToIgnoreCase(obj2.name); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
            }
        });

        folderAdapter.notifyDataSetChanged();


    }

    private class AsyncLoadVideosAndFolder extends AsyncTask<Boolean, Integer, List<Folder>> {
        boolean isRefresh;
        String sorter;

        private AsyncLoadVideosAndFolder(String params) {
            progressDialog.setVisibility(View.VISIBLE);
            sorter = params;
        }

        protected void onPreExecute() {
//            progressDialog.setVisibility(View.VISIBLE);
//            emptyTextview.setVisibility(View.GONE);
        }

        @SuppressLint("LongLogTag")
        protected List<Folder> doInBackground(Boolean... args) {
            try {
                VideosAndFoldersUtility videosAndFoldersUtility = new VideosAndFoldersUtility(VideoFolder.this);
                VideoFolder.videos = videosAndFoldersUtility.fetchAllVideos();
                VideoFolder.folders = videosAndFoldersUtility.fetchAllFolders();
//                Collections.sort(VideoFolder.folders, new compareFolders());

            } catch (RuntimeException e) {
            }
            return VideoFolder.folders;
        }


        @SuppressLint("LongLogTag")
        protected void onPostExecute(List<Folder> result) {
            progressDialog.setVisibility(View.GONE);

            try {
//            progressDialog.setVisibility(View.GONE);
//            emptyTextview.setVisibility(View.GONE);
                if (sorter.equalsIgnoreCase("name")) {
                    sortListByName(result);
                    folderAdapter = new FolderAdapter(VideoFolder.this, result, true);
//                    Log.i("foldername2", VideoFolder.folders.get(1).getPath() + "");
                    recyclerViewFolders.setAdapter(folderAdapter);
                    folderAdapter.notifyDataSetChanged();
                } else if (sorter.equalsIgnoreCase("size")) {
                    sortBySize();
                    folderAdapter = new FolderAdapter(VideoFolder.this, result, true);
//                    Log.i("foldername2", VideoFolder.folders.get(1).getPath() + "");
                    recyclerViewFolders.setAdapter(folderAdapter);
                    folderAdapter.notifyDataSetChanged();
                } else if (sorter.equalsIgnoreCase("date")) {
                    sortListByDate(VideoFolder.folders);
                    folderAdapter = new FolderAdapter(VideoFolder.this, result, true);
//                    Log.i("foldername2", VideoFolder.folders.get(1).getPath() + "");
                    recyclerViewFolders.setAdapter(folderAdapter);
                    folderAdapter.notifyDataSetChanged();
                }
            } catch (IndexOutOfBoundsException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
            } catch (ActivityNotFoundException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
            } catch (SecurityException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
            } catch (IllegalStateException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
            } catch (NullPointerException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
            } catch (OutOfMemoryError e) {
                Log.e("ExceptionError", " = " + e.getMessage());
            } catch (RuntimeException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
            } catch (Exception e) {
                Log.e("ExceptionError", " = " + e.getMessage());
            } finally {
                Log.e("ExceptionError", " = Finally");
            }

        }
    }

    class compareFolders implements Comparator<Folder> {
        compareFolders() {
        }

        public int compare(Folder folder1, Folder folder2) {
            return folder1.getName().compareTo(folder2.getName());
        }
    }

    @SuppressLint("LongLogTag")
    public void getAllFolders(String sorter) {
        try {
            VideosAndFoldersUtility videosAndFoldersUtility = new VideosAndFoldersUtility(VideoFolder.this);
            VideoFolder.videos = videosAndFoldersUtility.fetchAllVideos();
            VideoFolder.folders = videosAndFoldersUtility.fetchAllFolders();
            Collections.sort(VideoFolder.folders, new compareFolders());
            try {
//            progressDialog.setVisibility(View.GONE);
//            emptyTextview.setVisibility(View.GONE);
                if (sorter.equalsIgnoreCase("name")) {
                    sortListByName(VideoFolder.folders);
                    folderAdapter = new FolderAdapter(VideoFolder.this, VideoFolder.folders, true);
                    Log.i("foldername2", VideoFolder.folders.get(1).getPath() + "");
                    recyclerViewFolders.setAdapter(folderAdapter);
                    folderAdapter.notifyDataSetChanged();
                } else if (sorter.equalsIgnoreCase("size")) {
                    sortBySize();
                    folderAdapter = new FolderAdapter(VideoFolder.this, VideoFolder.folders, true);
                    Log.i("foldername2", VideoFolder.folders.get(1).getPath() + "");
                    recyclerViewFolders.setAdapter(folderAdapter);
                    folderAdapter.notifyDataSetChanged();
                } else if (sorter.equalsIgnoreCase("date")) {
                    sortListByDate(VideoFolder.folders);
                    folderAdapter = new FolderAdapter(VideoFolder.this, VideoFolder.folders, true);
                    Log.i("foldername2", VideoFolder.folders.get(1).getPath() + "");
                    recyclerViewFolders.setAdapter(folderAdapter);
                    folderAdapter.notifyDataSetChanged();
                }
            } catch (IndexOutOfBoundsException e) {
                Log.i("IndexOutOfBoundsException", e.getMessage());
            }
        } catch (IndexOutOfBoundsException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (ActivityNotFoundException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (SecurityException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (IllegalStateException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (OutOfMemoryError e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (RuntimeException e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } catch (Exception e) {
            Log.e("ExceptionError", " = " + e.getMessage());
        } finally {
            Log.e("ExceptionError", " = Finally");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                {
                    {
                        {
                            AsyncLoadVideosAndFolder asyncLoadVideosAndFolder = new AsyncLoadVideosAndFolder("name");
                            asyncLoadVideosAndFolder.execute();
                        }
                    }
                }
            } else {
                checkStoragePermission();
                checkStoragePermission1();
            }
        }
    }


    private boolean checkStoragePermission1() {
        return ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


    }


    public void localdata(boolean isResumed) {

        try {
//          Collection<String> folder= (List<String>) StorageUtil.dirList.values();


            AppClass.folder.clear();
//            Set<String> set = mSharedPrefs.getStringSet("folderlist", null);
            Iterator it = AppClass.dirList.entrySet().iterator();

            Log.i("SizeofFolders", AppClass.dirList.size() + "");
            while (it.hasNext()) {

                Map.Entry pairs = (Map.Entry) it.next();

//                System.out.println("Allfolders" );
                Log.i("Allfolders", " = " + pairs.getValue().toString() + " " + pairs.getKey().toString());
                FolderEntity entity = new FolderEntity();
                entity.setName(pairs.getValue().toString());
                entity.setPath(pairs.getKey().toString());
                AppClass.folder.add(entity);
            }


            // Collections.sort(StorageUtil.folder,null);
            Collections.sort(AppClass.folder, new Comparator<FolderEntity>() {
                public int compare(FolderEntity obj1, FolderEntity obj2) {
                    // ## Ascending order
                    return obj1.name.compareToIgnoreCase(obj2.name); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
                }
            });

//            folderAdapter = new FolderAdapter(VideoFolder.this,AppClass.folder, isResumed);
//            recyclerViewFolders.setAdapter(folderAdapter);
//            folderAdapter.notifyDataSetChanged();
//            folderAdapter.notifyItemRangeChanged(0, AppClass.folder.size());
//        recyclerViewFolders.getRecycledViewPool().clear();

            //emptyTextview.setVisibility(View.GONE);
        } catch (RuntimeException e) {
        } catch (Exception e) {
        }
    }

    private void moveFiles(File file, File dir) throws IOException {
        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        MenuItem name = menu.findItem(R.id.action_name);
        MenuItem date = menu.findItem(R.id.action_date);
        MenuItem size = menu.findItem(R.id.action_size);

        View view = getLayoutInflater().inflate(R.layout.radiomenulayout, null, false);

        RadioButton switchCompat = (RadioButton) view.findViewById(R.id.switchForActionBar);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(VideoFolder.this, "" + isChecked, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VideoFolder.this, "" + isChecked, Toast.LENGTH_SHORT).show();
                }
            }
        });
        size.setActionView(view);
        name.setActionView(view);
        date.setActionView(view);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.ic_frame) {
            if (listGird) {
                listGird = false;
                item.setIcon(R.drawable.ic_list_white);
                recyclerViewFolders.setLayoutManager(new GridLayoutManager(this, 2));
            } else {
                listGird = true;
                item.setIcon(R.drawable.ic_grid_white);
                recyclerViewFolders.setLayoutManager(new GridLayoutManager(this, 1));
            }
        }
        //noinspection SimplifiableIfStatement
        else if (id == R.id.sycn) {
            sycn();
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        } else if (id == R.id.action_name) {
            getAllFolders("name");
            item.setChecked(true);
            return true;
        } else if (id == R.id.action_size) {
            getAllFolders("size");
            item.setChecked(true);
            return true;
        } else if (id == R.id.action_date) {
            getAllFolders("date");
            item.setChecked(true);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public void sortByName() {
        Collections.sort(VideoFolder.videos, new compareVideos());
    }

    public void sortBySize() {
        Collections.sort(VideoFolder.folders, new Comparator<Folder>() {
            @Override
            public int compare(Folder lhs, Folder rhs) {
                long a = (lhs.getTotalVideos());
                long b = (rhs.getTotalVideos());
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return a < b ? -1 : (a > b ? 1 : 0);
            }
        });
    }

    class compareVideos implements Comparator<Video> {
        compareVideos() {
        }

        public int compare(Video folder1, Video folder2) {
            return folder1.getName().compareTo(folder2.getName());
        }
    }
    /*]=====Sorting=====[*/

    private void sortListByName(List<Folder> theArrayListEvents) {
        Collections.sort(theArrayListEvents, new EventDetailSortByName());
    }

    private class EventDetailSortByName implements java.util.Comparator<Folder> {
        @Override
        public int compare(Folder customerEvents1, Folder customerEvents2) {
            String name1, name2;
            name1 = customerEvents1.getName().toLowerCase().trim();
            name2 = customerEvents2.getName().toLowerCase().trim();
            return name1.compareTo(name2);
        }
    }

    private void sortListByDate(List<Folder> theArrayListEvents) {
        Collections.sort(theArrayListEvents, new EventDetailSortByDate());
    }

    private class EventDetailSortByDate implements java.util.Comparator<Folder> {
        @Override
        public int compare(Folder customerEvents1, Folder customerEvents2) {
            Date DateObject1 = StringToDate(customerEvents1.getName());
            Date DateObject2 = StringToDate(customerEvents2.getName());

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(DateObject1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(DateObject2);

            int month1 = cal1.get(Calendar.MONTH);
            int month2 = cal2.get(Calendar.MONTH);

            if (month1 < month2)
                return -1;
            else if (month1 == month2)
                return cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);

            else return 1;
        }
    }

    public static Date StringToDate(String theDateString) {
        Date returnDate = new Date();
        if (theDateString.contains("-")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                returnDate = dateFormat.parse(theDateString);
            } catch (ParseException e) {
                SimpleDateFormat altdateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    returnDate = altdateFormat.parse(theDateString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                returnDate = dateFormat.parse(theDateString);
            } catch (ParseException e) {
                SimpleDateFormat altdateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    returnDate = altdateFormat.parse(theDateString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return returnDate;
    }


    public void sycn() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (checkStoragePermission()) {
                        AsyncLoadVideosAndFolder asyncLoadVideosAndFolder = new AsyncLoadVideosAndFolder("name");
                        asyncLoadVideosAndFolder.execute();
                    } else {
                        requestStoragePermission();
                    }
                }
            });
        } catch (Exception ff) {

        }


        try {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    itemAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception ff) {

        }
    }

    private void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        try {
            adcheck = false;
            folders_list.clear();
            folders_path.clear();

            videoActivitySongsList.clear();
            pathTextView.setText("Video Folders");

            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol, 0);
            mState2 = 1;
            mState = 1;
            invalidateOptionsMenu();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String jj = "nofolder";
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("folder", jj);
            editor.commit();
            folderclick = mSharedPrefs.getString("folder", "true");
            if (backcheck) {
                videoActivitySongsList.clear();
                recyclerViewFolders.setVisibility(View.VISIBLE);
                recyclerViewVideos.setVisibility(View.GONE);
                backcheck = false;
//            mAdView.setVisibility(View.GONE);
                return;
//            Intent i = new Intent(VideoFolder.this,MainActivity_Front.class);
//            startActivity(i);
//            finish();
            } else {
                //  localdata(true);

            }

            this.doubleBackToExitPressedOnce = true;

            if (doubleBackToExitPressedOnce) {

                network_stream(1);
                return;
            }


            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } catch (IndexOutOfBoundsException e) {
        } catch (RuntimeException e) {
        } catch (Exception e) {
        }
    }


    void network_stream(int checkCondition) {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(VideoFolder.this);
        dialogBuilder.setCancelable(true);

// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rate_us_demo, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();


        // Button cancel = (Button) dialogView.findViewById(R.id.rateUsCancel);
        Button ok = (Button) dialogView.findViewById(R.id.rateUsOk);
        Button quit = (Button) dialogView.findViewById(R.id.quit);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                alertDialog.dismiss();

            }
        });


        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkCondition == 0)
                {
                    alertDialog.dismiss();
                }else if (checkCondition == 1)
                {
                    alertDialog.dismiss();
                    finishAffinity();
                }

            }
        });


        alertDialog.show();

    }

    public static class AudioFilter implements FileFilter {

        private String[] extension = {".3gp", "3g2",
                ".mp4", ".m4p", ".m4v", ".mxf", ".m2v", ".wav",
                ".flv", ".webm", ".flac", ".f4v", "f4p", "f4a", "f4b", ".ogv", ".gif"
                , ".mng", ".au", ".svi", ".snd", ".mid", ".midi", ".kar"
                , ".mga", ".avi", ".rm", ".rmvb", ".asf",
                ".aif", ".aiff", ".aifc", ".oga",
                ".spx", ".mkv", ".m4a", ".amv", ".nsv"
                , "wmv", ".avi", ".mov", ".qt", ".yuv", ".vob"
        };

        @Override
        public boolean accept(File pathname) {

            // if we are looking at a directory/file that's not hidden we want to see it so return TRUE
            if ((pathname.isDirectory() || pathname.isFile()) && !pathname.isHidden()) {
                return true;
            }

            // loops through and determines the extension of all files in the directory
            // returns TRUE to only show the audio files defined in the String[] extension array
            for (String ext : extension) {
                if (pathname.getName().toLowerCase().endsWith(ext) || pathname.getName().toUpperCase().endsWith(ext)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public RecyclerItemClickListener() {

        }

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
                try {
                    mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                } catch (Exception dd) {

                }
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_eq) {
            equiliazerScreen.setVisibility(View.VISIBLE);
            drawer.closeDrawers();
            equalizerFragment = new CustomFragment2()
                    .newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(0)
                    .build();
            SessionStorage sessionStorage = new SessionStorage(getApplicationContext());
            sessionStorage.storeSession(4567);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_view, equalizerFragment)
                    .commit();
        } else if (id == R.id.nav_folders) {
            startActivity(new Intent(VideoFolder.this, VideoFolder.class));
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_video) {
            startActivity(new Intent(VideoFolder.this, AllVideosActivity.class));
            finish();
        }else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String title = "Share Video  with Freinds";
            String link = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
            intent.putExtra(Intent.EXTRA_SUBJECT, title);
            intent.putExtra(Intent.EXTRA_TEXT, link);
            startActivity(Intent.createChooser(intent, "Share XM videoplayer using"));
        } else if (id == R.id.nav_rate) {
            network_stream(0);
            return true;
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, About.class));
            return true;
        }


        return true;
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

    class ImageDownloaderTask extends AsyncTask<String, Void, String> {


        int k = 0;
        ArrayList<String> data;


        @Override
        protected String doInBackground(String... params) {

            try {
                System.out.println("FileExplorer.subDirs.size() 1 " + AppClass.subDirs.size());

                mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPrefs.edit();

                mEditor.putInt("cache_size", AppClass.subDirs.size());
                mEditor.commit();

                //  System.out.println("get file name in background "+FileExplorer.subDirs.size());
                for (; k < AppClass.subDirs.size(); k++) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                        }
                    }, 100);

                    String name = AppClass.subDirs.get(k);

                    String path = name.substring(name.lastIndexOf("/") + 1, name.length() - 4);

                    String createFolder = Environment.getExternalStorageDirectory().getPath() + "/.thumbs";

                    File file = new File(createFolder + "/" + path + ".png");

                    if (!file.exists()) {

                        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(name, MediaStore.Video.Thumbnails.MICRO_KIND);

                        //System.out.println("get file name 2 "+bMap);
                        try {
                            saveeFile(bMap, name);
                        } catch (Exception d) {

                        }
                    }


                    try {
                        if (k == AppClass.subDirs.size() - 1) {


                            break;
                        }

                    } catch (Exception g) {
                        break;
                    }
                }

            } catch (RuntimeException e) {
            }
            return "";
        }

        @Override
        protected void onPostExecute(String bitmap) {


        }


    }

    public void saveeFile(Bitmap bmp, String path) {


        path = path.substring(path.lastIndexOf("/") + 1, path.length() - 4);


        String createFolder = Environment.getExternalStorageDirectory().getPath() + "/.thumbs";

        // System.out.println("file path  "+path+" "+createFolder);

        try {
            FileOutputStream out = new FileOutputStream(createFolder + "/" + path + ".png");
            bmp.compress(Bitmap.CompressFormat.PNG, 60, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}