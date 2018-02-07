package slideshow.lab411.com.slideshow.base;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface IBaseView {
    void showMessage(@StringRes int resId);

    void showMessage(@NonNull String msg);

    boolean isNetworkConnected();
}
