package minds.technited.asaimagelibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.chrisbanes.photoview.PhotoView;

public class ImageFragment extends Fragment {

    private MediaInfo mMediaInfo;

    private MyViewPager viewPager;
    private PhotoView photoView;
    private ScrollGallery.OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(ScrollGallery.OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public void setMediaInfo(MediaInfo mediaInfo) {
        mMediaInfo = mediaInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.image_fragment, container, false);
        photoView = rootView.findViewById(R.id.photoView);
        if (onImageClickListener != null) {
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    onImageClickListener.onClick();
                }
            });
        }
        viewPager = getActivity().findViewById(R.id.viewPager);

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(Constants.IS_LOCKED, false);
            viewPager.setLocked(isLocked);
        }

        loadImageToView();

        return rootView;
    }

    private void loadImageToView() {
        if (mMediaInfo != null) {
            mMediaInfo.getLoader().loadMedia(getActivity(), photoView, new MediaLoader.SuccessCallback() {
                @Override
                public void onSuccess() {
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(Constants.IS_LOCKED, viewPager.isLocked());
        }
        super.onSaveInstanceState(outState);
    }

    private boolean isViewPagerActive() {
        return viewPager != null;
    }

    private boolean isBackgroundImageActive() {
        return photoView != null && photoView.getDrawable() != null;
    }
}