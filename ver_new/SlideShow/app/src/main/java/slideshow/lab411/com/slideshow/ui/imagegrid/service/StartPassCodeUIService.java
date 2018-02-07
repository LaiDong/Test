package slideshow.lab411.com.slideshow.ui.imagegrid.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import slideshow.lab411.com.slideshow.ui.imagegrid.database.DBHelper;
import slideshow.lab411.com.slideshow.ui.passcode.model.SharedPrefsHelper;

/**
 * Created by ld on 20/12/2017.
 */

public class StartPassCodeUIService extends Service {
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    SharedPrefsHelper mSharedPrefsHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimerPassCode();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }
        mSharedPrefsHelper.putPassCodeUI(false);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTimerPassCode() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mSharedPrefsHelper.putPassCodeUI(true);
            }
        };
        mTimer.scheduleAtFixedRate(mTimerTask, 5000, 1000);

    }
}
