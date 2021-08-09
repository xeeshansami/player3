package com.play.view.player.videoplayer4k;


import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenreFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Songs>> {
    ProductAdapterGenre productAdapter;

    RecyclerView recyclerView;
    Cursor cursor;
    String data;
    ArrayList<Songs> list = new ArrayList<Songs>();
    Uri albumArtUri = null;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private boolean auto_scan;
    private String album,track,artist;

    int duration;
    private int message;

    public GenreFragment() {
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
            getActivity().registerReceiver(myReciver, filter);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        register_notification();
        myReciver = new MyBroadcastReceiver();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        auto_scan = sharedPreferences.getBoolean(getResources().getString(R.string.auto_scan_key), true);

        productAdapter = new ProductAdapterGenre(getActivity(), list);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager lm = new GridLayoutManager(getContext(), 2);
        lm.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(productAdapter);

        if (auto_scan)
            if (checkStoragePermission()) {
//                new GetAudioListAsynkTask(getActivity()).execute();
                getLoaderManager().initLoader(3,null,GenreFragment.this);


            } else {
                requestStoragePermission();
            }
        return view;
    }

    private void initLayout() throws IOException {

        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final Uri uriGenre = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;

        final String[] cursor_cols = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION};
        final String where = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        cursor = getActivity().getContentResolver().query(uri,
                cursor_cols, where, null, sortOrder);

//        list.clear();
        while (cursor.moveToNext()) {
            String GenreId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres._ID));
            String GenreNAme = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.TITLE));
            Log.e("NameGenre", GenreNAme);
            Log.e("GenreId", GenreId);
//            Cursor count = getActivity().getContentResolver().query(uriGenre, new String[]{GenreId}, null, null, null);
//


            String artist = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String track = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String data = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            Long albumId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
            int duration = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

//            Log.i("Album art", albumArtUri.toString());
//            Log.i("Duration", milliSecondsToTimer(duration));
//            Log.i("Title", track);
//            Log.i("Artist", artist);
//            Log.i("Album", album);
//            Log.i("Data", data);


            list.add(new Songs(albumArtUri.toString(), track, artist, data, album, milliSecondsToTimer(duration)));

        }

    }

    @Override
    public Loader<ArrayList<Songs>> onCreateLoader(int id, Bundle args) {
        return new LoaderDrone(getContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Songs>> loader, ArrayList<Songs> data) {
        productAdapter.setLv(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Songs>> loader) {

    }


    private static class LoaderDrone extends AsyncTaskLoader<ArrayList<Songs>> {

        private Cursor cursor;

        public LoaderDrone(Context context) {
            super(context);
            onForceLoad();
        }

        @Override
        public ArrayList<Songs> loadInBackground() {
            ArrayList<Songs> result = new ArrayList<Songs>();


            final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            final Uri uriGenre = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;

            final String[] cursor_cols = {MediaStore.Audio.AudioColumns._ID,
                    MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.DURATION};
            final String where = MediaStore.Audio.Media.IS_MUSIC + "!=0";
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

            cursor = getContext().getContentResolver().query(uri,
                    cursor_cols, where, null, sortOrder);



            while (cursor.moveToNext()) {
                String GenreId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres._ID));
                String GenreNAme = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.Members.TITLE));
                Log.e("NameGenre", GenreNAme);
                Log.e("GenreId", GenreId);
//            Cursor count = getActivity().getContentResolver().query(uriGenre, new String[]{GenreId}, null, null, null);
//


                String artist = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                String track = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                Long albumId = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                int duration = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                Uri sArtworkUri = Uri
                        .parse("content://media/external/audio/albumart");
                 Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

//                Log.i("Album art", albumArtUri.toString());
//                Log.i("Duration", milliSecondsToTimer(duration));
//                Log.i("Title", track);
//                Log.i("Artist", artist);
//                Log.i("Album", album);
//                Log.i("Data", data);

                 result.add(new Songs(albumArtUri.toString(), track, artist, data, album, milliSecondsToTimer(duration)));

            }
             return result;
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


    private class GetAudioListAsynkTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;

        public GetAudioListAsynkTask(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog.setCancelable(false);
//            prodialog = progressDialog.show(getActivity(), "", "Loading....", false);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                initLayout();
//                prodialog.dismiss();

                return true;
            } catch (Exception e) {
                return false;

            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
//            prodialog.dismiss();
//            productAdapter = new ProductAdapterGenre(context, list);
            recyclerView.setAdapter(productAdapter);

            productAdapter.notifyDataSetChanged();
        }
    }

    private boolean checkStoragePermission() {
        return ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (auto_scan) {
                    getLoaderManager().restartLoader(3,null,GenreFragment.this);

                }
            } else {
                checkStoragePermission();
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();


        register_notification();
        System.out.println("Activity is Resume !!!");
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getActivity() != null){

            getActivity().unregisterReceiver(myReciver);
        }}

}
