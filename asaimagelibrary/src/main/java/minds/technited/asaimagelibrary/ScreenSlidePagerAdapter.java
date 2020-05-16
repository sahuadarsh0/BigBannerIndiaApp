package minds.technited.asaimagelibrary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<MediaInfo> mListOfMedia;

    private boolean isZoom = false;

    private ScrollGallery.OnImageClickListener onImageClickListener;

    public ScreenSlidePagerAdapter(FragmentManager fm,
                                   List<MediaInfo> listOfMedia,
                                   boolean isZoom,
                                   ScrollGallery.OnImageClickListener onImageClickListener) {
        super(fm);
        this.mListOfMedia = listOfMedia;
        this.isZoom = isZoom;
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position < mListOfMedia.size()) {
            MediaInfo mediaInfo = mListOfMedia.get(position);
            fragment = loadImageFragment(mediaInfo, position);
        }

        return fragment;
    }

    private Fragment loadImageFragment(MediaInfo mediaInfo, int position) {
        ImageFragment fragment = new ImageFragment();
        fragment.setRetainInstance(true);
        fragment.setMediaInfo(mediaInfo);

        if (onImageClickListener != null) {
            fragment.setOnImageClickListener(onImageClickListener);
        }

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.ZOOM, isZoom);
        bundle.putInt("position", position);

        fragment.setArguments(bundle);
        return fragment;
    }

    public void removeItem(int position) {
        mListOfMedia.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListOfMedia.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}