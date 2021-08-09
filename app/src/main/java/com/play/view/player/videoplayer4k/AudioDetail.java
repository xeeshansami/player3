package com.play.view.player.videoplayer4k;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.core.app.NavUtils;
import androidx.core.app.ShareCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class AudioDetail extends AppCompatActivity implements Serializable {
    /**
     * Handles audio focus when playing a sound file
     */
    String[] DayOfWeek = {"BASS", "TREBLE", "FLAT", "JAZZ", "POP", "CLASSICAL", "ROCK"};

    private AudioManager mAudioManager = null;
    Equalizer equalizer;
    BassBoost bassBoost;
    Button bass, treble, jazz, rock, classical, flat, pop;
    Runnable runnable;

    public static final String Broadcast_PLAY_NEW_AUDIO = "com.example.azhar.playerapp.PlayNewAudio";
    //Class used to store data
    SharedPreferences mSharedPrefs;

    private MediaPlayerService player;
    boolean serviceBound = false;
    private ImageButton next, prev, play_pause, suffle, repeat;
    Button euailizer;

    private MediaPlayer mediaPlayer;
    private boolean repeatSong = false;
    private String filename, title, artist;
    int id;
    long albumId;
    Bitmap bm = null;
    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1, totaldura, runingtime, thumbName, thumbArtist;

    public static int oneTimeOnly = 0;
    private long dura, curr;

    ImageView thumb, card_image;
    private int length, length1, songPosn;

    private boolean shuffleSong = false;
    private Random rand;
// Add this code in a method
    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private volatile boolean threadStarted = false;
    Thread thread;
    LinearLayout eqaulizerview;
    private boolean isShowing = false;
    private int mProgress;
    private String images;
    ArrayList<Songs> myList;
    private boolean showNoti;
    Cursor cursor;
    private Uri uri;
    private boolean showButtons;
    Dialog dialog1;
    SeekBar volumeSeekbar;
    private int MAX_VOLUME = 15;
    private int volprogess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_detail);
        eqaulizerview = (LinearLayout) findViewById(R.id.eqaulizerview);
        rand = new Random();
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final Intent i = getIntent();
        Bundle extras = i.getExtras();
//        Bitmap bmp = (Bitmap) i.getParcelableExtra("bmp_Image");
        myList = (ArrayList<Songs>) getIntent().getSerializableExtra("mylist");

        Log.i("MyArray", "From intent" + Arrays.deepToString(myList.toArray()));
        images = extras.getString("bmp_Image");
        artist = extras.getString("artist");
        showButtons = extras.getBoolean("Showbuttons");
        filename = extras.getString("filename");
        title = extras.getString("title");
        showNoti = extras.getBoolean("ShowNoti");
        id = extras.getInt("id", 0);
        songPosn = id;

        System.out.println(id + title + artist + filename);
        suffle = (ImageButton) findViewById(R.id.suffle);
        repeat = (ImageButton) findViewById(R.id.repeat);
        thumb = (ImageView) findViewById(R.id.thumbin);
        thumbArtist = (TextView) findViewById(R.id.artistthumb);
        thumbName = (TextView) findViewById(R.id.thumbinname);
        thumbName.setText(title);
        thumbArtist.setText(artist);

        Picasso.get().load(images).error(R.drawable.logo)
                .placeholder(R.drawable.logo).into(thumb);
//        Glide.with(this)
//                .load(images).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.logo)
//                .into(thumb);
        play_pause = (ImageButton) findViewById(R.id.play_pause);
        play_pause.setBackgroundColor(getResources().getColor(R.color.Lightblack));
        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.previous);
//        euailizer = (Spinner) findViewById(R.id.equalizericon);
        //euailizer = (Button) findViewById(R.id.equalizericon);
        card_image = (ImageView) findViewById(R.id.card_image);
        runingtime = (TextView) findViewById(R.id.runingtime);
        totaldura = (TextView) findViewById(R.id.totaldura);
        bass = (Button) findViewById(R.id.butBass);
        treble = (Button) findViewById(R.id.buttreble);
        jazz = (Button) findViewById(R.id.jazz);
        pop = (Button) findViewById(R.id.pop);
        rock = (Button) findViewById(R.id.rock);
        flat = (Button) findViewById(R.id.flat);
        classical = (Button) findViewById(R.id.classical);
        Picasso.get().load(images).error(R.drawable.logo)
                .placeholder(R.drawable.logo).into(card_image);
