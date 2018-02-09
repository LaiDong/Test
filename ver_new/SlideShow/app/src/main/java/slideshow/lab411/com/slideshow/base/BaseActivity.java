package slideshow.lab411.com.slideshow.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import butterknife.Unbinder;
import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.ServiceUtils;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.StartPassCodeUIService;
import slideshow.lab411.com.slideshow.ui.passcode.model.SharedPrefsHelper;
import slideshow.lab411.com.slideshow.ui.passcode.view.PasscodeLogin;
import slideshow.lab411.com.slideshow.utils.NetworkUtils;

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    private static final String TAG = "BaseActivity";

//    private ActivityComponent mActivityComponent;

    private Unbinder mUnBinder;
    SharedPrefsHelper mSharedPrefsHelper;
    boolean mFinish = false;

    public int getContentRes() {
        return R.layout.activity_default;
    }

    @NonNull
    public abstract Fragment getFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentRes());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frame_container);
        if (fragment == null) {
            fragment = getFragment();
            fm.beginTransaction().add(R.id.frame_container, fragment).commit();
        }
        mSharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());

        sendBroadcast(new Intent("resetservice"));
    }

    @Override
    public void showMessage(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(@NonNull String msg) {
        if (msg != null)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSharedPrefsHelper.getKeyStart() || mFinish){
            Log.d(TAG, "onResume finish");
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

    @Override
    protected void onPause() {
        super.onPause();
        ServiceUtils.startService(getApplicationContext());
        mSharedPrefsHelper.putPassCodeUI(false);
        if(mFinish){
            finish();
            mSharedPrefsHelper.putKeyStart(true);
            mSharedPrefsHelper.putPassCodeUI(true);
            Log.d(TAG, "onPause: finish");
        }

    }

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        mSharedPrefsHelper.putKeyStart(false);
        super.onDestroy();
    }

    public void requestTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setStatusColor(@ColorRes int color) {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(color));
    }
}
