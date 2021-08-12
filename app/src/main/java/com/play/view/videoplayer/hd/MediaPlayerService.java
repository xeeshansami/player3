package com.play.view.videoplayer.hd;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.play.view.videoplayer.hd.Model.Video;

import com.play.view.videoplayer.hd.R;

import java.io.IOException;
import java.util.List;

//import com.squareup.picasso.Picasso;

/**
 * Created by DuskySolution on 12/11/2017.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,

        AudioManager.OnAudioFocusChangeListener {
    private int mProgress = 0;
    int folderposition;
    public static final String ACTION_PLAY = "com.azhar.azhar.videoplayer.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.azhar.azhar.videoplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.azhar.azhar.videoplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.azhar.azhar.videoplayer.ACTION_NEXT";
    public static final String ACTION_STOP = "com.azhar.azhar.videoplayer.ACTION_STOP";
    public static final String CLOSE_ACTION = "com.azhar.azhar.videoplayer.ACTION_CLOSE";

    private MediaPlayer mediaPlayer;

    //MediaSession
    private MediaSessionManager mediaSessionManager;
    public MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;

    //Used to pause/resume MediaPlayer
    private int resumePosition;

    //AudioFocus
    private AudioManager audioManager;

    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();

    //List of available Audio files
    private List<Video> audioList;
    private int audioIndex;
    private Video activeAudio; //an object on the currently playing audio


    //Handle incoming phone calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    private SharedPreferences mSharedPrefs;
    private int songid;
    private WallpaperManager wallpaperManager;
    Bitmap largeIcon;

    /**
     * Service lifecycle methods
     */
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    //    @Override
//    public void onStart(Intent intent, int startId) {
//        new  GetAudioListAsynkTask(this).execute((Void) null);
//
//
//    }
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mediaPlayer = new MediaPlayer();//new MediaPlayer instance

            // Perform one-time setup procedures
            mSharedPrefs = getSharedPreferences("pre_name", 0);
            mProgress = mSharedPrefs.getInt("mMySeekBarProgress", 0);
            System.out.println("Last Prgress Saved" + mProgress);
            if (mediaPlayer != null) {
                Log.i("Get Duration", String.valueOf(mediaPlayer.getDuration()));

                if (mediaPlayer.getDuration() <= 60000) {
                    mediaPlayer.seekTo(0);
                } else {
                    mediaPlayer.seekTo(mProgress);
                }

            }
            // Manage incoming phone calls during playback.
            // Pause MediaPlayer on incoming call,
            // Resume on hangup.
            callStateListener();
            //ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
            registerBecomingNoisyReceiver();
            //Listen for new Audio to play -- BroadcastReceiver
            register_playNewAudio();
//        registered_receiver_activity();
            new GetAudioListAsynkTask(MediaPlayerService.this).execute((Void) null);
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

    //The system calls this method when an activity, requests the service be started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent != null) {
                final String action = intent.getAction();
                if (action != null) {
                    switch (action) {
                        case CLOSE_ACTION:
                            stopSelf();
                            break;
                    }
                }
            }
            try {
                new GetAudioListAsynkTask(MediaPlayerService.this).execute((Void) null);

                //Load data from SharedPreferences
                StorageUtil storage = new StorageUtil(getApplicationContext());
                audioList = storage.loadAudio();
                audioIndex = storage.loadAudioIndex();

                if (audioIndex >= 0 && audioIndex < audioList.size()) {
                    //index is in a valid range
                    activeAudio = audioList.get(audioIndex);
                } else {
                    stopSelf();
                }

            } catch (NullPointerException e) {
                stopSelf();
            }

            //Request audio focus
            if (requestAudioFocus() == false) {
                //Could not gain focus
                stopSelf();
            }

            if (mediaSessionManager == null) {
                try {
                    initMediaSession();
                    initMediaPlayer();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    stopSelf();
                }
                buildNotification(PlaybackStatus.PLAYING);
            }
            mSharedPrefs = getSharedPreferences("pre_name", 0);
            mProgress = mSharedPrefs.getInt("mMySeekBarProgress", 0);
            System.out.println("Last Prgress Saved" + mProgress);
            if (mediaPlayer != null) {
                Log.i("Get Duration", String.valueOf(mediaPlayer.getDuration()));

                if (mediaPlayer.getDuration() <= 60000) {
                    mediaPlayer.seekTo(0);
                } else {
                    mediaPlayer.seekTo(mProgress);
                }
            }
            //Handle Intent action from MediaSession.TransportControls
            handleIncomingActions(intent);
            broadcast(audioIndex);
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

        }


        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        try {
            mediaSession.release();
            removeNotification();
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

        }


        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mSharedPrefs = getSharedPreferences("pre_name", 0);
            SharedPreferences.Editor mEditor = mSharedPrefs.edit();