//        Glide.with(this)
//                .load(images).placeholder(R.drawable.logo)
//                .into(card_image);


        mediaPlayer = new MediaPlayer();
        if (mediaPlayer != null) {

            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {


//                mediaPlayer.reset();
                mediaPlayer.setDataSource(filename);
                mediaPlayer.prepare();
                mediaPlayer.start();
                play_pause.setImageResource(R.drawable.ic_pause_white_24dp);
//


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        ImageButton vol_icon = (ImageButton) findViewById(R.id.vol_icon);
        vol_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (volumeSeekbar.getVisibility() == View.INVISIBLE) {
                    volumeSeekbar.setVisibility(View.VISIBLE);
                    eqaulizerview.setVisibility(View.INVISIBLE);
                } else if (volumeSeekbar.getVisibility() == View.VISIBLE)
                    volumeSeekbar.setVisibility(View.INVISIBLE);
                eqaulizerview.setVisibility(View.INVISIBLE);


            }
        });
        volumeSeekbar = (SeekBar) findViewById(R.id.volumeseek);
//        volumeSeekbar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
//        volumeSeekbar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        volumeSeekbar.setMax(mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekbar.setProgress(mAudioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


//                double percent = 0.70;
//                int seventyVolume = (int) (mx*percent);
//


                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, AudioManager.FLAG_PLAY_SOUND);
//                volprogess = i;
//                float volume = (float) (1 - (Math.log(MAX_VOLUME - volprogess) / Math.log(MAX_VOLUME)));
//                if (mediaPlayer != null) {
//
//                    mediaPlayer.setVolume(volume, volume);
//
//
//                }


//                volprogess = i;
//                float volume = (float) (1 - (Math.log(MAX_VOLUME - volprogess) / Math.log(MAX_VOLUME)));
//                setVolume(volume,volume);
//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
//                        i, 0);
//                volconvert = i;
//                volprogess = volconvert/100*100;
//                volprogess = i / 15 * 15;
//                volprogess = (int)(volconvert / (float) 255) * 100;
//                System.out.println(volconvert);

//                System.out.println(volprogess);
//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                //Calculate the brightness percentage
//                int perc = (i / maxVolume) * 100;
//
//                //Suhas
//                volconvert = Integer.parseInt(""+perc);

                //Set the brightness percentage

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        bassBoost = new BassBoost(0, 0);
        equalizer = new Equalizer(0, 0);


        bass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bass();
            }
        });
        treble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                treble();
            }
        });
        flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flat();
            }
        });
        jazz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jazz();
            }
        });
        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop();
            }
        });
        classical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classical();
            }
        });
        rock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rock();
            }
        });
//        Spinner spinner = (Spinner) findViewById(R.id.equalizericon);
//        spinner.setAdapter(new  MyCustomAdapter(this, R.layout.spinner_items, DayOfWeek));
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                String item = (String) adapterView.getItemAtPosition(position);
//                if (item == "BASS") {
// //                    Toast.makeText(adapterView.getContext(), "Selection " + item, Toast.LENGTH_LONG).show()
//                    bass();
//                } else if (item == "TREBLE") {
////                    startActivity(new Intent(this, MainActivity.class));
////                    Toast.makeText(adapterView.getContext(), "Selection " + item, Toast.LENGTH_LONG).show();
//                    treble();
//                } else if (item == "FLAT") {
//                    flat();
//                } else if (item == "JAZZ") {
//                    jazz();
//                } else if (item == "POP") {
//                    pop();
//                } else if (item == "CLASSICAL") {
//                    classical();
//                } else if (item == "ROCK") {
//                    rock();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        if (mediaPlayer != null) {

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.start();
                    dura = mediaPlayer.getDuration();

                    seekbar.setMax((int) dura);

                    totaldura.setText(milliSecondsToTimer(dura));
                    System.out.println(milliSecondsToTimer(dura));

                    seekbar.postDelayed(onEverySecond, 100);
                }
            });
        }
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

        }


        seekbar = (SeekBar) findViewById(R.id.seekBar);
//        seekbar.setClickable(true);
        mSharedPrefs = getSharedPreferences("pre_name", 0);
        mProgress = mSharedPrefs.getInt("mMySeekBarProgress", 0);
//        songPosnpre = mSharedPrefs.getInt("SongsPosition", 0);
//        mProgress =0;
//        if (songPosnpre == songPosn) {
        System.out.println("Last Prgress Saved" + mProgress);
//            System.out.println("Last Prgress Position Index" + songPosnpre);
        if (mediaPlayer != null) {

            Log.i("Get Duration", String.valueOf(mediaPlayer.getDuration()));

            if (mediaPlayer.getDuration() <= 60000) {
                mediaPlayer.seekTo(0);
            } else {
                mediaPlayer.seekTo(mProgress);
            }
        }
