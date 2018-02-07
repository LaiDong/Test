package slideshow.lab411.com.slideshow.ui.imagegrid;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import slideshow.lab411.com.slideshow.base.IBasePresenter;
import slideshow.lab411.com.slideshow.base.IBaseView;
import slideshow.lab411.com.slideshow.data.model.PhotoFolderInfo;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;

public interface IPhotoGridContract {
    /* Main Photo Grid */
    interface IPhotoGridView extends IBaseView {
        void updateListPhoto(@NonNull List<PhotoInfo> data);

        void onSelectModeSwitch(boolean enabled);

        void showPhoto(@NonNull List<PhotoInfo> data, int position);

        void openGallery();

        void openSlideShow();

        void deleteSelectedPhoto();
    }

    interface IPhotoGridPresenter<V extends IPhotoGridView> extends IBasePresenter<V> {
        void initDefaultPhoto();

        void loadPhoto(Context context);

        void savedAddedPhotos(List<PhotoInfo> data, Context context,boolean clearOldData);

        void deleteAllPhoto(Context context);
    }

}