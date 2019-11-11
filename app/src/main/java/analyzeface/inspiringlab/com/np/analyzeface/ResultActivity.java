package analyzeface.inspiringlab.com.np.analyzeface;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import analyzeface.inspiringlab.com.np.analyzeface.model.AgeRange;
import analyzeface.inspiringlab.com.np.analyzeface.model.Emotion;
import analyzeface.inspiringlab.com.np.analyzeface.model.Face;
import analyzeface.inspiringlab.com.np.analyzeface.model.Feature;
import analyzeface.inspiringlab.com.np.analyzeface.model.MainResponse;

public class ResultActivity extends AppCompatActivity {

    public static final String TAG = ResultActivity.class.getSimpleName();

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    RecyclerViewAdapter recyclerViewAdapter;

    ImageView ivResultImage;
    MySharedPreferences sharedPreferences;
    AdView mAdView;
    RecyclerView recyclerView;
    Context context;
    ImageView mainImage;
    private Activity mActivity;
    private ProgressBar progressBar;
    private TextView tv_agerange;

    RelativeLayout rl_gender_male, rl_gender_female, rl_mustache, rl_no_mustache, rl_beard, rl_no_beard, rl_eye_opened, rl_eye_closed, rl_sunglass, rl_eyeglass, rl_no_eye_glass, rl_mouth_open, rl_mouth_close, rl_smiling, rl_not_smiling;
    TextView tv_calm_value, tv_angry_value, tv_sad_value, tv_confused_value, tv_happy_value, tv_surprised_value, tv_disgusted_value, tv_fear_value;

    ImageView ivOriginalImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_result);
        progressBar = findViewById(R.id.progress_bar);

        ivResultImage = findViewById(R.id.result_image);
        mainImage = findViewById(R.id.originalImage);
        recyclerView = findViewById(R.id.recyclerView);
        ivOriginalImage = findViewById(R.id.originalImage);
        tv_agerange = findViewById(R.id.tv_agerange);

        rl_gender_female = findViewById(R.id.rl_gender_female);
        rl_gender_male = findViewById(R.id.rl_gender_male);
        rl_mustache = findViewById(R.id.rl_mustache);
        rl_no_mustache = findViewById(R.id.rl_no_mustache);
        rl_beard = findViewById(R.id.rl_beard);
        rl_no_beard = findViewById(R.id.rl_no_beard);
        rl_eye_opened = findViewById(R.id.rl_eye_opened);
        rl_eye_closed = findViewById(R.id.rl_eye_closed);
        rl_sunglass = findViewById(R.id.rl_sunglass);
        rl_eyeglass = findViewById(R.id.rl_eye_glass);
        rl_no_eye_glass = findViewById(R.id.rl_no_eye_glass);
        rl_mouth_open = findViewById(R.id.rl_mouth_open);
        rl_mouth_close = findViewById(R.id.rl_mouth_closed);
        rl_smiling = findViewById(R.id.rl_smiling);
        rl_not_smiling = findViewById(R.id.rl_not_smiling);
        tv_calm_value = findViewById(R.id.tv_calm_value);
        tv_angry_value = findViewById(R.id.tv_angry_vlaue);
        tv_sad_value = findViewById(R.id.tv_sad_value);
        tv_confused_value = findViewById(R.id.tv_confused_value);
        tv_happy_value = findViewById(R.id.tv_happy_value);
        tv_surprised_value = findViewById(R.id.tv_surprised_value);
        tv_disgusted_value = findViewById(R.id.tv_disgusted_value);
        tv_fear_value = findViewById(R.id.tv_fear_value);


        //progressBar = findViewById(R.id.image_loading);
        //progressText = findViewById(R.id.progress_text);

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        sharedPreferences = new MySharedPreferences(this);


