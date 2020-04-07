package minds.technited.asavideoslider;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;

public class MediaSlider extends Fragment {
    private ViewPager mPager;
    private TextView slider_media_number;
    private long playbackPosition = 0;
    private int currentWindow = 0;
    private boolean  isMediaCountVisible;
    private String title;
    private ArrayList<String> urlList;
    private String mediaType;
    private String titleTextColor;
    private int startPosition = 0;

    Context context;


    public MediaSlider() {
        // Required empty public constructor
    }


    public MediaSlider(Context context, ArrayList<String> urlList, String mediaType, boolean isTitleVisible, boolean isMediaCountVisible, boolean isNavigationVisible, String title, String titleTextColor) {
        this.context = context;
        this.urlList = urlList;
        this.mediaType = mediaType;
        this.isMediaCountVisible = isMediaCountVisible;
        this.title = title;
        this.titleTextColor = titleTextColor;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slider,
                container, false);
        initViewsAndSetAdapter(view);
        return view;
    }


    private void setStartPosition() {
        if (startPosition >= 0) {
            if (startPosition > urlList.size()) {
                mPager.setCurrentItem((urlList.size() - 1));
                return;
            }
            mPager.setCurrentItem(startPosition);
        } else {
            mPager.setCurrentItem(0);
        }
        mPager.setOffscreenPageLimit(0);
    }

    private void initViewsAndSetAdapter(View view) {
        RelativeLayout statusLayout = view.findViewById(R.id.status_holder);
        TextView slider_title = view.findViewById(R.id.title);
        slider_media_number = view.findViewById(R.id.number);
        ImageView left = view.findViewById(R.id.left_arrow);
        ImageView right = view.findViewById(R.id.right_arrow);
        mPager = view.findViewById(R.id.pager);
        mPager.setAdapter(new ScreenSlidePagerAdapter(getContext(), urlList, mediaType));
        setStartPosition();
        String hexRegex = "/^#(?:(?:[\\da-f]{3}){1,2}|(?:[\\da-f]{4}){1,2})$/i";
        if ( isMediaCountVisible) {

            statusLayout.setBackgroundColor(getResources().getColor(R.color.transparent));

        }

        if (isMediaCountVisible) {
            slider_media_number.setVisibility(View.VISIBLE);
            slider_media_number.setText((mPager.getCurrentItem() + 1) + "/" + urlList.size());
        }
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                View viewTag = mPager.findViewWithTag("view" + i);
                PlayerView simpleExoPlayerView = viewTag.findViewById(R.id.video_view);
                if (simpleExoPlayerView.getPlayer() != null) {
                    SimpleExoPlayer player = (SimpleExoPlayer) simpleExoPlayerView.getPlayer();
                    playbackPosition = player.getCurrentPosition();
                    currentWindow = player.getCurrentWindowIndex();
                    player.setPlayWhenReady(true);
//                        player.addListener(new Player.EventListener() {
//                            @Override
//                            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                                if (playbackState == Player.STATE_ENDED) {
//                                    int i = mPager.getCurrentItem();
//                                    if (i + 1 != urlList.size()) {
//                                        mPager.setCurrentItem(i + 1);
//                                        slider_media_number.setText((mPager.getCurrentItem()) + 1 + "/" + urlList.size());
//                                    } else {
//                                        mPager.setCurrentItem(0);
//                                        slider_media_number.setText((1) + "/" + urlList.size());
//                                    }
//                                }
//                            }
//                        });
                }
            }


            @Override
            public void onPageSelected(int i) {
                slider_media_number.setText((mPager.getCurrentItem() + 1) + "/" + urlList.size());
                if (mediaType.equalsIgnoreCase("video")) {
                    View viewTag = mPager.findViewWithTag("view" + i);
                    PlayerView simpleExoPlayerView = viewTag.findViewById(R.id.video_view);
                    if (simpleExoPlayerView.getPlayer() != null) {
                        SimpleExoPlayer player = (SimpleExoPlayer) simpleExoPlayerView.getPlayer();
                        Uri mediaUri = Uri.parse(urlList.get(i));
                        MediaSource mediaSource = new ProgressiveMediaSource.Factory(
                                new DefaultHttpDataSourceFactory("media-slider-view")).
                                createMediaSource(mediaUri);
                        playbackPosition = player.getCurrentPosition();
                        currentWindow = player.getCurrentWindowIndex();
                        player.prepare(mediaSource, true, true);
                        player.setPlayWhenReady(false);
                        player.seekTo(0, 0);

                    }


                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }


    private class ScreenSlidePagerAdapter extends PagerAdapter {
        private Context context;
        private ArrayList<String> urlList;
        private String token;
        SimpleExoPlayer player;
        PlayerView simpleExoPlayerView;
        MediaSource mediaSource;
        //ProgressBar mProgressBar;


        private ScreenSlidePagerAdapter(Context context, ArrayList<String> urlList, String token) {
            this.context = context;
            this.urlList = urlList;
            this.token = token;

        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = null;

            if (token.equalsIgnoreCase("video")) {
                view = inflater.inflate(R.layout.video_item, container, false);
                simpleExoPlayerView = view.findViewById(R.id.video_view);
                simpleExoPlayerView.setTag("view" + position);
                player = new SimpleExoPlayer.Builder(context).build();
                DataSource.Factory dataSourceFactory =
                        new DefaultHttpDataSourceFactory("media-slider-view");
                Uri mediaUri = Uri.parse(urlList.get(position));
                mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaUri);

//                player = ExoPlayerFactory.newSimpleInstance(
//                        new DefaultRenderersFactory(context),
//                        new DefaultTrackSelector(), new DefaultLoadControl());
//                mediaSource = new ExtractorMediaSource.Factory(
//                        new DefaultHttpDataSourceFactory("media-slider-view")).
//                        createMediaSource(mediaUri);

                simpleExoPlayerView.setPlayer(player);
                player.prepare(mediaSource, true, true);
                if (position != 0)
                    player.setPlayWhenReady(false);
                else
                    player.setPlayWhenReady(true);
                player.seekTo(0, 0);
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return urlList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return (view == o);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }


    }
}
