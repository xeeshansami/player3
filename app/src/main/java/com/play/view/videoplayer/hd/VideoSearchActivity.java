package com.play.view.videoplayer.hd;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class VideoSearchActivity extends AppCompatActivity {
    private int video_column_index;
    ListView videolist;
    int count;
    static String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID};
    Cursor videoCursor;
    ListView recyclerView;
    CustomListAdapter videoSongsAdapter;
    ArrayList<VideoSongs>  videoSongsList  = new ArrayList<VideoSongs>(); ;
    private String filename,title,id,albu,artist;
    MediaMetadataRetriever retriever;
    EditText searchText;
    ProgressDialog pd;
    public static AdView mAdView;
    private Timer timer;
    private AdRequest adRequest;
    private ProgressBar progressBar;
    Toolbar toolbar;

    private void showProgressbar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_search);


        toolbar = (Toolbar) findViewById(R.id.videoSearchtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Videos");



        pd = new ProgressDialog(this);
        pd.setMessage("Searching...");
        pd.setCancelable(false);

        recyclerView = (ListView) findViewById(R.id.videoSearchrecycler);

        searchText=(EditText) findViewById(R.id.account_jid);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        showProgressbar(false);

        mAdView = (AdView) findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
                //.addTestDevice("190af34e322acedf")
                .build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {






                if(isOnline()) {
                    mAdView.setVisibility(View.VISIBLE);

                }


            }
            // Implement AdListener
        });

        recyclerView.setNestedScrollingEnabled(false);
        loadDataAll();
       // recyclerView.setHasFixedSize(true);

       // recyclerView.setLayoutManager(lm);
//        recyclerView.setAdapter(videoSongsAdapter);
//        recyclerView.addOnItemTouchListener(
//                new VideoActivity.RecyclerItemClickListener(this, recyclerView,
//                        new  VideoActivity.RecyclerItemClickListener.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                // do whatever
//                                Log.i("", "OnItemClick");
//                                System.gc();
//                                video_column_index = videoCursor
//                                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
//                                videoCursor.moveToPosition(position);
//                                filename = videoCursor.getString(video_column_index);
//                                title = videoCursor.getString(
//                                        videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
//                                id  = String.valueOf(position);
//                                artist =  videoCursor.getString(
//                                        videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
//                                Intent intent = new Intent(VideoSearchActivity.this, VideoDetailActivity.class);
//                                intent.putExtra("videofilename", filename);
//                                intent.putExtra("title",title);
//                                intent.putExtra("id",id);
////                                intent.putExtra("artist",artist);
//                                System.out.println("Song id " +id);
//                                startActivity(intent);
//
//                            }
//
//                            @Override
//                            public void onLongItemClick(View view, int position) {
//                                // do whatever
//                            }
//                        })
//        );


//        init_phone_video_grid();


    }





    private void loadDataAll()
    {



        Map<String, String> snapshot=null;
        synchronized (AppClass.mVideoPathCache) {

            snapshot   = AppClass.mVideoPathCache.snapshot();
        }





        for (String id : snapshot.keySet()) {
            Object myObject = AppClass.mVideoPathCache.get(id);

            VideoSongs songs = new VideoSongs();
            songs.setData(id);
            //  songs.setImage(albumArtUri.toString());
//            songs.setDuration(milliSecondsToTimer(getVideoDuration(id)));
            songs.setName(id.substring(id.lastIndexOf("/")+1,id.length()-4));
            // songs.setArtist(artist);
            songs.setSize(getFileSize(new File(id).length()));
            videoSongsList.add(songs);

        }


        videoSongsAdapter = new CustomListAdapter(this, videoSongsList,"");

        recyclerView.setAdapter(videoSongsAdapter);
        // recyclerViewFiles.getRecycledViewPool().clear();
        videoSongsAdapter.notifyDataSetChanged();






        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(final CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text



                if (timer != null) {
                    timer.cancel();
                }





               // new BigComputationTask4(cs).execute();




                //videoSongsAdapter.notifyDataSetChanged();




            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(final Editable arg0) {

                timer = new Timer();
                timer.schedule(new TimerTask()
                {
                    @Override
                    public void run() {
                        // do your actual work here
                        VideoSearchActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgressbar(true);

                                videoSongsAdapter.getFilter().filter(arg0.toString());
                                //   resultText.setVisibility(View.INVISIBLE);
                            }
                        });

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        VideoSearchActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                showProgressbar(false);




                                videoSongsAdapter = new CustomListAdapter(getApplicationContext(), CustomListAdapter.mFilteredList,"search");

                                recyclerView.setAdapter(videoSongsAdapter);


                            }
                        });

                        // hide keyboard as well?
                        // InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        // in.hideSoftInputFromWindow(searchText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }, 600); // 600ms



            }
        });









    }



    /*

    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable arg0) {
            // user typed: start the timer
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // do your actual work here
                    VideoSearchActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgressbar(true);
                            resultText.setVisibility(View.INVISIBLE);
                        }
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    VideoSearchActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            showProgressbar(false);

                            videoSongsAdapter.getFilter().filter(cs);

                            videoSongsAdapter = new CustomListAdapter(getApplicationContext(), CustomListAdapter.mFilteredList,"search");

                            recyclerView.setAdapter(videoSongsAdapter);

                        }
                    });

                    // hide keyboard as well?
                    // InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    // in.hideSoftInputFromWindow(searchText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }, 600); // 600ms delay before the timer executes the "run" method from TimerTask
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // nothing to do here
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // user is typing: reset already started timer (if existing)
            if (timer != null) {
                timer.cancel();
            }
        }
    };

*/

    public long  getVideoDuration(String path)
    {

        retriever = new MediaMetadataRetriever();

//use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(getApplicationContext(), Uri.fromFile(new File(path)));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        long timeInMillisec = Long.parseLong(time );

        retriever.release();

        return timeInMillisec;


        /*

        MediaPlayer mp = MediaPlayer.create(context, Uri.parse(path));
        int duration = mp.getDuration();
        mp.release();

        System.out.println("duration of view "+duration);

        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );


*/


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

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

   /* public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
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
        }   else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

// return timer string
        return finalTimerString;
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private   OnItemClickListener mListener;

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        // Associate searchable configuration with the SearchView
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

        }

        return super.onOptionsItemSelected(item);
    }



    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.audio, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }



    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {




                        videoSongsAdapter.getFilter().filter(newText);
                      //  videoSongsAdapter.notifyDataSetChanged();




                return true;
            }
        });
    }
    */



    private class BigComputationTask4 extends AsyncTask<Void, Void, Void> {


        CharSequence cs;
        public BigComputationTask4(CharSequence cs)
        {
            this.cs=cs;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            // Runs on the background thread
            //doBigComputation();
















            return null;
        }

        @Override
        protected void onPostExecute(Void res) {



        }

    }


}