//        Intent resultIntent = getIntent();
//         MainResponse data = (MainResponse) resultIntent.putExtra("KEY",);
        // MainResponse data  = (MainResponse )mIntent.getSerializableExtra("data");

        //String url_name = getIntent().getExtras().getString("url_name");
        //String encodedImage = getIntent().getExtras().getString("encoded_image");
        String encodedImage = sharedPreferences.getEncodedImage();


        //Log.d(TAG, url_name);

        //byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        // new AsyncTaskLoadImage(imageView, progressBar, progressText).execute(url_name);

//        byte[] decodedString = Base64.decode(encodedImage,Base64.NO_WRAP);
//        InputStream inputStream  = new ByteArrayInputStream(decodedString);
//        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
//        imageView.setImageBitmap(bitmap);

        RelativeLayout rl_banner_ad = findViewById(R.id.result_banner_ad_view);
        progressBar.setVisibility(View.VISIBLE);
        AdView mAdView = new AdView(ResultActivity.this);
        mAdView.setAdSize(AdSize.FULL_BANNER);
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


        loadValueFromIntent();

    }


    private void loadValueFromIntent() {

        String response = getIntent().getExtras().getString("analysis_result");
        try {
            JSONObject res = new JSONObject(response);
            Log.d(TAG, "loadValueFromIntent: " + response);
            JSONObject data = new JSONObject(res.getString("DATA"));


            String imageName = data.getString("image_id");
            MainResponse mainResponse = new MainResponse();
            mainResponse.setImage(imageName);
            Log.d(TAG, "main image " + imageName);

            JSONObject details = data.getJSONObject("details");
            JSONArray faces = details.getJSONArray("faces"); // list of faces

            ArrayList<Face> listFace = new ArrayList<>(); // parse list of faces
            Face currentFace;
            JSONObject object;
            JSONArray array;
            Feature features;


            for (int i = 0; i < faces.length(); i++) {
                JSONObject face = faces.getJSONObject(i);
                Iterator<String> keys = face.keys();

                currentFace = new Face();

                while (keys.hasNext()) {
                    /**
                     * key value pairs
                     * distinct for emotion, image and age_range
                     */
                    object = new JSONObject();
                    array = new JSONArray();
                    features = new Feature();

                    String key = keys.next();

                    if (key.equalsIgnoreCase("Emotions")) {
                        array = face.getJSONArray(key);
                    } else {
                        if (!key.equalsIgnoreCase("Image")) {
                            object = face.getJSONObject(key);
                        }
                    }
                    if (key.equalsIgnoreCase("Image")) {
                        currentFace.setImage(face.getString(key));
                    } else if (key.equalsIgnoreCase("Age_range")) {
                        currentFace.setAgeRange(new AgeRange(object.getInt("Low"), object.getInt("High")));
                    } else if (key.equalsIgnoreCase("Emotions")) {
                        ArrayList<Emotion> emotionList = new ArrayList<>();
//                        int max = -1;
//                        String emotion = "";
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject o = array.getJSONObject(j);
//                            if(o.getInt("Confidence") > max) {
//                                max = o.getInt("Confidence");
//                                emotion = o.getString("Type");
//                            }
//
//                           Log.e("Max", "" + max);
                            Emotion f = new Emotion();
                            f.setType(o.getString("Type"));
                            f.setConfidence(o.getInt("Confidence"));

                            emotionList.add(f);


                        }
//                        Emotion f = new Emotion();
//                        f.setType(emotion);
//                        f.setConfidence(max);
//
//                        emotionList.add(f);

                        currentFace.setEmotion(emotionList);
                    } else {
                        // add feature to feature list
                        features.setFeature(key);
                        features.setConfidence(object.getDouble("Confidence"));
                        features.setValue(String.valueOf(object.get("Value")));
                        features.setName(String.valueOf(object.get("Name")));
                        currentFace.getFeatureList().add(features);
                    }

                }

                listFace.add(currentFace);
            }
            Glide.with(this).load(Config.IMAGE_URL + mainResponse.getImage()).into(ivOriginalImage);

            mainResponse.setFaces(listFace);
            Log.d(TAG, "loadValueFromIntent: main faces size" + mainResponse.getFaces().size());
            recyclerViewAdapter = new RecyclerViewAdapter(ResultActivity.this, mainResponse.getFaces());
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setUpFacesInfromation(Face face) {
        Glide.with(this).load(Config.IMAGE_URL + face.getImage()).into(ivResultImage);
        Log.d(TAG, "face image" + Config.IMAGE_URL + face.getImage());
        tv_agerange.setText(face.getAgeRange().getLow() + "-" + face.getAgeRange().getHigh());

        ArrayList<Feature> featureList = face.getFeatureList();

        for (Feature feature : featureList) {
            if (feature.getFeature().equalsIgnoreCase("Gender")) {
                String gender = feature.getValue();
                if (gender.equalsIgnoreCase("male")) {
                    rl_gender_male.setVisibility(View.VISIBLE);
                    rl_gender_female.setVisibility(View.GONE);
                } else {
                    rl_gender_male.setVisibility(View.GONE);
                    rl_gender_female.setVisibility(View.VISIBLE);
                }
            }
            if (feature.getFeature().equalsIgnoreCase("Mustache")) {
                String mustache = feature.getValue();
                if (mustache.equalsIgnoreCase("true")) {
                    rl_mustache.setVisibility(View.VISIBLE);
                    rl_no_mustache.setVisibility(View.GONE);
                } else {
                    rl_mustache.setVisibility(View.GONE);
                    rl_no_mustache.setVisibility(View.VISIBLE);
                }
            }
            if (feature.getFeature().equalsIgnoreCase("Beard")) {
                String beard = feature.getValue();
                if (beard.equalsIgnoreCase("true")) {
                    rl_beard.setVisibility(View.VISIBLE);
                    rl_no_beard.setVisibility(View.GONE);
                } else {
                    rl_beard.setVisibility(View.GONE);
                    rl_no_beard.setVisibility(View.VISIBLE);
                }
            }
            if (feature.getFeature().equalsIgnoreCase("Eyes_open")) {
                String eyes_open = feature.getValue();
                if (eyes_open.equalsIgnoreCase("true")) {
                    rl_eye_opened.setVisibility(View.VISIBLE);
                    rl_eye_closed.setVisibility(View.GONE);
                } else {
                    rl_eye_opened.setVisibility(View.GONE);
                    rl_eye_closed.setVisibility(View.VISIBLE);
                }
            }
            if (feature.getFeature().equalsIgnoreCase("Sunglasses")) {
                String sunglasses = feature.getValue();
                if (sunglasses.equalsIgnoreCase("true")) {
                    rl_sunglass.setVisibility(View.VISIBLE);
                    rl_eyeglass.setVisibility(View.GONE);
                    rl_no_eye_glass.setVisibility(View.GONE);
                }
            }
            if (feature.getFeature().equalsIgnoreCase("Eyeglasses")) {
                String eyeglasses = feature.getValue();
                if (eyeglasses.equalsIgnoreCase("true")) {
                    rl_eyeglass.setVisibility(View.VISIBLE);
                    rl_sunglass.setVisibility(View.GONE);
                    rl_no_eye_glass.setVisibility(View.GONE);
                } else {
                    rl_eyeglass.setVisibility(View.GONE);
                    rl_sunglass.setVisibility(View.GONE);
                    rl_no_eye_glass.setVisibility(View.VISIBLE);

                }
            }
            if (feature.getFeature().equalsIgnoreCase("Mouth_open")) {
                String mouth_open = feature.getValue();
                if (mouth_open.equalsIgnoreCase("true")) {
                    rl_mouth_open.setVisibility(View.VISIBLE);
                    rl_mouth_close.setVisibility(View.GONE);
                } else {
                    rl_mouth_open.setVisibility(View.GONE);
                    rl_mouth_close.setVisibility(View.VISIBLE);
                }
            }
            if (feature.getFeature().equalsIgnoreCase("Smile")) {
                String mouth_open = feature.getValue();
                if (mouth_open.equalsIgnoreCase("true")) {
                    rl_smiling.setVisibility(View.VISIBLE);
                    rl_not_smiling.setVisibility(View.GONE);
                } else {
                    rl_smiling.setVisibility(View.GONE);
                    rl_not_smiling.setVisibility(View.VISIBLE);
                }
            }


        }

    }

    public void setUpEmotionInformation(Face face) {
        ArrayList<Emotion> emotionList = face.getEmotion();
        for (Emotion emotion : emotionList) {
            if (emotion.getType().equalsIgnoreCase("CALM")) {
                int calm = emotion.getConfidence();
                tv_calm_value.setText(calm +"%");
            }

            if (emotion.getType().equalsIgnoreCase("ANGRY")) {
                int angry = emotion.getConfidence();
                tv_angry_value.setText(angry+"%");
            }
            if (emotion.getType().equalsIgnoreCase("SAD")) {
                int sad = emotion.getConfidence();
                tv_sad_value.setText(sad+"%");
            }
            if (emotion.getType().equalsIgnoreCase("CONFUSED")) {
                int confused = emotion.getConfidence();
                tv_confused_value.setText(confused+"%");
            }
            if (emotion.getType().equalsIgnoreCase("HAPPY")) {
                int happy = emotion.getConfidence();
                tv_happy_value.setText(happy+"%");
            }
            if (emotion.getType().equalsIgnoreCase("SURPRISED")) {
                int surprised = emotion.getConfidence();
                tv_surprised_value.setText(surprised+"%");
            }
            if (emotion.getType().equalsIgnoreCase("DISGUSTED")) {
                int disgusted = emotion.getConfidence();
                tv_angry_value.setText(disgusted+"%");
            }
            if (emotion.getType().equalsIgnoreCase("FEAR")) {
                int fear = emotion.getConfidence();
                tv_fear_value.setText(fear+"%");
            }

        }


    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    private void showDownloadNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round) // notification icon
                .setContentTitle("Download successful!") // title for notification
                .setContentText("Click to open gallery") // message for notification
                .setAutoCancel(true); // clear notification after click


        Intent intent = new Intent();
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        File root = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ),
                "WhatIsThis"
        );
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.withAppendedPath(Uri.fromFile(root), "/"), "image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent);

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }


    private void saveImage() {
        Log.d("SAVEIMAGE", "called");
        ivResultImage.buildDrawingCache();
        Bitmap bmp = ivResultImage.getDrawingCache();

        OutputStream fOut = null;
        File root = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ),
                "AnalyzeFace"
        );

        String randName = "WIT_" + Long.toHexString(Double.doubleToLongBits(Math.random())) + ".jpg";

        //Uri outputFileUri;
        try {
//            File root = new File(Environment.getExternalStorageDirectory()
//                    + File.separator + "Pictures" + File.separator + "WhatIsThis" + File.separator);
//
            if (!root.exists())
                root.mkdirs();

            File sdImageMainDirectory = new File(root, randName);
            //outputFileUri = Uri.fromFile(sdImageMainDirectory);
            fOut = new FileOutputStream(sdImageMainDirectory);
        } catch (Exception e) {
            Toast.makeText(this, "Error occured. Please try again later.",
                    Toast.LENGTH_SHORT).show();
        }
        try {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            Toast.makeText(this, "Image saved successfully!", Toast.LENGTH_SHORT).show();

            // refresh gallery
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(root, randName);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);

            // Show notification
            //showDownloadNotification();

        } catch (Exception e) {
            Toast.makeText(this, "Couldn't save image. Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private String rotateImage(int degree, String imagePath) {

        if (degree <= 0) {
            return imagePath;
        }
        try {
            Bitmap b = BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            if (b.getWidth() > b.getHeight()) {
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else if (imageType.equalsIgnoreCase("jpeg") || imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            ivResultImage.setScaleX(mScaleFactor);
            //scrollView.setScaleX(mScaleFactor);
            ivResultImage.setScaleY(mScaleFactor);
            //scrollView.setScaleY(mScaleFactor);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.m_save_button:
                saveImage();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
