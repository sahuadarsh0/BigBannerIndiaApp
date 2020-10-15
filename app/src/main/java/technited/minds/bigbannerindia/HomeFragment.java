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
import technited.minds.bigbannerindia.models.HomeItems;
import technited.minds.bigbannerindia.models.Shop;
import technited.minds.bigbannerindia.models.Slider;


public class HomeFragment extends Fragment {

    private Context context;
    private HomeActivityViewModel homeActivityViewModel;
    private Fragment videoSliderFrag;
    List<Slider> slider;
    RecyclerView recycler_categories;
    List<HomeItems> items = new ArrayList<>();
    AllShopAdapter allShopAdapter;
    ArrayList<List<String>> imo = new ArrayList<>();


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

//         Get All Slider Videos
        getSliderVideos();

        recycler_categories = view.findViewById(R.id.recycler_all_shops);
        recycler_categories.setLayoutManager(new LinearLayoutManager(context));
        allShopAdapter = new AllShopAdapter(context, items);
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
                    ArrayList<List<String>> image = homeActivityViewModel.imo.getValue();
                    for (int i = 0, j = 0, k = 0; i < shops.size() + image.size(); i++) {
                            HomeItems item = null;
                            if ((i + 1) % 3 != 0) {
                                item = new HomeItems(i, 0, shops.get(j), new ArrayList<>());
                                j++;
                            } else {
                                if (image.size() > k) {
                                    item = new HomeItems(i, 1, new Object(), image.get(k));
                                    k++;
                                } else {
                                    item = new HomeItems(i, 0, shops.get(j), new ArrayList<>());
                                    j++;
                                }
                            }
                            items.add(item);
                        }
                        recycler_categories.setItemViewCacheSize(items.size());
                        allShopAdapter.notifyDataSetChanged();

                }
            }
        });


    }

    private void getSliderVideos() {

        Call<List<Slider>> videoSlider = HomeApi.getApiService().getSliders();
        videoSlider.enqueue(new Callback<List<Slider>>() {
            @Override
            public void onResponse(Call<List<Slider>> call, Response<List<Slider>> response) {

                slider = response.body();
                ArrayList<String> videos = new ArrayList<>();
                ArrayList<String> images = new ArrayList<>();
                assert slider != null;
                for (int i = 0; i < slider.size(); i++) {
                    if (slider.get(i).getVideo() != null)
                        videos.add(API.VIDEO_SLIDER_FOLDER.toString() + slider.get(i).getVideo());
                    else {
                        images.add(API.IMAGE_SLIDER_FOLDER.toString() + slider.get(i).getImage());
                    }
                }
                for (int i = images.size() - 1; i > 0; i -= 2) {
                    ArrayList<List<String>> image = new ArrayList<>();
                    if (i - 2 < 0)
                        image.add(images.subList(0, i));
                    else image.add(images.subList(i - 2, i));
                    imo.addAll(image);
                }
                homeActivityViewModel.imo.setValue(imo);

//                for (int i = 0; i < imo.size(); i++)
//                    items.add(new HomeItems(i,1, new Object(), imo.get(i)));

                allShopAdapter = new AllShopAdapter(context, items);
                recycler_categories.setAdapter(allShopAdapter);

                videoSliderFrag = new VideoSlider(context, videos);
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

