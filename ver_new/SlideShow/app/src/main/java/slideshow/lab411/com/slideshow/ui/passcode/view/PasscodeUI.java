package slideshow.lab411.com.slideshow.ui.passcode.view;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.ServiceUtils;
import slideshow.lab411.com.slideshow.ui.passcode.model.SharedPrefsHelper;

/**
 * Created by ld on 14/12/2017.
 */

public class PasscodeUI extends PreferenceActivity{
    public static final String PASSCODE_LOCK_PREF = "passcode_pref";
    public static final String CHANGE_PASSCODE_PREF = "change_passcode_pref";
    private SharedPrefsHelper mSharedPrefsHelper;
    SwitchPreference passcode_lock;
    Preference change_passcode;
    boolean mFinish = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_preference);

        setupToolbar();
        mSharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());

        passcode_lock = (SwitchPreference) findPreference(PASSCODE_LOCK_PREF);
        change_passcode = findPreference(CHANGE_PASSCODE_PREF);

        passcode_lock.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object isLock) {
                boolean is_Passcode_Lock = (Boolean) isLock;
                if(is_Passcode_Lock){
                    gotoSetPasscodeActivity();
                }else{
                    mSharedPrefsHelper.clear();
                    change_passcode.setEnabled(false);
                }
                return true;
            }
        });


        change_passcode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                gotoSetPasscodeActivity();
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSharedPrefsHelper.getPassCode() != null){
            change_passcode.setEnabled(true);
            passcode_lock.setChecked(true);
        }else{
            change_passcode.setEnabled(false);
            passcode_lock.setChecked(false);
        }

        SharedPrefsHelper mSharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());
        if(mSharedPrefsHelper.getKeyStart() || mFinish){
            mSharedPrefsHelper.putKeyStart(false);
            mFinish = true;
            finish();
        }else{
            if(mSharedPrefsHelper.getBooleanPassCodeUI() && mSharedPrefsHelper.getPassCode() != null){
                Intent intent = new Intent(getBaseContext(), PasscodeLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        }
    }

    private void setupToolbar(){
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void gotoSetPasscodeActivity(){
        Intent intent = new Intent(getApplicationContext(), SetPasscodeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        ServiceUtils.startService(getApplicationContext());
        mSharedPrefsHelper.putPassCodeUI(false);
        if(mFinish){
            finish();
            mSharedPrefsHelper.putKeyStart(true);
            mSharedPrefsHelper.putPassCodeUI(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSharedPrefsHelper.putKeyStart(false);
    }
}
