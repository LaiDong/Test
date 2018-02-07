// Generated code from Butter Knife. Do not modify!
package slideshow.lab411.com.slideshow.ui.selectphoto.view;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import pl.itto.squarelayout.SquareLayout;
import slideshow.lab411.com.slideshow.R;

public class PhotoSelectFragment$PhotoGridAdapter$ItemHolder_ViewBinding implements Unbinder {
  private PhotoSelectFragment.PhotoGridAdapter.ItemHolder target;

  private View view2131230857;

  @UiThread
  public PhotoSelectFragment$PhotoGridAdapter$ItemHolder_ViewBinding(final PhotoSelectFragment.PhotoGridAdapter.ItemHolder target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.photo_layout, "field 'mPhotoBgr' and method 'onItemClick'");
    target.mPhotoBgr = Utils.castView(view, R.id.photo_layout, "field 'mPhotoBgr'", SquareLayout.class);
    view2131230857 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onItemClick();
      }
    });
    target.mImg = Utils.findRequiredViewAsType(source, R.id.photo_view, "field 'mImg'", ImageView.class);
    target.mSelector = Utils.findRequiredViewAsType(source, R.id.photo_selector, "field 'mSelector'", CheckBox.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PhotoSelectFragment.PhotoGridAdapter.ItemHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mPhotoBgr = null;
    target.mImg = null;
    target.mSelector = null;

    view2131230857.setOnClickListener(null);
    view2131230857 = null;
  }
}
