package com.play.view.videoplayer.hd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.play.view.videoplayer.hd.Model.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DuskySolution on 12/11/2017.
 */

public class StorageUtil {

    private final String STORAGE = " com.example.azhar.playerapp.STORAGE";
    private SharedPreferences preferences;


    private Context context;






    public StorageUtil(Context context) {
        this.context = context;

    }

    /*

    public static LruCache<String, String> getVideoCache() {

        if (AppClass.mVideoPathCache == null)

        {

            AppClass.mVideoPathCache = new LruCache<String, String>(AppClass.cacheSize);
        }

        return AppClass.mVideoPathCache;
    }

    public static void putVideoCatche(String key,String value)
    {

        if (AppClass.mVideoPathCache == null)

        {

            AppClass.mVideoPathCache = new LruCache<String, String>(AppClass.cacheSize);
        }


        AppClass.mVideoPathCache.put(key,value);

    }
    */

    public void storeAudio(ArrayList<Songs> arrayList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("audioArrayList", json);
        editor.apply();
    }

    public List<Video> loadAudio() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("audioArrayList", null);
        Type type = new TypeToken<ArrayList<Songs>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void storeAudioIndex(int index) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("audioIndex", index);
        editor.apply();
    }

    public int loadAudioIndex() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getInt("audioIndex", -1);//return -1 if no data found
    }

    public void clearCachedAudioPlaylist() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}