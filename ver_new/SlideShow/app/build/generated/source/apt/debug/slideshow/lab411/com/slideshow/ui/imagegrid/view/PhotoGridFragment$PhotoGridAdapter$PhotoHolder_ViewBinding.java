// Generated code from Butter Knife. Do not modify!
package slideshow.lab411.com.slideshow.ui.imagegrid.view;

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

public class PhotoGridFragment$PhotoGridAdapter$PhotoHolder_ViewBinding implements Unbinder {
  private PhotoGridFragment.PhotoGridAdapter.PhotoHolder target;

  private View view2131230862;

  @UiThread
  public PhotoGridFragment$PhotoGridAdapter$PhotoHolder_ViewBinding(final PhotoGridFragment.PhotoGridAdapter.PhotoHolder target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.photo_layout, "field 'mPhotoBgr', method 'onClick', and method 'onLongClick'");
    target.mPhotoBgr = Utils.castView(view, R.id.photo_layout, "field 'mPhotoBgr'", SquareLayout.class);
    view2131230862 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    view.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View p0) {
        return target.onLongClick();
      }
    });
    target.mImg = Utils.findRequiredViewAsType(source, R.id.photo_view, "field 'mImg'", ImageView.class);
    target.mCheckBox = Utils.findRequiredViewAsType(source, R.id.photo_selector, "field 'mCheckBox'", CheckBox.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PhotoGridFragment.PhotoGridAdapter.PhotoHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mPhotoBgr = null;
    target.mImg = null;
    target.mCheckBox = null;

    view2131230862.setOnClickListener(null);
    view2131230862.setOnLongClickListener(null);
    view2131230862 = null;
  }
}
