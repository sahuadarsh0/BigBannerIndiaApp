package minds.technited.bigbannerindia;


import android.content.Context;
import android.os.Bundle;
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

import minds.technited.bigbannerindia.adapters.AllShopAdapter;
import minds.technited.bigbannerindia.models.Category;
import minds.technited.bigbannerindia.models.Shop;


public class ShopsFragment extends Fragment {

    private Context context;


    public ShopsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shops, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HomeActivityViewModel homeActivityViewModel = new ViewModelProvider(requireActivity()).get(HomeActivityViewModel.class);
        context = requireContext();


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


    }
}
