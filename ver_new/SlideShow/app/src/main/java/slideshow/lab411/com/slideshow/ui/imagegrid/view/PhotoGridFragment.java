package slideshow.lab411.com.slideshow.ui.imagegrid.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import pl.itto.squarelayout.SquareLayout;
import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.base.BaseFragment;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;
import slideshow.lab411.com.slideshow.ui.imagegrid.IPhotoGridContract.IPhotoGridPresenter;
import slideshow.lab411.com.slideshow.ui.imagegrid.IPhotoGridContract.IPhotoGridView;
import slideshow.lab411.com.slideshow.ui.imagegrid.presenter.PhotoGridPresenter;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.RecordingService;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.ServiceUtils;
import slideshow.lab411.com.slideshow.ui.passcode.model.SharedPrefsHelper;
import slideshow.lab411.com.slideshow.ui.passcode.view.PasscodeUI;
import slideshow.lab411.com.slideshow.ui.passcode.view.SetPasscodeActivity;
import slideshow.lab411.com.slideshow.ui.selectphoto.view.PhotoSelectActivity;
import slideshow.lab411.com.slideshow.ui.showphoto.view.ShowPhotoActivity;
import slideshow.lab411.com.slideshow.ui.widget.PhotoItemDecoration;
import slideshow.lab411.com.slideshow.utils.AppConstants;
import slideshow.lab411.com.slideshow.utils.AppConstants.ShowPhoto;
import slideshow.lab411.com.slideshow.utils.UiUtils;

/**
 * Created by PL_itto on 12/6/2017.
 */

public class PhotoGridFragment extends BaseFragment implements IPhotoGridView {
    private static final String TAG = "Lab411." + PhotoGridFragment.class.getSimpleName();

    /**
     * Init Views
     */
    /* Toolbar */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
//    RelativeLayout mToolbar;
//    @BindView(R.id.gallery_title)
//    TextView mToolbarTitle;

    @BindView(R.id.photo_layout)
    CoordinatorLayout mPhotoLayout;
    @BindView(R.id.photo_grid)
    RecyclerView mPhotoGrid;

    @BindView(R.id.no_photo_layout)
    RelativeLayout mNoPhotoLayout;

    IPhotoGridPresenter<IPhotoGridView> mPresenter;
    PhotoGridAdapter mPhotoGridAdapter;
    private ActionMode mActionMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new PhotoGridPresenter<>();
        mPresenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_photo_grid, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        setup();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null)
