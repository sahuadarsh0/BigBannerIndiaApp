package technited.minds.asaimagelibrary;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScrollGallery extends LinearLayout {
    // Listeners
    private final ViewPager.SimpleOnPageChangeListener viewPagerChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {


        }
    };
    private FragmentManager fragmentManager;
    private Context context;
    private Point displayProps;
    private ScreenSlidePagerAdapter pagerAdapter;
    private List<MediaInfo> mListOfMedia;
    // Options
    private boolean zoomEnabled;
    // Views
    private MyViewPager viewPager;
    private boolean initOnce;
    private OnImageClickListener onImageClickListener;


    private OnImageClickListener innerOnImageClickListener = new OnImageClickListener() {
        @Override
        public void onClick(int position) {

            if (onImageClickListener != null) onImageClickListener.onClick(position);
        }
    };


    public ScrollGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mListOfMedia = new ArrayList<>();

        setOrientation(VERTICAL);
        displayProps = getDisplaySize();
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.scroll_gallery, this, true);

    }

    public static GalleryBuilder from(ScrollGallery galleryView) {
        return new GalleryBuilderImpl(galleryView);
    }

    public ScrollGallery setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        initializeViewPager();
        return this;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * Set up OnImageClickListener for your gallery images
     * You should set OnImageClickListener only before setFragmentManager call!
     *
     * @param onImageClickListener which is called when you click on image
     * @return ScrollGalleryView
     */
    public ScrollGallery addOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
        return this;
    }


    /**
     * Add OnPageChangeListener for internal ViewPager
     *
     * @param listener is an OnPageChange listener which is used by internal ViewPager
     * @return ScrollGalleryView object
     */
    public ScrollGallery addOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
        viewPager.clearOnPageChangeListeners();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                listener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                listener.onPageScrollStateChanged(state);
            }
        });

        return this;
    }

    public ScrollGallery addMedia(MediaInfo mediaInfo) {
        if (mediaInfo == null) {
            throw new NullPointerException("Infos may not be null!");
        }
        return addMedia(Collections.singletonList(mediaInfo));
    }

    public ScrollGallery addMedia(List<MediaInfo> infos) {
        if (infos == null) {
            throw new NullPointerException("Infos may not be null!");
        }

        for (MediaInfo info : infos) {
            mListOfMedia.add(info);

            info.getLoader();

            pagerAdapter.notifyDataSetChanged();
        }

        // Set image description only once on first image, when user added first medias
        if (!initOnce && !infos.isEmpty()) {

            initOnce = true;
        }

        return this;
    }

    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }

    public ScrollGallery setCurrentItem(int i) {
        viewPager.setCurrentItem(i, false);
        return this;
    }

    public ScrollGallery setZoom(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
        return this;
    }


    /**
     * Remove all images from gallery
     */
    public void clearGallery() {
        // remove all media infos
        mListOfMedia.clear();

        pagerAdapter = new ScreenSlidePagerAdapter(
                fragmentManager,
                mListOfMedia,
                zoomEnabled,
                innerOnImageClickListener
        );

        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * Remove a media from the gallery
     *
     * @param position media's position to remove
     */
    public void removeMedia(int position) {
        if (position >= mListOfMedia.size() || position < 0) {
            return;
        }
        pagerAdapter.removeItem(position);
    }

    private Point getDisplaySize() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point;
    }

    private void initializeViewPager() {
        viewPager = findViewById(R.id.viewPager);

        pagerAdapter = new ScreenSlidePagerAdapter(
                fragmentManager,
                mListOfMedia,
                zoomEnabled,
                innerOnImageClickListener
        );

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerChangeListener);
    }

    private int calculateInSampleSize(int imgWidth, int imgHeight, int maxWidth, int maxHeight) {
        int inSampleSize = 1;
        while (imgWidth / inSampleSize > maxWidth || imgHeight / inSampleSize > maxHeight) {
            inSampleSize *= 2;
        }
        return inSampleSize;
    }


    public interface OnImageClickListener {
        void onClick(int position);
    }
}
