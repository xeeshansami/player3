package com.play.view.videoplayer.hd.equalizer;

import android.graphics.Color;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final List<Integer> mFragmentAudioSessionList = new ArrayList<>();
    static int sessionID = 0;
    static boolean switchButtonValue;

    public ViewPagerAdapter(FragmentManager manager, int sessionID, boolean switchButtonValue) {
        super(manager);
        this.sessionID = sessionID;
        this.switchButtonValue = switchButtonValue;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CustomFragment equalizerFragment;
                equalizerFragment = CustomFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(this.sessionID)
                        .setSwitchSession(this.switchButtonValue)
                        .build();
                Log.i("CustomFragment", this.switchButtonValue + " = "+ this.sessionID);
                return equalizerFragment;
            case 1:
                NormalFragment normalFragment;
                normalFragment = NormalFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(sessionID)
                        .build();
                Log.i("NormalFragment", this.switchButtonValue + " = "+ this.sessionID);
                return normalFragment;
            case 2:
                ClassicalFragment classicalFragment;
                classicalFragment = ClassicalFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(sessionID)
                        .build();
                return classicalFragment;
            case 3:
                DanceFragment danceFragment;
                danceFragment = DanceFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(sessionID)
                        .build();
                return danceFragment;
            case 4:
                FlatFragment flatFragment;
                flatFragment = FlatFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(sessionID)
                        .build();
                return flatFragment;
            case 5:
                FolkFragment folkFragment;
                folkFragment = FolkFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(sessionID)
                        .build();
                return folkFragment;
            case 6:
                HeavyMetalFragment heavyMetalFragment;
                heavyMetalFragment = HeavyMetalFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(sessionID)
                        .build();
                return heavyMetalFragment;
            case 7:
                HiphopFragment hiphopFragment;
                hiphopFragment = HiphopFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(sessionID)
                        .build();
                return hiphopFragment;
            case 8:
                RockFragment rockFragment;
                rockFragment = RockFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(sessionID)
                        .build();
                return rockFragment;
            case 9:
                JazzFragment jazzFragment;
                jazzFragment = JazzFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(sessionID)
                        .build();
                return jazzFragment;
            case 10:
                PopFragment popFragment;
                popFragment = PopFragment.newBuilder()
                        .setAccentColor(Color.parseColor("#FEAD02"))
                        .setAudioSessionId(sessionID)
                        .build();
                return popFragment;

            default:
                return mFragmentList.get(position);

        }


    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}