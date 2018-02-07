package slideshow.lab411.com.slideshow.data.model;

import android.support.annotation.StringRes;

import java.io.Serializable;

public class PhotoItem implements Serializable {
    @StringRes
    int resId;
    String mImgPath;
    boolean mDefault;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getImgPath() {
        return mImgPath;
    }

    public void setImgPath(String imgPath) {
        mImgPath = imgPath;
    }

    public boolean isDefault() {
        return mDefault;
    }

    public void setDefault(boolean aDefault) {
        mDefault = aDefault;
    }
}
