package com.play.view.player.videoplayer4k.CursorUtils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {
    public static final String DPI = "dpi";
    public static final String PIXEL = "pixel";

    public static float getDeviceWidth(Context context, String unit) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        Object obj = -1;
        switch (unit.hashCode()) {
            case 99677:
                if (unit.equals(DPI)) {
                    obj = 1;
                    break;
                }
                break;
            case 106680966:
                if (unit.equals(PIXEL)) {
                    obj = null;
                    break;
                }
                break;
        }
        if (obj == null)
            return (float) mDisplayMetrics.widthPixels;
        else if ((int)obj == 1)
            return mDisplayMetrics.xdpi;
        else
            return -1.0f;


    }

    public static float getDeviceHeight(Context context, String unit) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        Object obj = -1;
        switch (unit.hashCode()) {
            case 99677:
                if (unit.equals(DPI)) {
                    obj = 1;
                    break;
                }
                break;
            case 106680966:
                if (unit.equals(PIXEL)) {
                    obj = null;
                    break;
                }
                break;
        }
        if (obj == null)
            return (float) mDisplayMetrics.heightPixels;
        else if ((int)obj== 1)
            return mDisplayMetrics.ydpi;
        else
            return -1.0f;
    }
}
