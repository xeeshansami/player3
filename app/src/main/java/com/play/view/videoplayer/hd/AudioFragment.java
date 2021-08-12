package com.play.view.videoplayer.hd;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

//import com.sothree.slidinguppanel.SlidingUpPanelLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class AudioFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Songs>> {

     ProductAdapter productAdapter;
    RecyclerView recyclerView;
    public static Cursor audiocursor;
    String data;
    ArrayList<Songs> list = new ArrayList<Songs>();
      public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
       private String filename, title, id, albu, artist;
    private long albumId;
    ProgressDialog progressDialog;
    private Handler handler, handlerLoader;
    ProgressDialog prodialog;
    private boolean auto_scan;
    private String actions;
    private AdView mAdView;
    Toolbar toolbar;
    private AdRequest adRequest, ar;
    private int selectedPosition = -1;
    Dialog dialog;
    ProgressBar loadingIndicator;
    Dialog dialog1;
    private int message;
    Button home;
    private InterstitialAd mInterstitial;
    private int position;
    Boolean adcheck;

    public AudioFragment() {
        // Required empty public constructor
    }
    private MyBroadcastReceiver myReciver;
    public static String TAG_FrAG = "com.example.azhar.broadcast.MEDIA_ACTIONS";
//
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("Action: " + intent.getAction() + "\n");
//            sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
//            String log = sb.toString();
//            Log.d(TAG, log);
//            Toast.makeText(context, log, Toast.LENGTH_LONG).show();
//
//            final Intent intentReceiver = getIntent();
//            Bundle extras = intentReceiver.getExtras();
//            String actions =  extras.getString("action");
//            Log.i(",", actions);

             message = intent.getIntExtra("action",0);
            Log.d("receiver", "Got message: " + message);
            if(productAdapter != null){
                productAdapter.notifyDataSetChanged();

            }
//            if (intent.getAction().equalsIgnoreCase(TAG)) {
//                actions = intent.getExtras().getString("action");
//                if (actions.equals(ACTION_NEXT)) {
//
//
//                }
//                Log.d("","Action Play from service :" + intent.getString("action"));

//                Log.d("INFO", intent.getExtras().getString("action"));
//                finish(); // do here whatever you want
//            }

        }
    }

    public void register_notification() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("");
        if(getActivity() != null){
            getContext().registerReceiver(myReciver, filter);

        }
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        setHasOptionsMenu(true);
        adcheck = true;
        dialog1 = new Dialog(getContext());
        // dialog.setTitle("               Please Wait");
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView( getLayoutInflater().inflate( R.layout.custom, null ) );
        MobileAds.initialize(getContext(), getResources().getString(R.string.banner_ad_unit_id));
        loadingIndicator = (ProgressBar) view.findViewById(R.id.loading_indicator);
        mAdView = (AdView) view.findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
////                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                .build();
        mAdView.loadAd(adRequest);






        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView( getLayoutInflater().inflate( R.layout.custom2, null ) );
        dialog.show();
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                {
                    ads();
                }
            }
        }, 1000);


        register_notification();
        myReciver = new MyBroadcastReceiver();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        auto_scan = sharedPreferences.getBoolean(getResources().getString(R.string.auto_scan_key), true);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(false);
        productAdapter = new ProductAdapter(getActivity(), list);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        home=view.findViewById(R.id.home);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(productAdapter);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.hme8);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Music");




        if (auto_scan)
            if(checkStoragePermission()) {
                getLoaderManager().initLoader(1,null,AudioFragment.this);

            }else {
                requestStoragePermission();
            }


        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss();
                timer2.cancel(); //this will cancel the timer of the system
            }
        }, 1000); // the timer will count 5 seconds....





        // dialog.dismiss();
        return view;

    }
    public boolean isOnline() {

        if (getActivity() != null) {
// Code goes here.
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

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


        }
        return false;
    }
    public void ads() {
        if (isOnline()) {
            if (getActivity().isFinishing()){
                dialog1.show();
            }

            mInterstitial = null;


            Display display = ((WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE)).getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            Log.v("width", width + "");
            dialog.getWindow().setLayout((6 * width) / 8, (4 * height) / 15);

            AdRequest adRequestInter = new AdRequest.Builder().build();
            mInterstitial = new InterstitialAd(getActivity());
            mInterstitial.setAdUnitId(getResources().getString(R.string.intersitial_as_unit_id));

            mInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (mInterstitial.isLoaded()) {
                        if (mInterstitial != null) {
                            loadingIndicator.setVisibility(View.GONE);
                            dialog1.dismiss();
                            //loadingIndicator.draw();
                        {

                            {
                                mInterstitial.show();
                            }
                            }
                        }
                    }
                }
            });
            mInterstitial.loadAd(adRequestInter);
        }
    }




