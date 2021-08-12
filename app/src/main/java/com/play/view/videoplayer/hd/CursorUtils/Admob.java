package com.play.view.videoplayer.hd.CursorUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.play.view.videoplayer.hd.R;
import com.play.view.videoplayer.hd.VideoFolder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class Admob extends Application {

    public static InterstitialAd mInterstitial; // Interstital
    private static AdView mAdView; // banner
    private static AdRequest adRequest;

    public static void createLoadInterstitial(final Context context, View view) {

        mInterstitial = new InterstitialAd(context);
        mInterstitial.setAdUnitId(context.getResources().getString(
                R.string.intersitial_as_unit_id));
        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub
                showInterstitial(context);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // TODO Auto-generated method stub
                doFunc(context);
            }

            @Override
            public void onAdOpened() {
                // TODO Auto-generated method stub
                super.onAdOpened();

            }

            @Override
            public void onAdClosed() {
                // TODO Auto-generated method stub
                doFunc(context);
            }

            @Override
            public void onAdLeftApplication() {
                // TODO Auto-generated method stub
                // Called when an ad leaves the app (for example, to go to the
                // browser).

                super.onAdLeftApplication();
            }

        });

        loadInterstitial();


    }

    public static void doFunc(Context activity) {
        /* Create an Intent that will start the Menu-Activity. */
        Intent mainIntent = new Intent(activity, VideoFolder.class);
        activity.startActivity(mainIntent);
        ((Activity) activity).finish();
    }

    public static void loadInterstitial() {

        mInterstitial.loadAd(new AdRequest.Builder()
//                .addTestDevice("8B3B217C5524B71E3E3CBA2A407F395E")
                .build());
    }

    public static void showInterstitial(Context context) {
        if (mInterstitial.isLoaded()) {
            mInterstitial.show();
        } else {
            doFunc(context);
        }
    }

    public static void createLoadBanner(final Context context, View view) {
        mAdView = (AdView) ((Activity) context).findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
//                .addTestDevice("8B3B217C5524B71E3E3CBA2A407F395E")
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }

}