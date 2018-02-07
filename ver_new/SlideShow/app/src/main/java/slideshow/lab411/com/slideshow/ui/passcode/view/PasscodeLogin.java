package slideshow.lab411.com.slideshow.ui.passcode.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hanks.passcodeview.PasscodeView;

import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.ServiceUtils;
import slideshow.lab411.com.slideshow.ui.imagegrid.view.PhotoGridActivity;
import slideshow.lab411.com.slideshow.ui.passcode.model.SharedPrefsHelper;

public class PasscodeLogin extends AppCompatActivity {
    private SharedPrefsHelper mSharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_passcode_login);
        mSharedPrefsHelper = new SharedPrefsHelper(this);

        PasscodeView passcodeView = findViewById(R.id.passcodeView);
        if(mSharedPrefsHelper.getPassCode() != null){
            passcodeView
                    .setPasscodeLength(4)
                    .setLocalPasscode(mSharedPrefsHelper.getPassCode())
                    .setListener(new PasscodeView.PasscodeViewListener() {
                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void onSuccess(String number) {
                            mSharedPrefsHelper.putPassCodeUI(false);
                            ServiceUtils.stopService(getApplicationContext());
                            finish();
                        }
                    });
        }else{
            gotoPhotoGridActivity();
        }
    }

    private void gotoPhotoGridActivity(){
        Intent intent = new Intent(getApplicationContext(), PhotoGridActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSharedPrefsHelper.putKeyStart(true);
    }

}
