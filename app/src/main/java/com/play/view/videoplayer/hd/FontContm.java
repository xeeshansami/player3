package com.play.view.videoplayer.hd;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Paxees on 10/12/2018.
 */

public class FontContm {
    Context context;
    public FontContm(Context context, EditText editText)
    {
        Typeface amaranth_regular = Typeface.createFromAsset(context.getAssets(), "fonts/calibri.ttf");
        editText.setTypeface(amaranth_regular);
    }
    public FontContm(Context context, Button button)
    {
        Typeface amaranth_regular = Typeface.createFromAsset(context.getAssets(), "fonts/calibri.ttf");
        button.setTypeface(amaranth_regular);
    }
    public FontContm(Context context, TextView textView)
    {
        Typeface amaranth_regular = Typeface.createFromAsset(context.getAssets(), "fonts/calibri.ttf");
        textView.setTypeface(amaranth_regular);
    }
}
