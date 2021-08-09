package com.play.view.player.videoplayer4k;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.play.view.player.videoplayer4k.CursorUtils.VideosAndFoldersUtility;
import com.play.view.player.videoplayer4k.Model.Video;

import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends AppCompatActivity{

    private VrVideoView videoWidgetView;
    SeekBar vrvideoSeekbar;
    SharedPreferences mSharedPrefs = null;
    //    TextureView textureView;
//    MediaPlayer mMediaPlayer;
//    private String filename="/storage/emulated/0/zapya/view/Atif_Aslam_New__Sad_Song_(2017)_Khair_Mangda___with_Raihan_Vai.!!!.mp4";
//
//    String filnamethree = "/storage/emulated/0/DCIM/Music/videoplayback.mp4";
//
//    String filenameThreeMo = "/storage/emulated/0/DCIM/Music/mo.mp4";
    private int mProgresssixty,mProgress;
    private long dura;
    private String filenameThreeSixty;
    private boolean showButtons ,showNoti;
    String  artist, id, title;
    private List<Video> myList;
    long intprog,curr;
    int folderPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_main4);


        final Intent i = getIntent();
        Bundle extras = i.getExtras();
        filenameThreeSixty = extras.getString("FilenameThreeSixty");
        artist = extras.getString("artist");
        curr = extras.getLong("progress");
        folderPosition  = extras.getInt("folderposition");
        myList=new ArrayList<>();
        if (folderPosition == -2) {
            loadAllVideos();
        } else {
            loadvideos(folderPosition);
        }
        showButtons = extras.getBoolean("Showbuttons", true);
        showNoti = extras.getBoolean("ShowNoti", true);
        title = extras.getString("title");
        id = extras.getString("id");
        intprog = extras.getLong("progress");

