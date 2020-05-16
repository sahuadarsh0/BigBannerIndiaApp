package minds.technited.asaimagelibrary;

import android.content.Context;
import android.widget.ImageView;

public interface MediaLoader {


    boolean isImage();


    void loadMedia(Context context, ImageView imageView, SuccessCallback callback);


    interface SuccessCallback {
        void onSuccess();
    }
}