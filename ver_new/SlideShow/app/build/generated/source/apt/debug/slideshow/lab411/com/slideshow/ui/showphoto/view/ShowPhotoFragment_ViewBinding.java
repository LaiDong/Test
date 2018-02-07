// Generated code from Butter Knife. Do not modify!
package slideshow.lab411.com.slideshow.ui.showphoto.view;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import slideshow.lab411.com.slideshow.R;

public class ShowPhotoFragment_ViewBinding implements Unbinder {
  private ShowPhotoFragment target;

  @UiThread
  public ShowPhotoFragment_ViewBinding(ShowPhotoFragment target, View source) {
    this.target = target;

    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'mToolbar'", Toolbar.class);
    target.mPhotoPager = Utils.findRequiredViewAsType(source, R.id.photo_pager, "field 'mPhotoPager'", ViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShowPhotoFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolbar = null;
    target.mPhotoPager = null;
  }
}
