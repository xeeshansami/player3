package com.play.view.player.videoplayer4k;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.play.view.player.videoplayer4k.CursorUtils.VideosAndFoldersUtility;
import com.play.view.player.videoplayer4k.Model.Video;

import com.play.view.player.videoplayer4k.R;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by DuskySolution on 12/6/2017.
 */

public class FloatWidgetService extends Service implements Serializable,TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private WindowManager mWindowManager;
    private View mFloatingWidget;
    MediaPlayer mMediaPlayer;
    AudioManager audioManager;
    int volumeDani;
    int folderposition;
    TextureView textureView;
    int id;
    Boolean checking;
    private int APP_PERMISSION_REQUEST = 1220;
    String VID_ID, artist, title;
    WindowManager.LayoutParams params;
    private FrameLayout.LayoutParams mRootParam;
    private GestureDetector detector;
    long intpro;

    private List<Video> myList;
    ImageView play_action, resize;
    private int MIN_WIDTH = 100;
    private int mProgress;
    private SharedPreferences mSharedPrefs = null;
    android.view.ViewGroup.LayoutParams lp;
    private boolean showButtons;
    private boolean showNoti;

    public FloatWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Bundle b = intent.getExtras();
        VID_ID = b.getString("Filename");
        artist = b.getString("artist");
        intpro = b.getLong("progress");
        Log.i("floatCurrPosition1"," = " +intpro);
        folderposition = b.getInt("folderposition");
        myList =new ArrayList<>();
        if (folderposition == -2) {
            loadAllVideos();
        } else {
            loadvideos(folderposition);
        }
        showButtons = b.getBoolean("Showbuttons", true);
        showNoti = b.getBoolean("checkNot", false);
        title = b.getString("title");
        id = b.getInt("id",0);
//        v.setVideoURI(Uri.parse(VID_ID));
//        v.requestFocus();
//        if (v != null) {
//            v.seekTo(mProgress);
//        }
//        v.start();
        play_action.setImageResource(R.drawable.ic_pause_white_24dp);
        mSharedPrefs = getSharedPreferences("com.azhar.azhar.player", Context.MODE_PRIVATE);
//        mProgress = mSharedPrefs.getInt("videoprogress", 0);
//        if (mMediaPlayer != null) {
//            mMediaPlayer.seekTo(mProgress);
//        }
        return START_NOT_STICKY;
    }


    @Override
    public void onCreate() {

//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !canDrawOverlays(this)) {
//            Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//            if (getApplicationContext() instanceof Activity) {
//                ((Activity) getApplicationContext()).startActivityForResult(intent, APP_PERMISSION_REQUEST);
//            } else {
//                Intent intenti = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                intenti.setData(Uri.parse("package:" + this.getPackageName()));
//                intenti.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intenti);
//            }
//        }
        super.onCreate();
        mMediaPlayer = new MediaPlayer();//new MediaPlayer instance

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeDani = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeDani, 0);
        Log.i("volumeDani",""+volumeDani);

        mSharedPrefs = getSharedPreferences("com.azhar.azhar.player", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
        mEditor.putInt("oneattime", 1);
//        mEditor.putInt("SongPosition", videoIndex);
        mEditor.apply();
        mEditor.commit();
//        mProgress = mSharedPrefs.getInt("videoprogress", 0);
//        if (mMediaPlayer != null) {
//            mMediaPla
//            yer.seekTo(mProgress);
//        }
//
        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        textureView = (TextureView) mFloatingWidget.findViewById(R.id.collapsed_iv);
        textureView.setSurfaceTextureListener(this);
//         detector = new GestureDetector(getApplicationContext(), new TapDetector());
        play_action = (ImageView) mFloatingWidget.findViewById(R.id.playpausebtn);
        resize = (ImageView) mFloatingWidget.findViewById(R.id.resize);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }else{
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }


        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingWidget, params);
        checking = true;




        final View collapsedView = mFloatingWidget.findViewById(R.id.collapse_view);