//        String uriPath2 = "android.resource://com.example.azhar.playerapp/"+R.raw.mo;
//        Uri uri2 = Uri.parse(uriPath2);
        videoWidgetView = (VrVideoView) findViewById(R.id.video_view);

        videoWidgetView.setKeepScreenOn(true);

        vrvideoSeekbar = (SeekBar) findViewById(R.id.vrVideoSeek);

        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        mProgress = mSharedPrefs.getInt("videoprogress", 0);
        if (videoWidgetView != null) {
            videoWidgetView.seekTo(intprog);

        }

        vrvideoSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                intprog = i;
                if (b) {
                    // this is when actually seekbar has been seeked to a new position
                    if (videoWidgetView != null) {
                        videoWidgetView.seekTo(intprog);

                    }
//                    mSharedPrefs = getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor mEditor = mSharedPrefs.edit();
//                     System.out.println( "mMySeekBarProgress"+mProgress);
//                    mEditor.putInt("mMySeekBarProgress", mProgress).apply();

//                    mSharedPrefs = getSharedPreferences("pre_name", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor mEditor = mSharedPrefs.edit();
//                    mProgress = seekbar.getProgress();
                    //                    System.out.println("Prgress Save" + mProgress);
//                    mEditor.putInt("mMySeekBarProgress", mProgress).commit();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        videoWidgetView.setInfoButtonEnabled(false);
        videoWidgetView.setStereoModeButtonEnabled(false);
        videoWidgetView.setFullscreenButtonEnabled(false);


//        videoWidgetView.setScaleX(-1);

//        Button button = (Button) findViewById(R.id.mirror);
//        Button threeview = (Button) findViewById(R.id.three_view);
//        threeview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                textureView.setVisibility(View.INVISIBLE);
//                videoWidgetView.setVisibility(View.VISIBLE);
//                videoWidgetView.playVideo();
////                mMediaPlayer.pause();
//
//                dura = videoWidgetView.getDuration();
//
//                vrvideoSeekbar.setMax((int) dura);
//
// //        System.out.println(total);
//
//
//                vrvideoSeekbar.postDelayed(onEverySecond, 300);
//            }
//        });
//        Button remover = (Button) findViewById(R.id.mirrorremove);
//        button.setOnClickListener(this);
//        remover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                textureView.setScaleX(1);
////                textureView.setVisibility(View.VISIBLE);
//                videoWidgetView.setVisibility(View.INVISIBLE);
//                videoWidgetView.pauseVideo();
////                mMediaPlayer.start();
//            }
//        });
//
//        mMediaPlayer = new MediaPlayer();
//        textureView = (TextureView) findViewById(R.id.textureview);
//
//        textureView.setSurfaceTextureListener(this);
//        // initialize the view here

        VrVideoView.Options options = new VrVideoView.Options();


// This tells the player that the view is a monoscopic 360 view
        options.inputType = VrVideoView.Options.TYPE_MONO;
//
//// This tells the player that it should play using HLS or progressive view play
//// If you are linking to a single view file, use default.
        options.inputFormat = VrVideoView.Options.FORMAT_DEFAULT;

// Assuming you've downloaded the view...
        try {
            videoWidgetView.loadVideo(Uri.parse(filenameThreeSixty), options);
//            videoWidgetView.playVideo();

        } catch (IOException e) {
            e.printStackTrace();
        }

        fullScreen();
        } catch (IndexOutOfBoundsException e) {
            Log.e("ExceptionError"," = "+e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("ExceptionError"," = "+e.getMessage());
        } catch (ActivityNotFoundException e) {
            Log.e("ExceptionError"," = "+e.getMessage());
        } catch (SecurityException e) {
            Log.e("ExceptionError"," = "+e.getMessage());
        } catch (IllegalStateException e) {
            Log.e("ExceptionError"," = "+e.getMessage());
        } catch (NullPointerException e) {
            Log.e("ExceptionError"," = "+e.getMessage());
        }catch (OutOfMemoryError e) {
            Log.e("ExceptionError"," = "+e.getMessage());
        } catch (RuntimeException e) {
            Log.e("ExceptionError"," = "+e.getMessage());
        } catch (Exception e) {
            Log.e("ExceptionError"," = "+e.getMessage());
        } finally {
            Log.e("ExceptionError"," = Finally");
        }
    }

    public void fullScreen() {
        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private Runnable onEverySecond = new Runnable() {

        @Override
        public void run() {

            if (vrvideoSeekbar != null && videoWidgetView != null) {
                curr = videoWidgetView.getCurrentPosition();

                vrvideoSeekbar.setProgress((int) curr);


                if (isPlayingVideoVr()) {
                    vrvideoSeekbar.postDelayed(onEverySecond, 300);

                } else if (!isPlayingVideoVr()) {
                    vrvideoSeekbar.postDelayed(onEverySecond, 300);
                }
            }


        }
    };

    private boolean isPlayingVideoVr() {
        videoWidgetView.playVideo();
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
        if (videoWidgetView != null) {

            mProgress = (int) videoWidgetView.getCurrentPosition();
        }
        System.out.println("onPause Last saved Progress" + mProgress);
        mEditor.putInt("videoprogress", mProgress);
//        mEditor.putInt("SongPosition", songPosn);
        mEditor.apply();
//        Intent backtodetail = new Intent(this, VideoDetailActivityFliper.class);
//        backtodetail.putExtra("videofilename", filenameThreeSixty);
//        backtodetail.putExtra("title", title);
//        backtodetail.putExtra("id", id);
//        backtodetail.putExtra("mylistVideo", myList);
//        backtodetail.putExtra("Showbuttons", false);
//        backtodetail.putExtra("ShowNoti", true);
//        startActivity(backtodetail);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoWidgetView.pauseVideo();
        mSharedPrefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
        if (videoWidgetView != null) {

            mProgresssixty = (int) videoWidgetView.getCurrentPosition();
        }
        System.out.println("onPause Last saved Progress" + mProgresssixty);
        mEditor.putInt("videothreesixty", mProgresssixty);
//        mEditor.putInt("SongPosition", songPosn);
        mEditor.apply();

        Intent backtodetail = new Intent(this, VideoDetailActivityFliper.class);
        backtodetail.putExtra("videofilename", filenameThreeSixty);
        backtodetail.putExtra("title", title);
        backtodetail.putExtra("true360", true);
        backtodetail.putExtra("id", id);
        backtodetail.putExtra("folderposition", folderPosition);
        backtodetail.putExtra("Showbuttons", false);
        backtodetail.putExtra("ShowNoti", true);
        backtodetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backtodetail);
    }
    public void loadvideos(int posi) {
        try {
            this.myList.clear();
            this.myList = new VideosAndFoldersUtility(this).fetchVideosByFolder(VideoFolder.folders.get(posi).getPath());
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void loadAllVideos() {
        try {
            this.myList.clear();
            this.myList=VideoFolder.videos;
        } catch (IndexOutOfBoundsException e) {
        }
    }
}
