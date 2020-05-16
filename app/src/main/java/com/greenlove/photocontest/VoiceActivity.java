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

public class VoiceActivity extends AppCompatActivity {
    private InterstitialAd vinterstitialAd;
    private AdView vadView;

    private PDFView voicepdfView;
    private ZoomControls voicezoomControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        AudienceNetworkAds.initialize(this);
        fdIni();
        fbbannerad();

        voicepdfView = findViewById(R.id.voice_pdfViewId);

        voicepdfView.fromAsset("voice.pdf").load();

        voicezoomControls = findViewById(R.id.voice_zoom);

        zoom();
    }
    //fb_ins_ds starts
    public void fdIni() {
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

        vinterstitialAd = new InterstitialAd(this, getString(R.string.fb_instanstial_ads));

        vinterstitialAd.loadAd();
        vinterstitialAd.setAdListener(new InterstitialAdListener() {
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
                vinterstitialAd.show();
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
        vadView = new AdView(this, "320260812298566_320266975631283", AdSize.BANNER_HEIGHT_90);
        LinearLayout adContainer = findViewById(R.id.voice_banner_container);
        adContainer.addView(vadView);
        vadView.loadAd();
    }
    //fb banner ads ends

    //Zoom Method start
    public void zoom(){
        voicezoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = voicepdfView.getScaleX();
                float y = voicepdfView.getScaleY();

                if (x<3 && y<3){
                    voicepdfView.setScaleX(x+1);
                    voicepdfView.setScaleY(y+1);
                }

            }
        });

        voicezoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = voicepdfView.getScaleX();
                float y = voicepdfView.getScaleY();

                if (x>1 && y>1){
                    voicepdfView.setScaleX(x-1);
                    voicepdfView.setScaleY(y-1);
                }

            }
        });

    }
    //Zoom Method ends
}
