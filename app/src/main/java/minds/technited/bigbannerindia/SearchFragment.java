package minds.technited.bigbannerindia;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import minds.technited.bigbannerindia.adapters.AllShopAdapter;
import minds.technited.bigbannerindia.models.Category;
import minds.technited.bigbannerindia.models.Shop;


public class SearchFragment extends Fragment {

    private Context context;
    private List<Category> categories;


    public SearchFragment() {
        // Required empty public constructor
    }


    public SearchFragment(Context context, List<Category> category) {
        this.context = context;
        this.categories = category;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,
                container, false);


        if (categories.size() > 0) {
            List<Shop> shops = new ArrayList<>();
            for (int i = 0; i < categories.size(); i++) {
                shops.addAll(categories.get(i).getShop());
            }

            RecyclerView recycler_categories = view.findViewById(R.id.recycler_all_shops);
            recycler_categories.setLayoutManager(new LinearLayoutManager(context));
            recycler_categories.setAdapter(new AllShopAdapter(context, shops));
        }

        return view;
    }
}