//        } else {
//            mProgress = 0;
//            mediaPlayer.seekTo(mProgress);
//        }
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                mProgress = progress;

                if (input) {
                    mediaPlayer.seekTo(mProgress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                mediaPlayer.pause();
                play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
//                broadcastSendtoService(ACTION_PAUSE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                broadcastSendtoService(ACTION_PLAY);
                mediaPlayer.start();
                play_pause.setImageResource(R.drawable.ic_pause_white_24dp);
            }
        });
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                toggleButton();
//                playAudio(songPosn);
//
////                    finalTime = mediaPlayer.getDuration();
////                    startTime = mediaPlayer.getCurrentPosition();
////
////                if (oneTimeOnly == 0) {
////                    seekbar.setMax((int) finalTime);
////                    oneTimeOnly = 1;
////                }
////
////                tx2.setText(String.format("%d : %d",
////                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
////                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
////                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
////                                        finalTime)))
////                );
////
////                tx1.setText(String.format("%d : %d",
////                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
////                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
////                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
////                                        startTime)))
////                );
////                tx3.setText(String.format("%d: %d",
////                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
////                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
////                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
////                                        startTime)))
////
////                );
//                seekbar.setProgress((int)startTime);
//                myHandler.postDelayed(UpdateSongTime,100);
//
////                b2.setEnabled(true);
////                b3.setEnabled(false);
//
//
            }
        });

//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int temp = (int)startTime;
//
//                if((temp+forwardTime)<=finalTime){
//                    startTime = startTime + forwardTime;
//                    mediaPlayer.seekTo((int) startTime);
////                    Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        b4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int temp = (int)startTime;
//
//                if((temp-backwardTime)>0){
//                    startTime = startTime - backwardTime;
//                    mediaPlayer.seekTo((int) startTime);
////                    Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


//        card_image.setOnTouchListener(new OnSwipeTouchListener(this) {
//
//            @Override
//            public void onSwipeLeft() {
//                super.onSwipeLeft();
////                broadcastSendtoService(ACTION_NEXT);
//                playNext();
//                playPauseNP();
//            }
//
//            @Override
//            public void onSwipeBottom() {
//                super.onSwipeBottom();
////                broadcastSendtoService(ACTION_PREVIOUS);
//                playPrev();
//                playPauseNP();
//            }
//
//            @Override
//            public void onSwipeTop() {
//                super.onSwipeTop();
////                broadcastSendtoService(ACTION_NEXT);
//                playNext();
//                playPauseNP();
//            }
//
//            @Override
//            public void onSwipeRight() {
//                super.onSwipeRight();
////                broadcastSendtoService(ACTION_PREVIOUS);
//                playPrev();
//                playPauseNP();
//            }
//
//        });
//        prev.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                curr = mediaPlayer.getCurrentPosition();
//                curr = mediaPlayer.getCurrentPosition() - 5000;
//                mediaPlayer.seekTo((int) curr);
//                return true;
//            }
//        });
//        next.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                curr = mediaPlayer.getCurrentPosition();
//                curr = mediaPlayer.getCurrentPosition() + 5000;
//                mediaPlayer.seekTo((int) curr);
//                return true;
//            }
//        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                broadcastSendtoService(ACTION_PREVIOUS);
                playPrev();
                playPauseNP();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                broadcastSendtoService(ACTION_NEXT);
                playNext();
                playPauseNP();

            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRepeat();
            }
        });
        suffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShuffle();
            }
        });
        if (mediaPlayer != null) {

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {


                    if (repeatSong) {
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    } else if (shuffleSong) {
//            songPosn = Integer.parseInt(id);

                        int newSong = songPosn;

                        while (newSong == songPosn) {
                            newSong = rand.nextInt(myList.size());

                        }
                        songPosn = newSong;
                        playSong(songPosn);
                    } else {
                        if (mediaPlayer.getCurrentPosition() > 0) {
                            mediaPlayer.reset();
                            playNext();
//                    broadcastSendtoService(ACTION_NEXT);

                        }
                    }


                }
            });
        }

