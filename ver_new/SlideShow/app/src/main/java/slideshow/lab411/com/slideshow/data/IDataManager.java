package slideshow.lab411.com.slideshow.data;

import java.util.List;

import slideshow.lab411.com.slideshow.base.IActionCallback;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;

/**
 * Created by ld on 16/12/2017.
 */

public interface IDataManager {
    void loadPhoto(IActionCallback callback);

    void savedAddedPhotos(List<PhotoInfo> list, boolean clearOldData);

    void deleteAllSavedPhoto();
}
