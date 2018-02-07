package slideshow.lab411.com.slideshow.data.model;

import java.util.ArrayList;
import java.util.List;

public class PhotoAlbumList {
    List<PhotoFolderInfo> mAlbumList;
    int mPhotoCount;

    public PhotoAlbumList() {
        mAlbumList = new ArrayList<>();
        mPhotoCount = 0;
    }

    public void clear() {
        mAlbumList.clear();
        mPhotoCount = 0;
    }

    public List<PhotoFolderInfo> getAlbumList() {
        return mAlbumList;
    }

    public void setAlbumList(List<PhotoFolderInfo> albumList) {
        mAlbumList = albumList;
        mPhotoCount = 0;
        for (PhotoFolderInfo folderInfo : albumList)
            mPhotoCount += folderInfo.getPhotoCount();
    }

    public int getPhotoCount() {
        return mPhotoCount;
    }

    public void setPhotoCount(int photoCount) {
        mPhotoCount = photoCount;
    }

    public int getAlbumCount() {
        return mAlbumList.size();
    }

    public void addPhotoAlbum(PhotoFolderInfo info) {
        mAlbumList.add(info);
    }

    public void updatePhotoCount() {
        mPhotoCount = 0;
        for (PhotoFolderInfo info : mAlbumList) {
            mPhotoCount += info.getPhotoCount();
        }
    }
}
