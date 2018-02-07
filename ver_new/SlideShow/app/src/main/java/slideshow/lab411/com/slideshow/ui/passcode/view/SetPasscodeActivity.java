package slideshow.lab411.com.slideshow.ui.passcode.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.hanks.passcodeview.PasscodeView;

import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.base.BaseActivity;
import slideshow.lab411.com.slideshow.ui.passcode.model.SharedPrefsHelper;

/**
 * Created by ld on 14/12/2017.
 */

public class SetPasscodeActivity extends BaseActivity{
    @NonNull
    @Override
    public Fragment getFragment() {
        return new SetPasscodeFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
