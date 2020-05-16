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

public class ArticlesActivity extends AppCompatActivity {
    private InterstitialAd ainterstitialAd;
    private AdView aadView;

    private PDFView articlespdfView;
    private ZoomControls articleszoomControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        AudienceNetworkAds.initialize(this);
        fdIni();
        fbbannerad();

        articlespdfView = findViewById(R.id.articles_pdfViewId);

        articlespdfView.fromAsset("articles.pdf").load();

        articleszoomControls = findViewById(R.id.articles_zoom);

        zoom();
    }
    //fb_ins_ds starts
    public void fdIni() {
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

        ainterstitialAd = new InterstitialAd(this, getString(R.string.fb_instanstial_ads));

        ainterstitialAd.loadAd();
        ainterstitialAd.setAdListener(new InterstitialAdListener() {
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
                ainterstitialAd.show();
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
        aadView = new AdView(this, "320260812298566_320266975631283", AdSize.BANNER_HEIGHT_90);
        LinearLayout adContainer = findViewById(R.id.articles_banner_container);
        adContainer.addView(aadView);
        aadView.loadAd();
    }
    //fb banner ads ends

    //Zoom Method start
    public void zoom(){
        articleszoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = articlespdfView.getScaleX();
                float y = articlespdfView.getScaleY();

                if (x<3 && y<3){
                    articlespdfView.setScaleX(x+1);
                    articlespdfView.setScaleY(y+1);
                }

            }
        });

        articleszoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = articlespdfView.getScaleX();
                float y = articlespdfView.getScaleY();

                if (x>1 && y>1){
                    articlespdfView.setScaleX(x-1);
                    articlespdfView.setScaleY(y-1);
                }

            }
        });

    }
    //Zoom Method ends
}
