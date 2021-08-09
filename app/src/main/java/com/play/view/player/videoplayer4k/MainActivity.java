package com.play.view.player.videoplayer4k;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , SharedPreferences.OnSharedPreferenceChangeListener {
    ViewPager viewPager;
    DrawerLayout drawer;
    private AlertDialog alertDialog;
    boolean doubleBackToExitPressedOnce = false;
    String[] DayOfWeek = {"Music", "Videos", "Audios"};
    public static TabLayout tabLayout;
    ExpandedMenuModel item1, item2;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    boolean mVisible;
    private boolean mVisibleVideo;
    Dialog dialog, dialog1;
    String listPref;
    StringBuilder builder;
    private AdView mAdView;
    private InterstitialAd mInterstitial;
    private AdRequest adRequest, ar;
    ProgressBar loadingIndicator;
    AlbumFragment albumFragment;
    private Handler handler, handlerLoader;
    boolean isActivityIsVisible = true;


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected() && wifiNetwork.isAvailable()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected() && mobileNetwork.isAvailable()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable()) {
            return true;
        }

        return false;
    }

    public void ads() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
//                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                .build();
        mAdView.loadAd(adRequest);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        dialog = new Dialog(MainActivity.this);
        // dialog.setTitle("               Please Wait");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(getLayoutInflater().inflate(R.layout.custom, null));


        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (isOnline()) {

                    if (!MainActivity.this.isFinishing()) {
                        dialog.show();
                    }

                    mInterstitial = null;

                    Display display = ((WindowManager) getSystemService(getApplication().WINDOW_SERVICE)).getDefaultDisplay();
                    int width = display.getWidth();
                    int height = display.getHeight();
                    Log.v("width", width + "");
                    dialog.getWindow().setLayout((6 * width) / 8, (4 * height) / 20);

                    AdRequest adRequestInter = new AdRequest.Builder().build();
                    mInterstitial = new InterstitialAd(getApplicationContext());
                    mInterstitial.setAdUnitId(getResources().getString(R.string.intersitial_as_unit_id));

                    mInterstitial.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            if (mInterstitial.isLoaded()) {
                                if (mInterstitial != null) {
                                    loadingIndicator.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    //loadingIndicator.draw();
                                    if (isActivityIsVisible) {
                                        mInterstitial.show();
                                    }
                                }
                            }
                        }
                    });
                    mInterstitial.loadAd(adRequestInter);
                }
            }
        }, 1000);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /* to set the menu icon image*/
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);


        AppBarLayout barLayout = (AppBarLayout) findViewById(R.id.app_bar);
        barLayout.setBackgroundColor(getResources().getColor(R.color.Lightblack));
        toolbar.setBackgroundColor(getResources().getColor(R.color.Lightblack));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                Intent intent = new Intent(MainActivity.this,VideoDetailActivity.class);
//                startActivity(intent);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        view.setBackgroundColor(getResources().getColor(R.color.Lightblack));
//        Spinner spinner = (Spinner) view.findViewById(R.id.drop_down);
//        spinner.setAdapter(new MyCustomAdapter(MainActivity.this, R.layout.spinner_items, DayOfWeek));
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String item = (String) adapterView.getItemAtPosition(i);
//                if (item == "Videos") {
//                    startActivity(new Intent(MainActivity.this, VideoActivity.class));
//                    Toast.makeText(adapterView.getContext(), "Selection " + item, Toast.LENGTH_LONG).show();
//
//                } else if (item == "Audios") {
//                    startActivity(new Intent(MainActivity.this, MainActivity.class));
//                    Toast.makeText(adapterView.getContext(), "Selection " + item, Toast.LENGTH_LONG).show();
//                } else {
//                    drawer.closeDrawers();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        prepareListData();
        setupShare();
        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);
