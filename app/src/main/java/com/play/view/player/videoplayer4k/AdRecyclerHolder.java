package com.play.view.player.videoplayer4k;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AdRecyclerHolder extends AppCompatActivity{
    private AdView mAdView;
    private AdRequest adRequest, ar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adview_item);

        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
//                .addTestDevice("DF3AC08CDD630177F1719EF8950F138D")
                .build();
        mAdView.loadAd(adRequest);

    }
}
