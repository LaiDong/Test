package slideshow.lab411.com.slideshow.data.local;

import java.util.List;

import slideshow.lab411.com.slideshow.base.IActionCallback;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;

public interface ILocalDataHelper {
    void loadPhotoFromLocal(IActionCallback callback);

    void loadPhotoFromDb(IActionCallback callback);

    void loadPhotoFromAsset(IActionCallback callback);

    void savedPhotos(List<PhotoInfo> list, boolean clearOldData);

    void deleteAllPhotos();
}
