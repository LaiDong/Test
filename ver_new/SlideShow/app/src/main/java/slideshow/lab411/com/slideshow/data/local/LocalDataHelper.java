package slideshow.lab411.com.slideshow.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import slideshow.lab411.com.slideshow.base.IActionCallback;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;
import slideshow.lab411.com.slideshow.utils.AppConstants;

public class LocalDataHelper implements ILocalDataHelper {
    private static final String TAG = "Lab411." + LocalDataHelper.class.getSimpleName();
    private SharedPreferences mPreferences = null;
    private static final String PREFS_NAME = "preference";
    private static LocalDataHelper sInstance = null;
    private static final String PREF_FIRST_LOAD_KEY = "first_run";
    private PhotoDataHelper mDbHelper = null;

    public static LocalDataHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LocalDataHelper(context);
        }
        return sInstance;
    }

    private LocalDataHelper(Context context) {
        mDbHelper = new PhotoDataHelper(context);
        mPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public boolean isFirstLoad() {
        return mPreferences.getBoolean(PREF_FIRST_LOAD_KEY, true);
    }

    public void afterFirstLoad() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(PREF_FIRST_LOAD_KEY, false);
        editor.apply();
    }

    @Override
    public void loadPhotoFromLocal(IActionCallback callback) {
        if (isFirstLoad()) {
            loadPhotoFromAsset(callback);
        } else {
            loadPhotoFromDb(callback);
        }
    }

    @Override
    public void loadPhotoFromDb(IActionCallback callback) {
        Log.d(TAG, "loadPhotoFromDb: ");
        new LoadDbPhotoAsync(callback).execute();
    }

    @Override
    public void loadPhotoFromAsset(IActionCallback callback) {
        List<PhotoInfo> data = new ArrayList<>();
        for (int res : AppConstants.PhotoGrid.DEFAULT_IMAGES) {
            PhotoInfo info = new PhotoInfo();
            info.setResImage(true);
            info.setResImageId(res);
            data.add(info);
        }
        savedPhotos(data, false);
        if (callback != null)
            callback.onSuccess(data);
        afterFirstLoad();
    }

    @Override
    public void savedPhotos(List<PhotoInfo> list, boolean clearOldData) {
        if (list != null) {
            Log.d(TAG, "savedPhotos: " + list.size());
            new InsertDbPhotoAsync(clearOldData).execute(list);
        }
    }

    @Override
    public void deleteAllPhotos() {
        if (mDbHelper != null) {
            mDbHelper.deleteAllPhotos();
        }
    }


    class PhotoDataHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "photos.db";
        private static final int DB_VER = 1;

        private static final String TABLE_SAVED = "saved_photos";
        private static final String SAVED_ID = "_id";
        private static final String SAVED_PHOTO_ID = "photo_id";
        private static final String SAVED_PATH = "file_path";
        private static final String SAVED_TIME = "time";
        private static final String SAVED_DEFAULT = "is_default";
        private static final String SAVED_DEFAULT_RES = "default_res_index";

        public PhotoDataHelper(Context context) {
            super(context, DB_NAME, null, DB_VER);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            createSavedTable(sqLiteDatabase);
        }

        private void createSavedTable(SQLiteDatabase db) {
            String sqlQuery = "Create Table " + TABLE_SAVED + "(  " +
                    SAVED_ID + " integer primary key autoincrement, " +
                    SAVED_PHOTO_ID + " integer, " +
                    SAVED_PATH + " text, " +
                    SAVED_TIME + " long, " +
                    SAVED_DEFAULT + " integer, " +
                    SAVED_DEFAULT_RES + " integer)";
            db.execSQL(sqlQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists " + TABLE_SAVED);
            onCreate(sqLiteDatabase);
        }


        List<PhotoInfo> loadPhoto() {
            Log.d(TAG, "loadPhoto: ");
            String query = "select * from " + TABLE_SAVED;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            List<PhotoInfo> photoList = new ArrayList<>();
            if (cursor != null) {
                int column_id = cursor.getColumnIndex(SAVED_PHOTO_ID);
                int column_path = cursor.getColumnIndex(SAVED_PATH);
                int column_time = cursor.getColumnIndex(SAVED_TIME);
                int column_default = cursor.getColumnIndex(SAVED_DEFAULT);
                int column_default_id = cursor.getColumnIndex(SAVED_DEFAULT_RES);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    PhotoInfo info = new PhotoInfo();
                    info.setPhotoId(cursor.getInt(column_id));
                    info.setPhotoPath(cursor.getString(column_path));
                    info.setDate(new Date(cursor.getLong(column_time)));
                    info.setResImage(cursor.getInt(column_default) == 1 ? true : false);
                    info.setResImageId(cursor.getInt(column_default_id));
                    photoList.add(info);
                    cursor.moveToNext();
                }
                cursor.close();
                return photoList;
            } else {
                return null;
            }
        }

        void savedAddedPhotos(List<PhotoInfo> list, boolean clearOldData) {
            SQLiteDatabase db = getWritableDatabase();
            if (clearOldData) {
                deleteAllPhotos();
            }
            ContentValues cv = new ContentValues();
            for (PhotoInfo info : list) {
                cv.put(SAVED_PHOTO_ID, info.getPhotoId());
                cv.put(SAVED_PATH, info.getPhotoPath());
                cv.put(SAVED_PHOTO_ID, info.getPhotoId());
                cv.put(SAVED_DEFAULT, info.isResImage() ? 1 : 0);
                cv.put(SAVED_DEFAULT_RES, info.getResImageId());
                cv.put(SAVED_TIME, info.getDate().getTime());
                db.insert(TABLE_SAVED, null, cv);
                cv.clear();
            }
        }

        void deleteAllPhotos() {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_SAVED, null, null);
        }
    }

    class LoadDbPhotoAsync extends AsyncTask<Void, Void, List<PhotoInfo>> {
        IActionCallback mCallback;

        public LoadDbPhotoAsync(IActionCallback callback) {
            mCallback = callback;
        }

        @Override
        protected List<PhotoInfo> doInBackground(Void... voids) {
            return mDbHelper.loadPhoto();
        }

        @Override
        protected void onPostExecute(List<PhotoInfo> photoInfos) {
            super.onPostExecute(photoInfos);
            if (mCallback != null) {
                if (photoInfos != null)
                    mCallback.onSuccess(photoInfos);
                else
                    mCallback.onFailed(null);
            }
        }
    }

    class InsertDbPhotoAsync extends AsyncTask<List<PhotoInfo>, Void, Void> {
        private boolean mClearOldData = false;

        public InsertDbPhotoAsync(boolean clearOldData) {
            mClearOldData = clearOldData;
        }

        @Override
        protected Void doInBackground(List<PhotoInfo>[] lists) {
            if (mDbHelper != null)
                mDbHelper.savedAddedPhotos(lists[0], mClearOldData);
            return null;
        }
    }

}
