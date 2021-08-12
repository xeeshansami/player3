package com.play.view.videoplayer.hd;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.play.view.videoplayer.hd.CursorUtils.Constants;
import com.play.view.videoplayer.hd.CursorUtils.ResizeSurfaceView;
import com.play.view.videoplayer.hd.CursorUtils.VideosAndFoldersUtility;
import com.play.view.videoplayer.hd.CursorUtils.VideosListAdapterForMediaPlayer;
import com.play.view.videoplayer.hd.Model.Video;

import com.play.view.videoplayer.hd.equalizer.CustomFragment;
import com.play.view.videoplayer.hd.equalizer.SessionStorage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION;
import static android.provider.Settings.canDrawOverlays;

public class VideoDetailActivityFliper extends AppCompatActivity implements
        GestureDetector.OnGestureListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener,
        TextureView.SurfaceTextureListener {
    //    private GestureDetector mGestureDetector;
    boolean mDragging = false;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.view.android.playerapp.PlayNewAudio";
    private MediaPlayerServiceVideo playerService;
    boolean serviceBoundVideo = false;
    private ScaleGestureDetector mScaleGestureDetector;
    private int video_column_index;
    private String mScreenSize = Constants.FIT_SCREEN;
    private int mMaxHeight = -1;
    private int mMaxWidth = -1;
    private int mMinHeight = -1;
    private int mMinWidth = -1;
    private int mPreviousHeight = -1;
    private int mPreviousWidth = -1;
    Boolean aBoolean = false;
    RelativeLayout relativbAck;
    AudioManager audioManager;
    String gestvalue;
    ArrayList<String> videoActivitySongsList;
    float volume;
    static String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID};
    Cursor videoCursor;
    RecyclerView recyclerView;
    int videoIndex;
    boolean nightmode = false, timer = false;
    VideosListAdapterForMediaPlayer videoSongsAdapter;
    ArrayList<VideoSongs> videoSongsList = new ArrayList<VideoSongs>();
    private DrawerLayout drawer;
    private String filename = "", title = "", albu = "", artist = "";
    private String filenameInside = "", titleInside = "", albuInside = "", artistInside = "";
    private int id, idInside;
    Integer s;
    LinearLayout backgroundModeLay;
    private ImageButton orientation, next, prev, play_pause, locker, videoSizeChanger;
    //    private VodView videoView;
    private boolean state = false;
    public FrameLayout equiliazerScreen;
    private boolean show = false;
    private int songPosn = 0;
    private boolean shuffleSong = false;
    private Random rand;
    int mProgress = 0;
    private SeekBar seekbar, seekForRev, volumeControl, brightcontrol;
    public static int prog;
    private TextView ablumVideo, sizeChangerText, current, runingduration, ontouchShow, seekTouchDurationTotal, voltouchshow, brighttouchshow;
    private long dura, curr;
    ImageButton share, floating, vol, ratio;
    ImageView three60, background, openlist, openlist_2, eq;
    int brightness;
    float perc;
    LinearLayout ontouch, ontouchvol, ontouchLayoutseek;
    public LinearLayout brightShowLayout, volShowLayout, seekTouchLayout;
    private Toolbar toolbar;
    int brightprogress;
    int volconvert;
    FrameLayout container;
    SharedPreferences mSharedPrefs = null;
    Dialog dialog, dialog2, dialog3;
    TextView sleeper1, sleeper2, sleeper3, sleeper4, sleeperoff;
    int backprogress1;
    int threeprogress;
    boolean backgroundIsOnOff = true, isSingleplayOnOff = true, isShuffleOnOff = true;
    private boolean true1 = false;
    String true2 = "", true360two = "";
    Boolean true360 = false;
    boolean stimercheck = true;
    int count, stimer;
    String finaltrue;
    private FrameLayout.LayoutParams mRootParam;
    private int APP_PERMISSION_REQUEST = 1220;
    private boolean isShowing = false;
    public LinearLayout controller;
    public RelativeLayout relcon1, relcon2;
    private int maxVolume;
    private boolean check = false;
    private Handler mHandler;
    private int mInterval;
    private ImageView brightupper, volupper, seekupper;
    private int currentVolume;
    private boolean scrollstate = true;
    private static final String DEBUG_TAG = "Gestures_";
    private GestureDetectorCompat mDetector;
    GestureDetector gestureDetector;
    private int heightdp;
    private int volprogess = 0;
    private int widthdp;
    float delx, dely, deltaX, deltaY, halfwidth, halfheight, delx1, lastY;
    int checkin = 0;
    private ContentResolver cResolver;
    public boolean mIsScrolling = true;

    private DisplayMetrics dm;
    int MIN_WIDTH = 600;
    boolean isLock = false;
    MediaPlayer mMediaPlayer;
    private int MAX_VOLUME = 100;
    private ResizeSurfaceView textureView;
    android.view.ViewGroup.LayoutParams lp;
    private int screenWidth, screenHeight;
    private int clickCount;
    private final int SWIPE_VELOCITY_THRESHOLD = 200;
    RelativeLayout sleepertimer;
    private boolean showButtons;
    private boolean showNoti = false;
    private AlertDialog alertDialog;
    ProgressDialog progressDialog;
    ProgressDialog prodialog;
    private AudioManager mAudioManager;
    private int songPosnPre;
    CustomFragment equalizerFragment;
    int floatprogress;
    private float SWIPE_THRESHOLDVOl = 10;
    private float SWIPE_THRESHOLDSEEKBAR = 10;
    private float SWIPE_THRESHOLDSEEK = 10;
    private float SWIPE_THRESHOLDBRIGHT = 10;
    private float SWIPE_THRESHOLD = 10;
    int backprogress = 0;
    private String scroll = null;
    private int oreintat;
    private String resolVideo, file_name;
    int videoWidth;
    //DataBaseManager db;
    int videoHeight;
    private Cursor videoCursorActivity;
    private List<Video> myList;
    int folderposition;
    private Video videosongsClass;
    private float oldTouchValue;
    int playback;
    int curBrightnessValue;
    private int screenorientation;
    Boolean bolseek, boolvol, boolbri, boolmute = false;
    public ArrayList<String> recent, recentpath;
    String insert_query;
    int curr_value;
    int currentVol;
    Bundle extras;
    int brightnessValue;
    Context context;

    // Get the screen current brightness
    protected int getScreenBrightness() {
        try {
            brightnessValue = Settings.System.getInt(
                    getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS, 0);
            System.out.println("Sysytem bright" + brightnessValue);
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
        return brightnessValue;
    }

    public void getDeviceWidthAndHeight() {
        try {
            lp = textureView.getLayoutParams();

            screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            screenHeight = getWindowManager().getDefaultDisplay().getHeight();
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

    boolean horizontalcheck = true;
    HorizontalScrollView horizontalScrollView, horizontalScrollViewright;
    public ImageView arrow, arrowright;
    ImageView arrowvideo, option1, option2, option3, option4, option5, option6, shuffle_btn, option8, option9, single_btn, option11, option12, option13, arrowvideoclose;
    SharedPreferences.Editor mEditor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ClickableViewAccessibility", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_video_detail);
            audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            mEditor = mSharedPrefs.edit();
            curBrightnessValue = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            bolseek = mSharedPrefs.getBoolean("checked1", true);
            boolvol = mSharedPrefs.getBoolean("checked2", true);
            boolbri = mSharedPrefs.getBoolean("checked3", true);
            Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            screenorientation = display.getOrientation();
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volprogess = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            recent = new ArrayList<String>();
            recentpath = new ArrayList<String>();
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(false);
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            heightdp = displayMetrics.heightPixels;
            widthdp = displayMetrics.widthPixels;
            System.out.println(widthdp + "\\" + heightdp);
            System.out.println(widthdp / 2);
            getWindow().setFormat(PixelFormat.UNKNOWN);
            drawer = findViewById(R.id.inside_drawer_layout);
            mDetector = new GestureDetectorCompat(this, this);
            // Detect touched area
            mScaleGestureDetector = new ScaleGestureDetector(this, new MyScaleGestureDetector());
            // Create a GestureDetector
            toolbar = (Toolbar) findViewById(R.id.toolbar_video);
            stopService(new Intent(this, FloatWidgetService.class));
            controller = (LinearLayout) findViewById(R.id.controller);
            ratio = findViewById(R.id.ratio);
            three60 = findViewById(R.id.three60);
            share = findViewById(R.id.n_share);
            openlist = findViewById(R.id.n_open_list);
            openlist_2 = findViewById(R.id.openlist_2);
            background = findViewById(R.id.option3);
            floating = findViewById(R.id.n_floating);
            vol = findViewById(R.id.n_vol);
            runingduration = (TextView) findViewById(R.id.running);
            current = (TextView) findViewById(R.id.duration);
            ontouchShow = (TextView) findViewById(R.id.seekTouch);
            seekTouchDurationTotal = (TextView) findViewById(R.id.seekTouchDurationTotal);
            voltouchshow = (TextView) findViewById(R.id.volseekTouch);
            sizeChangerText = (TextView) findViewById(R.id.sizeChangerText);
            brighttouchshow = (TextView) findViewById(R.id.brightseekTouch);
            brightShowLayout = (LinearLayout) findViewById(R.id.brightLayout);
            volShowLayout = (LinearLayout) findViewById(R.id.volseekTouchLayout);
            seekTouchLayout = findViewById(R.id.seekTouchLayout);
            brightupper = (ImageView) findViewById(R.id.brightupper);
            volupper = (ImageView) findViewById(R.id.volupper);
            seekupper = (ImageView) findViewById(R.id.seekupper);
            equiliazerScreen = (FrameLayout) findViewById(R.id.root_view2);
            option5 = findViewById(R.id.option5);
            option6 = findViewById(R.id.option6);
            shuffle_btn = findViewById(R.id.option7);
            option8 = findViewById(R.id.option8);
            option9 = findViewById(R.id.option9);
            single_btn = findViewById(R.id.option10);
            Log.i("volumeDani0", "" + volprogess);
            dialog2 = new Dialog(this);
            dialog2.setCancelable(false);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setContentView(getLayoutInflater().inflate(R.layout.timer, null));
            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            sleepertimer = findViewById(R.id.sleepertimer);
            sleeper1 = dialog2.findViewById(R.id.sleeper1);
            sleeper2 = dialog2.findViewById(R.id.sleeper2);
            sleeper3 = dialog2.findViewById(R.id.sleeper3);
            sleeper4 = dialog2.findViewById(R.id.sleeper4);
            sleeperoff = dialog2.findViewById(R.id.sleeperoff);
            ablumVideo = (TextView) findViewById(R.id.ablumVideo);
            orientation = (ImageButton) findViewById(R.id.orien);
            next = (ImageButton) findViewById(R.id.next);
            prev = (ImageButton) findViewById(R.id.prev);
            play_pause = (ImageButton) findViewById(R.id.play_pause);
            locker = (ImageButton) findViewById(R.id.locker);
            videoSizeChanger = (ImageButton) findViewById(R.id.videoSizeChanger);
            textureView = findViewById(R.id.videoView);
            textureView.setSurfaceTextureListener(this);
            videoActivitySongsList = new ArrayList<>();
            audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            horizontalScrollView = findViewById(R.id.horizontal);
            horizontalScrollViewright = findViewById(R.id.horizontalright);
            equiliazerScreen = (FrameLayout) findViewById(R.id.root_view2);
            seekbar = (SeekBar) findViewById(R.id.seekBar);
            seekForRev = (SeekBar) findViewById(R.id.seekForRev);
            volumeControl = (SeekBar) findViewById(R.id.volumeSeek);
            brightcontrol = (SeekBar) findViewById(R.id.brightseek);
            brightprogress = getScreenBrightness();
            brightcontrol.setProgress(brightprogress);
            arrow = findViewById(R.id.arrowleft);
            arrowright = findViewById(R.id.arrowright);
            sleeper1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        stimercheck = false;
                        dialog2.dismiss();
                        mMediaPlayer.start();
                        play_pause.setImageResource(R.drawable.ic_pause_white_24dp);
                        int minutes = 15;
                        long millis = minutes * 60 * 1000;
                        stimer = (int) millis;
                        Toast.makeText(getApplicationContext(), "Player Will Be Close After 15 Mins", Toast.LENGTH_SHORT).show();
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                releaseMediaPlayer();
                                finish();
                            }
                        }, stimer);
                        sleepertimer.setVisibility(View.GONE);
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
            });
            sleeper2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        stimercheck = false;
                        dialog2.dismiss();
                        mMediaPlayer.start();
                        play_pause.setImageResource(R.drawable.ic_pause_white_24dp);
                        int minutes = 30;
                        long millis = minutes * 60 * 1000;
                        stimer = (int) millis;
                        Toast.makeText(getApplicationContext(), "Player Will Be Close After 30 Mins", Toast.LENGTH_SHORT).show();
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                releaseMediaPlayer();
                                finish();
                            }
                        }, stimer);
                        sleepertimer.setVisibility(View.GONE);
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
            });
            sleeper3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        stimercheck = false;
                        dialog2.dismiss();
                        mMediaPlayer.start();
                        play_pause.setImageResource(R.drawable.ic_pause_white_24dp);
                        int minutes = 45;
                        long millis = minutes * 60 * 1000;
                        stimer = (int) millis;
                        Toast.makeText(getApplicationContext(), "Player Will Be Close After 45mins", Toast.LENGTH_SHORT).show();
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                releaseMediaPlayer();
                                finish();
                            }
                        }, stimer);
                        sleepertimer.setVisibility(View.GONE);
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
            });
            sleeper4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        stimercheck = false;
                        dialog2.dismiss();
                        mMediaPlayer.start();
                        play_pause.setImageResource(R.drawable.ic_pause_white_24dp);
                        int minutes = 60;
                        long millis = minutes * 60 * 1000;
                        stimer = (int) millis;
                        Toast.makeText(getApplicationContext(), "Player Will Be Close After 1hr", Toast.LENGTH_SHORT).show();
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                releaseMediaPlayer();
                                finish();
                            }
                        }, stimer);
                        sleepertimer.setVisibility(View.GONE);
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
            });
            sleeperoff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        stimercheck = false;
                        dialog2.dismiss();
                        mMediaPlayer.start();
                        play_pause.setImageResource(R.drawable.ic_pause_white_24dp);
                        int minutes = 0;
                        long millis = minutes * 60 * 1000;
                        stimer = (int) millis;
                        Toast.makeText(getApplicationContext(), "Timer Off", Toast.LENGTH_SHORT).show();
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                        releaseMediaPlayer();
////                        finish();
                            }
                        }, stimer);
                        sleepertimer.setVisibility(View.GONE);
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
            });
            eq = findViewById(R.id.eq);
            /*backgroundPlayCheck*/
            if (mSharedPrefs.getBoolean("checkNot", false)) {
                backgroundIsOnOff = false;
                background.setBackground(getResources().getDrawable(R.drawable.selected_circle));
            } else {
                backgroundIsOnOff = true;
                background.setBackground(getResources().getDrawable(R.drawable.circle2));
            }
            /*singlePlayCheck*/
            if (mSharedPrefs.getBoolean("single_play", false)) {
                isSingleplayOnOff = false;
                single_btn.setBackground(getResources().getDrawable(R.drawable.selected_circle));
            } else {
                isSingleplayOnOff = true;
                single_btn.setBackground(getResources().getDrawable(R.drawable.circle2));
            }
            /*shufflePlayCheck*/
            if (mSharedPrefs.getBoolean("shuffle_play", false)) {
                isShuffleOnOff = false;
                shuffle_btn.setBackground(getResources().getDrawable(R.drawable.selected_circle));
            } else {
                isShuffleOnOff = true;
                shuffle_btn.setBackground(getResources().getDrawable(R.drawable.circle2));
            }
            eq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int sessionId = mMediaPlayer.getAudioSessionId();
                        mMediaPlayer.setLooping(true);

                        equalizerFragment = new CustomFragment()
                                .newBuilder()
                                .setAccentColor(Color.parseColor("#FEAD02"))
                                .setAudioSessionId(0)
                                .build();

                        SessionStorage sessionStorage = new SessionStorage(getApplicationContext());
                        sessionStorage.storeSession(sessionId);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.root_view2, equalizerFragment)
                                .commit();

                        equiliazerScreen.setVisibility(View.VISIBLE);
                        horizontalScrollView.setVisibility(View.GONE);

                        if (equalizerFragment != null || equalizerFragment.isVisible() || equiliazerScreen.getVisibility() == View.VISIBLE) {
                            horizontalScrollView.setVisibility(View.GONE);
                            openlist.setVisibility(View.GONE);
                            openlist_2.setVisibility(View.GONE);
                            seekTouchLayout.setVisibility(View.GONE);
                            toolbar.setVisibility(View.GONE);
                            sizeChangerText.setVisibility(View.GONE);
                            brighttouchshow.setVisibility(View.GONE);
                            brightShowLayout.setVisibility(View.GONE);
                            volShowLayout.setVisibility(View.GONE);
                            seekTouchLayout.setVisibility(View.GONE);
                            seekupper.setVisibility(View.GONE);
                            volupper.setVisibility(View.GONE);
                            controller.setVisibility(View.GONE);
                            locker.setVisibility(View.GONE);
                            ontouchShow.setVisibility(View.GONE);
                            seekTouchDurationTotal.setVisibility(View.GONE);
                            voltouchshow.setVisibility(View.GONE);
                            sizeChangerText.setVisibility(View.GONE);
                            brighttouchshow.setVisibility(View.GONE);
                            brightShowLayout.setVisibility(View.GONE);
                            volShowLayout.setVisibility(View.GONE);
                            brightupper.setVisibility(View.GONE);
                            volupper.setVisibility(View.GONE);
                            seekupper.setVisibility(View.GONE);
                        } else {
                            horizontalScrollView.setVisibility(View.VISIBLE);
                            openlist.setVisibility(View.VISIBLE);
                            openlist_2.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.VISIBLE);
                            sizeChangerText.setVisibility(View.VISIBLE);
                            brighttouchshow.setVisibility(View.VISIBLE);
                            brightShowLayout.setVisibility(View.VISIBLE);
                            volShowLayout.setVisibility(View.VISIBLE);
                            seekTouchLayout.setVisibility(View.VISIBLE);
                            seekupper.setVisibility(View.VISIBLE);
                            volupper.setVisibility(View.VISIBLE);
                            controller.setVisibility(View.VISIBLE);
                            locker.setVisibility(View.VISIBLE);
                            ontouchShow.setVisibility(View.VISIBLE);
                            seekTouchDurationTotal.setVisibility(View.VISIBLE);
                            voltouchshow.setVisibility(View.VISIBLE);
                            sizeChangerText.setVisibility(View.VISIBLE);
                            brighttouchshow.setVisibility(View.VISIBLE);
                            brightShowLayout.setVisibility(View.VISIBLE);
                            volShowLayout.setVisibility(View.VISIBLE);
                            brightupper.setVisibility(View.VISIBLE);
                            volupper.setVisibility(View.VISIBLE);
                            seekupper.setVisibility(View.VISIBLE);
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
            });
            option9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!timer) {
                            horizontalScrollView.setVisibility(View.GONE);
                            mMediaPlayer.pause();
                            play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                            dialog2.show();
                            //sleepertimer.setVisibility(View.VISIBLE);
                        } else {
                            timer = false;
                            sleepertimer.setVisibility(View.GONE);
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
            });
            option6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        releaseMediaPlayer();
                        stopMedia();
                        finish();
                        Intent intent = new Intent(VideoDetailActivityFliper.this, Main4Activity.class);
                        intent.putExtra("FilenameThreeSixty", filename);
                        intent.putExtra("title", title);
                        intent.putExtra("id", id);
                        intent.putExtra("folderposition", folderposition);
                        intent.putExtra(Constants.VIDEO_INDEX, videoIndex);
                        intent.putExtra("progress", curr);
                        intent.putExtra("Showbuttons", false);
                        intent.putExtra("ShowNoti", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
            });
            shuffle_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
