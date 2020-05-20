package minds.technited.asaimagelibrary.glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import minds.technited.asaimagelibrary.MediaHelper;
import minds.technited.asaimagelibrary.MediaInfo;


public class GlideMediaHelper implements MediaHelper {
    @Override
    public MediaInfo image(String url) {
        return MediaInfo.mediaLoader(new GlideImageLoader(url));
    }

    @Override
    public List<MediaInfo> images(List<String> urls) {
        List<MediaInfo> medias = new ArrayList<>();

        for (String url : urls) {
            medias.add(mediaInfo(url));
        }

        return medias;
    }

    @Override
    public List<MediaInfo> images(String... urls) {
        return images(Arrays.asList(urls));
    }

    private MediaInfo mediaInfo(String url) {
        return mediaInfo(url);
    }


}
