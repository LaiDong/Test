package slideshow.lab411.com.slideshow.mqtt.service;

/**
 * Created by ld on 07/02/2018.
 */

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.File;

import slideshow.lab411.com.slideshow.base.BaseActivity;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.RecordingService;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.ServiceUtils;

public class MQTTService extends Service {

    private static final String TAG = "MQTTService";
    private static boolean hasWifi = false;
    private static boolean hasMmobile = false;
    private Thread thread;
    private ConnectivityManager mConnMan;
    private volatile IMqttAsyncClient mqttClient;
    private String deviceId;
    private MQTTBinder mqttBinder;
    private MQTTBroadcastReceiver mqttBroadcastReceiver;
    private long lastTimePublishMessageToBroker = 0;
    private final String COMMAND_START = "command_start";
    private final String COMMAND_STOP = "command_stop";
    private final String GET_LOCATION = "get_location";
    private final String POSITION_X_Y = "position_x_y";
    private final String CHECK_STATUS = "check_status";
    private final String STATUS_ONLINE = "status_online";

    Handler hand = new Handler() {
        public void handleMessage(Message paramAnonymousMessage) {
            int i = paramAnonymousMessage.what;
            if (i == 5) {
                lastTimePublishMessageToBroker = System.currentTimeMillis();
                publishMessageToBroker("status_online");
            }
        }
    };

    private void publishMessageToBroker(String status) {
        Log.v(TAG, "publish message to broken");
        if (mqttClient != null) {
            byte[] payload = status.getBytes();
            try {
                mqttClient.publish("/SlideShowControl", new MqttMessage(payload));
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public class MQTTBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            doConnect();
        }
    }

    public class MQTTBinder extends Binder {
        public MQTTService getService() {
            return MQTTService.this;
        }
    }

    @Override
    public void onCreate() {
        init();
    }

    public void init() {
        lastTimePublishMessageToBroker = System.currentTimeMillis();
        mqttBroadcastReceiver = new MQTTBroadcastReceiver();
        IntentFilter intentf = new IntentFilter();
        setClientID();
        Log.v(TAG, "init + client ID: " + deviceId);
        intentf.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mqttBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged()");
        android.os.Debug.waitForDebugger();
        super.onConfigurationChanged(newConfig);

    }

    private void setClientID() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        deviceId = wInfo.getMacAddress();
        if (deviceId == null) {
            deviceId = "FHHeej6797THYYTrtrrtt";
//            deviceId = MqttAsyncClient.generateClientId();
        }
    }

