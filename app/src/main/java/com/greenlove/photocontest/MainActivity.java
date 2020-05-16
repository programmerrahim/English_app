package com.greenlove.photocontest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;

    private AdView adView;

    private RecyclerView displayRecyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter madapter;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;


    //-------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AudienceNetworkAds.initialize(this);


        firebaseFirestore = FirebaseFirestore.getInstance();

        displayRecyclerView = findViewById(R.id.displayRecyclerviewId);
        displayRecyclerView.setHasFixedSize(true);
        displayRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        fbbannerad();


        mToolbar = findViewById(R.id.main_page_toolbar_Id);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        drawerLayout = findViewById(R.id.drawerLayoutId);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigationId);

        View navView = navigationView.inflateHeaderView(R.layout.header_layout);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });

        Query query = firebaseFirestore.collection("Products").orderBy("date", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<DisplayModel> options = new FirestoreRecyclerOptions.Builder<DisplayModel>()
                .setQuery(query, DisplayModel.class)
                .build();

        madapter = new FirestoreRecyclerAdapter<DisplayModel, MainActivity.ProductsViewHolder>(options) {
            @NonNull
            @Override
            public MainActivity.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_layout, parent, false);
                return new MainActivity.ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainActivity.ProductsViewHolder holder, int position, @NonNull DisplayModel model) {
                holder.nameText.setText(model.getName());
                holder.description.setText(model.getDescription());


                boolean isExpanded = model.isExpanded();
                holder.expRelativeLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


            }
        };
        displayRecyclerView.setAdapter(madapter);

        checkConnection();

    }
    //-------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Exit?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog progressBar = new ProgressDialog(MainActivity.this);
                    progressBar.setMessage("Welcome back again");
                    progressBar.show();


                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progressBar.dismiss();

                        }
                    }, 1000);

                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                    finish();
                }
            }).show();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView description;
        private Button moreButton;
        private RelativeLayout expRelativeLayout;


        private Button collapse;


        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.sample_nameId);
            description = itemView.findViewById(R.id.sample_descriptionId);
            moreButton = itemView.findViewById(R.id.sample_moreId);
            expRelativeLayout = itemView.findViewById(R.id.expandable_relative_layout);


            collapse = itemView.findViewById(R.id.sample_collapseId);


            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expRelativeLayout.setVisibility(View.VISIBLE);
                    fdIni();

                }
            });
            collapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expRelativeLayout.setVisibility(View.GONE);

                }
            });
        }
    }


    //onOptionsItemSelected starts
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //onOptionsItemSelected ends


    //UserMenuselector Starts
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_sentence_Id:
                Intent sIntent = new Intent(MainActivity.this, SentenceActivity.class);
                startActivity(sIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

            case R.id.nav_articles_Id:
                Intent aintent = new Intent(MainActivity.this, ArticlesActivity.class);
                startActivity(aintent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

            case R.id.nav_tense_Id:
                Intent tintent = new Intent(MainActivity.this, TenseActivity.class);
                startActivity(tintent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case R.id.nav_preposition_Id:
                Intent pintent = new Intent(MainActivity.this, PrepositionActivity.class);
                startActivity(pintent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case R.id.nav_voice_change_Id:
                Intent vintent = new Intent(MainActivity.this, VoiceActivity.class);
                startActivity(vintent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;

            case R.id.nav_logout_Id:
                Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(adminIntent);

                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
        }
    }

    //UserMenuselector ends

    @Override
    protected void onStop() {
        super.onStop();
        madapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        madapter.startListening();
    }

    //Check Connection Start
    public void checkConnection() {

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null != activeNetwork) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(this, "No Data Connections", Toast.LENGTH_LONG).show();
        }
    }
    //Check Connection Ends

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    //fb_ins_ds starts
    public void fdIni() {
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

        interstitialAd = new InterstitialAd(this, getString(R.string.fb_instanstial_ads));

        interstitialAd.loadAd();
        interstitialAd.setAdListener(new InterstitialAdListener() {
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
                interstitialAd.show();
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
        adView = new AdView(this, "320260812298566_320266975631283", AdSize.BANNER_HEIGHT_90);
        LinearLayout adContainer = findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();
    }
    //fb banner ads ends

}
