package com.play.view.player.videoplayer4k;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ishop on 16/05/2018.
 */

public class AppClass extends Application {

    public static  int height=0;
    private static AppClass instance;
    Handler handler;
    public static int callLogId;

  //  public static int firsttime=0;

    static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    static final int cacheSize = maxMemory / 4;
    public static Map<String,String> dirList = new HashMap<String, String>();
    public static List<FolderEntity> folder=new ArrayList<FolderEntity>();

    public static List<String> folder2=new ArrayList<String>();

    //DiskLruCache
    public static  LruCache<String, String> mVideoPathCache= new LruCache<String, String>(cacheSize);

    public static ArrayList<String> subDirs = new ArrayList<String>();

    public static int subDirsSize = 0;

    @Override
    public void onCreate() {
        super.onCreate();

    //    MultiDex.install(this);


        handler = new Handler();
        instance=this;


    }


    public static int getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        height= metrics.heightPixels;
        ////System.out.println("width orginal "+width);

        return   width ;
    }
    public static synchronized AppClass getInstance() {
        return instance;
    }


    public void runOnUiThread(final Runnable runnable) {
        handler.post(runnable);
    }

    /**
     * Submits request to be executed in UI thread.
     */
    public void runOnUiThreadDelay(final Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }

}
