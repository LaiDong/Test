package slideshow.lab411.com.slideshow.ui.showphoto.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.base.BaseActivity;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;
import slideshow.lab411.com.slideshow.utils.AppConstants;

/**
 * Created by PL_itto on 12/10/2017.
 */

public class ShowPhotoActivity extends BaseActivity {
    private static final String TAG = "Lab411." + ShowPhotoActivity.class.getSimpleName();

    @NonNull
    @Override
    public Fragment getFragment() {

        ShowPhotoFragment fragment = new ShowPhotoFragment();
        fragment.setArguments(getIntent().getExtras());

        List<PhotoInfo> list = (List<PhotoInfo>) getIntent().getExtras().getSerializable(AppConstants.ShowPhoto.EXTRA_PHOTO_LIST);
        Log.d(TAG, "getFragment: " + list.size());
        return fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