//        mProgress = seekbar.getProgress();
            if (mediaPlayer != null) {
                mProgress = mediaPlayer.getCurrentPosition();
            }
            mEditor.putInt("mMySeekBarProgress", mProgress).apply();
            System.out.println(mProgress);


            if (mediaPlayer != null) {
                stopMedia();

            }
            removeAudioFocus();
            //Disable the PhoneStateListener
            if (phoneStateListener != null) {
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            }

            removeNotification();

            //unregister BroadcastReceivers
            unregisterReceiver(becomingNoisyReceiver);
            unregisterReceiver(playNewAudio);
//        unregisterReceiver(activiyResponse);
            //clear cached playlist
            new StorageUtil(getApplicationContext()).clearCachedAudioPlaylist();

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


    /**
     * Service Binder
     */
    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MediaPlayerService.this;
        }
    }


    /**
     * MediaPlayer callback methods
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
            //        stopMedia();

//        removeNotification();
            //stop the service
//        stopSelf();
//        broadcast(ACTION_NEXT);
//        skipToNext();
//        updateMetaData();
//        buildNotification(PlaybackStatus.PLAYING);
//        new  GetAudioListAsynkTask(MediaPlayerService.this).execute((Void) null);
            //Invoked when playback of a media source has completed.
//        stopMedia();
//
//        removeNotification();
//        //stop the service
//        stopSelf();
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
    public boolean onError(MediaPlayer mp, int what, int extra) {
        try {
            //Invoked when there has been an error during an asynchronous operation
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                    Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                    break;
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                    break;
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
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        try {
            //Invoked when the media source is ready for playback.
            playMedia();
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
    public void onSeekComplete(MediaPlayer mp) {
        //Invoked indicating the completion of a seek operation.

    }

    @Override
    public void onAudioFocusChange(int focusState) {
        try {
            //Invoked when the audio focus of the system is updated.
            switch (focusState) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    // resume playback
//                if (mediaPlayer == null) initMediaPlayer();
////                else if (!mediaPlayer.isPlaying()) mediaPlayer.start();
//                mediaPlayer.setVolume(1.0f, 1.0f);
//                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    // Lost focus for an unbounded amount of time: stop playback and release media videoplayer
//                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
//                mediaPlayer.release();
//                mediaPlayer = null;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // Lost focus for a short time, but we have to stop
                    // playback. We don't release the media videoplayer because playback
                    // is likely to resume
//                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // Lost focus for a short time, but it's ok to keep playing
                    // at an attenuated level
//                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
//                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                    break;
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


    /**
     * AudioFocus
     */
    private boolean requestAudioFocus() {
        try {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                //Focus gained
                return true;
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
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }


    /**
     * MediaPlayer actions
     */
    public void initMediaPlayer() {
        try {
            if (mediaPlayer != null)

                //Set up MediaPlayer event listeners
                mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnInfoListener(this);
            //Reset so that the MediaPlayer is not pointing to another data source
            mediaPlayer.reset();


            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                // Set the data source to the mediaFile location
                mediaPlayer.setDataSource(activeAudio.getData());
            } catch (IOException e) {
                e.printStackTrace();
                stopSelf();
            }
            mediaPlayer.prepareAsync();
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

    public void playMedia() {
        try {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
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
            if (mediaPlayer == null) return;
            else if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
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

    public void pauseMedia() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                resumePosition = mediaPlayer.getCurrentPosition();
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

    public void resumeMedia() {
        try {

            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(resumePosition);
                mediaPlayer.start();
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

    public void skipToNext() {
        try {
            new GetAudioListAsynkTask(MediaPlayerService.this).execute((Void) null);

//        broadcast(ACTION_NEXT);

            if (audioIndex == audioList.size() - 1) {
                //if last in playlist
                audioIndex = 0;
                activeAudio = audioList.get(audioIndex);
            } else {
                //get next in playlist
                activeAudio = audioList.get(++audioIndex);
            }
//        audioIndex++;
//        if (audioIndex >= audioList.size()) audioIndex = 0;
            //Update stored index
            new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);

            stopMedia();
            //reset mediaPlayer

            initMediaPlayer();
            broadcast(audioIndex);
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

    public void skipToPrevious() {
        try {
            new GetAudioListAsynkTask(MediaPlayerService.this).execute((Void) null);

//        broadcast(ACTION_PREVIOUS);

            if (audioIndex == 0) {
                //if first in playlist
                //set index to the last of audioList
                audioIndex = audioList.size() - 1;
                activeAudio = audioList.get(audioIndex);
            } else {
                //get previous in playlist
                activeAudio = audioList.get(--audioIndex);
            }
//        audioIndex--;
//        if (audioIndex < 0) audioIndex = audioList.size() - 1;
            //Update stored index
            new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);

            stopMedia();
            //reset mediaPlayer
//        mediaPlayer.reset();
            initMediaPlayer();
            broadcast(audioIndex);
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


    /**
     * ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs
     */
    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                //pause audio on ACTION_AUDIO_BECOMING_NOISY
                pauseMedia();
                buildNotification(PlaybackStatus.PAUSED);
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

    private void registerBecomingNoisyReceiver() {
        try {
            //register after getting audio focus
            IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
            registerReceiver(becomingNoisyReceiver, intentFilter);
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

    /**
     * Handle PhoneState changes
     */
    private void callStateListener() {
        try {
            // Get the telephony manager
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //Starting listening for PhoneState changes
            phoneStateListener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    switch (state) {
                        //if at least one call exists or the phone is ringing
                        //pause the MediaPlayer
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                        case TelephonyManager.CALL_STATE_RINGING:
                            if (mediaPlayer != null) {
                                pauseMedia();
                                ongoingCall = true;
                            }
                            break;
                        case TelephonyManager.CALL_STATE_IDLE:
                            // Phone idle. Start playing.
                            if (mediaPlayer != null) {
                                if (ongoingCall) {
                                    ongoingCall = false;
//                                resumeMedia();
                                    pauseMedia();
                                }
                            }
                            break;
                    }
                }
            };
            // Register the listener with the telephony manager
            // Listen for changes to the device call state.
            telephonyManager.listen(phoneStateListener,
                    PhoneStateListener.LISTEN_CALL_STATE);
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


    /**
     * MediaSession and Notification actions
     */
    public void initMediaSession() throws RemoteException {
        try {
            if (mediaSessionManager != null) return; //mediaSessionManager exists

            mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
            // Create a new MediaSession
            mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
            //Get MediaSessions transport controls
            transportControls = mediaSession.getController().getTransportControls();
            //set MediaSession -> ready to receive media commands
            mediaSession.setActive(true);
            //indicate that the MediaSession handles transport control commands
            // through its MediaSessionCompat.Callback.
            mediaSession.setFlags(

                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);

            //Set mediaSession's MetaData
            updateMetaData();

            // Attach Callback to receive MediaSession updates
            mediaSession.setCallback(new MediaSessionCompat.Callback() {
                // Implement callbacks
                @Override
                public void onPlay() {
                    super.onPlay();
//                broadcast(ACTION_PLAY);
                    resumeMedia();
                    buildNotification(PlaybackStatus.PLAYING);
                }

                @Override
                public void onPause() {
                    super.onPause();
//                broadcast(ACTION_PAUSE);
                    pauseMedia();
                    buildNotification(PlaybackStatus.PAUSED);

                }

                @Override
                public void onSkipToNext() {
                    super.onSkipToNext();

                    skipToNext();
                    updateMetaData();
                    buildNotification(PlaybackStatus.PLAYING);
//                broadcast(ACTION_NEXT);
                    new GetAudioListAsynkTask(MediaPlayerService.this).execute((Void) null);
                }

                @Override
                public void onSkipToPrevious() {
                    super.onSkipToPrevious();

                    skipToPrevious();
                    updateMetaData();
                    buildNotification(PlaybackStatus.PLAYING);
//                broadcast(ACTION_PREVIOUS);
                    new GetAudioListAsynkTask(MediaPlayerService.this).execute((Void) null);
                }

                @Override
                public void onStop() {
                    super.onStop();
                    removeNotification();
                    //Stop the service
                    stopSelf();
                }

                @Override
                public void onSeekTo(long position) {
                    super.onSeekTo(position);
                }
            });
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

    private void updateMetaData() {
        try {
//        Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
//                R.drawable.ic_launcher_background); //replace with medias albumArt
            // Update the current metadata

            if (activeAudio != null) {
                mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, activeAudio.getData())
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, activeAudio.getName())
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, activeAudio.getName())
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, activeAudio.getName())
                        .build());

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void buildNotification(PlaybackStatus playbackStatus) {
        try {

            new GetAudioListAsynkTask(MediaPlayerService.this).execute((Void) null);

            /**
             * Notification actions -> playbackAction()
             *  0 -> Play
             *  1 -> Pause
             *  2 -> Next track
             *  3 -> Previous track
             */

            int notificationAction = R.drawable.ic_pause_white_24dp;//needs to be initialized
            PendingIntent play_pauseAction = null;

            //Build a new notification according to the current state of the MediaPlayer
            if (playbackStatus == PlaybackStatus.PLAYING) {
                notificationAction = R.drawable.ic_pause_white_24dp;
                //create the pause action
                play_pauseAction = playbackAction(1);
            } else if (playbackStatus == PlaybackStatus.PAUSED) {
                notificationAction = R.drawable.ic_play_arrow_white_24dp;
                //create the play action
                play_pauseAction = playbackAction(0);
            }
            final Intent notificationIntent_Close = new Intent(this, MediaPlayerService.class);
            notificationIntent_Close.setAction(CLOSE_ACTION); // use Action

            PendingIntent closeIntent = PendingIntent.getService(this,
                    0, notificationIntent_Close, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
//            // Configure the notification channel.
//            notificationChannel.setDescription("Channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            notificationManager.createNotificationChannel(notificationChannel);
//
//        }        // Hide the timestamp
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//        notificationBuilder.setAutoCancel(true)
//
//                .setShowWhen(false)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//
//                // Set the Notification style
//                .setStyle(new MediaStyle()
//                        // Attach our MediaSession token
//                        .setMediaSession(mediaSession.getSessionToken())
//                        // Show our playback controls in the compat view
//                        .setShowActionsInCompactView(0, 1, 2))
//                // Set the Notification color
//                .setColor(getResources().getColor(android.R.color.black))
//                .addAction(R.drawable.ic_close_black_24dp, null, closeIntent)
//                .setOngoing(true)
//                // Set the large and small icons
//                .setLargeIcon(largeIcon)
//                .setSmallIcon(R.drawable.head)
//                // Set Notification content information
//                .setContentInfo(activeAudio.getArtist())
//                .setContentText(activeAudio.getAlbum())
//                .setContentTitle(activeAudio.getName())
//                // Add playback actions
//                .addAction(R.drawable.ic_skip_previous_black_24dp, "previous", playbackAction(3))
//                .addAction(notificationAction, "pause", play_pauseAction)
//                .addAction(R.drawable.ic_skip_next_black_24dp, "next", playbackAction(2));
//
//        notificationManager.notify(/*notification id*/0, notificationBuilder.build());
//
//        Intent intent = new Intent(this, AudioDetail.class);
//        intent.putExtra("filename", activeAudio.getData());
//        intent.putExtra("title", activeAudio.getName());
//        intent.putExtra("ShowNoti", true);
//        songid = audioIndex;
//        intent.putExtra("id", songid);
//        intent.putExtra("mylist", audioList);
//        intent.putExtra("artist", activeAudio.getArtist());
//        intent.putExtra("bmp_Image", activeAudio.getImage());
////        intent.setAction(Intent.ACTION_MAIN);
////        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        notificationBuilder.setContentIntent(contentIntent);
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


    private PendingIntent playbackAction(int actionNumber) {
        try {
            Intent playbackAction = new Intent(this, MediaPlayerService.class);
            switch (actionNumber) {
                case 0:
                    // Play
                    playbackAction.setAction(ACTION_PLAY);
//               return PendingIntent.getBroadcast(this,actionNumber,playbackAction,0);
                    return PendingIntent.getService(this, actionNumber, playbackAction, 0);
                case 1:
                    // Pause
                    playbackAction.setAction(ACTION_PAUSE);
                    return PendingIntent.getService(this, actionNumber, playbackAction, 0);
                case 2:
                    // Next track
                    playbackAction.setAction(ACTION_NEXT);
                    return PendingIntent.getService(this, actionNumber, playbackAction, 0);
                case 3:
                    // Previous track
                    playbackAction.setAction(ACTION_PREVIOUS);
                    return PendingIntent.getService(this, actionNumber, playbackAction, 0);
                default:
                    break;
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
        return null;
    }

    private void removeNotification() {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
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

    private void handleIncomingActions(Intent playbackAction) {
        try {
            if (playbackAction == null || playbackAction.getAction() == null) return;

            String actionString = playbackAction.getAction();
            if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
                transportControls.play();
            } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
                transportControls.pause();
            } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
                transportControls.skipToNext();
            } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
                transportControls.skipToPrevious();
            } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
                transportControls.stop();
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


    /**
     * Play new Audio
     */
    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                //Get the new media index form SharedPreferences
                audioIndex = new StorageUtil(getApplicationContext()).loadAudioIndex();
                if (audioIndex >= 0 && audioIndex < audioList.size()) {
                    //index is in a valid range
                    activeAudio = audioList.get(audioIndex);
                } else {
                    stopSelf();
                }

                //A PLAY_NEW_AUDIO action received
                //reset mediaPlayer to play the new Audio
                stopMedia();
                mediaPlayer.reset();
                initMediaPlayer();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
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

    private void register_playNewAudio() {
        try {
            //Register playNewMedia receiver
            IntentFilter filter = new IntentFilter(AudioDetail.Broadcast_PLAY_NEW_AUDIO);
            registerReceiver(playNewAudio, filter);
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

    private void broadcast(int position) {
        try {
            Intent intent = new Intent("");
//        intent.setAction("com.example.azhar.broadcast.MEDIA_ACTIONS");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("action", position);
            Log.i("", "action" + position);
            sendBroadcast(intent);
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

    private class GetAudioListAsynkTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
//        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
//            dialog = new ProgressDialog(getActivity());
//            dialog.setMessage("Loading...");
//            dialog.show();


        }

        public GetAudioListAsynkTask(MediaPlayerService mediaPlayerService) {
            this.context = mediaPlayerService;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

                // largeIcon = Picasso.with(getApplicationContext()).load(activeAudio.getImage()).get();


                return true;
            } catch (IndexOutOfBoundsException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
                return false;
            } catch (IllegalArgumentException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
                return false;
            } catch (ActivityNotFoundException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
                return false;
            } catch (SecurityException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
                return false;
            } catch (IllegalStateException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
                return false;
            } catch (NullPointerException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
                return false;
            } catch (OutOfMemoryError e) {
                Log.e("ExceptionError", " = " + e.getMessage());
                return false;
            } catch (RuntimeException e) {
                Log.e("ExceptionError", " = " + e.getMessage());
                return false;
            } catch (Exception e) {
                Log.e("ExceptionError", " = " + e.getMessage());
                return false;
            } finally {
                Log.e("ExceptionError", " = Finally");
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {

//            if (dialog.isShowing()) {
//                dialog.dismiss();
//            }
            try {
                wallpaperManager.setBitmap(largeIcon);

            } catch (Exception e) {


            }
        }
    }

}