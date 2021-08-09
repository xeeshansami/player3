package com.play.view.player.videoplayer4k.equalizer;

import android.content.Context;
import android.graphics.Color;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AudioFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    int sessionId;

    public AudioFragmentPagerAdapter(Context context, FragmentManager fm, int sessionID) {
        super(fm);
        mContext = context;
        sessionId = sessionID;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            CustomFragment customFragment = CustomFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return customFragment;
        } else if (position == 1) {
            NormalFragment normalFragment = NormalFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return normalFragment;
        } else if (position == 2) {
            ClassicalFragment classicalFragment = ClassicalFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return classicalFragment;
        } else if (position == 3) {
            DanceFragment danceFragment = DanceFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return danceFragment;
        } else if (position == 4) {
            FlatFragment flatFragment = FlatFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return flatFragment;
        } else if (position == 5) {
            FolkFragment folkFragment = FolkFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return folkFragment;
        } else if (position == 6) {
            HeavyMetalFragment heavyMetalFragment = HeavyMetalFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return heavyMetalFragment;
        } else if (position == 7) {
            HiphopFragment hiphopFragment = HiphopFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return hiphopFragment;
        } else if (position == 8) {
            JazzFragment jazzFragment = JazzFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return jazzFragment;
        } else if (position == 9) {
            PopFragment popFragment = PopFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return popFragment;
        } else if (position == 10) {
            RockFragment rockFragment = RockFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return rockFragment;
        } else {
            CustomFragment customFragment = CustomFragment.newBuilder()
                    .setAccentColor(Color.parseColor("#FEAD02"))
                    .setAudioSessionId(sessionId)
                    .build();
            return customFragment;
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 11;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Custom";
            case 1:
                return "Normal";
            case 2:
                return "Classical";
            case 3:
                return "Dance";
            case 4:
                return "Flat";
            case 5:
                return "Folk";
            case 6:
                return "Heavy Metal";
            case 7:
                return "Hip Hop";
            case 8:
                return "Jazz";
            case 9:
                return "Pop";
            case 10:
                return "Rock";
            default:
                return null;
//        }
        }

    }
}