//        euailizer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                alert();
////                if (isShowing) {
////                    isShowing = false;
////                    eqaulizerview.setVisibility(View.INVISIBLE);
////
////                } else {
////                    isShowing = true;
////                    eqaulizerview.setVisibility(View.VISIBLE);
////                }
//
//                if (eqaulizerview.getVisibility() == View.INVISIBLE) {
//                    eqaulizerview.setVisibility(View.VISIBLE);
//                    volumeSeekbar.setVisibility(View.INVISIBLE);
//                } else if (eqaulizerview.getVisibility() == View.VISIBLE)
//                    eqaulizerview.setVisibility(View.INVISIBLE);
//                volumeSeekbar.setVisibility(View.INVISIBLE);
//
//            }
//        });
//        if (showNoti) {
//            Log.d("","Song pos for notification" + String.valueOf(songPosn));
//            playAudio(songPosn);
////            Intent playerIntent = new Intent(this, MediaPlayerService.class);
////            startService(playerIntent);
//
//        } else {
//            releaseMediaPlayer();
//        }
//        Intent playerIntentVideo = new Intent(this, MediaPlayerServiceVideo.class);
//        stopService(playerIntentVideo);


        if (showButtons == true) {
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
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

    //toggle repeat
    public void setRepeat() {
        if (repeatSong) {
            repeatSong = false;
            repeat.setImageResource(R.drawable.ic_repeat_black_24dp);
            mediaPlayer.setLooping(false);
        } else {
            repeatSong = true;
            repeat.setImageResource(R.drawable.ic_repeat_one_black_24dp);
            mediaPlayer.setLooping(true);

        }

    }

    //toggle shuffle
    public void setShuffle() {
        if (shuffleSong) {
            shuffleSong = false;
            suffle.setImageResource(R.drawable.ic_shuffle_black_24dp);

        } else {
            shuffleSong = true;
            suffle.setImageResource(R.drawable.ic_shuffle_red_24dp);
//            ShuffleSongs();

        }
    }

    //skip to previous track
    public void playPrev() {
        if (shuffleSong) {
//            songPosn = Integer.parseInt(id);

            int newSong = songPosn;

            while (newSong == songPosn) {
                newSong = rand.nextInt(myList.size());

            }
            songPosn = newSong;
        } else {

            songPosn--;
            if (songPosn < 0) songPosn = myList.size() - 1;


        }
        stopMedia();
        playSong(songPosn);
    }

    //skip to next
    public void playNext() {
        if (shuffleSong) {
//            songPosn = Integer.parseInt(id);

            int newSong = songPosn;

            while (newSong == songPosn) {
                newSong = rand.nextInt(myList.size());

            }
            songPosn = newSong;
        } else {
            songPosn++;
            if (songPosn >= myList.size()) songPosn = 0;
        }
        stopMedia();
        playSong(songPosn);
//        mediaPlayer.reset();
//        getMusic();
    }


//    @SuppressWarnings("deprecation")
//    private void getMusic() {
//        // TODO Auto-generated method stub
//        uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        final String[] projection = {MediaStore.Audio.Media._ID,
//                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
//                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.SIZE,
//                MediaStore.Audio.Media.ALBUM_ID,
//                MediaStore.Audio.Media.DURATION};
//
//        cursor=this.managedQuery(uri, projection, null, null,null);
//
////        while (cursor.moveToNext()) {
//            int music_column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
//        cursor.moveToPosition(songPosn);
//            title = cursor.getString(
//                    cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//            //        if(filename != null){
//            filename = cursor.getString(music_column_index);
//
//            artist = cursor.getString(
//                    cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//            albumId = cursor.getLong(cursor
//                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//
//            Uri sArtworkUri = Uri
//                    .parse("content://media/external/audio/albumart");
//            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
//
//            Log.i("Album art", albumArtUri.toString());
//
////        }
//            System.out.println("FileName" + filename);
//            System.out.println("Title :" + title);
//
//            Picasso.with(this).load(albumArtUri.toString()).error(com.azhar.azhar.player.R.drawable.head)
//                    .placeholder(com.azhar.azhar.player.R.drawable.head).into(thumb);
//            Picasso.with(this).load(albumArtUri.toString()).error(com.azhar.azhar.player.R.drawable.head).
//                    placeholder(com.azhar.azhar.player.R.drawable.head).into(card_image);
//
//            thumbName.setText(title);
//            thumbArtist.setText(artist);
//
//            //set the data source
//            try {
//                if (mediaPlayer != null) {
//
//                    mediaPlayer.reset();
//                    mediaPlayer.setDataSource(filename);
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                }
//            } catch (Exception e) {
//                Log.e("MUSIC SERVICE", "Error setting data source", e);
//            }
//
//
//
//       }
//    }


    public void setList(ArrayList<Songs> theSongs) {
        myList = theSongs;
    }

    public void playSong(int index) {

        //get song
//        Songs playSong = myList.get(songPosn);
//
//        //get id
//        long currSong = playSong.getId();
//        //set uri
//        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                currSong);

        //play
//        if (audiocursor != null) {

//              audiocursor =  getContentResolver().query(uri,
//                    cursor_cols, where, null, sortOrder);
//            int count = audiocursor.getCount();
//        int music_column_index = audiocursor
//                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
//        audiocursor.moveToPosition(songPosn);
//        title = audiocursor.getString(
//                audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
//        filename = audiocursor.getString(music_column_index);
//
//        artist = audiocursor.getString(
//                audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
//        albumId = audiocursor.getLong(audiocursor
//                .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
//
//        Uri sArtworkUri = Uri
//                .parse("content://media/external/audio/albumart");
//        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
//
//        Log.i("Album art", albumArtUri.toString());
//
////        }
//        System.out.println("FileName" + filename);
//        System.out.println("Title :" + title);


//        Picasso.with(this).load(albumArtUri.toString()).error(com.azhar.azhar.player.R.drawable.head)
//                .placeholder(com.azhar.azhar.player.R.drawable.head).into(thumb);
//        Picasso.with(this).load(albumArtUri.toString()).error(com.azhar.azhar.player.R.drawable.head).
//                placeholder(com.azhar.azhar.player.R.drawable.head).into(card_image);
//
//        thumbName.setText(title);
//        thumbArtist.setText(artist);
        Songs audioSongsClass = myList.get(index);
        Picasso.get().load(String.valueOf(audioSongsClass.getImage()))
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo).into(thumb);
        Picasso.get().load(String.valueOf(audioSongsClass.getImage()))
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo).into(card_image);
//        Glide.with(this)
//                .load(String.valueOf(audioSongsClass.getImage())).placeholder(R.drawable.logo)
//                .into(thumb);
//        Glide.with(this)
//                .load(String.valueOf(audioSongsClass.getImage())).placeholder(R.drawable.logo)
//                .into(card_image);
        title = audioSongsClass.getName();
        filename = audioSongsClass.getData();
        artist = audioSongsClass.getArtist();


        thumbName.setText(title);
        thumbArtist.setText(artist);
        //set the data source
        try {
            if (mediaPlayer != null) {

                mediaPlayer.reset();
                mediaPlayer.setDataSource(filename);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

    }

    public void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void playPauseNP() {
        int num = 0;
        if (num == 1) {

            play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);

        } else if (num == 0) {
            play_pause.setImageResource(R.drawable.ic_pause_white_24dp);

        } else {
            play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);

        }

    }

    public void toggleButton() {
        // check for already playing
        if (mediaPlayer.isPlaying()) {

//            broadcastSendtoService(ACTION_PAUSE);
            mediaPlayer.pause();
//            if (serviceBound) {
//                unbindService(serviceConnection);
//                //service is active
//                player.stopSelf();
//            }
            // Changing button image to play button
            play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);


        } else if (!mediaPlayer.isPlaying()) {
            play_pause.setImageResource(R.drawable.ic_pause_white_24dp);
            // Resume song
//            broadcastSendtoService(ACTION_PLAY);
            mediaPlayer.start();
//            Intent intent  = new Intent(this,ForegroundService.class);
//            intent.putExtra("SongsId",songPosn);
//            startService(intent);
            //            Intent playerIntent = new Intent(this, MediaPlayerService.class);
//            startService(playerIntent);
//            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            // Changing button image to pause button

        } else {
            play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            mediaPlayer.pause();
//            broadcastSendtoService(ACTION_PAUSE);
//            if (serviceBound) {
//             unbindService(serviceConnection);
//            //service is active
//            player.stopSelf();
//        }

        }
    }

    private Runnable onEverySecond = new Runnable() {

        @Override
        public void run() {
            if (seekbar != null && mediaPlayer != null) {

                curr = mediaPlayer.getCurrentPosition();

                seekbar.setProgress((int) curr);

                runingtime.setText(milliSecondsToTimer(curr));
//                System.out.println(time);


                if (mediaPlayer.isPlaying()) {
                    seekbar.postDelayed(onEverySecond, 100);
                } else if (!mediaPlayer.isPlaying()) {
                    seekbar.postDelayed(onEverySecond, 100);


                }
            }
        }
    };


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("serviceStatus", serviceBound);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("serviceStatus");


    }

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    @TargetApi(Build.VERSION_CODES.O)
    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(this);
            storage.storeAudio(myList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            startService(playerIntent);
//            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(this);
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        eqaulizerview.setVisibility(View.INVISIBLE);
        volumeSeekbar.setVisibility(View.INVISIBLE);
//     releaseMediaPlayer();


        if(mediaPlayer.isPlaying()){
            int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.KITKAT) {
                if (showNoti) {
                    Log.d("", "Song pos for notification" + String.valueOf(songPosn));
                    playAudio(songPosn);
//            Intent playerIntent = new Intent(this, MediaPlayerService.class);
//            startService(playerIntent);

                } else {
//            mediaPlayer.reset();
                    releaseMediaPlayer();
                }
                Intent playerIntentVideo = new Intent(this, MediaPlayerServiceVideo.class);
                stopService(playerIntentVideo);




            } else {
                releaseMediaPlayer();
            }
        }else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        playAudio(songPosn);
