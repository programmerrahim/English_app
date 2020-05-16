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

public class PrepositionActivity extends AppCompatActivity {
    private InterstitialAd pinterstitialAd;
    private AdView padView;

    private PDFView prepositionpdfView;
    private ZoomControls prepositionzoomControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preposition);

        AudienceNetworkAds.initialize(this);
        fdIni();
        fbbannerad();

        prepositionpdfView = findViewById(R.id.preposition_pdfViewId);

        prepositionpdfView.fromAsset("prepositions.pdf").load();

        prepositionzoomControls = findViewById(R.id.preposition_zoom);

        zoom();
    }

    //fb_ins_ds starts
    public void fdIni() {
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

        pinterstitialAd = new InterstitialAd(this, getString(R.string.fb_instanstial_ads));

        pinterstitialAd.loadAd();
        pinterstitialAd.setAdListener(new InterstitialAdListener() {
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
                pinterstitialAd.show();
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
        padView = new AdView(this, "320260812298566_320266975631283", AdSize.BANNER_HEIGHT_90);
        LinearLayout adContainer = findViewById(R.id.preposition_banner_container);
        adContainer.addView(padView);
        padView.loadAd();
    }
    //fb banner ads ends

    //Zoom Method start
    public void zoom(){
        prepositionzoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = prepositionpdfView.getScaleX();
                float y = prepositionpdfView.getScaleY();

                if (x<3 && y<3){
                    prepositionpdfView.setScaleX(x+1);
                    prepositionpdfView.setScaleY(y+1);
                }

            }
        });

        prepositionzoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = prepositionpdfView.getScaleX();
                float y = prepositionpdfView.getScaleY();

                if (x>1 && y>1){
                    prepositionpdfView.setScaleX(x-1);
                    prepositionpdfView.setScaleY(y-1);
                }

            }
        });

    }
    //Zoom Method ends
}
