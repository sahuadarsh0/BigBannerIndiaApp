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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment {

    private Context context;
    private RecyclerView recycler_categories;

    public Home() {
        // Required empty public constructor
    }


    public Home(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        recycler_categories = view.findViewById(R.id.recycler_categories);
        recycler_categories.setLayoutManager(new LinearLayoutManager(context));


        Call<List<Category>> getCategory = HomeApi.getApiService().getCategories();
        getCategory.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> category = response.body();
                recycler_categories.setAdapter(new CategoryAdapter(context, category));
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });

        return view;
    }
}