//        Intent playerIntent = new Intent(this, MediaPlayerService.class);
//        startService(playerIntent);
//        if (showNoti) {
//            Log.d("","Song pos for notification" + String.valueOf(songPosn));
//            playAudio(songPosn);
////            Intent playerIntent = new Intent(this, MediaPlayerService.class);
////            startService(playerIntent);
//
//        } else {
//            releaseMediaPlayer();
//        }
//        if (showNoti) {
//            Log.d("", "Song pos for notification" + String.valueOf(songPosn));
//            playAudio(songPosn);
////            Intent playerIntent = new Intent(this, MediaPlayerService.class);
////            startService(playerIntent);
//
//        } else {
////            mediaPlayer.reset();
//            releaseMediaPlayer();
//        }
//        Intent playerIntentVideo = new Intent(this, MediaPlayerServiceVideo.class);
//        stopService(playerIntentVideo);
        mSharedPrefs = getSharedPreferences("pre_name", 0);
        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
        if (mediaPlayer != null) {
            mProgress = mediaPlayer.getCurrentPosition();
        }
        System.out.println("Last Prgress Position" + mProgress);
        mEditor.putInt("mMySeekBarProgress", mProgress).apply();


//        SharedPreferences.Editor ed = preferences.edit();
//        ed.putLong("time", mediaPlayer.getCurrentPosition());
//        ed.commit();
//        System.out.println("Pause Shared" + ed);