//        final View expandedView = mFloatingWidget.findViewById(R.id.expanded_container);


        ImageView closeButtonCollapsed = (ImageView) mFloatingWidget.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseMediaPlayer();
                stopSelf();
                mSharedPrefs = getSharedPreferences("com.azhar.azhar.player", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPrefs.edit();
                mEditor.putInt("oneattime", 0);
//        mEditor.putInt("SongPosition", videoIndex);
                mEditor.apply();
                mEditor.commit();
            }
        });
        ImageView resize = (ImageView) mFloatingWidget.findViewById(R.id.resize);
        resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSharedPrefs = getApplicationContext().getSharedPreferences("com.azhar.azhar.player", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPrefs.edit();
                if (mMediaPlayer != null) {

                    mProgress = mMediaPlayer.getCurrentPosition();
                }
//                mEditor.putInt("videoprogress", mProgress);
//        mEditor.putInt("SongPosition", videoIndex);
                mEditor.apply();
                mEditor.commit();
                releaseMediaPlayer();
                stopMedia();

                Intent i = new Intent(FloatWidgetService.this, VideoDetailActivityFliper.class);
                i.putExtra("videofilename", VID_ID);
                i.putExtra("artist", artist);
                i.putExtra("title", title);
                i.putExtra("videoprogress", mProgress);
                i.putExtra("folderposition", folderposition);
                i.putExtra("Showbuttons", showButtons);
                i.putExtra("ShowNoti",showNoti);
                i.putExtra("id", id);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getApplicationContext().startActivity(i);

                mSharedPrefs = getSharedPreferences("com.azhar.azhar.player", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor1 = mSharedPrefs.edit();
                mEditor1.putInt("oneattime", 0);
                mEditor1.apply();
                mEditor1.commit();

            }
        });


        play_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaPlayer != null){
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        // Changing button image to play button
                        play_action.setImageResource(R.drawable.ic_play_arrow_white_24dp);


                    } else if (!mMediaPlayer.isPlaying()) {
                        play_action.setImageResource(R.drawable.ic_pause_white_24dp);
                        // Resume song

                        mMediaPlayer.start();
                        // Changing button image to pause button

                    } else {
                        play_action.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                        mMediaPlayer.pause();

                    }
                    mSharedPrefs = getSharedPreferences("com.azhar.azhar.player", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mSharedPrefs.edit();
                    mEditor.putInt("oneattime", 0);
//        mEditor.putInt("SongPosition", videoIndex);
                    mEditor.apply();
                    mEditor.commit();
                }
            }
        });

//        v.findViewById(R.id.collapsed_iv).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                mScaleGestureDetector.onTouchEvent(motionEvent);
//                return true;
//            }
//        });
        mFloatingWidget.findViewById(R.id.collapse_view).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                mScaleGestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {
//                            if (isViewCollapsed()) {
////                                collapsedView.setVisibility(View.GONE);
////                                expandedView.setVisibility(View.VISIBLE);
//                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingWidget, params);
                        return true;
                }
                return false;
            }
        });
//        lp = textureView.getLayoutParams();
    }
//    private boolean isViewCollapsed() {
//        return mFloatingWidget == null || mFloatingWidget.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedPrefs = getApplicationContext().getSharedPreferences("com.azhar.azhar.player", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
        if (mMediaPlayer != null) {

            mProgress = mMediaPlayer.getCurrentPosition();
        }
//        mEditor.putInt("videoprogress", mProgress);
//        mEditor.putInt("SongPosition", videoIndex);
        mEditor.apply();
        mEditor.commit();
        stopForeground(true);
        releaseMediaPlayer();
        stopSelf();

        if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
        checking = false;

    }
    public void stopMedia() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {

        if (mMediaPlayer != null) {

//            mMediaPlayer = new MediaPlayer();

            Surface surfaceView = new Surface(surfaceTexture);
//        mMediaPlayer.setDisplay(mSurfaceHolder);
            try {
                mMediaPlayer.setDataSource(VID_ID);
                mMediaPlayer.setSurface(surfaceView);
                mMediaPlayer.prepare();
                mMediaPlayer.setOnCompletionListener(FloatWidgetService.this);
                mMediaPlayer.setOnPreparedListener(FloatWidgetService.this);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeDani, 0);
//                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//                songPosnPre = mSharedPrefs.getInt("SongPosition",0);
//                if(songPosnPre == songPosn){
//                mProgress = 0;
               /* if (mMediaPlayer.getDuration() <= 30000) {
                    mMediaPlayer.seekTo(0);
                } else {*/
                    mMediaPlayer.seekTo((int) intpro);
//                }
//                    System.out.println("On Start set Last Song position Saved" + songPosnPre);
//
//                }else {
//                    mMediaPlayer.seekTo(0);
//                }
                mMediaPlayer.start();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

//        if(!mMediaPlayer.isPlaying()){
//            mMediaPlayer.start();
//        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopForeground(true);
        stopSelf();

        if (mFloatingWidget != null)
            if (checking.equals(Boolean.TRUE)) {

                mWindowManager.removeView(mFloatingWidget);
            }
    }


//    private class MyScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
//        private int mW, mH;
//
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            // scale our view view
//            mW *= detector.getScaleFactor();
//            mH *= detector.getScaleFactor();
//            if (mW < MIN_WIDTH) { // limits width
//                mW = v.getWidth();
//                mH = v.getHeight();
//            }
//            Log.d("onScale", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
//            v.setFixedVideoSize(mW, mH); // important
//            mRootParam.width = mW;
//            mRootParam.height = mH;
//            return true;
//        }
//
//        @Override
//        public boolean onScaleBegin(ScaleGestureDetector detector) {
//            mW = v.getWidth();
//            mH = v.getHeight();
//            Log.d("onScaleBegin", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
//            return true;
//        }
//
//        @Override
//        public void onScaleEnd(ScaleGestureDetector detector) {
//            Log.d("onScaleEnd", "scale=" + detector.getScaleFactor() + ", w=" + mW + ", h=" + mH);
//        }
//
//    }
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
