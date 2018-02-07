package slideshow.lab411.com.slideshow.ui.imagegrid.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import slideshow.lab411.com.slideshow.ui.passcode.model.SharedPrefsHelper;
import slideshow.lab411.com.slideshow.ui.passcode.view.PasscodeLogin;

/**
 * Created by ld on 13/12/2017.
 */

public final class ServiceUtils {
    public final static String recording_serviceName = "slideshow.lab411.com.slideshow.ui.imagegrid.service.RecordingService";
    public final static String passcode_serviceName = "slideshow.lab411.com.slideshow.ui.imagegrid.service.StartPassCodeUIService";

    public static boolean isMyServiceRunning(String serviceName, Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void stopService(Context context){
        if(isMyServiceRunning(passcode_serviceName, context)){
            Intent intent = new Intent(context, StartPassCodeUIService.class);
            context.stopService(intent);
            SharedPrefsHelper mSharedPrefsHelper = new SharedPrefsHelper(context);
            mSharedPrefsHelper.putPassCodeUI(false);
        }
    }

    public static void startService(Context context){
        if(!ServiceUtils.isMyServiceRunning(ServiceUtils.passcode_serviceName, context)){
            Intent intent1= new Intent(context, StartPassCodeUIService.class);
            context.startService(intent1);
        }
    }
}
