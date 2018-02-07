package slideshow.lab411.com.slideshow.data.model;

import android.support.annotation.DrawableRes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PhotoInfo implements Serializable {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private int photoId;
    private String photoPath;
    private Date date;
    private boolean mResImage = false;
    private @DrawableRes
    int resImageId = -1;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isResImage() {
        return mResImage;
    }

    public int getResImageId() {
        return resImageId;
    }

    public void setResImageId(int resImageId) {
        this.resImageId = resImageId;
    }

    public void setResImage(boolean resImage) {
        mResImage = resImage;
    }

    public PhotoInfo() {
        date = Calendar.getInstance().getTime();
        mResImage = false;
        resImageId = -1;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getDateTaken() {
        if (date != null) {
            return dateFormat.format(date);
        }
        return "";
    }

    public String getTimeTaken() {
        if (date != null) {
            return timeFormat.format(date);
        }
        return "";
    }
}
