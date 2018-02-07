package slideshow.lab411.com.slideshow.data.model;

import java.util.ArrayList;
import java.util.List;

public class PhotoFolderInfo {
    private int folderId;
    private String folderName;
    private PhotoInfo coverPhoto;
    private List<PhotoInfo> photoList;

    public PhotoFolderInfo() {
        photoList = new ArrayList<>();
        folderId = -1;
        folderName = "NA";
        coverPhoto = null;
    }


    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public PhotoInfo getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(PhotoInfo coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public List<PhotoInfo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<PhotoInfo> photoList) {
        this.photoList = photoList;
    }

    public int getPhotoCount() {
        return photoList.size();
    }

    public PhotoInfo getPhoto(int pos) {
        return photoList.get(pos);
    }

    public void addPhoto(PhotoInfo info) {
        photoList.add(info);
    }
}
