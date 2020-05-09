package minds.technited.asavideoslider;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheKeyFactory;
import com.google.android.exoplayer2.upstream.cache.CacheUtil;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {


    private boolean mState = false;
    private int counter = 0;
    private Context context;
    private ArrayList<String> urlList;
    private TextView slider_media_number;
    private ViewPager2 pager2;
    private SliderViewHolder[] mholder;
    private int currentPagePosition = 0;

    public SliderAdapter(Context context,
                         ArrayList<String> urlList,
                         TextView slider_media_number,
                         ViewPager2 pager2) {
        this.context = context;
        this.urlList = urlList;
        this.slider_media_number = slider_media_number;
        this.pager2 = pager2;
        mholder = new SliderViewHolder[urlList.size()];
    }


    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_item, parent, false);

        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SliderViewHolder holder, final int position) {

        mholder[position] = holder;

        holder.mediaUri = Uri.parse(urlList.get(position));
//        if (first >= 0 && first <= urlList.size()) {
//            holder.mediaSource = new ProgressiveMediaSource.Factory(holder.dataSourceFactory)
//                    .createMediaSource(holder.mediaUri);
//        } else {
        holder.mediaSource = new ProgressiveMediaSource.Factory(holder.cacheDataSourceFactory)
                .createMediaSource(holder.mediaUri);
//        }

        holder.simpleExoPlayerView.setPlayer(holder.player);
        holder.simpleExoPlayerView.hideController();
        holder.simpleExoPlayerView.setControllerAutoShow(false);
        holder.player.prepare(holder.mediaSource, true, true);
        holder.player.seekTo(0, 0);
        if (position == 0)
            holder.player.setPlayWhenReady(true);


        holder.player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    int i = pager2.getCurrentItem();
                    if (position == i) {
                        if (i + 1 == urlList.size()) {
                            pager2.setCurrentItem(0);
                            slider_media_number.setText((pager2.getCurrentItem()) + "/" + urlList.size());

                        } else {

                            pager2.setCurrentItem(i + 1);
                            slider_media_number.setText((pager2.getCurrentItem() + 1) + "/" + urlList.size());
                        }

                    }

                }
                holder.showProgressBar(holder.player, holder.progressBar);

            }
        });


        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position1, float positionOffset, int positionOffsetPixels) {
                slider_media_number.setText((pager2.getCurrentItem() + 1) + "/" + urlList.size());
                if (position != position1) {
                    if (positionOffsetPixels == 0) {
                        holder.player.seekTo(0);
                        holder.player.setPlayWhenReady(false);

                    }
                }
                if (position == position1) {
                    holder.player.seekTo(0);
                    holder.player.setPlayWhenReady(true);

                }
            }

            @Override
            public void onPageSelected(int position1) {
                slider_media_number.setText((pager2.getCurrentItem() + 1) + "/" + urlList.size());
                if (position == position1) {
                    holder.player.seekTo(0);
                    holder.player.setPlayWhenReady(true);

                }
            }
        });

//        if (!(first >= 0 && first <= urlList.size())) {
//        holder.dataSpec = new DataSpec(holder.mediaUri);
//        holder.cacheVideo(holder.dataSpec, holder.defaultCacheKeyFactory, holder.dataSource, holder.progressListener);

//        }
//        first++;
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    void setOnPlayerPause(boolean state) {
        mState = state;
        for (int i = 0; i < urlList.size(); i++) {
            if (mholder[i] != null) {
                if (mState) {
                    if (mholder[i].player.isPlaying())
                        currentPagePosition = i;
                    Log.d("asa-adap", "onRelease: Pause ");
                    mholder[i].player.setPlayWhenReady(false);

                } else {
                    Log.d("asa-adap", "onRelease: Play");
                    mholder[currentPagePosition].player.setPlayWhenReady(true);
                }
            }
        }
    }

    void setOnPlayerPause(boolean state, boolean release) {
        mState = state;
        for (int i = 0; i < urlList.size(); i++) {
            if (mholder[i] != null) {
                if (mState && release) {
                    Log.d("asa-adap", "onRelease: Pause ");
                    mholder[i].player.setPlayWhenReady(false);
                    mholder[i].player.release();
                    mholder[i].cache.release();
                }
            }
        }
    }


    class SliderViewHolder extends RecyclerView.ViewHolder {
        SimpleExoPlayer player;
        PlayerView simpleExoPlayerView;
        MediaSource mediaSource;
        DataSource.Factory dataSourceFactory;
        Uri mediaUri;
        ProgressBar progressBar;


        ExoDatabaseProvider exoDatabaseProvider;
        File cacheFolder;
        LeastRecentlyUsedCacheEvictor cacheEvictor = new LeastRecentlyUsedCacheEvictor(90 * 1024 * 1024);// My cache size will be 1MB and it will automatically remove least recently used files if the size is reached out.
        SimpleCache cache;

        CacheDataSourceFactory cacheDataSourceFactory;
        DataSpec dataSpec;
        CacheKeyFactory defaultCacheKeyFactory;
        CacheUtil.ProgressListener progressListener;
        DefaultDataSource dataSource;

        public SliderViewHolder(@NonNull View view) {
            super(view);

            simpleExoPlayerView = view.findViewById(R.id.video_view);
            player = new SimpleExoPlayer.Builder(context).build();
            dataSourceFactory = new DefaultHttpDataSourceFactory("asa-video-slider");
            progressBar = view.findViewById(R.id.progress_horizontal);

            exoDatabaseProvider = new ExoDatabaseProvider(context);
            cacheFolder = new File(context.getFilesDir(), "video_" + counter());
            if (cache == null)
                cache = new SimpleCache(cacheFolder, cacheEvictor, exoDatabaseProvider);
            cacheDataSourceFactory = new CacheDataSourceFactory(cache, new DefaultHttpDataSourceFactory(context.getString(R.string.lib_name)), CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);


            dataSource =
                    new DefaultDataSourceFactory(
                            context,
                            Util.getUserAgent(context, context.getString(R.string.lib_name))).createDataSource();

            defaultCacheKeyFactory = CacheUtil.DEFAULT_CACHE_KEY_FACTORY;
            progressListener = new CacheUtil.ProgressListener() {
                @Override
                public void onProgress(long requestLength, long bytesCached, long newBytesCached) {
                    double downloadPercentage = (bytesCached * 100.0
                            / requestLength);

                    Log.d("asa-Download", "onProgress: " + downloadPercentage);
                }
            };


        }


        private int counter() {
            return counter++;
        }

        private void cacheVideo(DataSpec dataSpec, CacheKeyFactory defaultCacheKeyFactory, DataSource dataSource, CacheUtil.ProgressListener progressListener) {

            try {
                CacheUtil.cache(
                        dataSpec,
                        cache,
                        defaultCacheKeyFactory,
                        dataSource,
                        progressListener,
                        null
                );
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Log.e("asa-cache-error", "cacheVideo: " + e.toString());
            }
        }

        private void showProgressBar(SimpleExoPlayer player, ProgressBar progressBar) {
            if (player.isPlaying()) {
                progressBar.setVisibility(View.GONE);
            } else if (player.isLoading()) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

    }


}