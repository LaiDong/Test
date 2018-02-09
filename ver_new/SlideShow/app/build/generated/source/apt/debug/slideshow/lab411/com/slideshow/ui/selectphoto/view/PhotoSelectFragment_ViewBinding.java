// Generated code from Butter Knife. Do not modify!
package slideshow.lab411.com.slideshow.ui.selectphoto.view;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import slideshow.lab411.com.slideshow.R;

public class PhotoSelectFragment_ViewBinding implements Unbinder {
  private PhotoSelectFragment target;

  private View view2131230763;

  @UiThread
  public PhotoSelectFragment_ViewBinding(final PhotoSelectFragment target, View source) {
    this.target = target;

    View view;
    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'mToolbar'", Toolbar.class);
    view = Utils.findRequiredView(source, R.id.btn_done, "field 'mDoneBtn' and method 'onDone'");
    target.mDoneBtn = Utils.castView(view, R.id.btn_done, "field 'mDoneBtn'", Button.class);
    view2131230763 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDone();
      }
    });
    target.mPhotoGrid = Utils.findRequiredViewAsType(source, R.id.photo_grid, "field 'mPhotoGrid'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PhotoSelectFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolbar = null;
    target.mDoneBtn = null;
    target.mPhotoGrid = null;

    view2131230763.setOnClickListener(null);
    view2131230763 = null;
  }
}
