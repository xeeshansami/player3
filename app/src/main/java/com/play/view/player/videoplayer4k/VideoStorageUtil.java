package com.play.view.player.videoplayer4k;

import android.content.Context;
import android.content.SharedPreferences;

import com.play.view.player.videoplayer4k.Model.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DuskySolution on 12/11/2017.
 */


public class VideoStorageUtil {

    private final String STORAGE = " com.view.android.playerapp.STORAGE";
    private SharedPreferences preferences;
    private Context context;

    public VideoStorageUtil(Context context) {
        this.context = context;
    }

    public void storeVideo(List<Video> arrayList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.remove("videoArrayList").commit();
        editor.putString("videoArrayList", json);
        editor.apply();
    }

    public  List<Video> loadVideo() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("videoArrayList", null);
        Type type = new TypeToken<ArrayList<Video>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void storeVideoIndex(int index) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("videoIndex").commit();
        editor.putInt("videoIndex", index);
        editor.apply();
    }
    public void storeFolderIndex(int index) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("folderIndex").commit();
        editor.putInt("folderIndex", index);
        editor.apply();
    }


    public int loadVideoIndex() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getInt("videoIndex", -1);//return -1 if no data found
    }
    public int loadFolderIndex() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getInt("folderIndex", -1);//return -1 if no data found
    }


    public void clearCachedAudioPlaylist() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}