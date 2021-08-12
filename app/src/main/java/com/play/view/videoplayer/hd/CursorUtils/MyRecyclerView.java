package com.play.view.videoplayer.hd.CursorUtils;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

public class MyRecyclerView extends RecyclerView {
    Context context;

    public MyRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean fling(int velocityX, int velocityY) {
        return super.fling(velocityX, (int) (((double) velocityY) * 0.9d));
    }
}