//            mPresenter.initDefaultPhoto();
            mPresenter.loadPhoto(getContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.photo_grid_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_photo:
                openGallery();
                return true;
            case R.id.action_passcode:
                Intent intent = new Intent(getContext(), PasscodeUI.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    void setup() {
        /* Photo Grid */
        int span = getResources().getInteger(R.integer.photo_grid_span);
        mPhotoGrid.setLayoutManager(new GridLayoutManager(getContext(), span, LinearLayoutManager.VERTICAL, false));
        mPhotoGrid.addItemDecoration(new PhotoItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.grid_item_spacing),
                span));
        mPhotoGridAdapter = new PhotoGridAdapter();
        mPhotoGrid.setAdapter(mPhotoGridAdapter);
        setupActionBar();
    }

    void setupActionBar() {
        getParentActivity().setSupportActionBar(mToolbar);
//        getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mToolbarTitle.setText(getParentActivity().getTitle());
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
        super.onDestroy();
    }


    @Override
    public void updateListPhoto(@NonNull List<PhotoInfo> data) {
        if (mPhotoGridAdapter != null) {
            Log.d(TAG, "updateListPhoto: " + data.size());
            mPhotoGridAdapter.replaceData(data);
            switchViewVisible();
        }
    }

    @Override
    public void onSelectModeSwitch(boolean enabled) {
        if (enabled) {
            Log.i(TAG, "onSelectModeSwitch: ");
            mActionMode = getParentActivity().startSupportActionMode(new PhotoSelectCallback());
        }
    }

    @Override
    public void showPhoto(@NonNull List<PhotoInfo> data, int position) {
        Intent intent = new Intent(getContext(), ShowPhotoActivity.class);
        intent.putExtra(ShowPhoto.EXTRA_PHOTO_LIST, (Serializable) data);
        intent.putExtra(ShowPhoto.EXTRA_PHOTO_POSITION, position);
        startActivity(intent);
    }

    @Override
    public void openGallery() {
        Intent intent = new Intent(getContext(), PhotoSelectActivity.class);
        startActivityForResult(intent, AppConstants.PhotoGrid.REQUEST_PHOTO);
    }

    @Override
    public void openSlideShow() {
        if (mPhotoGridAdapter != null) {
            List<PhotoInfo> dataList = mPhotoGridAdapter.getDataList();
            if (dataList != null && dataList.size() > 0) {
                Intent intent = new Intent(getContext(), ShowPhotoActivity.class);
                intent.putExtra(ShowPhoto.EXTRA_MODE_SHOW, ShowPhoto.MODE_SLIDE);
                intent.putExtra(ShowPhoto.EXTRA_PHOTO_LIST, (Serializable) dataList);
                startActivity(intent);
            } else {
//                showMessage();
            }

        }
    }

    @Override
    public void deleteSelectedPhoto() {
        if (mPhotoGridAdapter != null) {
            mPhotoGridAdapter.deleteSelectedPhotos();
            if (mPhotoGridAdapter.getItemCount() == 0 && mActionMode != null) {
//                mPhotoGridAdapter.switchSelectMode(false);
                mActionMode.finish();
                mPresenter.deleteAllPhoto(getContext());
            } else {
                mPresenter.savedAddedPhotos(mPhotoGridAdapter.getData(), getContext(), true);
            }
            switchViewVisible();
        }
    }

    class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoHolder> {
        List<PhotoInfo> mDataList;
        boolean mSelectMode = false;
        ArrayList<PhotoInfo> mSelectedPhoto;

        public PhotoGridAdapter() {
            mDataList = new ArrayList<>();
            mSelectMode = false;
            mSelectedPhoto = new ArrayList<>();
        }

        public List<PhotoInfo> getData() {
            return mDataList;
        }

        public void selectAllPhoto() {
            mSelectedPhoto.clear();
            mSelectedPhoto.addAll(mDataList);
            notifyDataSetChanged();
        }

        public void deleteSelectedPhotos() {
            if (mSelectedPhoto != null && mSelectedPhoto.size() > 0) {
                for (PhotoInfo info : mSelectedPhoto)
                    mDataList.remove(info);
            }
            mSelectedPhoto.clear();
            notifyDataSetChanged();
        }

        public void switchSelectMode(boolean enable) {
            mSelectMode = enable;
        }

        public void clearSelectedPhoto() {
            mSelectedPhoto.clear();
            notifyDataSetChanged();
        }

        public void replaceData(@NonNull List<PhotoInfo> data) {
            mDataList = data;
            notifyDataSetChanged();
            mSelectedPhoto.clear();
        }

        public void addData(List<PhotoInfo> list) {
            mDataList.addAll(list);
            notifyDataSetChanged();
        }

        public void clearData() {
            mDataList.clear();
            mSelectedPhoto.clear();
            notifyDataSetChanged();
            mSelectMode = false;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater.from(getContext()).inflate(R.layout.photo_grid_item, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            PhotoInfo item = mDataList.get(position);
            if (item != null) {
                holder.bindItem(item);
            }
        }

        @Override
        public int getItemCount() {
            int count = mDataList.size();
            return count;
        }

        public List<PhotoInfo> getDataList() {
            return mDataList;
        }

        class PhotoHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.photo_layout)
            SquareLayout mPhotoBgr;

            @BindView(R.id.photo_view)
            ImageView mImg;

            @BindView(R.id.photo_selector)
            CheckBox mCheckBox;

            public PhotoHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void bindItem(PhotoInfo item) {
                if (mSelectMode) {
                    mCheckBox.setVisibility(View.VISIBLE);
                    if (mSelectedPhoto.contains(item))
                        mCheckBox.setChecked(true);
                    else mCheckBox.setChecked(false);
                } else {
                    mCheckBox.setVisibility(View.GONE);
                }
                if (item.isResImage()) {
                    UiUtils.loadImageRes(getContext(), item.getResImageId(), mImg);
                } else
                    UiUtils.loadImageFromFile(getContext(), item.getPhotoPath(), mImg);
            }

            @OnLongClick(R.id.photo_layout)
            boolean onLongClick() {
                Log.d(TAG, "onLongClick: " + getAdapterPosition());
                if (!mSelectMode) {
                    mSelectMode = true;
                    mSelectedPhoto.add(mDataList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                    onSelectModeSwitch(true);
                    return true;
                }
                return false;
            }

            @OnClick(R.id.photo_layout)
            void onClick() {
                if (mSelectMode) {
                    PhotoInfo pos = mDataList.get(getAdapterPosition());
                    if (mSelectedPhoto.contains(pos)) {
                        mSelectedPhoto.remove(pos);
                        mCheckBox.setChecked(false);
                    } else {
                        mSelectedPhoto.add(pos);
                        mCheckBox.setChecked(true);
                    }
                } else {
                    //Open Image in single activity
                    if (mDataList != null && mDataList.size() > 0) {
                        showPhoto(mDataList, getAdapterPosition());
                    }

                }
            }


        }
    }

    @OnClick(R.id.fab_slide_show)
    void onfabClick() {
        //start Slide show here
        openSlideShow();
        if(!ServiceUtils.isMyServiceRunning(ServiceUtils.recording_serviceName, getContext()))
            onRecord();
    }


    private void onRecord() {
        final Intent intent = new Intent(getActivity(), RecordingService.class);
        File folder = new File(Environment.getExternalStorageDirectory() + "/SlideShow");
        if (!folder.exists()) {
            //folder /SlideShow doesn't exist, create the folder
            folder.mkdir();
        }
        getActivity().startService(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AppConstants.PhotoGrid.REQUEST_PHOTO:
                    List<PhotoInfo> list = (List<PhotoInfo>) data.getSerializableExtra(AppConstants.PhotoSelect.EXTRA_PHOTO_RESULT);
                    if (list != null && list.size() > 0 && mPhotoGridAdapter != null) {
                        mPhotoGridAdapter.addData(list);
                        mPresenter.savedAddedPhotos(list, getContext(), false);
                        switchViewVisible();
                    }
                    break;
            }
        }
    }

    class PhotoSelectCallback implements ActionMode.Callback {
        private boolean mSelectAll = false;

        public PhotoSelectCallback() {
            mSelectAll = false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.photo_grid_select, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteSelectedPhoto();
                    return true;
                case R.id.action_select_all:
                    if (mSelectAll) {
                        mSelectAll = false;
                        item.setIcon(R.drawable.ic_textual_not_checked);
                        item.setTitle(R.string.gallery_action_select_all);
                        if (mPhotoGridAdapter != null) {
                            mPhotoGridAdapter.clearSelectedPhoto();
                        }
                    } else {
                        mSelectAll = true;
                        item.setIcon(R.drawable.ic_textual_checked);
                        item.setTitle(R.string.gallery_action_unselect_all);
                        if (mPhotoGridAdapter != null) {
                            mPhotoGridAdapter.selectAllPhoto();
                        }
                    }
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (mPhotoGridAdapter != null) {
                mPhotoGridAdapter.switchSelectMode(false);
                mPhotoGridAdapter.clearSelectedPhoto();
            }
        }
    }

    private void switchViewVisible() {
        if (mPhotoGridAdapter != null && mPhotoGridAdapter.getItemCount() > 0) {
            mNoPhotoLayout.setVisibility(View.GONE);
            mPhotoLayout.setVisibility(View.VISIBLE);
        } else {
            mNoPhotoLayout.setVisibility(View.VISIBLE);
            mPhotoLayout.setVisibility(View.GONE);
        }
    }
}
