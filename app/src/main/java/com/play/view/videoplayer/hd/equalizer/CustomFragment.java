package com.play.view.videoplayer.hd.equalizer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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
public class CustomFragment extends Fragment {
    private static boolean switchValue = true;
    ImageView backBtn;
    TextView fragTitle;
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


    public CustomFragment() {
        // Required empty public constructor
    }

    public Equalizer mEqualizer;
    public BassBoost bassBoost;
    public PresetReverb presetReverb;
    private int audioSesionId;
    boolean enabledVar = true;
    static int themeColor = Color.parseColor("#B24242");

    public static CustomFragment newInstance(int audioSessionId) {

        Bundle args = new Bundle();
        CustomFragment fragment = new CustomFragment();
        fragment.audioSesionId = audioSessionId;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            SessionStorage sessionStorage = new SessionStorage(getActivity().getApplicationContext());
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
        } catch (IndexOutOfBoundsException e) {
        } catch (IllegalArgumentException e) {
        } catch (SecurityException e) {
        } catch (IllegalStateException e) {
        } catch (NullPointerException e) {
        } catch (RuntimeException e) {
        } catch (Exception e) {
        }
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
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            backBtn = view.findViewById(R.id.backequilizer);
            relativeLayout = view.findViewById(R.id.mainframe);
            backBtn.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    relativeLayout.setVisibility(View.GONE);
                }
            });
            equalizerSwitch = view.findViewById(R.id.equalizer_switch);
            equalizerSwitch.setChecked(true);
            mLinearLayout = view.findViewById(R.id.equalizerContainer);
            spinnerDropDownIcon = view.findViewById(R.id.spinner_dropdown_icon);
            spinnerDropDownIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presetSpinner.performClick();
                }
            });

            bassController = view.findViewById(R.id.controllerBass);
            reverbController = view.findViewById(R.id.controller3D);
            presetSpinner = view.findViewById(R.id.equalizer_preset_spinner);
            equalizerBlocker = view.findViewById(R.id.equalizerBlocker);
            equalizerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        mLinearLayout.setEnabled(isChecked);
                        mEqualizer.setEnabled(isChecked);
                        bassBoost.setEnabled(isChecked);
                        spinnerDropDownIcon.setEnabled(isChecked);
                        presetSpinner.setEnabled(isChecked);
                        bassController.setEnabled(isChecked);
                        reverbController.setEnabled(isChecked);
                    } catch (IndexOutOfBoundsException ef) {
                    } catch (IllegalArgumentException ef) {
                    } catch (SecurityException ef) {
                    } catch (IllegalStateException ef) {
                    } catch (NullPointerException ef) {
                    } catch (RuntimeException ef) {
                    } catch (Exception ef) {
                    }
//                presetReverb.setEnabled(isChecked);
                }
            });

            if (switchValue) {
                mEqualizer.setEnabled(switchValue);
                bassBoost.setEnabled(switchValue);
//            presetReverb.setEnabled(switchValue);
            }

            chart = view.findViewById(R.id.lineChart);
            paint = new Paint();
            dataset = new LineSet();

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
                seekBarFinal[i] = seekBar;
                seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.lightgolden), PorterDuff.Mode.SRC_IN));
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
                            Log.e("errors", e.getMessage());
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

            paint.setColor(Color.parseColor("#fdd683"));
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

        } catch (IndexOutOfBoundsException e) {
        } catch (IllegalArgumentException e) {
        } catch (SecurityException e) {
        } catch (IllegalStateException e) {
        } catch (NullPointerException e) {
        } catch (RuntimeException e) {
        } catch (Exception e) {
        }
    }

    public void equalizeSound() {
        ArrayList<String> equalizerPresetNames = new ArrayList<>();
        ArrayAdapter<String> equalizerPresetSpinnerAdapter = new ArrayAdapter<>(ctx,
                R.layout.equalizer_spinner_item,
                equalizerPresetNames);
        equalizerPresetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        equalizerPresetNames.add("Custom");

        for (short i = 0; i < mEqualizer.getNumberOfPresets(); i++) {
            equalizerPresetNames.add(mEqualizer.getPresetName(i));
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

        public Builder setSwitchSession(boolean switchSession) {
            switchValue = switchSession;
            return this;
        }

        public CustomFragment build() {
            return CustomFragment.newInstance(id);
        }
    }
}
