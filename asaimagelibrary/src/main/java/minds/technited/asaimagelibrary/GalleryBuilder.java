package minds.technited.asaimagelibrary;

import androidx.viewpager.widget.ViewPager;

import java.util.List;

public interface GalleryBuilder {
    /**
     * Sets up settings for gallery
     *
     * @param settings contains options for gallery
     * @return GalleryBuilder object
     */
    GalleryBuilder settings(GallerySettings settings);

    GalleryBuilder onImageClickListener(ScrollGallery.OnImageClickListener listener);


    GalleryBuilder onPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    /**
     * Adds single MediaInfo to gallery
     *
     * @param media is a single MediaInfo object
     * @return GalleryBuilder object
     */
    GalleryBuilder add(MediaInfo media);

    /**
     * Adds a list of MediaInfos to gallery
     *
     * @param medias is a list of MediaInfo objects
     * @return GalleryBuilder object
     */
    GalleryBuilder add(List<MediaInfo> medias);

    /**
     * Builds gallery from provided medias
     *
     * @return initialized ScrollGalleryView
     */
    ScrollGallery build();
}