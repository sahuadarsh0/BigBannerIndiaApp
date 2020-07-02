package technited.minds.bigbannerindia;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import technited.minds.asavideoslider.VideoSlider;
import technited.minds.bigbannerindia.adapters.AllShopAdapter;
import technited.minds.bigbannerindia.models.Category;
import technited.minds.bigbannerindia.models.Shop;
import technited.minds.bigbannerindia.models.Slider;


public class HomeFragment extends Fragment {

    private Context context;
    private HomeActivityViewModel homeActivityViewModel;
    private Fragment videoSliderFrag;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeActivityViewModel = new ViewModelProvider(requireActivity()).get(HomeActivityViewModel.class);
        context = requireContext();

//        RecyclerView recycler_categories = view.findViewById(R.id.recycler_categories);
//        recycler_categories.setLayoutManager(new LinearLayoutManager(context));
//        homeActivityViewModel.category.observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
//            @Override
//            public void onChanged(List<Category> categories) {
//                if (categories != null)
//                    recycler_categories.setAdapter(new CategoryAdapter(context, categories));
//
//            }
//        });
//

        RecyclerView recycler_categories = view.findViewById(R.id.recycler_all_shops);
        recycler_categories.setLayoutManager(new LinearLayoutManager(context));
        homeActivityViewModel.category.observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {

                if (categories.size() > 0) {
                    List<Shop> shops = new ArrayList<>();
                    for (int i = 0; i < categories.size(); i++) {
                        shops.addAll(categories.get(i).getShop());
                    }

                    Collections.sort(shops, new Comparator<Shop>() {
                        @Override
                        public int compare(Shop lhs, Shop rhs) {
                            if (Integer.parseInt(lhs.getId()) < Integer.parseInt(rhs.getId())) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    });
                    recycler_categories.setAdapter(new AllShopAdapter(context, shops));
                }
            }
        });


//         Get All Slider Videos
        getSliderVideos();
    }

    private void getSliderVideos() {

        Call<List<Slider>> videoSlider = HomeApi.getApiService().getSliderVideos();
        videoSlider.enqueue(new Callback<List<Slider>>() {
            @Override
            public void onResponse(Call<List<Slider>> call, Response<List<Slider>> response) {

                List<Slider> video = response.body();
                ArrayList<String> list = new ArrayList<>();
                assert video != null;
                for (int i = 0; i < video.size(); i++) {
                    list.add(API.VIDEO_SLIDER_FOLDER.toString() + video.get(i).getVideo());
                }
                videoSliderFrag = new VideoSlider(context, list);
                onResume();
            }

            @Override
            public void onFailure(Call<List<Slider>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoSliderFrag != null)
            getChildFragmentManager().beginTransaction()
                    .remove(videoSliderFrag)
                    .commitAllowingStateLoss();
        Log.d("asa", "onPause: fragment ");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoSliderFrag != null)
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.video_container, videoSliderFrag)
                    .commitAllowingStateLoss();
        Log.d("asa", "onResume: fragment ");
    }


}

