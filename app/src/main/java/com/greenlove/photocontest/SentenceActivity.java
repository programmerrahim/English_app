package com.greenlove.photocontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.github.barteksc.pdfviewer.PDFView;

public class SentenceActivity extends AppCompatActivity {
    private InterstitialAd sinterstitialAd;
    private AdView sadView;

   private PDFView pdfView;
    private ZoomControls zoomControls;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);
        pdfView = findViewById(R.id.sentence_pdfView);

        pdfView.fromAsset("sentence.pdf").load();

        zoomControls = findViewById(R.id.zoom);

        zoom();



        AudienceNetworkAds.initialize(this);
        fdIni();
        fbbannerad();
    }


    //fb_ins_ds starts
    public void fdIni() {
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

        sinterstitialAd = new InterstitialAd(this, getString(R.string.fb_instanstial_ads));

        sinterstitialAd.loadAd();
        sinterstitialAd.setAdListener(new InterstitialAdListener() {
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
                sinterstitialAd.show();
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
        sadView = new AdView(this, "320260812298566_320266975631283", AdSize.BANNER_HEIGHT_90);
        LinearLayout adContainer = findViewById(R.id.sentence_banner_container);
        adContainer.addView(sadView);
        sadView.loadAd();
    }
    //fb banner ads ends

    //Zoom Method start
    public void zoom(){
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = pdfView.getScaleX();
                float y = pdfView.getScaleY();

                if (x<3 && y<3){
                    pdfView.setScaleX(x+1);
                    pdfView.setScaleY(y+1);
                }

            }
        });

        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = pdfView.getScaleX();
                float y = pdfView.getScaleY();

                if (x>1 && y>1){
                    pdfView.setScaleX(x-1);
                    pdfView.setScaleY(y-1);
                }

            }
        });

    }
    //Zoom Method ends


}
