package slideshow.lab411.com.slideshow.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by PL_itto on 12/12/2017.
 */

public class CustomViewPager extends ViewPager {
    private boolean mSwipeEnable = true;

    public CustomViewPager(Context context) {
        super(context);
        mSwipeEnable = true;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSwipeEnable = true;
    }

    public boolean isSwipeEnable() {
        return mSwipeEnable;
    }

    public void setSwipeEnable(boolean swipeEnable) {
        mSwipeEnable = swipeEnable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isSwipeEnable())
            return super.onInterceptTouchEvent(ev);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mSwipeEnable) return super.onTouchEvent(ev);
        return false;
    }
}
