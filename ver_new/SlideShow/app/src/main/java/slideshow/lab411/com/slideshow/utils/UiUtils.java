package slideshow.lab411.com.slideshow.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import slideshow.lab411.com.slideshow.R;


/**
 * Created by PL_itto on 11/22/2017.
 */

public class UiUtils {
    public static void loadImageRes(Context context, @StringRes int resId, final ImageView view) {
        final RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.mipmap.ic_launcher);
//        requestOptions.centerInside();
//        requestOptions.override(view.getWidth(), view.getHeight());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).asBitmap().load(resId).apply(requestOptions).into(view);
    }

    public static void loadImageFromFile(Context context, String filepath, ImageView view) {
        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(new File(filepath)).apply(requestOptions).into(view);
    }

}
