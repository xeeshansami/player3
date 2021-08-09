package com.play.view.player.videoplayer4k;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

/**
 * Created by azhar on 3/8/2017.
 */

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    public SwitchPreferenceCompat auto_scan, clear_cache;
    Preference dialogPrefernce, languagePeference,quitPreferences;
    Preference seek;
    Preference volume;
    Preference brightness,showhidden,enablesubtitles,subembadded,characterenc,privacy;

    private SharedPreferences preferences = null;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefere);
//        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
//        PreferenceScreen prefScre = getPreferenceScreen();
//        int count = prefScre.getPreferenceCount();
//        for(int i = 0; i<count;i++){
//            Preference preference = prefScre.getPreference(i);
//            if(!(preference instanceof CheckBoxPreference)){
//                String value = sharedPreferences.getString(preference.getKey(),"");
//                setPreferenceSummary(preference,value);
//            }
//        }
        languagePeference = (Preference) findPreference(getResources().getString(R.string.language_key));
        privacy=findPreference("privacy");
        quitPreferences = (Preference) findPreference(getResources().getString(R.string.quit_key));
//        dialogPrefernce = (Preference) findPreference(getResources().getString(R.string.reset_key));
//        auto_scan = (SwitchPreferenceCompat) findPreference(getResources().getString(R.string.auto_scan_key)); //Preference Key
//        clear_cache = (SwitchPreferenceCompat) findPreference(getResources().getString(R.string.clear_cache_key));
        seek = findPreference("checkbox1");
        volume = findPreference("checkbox2");
        brightness = findPreference("checkbox3");

