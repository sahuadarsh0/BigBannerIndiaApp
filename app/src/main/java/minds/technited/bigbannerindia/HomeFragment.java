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

import java.util.List;

import minds.technited.bigbannerindia.adapters.CategoryAdapter;
import minds.technited.bigbannerindia.models.Category;


public class HomeFragment extends Fragment {

    private Context context;
    private List<Category> category;

    public HomeFragment() {
        // Required empty public constructor
    }


    public HomeFragment(Context context, List<Category> category) {
        this.context = context;
        this.category = category;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);

        RecyclerView recycler_categories = view.findViewById(R.id.recycler_categories);
        recycler_categories.setLayoutManager(new LinearLayoutManager(context));
        recycler_categories.setAdapter(new CategoryAdapter(context, category));


        return view;
    }
}
