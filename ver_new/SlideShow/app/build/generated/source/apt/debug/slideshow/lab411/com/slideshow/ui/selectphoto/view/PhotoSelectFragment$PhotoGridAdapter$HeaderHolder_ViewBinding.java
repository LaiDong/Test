// Generated code from Butter Knife. Do not modify!
package slideshow.lab411.com.slideshow.ui.selectphoto.view;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import slideshow.lab411.com.slideshow.R;

public class PhotoSelectFragment$PhotoGridAdapter$HeaderHolder_ViewBinding implements Unbinder {
  private PhotoSelectFragment.PhotoGridAdapter.HeaderHolder target;

  @UiThread
  public PhotoSelectFragment$PhotoGridAdapter$HeaderHolder_ViewBinding(PhotoSelectFragment.PhotoGridAdapter.HeaderHolder target,
      View source) {
    this.target = target;

    target.mHeaderText = Utils.findRequiredViewAsType(source, R.id.header_text, "field 'mHeaderText'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PhotoSelectFragment.PhotoGridAdapter.HeaderHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mHeaderText = null;
  }
}
