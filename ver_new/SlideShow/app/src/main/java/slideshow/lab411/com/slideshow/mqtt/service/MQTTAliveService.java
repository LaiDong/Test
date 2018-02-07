package slideshow.lab411.com.slideshow.mqtt.service;

/**
 * Created by ld on 07/02/2018.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.base.BaseActivity;
import slideshow.lab411.com.slideshow.mqtt.constants.Defines;
import slideshow.lab411.com.slideshow.mqtt.network.Connection;
import slideshow.lab411.com.slideshow.mqtt.utils.Notify;


public class MQTTAliveService extends Service {
    private Connection connection;
    private String TAG = "MQTTAliveService";
    private final String host = "stun.zoota.vn";
    private final int port = 1883;
    private Context context;
    private MqttHelper mqttHelper;

    Handler hand = new Handler() {
        public void handleMessage(Message paramAnonymousMessage) {
            int i = paramAnonymousMessage.what;
            if (i == Defines.SUBACK) {
                //start connect to Broker
            }
        }
    };

    private void startMqtt() {
        try {
            mqttHelper = new MqttHelper(getApplicationContext());
            mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean b, String s) {
                    Log.w("MqttS", "Connected");
                }

                @Override
                public void connectionLost(Throwable throwable) {
                    Log.w("MqttS", "connectionLost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    Log.w("MqttS", mqttMessage.toString());
                    Intent intent = new Intent();
                    intent.setClassName(context, String.valueOf(BaseActivity.class));
                    Notify.notifcation(getApplicationContext(), mqttMessage.toString(), intent, R.string.str_mqtt_title);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRebind(Intent paramIntent) {
        super.onRebind(paramIntent);
    }

    @Override
    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
//        startMqtt();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    startMqtt();
                } catch (Exception localException) {

                }
                new SystemAppRunning().start();
            }
        }, 4000L);

        NotificationManager nManager = ((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE));
        Notification notification = new Notification();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            notification.priority = Notification.PRIORITY_MIN;
        }
        startForeground(0, notification);
        nManager.cancel(0);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startMqtt();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        sendBroadcast(new Intent("resetservice"));
    }

    class SystemAppRunning extends Thread {
        SystemAppRunning() {
        }

        @Override
        public void run() {
            while (true) {

//                try {
//
//                } catch (Exception e) {
//                    MQTTAliveService.this.hand.sendEmptyMessage(Defines.SUBACK);
//                }
//
//                try {
//                    MQTTAliveService.this.hand.sendEmptyMessage(Defines.SUBACK);
//                } catch (Exception e) {
//
//                }
            }
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("resetservice"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

