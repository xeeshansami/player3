package com.play.view.videoplayer.hd.CursorUtils;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.play.view.videoplayer.hd.FontContm;
import com.play.view.videoplayer.hd.Model.Video;
import com.play.view.videoplayer.hd.R;
import com.play.view.videoplayer.hd.SettingActivity;
import com.play.view.videoplayer.hd.VideoFolder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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


public class SelectedFolderVideoActivity extends AppCompatActivity {
    boolean listGird = true;
    int sortVariable = 0;
    RecyclerView recyclerViewVideos;
    ArrayList<Video> videoActivitySongsList;
    VideosListAdapterForList videosListAdapterForList;
    VideosListAdapterForGrid videosListAdapterForGrid;
    Toolbar toolbar;
    TextView pathTextView;
    List<Video> videos;
    public static AdView mAdView, mAdView2;
    private AdRequest adRequest, adRequest2;
    ProgressDialog progressDialog;
    int pos;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectedfoldervideos_layout);
        try {
            progressDialog = new ProgressDialog(SelectedFolderVideoActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white));
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //What to do on back clicked
                    startActivity(new Intent(SelectedFolderVideoActivity.this, VideoFolder.class));
                    finish();
                }
            });
            MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
            mAdView = (AdView) findViewById(R.id.adView_banner);
//            mAdView2 = (AdView) findViewById(R.id.adView_banner2);
//            adRequest2=new AdRequest.Builder().build();
            adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
//            mAdView2.loadAd(adRequest2);

            mAdView.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    mAdView.setVisibility(View.VISIBLE);
                    Log.i("adLoaded", "yes");
                }
                // Implement AdListener
            });

//            mAdView2.setVisibility(View.VISIBLE);
            pathTextView = (TextView) toolbar.findViewById(R.id.path);
            new FontContm(this, pathTextView);
            recyclerViewVideos = findViewById(R.id.videoRecycler);
//            final LinearLayoutManager filesLm = new LinearLayoutManager(this);
//            filesLm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewVideos.setLayoutManager(new GridLayoutManager(this, 3));
//            recyclerViewVideos.setLayoutManager(filesLm);
            videoActivitySongsList = new ArrayList<Video>();
            Bundle bundle = getIntent().getExtras();
            pos = bundle.getInt("position");
            final String header = bundle.getString("header");
            loadvideos(pos, "name", 1);