//        Drawable d = getResources().getDrawable(R.drawable.drawer_about_us);
//        expandableList.setIndicatorBounds(345,375);
//        expandableList.setGroupIndicator(d);
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override

            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int child, long l) {
                //Log.d("DEBUG", "submenu item clicked");
// TODO Auto-generated method stub
//                Log.i("child", String.valueOf(i));
//                Toast.makeText(
//                        getApplicationContext(),listDataChild.get(listDataHeader.get(i)).get(child), Toast.LENGTH_LONG)
//                        .show();
                final String selected = (String) mMenuAdapter.getChild(i, child);
//                if(selected == "Videos"){
//                    Log.i("Selected","Videos");
//                }
                switch (selected) {
                    case "Audio Folders":
                        startActivity(new Intent(MainActivity.this, Main2Activity.class));
                        drawer.closeDrawers();
                        break;
                    case "All Songs":
                        Log.d("Submenu clicked", String.valueOf(child));
                        viewPager.setCurrentItem(0, true);
                        FragmentManager fmst = getSupportFragmentManager();
                        FragmentTransaction ftst = fmst.beginTransaction();
                        drawer.closeDrawers();
                        break;
                    case "Albums":
                        Log.d("Submenu clicked", String.valueOf(child));
                        viewPager.setCurrentItem(2, true);
                        FragmentManager fmsto = getSupportFragmentManager();
                        FragmentTransaction ftsto = fmsto.beginTransaction();
                        drawer.closeDrawers();
                        break;
                    case "Artists":
                        Log.d("Submenu clicked", String.valueOf(child));
                        viewPager.setCurrentItem(3, true);
                        FragmentManager fmbf = getSupportFragmentManager();
                        FragmentTransaction ftbf = fmbf.beginTransaction();
                        drawer.closeDrawers();
                        break;
                    case "Genres":

                        Log.d("Submenu clicked", String.valueOf(child));
                        viewPager.setCurrentItem(1, true);
                        FragmentManager fmin = getSupportFragmentManager();
                        FragmentTransaction ftin = fmin.beginTransaction();
                        drawer.closeDrawers();
                        break;
                    case "Video Folders":
                        Log.d("Submenu clicked", "VideoFolders");
//                        Toast.makeText(MainActivity.this, "UnderConstruction", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(MainActivity.this, VideoFolder.class));
                        drawer.closeDrawers();
                        break;
                    case "All Videos":
                        Log.d("Submenu clicked", "Videos");
                        startActivity(new Intent(MainActivity.this, VideoActivity.class));
                        drawer.closeDrawers();
                        break;

                }
                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
//                    switch (i){
//                        case 0:
//                            Log.d("menu clicked" , String.valueOf(i));
//
//                            break;
//                        case 1:
//                            Log.d("menu clicked" , String.valueOf(i));
//                            break;
//                    }
                if (l == 0) {
                    Log.d("Header clicked", String.valueOf(l));
                    toggle();

                }
                if (l == 1) {
                    Log.d("Header clicked", String.valueOf(l));
                    toggleVideo();

                }
//                else if (l == 2) {
//                    Log.d("Header clicked", String.valueOf(l));
//                    startActivity(new Intent(MainActivity.this, Main2Activity.class));
//                    drawer.closeDrawers();
//                }
                else if (l == 2) {
                    Log.d("Header clicked", String.valueOf(l));
//                    Intent intent = new Intent(MainActivity.this,HelpActivity.class);
//                    startActivity(intent);
                    Intent shareIntent = createShareForecastIntent();
                    startActivity(shareIntent);
                    drawer.closeDrawers();
                } else if (l == 3) {
                    //RATEUS ONCLICK
                    Log.d("Header clicked", String.valueOf(l));
                    String url = "\"https://play.google.com/store/apps/details?id=com.dhdvideo.player\"";
                    // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.duskystudio.hdvideoplayer")));
                    Intent rateIntent = rateIntentForUrl(url);
                    startActivity(rateIntent);
//                    Intent intent = new Intent(MainActivity.this,SocialActivity.class);
//                    startActivity(intent);
//                    Toast.makeText(MainActivity.this, "UnderConstruction", Toast.LENGTH_SHORT).show();
                    //drawer.closeDrawers();
                } else if (l == 4) {
                    Log.d("Header clicked", String.valueOf(l));
                    Intent intent = new Intent(MainActivity.this, About.class);
                    startActivity(intent);
                    drawer.closeDrawers();
                } else if (l == 5) {
                    Log.d("Header clicked", String.valueOf(l));
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    drawer.closeDrawers();
                }


                return false;
            }
        });

    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        item1 = new ExpandedMenuModel();
        item1.setIconName("Music");
        item1.setIconImg(R.drawable.ic_keyboard_arrow_down_black_24dp);

        // Adding data header
        listDataHeader.add(item1);
