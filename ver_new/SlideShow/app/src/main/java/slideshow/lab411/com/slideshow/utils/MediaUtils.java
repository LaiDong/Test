package slideshow.lab411.com.slideshow.utils;

import android.app.admin.DeviceAdminInfo;
import android.content.Context;
import android.database.Cursor;
import android.inputmethodservice.ExtractEditText;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.base.IActionCallback;
import slideshow.lab411.com.slideshow.data.model.PhotoAlbumList;
import slideshow.lab411.com.slideshow.data.model.PhotoFolderInfo;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;

/**
 * Created by PL_itto on 12/7/2017.
 */

public class MediaUtils {
    private static final String TAG = "Lab411." + MediaUtils.class.getSimpleName();

    public static void getAllPhotoFolder(Context context, List<PhotoInfo> selectPhotoMap, IActionCallback callback) {
        long time = System.currentTimeMillis();
        List<PhotoFolderInfo> allFolderList = new ArrayList<>();
        final String[] projectionPhotos = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
//                MediaStore.Images.Media.ORIENTATION,
//                MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Media.DATE_TAKEN
        };

        final ArrayList<PhotoFolderInfo> allPhotoFolderList = new ArrayList<>();
        HashMap<Integer, PhotoFolderInfo> bucketMap = new HashMap<>();
        Cursor cursor = null;
        PhotoFolderInfo allPhotoFolderInfo = new PhotoFolderInfo();
        allPhotoFolderInfo.setFolderId(0);

        /* Create All photo Album */
        allPhotoFolderInfo.setFolderName(context.getString(R.string.all_photo));
        allPhotoFolderInfo.setPhotoList(new ArrayList<PhotoInfo>());
        allPhotoFolderList.add(0, allPhotoFolderInfo);

        try {
            cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            final int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int dateCollumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            while (cursor.moveToNext()) {
                int bucketId = cursor.getInt(bucketIdColumn);
                String bucketName = cursor.getString(bucketNameColumn);
                final int dataCollumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                final int imageIdColuumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                final int imageId = cursor.getInt(imageIdColuumn);
                final String path = cursor.getString(dataCollumn);
                final int date = cursor.getInt(dateCollumn);
                Date date1 = new Date(date);
                Log.i(TAG, "Image: " + "ID:" + imageId + " - PATH: " + path);
                File file = new File(path);
                final PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setPhotoId(imageId);
                photoInfo.setPhotoPath(path);
                photoInfo.setDate(date1);
                if (allPhotoFolderInfo.getCoverPhoto() == null) {
                    allPhotoFolderInfo.setCoverPhoto(photoInfo);
                }
                //Add photo to AllPhoto folder
                allPhotoFolderInfo.getPhotoList().add(photoInfo);

                //Get Bucket using bucketId
                PhotoFolderInfo photoFolderInfo = bucketMap.get(bucketId);

                //if photo bucket is not exist, create new
                if (photoFolderInfo == null) {
                    photoFolderInfo = new PhotoFolderInfo();
                    photoFolderInfo.setPhotoList(new ArrayList<PhotoInfo>());
                    photoFolderInfo.setFolderId(bucketId);
                    photoFolderInfo.setCoverPhoto(photoInfo);
                    bucketMap.put(bucketId, photoFolderInfo);
                    allPhotoFolderList.add(photoFolderInfo);
                }

                //add photo to bucket
                photoFolderInfo.getPhotoList().add(photoInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllPhotoFailed:\n" + e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
        }
//        allFolderList.addAll(allPhotoFolderList);
        if (callback != null) {
            if (allPhotoFolderInfo != null) {
                callback.onSuccess(allPhotoFolderInfo);
            } else {
                callback.onFailed(null);
            }
        }
        Log.i(TAG, "Time to initDefaultPhoto: " + allPhotoFolderInfo.getPhotoList().size() + " images: " + (System.currentTimeMillis() - time) + " ms");
    }

    public static void loadPhotoAlbums(Context context, IActionCallback callback) {
        long time = System.currentTimeMillis();
        List<PhotoFolderInfo> allFolderList = new ArrayList<>();
        final String[] projectionPhotos = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
//                MediaStore.Images.Media.ORIENTATION,
//                MediaStore.Images.Thumbnails.DATA,
                MediaStore.Images.Media.DATE_TAKEN
        };

        final PhotoAlbumList allPhotoFolderList = new PhotoAlbumList();
        HashMap<Integer, PhotoFolderInfo> bucketMap = new HashMap<>();
        Cursor cursor = null;
        try {
            cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            final int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int dateCollumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            while (cursor.moveToNext()) {
                int bucketId = cursor.getInt(bucketIdColumn);
                String bucketName = cursor.getString(bucketNameColumn);
                final int dataCollumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                final int imageIdColuumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                final int imageId = cursor.getInt(imageIdColuumn);
                final String path = cursor.getString(dataCollumn);
                final int date = cursor.getInt(dateCollumn);
                Date date1 = new Date(date);
                Log.i(TAG, "Image: " + "ID:" + imageId + " - PATH: " + path);
                final PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setPhotoId(imageId);
                photoInfo.setPhotoPath(path);
                photoInfo.setDate(date1);

                //Get Bucket using bucketId
                PhotoFolderInfo photoFolderInfo = bucketMap.get(bucketId);

                //if photo bucket is not exist, create new
                if (photoFolderInfo == null) {
                    Log.d(TAG, "new Album");
                    photoFolderInfo = new PhotoFolderInfo();
                    photoFolderInfo.setFolderName(bucketName);
                    photoFolderInfo.setPhotoList(new ArrayList<PhotoInfo>());
                    photoFolderInfo.setFolderId(bucketId);
                    photoFolderInfo.setCoverPhoto(photoInfo);
                    bucketMap.put(bucketId, photoFolderInfo);
                    allPhotoFolderList.addPhotoAlbum(photoFolderInfo);
                }
                //add photo to bucket
                photoFolderInfo.addPhoto(photoInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllPhotoFailed:\n" + e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
        }
//        allFolderList.addAll(allPhotoFolderList);
        if (callback != null) {
            if (allPhotoFolderList != null) {
                allPhotoFolderList.updatePhotoCount();
                callback.onSuccess(allPhotoFolderList);
            } else {
                callback.onFailed(null);
            }
        }
        Log.i(TAG, "Time to initDefaultPhoto: " + allPhotoFolderList.getPhotoCount() + " images: " + (System.currentTimeMillis() - time) + " ms");
    }
}