//        showhidden = findPreference("checkhidden");
//        enablesubtitles = findPreference("checksub");
//        subembadded = findPreference("checkemb");
//        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
//        PreferenceScreen prefScre = getPreferenceScreen();
//        int count = prefScre.getPreferenceCount();
//        for(int i = 0; i<count;i++){
//            Preference preference = prefScre.getPreference(i);
//            if(!(preference instanceof CheckBoxPreference)){
//                String value = sharedPreferences.getString(preference.getKey(),"");
//                setPreferenceSummary(preference,value);
//            }
//        }
        privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse("https://www.google.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            }
        });
        seek.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean){
                    Boolean boolSeek = (Boolean)newValue;
                    preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("checked1", boolSeek);
                    editor.commit();
                    if (boolSeek == true)
                    {
                        Toast.makeText(getContext(), "Enabled", Toast.LENGTH_SHORT).show();
                    }else if (boolSeek == false)
                    {
                        Toast.makeText(getContext(), "Disabled", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            }
        });
        volume.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean){
                    Boolean boolVol = (Boolean)newValue;
                    preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("checked2", boolVol);
                    editor.commit();
                    if (boolVol == true)
                    {
                        Toast.makeText(getContext(), "Enabled", Toast.LENGTH_SHORT).show();
                    }else if (boolVol == false)
                    {
                        Toast.makeText(getContext(), "Disabled", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
        brightness.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean){
                    Boolean boolBri = (Boolean)newValue;
                    preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("checked3", boolBri);
                    editor.commit();
                    if (boolBri == true)
                    {
                        Toast.makeText(getContext(), "Enabled", Toast.LENGTH_SHORT).show();
                    }else if (boolBri == false)
                    {
                        Toast.makeText(getContext(), "Disabled", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
//        showhidden.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                if(newValue instanceof Boolean){
//                    Boolean boolhidden = (Boolean)newValue;
//                    preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putBoolean("checkhidden", boolhidden);
//                    editor.commit();
//                    Toast.makeText(getContext(), "Refresh view list to apply the changes", Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//        });
//        enablesubtitles.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                if(newValue instanceof Boolean){
//                    Boolean boolsubtitles = (Boolean)newValue;
//                    preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putBoolean("checksub", boolsubtitles);
//                    editor.commit();
//                    if (boolsubtitles.equals(Boolean.TRUE)){
//                    Toast.makeText(getContext(), "Enable Subtitles", Toast.LENGTH_SHORT).show();
//                } else {
//                        Toast.makeText(getContext(), "Disable Subtitles", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                return true;
//            }
//        });
//        subembadded.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                if(newValue instanceof Boolean){
//                    Boolean boolembeded = (Boolean)newValue;
//                    preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putBoolean("checkemb", boolembeded);
//                    editor.commit();
//                    if (boolembeded.equals(Boolean.TRUE)){
//                        Toast.makeText(getContext(), "Enable Embedded Subtitles", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getContext(), "Disable Embedded Subtitles", Toast.LENGTH_SHORT).show();
//                    }                }
//                return true;
//            }
//        });
        String locale = this.getResources().getConfiguration().locale.getDisplayName();
//        String LAnglocale = locale.getDefault().getDisplayName();
        String lan = Locale.getDefault().getDisplayLanguage();
        String langCode = Locale.getDefault().getLanguage(); //to get usual language code

        System.out.println(lan + "\n" + langCode + "\n" + locale);
        languagePeference.setSummary(locale);

        getPreferenceManager().findPreference(getResources()
                .getString(R.string.quit_key)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getActivity().finishAffinity();
                return false;
            }
        });

//        getPreferenceManager().findPreference(getResources().getString(R.string.reset_key)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//
//            public boolean onPreferenceClick(final Preference preference) {
//                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
//                alertDialog.setMessage("Are you sure you want to reset all settings to default?");
//                alertDialog.setCancelable(true);
//                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.clear();
//                        editor.apply();
//                        boolean test = preferences.getBoolean(getResources().getString(R.string.auto_scan_key), true);
//                        System.out.println("RESET"+test);
//
//                    }
//                });
//                alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                alertDialog.show();
//                return false;
//            }
//        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Preference pref = findPreference(key);
//        if (pref != null) {
//            if(!(pref instanceof CheckBoxPreference)){
//                String value = sharedPreferences.getString(pref.getKey(),"");
//                setPreferenceSummary(pref,value);
//            }
//        }
        String locale = this.getResources().getConfiguration().locale.getDisplayName();
//        String LAnglocale = locale.getDefault().getDisplayName();
        String lan = Locale.getDefault().getDisplayLanguage();
        String langCode = Locale.getDefault().getLanguage(); //to get usual language code

        System.out.println(lan + "\n" + langCode + "\n" + locale);
        if (key.equals(getResources().getString(R.string.language_key))) {
            languagePeference.setSummary(locale);
        }
//        if (key.equals(getResources().getString(R.string.auto_scan_key))) {
//            boolean test = sharedPreferences.getBoolean(getResources().getString(R.string.auto_scan_key), true);
//            //Do whatever you want here. This is an example.
//            if (test) {
////                testPref.setSummary("Enabled");
//                System.out.println(test);
//            } else {
////                testPref.setSummary("Disabled");
//                System.out.println(test);
//            }
//
//            if (key.equals(getResources().getString(R.string.clear_cache_key))) {
//                boolean clear_cache = sharedPreferences.getBoolean(getResources().getString(R.string.clear_cache_key), true);
//                //Do whatever you want here. This is an example.
//                if (clear_cache) {
//                    deleteCache(getContext());
////                testPref.setSummary("Enabled");
//                    System.out.println(clear_cache);
//                } else {
////                testPref.setSummary("Disabled");
//                    System.out.println(clear_cache);
//                }
//
//
//            }
//
//        }
    }

//    private void setPreferenceSummary(Preference preference, String value) {
//
//        if(preference instanceof ListPreference){
//            ListPreference listPreference = (ListPreference)preference;
//            int preIndex = listPreference.findIndexOfValue(value);
//            if(preIndex >= 0){
//                listPreference.setSummary(listPreference.getEntries()[preIndex]);
//            }
//        }
//    }
public static void deleteCache(Context context) {
    try {
        File dir = context.getCacheDir();
        deleteDir(dir);
    } catch (Exception e) {}
}
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean clear_cache = preferences.getBoolean(getResources().getString(R.string.clear_cache_key), true);
        if (clear_cache) {
//                testPref.setSummary("Enabled");
            System.out.println(clear_cache);
        } else {
//                testPref.setSummary("Disabled");
            System.out.println(clear_cache);
        }
        boolean test = preferences.getBoolean(getResources().getString(R.string.auto_scan_key), true);

        if (test) {
//            testPref.setSummary("Enabled");
        } else {
//            testPref.setSummary("Disabled");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}
