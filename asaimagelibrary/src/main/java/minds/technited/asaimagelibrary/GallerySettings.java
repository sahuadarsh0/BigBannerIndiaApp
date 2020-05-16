package minds.technited.asaimagelibrary;

import androidx.fragment.app.FragmentManager;


public class GallerySettings {
    private boolean isZoomEnabled;
    private FragmentManager fragmentManager;

    public static GallerySettingsBuilder from(FragmentManager fm) {
        GallerySettingsBuilder builder = new GallerySettingsBuilderImpl();
        builder.withFragmentManager(fm);
        return builder;
    }

    public boolean isZoomEnabled() {
        return isZoomEnabled;
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        isZoomEnabled = zoomEnabled;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
}