//                        horizontalScrollView.setVisibility(View.GONE);
                        if (isShuffleOnOff) {
                            isShuffleOnOff = false;
                            isSingleplayOnOff=true;
                            shuffle_btn.setBackground(getResources().getDrawable(R.drawable.selected_circle));
                            if (mSharedPrefs.getBoolean("single_play", false)) {
                                single_btn.setBackground(getResources().getDrawable(R.drawable.circle2));
                            }
                            mEditor.putBoolean("shuffle_play", true);
                            mEditor.putBoolean("single_play", false);
                            mMediaPlayer.setLooping(false);
                            mEditor.commit();
                            Toast.makeText(getApplicationContext(), "Shuffle Enabled", Toast.LENGTH_SHORT).show();
                            Log.i("mediaLogCat", "Shuffle Enabled");
                        } else {
                            isShuffleOnOff = true;
                            mEditor.putBoolean("shuffle_play", false);
                            mEditor.commit();
                            shuffle_btn.setBackground(getResources().getDrawable(R.drawable.circle2));
                            Toast.makeText(getApplicationContext(), "Shuffle Disabled", Toast.LENGTH_SHORT).show();
                            Log.i("mediaLogCat", "Shuffle Disabled");
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
            });
            single_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
//                        horizontalScrollView.setVisibility(View.GONE);
                        if (isSingleplayOnOff) {
                            isSingleplayOnOff = false;
                            isShuffleOnOff=true;
                            mMediaPlayer.setLooping(true);
                            single_btn.setBackground(getResources().getDrawable(R.drawable.selected_circle));
                            if (mSharedPrefs.getBoolean("shuffle_play", false)) {
                                shuffle_btn.setBackground(getResources().getDrawable(R.drawable.circle2));
                            }
                            mEditor.putBoolean("single_play", true);
                            mEditor.putBoolean("shuffle_play", false);
                            mEditor.commit();
                            Toast.makeText(getApplicationContext(), "Single Play Active", Toast.LENGTH_SHORT).show();
                            Log.i("mediaLogCat", "Single Play Active");
                        } else {
                            mEditor.putBoolean("single_play", false);
                            mEditor.commit();
                            isSingleplayOnOff = true;
                            mMediaPlayer.setLooping(false);
                            single_btn.setBackground(getResources().getDrawable(R.drawable.circle2));
                            Toast.makeText(getApplicationContext(), "Single Play Deactivated", Toast.LENGTH_SHORT).show();
                            Log.i("mediaLogCat", "Single Play Deactivated");
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
            });
            option8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        horizontalScrollView.setVisibility(View.GONE);
                        if (!nightmode) {
                            nightmode = true;
                            Toast.makeText(getApplicationContext(), "Night Mode Active", Toast.LENGTH_SHORT).show();
                            WindowManager.LayoutParams params = getWindow().getAttributes();
                            params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                            params.screenBrightness = 0.1f;
                            getWindow().setAttributes(params);
                            option8.setBackground(getResources().getDrawable(R.drawable.selected_circle));
                        } else {
                            nightmode = false;
                            Toast.makeText(getApplicationContext(), "Night Mode Deactivated", Toast.LENGTH_SHORT).show();
                            WindowManager.LayoutParams params = getWindow().getAttributes();
                            params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                            params.screenBrightness = -1;
                            getWindow().setAttributes(params);
                            option8.setBackground(getResources().getDrawable(R.drawable.circle2));
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
            });
            option5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        onAlertSubTitle();
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
            });
            arrowright.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Animation animation = AnimationUtils.loadAnimation(VideoDetailActivityFliper.this, R.anim.left_from_right);
                        horizontalScrollViewright.startAnimation(animation);
                        horizontalScrollViewright.setVisibility(View.VISIBLE);
                        arrow.setVisibility(View.VISIBLE);
                        equiliazerScreen.setVisibility(View.GONE);
                        arrowright.setVisibility(View.GONE);
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
            });
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Animation animation = AnimationUtils.loadAnimation(VideoDetailActivityFliper.this, R.anim.right_from_left);
                        horizontalScrollViewright.startAnimation(animation);
                        horizontalScrollViewright.setVisibility(View.GONE);
                        arrowright.setVisibility(View.VISIBLE);
                        arrow.setVisibility(View.GONE);
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
            });
            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (backgroundIsOnOff) {
                            backgroundIsOnOff = false;
                            mEditor.putBoolean("checkNot", true);
                            mEditor.commit();
                            Toast.makeText(VideoDetailActivityFliper.this, "Enabled", Toast.LENGTH_SHORT).show();
                            background.setBackground(getResources().getDrawable(R.drawable.selected_circle));
                        } else {
                            backgroundIsOnOff = true;
                            mEditor.putBoolean("checkNot", false);
                            mEditor.commit();
                            Log.i("NotificationBackground", "true");
                            Toast.makeText(VideoDetailActivityFliper.this, "Disabled", Toast.LENGTH_SHORT).show();
                            background.setBackground(getResources().getDrawable(R.drawable.circle2));
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
            });
            try {
                final Intent i = getIntent();
                extras = i.getExtras();
                try {
                    filename = extras.getString("videofilename");
                    videoIndex = extras.getInt(Constants.VIDEO_INDEX);
                    folderposition = extras.getInt("folderposition");
                    myList = new ArrayList<>();
                    recyclerView = findViewById(R.id.videoRecyclerInside);
                    recyclerView.setNestedScrollingEnabled(true);
                    recyclerView.setHasFixedSize(true);
                    final LinearLayoutManager lm = new LinearLayoutManager(this);
                    lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(lm);
                    if (folderposition == -2) {
                        loadAllVideos();
                        Log.i("valuesCheck", "2");
                    } else {
                        loadvideos(folderposition);
                        Log.i("valuesCheck", "3");
                    }
                    recentpath.add((filename));
                } catch (Exception gg) {

                }
                try {
                    floatprogress = extras.getInt("videoprogress");
                } catch (Exception f) {

                }

                try {
                    title = extras.getString("title");
                    toolbar.setTitle(title);
                    toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                    recent.add(title);

                } catch (Exception f) {

                }


                try {
                    showButtons = extras.getBoolean("Showbuttons", true);
                } catch (Exception ff) {

                }

                try {
                    showNoti = extras.getBoolean("ShowNoti", true);

                } catch (Exception ff) {

                }
                try {
                    true1 = extras.getBoolean("true", false);
                    true2 = String.valueOf("" + true1 + "");

                } catch (Exception ff) {

                }


                true360 = extras.getBoolean("true360", false);

                true360two = String.valueOf("" + true360 + "");

                id = extras.getInt("id", 0);

                songPosn = id;

            } catch (Exception f) {
            }
            Log.i("", "Come from activity or service" + songPosn);
            finaltrue = "true";
            gestvalue = mSharedPrefs.getString("gestvalue", "true");
            file_name = mSharedPrefs.getString("filename_value", "");
            curr_value = mSharedPrefs.getInt("curent_val", 0);
            insert_query = " Insert into recent (name,path) values(" + '"' + "" + title + "" + '"' + "," + '"' + "" + filename + "" + '"' + ")";
            Log.e("Insert query", insert_query);
            controller.setVisibility(View.INVISIBLE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
            ablumVideo.setText(artist);
            try {
                Uri uri = getIntent().getData();
                if (uri != null) {
                    try {
                        getContentName(uri);
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
            playPauseNP();
            locker.setVisibility(View.INVISIBLE);
            orientation.setVisibility(View.INVISIBLE);
            ratio.setVisibility(View.INVISIBLE);
            ontouchShow.setVisibility(View.INVISIBLE);
            seekTouchDurationTotal.setVisibility(View.INVISIBLE);
            voltouchshow.setVisibility(View.INVISIBLE);
            brighttouchshow.setVisibility(View.INVISIBLE);
            brightShowLayout.setVisibility(View.INVISIBLE);
            brightcontrol.setMax(100);
            brightcontrol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int brightProgress, boolean b) {
                    try {
                        brightprogress = brightProgress;
                        System.out.println(brightprogress);
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
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            if (equiliazerScreen.getVisibility() == View.VISIBLE) {
                horizontalScrollView.setVisibility(View.GONE);
                openlist.setVisibility(View.GONE);
                openlist_2.setVisibility(View.GONE);
                seekTouchLayout.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                sizeChangerText.setVisibility(View.GONE);
                brighttouchshow.setVisibility(View.GONE);
                brightShowLayout.setVisibility(View.GONE);
                volShowLayout.setVisibility(View.GONE);
                seekTouchLayout.setVisibility(View.GONE);
                seekupper.setVisibility(View.GONE);
                volupper.setVisibility(View.GONE);
                controller.setVisibility(View.GONE);
                locker.setVisibility(View.GONE);
            } else {
                horizontalScrollView.setVisibility(View.VISIBLE);
                openlist.setVisibility(View.VISIBLE);
                openlist_2.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                sizeChangerText.setVisibility(View.VISIBLE);
                brighttouchshow.setVisibility(View.VISIBLE);
                brightShowLayout.setVisibility(View.VISIBLE);
                volShowLayout.setVisibility(View.VISIBLE);
                seekTouchLayout.setVisibility(View.VISIBLE);
                seekupper.setVisibility(View.VISIBLE);
                volupper.setVisibility(View.VISIBLE);
                controller.setVisibility(View.VISIBLE);
                locker.setVisibility(View.VISIBLE);
            }
            volumeControl.setMax(MAX_VOLUME);
            volumeControl.setProgress(MAX_VOLUME * mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    try {
                        volprogess = i;

                        /*daniyal*/
                       /* volume = (float) (1 - (Math.log(MAX_VOLUME - volprogess) / Math.log(MAX_VOLUME)));
                        if (mMediaPlayer != null) {
                            mMediaPlayer.setVolume(volume, volume);

                        }*/
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
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            mProgress = mSharedPrefs.getInt("videoprogress", 0);
            Log.i("seekProShrared", "" + mProgress);
            if (mMediaPlayer != null) {
                mMediaPlayer.seekTo(floatprogress);
            }
            seekForRev.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    try {
                        mProgress = i;
                        if (b) {
                            // this is when actually seekbar has been seeked to a new position
//                    if (mMediaPlayer != null) {
                            mMediaPlayer.seekTo(mProgress);
                            Log.i("seekProRev", "" + mProgress);


//                    }
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
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    try {
                        mProgress = i;
                        if (b) {
                            // this is when actually seekbar has been seeked to a new position
                            //  if (mMediaPlayer != null) {
                            mMediaPlayer.seekTo(mProgress);
                            Log.i("seekPro", "" + mProgress);

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
                public void onStartTrackingTouch(SeekBar seekBar) {
                    try {
                        mMediaPlayer.pause();
                        play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
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
                public void onStopTrackingTouch(SeekBar seekBar) {
                    try {
                        mMediaPlayer.start();
                        play_pause.setImageResource(R.drawable.ic_pause_white_24dp);
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
            });
            recyclerView.setAdapter(videoSongsAdapter);
            drawer = findViewById(R.id.inside_drawer_layout);
            if (mMediaPlayer != null) {
                mMediaPlayer.setOnPreparedListener(this);
            }
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createShareForecastIntent2(file_name);
                }
            });
            openlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        recyclerView.setVisibility(View.VISIBLE);
                        openlist_2.setVisibility(View.VISIBLE);
                        openlist.setVisibility(View.GONE);
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
            });
            openlist_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        recyclerView.setVisibility(View.GONE);
                        openlist_2.setVisibility(View.GONE);
                        openlist.setVisibility(View.VISIBLE);
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
            });
            floating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !canDrawOverlays(getApplicationContext())) {
                            Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, APP_PERMISSION_REQUEST);
                            Toast.makeText(VideoDetailActivityFliper.this, "Give Permission Then Reopen", Toast.LENGTH_SHORT).show();
                        } else {
                            int MyVersion = Build.VERSION.SDK_INT;
                            if (MyVersion > Build.VERSION_CODES.KITKAT) {
                                boolean isChecked = mSharedPrefs.getBoolean("checkNot", false);
                                if (isChecked) {
                                    mEditor.putBoolean("checkNot", false);
                                    mEditor.commit();
                                }
                                releaseMediaPlayer();
                                stopMedia();
                                Intent intent = new Intent(VideoDetailActivityFliper.this, FloatWidgetService.class);
                                intent.putExtra("Filename", filename);
                                intent.putExtra("title", title);
                                intent.putExtra("id", id);
                                intent.putExtra("progress", curr);
                                intent.putExtra(Constants.VIDEO_INDEX, videoIndex);
                                intent.putExtra("folderposition", folderposition);
                                intent.putExtra("Showbuttons", false);
                                intent.putExtra("ShowNoti", true);
                                System.out.println("Id of songs" + id);
                                Log.i("floatCurrPosition", " = " + curr);
                                startService(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG).show();
                            }
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
            });
            vol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (boolmute.equals(Boolean.FALSE)) {
                            vol.setImageResource(R.drawable.ic_volume_off_black_24dp);
                            /*daniyal*/
                            mMediaPlayer.setVolume(0, 0);
                            MuteAudio();
                            boolmute = true;
                        } else if (boolmute.equals(Boolean.TRUE)) {
                            vol.setImageResource(R.drawable.ic_volume_up_black_24dp);
                            /*daniyal*/
                            UnMuteAudio();
                            mMediaPlayer.setVolume(1, 1);
                            boolmute = false;
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

            });
            three60.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        releaseMediaPlayer();
                        stopMedia();
                        finish();
                        Intent intent = new Intent(VideoDetailActivityFliper.this, Main4Activity.class);
                        intent.putExtra("FilenameThreeSixty", filename);
                        intent.putExtra("title", title);
                        intent.putExtra("id", id);
                        intent.putExtra("folderposition", folderposition);
                        intent.putExtra(Constants.VIDEO_INDEX, videoIndex);
                        intent.putExtra("progress", curr);
                        intent.putExtra("Showbuttons", false);
                        intent.putExtra("ShowNoti", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
            });
            locker.setOnClickListener(new View.OnClickListener() {
//            int posVideo = 0;


                @Override
                public void onClick(View view) {
                    try {
//                posVideo++;
//                if (posVideo == 1) {
//                    //                    params.width = 1250;
////                    videoView.setLayoutParams(params);
////                    ViewGroup.LayoutParams params=videoView.getLayoutParams();
////                    params.height=150;
////                    videoView.setLayoutParams(params);
////                    Display display = getWindowManager().getDefaultDisplay();
////                    int width = display.getWidth();
////                    int height = display.getHeight();
////
////                    //   videoview.setLayoutParams(new FrameLayout.LayoutParams(550,550));
////
////                    mRootParam.setLayoutParams(new FrameLayout.LayoutParams(width,height));
//                    System.out.println(posVideo);
//                } else if (posVideo == 2) {
////                    ViewGroup.LayoutParams crop = videoView.getLayoutParams();
////                    crop.height = 800;
////                    crop.width =800;
////                    videoView.setLayoutParams(crop);
//                    System.out.println(posVideo);
//                } else if (posVideo == 3) {
////                    ViewGroup.LayoutParams dhdvideo = videoView.getLayoutParams();
////                    dhdvideo.height = 1024;
////                    dhdvideo.width = 1024;
////                    videoView.setLayoutParams(dhdvideo);
////                    System.out.println(posVideo);
////
//                } else if (posVideo == 2) {
//                    //                    params.height = 1250;
////                    params.width = 1250;
////                    videoView.setLayoutParams(params);
//
//                    System.out.println(posVideo);
//                } else {
//                    posVideo = 0;
//
                        if (isLock) {
                            isLock = false;
                            horizontalScrollView.setVisibility(View.VISIBLE);
                            locker.setImageResource(R.drawable.ic_lock_outline_black_24dp);
                            controller.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.VISIBLE);
                            toolbar.animate().translationY(-toolbar.getTop()).setInterpolator(new DecelerateInterpolator()).start();
                            share.setVisibility(View.VISIBLE);
                            openlist.setVisibility(View.VISIBLE);
//                    if (openlist.getVisibility() == View.GONE) {
//                        openlist.setVisibility(View.VISIBLE);
//                    }
//                    if (openlist_2.getVisibility() == View.GONE) {
//                        openlist_2.setVisibility(View.VISIBLE);
//                    }
//                    if (recyclerView.getVisibility() == View.GONE) {
//                        recyclerView.setVisibility(View.VISIBLE);
//                    }
//                    if (horizontalScrollView.getVisibility() == View.GONE) {
//                        horizontalScrollView.setVisibility(View.VISIBLE);
//                    }
//                    if (arrow.getVisibility() == View.GONE) {
//                        arrow.setVisibility(View.VISIBLE);
//                    }
                            orientation.setVisibility(View.VISIBLE);
                            ratio.setVisibility(View.VISIBLE);
                            floating.setVisibility(View.VISIBLE);
                            // subtitles.setVisibility(View.INVISIBLE);
                            vol.setVisibility(View.VISIBLE);
                            //subtitle.setVisibility(View.INVISIBLE);
//                    ontouchShow.setVisibility(View.VISIBLE);
//                    seekTouchDurationTotal.setVisibility(View.VISIBLE);
//                    voltouchshow.setVisibility(View.VISIBLE);
//                    brighttouchshow.setVisibility(View.VISIBLE);
//                    brightShowLayout.setVisibility(View.VISIBLE);
                            //   background.setVisibility(View.VISIBLE);
                        } else {
                            openlist.setVisibility(View.GONE);
                            openlist_2.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            horizontalScrollView.setVisibility(View.GONE);
                            isLock = true;
                            locker.setImageResource(R.drawable.ic_lock_open_black_24dp);
                            controller.setVisibility(View.INVISIBLE);
                            toolbar.setVisibility(View.INVISIBLE);
                            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
//                    share.setVisibility(View.INVISIBLE);
//                    openlist.setVisibility(View.INVISIBLE);
                            orientation.setVisibility(View.INVISIBLE);
                            ratio.setVisibility(View.INVISIBLE);
//                    floating.setVisibility(View.INVISIBLE);
                            // subtitles.setVisibility(View.INVISIBLE);
//                    vol.setVisibility(View.INVISIBLE);
                            //subtitle.setVisibility(View.INVISIBLE);
//                    ontouchShow.setVisibility(View.INVISIBLE);
//                    seekTouchDurationTotal.setVisibility(View.INVISIBLE);
//                    voltouchshow.setVisibility(View.INVISIBLE);
//                    brighttouchshow.setVisibility(View.INVISIBLE);
//                    brightShowLayout.setVisibility(View.INVISIBLE);
//                            videoSizeChanger.setVisibility(View.INVISIBLE);

                            //  background.setVisibility(View.INVISIBLE);


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
            });
            videoSizeChanger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int id = view.getId();
                        if (id == R.id.videoSizeChanger) {
                            if (clickCount == 0) {
                                textureView.setScaleX(-1);
                                System.out.println(clickCount);
                                clickCount = 1;
                                videoSizeChanger.setBackground(getResources().getDrawable(R.drawable.selected_circle));
                            } else if (clickCount == 1) {
                                textureView.setScaleX(1);
                                System.out.println(clickCount);
                                clickCount = 0;
                                videoSizeChanger.setBackground(getResources().getDrawable(R.drawable.circle2));

                            }


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
            });
            ratio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int id = view.getId();
                        if (id == R.id.ratio) {
//                    if (clickCount == 0) {
//
//                        textureView.setScaleX(-1);
//                        System.out.println(clickCount);
//                        clickCount = 1;
//                    } else if (clickCount == 1) {
//                        textureView.setScaleX(1);
//                        System.out.println(clickCount);
//                        clickCount = 0;
//
                            if (id == R.id.ratio) {
                                if (clickCount == 0) {
                                    ratio.setImageResource(R.drawable.sz2);
                                    getDeviceWidthAndHeight();
                                    lp.width = screenWidth - 50;
                                    lp.height = screenHeight - 50;
                                    textureView.setLayoutParams(lp);
                                    sizeChangerText.setText("100%");
//        mEditor.putInt("SongPosition", songPosn);

                                    mEditor.putInt("width", screenWidth - 50);
                                    mEditor.putInt("height", screenHeight - 50);

                                    mEditor.apply();
                                    mEditor.commit();


                                    clickCount = 1;
                                } else if (clickCount == 1) {
                                    getDeviceWidthAndHeight();
                                    ratio.setImageResource(R.drawable.sz3);
                                    lp.width = screenWidth - 300;
                                    lp.height = screenHeight - 100;

                                    mEditor.putInt("width", screenWidth - 300);
                                    mEditor.putInt("height", screenHeight - 100);

                                    mEditor.apply();
                                    mEditor.commit();

                                    textureView.setLayoutParams(lp);
                                    sizeChangerText.setText("Full Screen");
                                    clickCount = 2;
                                } else if (clickCount == 2) {
                                    getDeviceWidthAndHeight();
                                    lp.width = screenWidth;
                                    ratio.setImageResource(R.drawable.sz1);
                                    lp.height = screenHeight - 500;

                                    mEditor.putInt("width", screenWidth);
                                    mEditor.putInt("height", screenHeight - 500);
                                    mEditor.apply();
                                    mEditor.commit();

                                    textureView.setLayoutParams(lp);
                                    sizeChangerText.setText("Fit to Screen");
                                    clickCount = 3;
                                } else if (clickCount == 3) {
                                    ratio.setImageResource(R.drawable.fulsz);
                                    getDeviceWidthAndHeight();
                                    lp.width = screenWidth;
                                    lp.height = screenHeight;

                                    mEditor.putInt("width", screenWidth);
                                    mEditor.putInt("height", screenHeight);


                                    mEditor.apply();
                                    mEditor.commit();

                                    textureView.setLayoutParams(lp);
                                    sizeChangerText.setText("100%");
                                    clickCount = 0;
                                }
                            }
                        }


//                    if (clickCount == 0) {
//                        getDeviceWidthAndHeight();
//                        lp.width = screenWidth - 50;
//                        lp.height = screenHeight - 50;
//                        mSurfaceView.setLayoutParams(lp);
//                        sizeChangerText.setText("100%");
//
//                        System.out.println(clickCount);
//                        clickCount = 1;
//                    } else if (clickCount == 1) {
//                        getDeviceWidthAndHeight();
//                        lp.width = screenWidth - 300;
//                        lp.height = screenHeight - 100;
//                        mSurfaceView.setLayoutParams(lp);
//                        sizeChangerText.setText("Full Screen");
//
//                        System.out.println(clickCount);
//                        clickCount = 2;
//                    } else if (clickCount == 2) {
//                        getDeviceWidthAndHeight();
//                        lp.width = screenWidth;
//                        lp.height = screenHeight;
//                        mSurfaceView.setLayoutParams(lp);
//                        sizeChangerText.setText("100%");
//
//                        System.out.println(clickCount);
//                        clickCount = 3;
//                    } else if (clickCount == 3) {
//                        getDeviceWidthAndHeight();
//                        lp.width = screenWidth;
//                        lp.height = screenHeight - 500;
//                        mSurfaceView.setLayoutParams(lp);
//                        sizeChangerText.setText("Fit to Screen");
//                        System.out.println(clickCount);
//                        clickCount = 0;
//
//                    }
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
            });
            play_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (mMediaPlayer != null) {
                            if (mMediaPlayer.isPlaying()) {
                                scrollstate = false;
                                System.out.println(scrollstate);
                                s = mMediaPlayer.getCurrentPosition();
                                //Toast.makeText(getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();

                                mMediaPlayer.pause();

                                // Changing button image to play button
                                play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);


                            } else if (!mMediaPlayer.isPlaying()) {
                                play_pause.setImageResource(R.drawable.ic_pause_white_24dp);
                                // Resume song

                                scrollstate = true;
                                System.out.println(scrollstate);
                                mMediaPlayer.start();

                                // Changing button image to pause button

                            } else {
                                play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                                mMediaPlayer.pause();
                                scrollstate = false;

                            }
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
            });
            oreintat = 1;
            orientation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        toggleRotateScreen();
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
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if (myList.size() > 1) {


                            playNext();
                            //                durationSongs();
                            playPauseNP();
//                getVideoAspectRatio();
//                if (isVideoLandscaped()) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//
//                }
//                else {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }


                            System.out.println("saved size " + mSharedPrefs.getInt("width", 0) + " " + mSharedPrefs.getInt("height", 0));
                            try {

                                if (mSharedPrefs.getInt("width", 0) > 0 && mSharedPrefs.getInt("height", 0) > 0) {
                                    // getDeviceWidthAndHeight();
                                    widthdp = mSharedPrefs.getInt("width", 0);
                                    heightdp = mSharedPrefs.getInt("height", 0);


                                } else {

//                            heightdp = displayMetrics.heightPixels;
//                            widthdp = displayMetrics.widthPixels;
                                }
                                // textureView.setLayoutParams(lp);
                            } catch (Exception d) {

//                        heightdp = displayMetrics.heightPixels;
//                        widthdp = displayMetrics.widthPixels;
                                System.out.println("Title error " + d.getMessage());

                            }
//                        getVideoAspectRatio();
//                        if (isVideoLandscaped()) {
//                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                            DisplayMetrics displayMetrics = new DisplayMetrics();
//                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//                            heightdp = displayMetrics.heightPixels;
//                            widthdp = displayMetrics.widthPixels;
//
//
//
//                            if (!isVideoLandscaped()) {
//                                getDeviceWidthAndHeight();
//                                lp.width = widthdp / 2;
//                                lp.height = heightdp;
//                                textureView.setLayoutParams(lp);
//                            } else {
//
//                                getDeviceWidthAndHeight();
//                                lp.width = widthdp;
//                                lp.height = heightdp;
//                                textureView.setLayoutParams(lp);
//                            }
//                        } else {
//                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                            DisplayMetrics displayMetrics = new DisplayMetrics();
//                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                            heightdp = displayMetrics.heightPixels;
//                            widthdp = displayMetrics.widthPixels;
//
//                            if (!isVideoLandscaped()) {
//                                getDeviceWidthAndHeight();
//                                lp.width = widthdp;
//                                lp.height = heightdp;
//                                textureView.setLayoutParams(lp);
//                            } else {
//                                getDeviceWidthAndHeight();
//                                lp.width = widthdp;
//                                lp.height = heightdp / 2;
//                                textureView.setLayoutParams(lp);
//
//                            }
//                        }
//
//
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
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if (myList.size() > 1) {


                            playPrev();
                            //                durationSongs();
                            playPauseNP();

                            System.out.println("saved size " + mSharedPrefs.getInt("width", 0) + " " + mSharedPrefs.getInt("height", 0));
                            try {

                                if (mSharedPrefs.getInt("width", 0) > 0 && mSharedPrefs.getInt("height", 0) > 0) {
                                    // getDeviceWidthAndHeight();
                                    widthdp = mSharedPrefs.getInt("width", 0);
                                    heightdp = mSharedPrefs.getInt("height", 0);


                                } else {

//                            heightdp = displayMetrics.heightPixels;
//                            widthdp = displayMetrics.widthPixels;
                                }
                                // textureView.setLayoutParams(lp);
                            } catch (Exception d) {

                                heightdp = displayMetrics.heightPixels;
                                widthdp = displayMetrics.widthPixels;
                                System.out.println("Title error " + d.getMessage());

                            }

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
            });
            mInterval = 5000; // 3 seconds by default, can be changed later
            mHandler = new Handler();
            startRepeatingTask();//
            getScreenBrightness();
            FrameLayout backgrou = (FrameLayout) findViewById(R.id.root_view);
            relativbAck = (RelativeLayout) findViewById(R.id.container);
            relativbAck.setBackgroundColor(getResources().getColor(R.color.Lightblack));
            backgrou.setBackgroundColor(getResources().getColor(R.color.Lightblack));
            fullScreen();
            Intent playerIntent = new Intent(VideoDetailActivityFliper.this, MediaPlayerServiceVideo.class);
            stopService(playerIntent);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
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

    public void loadvideos(int posi) {
        try {
            this.myList.clear();
            this.myList = new VideosAndFoldersUtility(this).fetchVideosByFolder(VideoFolder.folders.get(posi).getPath());
            this.videoSongsAdapter = new VideosListAdapterForMediaPlayer(VideoDetailActivityFliper.this, this.myList, posi);
            Log.i("listOfVideos", myList.size() + "");
            recyclerView.setAdapter(videoSongsAdapter);
            this.videoSongsAdapter.notifyDataSetChanged();
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

    public void loadAllVideos() {
        try {
            this.myList.clear();
            this.myList = VideoFolder.videos;
            Log.i("listOfVideos", myList.size() + "");
            videoSongsAdapter = new VideosListAdapterForMediaPlayer(VideoDetailActivityFliper.this, VideoFolder.videos, -2);
            videoSongsAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(videoSongsAdapter);
            this.videoSongsAdapter.notifyDataSetChanged();
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

    @SuppressLint("WrongConstant")
    public boolean isLandscape() {
        return getRequestedOrientation() == ActivityInfo.COLOR_MODE_DEFAULT;
    }

    public void toggleRotateScreen() {
        try {
            if (isLandscape()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
    public void onTrimMemory(int level) {
        try {
            super.onTrimMemory(level);
            if (level >= TRIM_MEMORY_RUNNING_LOW || level >= TRIM_MEMORY_RUNNING_CRITICAL || level >= TRIM_MEMORY_COMPLETE || level >= TRIM_MEMORY_BACKGROUND) {
                System.gc();
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
    public void onCompletion(MediaPlayer mediaPlayer) {
        try {
            if (mediaPlayer.getCurrentPosition() > 0) {
                mediaPlayer.reset();
                if (mSharedPrefs.getBoolean("shuffle_play", false)) {
                    Random random = new Random();
                    int songIndex = random.nextInt((myList.size() - 1) + 1);
                    playSong(songIndex);
                } else {
                    playNext();
                }
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

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        try {
            if (this.videoHeight > 0 && this.videoWidth > 0) {
                this.textureView.adjustSize((float) this.relativbAck.getWidth(), (float) this.relativbAck.getHeight(), this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer.getVideoHeight(), Constants.FIT_SCREEN);
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
    public void onPrepared(MediaPlayer mediaPlayer) {
        try {
            dura = mMediaPlayer.getDuration();
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            seekbar.setMax((int) dura);
            seekForRev.setMax((int) dura);
            current.setText(milliSecondsToTimer(dura));
            this.videoHeight = mMediaPlayer.getVideoHeight();
            this.videoWidth = mMediaPlayer.getVideoWidth();
            if (this.videoWidth > this.videoHeight) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            seekTouchDurationTotal.setText("-" + milliSecondsToTimer(dura) + "]");
            seekbar.postDelayed(onEverySecond, 300);
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void fullScreen() {
        try {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
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

    public void setBrightness(float brightness) {
        try {
            float BackLightValue = brightness / 255;
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
            layoutParams.screenBrightness = BackLightValue; // Set Value
            getWindow().setAttributes(layoutParams);
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

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                System.out.println("Show hide");
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        locker.setVisibility(View.INVISIBLE);
                        horizontalScrollView.setVisibility(View.INVISIBLE);
                        openlist.setVisibility(View.INVISIBLE);
                        ratio.setVisibility(View.INVISIBLE);
                        orientation.setVisibility(View.INVISIBLE);
                        controller.setVisibility(View.INVISIBLE);
                        toolbar.setVisibility(View.INVISIBLE);
                        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
                    } else if (!isLock) {
                        if (!mMediaPlayer.isPlaying()) {
                            controller.setVisibility(View.VISIBLE);
                            locker.setVisibility(View.VISIBLE);
                            share.setVisibility(View.VISIBLE);
                            openlist.setVisibility(View.VISIBLE);
                            horizontalScrollView.setVisibility(View.VISIBLE);
                            ratio.setVisibility(View.VISIBLE);
                            orientation.setVisibility(View.VISIBLE);
                            floating.setVisibility(View.VISIBLE);
                            vol.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.VISIBLE);
                            toolbar.animate().translationY(-toolbar.getTop()).setInterpolator(new DecelerateInterpolator()).start();

                        }
                    }
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
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        try {
            mStatusChecker.run();
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

    void stopRepeatingTask() {
        try {
            mHandler.removeCallbacks(mStatusChecker);
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

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            finalTimerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;
        return finalTimerString;
    }

    private Runnable onEverySecond = new Runnable() {

        @Override
        public void run() {
            try {
                if (seekbar != null && mMediaPlayer != null) {
                    curr = mMediaPlayer.getCurrentPosition();
                    seekbar.setProgress((int) curr);
                    seekForRev.setProgress((int) curr);
                    runingduration.setText(milliSecondsToTimer(curr));
                    ontouchShow.setText("[" + milliSecondsToTimer(curr));
                    if (mMediaPlayer.isPlaying()) {
                        seekbar.postDelayed(onEverySecond, 300);
                    } else if (!mMediaPlayer.isPlaying()) {
                        seekbar.postDelayed(onEverySecond, 300);
                    }
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
    };

    public void playPauseNP() {
        try {
            int num = 0;
            if (num == 1) {

                play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);

            } else if (num == 0) {
                play_pause.setImageResource(R.drawable.ic_pause_white_24dp);

            } else {
                play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);

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

    private Intent createShareForecastIntent(String number) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/*");
        File fileToShare = new File(number);
        Uri uri = Uri.fromFile(fileToShare);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return sharingIntent;
    }

    public void createShareForecastIntent2(String v) {
        File file = new File(v);
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("*/*");
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(videoshare);
    }


    public void playPrev() {

        try {
            if (mSharedPrefs.getBoolean("shuffle_play", true)) {
                Random random = new Random();
                int songIndex = random.nextInt((myList.size() - 1) - 1);
                playSong(songIndex);
            } else {
                videoIndex--;

                stopMedia();

                if (videoIndex < 0) {
                    videoIndex = myList.size() - 1;


                }

                if (videoIndex % 9 == 0) {
                    videoIndex -= 1;

                    playSong(videoIndex);
                } else {
                    playSong(videoIndex);
                }
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

    public void stopMedia() {
        try {
            if (mMediaPlayer == null) return;
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();

                try {
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    mAudioManager.setMode(AudioManager.MODE_NORMAL);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                    stopRepeatingTask();

                } catch (Exception f) {

                }
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
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.videoWidth > 0 && this.videoHeight > 0) {
                this.textureView.adjustSize(getDeviceWidth(this), getDeviceHeight(this), this.textureView.getWidth(), this.textureView.getHeight(), this.mScreenSize);
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

    public void playNext() {
        try {
            if (mSharedPrefs.getBoolean("shuffle_play", false)) {
                Random random = new Random();
                int songIndex = random.nextInt((myList.size() - 1) + 1);
                playSong(songIndex);
            } else {
                videoIndex++;

                stopMedia();
                if (videoIndex >= myList.size()) {
                    videoIndex = 0;
                }

                if (videoIndex % 9 == 0) {
                    playSong(videoIndex);
                } else {
                    playSong(videoIndex);
                }
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

    @SuppressLint("SetWorldReadable")
    public void playSong(int index) {
        try {
            //play
            videosongsClass = myList.get(index);
            filename = videosongsClass.getData();
            title = videosongsClass.getName();
            title = title.substring(title.lastIndexOf("/") + 1);

            {
                Log.i("", "ON NEXT" + title);
                System.out.println("FileName" + title);
                getSupportActionBar().setTitle(title);

                try {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.reset();
                        mMediaPlayer.setDataSource(filename);
                        mMediaPlayer.prepare();
                        mMediaPlayer.setOnPreparedListener(VideoDetailActivityFliper.this);
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.start();


                    }

                } catch (Exception e) {


                    Log.e("MUSIC SERVICE", "Error setting data source", e);
                    Log.e("", "Should be next on Next press");

                }
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
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        try {
            Surface surfaceView = new Surface(surfaceTexture);
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(filename);
                mMediaPlayer.setSurface(surfaceView);
                mMediaPlayer.prepare();
                mMediaPlayer.setOnVideoSizeChangedListener(this);
                mMediaPlayer.setOnCompletionListener(VideoDetailActivityFliper.this);
                mMediaPlayer.setOnPreparedListener(VideoDetailActivityFliper.this);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setScreenOnWhilePlaying(true);

               /* if (mMediaPlayer.getDuration() <= 6000) {
                    mMediaPlayer.seekTo(0);
                } else */
                if (true2.equals("true")) {
                    backprogress1 = mSharedPrefs.getInt("backprogress1", 0);
                    mMediaPlayer.seekTo(backprogress1);
                } else if (true360two.equals("true")) {
                    threeprogress = mSharedPrefs.getInt("videothreesixty", 0);
                    mMediaPlayer.seekTo(threeprogress);
                } else if (file_name.equals(filename)) {
                    if (floatprogress > 10) {
                        curr_value = floatprogress;
                    }
                    mMediaPlayer.seekTo(curr_value);

                } else {
                    mMediaPlayer.seekTo(floatprogress);
                }

                mMediaPlayer.start();


            } catch (IOException e) {


                Log.e("", "Should be next onSurface");
                e.printStackTrace();
                Log.e("MUSIC SERVICE", "Error setting data source" + e);
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
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {

        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
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
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    private void getVideoAspectRatio() {

        if (mMediaPlayer != null) {

            int width = mMediaPlayer.getVideoWidth();
            int height = mMediaPlayer.getVideoHeight();

            Log.i("", " WIDTH" + width + "HEIGHT " + height);
            videoWidth = width;
            videoHeight = height;
        }
//        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//        mediaMetadataRetriever.setDataSource(this, Uri.parse(filename));
//        String height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
//        String width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
//        videoWidth = Integer.parseInt(width);
//        videoHeight = Integer.parseInt(height);
    }

    private boolean isVideoLandscaped() {
        if (videoWidth > videoHeight) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        // Associate searchable configuration with the SearchView
////        SharedPreferences settings = getSharedPreferences("settings", 0);
//

//        MenuItem item = menu.findItem(R.id.background_noification);
//        item.setChecked(isChecked);
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.menu_eq) {

            int sessionId = mMediaPlayer.getAudioSessionId();
            mMediaPlayer.setLooping(true);

            equalizerFragment = new CustomFragment()
                    .newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(0)
                    .build();

            SessionStorage sessionStorage = new SessionStorage(getApplicationContext());
            sessionStorage.storeSession(sessionId);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_view2, equalizerFragment)
                    .commit();

            equiliazerScreen.setVisibility(View.VISIBLE);
            horizontalScrollView.setVisibility(View.GONE);

            if (equalizerFragment != null || equalizerFragment.isVisible() || equiliazerScreen.getVisibility() == View.VISIBLE) {
                horizontalScrollView.setVisibility(View.GONE);
                openlist.setVisibility(View.GONE);
                openlist_2.setVisibility(View.GONE);
                seekTouchLayout.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                sizeChangerText.setVisibility(View.GONE);
                brighttouchshow.setVisibility(View.GONE);
                brightShowLayout.setVisibility(View.GONE);
                volShowLayout.setVisibility(View.GONE);
                seekTouchLayout.setVisibility(View.GONE);
                seekupper.setVisibility(View.GONE);
                volupper.setVisibility(View.GONE);
                controller.setVisibility(View.GONE);
                locker.setVisibility(View.GONE);

                ontouchShow.setVisibility(View.GONE);
                seekTouchDurationTotal.setVisibility(View.GONE);
                voltouchshow.setVisibility(View.GONE);
                sizeChangerText.setVisibility(View.GONE);
                brighttouchshow.setVisibility(View.GONE);
                brightShowLayout.setVisibility(View.GONE);
                volShowLayout.setVisibility(View.GONE);
                brightupper.setVisibility(View.GONE);
                volupper.setVisibility(View.GONE);
                seekupper.setVisibility(View.GONE);


            } else {
                horizontalScrollView.setVisibility(View.VISIBLE);
                openlist.setVisibility(View.VISIBLE);
                openlist_2.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                sizeChangerText.setVisibility(View.VISIBLE);
                brighttouchshow.setVisibility(View.VISIBLE);
                brightShowLayout.setVisibility(View.VISIBLE);
                volShowLayout.setVisibility(View.VISIBLE);
                seekTouchLayout.setVisibility(View.VISIBLE);
                seekupper.setVisibility(View.VISIBLE);
                volupper.setVisibility(View.VISIBLE);
                controller.setVisibility(View.VISIBLE);
                locker.setVisibility(View.VISIBLE);


                ontouchShow.setVisibility(View.VISIBLE);
                seekTouchDurationTotal.setVisibility(View.VISIBLE);
                voltouchshow.setVisibility(View.VISIBLE);
                sizeChangerText.setVisibility(View.VISIBLE);
                brighttouchshow.setVisibility(View.VISIBLE);
                brightShowLayout.setVisibility(View.VISIBLE);
                volShowLayout.setVisibility(View.VISIBLE);
                brightupper.setVisibility(View.VISIBLE);
                volupper.setVisibility(View.VISIBLE);
                seekupper.setVisibility(View.VISIBLE);
            }
        }


        return super.onOptionsItemSelected(item);
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            super.onSaveInstanceState(outState);
            outState.putBoolean("serviceStatus", serviceBoundVideo);
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
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onRestoreInstanceState(savedInstanceState);
            serviceBoundVideo = savedInstanceState.getBoolean("serviceStatus");
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

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                MediaPlayerServiceVideo.LocalBinder binder = (MediaPlayerServiceVideo.LocalBinder) service;
                playerService = binder.getService();
                serviceBoundVideo = true;
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
        public void onServiceDisconnected(ComponentName name) {
            serviceBoundVideo = false;
        }
    };


    private void playAudio(int videoIndex) {
        try {
            //Check is service is active
            if (!serviceBoundVideo) {
                //Store Serializable audioList to SharedPreferences
                VideoStorageUtil storage = new VideoStorageUtil(getApplicationContext());
                storage.storeVideo(myList);
                storage.storeFolderIndex(folderposition);
//                Log.i("listVideoFolderPoistion", "list = "+myList.size()+" videoIndex = "+videoIndex);
                storage.storeVideoIndex(videoIndex);
                Intent playerIntent = new Intent(VideoDetailActivityFliper.this, MediaPlayerServiceVideo.class);
                startService(playerIntent);
//            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            } else {
                //Store the new audioIndex to SharedPreferences
                VideoStorageUtil storage = new VideoStorageUtil(getApplicationContext());
                storage.storeVideo(myList);
                storage.storeFolderIndex(folderposition);
                storage.storeVideoIndex(videoIndex);

                //Service is active
                //Send a broadcast to the service -> PLAY_NEW_AUDIO
                Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
                sendBroadcast(broadcastIntent);
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
    protected void onStart() {
        try {
            super.onStart();
            Intent playerIntent = new Intent(VideoDetailActivityFliper.this, MediaPlayerServiceVideo.class);
            stopService(playerIntent);
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
    protected void onDestroy() {
        try {
            super.onDestroy();
            stopRepeatingTask();

            try {

                if (mMediaPlayer != null) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = null;

                }
            } catch (Exception f) {

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
    protected void onStop() {

        try {

            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;

            }
        } catch (Exception f) {

        }

        super.onStop();


//        setBrightness(10);

//        releaseMediaPlayer();

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            finish();
            boolean isChecked = mSharedPrefs.getBoolean("checkNot", false);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol, 0);
            mEditor.putString("gestvalue-", "true");
            mEditor.putString("filename_value", filename);
            mEditor.putInt("curent_val", (int) curr);
            mEditor.apply();
            gestvalue = mSharedPrefs.getString("gestvalue", "true");

            if (isChecked) {
                if (mMediaPlayer != null) {
                    backprogress = mMediaPlayer.getCurrentPosition();
                }
                mEditor.putInt("backprogress", backprogress);
                mEditor.apply();
                mEditor.commit();
                stopRepeatingTask();

                int MyVersion = Build.VERSION.SDK_INT;
                if (MyVersion > Build.VERSION_CODES.KITKAT) {
                    if (showNoti) {
                        Log.d("", "Song pos for notification" + String.valueOf(songPosn));
                        playAudio(videoIndex);

                    } else {
                        releaseMediaPlayer();
                    }

                } else {
                    releaseMediaPlayer();
                }
            } else {
                super.onBackPressed();
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

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {

                if (mMediaPlayer != null) {
                    mMediaPlayer.pause();
                    play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                if (mMediaPlayer != null) {
                    mMediaPlayer.pause();
                    play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                }

            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                if (mMediaPlayer != null) {
                    mMediaPlayer.pause();
                    play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                }

            }
        }
    };

    @Override
    protected void onResume() {
        try {
            super.onResume();
            volprogess = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            fullScreen();
            Intent playerIntent = new Intent(VideoDetailActivityFliper.this, MediaPlayerServiceVideo.class);
            stopService(playerIntent);
            releaseMediaPlayer();
            Intent playerIntentAudio = new Intent(VideoDetailActivityFliper.this, MediaPlayerService.class);
            stopService(playerIntentAudio);
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
    protected void onRestart() {
        try {
            super.onRestart();
            fullScreen();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MOVE_HOME) {
            try {
                releaseMediaPlayer();
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
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void releaseMediaPlayer() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
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
    public void onBackPressed() {
        try {

            boolean isChecked = mSharedPrefs.getBoolean("checkNot", false);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol, 0);
            mEditor.putString("gestvalue", "true");
            mEditor.putString("filename_value", filename);
            mEditor.putInt("curent_val", (int) curr);
            mEditor.apply();
            gestvalue = mSharedPrefs.getString("gestvalue", "true");
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                if (isChecked) {
                    super.onBackPressed();

                    if (mMediaPlayer != null) {
                        backprogress = mMediaPlayer.getCurrentPosition();
                        playAudio(videoIndex);
                    }

                    mEditor.putInt("backprogress", backprogress);
                    mEditor.apply();
                    mEditor.commit();
                    stopRepeatingTask();

                    int MyVersion = Build.VERSION.SDK_INT;
                    if (MyVersion > Build.VERSION_CODES.KITKAT) {
                        if (showNoti) {
                            Log.d("", "Song pos for notification" + String.valueOf(songPosn));
                            playAudio(videoIndex);
                        } else {
                            releaseMediaPlayer();
                        }
                    } else {
                        releaseMediaPlayer();
                    }
                } else {
                    releaseMediaPlayer();
                    super.onBackPressed();
                }
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

    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        try {
            delx = distanceX;
            dely = distanceY;
            delx1 = event1.getX();
            float dely2 = event2.getY();
            deltaX = event1.getRawX() - event2.getRawX();
            deltaY = event1.getRawY() - event2.getRawY();

            Boolean checking;
            halfwidth = widthdp / 2;
            isX = -1;


            Log.d(DEBUG_TAG, "onScroll: " + event1.toString() + event2.toString());
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
        return true;


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        try {
            halfwidth = widthdp / 3;
            Log.i("deviceValueCheck", halfheight + " = " + halfwidth);
            float abc = event.getAction();
            float cde = event.getAction();
            if (Math.abs(delx) > Math.abs(dely)) {
                if (abc == 1) {
                    delx = 0;
                    dely = 0;
                }

                if (Math.abs(delx) > SWIPE_THRESHOLD) {
                    checkin = 0;
                    if (deltaX > lastY) {
                        lastY = deltaX;
                        if (bolseek.equals(Boolean.TRUE)) {
                            leftScroll();
                            dely = 0;
                            delx = 0;
                        } else if (bolseek.equals(Boolean.FALSE)) {//Toast.makeText(getApplicationContext(), "Cant Change Please Change Setting", Toast.LENGTH_SHORT).show();
                        }
                        Log.i("aa", "Slide right");
                    } else {
                        lastY = deltaX;
                        if (bolseek.equals(Boolean.TRUE)) {

                            rightScroll();
                            dely = 0;
                            delx = 0;
                        } else if (bolseek.equals(Boolean.FALSE)) {
                        }
                        Log.i("aa", "Slide left");
                    }
                }
            }
            if (deltaX == 0.0 && deltaY == 0.0) {
                dely = 1;
            }

            {
                if (delx == 0.0 && dely == 0.0) {
                    dely = 1;
                }
            }

            {
                if (Math.abs(dely) > Math.abs(delx)) {
                    if (Math.abs(dely) > Math.abs(delx) && abc == checkin) {
                        checkin = 2;
                        if (delx1 < halfwidth) {
                            if (Math.abs(dely) > SWIPE_THRESHOLDVOl) {
                                if (deltaY > lastY) {
                                    lastY = deltaY;
                                    if (boolbri.equals(Boolean.TRUE)) {
                                        upBrightness();
                                        dely = 0;
                                        delx = 0;
                                    } else if (boolbri.equals(Boolean.FALSE)) {
                                    }
                                    Log.i("", "Slide down");
                                } else {
                                    lastY = deltaY;
                                    if (boolbri.equals(Boolean.TRUE)) {
                                        downBrightness();
                                        dely = 0;
                                        delx = 0;
                                    } else {
                                    }
                                    Log.i("", "Slide up");
                                }
                            }
                        } else {
                            if (Math.abs(dely) > SWIPE_THRESHOLDVOl) {
                                if (deltaY > lastY) {
                                    lastY = deltaY;
                                    if (boolvol.equals(Boolean.TRUE)) {
                                        upvolume();
                                        dely = 0;
                                        delx = 0;
                                    } else if (boolvol.equals(Boolean.FALSE)) {
                                    }
                                    Log.i("", "Slide down");
                                } else {
                                    lastY = deltaY;
                                    if (boolvol.equals(Boolean.TRUE)) {
                                        downvolume();
                                        dely = 0;
                                        delx = 0;
                                    } else {
                                    }
                                    Log.i("", "Slide up");
                                }
                            }
                        }
                    }
                }
            }

            if (mMediaPlayer != null) {
                if (event.getPointerCount() > 1) {

                    this.mScaleGestureDetector.onTouchEvent(event);
                    fullScreen();
                } else {
                    this.mDetector.onTouchEvent(event);
                    fullScreen();
                }
                // Be sure to call the superclass implementation
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mIsScrolling) {

                        Log.d("", "OnTouchListener --> onTouch ACTION_UP");

                        scroll = null;
                        seekTouchLayout.setVisibility(View.INVISIBLE);
                        brightShowLayout.setVisibility(View.INVISIBLE);
                        volShowLayout.setVisibility(View.INVISIBLE);
                        volumeControl.setVisibility(View.INVISIBLE);
                        brightcontrol.setVisibility(View.INVISIBLE);
                        ontouchShow.setVisibility(View.INVISIBLE);
                        seekTouchDurationTotal.setVisibility(View.INVISIBLE);
                        voltouchshow.setVisibility(View.INVISIBLE);
                        brighttouchshow.setVisibility(View.INVISIBLE);
                        brightShowLayout.setVisibility(View.INVISIBLE);

                        mIsScrolling = true;
                        if (scrollstate) {
                            System.out.println(scrollstate);
                            if (mMediaPlayer != null) {
                                mMediaPlayer.start();
                            }
                        }
                    }
                }

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

        return true;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        try {
            onTouchShowHide();
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
        return false;
    }

    public void MuteAudio(){
        AudioManager mAlramMAnager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
        } else {
            mAlramMAnager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_ALARM, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    public void UnMuteAudio(){
        AudioManager mAlramMAnager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
        } else {
            mAlramMAnager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_ALARM, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
                           float velocityY) {
        boolean result = false;
        return result;
    }

    private void onSwipeLeft() {
        if (mMediaPlayer != null) {
            mIsScrolling = true;
            curr = mMediaPlayer.getCurrentPosition();
            curr = mMediaPlayer.getCurrentPosition() - 1000;
            mMediaPlayer.seekTo((int) curr);
            seekbar.setProgress((int) curr);
            seekForRev.setProgress((int) curr);
            seekForRev.setVisibility(View.VISIBLE);
            System.out.println(mMediaPlayer.getCurrentPosition());
            ontouchShow.setVisibility(View.VISIBLE);
            seekTouchDurationTotal.setVisibility(View.VISIBLE);
            seekupper.setImageResource(R.drawable.ic_fast_rewind_black_24dp);
            seekupper.setVisibility(View.VISIBLE);
            seekTouchLayout.setVisibility(View.VISIBLE);
            voltouchshow.setVisibility(View.INVISIBLE);
            volShowLayout.setVisibility(View.INVISIBLE);
            brighttouchshow.setVisibility(View.INVISIBLE);
            brightShowLayout.setVisibility(View.INVISIBLE);
            controller.setVisibility(View.INVISIBLE);
            locker.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            ratio.setVisibility(View.VISIBLE);
            openlist.setVisibility(View.VISIBLE);
            orientation.setVisibility(View.VISIBLE);
            floating.setVisibility(View.VISIBLE);
            // subtitles.setVisibility(View.VISIBLE);
            vol.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();


            System.out.println("left");

            Log.i("", "Slide left");
        }
    }

    private void onSwipeRight() {

        if (mMediaPlayer != null) {

            mIsScrolling = true;
//            mMediaPlayer.pause();
            curr = mMediaPlayer.getCurrentPosition();
            curr = mMediaPlayer.getCurrentPosition() + 1000;
            mMediaPlayer.seekTo((int) curr);
            seekbar.setProgress((int) curr);
            seekForRev.setProgress((int) curr);
            seekForRev.setVisibility(View.VISIBLE);
            System.out.println(mMediaPlayer.getCurrentPosition());
            ontouchShow.setVisibility(View.VISIBLE);
            seekTouchDurationTotal.setVisibility(View.VISIBLE);
            seekupper.setImageResource(R.drawable.ic_fast_rewind_black_24dp);
            seekupper.setVisibility(View.VISIBLE);
            seekTouchLayout.setVisibility(View.VISIBLE);
            voltouchshow.setVisibility(View.INVISIBLE);
            volShowLayout.setVisibility(View.INVISIBLE);
            brighttouchshow.setVisibility(View.INVISIBLE);
            brightShowLayout.setVisibility(View.INVISIBLE);
            controller.setVisibility(View.INVISIBLE);
            locker.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            ratio.setVisibility(View.VISIBLE);
            openlist.setVisibility(View.VISIBLE);
            orientation.setVisibility(View.VISIBLE);
            floating.setVisibility(View.VISIBLE);
            // subtitles.setVisibility(View.VISIBLE);
            vol.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.INVISIBLE);
            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();


            System.out.println("Right");
            Log.i("", "Slide right");
        }
    }

    @Override
    public void onLongPress(MotionEvent event) {
        try {
            mDetector.setIsLongpressEnabled(true);
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

    int isX = 0;

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);

        return mDisplayMetrics.widthPixels;
    }

    public static int getDeviceHeight(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    @Override
    public void onShowPress(MotionEvent event) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return false;
    }

    private final class MyScaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        private int mW, mH;


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            try {
                lp = textureView.getLayoutParams();
                mW *= detector.getScaleFactor();
                mH *= detector.getScaleFactor();
                if (mW < 250 && mH < 250) { // limits
                    mW = textureView.getWidth();
                    mH = textureView.getHeight();
                }
                lp.width = mW;
                lp.height = mH;

                if (mW > 5277 || mH > 6287) {
                    lp.width = 5277;
                    lp.height = 6287;
                }
                textureView.setLayoutParams(lp);
                Log.d("onScale", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
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
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mW = textureView.getWidth();
            mH = textureView.getHeight();
            Log.d("onScaleBegin", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

        }
    }

    public void onTouchShowHide() {
        try {
            fullScreen();
            if (mMediaPlayer != null) {
                if (isLock) {
                    controller.setVisibility(View.INVISIBLE);
                    horizontalScrollView.setVisibility(View.INVISIBLE);
                    openlist.setVisibility(View.INVISIBLE);
                    openlist_2.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    locker.setVisibility(View.VISIBLE);

                    if (!isLock) {
                        share.setVisibility(View.VISIBLE);
                        openlist.setVisibility(View.VISIBLE);
                        ratio.setVisibility(View.VISIBLE);
                        orientation.setVisibility(View.VISIBLE);
                        floating.setVisibility(View.VISIBLE);
                        horizontalScrollView.setVisibility(View.VISIBLE);
                        vol.setVisibility(View.VISIBLE);
                    }
                    toolbar.setVisibility(View.INVISIBLE);
                    toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
                } else {
                    if (mMediaPlayer.isPlaying()) {
                        if (isShowing) {
                            isShowing = false;
                            locker.setVisibility(View.INVISIBLE);
                            openlist_2.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            orientation.setVisibility(View.INVISIBLE);
                            horizontalScrollView.setVisibility(View.INVISIBLE);
                            openlist.setVisibility(View.INVISIBLE);
                            controller.setVisibility(View.INVISIBLE);
                            toolbar.setVisibility(View.INVISIBLE);
                            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
                        } else {
                            isShowing = true;
                            if (!isLock) {
                                locker.setVisibility(View.VISIBLE);
                                controller.setVisibility(View.VISIBLE);
                                share.setVisibility(View.VISIBLE);
                                openlist.setVisibility(View.VISIBLE);
                                orientation.setVisibility(View.VISIBLE);
                                ratio.setVisibility(View.VISIBLE);
                                floating.setVisibility(View.VISIBLE);
                                horizontalScrollView.setVisibility(View.VISIBLE);
                                vol.setVisibility(View.VISIBLE);
                                toolbar.setVisibility(View.VISIBLE);
                                toolbar.animate().translationY(-toolbar.getTop()).setInterpolator(new DecelerateInterpolator()).start();
                            }

                        }
                    } else if (!mMediaPlayer.isPlaying()) {

                        if (!isLock) {
                            controller.setVisibility(View.VISIBLE);
                            locker.setVisibility(View.VISIBLE);
                            share.setVisibility(View.VISIBLE);
                            openlist.setVisibility(View.VISIBLE);
                            ratio.setVisibility(View.VISIBLE);
                            orientation.setVisibility(View.VISIBLE);
                            floating.setVisibility(View.VISIBLE);
                            horizontalScrollView.setVisibility(View.VISIBLE);
                            vol.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.VISIBLE);
                            toolbar.animate().translationY(-toolbar.getTop()).setInterpolator(new DecelerateInterpolator()).start();
                        }

                    } else {
                        controller.setVisibility(View.INVISIBLE);
                        toolbar.setVisibility(View.INVISIBLE);
                        locker.setVisibility(View.INVISIBLE);
                        horizontalScrollView.setVisibility(View.INVISIBLE);
                        orientation.setVisibility(View.INVISIBLE);
                        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();

                    }
                }

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

    public void onAlertSubTitle() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(VideoDetailActivityFliper.this);
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.demo, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();

        EditText editText = (EditText) dialogView.findViewById(R.id.subtitleedit);
        editText.setText(title);

        Button cancel = (Button) dialogView.findViewById(R.id.cancel);
        Button ok = (Button) dialogView.findViewById(R.id.ok);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    alertDialog.cancel();
                    progressDialog.setCancelable(false);
                    prodialog = progressDialog.show(VideoDetailActivityFliper.this, "", "Searching....", false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            prodialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_LONG).show();

                        }
                    }, 1000);


                } else {
                    alertDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Internet Connection error", Toast.LENGTH_LONG).show();
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });


        alertDialog.show();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

    private void leftScroll() {
        try {
            deltaY = 0;
            if (mMediaPlayer != null) {
                mIsScrolling = true;
                mMediaPlayer.pause();
                curr = mMediaPlayer.getCurrentPosition();
                curr = mMediaPlayer.getCurrentPosition() - 2000;
                int divid = (int) (curr / 2);
                mMediaPlayer.seekTo((int) curr);
                seekbar.setProgress((int) curr);
                seekForRev.setProgress((int) curr);
                System.out.println(mMediaPlayer.getCurrentPosition());
                ontouchShow.setVisibility(View.VISIBLE);
                seekTouchDurationTotal.setVisibility(View.VISIBLE);
                seekForRev.setVisibility(View.VISIBLE);
                seekupper.setImageResource(R.drawable.ic_fast_rewind_black_24dp);
                seekupper.setVisibility(View.VISIBLE);
                seekTouchLayout.setVisibility(View.VISIBLE);
                voltouchshow.setVisibility(View.INVISIBLE);
                volumeControl.setVisibility(View.INVISIBLE);
                brightcontrol.setVisibility(View.INVISIBLE);
                volShowLayout.setVisibility(View.INVISIBLE);
                brighttouchshow.setVisibility(View.INVISIBLE);
                brightShowLayout.setVisibility(View.INVISIBLE);
                controller.setVisibility(View.INVISIBLE);
                locker.setVisibility(View.INVISIBLE);
                ratio.setVisibility(View.INVISIBLE);
                toolbar.setVisibility(View.INVISIBLE);
                toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();

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

    private void rightScroll() {
        try {
            deltaY = 0;
            if (mMediaPlayer != null) {

                mIsScrolling = true;
                mMediaPlayer.pause();
                curr = mMediaPlayer.getCurrentPosition();
                curr = mMediaPlayer.getCurrentPosition() + 2000;
                mMediaPlayer.seekTo((int) curr);
                seekbar.setProgress((int) curr);
                seekForRev.setProgress((int) curr);
                System.out.println(mMediaPlayer.getCurrentPosition());
                ontouchShow.setVisibility(View.VISIBLE);
                seekTouchDurationTotal.setVisibility(View.VISIBLE);
                seekForRev.setVisibility(View.VISIBLE);
                seekupper.setImageResource(R.drawable.ic_fast_forward_black_24dp);
                seekupper.setVisibility(View.VISIBLE);
                seekTouchLayout.setVisibility(View.VISIBLE);
                voltouchshow.setVisibility(View.INVISIBLE);
                volShowLayout.setVisibility(View.INVISIBLE);
                brighttouchshow.setVisibility(View.INVISIBLE);
                brightShowLayout.setVisibility(View.INVISIBLE);
                controller.setVisibility(View.INVISIBLE);
                locker.setVisibility(View.INVISIBLE);
                ratio.setVisibility(View.INVISIBLE);

                toolbar.setVisibility(View.INVISIBLE);
                toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
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

    private void upBrightness() {
        try {
            if (brightprogress != 255) {

                setBrightness(brightprogress += 5);
                if (brightprogress >= 255) {
                    brightprogress = 255;

                }
                brightupper.setImageResource(R.drawable.ic_wb_sunny_black_24dp);

                System.out.println(brightprogress);
            }

            perc = (brightprogress / (float) 255) * 100;
            brighttouchshow.setText((int) perc + "%");
            brightcontrol.setProgress(brightprogress);
            brightcontrol.setVisibility(View.VISIBLE);
            brighttouchshow.setVisibility(View.VISIBLE);
            seekTouchLayout.setVisibility(View.INVISIBLE);
            seekForRev.setVisibility(View.INVISIBLE);
            volShowLayout.setVisibility(View.INVISIBLE);
            brightShowLayout.setVisibility(View.VISIBLE);
            volumeControl.setVisibility(View.INVISIBLE);
            voltouchshow.setVisibility(View.INVISIBLE);
            ontouchShow.setVisibility(View.INVISIBLE);
            controller.setVisibility(View.INVISIBLE);
            locker.setVisibility(View.INVISIBLE);
            ratio.setVisibility(View.INVISIBLE);
//        share.setVisibility(View.INVISIBLE);
//        floating.setVisibility(View.INVISIBLE);
            orientation.setVisibility(View.INVISIBLE);
//        openlist.setVisibility(View.INVISIBLE);
//        vol.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(View.INVISIBLE);
            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
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

    private void downBrightness() {
        try {
            if (brightprogress != 0) {

                setBrightness(brightprogress -= 5);
                if (brightprogress <= 0) {
                    brightprogress = 0;
                    brightupper.setImageResource(R.drawable.ic_flare_black_24dp);
                }

                System.out.println(brightprogress);
            }

            perc = (brightprogress / (float) 255) * 100;
            brighttouchshow.setText((int) perc + "%");
            brightcontrol.setProgress(brightprogress);
            volShowLayout.setVisibility(View.INVISIBLE);
            seekTouchLayout.setVisibility(View.INVISIBLE);
            seekForRev.setVisibility(View.INVISIBLE);
            brighttouchshow.setVisibility(View.VISIBLE);
            brightShowLayout.setVisibility(View.VISIBLE);
            voltouchshow.setVisibility(View.INVISIBLE);
            volumeControl.setVisibility(View.INVISIBLE);
            ontouchShow.setVisibility(View.INVISIBLE);
            controller.setVisibility(View.INVISIBLE);
            locker.setVisibility(View.INVISIBLE);
            orientation.setVisibility(View.INVISIBLE);
//        share.setVisibility(View.INVISIBLE);
//        openlist.setVisibility(View.INVISIBLE);
//        vol.setVisibility(View.INVISIBLE);
//        floating.setVisibility(View.INVISIBLE);
            ratio.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(View.INVISIBLE);
            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
            brightcontrol.setVisibility(View.VISIBLE);
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

    private void upvolume() {
        try {
            volprogess += 1;
            vol.setImageResource(R.drawable.ic_volume_up_black_24dp);
            if (volprogess > 15) {
                volprogess = 15;
            } else if (volprogess > 15) {
                volupper.setImageResource(R.drawable.ic_volume_up_black_24dp);
                final Toast toast = Toast.makeText(getApplicationContext(), "If Volume exceeds 100%, sound quality may be " +
                        "affected ,protect your hearing", Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 200);
            } else if (volprogess == 1) {
                volupper.setImageResource(R.drawable.ic_volume_up_black_24dp);
            }
            voltouchshow.setText(volprogess + "%");
            volumeControl.setProgress(volprogess);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volprogess, 0);
            volumeControl.setVisibility(View.VISIBLE);
            voltouchshow.setVisibility(View.VISIBLE);
            volShowLayout.setVisibility(View.VISIBLE);
            brighttouchshow.setVisibility(View.INVISIBLE);
            seekTouchLayout.setVisibility(View.INVISIBLE);
            seekForRev.setVisibility(View.INVISIBLE);
            brightcontrol.setVisibility(View.INVISIBLE);
            brightShowLayout.setVisibility(View.INVISIBLE);
            ontouchShow.setVisibility(View.INVISIBLE);
            System.out.println("volume =" + volprogess);
            controller.setVisibility(View.INVISIBLE);
            locker.setVisibility(View.INVISIBLE);
            ratio.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(View.INVISIBLE);
            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
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

    private void downvolume() {

        try {
            volprogess -= 1;
            if (volprogess < 1) {
                volprogess = 0;
                vol.setImageResource(R.drawable.ic_volume_off_black_24dp);
                volupper.setImageResource(R.drawable.ic_volume_off_black_24dp);
            }

            voltouchshow.setText(volprogess + "%");
            volumeControl.setProgress(volprogess);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volprogess, 0);
            voltouchshow.setVisibility(View.VISIBLE);
            volShowLayout.setVisibility(View.VISIBLE);
            brighttouchshow.setVisibility(View.INVISIBLE);
            seekForRev.setVisibility(View.INVISIBLE);
            brightcontrol.setVisibility(View.INVISIBLE);
            seekTouchLayout.setVisibility(View.INVISIBLE);
            brightShowLayout.setVisibility(View.INVISIBLE);
            ontouchShow.setVisibility(View.INVISIBLE);
            controller.setVisibility(View.INVISIBLE);
            locker.setVisibility(View.INVISIBLE);
            ratio.setVisibility(View.INVISIBLE);
            toolbar.setVisibility(View.INVISIBLE);
            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateDecelerateInterpolator()).start();
            volumeControl.setVisibility(View.VISIBLE);
            System.out.println("Voulume =" + volprogess);
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

    private void getContentName(Uri uri) throws Exception {
        try {
            videoCursorActivity = getContentResolver().query(uri, null, null, null, null);
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
                    final long size = videoCursorActivity.getLong(
                            videoCursorActivity.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));


                    File file = new File(filename);
                    file_name = String.valueOf(file);


                }
            }
            videoCursorActivity.close();
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