//        playAudio(songPosn);
//        Intent playerIntent = new Intent(this, MediaPlayerService.class);
//        startService(playerIntent);
//        bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onStart() {
        super.onStart();


        Intent playerIntent = new Intent(this, MediaPlayerService.class);
        stopService(playerIntent);

        Intent playerIntentVideo = new Intent(this, MediaPlayerServiceVideo.class);
        stopService(playerIntentVideo);
//        if (serviceBound) {
//            unbindService(serviceConnection);
//            //service is active
//            player.stopSelf();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Destroy");
//        if (myReciver != null) {
//            unregisterReceiver(myReciver);
//            myReciver = null;
//        }

//        Intent playerIntent = new Intent(this, MediaPlayerService.class);
//        stopService(playerIntent);
//        if (serviceBound) {
//            unbindService(serviceConnection);
//            //service is active
//            player.stopSelf();
//        }
//        playAudio(songPosn);
//        Intent playerIntent = new Intent(this, MediaPlayerService.class);
//        startService(playerIntent);
//        mSharedPrefs = getSharedPreferences("pre_name", Context.MODE_PRIVATE);
//        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
////        mProgress = seekbar.getProgress();
//        mProgress = mediaPlayer.getCurrentPosition();
//        System.out.println(mProgress);
//        mEditor.putInt("mMySeekBarProgress", mProgress).apply();

//        bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//        Intent playerIntent = new Intent(this, MediaPlayerService.class);
//        startService(playerIntent);
//        bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//        if (serviceBound) {
//            unbindService(serviceConnection);
//            //service is active
//            player.stopSelf();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
//        Intent playerIntent = new Intent(this, MediaPlayerService.class);
//        stopService(playerIntent);
//        if (serviceBound) {
//            unbindService(serviceConnection);
//            //service is active
//            player.stopSelf();
//        }
//        playAudio(songPosn);
//        Intent playerIntent = new Intent(this, MediaPlayerService.class);
//        startService(playerIntent);
//        mSharedPrefs = getSharedPreferences("pre_name", Context.MODE_PRIVATE);
//        SharedPreferences.Editor mEditor = mSharedPrefs.edit();
////        mProgress = seekbar.getProgress();
//        mProgress = mediaPlayer.getCurrentPosition();
//        System.out.println(mProgress);
//        mEditor.putInt("mMySeekBarProgress", mProgress).apply();


