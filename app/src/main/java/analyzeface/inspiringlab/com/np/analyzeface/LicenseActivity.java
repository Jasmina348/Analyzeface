package analyzeface.inspiringlab.com.np.analyzeface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class LicenseActivity extends AppCompatActivity {

    MySharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_license);
        setTitle("License Used");

        sharedPreferences = new MySharedPreferences(this);
        RelativeLayout rl_banner_ad = findViewById(R.id.license_banner_ad_view);
        AdView mAdView = new AdView(LicenseActivity.this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(sharedPreferences.getRemoteconfig(Config.BANNER_AD_ID));

        // Load an ad into the AdMob banner view.
        rl_banner_ad.addView(mAdView);

        //adView.setAdUnitId(getString(R.string.banner_ad_unit_id_test));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(sharedPreferences.getRemoteconfig(Config.TEST_DEVICE_ID_1))
                .addTestDevice(sharedPreferences.getRemoteconfig(Config.TEST_DEVICE_ID_2))
                .addTestDevice(sharedPreferences.getRemoteconfig(Config.TEST_DEVICE_ID_3))
                .addTestDevice(sharedPreferences.getRemoteconfig(Config.TEST_DEVICE_ID_4))
                .addTestDevice(sharedPreferences.getRemoteconfig(Config.TEST_DEVICE_ID_5))
                .build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