//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.setting, menu);
//        super.onCreateOptionsMenu(menu,inflater);
//    }
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int itemId = item.getItemId();
//
//        if (itemId == android.R.id.home) {
//            Intent intent = new Intent(getActivity(), MainActivity_Front.class);
//            startActivity(intent);
//        }
//        else if (itemId ==  R.id.setting) {
//
//            Intent i = new Intent(getContext(),SettingActivity.class);
//            startActivity(i);
//
//        } else if (itemId == R.id.refresh) {
//            Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_SHORT).show();
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }






    private static class LoaderDrone extends AsyncTaskLoader<ArrayList<Songs>> {

        public LoaderDrone(Context context) {
            super(context);
            onForceLoad();
        }

        @Override
        public ArrayList<Songs> loadInBackground() {
            ArrayList<Songs> results = new ArrayList<Songs>();
            final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

            final String[] cursor_cols = {MediaStore.Audio.AudioColumns._ID,
                    MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.SIZE,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.DURATION};
            final String where = MediaStore.Audio.Media.IS_MUSIC + "!=0";
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

            audiocursor = getContext().getContentResolver().query(uri,
                    cursor_cols, where, null, sortOrder);
            int count = audiocursor.getCount();
            System.out.println("Total songs " + count);

//        list.clear();
            while (audiocursor.moveToNext()) {

                String s = audiocursor.getString(audiocursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                System.out.println("Sizes" + s);
                String artist = audiocursor.getString(audiocursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = audiocursor.getString(audiocursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String title = audiocursor.getString(audiocursor
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

                results.add(new Songs(albumArtUri.toString(), title, artist, data, album, milliSecondsToTimer(duration)));


            }


            return results;
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

    }
    private void initLayout() throws IOException {
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        final String[] cursor_cols = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION};
        final String where = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        audiocursor = getActivity().getContentResolver().query(uri,
                cursor_cols, where, null, sortOrder);
        int count = audiocursor.getCount();
        System.out.println("Total songs " + count);

//        list.clear();
        while (audiocursor.moveToNext()) {

            String s = audiocursor.getString(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            System.out.println("Sizes" + s);
            artist = audiocursor.getString(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = audiocursor.getString(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            title = audiocursor.getString(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            data = audiocursor.getString(audiocursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            albumId = audiocursor.getLong(audiocursor
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

            list.add(new Songs(albumArtUri.toString(), title, artist, data, album, milliSecondsToTimer(duration)));


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

    @Override
    public Loader<ArrayList<Songs>> onCreateLoader(int id, Bundle args) {
        return new LoaderDrone(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Songs>> loader, ArrayList<Songs> data) {

        productAdapter.setLv(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Songs>> loader) {

    }


    private class GetAudioListAsynkTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;

        @Override
        protected void onPreExecute() {

            progressDialog.setCancelable(false);
            prodialog = progressDialog.show(getActivity(), "", "Loading....", false);

        }

        public GetAudioListAsynkTask(Context context) {

            this.context = context;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            try {

                initLayout();


                if (prodialog.isShowing()) {
                    prodialog.dismiss();
                }
                return true;
            } catch (Exception e) {
                return false;

            }

        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (prodialog.isShowing()) {
                prodialog.dismiss();
            }

            recyclerView.setAdapter(productAdapter);

            productAdapter.notifyDataSetChanged();
        }
    }




    private boolean checkStoragePermission() {
        return ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (auto_scan) {
                    getLoaderManager().restartLoader(1,null,AudioFragment.this);

                }
            } else {
                checkStoragePermission();
            }
        }
    }




    @Override
    public void onPause() {
        super.onPause();
        adcheck = false;
        System.out.println("Activity is Pause !!!");


    }

    @Override
    public void onStop() {
        super.onStop();
        adcheck = false;
        System.out.println("Activity is Stop !!!");


    }


    @Override
    public void onResume() {
        super.onResume();
        adcheck = false;

        register_notification();
        System.out.println("Activity is Resume !!!");
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        adcheck = false;
        super.onDestroy();
        if(getActivity() != null){

            getActivity().unregisterReceiver(myReciver);
        }}


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

}


