package com.play.view.videoplayer.hd.equalizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.play.view.videoplayer.hd.Songs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by DuskySolution on 12/11/2017.
 */

public class SessionStorage {

    private final String STORAGE = " com.example.azhar.playerapp.STORAGE";
    private SharedPreferences preferences;


    private Context context;






    public SessionStorage(Context context) {
        this.context = context;

    }


    public void storeAudio(int sessionValue) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(sessionValue);
        editor.putString("audioArrayList", json);
        editor.apply();
    }

    public int loadAudio() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("audioArrayList", null);
        Type type = new TypeToken<ArrayList<Songs>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void storeSession(int index) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("audioIndex", index);
        editor.apply();
    }

    public int loadSession() {
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