package slideshow.lab411.com.slideshow.ui.imagegrid.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.NoSuchPaddingException;

import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.ui.imagegrid.database.DBHelper;
import slideshow.lab411.com.slideshow.ui.passcode.model.SharedPrefsHelper;
import slideshow.lab411.com.slideshow.utils.CryptLib;

/**
 * Created by ld on 07/12/2017.
 */

public class RecordingService extends Service {

    private static final String LOG_TAG = "LaiDongRecordingService";

    private String mFileName = null;
    private String mFilePath = null;

    private MediaRecorder mRecorder = null;

    private DBHelper mDatabase;

    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;
    private int mElapsedSeconds = 0;
    private OnTimerChangedListener onTimerChangedListener = null;
    private static final SimpleDateFormat mTimerFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private Timer mTimer = null;
    private TimerTask mIncrementTimerTask;

    private CryptLib _crypt;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface OnTimerChangedListener {
        void onTimerChanged(int seconds);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = new DBHelper(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecording();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        if (mRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }

    public void startRecording() {

        Log.e(LOG_TAG, "start recording");

        setFileNameAndPath();

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartingTimeMillis = System.currentTimeMillis();

            startTimer();
            //startForeground(1, createNotification());

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void setFileNameAndPath() {
        File f;
        do {
            //mFileName = mTimerFormat.format(Calendar.getInstance().getTime()) + ".mp3";
            mFileName = mTimerFormat.format(Calendar.getInstance().getTime());
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath += "/SlideShow/" + mFileName;

            f = new File(mFilePath);
        } while (f.exists() && !f.isDirectory());
    }

    public void stopRecording() {
        try{
            mRecorder.stop();
            mRecorder.release();
        }catch(IllegalStateException e){}
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);
        Log.e(LOG_TAG, "stop recording");
        // Toast.makeText(this, " Saved Recordings " + mFilePath, Toast.LENGTH_LONG).show();

        //remove notification
        if (mIncrementTimerTask != null) {
            mIncrementTimerTask.cancel();
            mIncrementTimerTask = null;
        }

        mRecorder = null;

        try {
            //Thuc hien ma hoa file sau khi stop
            _crypt = new CryptLib();

            Log.d(LOG_TAG, "getAudioFile: " + getAudioFile());
            saveFile(getAudioFile()); //duong dan cua file vua tao ra

            File delFile = new File(mFilePath);
            delFile.delete();

        } catch (Exception e) {
            Log.e(LOG_TAG, "exception", e);
        }
    }

    private byte[] getAudioFile() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        byte[] audio_data = null;
        byte[] inarry = null;

        FileInputStream fis = new FileInputStream(mFilePath);
        int length = fis.available();

        audio_data = new byte[length];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = fis.read(audio_data)) != -1) {
            output.write(audio_data, 0, bytesRead);
        }
        inarry = output.toByteArray();

        fis.close();
        return inarry;

    }

    private void saveFile(byte[] stringToSave){
        try {
            File file = new File(mFilePath+"_e");

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] filesBytes = _crypt.encryptFile(stringToSave, CryptLib.MySecretKeySHA256(), CryptLib.getIvByte());
            bos.write(filesBytes);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startTimer() {
        mTimer = new Timer();
        mIncrementTimerTask = new TimerTask() {
            @Override
            public void run() {
                stopRecording();
                startRecording();
            }
        };
        mTimer.scheduleAtFixedRate(mIncrementTimerTask, 30*60*1000, 1000);
    }

}
