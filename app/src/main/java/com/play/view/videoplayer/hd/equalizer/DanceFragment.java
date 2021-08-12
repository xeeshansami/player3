package com.play.view.videoplayer.hd.equalizer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.play.view.videoplayer.hd.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DanceFragment extends Fragment {

    ImageView backBtn;
    TextView fragTitle,equilizername;
    SwitchCompat equalizerSwitch;

    LineSet dataset;
    LineChartView chart;
    Paint paint;
    float[] points;

    int y = 0;

    ImageView spinnerDropDownIcon;

    short numberOfFrequencyBands;
    LinearLayout mLinearLayout;

    SeekBar[] seekBarFinal = new SeekBar[5];

    AnalogController bassController, reverbController;

    Spinner presetSpinner;

    FrameLayout equalizerBlocker;
    RelativeLayout relativeLayout;


    Context ctx;


    public DanceFragment() {
        // Required empty public constructor
    }

    public Equalizer mEqualizer;
    public BassBoost bassBoost;
    public PresetReverb presetReverb;
    private static int audioSesionId;

    static int themeColor = Color.parseColor("#FEAD02");

    public static DanceFragment newInstance(int audioSessionId) {

        Bundle args = new Bundle();
        DanceFragment fragment = new DanceFragment();
        audioSesionId = audioSessionId;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.equalizer_fragment, container, false);
        equilizername = view.findViewById(R.id.equilizername);
        return view;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        backBtn = view.findViewById(R.id.equalizer_back_btn);
        relativeLayout = view.findViewById(R.id.mainframe);
        SessionStorage sessionStorage = new SessionStorage(getActivity());
        mEqualizer = new Equalizer(0, sessionStorage.loadSession());
        bassBoost = new BassBoost(0, sessionStorage.loadSession());
        bassBoost.setEnabled(true);
        BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
        BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
        bassBoostSetting.strength = (1000 / 19);
        bassBoost.setProperties(bassBoostSetting);

//        presetReverb = new PresetReverb(0, audioSesionId);
//        presetReverb.setPreset(PresetReverb.PRESET_NONE);
//        presetReverb.setEnabled(true);
        Settings.equalizerModel = new EqualizerModel();
        mEqualizer.setEnabled(true);
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (getActivity() != null) {
////                    getActivity().onBackPressed();
////                }
//
//                if (getActivity() != null) {
//                    relativeLayout.setVisibility(View.GONE);
//                }
//            }
//        });

//        fragTitle = view.findViewById(R.id.equalizer_fragment_title);

//
//        equalizerSwitch = view.findViewById(R.id.equalizer_switch);
//        equalizerSwitch.setChecked(true);
//        equalizerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mEqualizer.setEnabled(isChecked);
//                bassBoost.setEnabled(isChecked);
////                presetReverb.setEnabled(isChecked);
//            }
//        });

        spinnerDropDownIcon = view.findViewById(R.id.spinner_dropdown_icon);
        spinnerDropDownIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presetSpinner.performClick();
            }
        });

        presetSpinner = view.findViewById(R.id.equalizer_preset_spinner);

        equalizerBlocker = view.findViewById(R.id.equalizerBlocker);

        chart = view.findViewById(R.id.lineChart);
        paint = new Paint();
        dataset = new LineSet();

        bassController = view.findViewById(R.id.controllerBass);
        reverbController = view.findViewById(R.id.controller3D);

        bassController.setLabel("BASS");
        reverbController.setLabel("3D");

        bassController.circlePaint2.setColor(themeColor);
        bassController.linePaint.setColor(themeColor);
        bassController.invalidate();
        reverbController.circlePaint2.setColor(themeColor);
        bassController.linePaint.setColor(themeColor);
        reverbController.invalidate();

        if (!Settings.isEqualizerReloaded) {
            int x = 0;
            if (bassBoost != null) {
                try {
                    x = ((bassBoost.getRoundedStrength() * 19) / 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (presetReverb != null) {
                try {
                    y = (presetReverb.getPreset() * 19) / 6;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (x == 0) {
                bassController.setProgress(1);
            } else {
                bassController.setProgress(x);
            }

            if (y == 0) {
                reverbController.setProgress(1);
            } else {
                reverbController.setProgress(y);
            }
        } else {
            int x = ((Settings.bassStrength * 19) / 1000);
            y = (Settings.reverbPreset * 19) / 6;
            if (x == 0) {
                bassController.setProgress(1);
            } else {
                bassController.setProgress(x);
            }

            if (y == 0) {
                reverbController.setProgress(1);
            } else {
                reverbController.setProgress(y);
            }
        }

        bassController.setOnProgressChangedListener(new AnalogController.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                Settings.bassStrength = (short) (((float) 1000 / 19) * (progress));
                try {
                    bassBoost.setStrength(Settings.bassStrength);
                    Settings.equalizerModel.setBassStrength(Settings.bassStrength);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        reverbController.setOnProgressChangedListener(new AnalogController.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                Settings.reverbPreset = (short) ((progress * 6) / 19);
                Settings.equalizerModel.setReverbPreset(Settings.reverbPreset);
                try {
                    presetReverb.setPreset(Settings.reverbPreset);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                y = progress;
            }
        });

        mLinearLayout = view.findViewById(R.id.equalizerContainer);

        TextView equalizerHeading = new TextView(getContext());
        equalizerHeading.setText(R.string.eq);
        equalizerHeading.setTextSize(20);
        equalizerHeading.setGravity(Gravity.CENTER_HORIZONTAL);

        numberOfFrequencyBands = 5;

        points = new float[numberOfFrequencyBands];

        final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];
        final short upperEqualizerBandLevel = mEqualizer.getBandLevelRange()[1];

        for (short i = 0; i < numberOfFrequencyBands; i++) {
            final short equalizerBandIndex = i;
            final TextView frequencyHeaderTextView = new TextView(getContext());
            frequencyHeaderTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            frequencyHeaderTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            frequencyHeaderTextView.setTextColor(Color.parseColor("#FFFFFF"));
            frequencyHeaderTextView.setText((mEqualizer.getCenterFreq(equalizerBandIndex) / 1000) + "Hz");

            LinearLayout seekBarRowLayout = new LinearLayout(getContext());
            seekBarRowLayout.setOrientation(LinearLayout.VERTICAL);

            TextView lowerEqualizerBandLevelTextView = new TextView(getContext());
            lowerEqualizerBandLevelTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            lowerEqualizerBandLevelTextView.setTextColor(Color.parseColor("#FFFFFF"));
            lowerEqualizerBandLevelTextView.setText((lowerEqualizerBandLevel / 100) + "dB");

            TextView upperEqualizerBandLevelTextView = new TextView(getContext());
            lowerEqualizerBandLevelTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            upperEqualizerBandLevelTextView.setTextColor(Color.parseColor("#FFFFFF"));
            upperEqualizerBandLevelTextView.setText((upperEqualizerBandLevel / 100) + "dB");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.weight = 1;

            SeekBar seekBar = new SeekBar(getContext());
            TextView textView = new TextView(getContext());
            switch (i) {
                case 0:
                    seekBar = view.findViewById(R.id.seekBar1);
                    textView = view.findViewById(R.id.textView1);
                    break;
                case 1:
                    seekBar = view.findViewById(R.id.seekBar2);
                    textView = view.findViewById(R.id.textView2);
                    break;
                case 2:
                    seekBar = view.findViewById(R.id.seekBar3);
                    textView = view.findViewById(R.id.textView3);
                    break;
                case 3:
                    seekBar = view.findViewById(R.id.seekBar4);
                    textView = view.findViewById(R.id.textView4);
                    break;
                case 4:
                    seekBar = view.findViewById(R.id.seekBar5);
                    textView = view.findViewById(R.id.textView5);
                    break;
            }
            seekBar.setEnabled(false);
            seekBarFinal[i] = seekBar;
            seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN));
            seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(themeColor, PorterDuff.Mode.SRC_IN));
            seekBar.setId(i);
//            seekBar.setLayoutParams(layoutParams);
            seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);

            textView.setText(frequencyHeaderTextView.getText());
            textView.setTextColor(Color.WHITE);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if (Settings.isEqualizerReloaded) {
                points[i] = Settings.seekbarpos[i] - lowerEqualizerBandLevel;
                dataset.addPoint(frequencyHeaderTextView.getText().toString(), points[i]);
                seekBar.setProgress(Settings.seekbarpos[i] - lowerEqualizerBandLevel);
            } else {
                points[i] = mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;
                dataset.addPoint(frequencyHeaderTextView.getText().toString(), points[i]);
                seekBar.setProgress(mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
                Settings.seekbarpos[i] = mEqualizer.getBandLevel(equalizerBandIndex);
                Settings.isEqualizerReloaded = true;
            }
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    try {
                        mEqualizer.setBandLevel(equalizerBandIndex, (short) (progress + lowerEqualizerBandLevel));
                        points[seekBar.getId()] = mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;
                        Settings.seekbarpos[seekBar.getId()] = (progress + lowerEqualizerBandLevel);
                        Settings.equalizerModel.getSeekbarpos()[seekBar.getId()] = (progress + lowerEqualizerBandLevel);
                        dataset.updateValues(points);
                        chart.notifyDataUpdate();
                    } catch (UnsupportedOperationException e) {
                        Log.i("exception", e.getMessage());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    presetSpinner.setSelection(0);
                    Settings.presetPos = 0;
                    Settings.equalizerModel.setPresetPos(0);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

        equalizeSound();

        paint.setColor(Color.parseColor("#555555"));
        paint.setStrokeWidth((float) (1.10 * Settings.ratio));

        dataset.setColor(themeColor);
        dataset.setSmooth(true);
        dataset.setThickness(5);

        chart.setXAxis(false);
        chart.setYAxis(false);

        chart.setYLabels(AxisController.LabelPosition.NONE);
        chart.setXLabels(AxisController.LabelPosition.NONE);
        chart.setGrid(ChartView.GridType.NONE, 7, 10, paint);

        chart.setAxisBorderValues(-300, 3300);

        chart.addData(dataset);
        chart.show();

        Button mEndButton = new Button(getContext());
        mEndButton.setBackgroundColor(themeColor);
        mEndButton.setTextColor(Color.WHITE);


    }

    public void equalizeSound() {
        ArrayList<String> equalizerPresetNames = new ArrayList<>();
        ArrayAdapter<String> equalizerPresetSpinnerAdapter = new ArrayAdapter<>(ctx,
                R.layout.equalizer_spinner_item,
                equalizerPresetNames);
        equalizerPresetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        equalizerPresetNames.add("Dance");

        for (short i = 0; i < mEqualizer.getNumberOfPresets(); i++) {
            equalizerPresetNames.add(mEqualizer.getPresetName(i));
            equilizername.setText(mEqualizer.getPresetName(i));
        }

        presetSpinner.setAdapter(equalizerPresetSpinnerAdapter);
        //presetSpinner.setDropDownWidth((Settings.screen_width * 3) / 4);
        if (Settings.isEqualizerReloaded && Settings.presetPos != 0) {
//            correctPosition = false;
            presetSpinner.setSelection(Settings.presetPos);
        }

        presetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    position = 3;
                    if (position != 0) {

                        mEqualizer.usePreset((short) (position - 1));
                        Settings.presetPos = position;
                        short numberOfFreqBands = 5;

                        final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];

                        for (short i = 0; i < numberOfFreqBands; i++) {
                            seekBarFinal[i].setProgress(mEqualizer.getBandLevel(i) - lowerEqualizerBandLevel);
                            points[i] = mEqualizer.getBandLevel(i) - lowerEqualizerBandLevel;
                            Settings.seekbarpos[i] = mEqualizer.getBandLevel(i);
                            Settings.equalizerModel.getSeekbarpos()[i] = mEqualizer.getBandLevel(i);
                        }
                        dataset.updateValues(points);
                        chart.notifyDataUpdate();
                    }
                } catch (Exception e) {
                    Toast.makeText(ctx, "Error while updating Equalizer", Toast.LENGTH_SHORT).show();
                }
                Settings.equalizerModel.setPresetPos(position);
                equilizername.setText(presetSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private int id = -1;

        public Builder setAudioSessionId(int id) {
            this.id = id;
            return this;
        }

        public Builder setAccentColor(int color) {
            themeColor = color;
            return this;
        }

        public DanceFragment build() {
            return DanceFragment.newInstance(id);
        }
    }


}