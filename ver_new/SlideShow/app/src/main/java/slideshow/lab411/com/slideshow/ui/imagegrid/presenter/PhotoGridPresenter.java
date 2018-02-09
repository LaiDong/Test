package slideshow.lab411.com.slideshow.ui.imagegrid.presenter;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import slideshow.lab411.com.slideshow.base.BasePresenter;
import slideshow.lab411.com.slideshow.base.IActionCallback;
import slideshow.lab411.com.slideshow.data.DataManager;
import slideshow.lab411.com.slideshow.data.IDataManager;
import slideshow.lab411.com.slideshow.data.model.PhotoFolderInfo;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;
import slideshow.lab411.com.slideshow.ui.imagegrid.IPhotoGridContract;
import slideshow.lab411.com.slideshow.utils.AppConstants;
import slideshow.lab411.com.slideshow.utils.MediaUtils;

public class PhotoGridPresenter<V extends IPhotoGridContract.IPhotoGridView>
        extends BasePresenter<V> implements IPhotoGridContract.IPhotoGridPresenter<V> {
    private static final String TAG = "Lab411." + PhotoGridPresenter.class.getSimpleName();

    @SuppressWarnings("unused")
    @Override
    public void initDefaultPhoto() {
//        MediaUtils.getAllPhotoFolder(context, null, mLoadPhotoCallback);
        List<PhotoInfo> data = new ArrayList<>();
        for (int res : AppConstants.PhotoGrid.DEFAULT_IMAGES) {
            PhotoInfo info = new PhotoInfo();
            info.setResImage(true);
            info.setResImageId(res);
            data.add(info);
        }
        if (mView != null)
            mView.updateListPhoto(data);
    }

    @Override
    public void loadPhoto(Context context) {
        IDataManager dataManager = DataManager.getInstance(context);
        dataManager.loadPhoto(mLoadPhotoCallback);
    }

    @Override
    public void savedAddedPhotos(List<PhotoInfo> data, Context context, boolean clearOldData) {
        DataManager.getInstance(context).savedAddedPhotos(data, clearOldData);
    }

    @Override
    public void deleteAllPhoto(Context context) {
        DataManager.getInstance(context).deleteAllSavedPhoto();
    }


    IActionCallback<List<PhotoInfo>> mLoadPhotoCallback = new IActionCallback<List<PhotoInfo>>() {

        @Override
        public void onSuccess(List<PhotoInfo> result) {
            if (mView != null && result != null) {
                mView.updateListPhoto(result);
            }
        }

        @Override
        public void onFailed(List<PhotoInfo> result) {

        }
    };
}
