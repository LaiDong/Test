package slideshow.lab411.com.slideshow.mqtt.action;

/**
 * Created by ld on 07/02/2018.
 */

import android.util.Log;

import org.eclipse.paho.android.service.MqttTraceHandler;

class MqttTraceCallback implements MqttTraceHandler {

    public void traceDebug(String arg0, String arg1) {
        Log.i(arg0, arg1);
    }

    public void traceError(String arg0, String arg1) {
        Log.e(arg0, arg1);
    }

    public void traceException(String arg0, String arg1,
                               Exception arg2) {
        Log.e(arg0, arg1, arg2);
    }

}
