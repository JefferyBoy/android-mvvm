package xyz.mxlei.mvvmx.binding;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author mxlei
 * @date 2020/7/12
 */
public class BindingImageView {

    @BindingAdapter(value = {"binding_src", "binding_placeholderRes"}, requireAll = false)
    public static void setAdapter(ImageView imageView, String url, Drawable placeHolderRes) {
        //使用Glide加载图片
        Glide.with(imageView).load(url)
                .apply(new RequestOptions().placeholder(placeHolderRes))
                .dontAnimate()
                .into(imageView);
    }
}
