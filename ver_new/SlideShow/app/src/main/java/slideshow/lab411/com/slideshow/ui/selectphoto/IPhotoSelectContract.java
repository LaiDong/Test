package slideshow.lab411.com.slideshow.ui.selectphoto;

import android.content.Context;
import android.support.annotation.NonNull;

import slideshow.lab411.com.slideshow.base.IBasePresenter;
import slideshow.lab411.com.slideshow.base.IBaseView;
import slideshow.lab411.com.slideshow.data.model.PhotoAlbumList;
import slideshow.lab411.com.slideshow.data.model.PhotoFolderInfo;

public interface IPhotoSelectContract {


    /* Photo Select */
    interface IPhotoSelectView extends IBaseView {
        void updateListPhoto(@NonNull PhotoAlbumList data);
    }

    interface IPhotoSelectPresenter<V extends IPhotoSelectView> extends IBasePresenter<V> {
        void loadImage(Context context);
    }
}
