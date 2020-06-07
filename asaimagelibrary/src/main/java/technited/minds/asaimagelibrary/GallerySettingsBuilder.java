package technited.minds.asaimagelibrary;

import androidx.fragment.app.FragmentManager;

public interface GallerySettingsBuilder {
    GallerySettingsBuilder enableZoom(boolean isZoomEnabled);

    GallerySettingsBuilder withFragmentManager(FragmentManager fragmentManager);

    GallerySettings build();
}