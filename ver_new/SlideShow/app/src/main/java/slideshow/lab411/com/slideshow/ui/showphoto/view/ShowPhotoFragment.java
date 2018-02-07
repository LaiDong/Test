package slideshow.lab411.com.slideshow.ui.showphoto.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import slideshow.lab411.com.slideshow.R;
import slideshow.lab411.com.slideshow.base.BaseFragment;
import slideshow.lab411.com.slideshow.data.model.PhotoInfo;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.RecordingService;
import slideshow.lab411.com.slideshow.ui.imagegrid.service.ServiceUtils;
import slideshow.lab411.com.slideshow.ui.showphoto.IShowPhotoContract.IShowPhotoPresenter;
import slideshow.lab411.com.slideshow.ui.showphoto.IShowPhotoContract.IShowPhotoView;
import slideshow.lab411.com.slideshow.ui.showphoto.presenter.ShowPhotoPresenter;
import slideshow.lab411.com.slideshow.utils.AppConstants;
import slideshow.lab411.com.slideshow.utils.AppConstants.ShowPhoto;
import slideshow.lab411.com.slideshow.utils.UiUtils;

/**
 * Created by PL_itto on 12/10/2017.
 */

public class ShowPhotoFragment extends BaseFragment implements IShowPhotoView {
    private static final String TAG = "Lab411." + ShowPhotoFragment.class.getSimpleName();
    IShowPhotoPresenter<IShowPhotoView> mPresenter;
    private List<PhotoInfo> mPhotoInfoList = null;
    private int mCurrentPosition = -1;


    /* Views */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.photo_pager)
    ViewPager mPhotoPager;

    MenuItem mSlideShowItem;
    ShowPhotoAdapter mAdapter;
    Timer mTimer = null;
    private int mMode = ShowPhoto.MODE_NORMAL;
    TimerTask mSlideShowTask = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setHasOptionsMenu(true);
        setupData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.frag_show_photo, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        setupActionBar();
        setupView();
        return view;
    }

    void setupData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMode = bundle.getInt(ShowPhoto.EXTRA_MODE_SHOW, ShowPhoto.MODE_NORMAL);
            mPhotoInfoList = (List<PhotoInfo>) bundle.getSerializable(ShowPhoto.EXTRA_PHOTO_LIST);
            if (mMode == ShowPhoto.MODE_NORMAL)
                mCurrentPosition = bundle.getInt(ShowPhoto.EXTRA_PHOTO_POSITION, 0);
        }
        mPresenter = new ShowPhotoPresenter<>();
        mPresenter.onAttach(this);
        if (mPhotoInfoList != null)
            mAdapter = new ShowPhotoAdapter(mPhotoInfoList);
        else mAdapter = new ShowPhotoAdapter(new ArrayList<PhotoInfo>());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        if (mMode == ShowPhoto.MODE_NORMAL) {
            if (mCurrentPosition != -1 && mAdapter != null && mAdapter.getCount() > 0) {
                mPhotoPager.setCurrentItem(mCurrentPosition);
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.menu, menu);
        mSlideShowItem = menu.findItem(R.id.action_slide);
        if (mMode == ShowPhoto.MODE_SLIDE)
            startSlideShow();
    }

    void setupView() {
        if (mAdapter == null)
            mAdapter = new ShowPhotoAdapter(mPhotoInfoList);
        mPhotoPager.setAdapter(mAdapter);
        mPhotoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean isFirst = true;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isFirst && position == 0 && positionOffset == 0) {
                    onPageSelected(0);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mAdapter != null) {
                    PhotoInfo info = mAdapter.getItem(position);
                    if (info != null) {
                        updateCurrentItem(info);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void setupActionBar() {
        getParentActivity().setSupportActionBar(mToolbar);
        getParentActivity().getSupportActionBar().setDisplayShowHomeEnabled(true);
        getParentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if (mPresenter != null)
            mPresenter.onDetach();
        stopSlideShow();
        getActivity().finish();
        ServiceUtils.stopService(getContext());

        super.onDestroy();
    }

    @Override
    public void updateCurrentItem(PhotoInfo info) {
        mToolbar.setTitle(info.getDateTaken());
        mToolbar.setSubtitle(info.getTimeTaken());
    }

    @Override
    public void startSlideShow() {
        if (mAdapter != null && mAdapter.getCount() > 0) {
            mSlideShowItem.setIcon(R.drawable.ic_stop);
            mTimer = new Timer();
            mSlideShowTask = new TimerTask() {
                @Override
                public void run() {
                    getParentActivity().runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {
                            if (mAdapter != null) {
                                int count = mAdapter.getCount();
                                if (count > 0) {
                                    int currentPosition = mPhotoPager.getCurrentItem();
                                    if (currentPosition < count - 1) {
                                        currentPosition++;
                                    } else {
                                        currentPosition = 0;
                                    }
                                    mPhotoPager.setCurrentItem(currentPosition);
                                }
                            }
                        }
                    });

                }
            };
            mTimer.schedule(mSlideShowTask, 3000, 2000);
        }
    }

    @Override
    public void stopSlideShow() {
        if (mSlideShowTask != null) {
            mSlideShowItem.setIcon(R.drawable.ic_slideshow_dark);
            mSlideShowTask.cancel();
            mSlideShowTask = null;
        }
    }

    class ShowPhotoAdapter extends PagerAdapter {
        List<PhotoInfo> mList;
        LayoutInflater mInflater;

        public ShowPhotoAdapter(List<PhotoInfo> data) {
            mList = data;
            mInflater = LayoutInflater.from(getContext());
        }

        public PhotoInfo getItem(int pos) {
            if (mList != null && mList.size() > pos) {
                return mList.get(pos);
            }
            return null;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem: " + position);
            View view = mInflater.inflate(R.layout.show_photo_item, container, false);
            ImageView img = view.findViewById(R.id.photo_item);
            PhotoInfo info = mList.get(position);
            if (info.isResImage())
                UiUtils.loadImageRes(getContext(), info.getResImageId(), img);
            else
                UiUtils.loadImageFromFile(getContext(), info.getPhotoPath(), img);
            container.addView(view, 0);
            return view;
        }

        @Override
        public int getCount() {
            if (mList != null)
                return mList.size();
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getParentActivity().onBackPressed();
                return true;
            case R.id.action_slide:
                if (mSlideShowTask != null) {
                    stopSlideShow();
                    onRecord(false);
                } else {
                    startSlideShow();
                    if(!ServiceUtils.isMyServiceRunning(ServiceUtils.recording_serviceName, getContext())){
                        onRecord(true);
                        Log.d("laidong", "service start");
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onRecord(boolean start) {
        final Intent intent = new Intent(getActivity(), RecordingService.class);
        if (start) {
            File folder = new File(Environment.getExternalStorageDirectory() + "/SlideShow");
            if (!folder.exists()) {
                //folder /SlideShow doesn't exist, create the folder
                folder.mkdir();
            }
            getActivity().startService(intent);
        } else {
            getActivity().stopService(intent);
        }
    }

}
