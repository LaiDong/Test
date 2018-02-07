package slideshow.lab411.com.slideshow.ui.passcode.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ld on 14/12/2017.
 */

public class SharedPrefsHelper {
    public static final String MY_PREFS = "MY_PREFS";
    public static final String PASS_CODE = "PASS_CODE";
    public static final String PASS_CODE_UI = "START_PASS_CODE";
    public static final String KEY_START = "KEY_START";

    SharedPreferences mSharedPreferences;

    public SharedPrefsHelper(Context context){
        mSharedPreferences = context.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
    }

    public void clear(){
        mSharedPreferences.edit().clear().apply();
    }

    public void putPassCode(String passcode){
        mSharedPreferences.edit().putString(PASS_CODE, passcode).apply();
    }

    public void putKeyStart(boolean start){
        mSharedPreferences.edit().putBoolean(KEY_START, start).apply();
    }

    public void putPassCodeUI(boolean start){
        mSharedPreferences.edit().putBoolean(PASS_CODE_UI, start).apply();
    }

    public boolean getBooleanPassCodeUI(){
        return mSharedPreferences.getBoolean(PASS_CODE_UI, false);
    }

    public boolean getKeyStart(){
        return mSharedPreferences.getBoolean(KEY_START, false);
    }

    public String getPassCode(){
        return mSharedPreferences.getString(PASS_CODE, null);
    }
}
