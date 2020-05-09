package minds.technited.asavideoslider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;


public class VideoSlider extends Fragment {

    private TextView slider_media_number;
    private ViewPager2 pager2;
    private ArrayList<String> urlList;
    private Context context;
    private SliderAdapter sliderAdapter;


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

        sliderAdapter = new SliderAdapter(context, urlList, slider_media_number, pager2);

        pager2.setAdapter(sliderAdapter);
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

    @Override
    public void onPause() {
        Log.d("asa-pause", "onPause: ");
        sliderAdapter.setOnPlayerPause(true);
        super.onPause();
        }

        @Override
        public void onResume() {
            Log.d("asa-onResume", "onResume: ");
            sliderAdapter.setOnPlayerPause(false);
            super.onResume();
        }

    @Override
    public void onStop() {
        Log.d("asa-pause", "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("asa-onDestroyView", "onDestroyView: ");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        sliderAdapter.setOnPlayerPause(true, true);
        Log.d("asa-pause", "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("asa-pause", "onDetach: ");
        super.onDetach();
        }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("asa-pause", "onAttach: ");
    }


}