//
//        ExpandedMenuModel item2 = new ExpandedMenuModel();
//        item2.setIconName("Help");
//        item2.setIconImg(R.drawable.draw);
//
//        listDataHeader.add(item2);
//
        item2 = new ExpandedMenuModel();
        item2.setIconName("Videos");
        item2.setIconImg(R.drawable.ic_keyboard_arrow_down_black_24dp);
        listDataHeader.add(item2);


        ExpandedMenuModel item3 = new ExpandedMenuModel();
        item3.setIconName("Share with Friends");
        item3.setIconImg(R.drawable.ic_share_black_24dp);
        listDataHeader.add(item3);


        ExpandedMenuModel item4 = new ExpandedMenuModel();
        item4.setIconName("Rate us");
        item4.setIconImg(R.drawable.ic_star_black_24dp);
        listDataHeader.add(item4);
        ExpandedMenuModel item5 = new ExpandedMenuModel();
        item5.setIconName("About");
        item5.setIconImg(R.drawable.ic_error_black_24dp);
        listDataHeader.add(item5);

        ExpandedMenuModel item6 = new ExpandedMenuModel();
        item6.setIconName("Settings");
        item6.setIconImg(R.drawable.ic_settings_black_24dp);
        listDataHeader.add(item6);
        // Adding child data
        List<String> heading1 = new ArrayList<String>();
        heading1.add("Audio Folders");
        heading1.add("All Songs");
        heading1.add("Albums");
        heading1.add("Artists");
        heading1.add("Genres");

        List<String> heading2 = new ArrayList<String>();
        heading2.add("Video Folders");
        heading2.add("All Videos");

//        List<String> heading2 = new ArrayList<String>();
        List<String> heading3 = new ArrayList<String>();
        List<String> heading4 = new ArrayList<String>();
        List<String> heading5 = new ArrayList<String>();
        List<String> heading6 = new ArrayList<String>();
//        heading2.add("Submenu of item 2");
//        heading2.add("Submenu of item 2");
//        heading2.add("Submenu of item 2");

        listDataChild.put(listDataHeader.get(0), heading1);// Header, Child data
        listDataChild.put(listDataHeader.get(1), heading2);
        listDataChild.put(listDataHeader.get(2), heading3);
        listDataChild.put(listDataHeader.get(3), heading4);
        listDataChild.put(listDataHeader.get(4), heading5);
        listDataChild.put(listDataHeader.get(5), heading6);

//        navigationView.setItemTextAppearance(R.style.Toolbar_TitleText);
        FragmentCategory fc = new FragmentCategory(this, getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(fc);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.Lightblack));
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press one more time to exit Player", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
////        MenuItem search = menu.findItem(R.id.search);
////        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
////        search(searchView);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                drawer.openDrawer(GravityCompat.START);
//                return true;
//        }        //noinspection SimplifiableIfStatement
//
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, SearchActivity.class));
//
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_eq) {

        } else if (id == R.id.nav_folders) {

            startActivity(new Intent(this, Main2Activity.class));
            return true;
        } else if (id == R.id.nav_eq) {

            viewPager.setCurrentItem(0, true);
            FragmentManager fmst = getSupportFragmentManager();
            FragmentTransaction ftst = fmst.beginTransaction();
            AudioFragment st = new AudioFragment();

            ftst.add(R.id.viewpager, st, "FULL HD VIDEO PLAYER");
            ftst.commit();

            drawer.closeDrawers();
            return true;
        } else if (id == R.id.nav_album) {

            viewPager.setCurrentItem(2, true);
            FragmentManager fmst = getSupportFragmentManager();
            FragmentTransaction ftst = fmst.beginTransaction();
            AlbumFragment st = new AlbumFragment();

            ftst.add(R.id.viewpager, st, "FULL HD VIDEO PLAYER");
            ftst.commit();
            drawer.closeDrawers();
            return true;
        } else if (id == R.id.nav_artist) {

            viewPager.setCurrentItem(3, true);
            FragmentManager fmst = getSupportFragmentManager();
            FragmentTransaction ftst = fmst.beginTransaction();
            ArtistsFragment st = new ArtistsFragment();

            ftst.add(R.id.viewpager, st, "Azhar");
            ftst.commit();
            drawer.closeDrawers();
            return true;
        } else if (id == R.id.nav_genre) {

            viewPager.setCurrentItem(1, true);
            FragmentManager fmst = getSupportFragmentManager();
            FragmentTransaction ftst = fmst.beginTransaction();
            GenreFragment st = new GenreFragment();
            ftst.add(R.id.viewpager, st, "Azhar");
            ftst.commit();
            drawer.closeDrawers();
            return true;
        } else if (id == R.id.nav_video) {

            startActivity(new Intent(this, VideoActivity.class));
            return true;
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String title = "Share Video  with Freinds";
            String link = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
            intent.putExtra(Intent.EXTRA_SUBJECT, title);
            intent.putExtra(Intent.EXTRA_TEXT, link);
            startActivity(Intent.createChooser(intent, "Share XM player using"));
        } else if (id == R.id.nav_rate) {
            network_stream();
            return true;
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, About.class));
            return true;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void network_stream() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setCancelable(true);

// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rate_us_demo, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();


        // Button cancel = (Button) dialogView.findViewById(R.id.rateUsCancel);
        Button ok = (Button) dialogView.findViewById(R.id.rateUsOk);
        Button quit = (Button) dialogView.findViewById(R.id.quit);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                alertDialog.dismiss();

            }
        });


        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finishAffinity();
            }
        });


        alertDialog.show();

    }
    private Intent createShareForecastIntent() {
        String textThatYouWantToShare =
                "Lovely Music:";
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle("Share via")
                .setType("text/plain")
                .setText(textThatYouWantToShare)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;

    }


    private void setupDrawerContent(NavigationView navigationView) {

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);

                Toast.makeText(getApplicationContext(), "clicked " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString(), Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();


                return true;
            }
        });


    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void toggleVideo() {
        if (mVisibleVideo) {
            hideVideo();
        } else {
            showVideo();
        }
    }

    private void hideVideo() {
        item2.setIconImg(R.drawable.ic_keyboard_arrow_down_black_24dp);
        mVisibleVideo = false;

    }

    private void showVideo() {
        item2.setIconImg(R.drawable.ic_keyboard_arrow_up_black_24dp);
        mVisibleVideo = true;

    }

    private void hide() {
        item1.setIconImg(R.drawable.ic_keyboard_arrow_down_black_24dp);
        mVisible = false;

    }

    private void show() {
        item1.setIconImg(R.drawable.ic_keyboard_arrow_up_black_24dp);
        mVisible = true;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Intent playerIntent = new Intent(this, MediaPlayerService.class);
//        stopService(playerIntent);
//
//        Intent playerIntentVideo = new Intent(this, MediaPlayerServiceVideo.class);
//        stopService(playerIntentVideo);
        if (mAdView != null) {
            mAdView.destroy();
        }
        if (mInterstitial != null) {
            mInterstitial = null;
        }
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
            isActivityIsVisible = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
            isActivityIsVisible = true;
        }
    }

    private void setupShare() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        listPref = prefs.getString(getString(R.string.hardware_key), getString(R.string.automatic_value));

        builder = new StringBuilder();
        builder.append("List preference: " + listPref);
        if (listPref.equals(getString(R.string.automatic_value))) {
            System.out.println(getString(R.string.automatic_value));
        } else if (listPref.equals(getString(R.string.disable_value))) {
            System.out.println(getString(R.string.disable_value));
        } else if (listPref.equals(getString(R.string.decoding_value))) {
            System.out.println(getString(R.string.decoding_value));
        } else if (listPref.equals(getString(R.string.full_value))) {
            System.out.println(getString(R.string.full_value));
        }
//        textView.setText(builder.toString());
        prefs.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.hardware_key))) {
            if (listPref.equals(getString(R.string.automatic_value))) {
                System.out.println(getString(R.string.automatic_value));
            } else if (listPref.equals(getString(R.string.disable_value))) {
                System.out.println(getString(R.string.disable_value));
            } else if (listPref.equals(getString(R.string.decoding_value))) {
                System.out.println(getString(R.string.decoding_value));
            } else if (listPref.equals(getString(R.string.full_value))) {
                System.out.println(getString(R.string.full_value));
            }
        }
    }


    public class MyCustomAdapter extends ArrayAdapter<String> {

        public MyCustomAdapter(Context context, int textViewResourceId,
                               String[] objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_items, parent, false);
            TextView label = (TextView) row.findViewById(R.id.weekofday);
            label.setText(DayOfWeek[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.icon);

            if (DayOfWeek[position] == "Videos") {
                icon.setImageResource(R.drawable.ic_video_library_black_24dp);
            } else if (DayOfWeek[position] == "Audios") {
                icon.setImageResource(R.drawable.ic_headset_black_24dp);
            } else {
                icon.setImageResource(R.drawable.ic_library_music_black_24dp);
            }
            return row;
        }
    }
}
