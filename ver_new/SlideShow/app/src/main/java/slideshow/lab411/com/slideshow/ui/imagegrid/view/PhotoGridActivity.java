package slideshow.lab411.com.slideshow.ui.imagegrid.view;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import slideshow.lab411.com.slideshow.base.BaseActivity;

public class PhotoGridActivity extends BaseActivity {

    @NonNull
    @Override
    public Fragment getFragment() {
        return new PhotoGridFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

