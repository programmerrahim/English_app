package com.greenlove.photocontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.github.barteksc.pdfviewer.PDFView;

public class TenseActivity extends AppCompatActivity {
    private InterstitialAd tinterstitialAd;
    private AdView tadView;

    private PDFView tensepdfView;
    private ZoomControls tensezoomControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tense);

        AudienceNetworkAds.initialize(this);
        fdIni();
        fbbannerad();

        tensepdfView = findViewById(R.id.tense_pdfViewId);

        tensepdfView.fromAsset("tense.pdf").load();

        tensezoomControls = findViewById(R.id.tense_zoom);

        zoom();
    }

    //fb_ins_ds starts
    public void fdIni() {
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

        tinterstitialAd = new InterstitialAd(this, getString(R.string.fb_instanstial_ads));

        tinterstitialAd.loadAd();
        tinterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                tinterstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
    }

    //fb_ins_ds ends

    //fb banner ads starts
    public void fbbannerad(){
        tadView = new AdView(this, "320260812298566_320266975631283", AdSize.BANNER_HEIGHT_90);
        LinearLayout adContainer = findViewById(R.id.tense_banner_container);
        adContainer.addView(tadView);
        tadView.loadAd();
    }
    //fb banner ads ends

    //Zoom Method start
    public void zoom(){
        tensezoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = tensepdfView.getScaleX();
                float y = tensepdfView.getScaleY();

                if (x<3 && y<3){
                    tensepdfView.setScaleX(x+1);
                    tensepdfView.setScaleY(y+1);
                }

            }
        });

        tensezoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = tensepdfView.getScaleX();
                float y = tensepdfView.getScaleY();

                if (x>1 && y>1){
                    tensepdfView.setScaleX(x-1);
                    tensepdfView.setScaleY(y-1);
                }

            }
        });

    }
    //Zoom Method ends
}
