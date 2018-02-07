// Generated code from Butter Knife. Do not modify!
package slideshow.lab411.com.slideshow.ui.imagegrid.view;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import slideshow.lab411.com.slideshow.R;

public class PhotoGridFragment_ViewBinding implements Unbinder {
  private PhotoGridFragment target;

  private View view2131230796;

  @UiThread
  public PhotoGridFragment_ViewBinding(final PhotoGridFragment target, View source) {
    this.target = target;

    View view;
    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'mToolbar'", Toolbar.class);
    target.mPhotoLayout = Utils.findRequiredViewAsType(source, R.id.photo_layout, "field 'mPhotoLayout'", CoordinatorLayout.class);
    target.mPhotoGrid = Utils.findRequiredViewAsType(source, R.id.photo_grid, "field 'mPhotoGrid'", RecyclerView.class);
    target.mNoPhotoLayout = Utils.findRequiredViewAsType(source, R.id.no_photo_layout, "field 'mNoPhotoLayout'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.fab_slide_show, "method 'onfabClick'");
    view2131230796 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onfabClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    PhotoGridFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolbar = null;
    target.mPhotoLayout = null;
    target.mPhotoGrid = null;
    target.mNoPhotoLayout = null;

    view2131230796.setOnClickListener(null);
    view2131230796 = null;
  }
}
