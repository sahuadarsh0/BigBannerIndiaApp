package technited.minds.asaimagelibrary.dsl;

import java.util.List;

import technited.minds.asaimagelibrary.MediaInfo;
import technited.minds.asaimagelibrary.glide.GlideMediaHelper;


public class DSL {
    private static GlideMediaHelper mediaHelper = new GlideMediaHelper();

    public static MediaInfo image(String url) {
        return mediaHelper.image(url);
    }


    public static List<MediaInfo> images(List<String> urls) {
        return mediaHelper.images(urls);
    }

    public static List<MediaInfo> images(String... urls) {
        return mediaHelper.images(urls);
    }

}
