package minds.technited.asaimagelibrary.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import minds.technited.asaimagelibrary.MediaLoader;


public class GlideImageLoader implements MediaLoader {

    private String url;
    private int width;
    private int height;
    private RequestOptions requestOptions;

    public GlideImageLoader(String url) {
        this.url = url;
        requestOptions = new RequestOptions();
    }

    public GlideImageLoader(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
        requestOptions = new RequestOptions()
                .override(width, height);

    }

    @Override
    public boolean isImage() {
        return true;
    }

    @Override
    public void loadMedia(Context context, ImageView imageView, final SuccessCallback callback) {
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context).applyDefaultRequestOptions(requestOptions)
                .load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                callback.onSuccess();
                return false;
            }
        }).into(imageView);
    }


}
