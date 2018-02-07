package slideshow.lab411.com.slideshow.ui.selectphoto.presenter;

import android.content.Context;
import android.util.Log;

import slideshow.lab411.com.slideshow.base.BasePresenter;
import slideshow.lab411.com.slideshow.base.IActionCallback;
import slideshow.lab411.com.slideshow.data.model.PhotoAlbumList;
import slideshow.lab411.com.slideshow.data.model.PhotoFolderInfo;
import slideshow.lab411.com.slideshow.ui.selectphoto.IPhotoSelectContract.IPhotoSelectPresenter;
import slideshow.lab411.com.slideshow.ui.selectphoto.IPhotoSelectContract.IPhotoSelectView;
import slideshow.lab411.com.slideshow.utils.MediaUtils;

public class PhotoSelectPresenter<V extends IPhotoSelectView> extends BasePresenter<V>
        implements IPhotoSelectPresenter<V> {

    private static final String TAG = "Lab411." + PhotoSelectPresenter.class.getSimpleName();


    @Override
    public void loadImage(Context context) {
        MediaUtils.loadPhotoAlbums(context, mLoadPhotoCallback);
    }

    IActionCallback<PhotoAlbumList> mLoadPhotoCallback = new IActionCallback<PhotoAlbumList>() {
        @Override
        public void onSuccess(PhotoAlbumList result) {
            if (result == null)
                return;
            mView.updateListPhoto(result);
        }

        @Override
        public void onFailed(PhotoAlbumList result) {
            Log.e(TAG, "onLoadPhotoFailed: ");
        }
    };
}
