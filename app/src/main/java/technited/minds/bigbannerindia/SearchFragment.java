package technited.minds.bigbannerindia;


import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

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

import technited.minds.bigbannerindia.adapters.SearchShopAdapter;
import technited.minds.bigbannerindia.models.Category;
import technited.minds.bigbannerindia.models.Shop;


public class SearchFragment extends Fragment {

    private Context context;
    private SearchShopAdapter searchShopAdapter;
    private List<Shop> shops;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HomeActivityViewModel homeActivityViewModel = new ViewModelProvider(requireActivity()).get(HomeActivityViewModel.class);
        context = requireContext();

        TextView search = view.findViewById(R.id.search);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!v.getText().toString().equals("")) {
                        List<Shop> newList = new ArrayList<>();
                        if (shops.size() > 0) {
                            for (Shop shop : shops) {
                                boolean flag = false;
                                if (shop.getName().toLowerCase().contains(v.getText().toString())) {
                                    flag = true;
                                } else if (shop.getState().toLowerCase().contains(v.getText().toString())) {
                                    flag = true;
                                } else if (shop.getDistrict().toLowerCase().contains(v.getText().toString())) {
                                    flag = true;
                                } else if (shop.getCity().toLowerCase().contains(v.getText().toString())) {
                                    flag = true;
                                } else if (shop.getPostalCode().toLowerCase().contains(v.getText().toString())) {
                                    flag = true;
                                } else if (shop.getLocality().toLowerCase().contains(v.getText().toString())) {
                                    flag = true;
                                } else if (shop.getSrn().toLowerCase().contains(v.getText().toString())) {
                                    flag = true;
                                }
                                if (flag) newList.add(shop);
                            }
                            searchShopAdapter.updateList(newList);
                        }
                    } else {
                        Toast.makeText(context, "Search Shops", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }

        });


        RecyclerView recycler_categories = view.findViewById(R.id.recycler_all_shops);
        recycler_categories.setLayoutManager(new LinearLayoutManager(context));
        homeActivityViewModel.category.observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {

                if (categories.size() > 0) {
                    shops = new ArrayList<>();
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
                    searchShopAdapter = new SearchShopAdapter(context, shops);
                    recycler_categories.setAdapter(searchShopAdapter);
                }
            }
        });

    }
}
