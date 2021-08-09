package com.play.view.player.videoplayer4k;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.util.Size;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.play.view.player.videoplayer4k.CursorUtils.SelectedFolderVideoActivity;
import com.play.view.player.videoplayer4k.CursorUtils.VideosAndFoldersUtility;
import com.play.view.player.videoplayer4k.CursorUtils.VideosListAdapterForGrid;
import com.play.view.player.videoplayer4k.CursorUtils.VideosListAdapterForList;
import com.play.view.player.videoplayer4k.Model.Video;

import com.play.view.player.videoplayer4k.equalizer.CustomFragment2;
import com.play.view.player.videoplayer4k.equalizer.SessionStorage;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class AllVideosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int video_column_index;
    ListView videolist;
    boolean listGird = true;
    int sortVariable = 0;
    AlertDialog alertDialog;
    CustomFragment2 equalizerFragment;
    int count;
    static String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID};
    Cursor videoCursor;
    RecyclerView recyclerView;
    VideosListAdapterForList videoSongsAdapter;
    VideosListAdapterForGrid videoSongsAdapterGrid;
    ArrayList<VideoSongs> videoSongsList;
    DrawerLayout drawer;
    private String filename, title, id, albu, artist;
    MediaMetadataRetriever retriever;
    EditText searchText;
    ProgressDialog pd;
    public static AdView mAdView;
    private Timer timer;
    private AdRequest adRequest;
    private ProgressBar progressBar;
    Toolbar toolbar;
    TextView pathTextView;
    ProgressDialog progressDialog;
    public FrameLayout equiliazerScreen;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_videos_activity);
        equiliazerScreen = (FrameLayout) findViewById(R.id.root_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pathTextView = (TextView) toolbar.findViewById(R.id.path);
        new FontContm(this, pathTextView);
        recyclerView = findViewById(R.id.videoSearchrecycler);
        loadDataAll("size", 1);
        mAdView = (AdView) findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
                .build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
                Log.i("adLoaded", "yes");
            }
            // Implement AdListener
        });
        recyclerView.setNestedScrollingEnabled(false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            sessionStorage.storeSession(4456);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_view, equalizerFragment)
                    .commit();
        } else if (id == R.id.nav_folders) {
            startActivity(new Intent(this, VideoFolder.class));
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_video) {
            startActivity(new Intent(this, AllVideosActivity.class));
            finish();
        }
        if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String title = "Share XM Player with Freinds";
            String link = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
            intent.putExtra(Intent.EXTRA_SUBJECT, title);
            intent.putExtra(Intent.EXTRA_TEXT, link);
            startActivity(Intent.createChooser(intent, "Share XM player using"));
        } else if (id == R.id.nav_rate) {
            network_stream();
            return true;
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, About.class));
            return true;
        }


        return true;
    }

    void network_stream() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AllVideosActivity.this);
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
                alertDialog.dismiss();
                finishAffinity();
            }
        });


        alertDialog.show();

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, VideoFolder.class));
        finish();
    }

    private void loadDataAll(String sorter, int value) {
        try {
            VideosAndFoldersUtility utility = new VideosAndFoldersUtility(this);
            VideoFolder.videos = utility.fetchAllVideos();
            if (value == 1) {
                sortVariable = value;
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                if (sorter.equalsIgnoreCase("date")) {
                    sortListByDate(VideoFolder.videos);
                    videoSongsAdapter = new VideosListAdapterForList(AllVideosActivity.this, VideoFolder.videos, -2);
                    recyclerView.setAdapter(videoSongsAdapter);
                    videoSongsAdapter.notifyDataSetChanged();
                } else if (sorter.equalsIgnoreCase("size")) {
                    sortBySize();
                    videoSongsAdapter = new VideosListAdapterForList(AllVideosActivity.this, VideoFolder.videos, -2);
                    recyclerView.setAdapter(videoSongsAdapter);
                    videoSongsAdapter.notifyDataSetChanged();
                } else {
                    sortListByName(VideoFolder.videos);
                    videoSongsAdapter = new VideosListAdapterForList(AllVideosActivity.this, VideoFolder.videos, -2);
                    recyclerView.setAdapter(videoSongsAdapter);
                    videoSongsAdapter.notifyDataSetChanged();
                }
            }
            if (value == 3) {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                sortVariable = value;
                if (sorter.equalsIgnoreCase("date")) {
                    sortListByDate(VideoFolder.videos);
                    videoSongsAdapterGrid = new VideosListAdapterForGrid(AllVideosActivity.this, VideoFolder.videos, -2);
                    recyclerView.setAdapter(videoSongsAdapterGrid);
                    videoSongsAdapterGrid.notifyDataSetChanged();
                } else if (sorter.equalsIgnoreCase("size")) {
                    sortBySize();
                    videoSongsAdapterGrid = new VideosListAdapterForGrid(AllVideosActivity.this, VideoFolder.videos, -2);
                    recyclerView.setAdapter(videoSongsAdapterGrid);
                    videoSongsAdapterGrid.notifyDataSetChanged();
                } else {
                    sortListByName(VideoFolder.videos);
                    videoSongsAdapterGrid = new VideosListAdapterForGrid(AllVideosActivity.this, VideoFolder.videos, -2);
                    recyclerView.setAdapter(videoSongsAdapterGrid);
                    videoSongsAdapterGrid.notifyDataSetChanged();
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

    public long getVideoDuration(String path) {

        retriever = new MediaMetadataRetriever();

        retriever.setDataSource(getApplicationContext(), Uri.fromFile(new File(path)));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        long timeInMillisec = Long.parseLong(time);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer2, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.ic_frame) {
            if (listGird) {
                listGird = false;
                loadDataAll("", 3);
                item.setIcon(R.drawable.ic_list_white);
            } else {
                listGird = true;
                item.setIcon(R.drawable.ic_grid_white);
                loadDataAll("", 1);
            }
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        } else if (id == R.id.action_name) {
            if (sortVariable == 3) {
                loadDataAll("name", 3);
            } else {
                loadDataAll("name", 1);
            }
            item.setChecked(true);
            return true;
        } else if (id == R.id.action_size) {
            if (sortVariable == 3) {
                loadDataAll("size", 3);
            } else {
                loadDataAll("size", 1);
            }
            item.setChecked(true);
            return true;
        } else if (id == R.id.action_date) {
            if (sortVariable == 3) {
                loadDataAll("date", 3);
            } else {
                loadDataAll("date", 1);
            }
            item.setChecked(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void sortByName() {
//        Collections.sort(VideoFolder.videos, new compareVideos());
    }

    public void sortBySize() {
        Comparator<Video> stringLengthComparator = new Comparator<Video>() {
            @Override
            public int compare(Video o1, Video o2) {
                return Long.compare(new File(o1.getData()).length(), new File(o2.getData()).length());
            }
        };
        Collections.sort(VideoFolder.videos, stringLengthComparator);
    }


    class compareVideos implements Comparator<Long> {
        compareVideos() {
        }

        public int compare(Long folder1, Long folder2) {
            return folder1 > (folder2) ? 0 : 1;
        }
    }
    /*]=====Sorting=====[*/

    private void sortListByName(List<Video> theArrayListEvents) {
        Collections.sort(theArrayListEvents, new EventDetailSortByName());
    }

    private class EventDetailSortByName implements java.util.Comparator<Video> {
        @Override
        public int compare(Video customerEvents1, Video customerEvents2) {
            String name1, name2;
            name1 = customerEvents1.getName().toLowerCase().trim();
            name2 = customerEvents2.getName().toLowerCase().trim();
            return name1.compareTo(name2);
        }
    }

    private void sortListByDate(List<Video> theArrayListEvents) {
        Collections.sort(theArrayListEvents, new EventDetailSortByDate());
    }

    private class EventDetailSortByDate implements java.util.Comparator<Video> {
        @Override
        public int compare(Video customerEvents1, Video customerEvents2) {
            Date DateObject1 = StringToDate(customerEvents1.getDateAdded());
            Date DateObject2 = StringToDate(customerEvents2.getDateAdded());

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

}
