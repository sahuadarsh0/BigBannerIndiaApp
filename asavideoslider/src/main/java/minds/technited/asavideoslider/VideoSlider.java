package minds.technited.asavideoslider;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;


public class VideoSlider extends Fragment {

    private TextView slider_media_number;
    private ViewPager2 pager2;
    private ArrayList<String> urlList;
    private Context context;

    public VideoSlider(Context context, ArrayList<String> urlList) {

        this.urlList = urlList;
        this.context = context;
    }


    public VideoSlider() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_slider,
                container, false);

        slider_media_number = view.findViewById(R.id.number);
        pager2 = view.findViewById(R.id.pager);

        pager2.setAdapter(new SliderAdapter(context, urlList));
        pager2.setClipToPadding(false);
        pager2.setClipChildren(false);
        pager2.setOffscreenPageLimit(3);
        pager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(10));

//        transformer.addTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                float r = 1 - Math.abs(position);
//                page.setScaleY(0.85f + r * 0.15f);
//            }
//        });

        pager2.setPageTransformer(transformer);

        slider_media_number.setVisibility(View.VISIBLE);
        slider_media_number.setText((pager2.getCurrentItem() + 1) + "/" + urlList.size());

        return view;
    }

    public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {


        private Context context;
        private ArrayList<String> urlList;

        public SliderAdapter(Context context, ArrayList<String> urlList) {
            this.context = context;
            this.urlList = urlList;
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
            holder.mediaUri = Uri.parse(urlList.get(position));
            holder.mediaSource = new ProgressiveMediaSource.Factory(holder.dataSourceFactory)
                    .createMediaSource(holder.mediaUri);

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
        }

        @Override
        public int getItemCount() {
            return urlList.size();
        }

        class SliderViewHolder extends RecyclerView.ViewHolder {
            SimpleExoPlayer player;
            PlayerView simpleExoPlayerView;
            MediaSource mediaSource;
            DataSource.Factory dataSourceFactory;
            Uri mediaUri;

            public SliderViewHolder(@NonNull View view) {
                super(view);

                simpleExoPlayerView = view.findViewById(R.id.video_view);
                player = new SimpleExoPlayer.Builder(context).build();
                dataSourceFactory = new DefaultHttpDataSourceFactory("asa-video-slider");

            }


        }
    }
}
