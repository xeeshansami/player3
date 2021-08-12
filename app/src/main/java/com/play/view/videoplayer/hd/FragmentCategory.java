package com.play.view.videoplayer.hd;


import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * Created by azhar on 5/9/2017.
 */

public class FragmentCategory extends FragmentPagerAdapter {

    private Context mContext;
    public FragmentCategory(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {

       if(position == 0){
            return  new AudioFragment();
        }else if(position == 1){
            return  new GenreFragment();
        }else if(position==2){
            return  new AlbumFragment();
        }
       else if(position==3){
           return  new ArtistsFragment();
       }

       else{
            return  new AudioFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

       if(position == 0){
            return mContext.getString(R.string.action_movies);
        }else if(position == 1){
            return mContext.getString(R.string.genre);
        }else if(position == 2){
            return mContext.getString(R.string.album);
        } else if(position == 3){
           return mContext.getString(R.string.artist);
       }  else {
            return mContext.getString(R.string.app_name);
        }
    }


}
