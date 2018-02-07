package slideshow.lab411.com.slideshow.mqtt.service;

/**
 * Created by ld on 07/02/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MQTTAction extends BroadcastReceiver {
    public void onReceive(Context paramContext, Intent paramIntent) {
        paramContext.startService(new Intent(paramContext, MQTTService.class));
    }
}