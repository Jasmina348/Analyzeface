package analyzeface.inspiringlab.com.np.analyzeface;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class MySharedPreferences {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Sharedpref file name
    private static final String PREFS_FILE_NAME = "AnalyzeFacePrefs";
    private static final String PREFERENCE_VERSION = "PREFERENCE_VERSION";

    private static int APP_VERSION_CODE = 1; /*change this each time you want to clear preference in updated app.*/
    private static final String ENCODED_IMAGE = "encoded_image";
    private static final int MODE_PRIVATE = 0;

    public MySharedPreferences(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE);
        editor = pref.edit();

        // clear old version config
        if (pref.getInt(PREFERENCE_VERSION, 0) != APP_VERSION_CODE) {
            pref.edit().clear().apply();
            pref.edit().putInt(PREFERENCE_VERSION, APP_VERSION_CODE).apply();
        }
    }

    public void setEncodedImage(String encodedImage) {
        // Storing name in pref
        editor.putString(ENCODED_IMAGE, encodedImage);

        // commit changes
        editor.apply();
    }

    public String getEncodedImage(){
        return pref.getString(ENCODED_IMAGE,"");
    }

    public void setRemoteConfig(ArrayList<HashMap> remoteConfigs) {
        Log.d("REMOTECONFIG", "set-sharedprefs");
        // Storing name in pref
        for(HashMap<String, String> remoteConfig : remoteConfigs){
            editor.putString(remoteConfig.get("key"), remoteConfig.get("value"));
        }
        // commit changes
        editor.apply();
    }

    public String getRemoteconfig(String key){
        return pref.getString(key,"");
    }
}
