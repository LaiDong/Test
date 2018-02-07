package slideshow.lab411.com.slideshow.data;

import android.content.Context;

import java.util.List;

import slideshow.lab411.com.slideshow.base.IActionCallback;
import slideshow.lab411.com.slideshow.data.local.ILocalDataHelper;
import slideshow.lab411.com.slideshow.data.local.LocalDataHelper;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;

public class DataManager implements IDataManager {
    private static final String TAG = "PL_itto." + DataManager.class.getSimpleName();
    private ILocalDataHelper mLocalDataHelper;

    private static IDataManager sDataManager;

    public static IDataManager getInstance(Context context) {
        if (sDataManager == null) {
            sDataManager = new DataManager(context);
        }
        return sDataManager;
    }

    private DataManager(Context context) {
        mLocalDataHelper = LocalDataHelper.getInstance(context);
    }

    @Override
    public void loadPhoto(IActionCallback callback) {
        if (mLocalDataHelper != null) {
            mLocalDataHelper.loadPhotoFromLocal(callback);
        }
    }

    @Override
    public void savedAddedPhotos(List<PhotoInfo> list, boolean clearOldData) {
        if (mLocalDataHelper != null)
            mLocalDataHelper.savedPhotos(list, clearOldData);
    }

    @Override
    public void deleteAllSavedPhoto() {
        if (mLocalDataHelper != null)
            mLocalDataHelper.deleteAllPhotos();
    }
}