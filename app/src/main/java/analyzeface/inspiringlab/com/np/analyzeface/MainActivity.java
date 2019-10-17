package analyzeface.inspiringlab.com.np.analyzeface;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int CAPTURE_REQUEST_CODE = 101;
    private static final int PICK_IMAGE = 102;

    //TextView scanText;
    ImageButton from_scan;
    ImageButton from_gallery;
    static ConstraintLayout activity_main;

    public final String APP_TAG = "MainActivity";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;

    ProgressDialog dialog;

    private AdView mAdView;
    NetworkChangeReceiver networkChangeReceiver;
    IntentFilter intentFilter;
    MySharedPreferences sharedPrefs;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private InterstitialAd mInterstitialAd;

    MySharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                //onLaunchCamera();
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();

        sharedPreferences = new MySharedPreferences(this);

        // Registering broadcast receiver for Network Connectivity
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

        setContentView(R.layout.activity_main);

        activity_main = findViewById(R.id.activity_main);

        from_scan = findViewById(R.id.scan_image);

        from_gallery = findViewById(R.id.from_gallery);

        sharedPrefs = new MySharedPreferences(this);

        from_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                MultiplePermissionsListener snackbarMultiplePermissionsListener =
                        SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                                .with(v, "Camera and audio access is needed to take pictures. Go to settings to enable the permissions.")
                                .withOpenSettingsButton("Settings")
                                .withCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onShown(Snackbar snackbar) {
                                        // Event handler for when the given Snackbar has been dismissed
                                    }
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        // Event handler for when the given Snackbar is visible
                                    }
                                })
                                .withDuration(Snackbar.LENGTH_LONG)
                                .build();

                MultiplePermissionsListener permissionsListener = new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            onLaunchCamera();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                };

                MultiplePermissionsListener compositePermissionsListener = new CompositeMultiplePermissionsListener(snackbarMultiplePermissionsListener, permissionsListener);

                Dexter.withActivity(MainActivity.this)
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(compositePermissionsListener).check();
            }
        });

        from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MultiplePermissionsListener snackbarMultiplePermissionsListener =
                        SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                                .with(v, "Storage access is needed to select images from Gallery. Go to settings to enable the permissions.")
                                .withOpenSettingsButton("Settings")
                                .withCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onShown(Snackbar snackbar) {
                                        // Event handler for when the given Snackbar has been dismissed
                                    }
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        // Event handler for when the given Snackbar is visible
                                    }
                                })
                                .withDuration(Snackbar.LENGTH_LONG)
                                .build();

                MultiplePermissionsListener permissionsListener = new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            openGallery();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                };

                MultiplePermissionsListener compositePermissionsListener = new CompositeMultiplePermissionsListener(snackbarMultiplePermissionsListener, permissionsListener);


                Dexter.withActivity(MainActivity.this)
                        .withPermissions(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(compositePermissionsListener).check();
            }
        });


        // Get Remote Config instance.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // [START enable_dev_mode]
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        // [END enable_dev_mode]
        // [START set_default_values]
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        // [END set_default_values]

        // Fetch firebase remote config
        fetchRemoteConfig();

        Log.d("REMOTECONFIG", sharedPreferences.getRemoteconfig(Config.ADMOB_APP_ID));

        // cloud messaging
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
    }

    /**
     * Fetch a welcome message from the Remote Config service, and then activate it.
     */
    private void fetchRemoteConfig() {
        Log.d("REMOTECONFIG", "fetching start...");
        long cacheExpiration = 10800; // 3 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                            Log.d("REMOTECONFIG", "fetch Succeeded.");
                            setRemoteConfig();
                            showMobileAds();
                        } else {
                            Log.d("REMOTECONFIG", "Fetch Failed");
                            setRemoteConfig();
                        }
                    }
                });
        // [END fetch_config_with_callback]
    }

    public void showMobileAds(){
        MobileAds.initialize(this, sharedPreferences.getRemoteconfig(Config.ADMOB_APP_ID));

        RelativeLayout rl_banner_ad = findViewById(R.id.home_banner_ad_view);
        AdView mAdView = new AdView(MainActivity.this);
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
                Log.e("ADS", "Error code:" + errorCode);
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

        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    public void setRemoteConfig(){
        // [START get_config_values]
        String admobAppId = mFirebaseRemoteConfig.getString(Config.ADMOB_APP_ID);
        String bannerAdId = mFirebaseRemoteConfig.getString(Config.BANNER_AD_ID);
        String interestialAdId = mFirebaseRemoteConfig.getString(Config.INTERESTITIAL_AD_ID);
        String testDeviceId1 = mFirebaseRemoteConfig.getString(Config.TEST_DEVICE_ID_1);
        String testDeviceId2 = mFirebaseRemoteConfig.getString(Config.TEST_DEVICE_ID_2);
        String testDeviceId3 = mFirebaseRemoteConfig.getString(Config.TEST_DEVICE_ID_3);
        String testDeviceId4 = mFirebaseRemoteConfig.getString(Config.TEST_DEVICE_ID_4);
        String testDeviceId5 = mFirebaseRemoteConfig.getString(Config.TEST_DEVICE_ID_5);
        // [END get_config_values]

        ArrayList<HashMap> remoteConfigs = new ArrayList();
        HashMap<String, String> remoteConfig;
        remoteConfig = new HashMap<>();
        remoteConfig.put("key", Config.ADMOB_APP_ID);
        remoteConfig.put("value", admobAppId);
        remoteConfigs.add(remoteConfig);
        remoteConfig = new HashMap<>();
        remoteConfig.put("key", Config.BANNER_AD_ID);
        remoteConfig.put("value", bannerAdId);
        remoteConfigs.add(remoteConfig);

        remoteConfig = new HashMap<>();
        remoteConfig.put("key", Config.INTERESTITIAL_AD_ID);
        remoteConfig.put("value", interestialAdId);
        remoteConfigs.add(remoteConfig);

        remoteConfig = new HashMap<>();
        remoteConfig.put("key", Config.TEST_DEVICE_ID_1);
        remoteConfig.put("value", testDeviceId1);
        remoteConfigs.add(remoteConfig);

        remoteConfig = new HashMap<>();
        remoteConfig.put("key", Config.TEST_DEVICE_ID_2);
        remoteConfig.put("value", testDeviceId2);
        remoteConfigs.add(remoteConfig);

        remoteConfig = new HashMap<>();
        remoteConfig.put("key", Config.TEST_DEVICE_ID_3);
        remoteConfig.put("value", testDeviceId3);
        remoteConfigs.add(remoteConfig);

        remoteConfig = new HashMap<>();
        remoteConfig.put("key", Config.TEST_DEVICE_ID_4);
        remoteConfig.put("value", testDeviceId4);
        remoteConfigs.add(remoteConfig);

        remoteConfig = new HashMap<>();
        remoteConfig.put("key", Config.TEST_DEVICE_ID_5);
        remoteConfig.put("value", testDeviceId5);
        remoteConfigs.add(remoteConfig);

        Log.d("REMOTECONFIG", remoteConfigs.toString());

        sharedPreferences.setRemoteConfig(remoteConfigs);
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(sharedPreferences.getRemoteconfig(Config.INTERESTITIAL_AD_ID));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d("ADS","lOADED..");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdClosed() {
                MainActivity.this.finish();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("ADS", "Ad did not load, interestial");
            goToNextLevel();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("91AF778964DCFCA6E818A2391E9BFB27")
                .addTestDevice("4E550AAAB797FF959C2B22B66548B3B2")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        File photoFile = getPhotoFileUri("photo.jpg");

        // wrap File object into a content provider
        Uri fileProvider;
        if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)) {
            fileProvider = FileProvider.getUriForFile(MainActivity.this, "analyzeface.inspiringlab.com.np.analyzeface.fileprovider", photoFile);
        }else {
            fileProvider = Uri.fromFile(photoFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                File photoFile = getPhotoFileUri("photo.jpg");
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Display display = getWindowManager(). getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                if(width >= height){
                    takenImage = Bitmap.createScaledBitmap(takenImage, width, height, false);
                }
                takenImage = getResizedBitmap(takenImage, 500);
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                //ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                //ivPreview.setImageBitmap(takenImage);
                Log.d("CAMERA", "IMAGE LOADED");
                uploadImage(convertIntoBytes(takenImage));
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "Picture wasn't taken!"+resultCode);
            }
        }

        if(requestCode == PICK_IMAGE){
            if(resultCode == RESULT_OK){

                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    uploadImage(convertIntoBytes(selectedImage));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }


            }else{
                Toast.makeText(this, "Could't select image!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * reduces the size of the image
     * param image bitmap received from camera
     * @param maxSize
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public static byte[] convertIntoBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "1mind_" + timeStamp + ".jpg";
        Log.d("IMAGEROOT:", Environment.getExternalStorageDirectory().toString());
        File photo = new File(Environment.getExternalStorageDirectory()+"/Pictures",  imageFileName);
        return photo;
    }

    private void uploadImage(byte[] imageBytes) {

        dialog = ProgressDialog.show(MainActivity.this, "",
                "Processing. Please wait...", true);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        String randomSalt = Long.toHexString(Double.doubleToLongBits(Math.random()));

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image_"+randomSalt+".jpg", requestFile);
        Call<ResponseModal> call = retrofitInterface.uploadImage(body);
        call.enqueue(new Callback<ResponseModal>() {
            @Override
            public void onResponse(Call<ResponseModal> call, retrofit2.Response<ResponseModal> response) {

                if (response.isSuccessful()) {

                    ResponseModal responseBody = response.body();
                    try{
                    dialog.dismiss();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Log.d(TAG, responseBody.getImage());
                    Intent resultIntent = new Intent(getApplicationContext(), ResultActivity.class);
                    //resultIntent.putExtra("encoded_image", responseBody.getImage());
                    sharedPrefs.setEncodedImage(responseBody.getImage());
                    startActivity(resultIntent);
                } else {

                    ResponseBody errorBody = response.errorBody();

                    Gson gson = new Gson();

                    try {
                        Log.e(TAG, errorBody.string());
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Upload failed")
                            .setMessage("Unable to upload image to the server. Please try again.")
                            .setCancelable(false)
                            .setNeutralButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Whatever...
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModal> call, Throwable t) {
                try{
                    dialog.dismiss();
                }catch(Exception e){
                    e.printStackTrace();
                }
                //mProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());

                new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Upload failed")
                    .setMessage("Unable to upload image to the server. Please try again.")
                    .setCancelable(false)
                    .setNeutralButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                        }
                    }).show();
            }
        });
    }

    public void shareThisApp(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        String messageBody = "I found this amazing Android app for object detection. " +
                "Check this out: https://inspiringlab.page.link/what-is-this-app";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, messageBody);
        startActivity(Intent.createChooser(share, "Share this app with friends"));
    }

    public void showRateDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                .setTitle("App rating")
                .setMessage("Please rate us and write your feedback in Google Play Store.")
                .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rateApp();
                    }
                })
                .setNegativeButton("CANCEL", null);
        builder.show();
    }

    /*
     * Start with rating the app
     * Determine if the Play Store is installed on the device
     *
     * */
    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);

        // hiding setting..
        MenuItem menu_rating = menu.findItem(R.id.action_rating);
        MenuItem menu_share_app = menu.findItem(R.id.action_share_app);
        MenuItem menu_about_us = menu.findItem(R.id.action_aboutus);
        MenuItem menu_license = menu.findItem(R.id.action_license);

        menu_share_app.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                shareThisApp();
                return true;
            }
        });

        menu_rating.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showRateDialog();
                return true;
            }
        });

        menu_about_us.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent aboutUsIntent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(aboutUsIntent);
                return false;
            }
        });

        menu_license.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent licenseIntent = new Intent(MainActivity.this, LicenseActivity.class);
                startActivity(licenseIntent);
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();


        try {
            if (networkChangeReceiver!=null) {
                unregisterReceiver(networkChangeReceiver);
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "On destroy networkChangeReceiver");
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {

        super.onPause();
        try {
            if (networkChangeReceiver!=null) {
                unregisterReceiver(networkChangeReceiver);
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "On destroy networkChangeReceiver");
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            registerReceiver(networkChangeReceiver, intentFilter);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        showInterstitial();
        super.onBackPressed();
    }

    public class NetworkChangeReceiver extends BroadcastReceiver
    {
        AlertDialog.Builder alert_builder;
        boolean isOnline = false;

        @Override
        public void onReceive(final Context context, final Intent intent)
        {
            try
            {
                if (isOnline(context)) {
                    if(isOnline) {
                        //displayNetworkActivityMessage(true);
                        isOnline = false;
                    }
                } else {
                    //displayNetworkActivityMessage(false);
                    alert_builder = new AlertDialog.Builder(MainActivity.this);
                    alert_builder.setTitle("Connection error");
                    alert_builder.setMessage("Unable to connect with the server. Check your internet connection and try again.");
                    alert_builder.setCancelable(false);
                    alert_builder.setNeutralButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Reload app
                                /*Intent mStartActivity = new Intent(context, MainActivity.class);
                                int mPendingIntentId = 123456;
                                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                System.exit(0);*/
                            //isOnline(context);
                            isOnline = true;
                            onReceive(context, intent);

                        }
                    }).show();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
