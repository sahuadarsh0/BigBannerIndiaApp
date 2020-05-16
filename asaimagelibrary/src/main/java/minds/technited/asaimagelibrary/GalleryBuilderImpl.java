package minds.technited.asaimagelibrary;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class GalleryBuilderImpl implements GalleryBuilder {
    private ScrollGallery galleryView;
    private GallerySettings settings;

    private ScrollGallery.OnImageClickListener onImageClickListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    private List<MediaInfo> medias;

    public GalleryBuilderImpl(ScrollGallery galleryView) {
        this.galleryView = galleryView;
        this.medias = new ArrayList<>();
    }

    @Override
    public GalleryBuilder settings(GallerySettings settings) {
        this.settings = settings;
        return this;
    }

    @Override
    public GalleryBuilder onImageClickListener(ScrollGallery.OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
        return this;
    }


    @Override
    public GalleryBuilder onPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        return this;
    }

    @Override
    public GalleryBuilder add(MediaInfo media) {
        this.medias.add(media);
        return this;
    }

    @Override
    public GalleryBuilder add(List<MediaInfo> medias) {
        this.medias.addAll(medias);
        return this;
    }

    @Override
    public ScrollGallery build() {
        // check here all parameters

        return galleryView
                .setZoom(settings.isZoomEnabled())
                .addOnImageClickListener(onImageClickListener)
                .setFragmentManager(settings.getFragmentManager())
                .addOnPageChangeListener(onPageChangeListener)
                .addMedia(medias);
    }
}