package com.example.ld.firebasedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ld on 24/01/2018.
 */

public class LocationService extends Service {
    private static final String TAG = "LaiDongLocationService";
    private DatabaseReference mFirebaseDatabase, myRef;
    private FirebaseDatabase mFirebaseInstance;
    double latitude, longtitude;
    private String userId;

    private Timer mTimer = null;
    private TimerTask mIncrementTimerTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimerPushLocation();
        Log.d(TAG, "onStartCommand start");
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mIncrementTimerTask != null) {
            mIncrementTimerTask.cancel();
            mIncrementTimerTask = null;
        }
    }

    private void pushLocation(){
        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longtitude = gps.getLongitude();

            if (TextUtils.isEmpty(userId)) {
                userId = mFirebaseDatabase.push().getKey();
            }

            User user = new User(latitude, longtitude);

            mFirebaseDatabase.child(userId).setValue(user);
            Log.d("donglv", "latitude: " + latitude + " - longtitude: " + longtitude);
        }else{
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
    }

    private void startTimerPushLocation(){
      /*  mTimer = new Timer();
        mIncrementTimerTask = new TimerTask() {
            @Override
            public void run() {


            }
        };
        mTimer.scheduleAtFixedRate(mIncrementTimerTask, 20*1000, 1000);*/

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: pushLocation");
                pushLocation();
            }
        },2000);
    }
}