//        playAudio(songPosn);
//        Intent playerIntent = new Intent(this, MediaPlayerService.class);
//        startService(playerIntent);
//        bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share, menu);
        // Associate searchable configuration with the SearchView

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_item_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent() {
        String textThatYouWantToShare =
                "Lovely Music:";
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle("Share via")
                .setType("*/*")
                .setText(textThatYouWantToShare)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

//    @Override
//    public void onAudioFocusChange(int focusChange) {
//        if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
//        {
//            // Pause
//            mediaPlayer.pause();
//        }
//        else if(focusChange == AudioManager.AUDIOFOCUS_GAIN)
//        {
//            // Resume
//            mediaPlayer.start();
//        }
//        else if(focusChange == AudioManager.AUDIOFOCUS_LOSS)
//        {
//            // Stop or pause depending on your need
//            mediaPlayer.stop();
//        }
//
//    }


    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                }
//                playPauseNP();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);

                }
//                playPauseNP();

            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources

                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    play_pause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                }

//                playPauseNP();

            }
        }
    };


    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void bass() {

        bassBoost = new BassBoost(0, mediaPlayer.getAudioSessionId());
        if (bassBoost != null || equalizer != null) {
            bassBoost.setEnabled(true);

            BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
            BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
            bassBoostSetting.strength = 1800; // 1000
            bassBoost.setProperties(bassBoostSetting);
            if (bassBoost.getStrengthSupported()) {
                short word1 = bassBoost.getRoundedStrength();
                bassBoost.setStrength(word1);
                //                    bassBoost.setStrength((short) 1000); // progress value from seek bar
            }


            equalizer.release();
        }
    }

    public void treble() {
        equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        if (bassBoost != null || equalizer != null) {

            equalizer.setEnabled(true);
            bassBoost.release();

            short numPresets = equalizer.getNumberOfPresets();

//                equalizer.usePreset((short) (numPresets - 2));
            short preset = equalizer.getCurrentPreset();

            String name = equalizer.getPresetName(preset);
            System.out.println(name);

            short numberOfBands = equalizer.getNumberOfBands();

            System.out.println("numberofbands" + numberOfBands);
            final short minLevel = equalizer.getBandLevelRange()[0]; // get min range
            final short maxLevel = equalizer.getBandLevelRange()[1]; // get max range

//                short level = equalizer.getBandLevel((short) 5);
            System.out.println("numberofbandsLoop" + numberOfBands);
            System.out.println("loops" + maxLevel);
            for (short i = 0; i < numberOfBands; i++) {

                equalizer.setBandLevel((short) 0, minLevel);
                equalizer.setBandLevel((short) 1, minLevel);
                equalizer.setBandLevel((short) 2, maxLevel);
                equalizer.setBandLevel((short) 3, maxLevel);
                equalizer.setBandLevel((short) 4, maxLevel);
//                equalizer.setBandLevel((short) 5, maxLevel);
            }
        }
    }

    public void flat() {
        equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        if (bassBoost != null || equalizer != null) {

            equalizer.setEnabled(true);
            bassBoost.release();

            short numPresets = equalizer.getNumberOfPresets();


            short preset = equalizer.getCurrentPreset();
//                equalizer.usePreset((short) (numPresets - 0));
            String name = equalizer.getPresetName(preset);
            System.out.println(name);

            short numberOfBands = equalizer.getNumberOfBands();

            System.out.println("numberofbands" + numberOfBands);
            final short minLevel = equalizer.getBandLevelRange()[0]; // get min range
            final short maxLevel = equalizer.getBandLevelRange()[1]; // get max range

//                short level = equalizer.getBandLevel((short) 5);
            System.out.println("numberofbandsLoop" + numberOfBands);
            System.out.println("loops" + maxLevel);
            for (short i = 0; i < numberOfBands; i++) {

                equalizer.setBandLevel((short) 0, maxLevel);
                equalizer.setBandLevel((short) 1, minLevel);
                equalizer.setBandLevel((short) 2, maxLevel);
                equalizer.setBandLevel((short) 3, minLevel);
                equalizer.setBandLevel((short) 4, maxLevel);
//                equalizer.setBandLevel((short) 5, maxLevel);
            }
        }
    }

    public void jazz() {
        equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        if (bassBoost != null || equalizer != null) {

            equalizer.setEnabled(true);
            bassBoost.release();

            short numPresets = equalizer.getNumberOfPresets();


            short preset = equalizer.getCurrentPreset();
//                equalizer.usePreset((short) (numPresets - 0));
            String name = equalizer.getPresetName(preset);
            System.out.println(name);

            short numberOfBands = equalizer.getNumberOfBands();

            System.out.println("numberofbands" + numberOfBands);
            final short minLevel = equalizer.getBandLevelRange()[0]; // get min range
            final short maxLevel = equalizer.getBandLevelRange()[1]; // get max range

//                short level = equalizer.getBandLevel((short) 5);
            System.out.println("numberofbandsLoop" + numberOfBands);
            System.out.println("loops" + maxLevel);
            for (short i = 0; i < numberOfBands; i++) {

                equalizer.setBandLevel((short) 0, maxLevel);
                equalizer.setBandLevel((short) 1, maxLevel);
                equalizer.setBandLevel((short) 2, maxLevel);
                equalizer.setBandLevel((short) 3, maxLevel);
                equalizer.setBandLevel((short) 4, minLevel);
//                equalizer.setBandLevel((short) 5, maxLevel);
            }
        }
    }

    public void pop() {
        equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        if (bassBoost != null || equalizer != null) {

            equalizer.setEnabled(true);
            bassBoost.release();

            short numPresets = equalizer.getNumberOfPresets();


            short preset = equalizer.getCurrentPreset();
//                equalizer.usePreset((short) (numPresets - 0));
            String name = equalizer.getPresetName(preset);
            System.out.println(name);

            short numberOfBands = equalizer.getNumberOfBands();

            System.out.println("numberofbands" + numberOfBands);
            final short minLevel = equalizer.getBandLevelRange()[0]; // get min range
            final short maxLevel = equalizer.getBandLevelRange()[1]; // get max range

//                short level = equalizer.getBandLevel((short) 5);
            System.out.println("numberofbandsLoop" + numberOfBands);
            System.out.println("loops" + maxLevel);
            for (short i = 0; i < numberOfBands; i++) {

                equalizer.setBandLevel((short) 0, minLevel);
                equalizer.setBandLevel((short) 1, minLevel);
                equalizer.setBandLevel((short) 2, maxLevel);
                equalizer.setBandLevel((short) 3, minLevel);
                equalizer.setBandLevel((short) 4, minLevel);
//                equalizer.setBandLevel((short) 5, maxLevel);
            }

        }
    }

    public void classical() {
        equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        if (bassBoost != null || equalizer != null) {

            equalizer.setEnabled(true);
            bassBoost.release();

            short numPresets = equalizer.getNumberOfPresets();


            short preset = equalizer.getCurrentPreset();
//                equalizer.usePreset((short) (numPresets - 0));
            String name = equalizer.getPresetName(preset);
            System.out.println(name);

            short numberOfBands = equalizer.getNumberOfBands();

            System.out.println("numberofbands" + numberOfBands);
            final short minLevel = equalizer.getBandLevelRange()[0]; // get min range
            final short maxLevel = equalizer.getBandLevelRange()[1]; // get max range

//                short level = equalizer.getBandLevel((short) 5);
            System.out.println("numberofbandsLoop" + numberOfBands);
            System.out.println("loops" + maxLevel);
            for (short i = 0; i < numberOfBands; i++) {

                equalizer.setBandLevel((short) 0, maxLevel);
                equalizer.setBandLevel((short) 1, minLevel);
                equalizer.setBandLevel((short) 2, maxLevel);
                equalizer.setBandLevel((short) 3, maxLevel);
                equalizer.setBandLevel((short) 4, minLevel);
//                equalizer.setBandLevel((short) 5, maxLevel);
            }
        }
    }

    public void rock() {
        equalizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        if (bassBoost != null || equalizer != null) {

            equalizer.setEnabled(true);
            bassBoost.release();
//                rock.setBackgroundResource((R.drawable.bgimage));
            short numPresets = equalizer.getNumberOfPresets();


            short preset = equalizer.getCurrentPreset();
//                equalizer.usePreset((short) (numPresets - 0));
            String name = equalizer.getPresetName(preset);
            System.out.println(name);

            short numberOfBands = equalizer.getNumberOfBands();

            System.out.println("numberofbands" + numberOfBands);
            final short minLevel = equalizer.getBandLevelRange()[0]; // get min range
            final short maxLevel = equalizer.getBandLevelRange()[1]; // get max range

//                short level = equalizer.getBandLevel((short) 5);
            System.out.println("numberofbandsLoop" + numberOfBands);
            System.out.println("loops" + maxLevel);
            for (short i = 0; i < numberOfBands; i++) {

                equalizer.setBandLevel((short) 0, maxLevel);
                equalizer.setBandLevel((short) 1, minLevel);
                equalizer.setBandLevel((short) 2, maxLevel);
                equalizer.setBandLevel((short) 3, minLevel);
                equalizer.setBandLevel((short) 4, maxLevel);
//                equalizer.setBandLevel((short) 5, maxLevel);
            }
        }
    }


}
