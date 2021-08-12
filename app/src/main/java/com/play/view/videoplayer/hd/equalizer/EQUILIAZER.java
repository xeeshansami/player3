package com.play.view.videoplayer.hd.equalizer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.play.view.videoplayer.hd.R;


public class EQUILIAZER extends Fragment {
    public SwitchCompat switchCompat;
    static Context context;
    ImageView backequilizer;
    static int themeColor = Color.parseColor("#B24242");
    public static int audioSesionId;
    boolean swtichButtonValue;

//    public static EQUILIAZER newInstance(int audioSessionId) {
//        Bundle args = new Bundle();
//        EQUILIAZER fragment = new EQUILIAZER();
//        audioSesionId = audioSessionId;
//        fragment.setArguments(args);
//
//        return fragment;
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.equiliazer_activity, container, false);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        Log.i("valuesChecks", "1");

        backequilizer = view.findViewById(R.id.backequilizer);
        backequilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "yes", Toast.LENGTH_SHORT).show();
            }
        });

        switchCompat = view.findViewById(R.id.equalizer_switch);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (viewPager != null) {
                    setupViewPager(viewPager, isChecked);
                }
            }
        });

        backequilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(getTargetFragment()).commitAllowingStateLoss();
            }
        });


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        assert viewPager != null;
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager, boolean swtichButtonValue) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), audioSesionId, swtichButtonValue);
        adapter.addFrag(new CustomFragment(), "Custom");
        adapter.addFrag(new NormalFragment(), "Normal");
        adapter.addFrag(new ClassicalFragment(), "Classical");
        adapter.addFrag(new PopFragment(), "Pop");
        adapter.addFrag(new FlatFragment(), "Flat");
        adapter.addFrag(new RockFragment(), "Rock");
        adapter.addFrag(new HeavyMetalFragment(), "HeavyMetal");
        adapter.addFrag(new HiphopFragment(), "Hip hop");
        adapter.addFrag(new FolkFragment(), "Folk");
        adapter.addFrag(new JazzFragment(), "Jazz");
        adapter.addFrag(new DanceFragment(), "Dance");
        viewPager.setAdapter(adapter);
    }

//    public static Builder newBuilder() {
//        return new Builder();
//    }
//
//
//    public static class Builder {
//        private int id = -1;
//
//        public Builder setAudioSessionId(int id) {
//            this.id = id;
//            return this;
//        }
//
//        public Builder setAccentColor(int color) {
//            themeColor = color;
//            return this;
//        }
//
//        public EQUILIAZER build() {
//            return EQUILIAZER.newInstance(id);
//        }
//    }

}