    private void doConnect() {
        Log.d(TAG, "doConnect()");
        IMqttToken token;
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setKeepAliveInterval(20 * 60);
        options.setConnectionTimeout(0);
        try {
//            if(mqttClient == null)
            mqttClient = new MqttAsyncClient("tcp://iot.eclipse.org:1883", deviceId, new MemoryPersistence());
            token = mqttClient.connect();
            token.waitForCompletion(35000);
            mqttClient.setCallback(new MqttEventCallback());
            token = mqttClient.subscribe("/SlideShowControl", 1);
            token.waitForCompletion(50000);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            switch (e.getReasonCode()) {
                case MqttException.REASON_CODE_BROKER_UNAVAILABLE:
                case MqttException.REASON_CODE_CLIENT_TIMEOUT:
                case MqttException.REASON_CODE_CONNECTION_LOST:
                    Log.v(TAG, "Lost Connection" + e.getMessage());
                    e.printStackTrace();
                case MqttException.REASON_CODE_SERVER_CONNECT_ERROR:
                    Log.v(TAG, "c" + e.getMessage());
                    e.printStackTrace();
                    break;
                case MqttException.REASON_CODE_FAILED_AUTHENTICATION:
                    Intent i = new Intent("RAISEALLARM");
                    i.putExtra("ALLARM", e);
                    Log.e(TAG, "b" + e.getMessage());
                    break;
                default:
                    Log.e(TAG, "a" + e.getMessage());
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        new SystemAppRunning().start();
        NotificationManager nManager = ((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE));
        Notification notification = new Notification();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            notification.priority = Notification.PRIORITY_MIN;
        }
        startForeground(0, notification);
        return START_STICKY;
    }


    class SystemAppRunning extends Thread {
        @SuppressWarnings("deprecation")
        public void run() {
            try {
                while (true) {
                    if (mqttClient != null) {
                        Log.v(TAG, "MQTTConnected:" + mqttClient.isConnected());

                        Thread.sleep(3000);
                        if (!mqttClient.isConnected()) {
//                            doConnect();
                        } else {
                            if (System.currentTimeMillis() - lastTimePublishMessageToBroker >= 60000) {
                                MQTTService.this.hand.sendEmptyMessage(5);
                            }
                        }
                    } else {
                        init();
                    }
//                    doConnect();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private class MqttEventCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable arg0) {
            Intent launchA = new Intent(MQTTService.this, BaseActivity.class);
            launchA.putExtra("message", "Connection Lost");
            //Notify.notifcation(getApplicationContext(), "Connection Lost", launchA, R.string.str_notification_title);
            doConnect();
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        @SuppressLint("NewApi")
        public void messageArrived(String topic, final MqttMessage msg) throws Exception {
            Log.i(TAG, "Message arrived from topic" + topic);
            Handler h = new Handler(getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Intent launchA = new Intent(MQTTService.this, BaseActivity.class);
                    launchA.putExtra("message", msg.getPayload());
                    //TODO write some                                                                                      thing that has some sense
                    Log.v(TAG, "MQTT Message:  " + new String(msg.getPayload()));
                    if (Build.VERSION.SDK_INT >= 11) {
                        // Notify.notifcation(getApplicationContext(), new String(msg.getPayload()), launchA, R.string.str_notification_title);
//                        launchA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    } /*else {
                        launchA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		    }*/
//                    startActivity(launchA);
                   // Toast.makeText(getApplicationContext(), "MQTT Message:\n" + new String(msg.getPayload()), Toast.LENGTH_SHORT).show();
                    String msg_response = new String(msg.getPayload()).trim();
                    if(msg_response.equals(COMMAND_START)){
                        if(!ServiceUtils.isMyServiceRunning(ServiceUtils.recording_serviceName, getApplicationContext()))
                            onRecord(true);
                    }else if(msg_response.equals(COMMAND_STOP)){
                        onRecord(false);
                    }else if(msg_response.equals(GET_LOCATION)){
                        publishMessageToBroker(getPosition());
                    }else if(msg_response.equals(CHECK_STATUS)){
                        publishMessageToBroker("online");
                    }
                }
            });
        }
    }

    private void onRecord(boolean start) {
        final Intent intent = new Intent(getApplicationContext(), RecordingService.class);
        if (start) {
            File folder = new File(Environment.getExternalStorageDirectory() + "/SlideShow");
            if (!folder.exists()) {
                //folder /SlideShow doesn't exist, create the folder
                folder.mkdir();
            }
            getApplicationContext().startService(intent);
        } else {
            getApplicationContext().stopService(intent);
        }
    }

    private String getPosition(){
        GPSTracker gps = new GPSTracker(this);
        double latitude = 0, longitude = 0;
        if(gps.canGetLocation()){
             latitude = gps.getLatitude();
             longitude = gps.getLongitude();

            Log.d(TAG, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude);
        }else{
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
        return "position" + "_" + latitude + "_" + longitude;
    }

    public String getThread() {
        return Long.valueOf(thread.getId()).toString();
    }

    private void restartService() {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.v(TAG, "resetservice");
        restartService();
//        sendBroadcast(new Intent("resetservice"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "resetservice");
        restartService();
//        unregisterReceiver(mqttBroadcastReceiver);
//        try {
//            mqttClient.close();
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//        sendBroadcast(new Intent("resetservice"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
}