//            GetAllVideosDataAndFolder getAllVideoFromSelectedFolder = new GetAllVideosDataAndFolder(pos);
//            getAllVideoFromSelectedFolder.execute();
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

    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadvideos(int posi, String header, int spanValue) {
        try {
            videos = new ArrayList<>();
            videos.clear();
            if (spanValue == 1) {
                sortVariable = spanValue;
                if (header.equalsIgnoreCase("date")) {
                    recyclerViewVideos.setLayoutManager(new GridLayoutManager(this, 1));
                    this.videos = new VideosAndFoldersUtility(SelectedFolderVideoActivity.this).fetchVideosByFolder(VideoFolder.folders.get(posi).getPath());
                    sortListByDate(this.videos);
                    this.videosListAdapterForList = new VideosListAdapterForList(SelectedFolderVideoActivity.this, this.videos, posi);
                    Log.i("sizeCheck", VideoFolder.folders.get(posi).getName() + "");
                    pathTextView.setText("" + VideoFolder.folders.get(posi).getName());
                    recyclerViewVideos.setAdapter(videosListAdapterForList);
                    this.videosListAdapterForList.notifyDataSetChanged();
                } else if (header.equalsIgnoreCase("size")) {
                    recyclerViewVideos.setLayoutManager(new GridLayoutManager(this, 1));
                    this.videos = new VideosAndFoldersUtility(SelectedFolderVideoActivity.this).fetchVideosByFolder(VideoFolder.folders.get(posi).getPath());
                    sortBySize(this.videos);
                    this.videosListAdapterForList = new VideosListAdapterForList(SelectedFolderVideoActivity.this, this.videos, posi);
                    Log.i("sizeCheck", VideoFolder.folders.get(posi).getName() + "");
                    pathTextView.setText("" + VideoFolder.folders.get(posi).getName());
                    recyclerViewVideos.setAdapter(videosListAdapterForList);
                    this.videosListAdapterForList.notifyDataSetChanged();
                } else {
                    recyclerViewVideos.setLayoutManager(new GridLayoutManager(this, 1));
                    this.videos = new VideosAndFoldersUtility(SelectedFolderVideoActivity.this).fetchVideosByFolder(VideoFolder.folders.get(posi).getPath());
                    sortListByName(this.videos);
                    this.videosListAdapterForList = new VideosListAdapterForList(SelectedFolderVideoActivity.this, this.videos, posi);
                    Log.i("sizeCheck", VideoFolder.folders.get(posi).getName() + "");
                    pathTextView.setText("" + VideoFolder.folders.get(posi).getName());
                    recyclerViewVideos.setAdapter(videosListAdapterForList);
                    this.videosListAdapterForList.notifyDataSetChanged();
                }
            }
            else if (spanValue == 3) {
                sortVariable = spanValue;
                if (header.equalsIgnoreCase("date")) {
                    recyclerViewVideos.setLayoutManager(new GridLayoutManager(this, 3));
                    this.videos = new VideosAndFoldersUtility(SelectedFolderVideoActivity.this).fetchVideosByFolder(VideoFolder.folders.get(posi).getPath());
                    sortListByDate(this.videos);
                    this.videosListAdapterForGrid = new VideosListAdapterForGrid(SelectedFolderVideoActivity.this, this.videos, posi);
                    Log.i("sizeCheck", VideoFolder.folders.get(posi).getName() + "");
                    pathTextView.setText("" + VideoFolder.folders.get(posi).getName());
                    recyclerViewVideos.setAdapter(videosListAdapterForGrid);
                    this.videosListAdapterForGrid.notifyDataSetChanged();
                } else if (header.equalsIgnoreCase("size")) {
                    recyclerViewVideos.setLayoutManager(new GridLayoutManager(this, 3));
                    this.videos = new VideosAndFoldersUtility(SelectedFolderVideoActivity.this).fetchVideosByFolder(VideoFolder.folders.get(posi).getPath());
                    sortBySize(this.videos);
                    this.videosListAdapterForGrid = new VideosListAdapterForGrid(SelectedFolderVideoActivity.this, this.videos, posi);
                    Log.i("sizeCheck", VideoFolder.folders.get(posi).getName() + "");
                    pathTextView.setText("" + VideoFolder.folders.get(posi).getName());
                    recyclerViewVideos.setAdapter(videosListAdapterForGrid);
                    this.videosListAdapterForGrid.notifyDataSetChanged();
                } else {
                    recyclerViewVideos.setLayoutManager(new GridLayoutManager(this, 3));
                    this.videos = new VideosAndFoldersUtility(SelectedFolderVideoActivity.this).fetchVideosByFolder(VideoFolder.folders.get(posi).getPath());
                    sortListByName(this.videos);
                    this.videosListAdapterForGrid = new VideosListAdapterForGrid(SelectedFolderVideoActivity.this, this.videos, posi);
                    Log.i("sizeCheck", VideoFolder.folders.get(posi).getName() + "");
                    pathTextView.setText("" + VideoFolder.folders.get(posi).getName());
                    recyclerViewVideos.setAdapter(videosListAdapterForGrid);
                    this.videosListAdapterForGrid.notifyDataSetChanged();
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
                loadvideos(pos, "", 3);
                item.setIcon(R.drawable.ic_list_white);
            } else {
                listGird = true;
                item.setIcon(R.drawable.ic_grid_white);
                loadvideos(pos, "", 1);
            }
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        } else if (id == R.id.action_name) {
            videos.clear();
            if (sortVariable == 3) {
                loadvideos(pos, "", 3);
            } else {
                loadvideos(pos, "name", 1);
            }
            item.setChecked(true);
            return true;
        } else if (id == R.id.action_size) {
            videos.clear();
            if (sortVariable == 3) {
                loadvideos(pos, "", 3);
            } else {
                loadvideos(pos, "size", 1);
            }
            item.setChecked(true);
            return true;
        } else if (id == R.id.action_date) {
            videos.clear();
            if (sortVariable == 3) {
                loadvideos(pos, "date", 3);
            } else {
                loadvideos(pos, "", 1);
            }
            item.setChecked(true);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    public void sortByName() {
        Collections.sort(this.videos, new compareVideos());
    }

    public void sortBySize(List<Video> videos) {
        Comparator<Video> stringLengthComparator = new Comparator<Video>() {
            @Override
            public int compare(Video o1, Video o2) {
                return Long.compare(new File(o1.getData()).length(), new File(o2.getData()).length());
            }
        };
        Collections.sort(videos, stringLengthComparator);
    }

    class compareVideos implements Comparator<Video> {
        compareVideos() {
        }

        public int compare(Video folder1, Video folder2) {
            return folder1.getName().compareTo(folder2.getName());
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
