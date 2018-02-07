package slideshow.lab411.com.slideshow.ui.selectphoto.view;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.itto.squarelayout.SquareLayout;
import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.base.BaseFragment;
import slideshow.lab411.com.slideshow.data.model.PhotoAlbumList;
import slideshow.lab411.com.slideshow.data.model.PhotoFolderInfo;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.ServiceUtils;
import slideshow.lab411.com.slideshow.ui.selectphoto.IPhotoSelectContract.IPhotoSelectPresenter;
import slideshow.lab411.com.slideshow.ui.selectphoto.IPhotoSelectContract.IPhotoSelectView;
import slideshow.lab411.com.slideshow.ui.selectphoto.presenter.PhotoSelectPresenter;
import slideshow.lab411.com.slideshow.utils.AppConstants;
import slideshow.lab411.com.slideshow.utils.UiUtils;

public class PhotoSelectFragment extends BaseFragment implements IPhotoSelectView {
    private static final String TAG = "Lab411." + PhotoSelectFragment.class.getSimpleName();

    /*Views */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_done)
    Button mDoneBtn;
    @BindView(R.id.photo_grid)
    RecyclerView mPhotoGrid;

    PhotoGridAdapter mAdapter;
    IPhotoSelectPresenter<IPhotoSelectView> mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new PhotoSelectPresenter<>();
        mPresenter.onAttach(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_photo_select, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        setup();
        setupActionBar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadImage(getContext());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getParentActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        //ServiceUtils.startService(getContext());
    }


    @Override
    public void onDestroy() {
        if (mPresenter != null)
            mPresenter.onDetach();

        super.onDestroy();
    }

    @Override
    public void updateListPhoto(@NonNull PhotoAlbumList data) {
        Log.i(TAG, "updateListPhoto: " + data.getPhotoCount());
        mAdapter.replaceData(data);
    }

    void setupActionBar() {
        getParentActivity().setSupportActionBar(mToolbar);
        getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setup() {
        mAdapter = new PhotoGridAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case PhotoGridAdapter.TYPE_HEADER:
                        Log.d(TAG, "getSpanSize: " + position + "/" + 1);
                        return 4;
                    case PhotoGridAdapter.TYPE_ITEM:
                        Log.d(TAG, "getSpanSize: " + position + "/" + 4);
                        return 1;
                    default:
                        return 0;
                }
            }
        });
        mPhotoGrid.setLayoutManager(layoutManager);
        mPhotoGrid.setAdapter(mAdapter);

    }

    class PhotoGridAdapter extends RecyclerView.Adapter {
        public static final int TYPE_HEADER = 1;
        public static final int TYPE_ITEM = 0;
        private PhotoAlbumList mAlbumList;
        private int[] mItemTypes;
        LayoutInflater mInflater;
        SparseArray<PhotoInfo> mSelectedPhoto;
        SparseArray<Object> mDataList;

        @Override
        public int getItemViewType(int position) {
            if (mItemTypes.length <= position) {
                return TYPE_HEADER;
            }
            return mItemTypes[position];
        }

        public PhotoGridAdapter() {
            mDataList = new SparseArray<>();
            mAlbumList = new PhotoAlbumList();
            mInflater = LayoutInflater.from(getContext());
            mSelectedPhoto = new SparseArray<>();
            initViewType();
        }

        public void replaceData(PhotoAlbumList albumList) {
            mSelectedPhoto.clear();
            mAlbumList = albumList;
            mDataList.clear();
            initViewType();
            notifyDataSetChanged();
        }

        public SparseArray<PhotoInfo> getSelectedPhoto() {
            return mSelectedPhoto;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (viewType == TYPE_HEADER) {
                v = mInflater.inflate(R.layout.header_layout, parent, false);
                return new HeaderHolder(v);
            } else {
                v = mInflater.inflate(R.layout.photo_grid_item_with_span, parent, false);
                return new ItemHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            try{
                if (holder instanceof ItemHolder) {
                    ((ItemHolder) holder).bindItem((PhotoInfo) mDataList.get(position));
                } else {
                    ((HeaderHolder) holder).bindItem((String) mDataList.get(position));
                }
            }catch(Exception e){
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return mAlbumList.getAlbumCount() + mAlbumList.getPhotoCount();
        }

        class HeaderHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.header_text)
            TextView mHeaderText;

            public HeaderHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void bindItem(String name) {
                mHeaderText.setText(name);
            }
        }

        class ItemHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.photo_layout)
            SquareLayout mPhotoBgr;

            @BindView(R.id.photo_view)
            ImageView mImg;
            @BindView(R.id.photo_selector)
            CheckBox mSelector;

            public ItemHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void bindItem(PhotoInfo item) {
                mSelector.setVisibility(View.VISIBLE);
                if (mSelectedPhoto.indexOfKey(getAdapterPosition()) >= 0)
                    mSelector.setChecked(true);
                else
                    mSelector.setChecked(false);
                UiUtils.loadImageFromFile(getContext(), item.getPhotoPath(), mImg);
            }

            @OnClick(R.id.photo_layout)
            void onItemClick() {
                Log.i(TAG, "onItemClick: ");
                int pos = getAdapterPosition();
                PhotoInfo info = (PhotoInfo) mDataList.get(pos);

                if (mSelectedPhoto.indexOfKey(pos) >= 0) {
                    mSelectedPhoto.remove(pos);
                    mSelector.setChecked(false);
                } else {
                    mSelector.setChecked(true);
                    mSelectedPhoto.put(pos, info);
                }
            }
        }

        void initViewType() {
            mItemTypes = new int[mAlbumList.getPhotoCount() + mAlbumList.getAlbumCount()];
            List<PhotoFolderInfo> list = mAlbumList.getAlbumList();
            int currentPos = 0;
            if (mItemTypes.length == 0)
                return;
            for (PhotoFolderInfo info : list) {
                mDataList.put(currentPos, info.getFolderName());
                mItemTypes[currentPos] = TYPE_HEADER;
                for (int i = 0; i < info.getPhotoCount(); i++) {
                    PhotoInfo photoInfo = info.getPhoto(i);
                    currentPos++;
                    mDataList.put(currentPos, photoInfo);
                }
                if (++currentPos >= mItemTypes.length) {
                    return;
                }
            }
        }

    }

    @OnClick(R.id.btn_done)
    void onDone() {
        if (mAdapter != null) {
            SparseArray<PhotoInfo> data = mAdapter.getSelectedPhoto();
            if (data != null && data.size() > 0) {

                List<PhotoInfo> result = new ArrayList<>();
                for(int i=0; i<data.size(); i++){
                    result.add(data.valueAt(i));
                }
                Intent intent = new Intent();
                intent.putExtra(AppConstants.PhotoSelect.EXTRA_PHOTO_RESULT, (Serializable) result);
                getParentActivity().setResult(Activity.RESULT_OK, intent);
                getParentActivity().finish();
            }
        }
    }
}
