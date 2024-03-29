package technited.minds.asaimagelibrary;

import androidx.fragment.app.FragmentManager;

public class GallerySettingsBuilderImpl implements GallerySettingsBuilder {
    private GallerySettings gallerySettings;

    public GallerySettingsBuilderImpl() {
        this.gallerySettings = new GallerySettings();
    }


    @Override
    public GallerySettingsBuilder enableZoom(boolean isZoomEnabled) {
        gallerySettings.setZoomEnabled(isZoomEnabled);
        return this;
    }

    @Override
    public GallerySettingsBuilder withFragmentManager(FragmentManager fragmentManager) {
        gallerySettings.setFragmentManager(fragmentManager);
        return this;
    }

    @Override
    public GallerySettings build() {
        return gallerySettings;
    }
}