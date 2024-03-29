package technited.minds.bigbannerindia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import technited.minds.bigbannerindia.HomeActivity;
import technited.minds.bigbannerindia.R;
import technited.minds.bigbannerindia.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private Context context;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category c = categories.get(position);
        holder.category.setText(c.getCategory());
        if (c.getShop().size() > 0)
            holder.recycler_shops.setAdapter(new ShopCategoryAdapter(context, c.getShop()));
        else
            holder.category.setVisibility(View.GONE);




        class RemindTask extends TimerTask {

            @Override
            public void run() {
                ((HomeActivity)context).runOnUiThread(new Runnable() {
                    public void run() {
                        if (holder.position == c.getShop().size()) {
                            holder.position = 0;
                        }
                        holder.recycler_shops.smoothScrollToPosition(holder.position);
                        holder.position++;
                    }
                });

            }
        }

        holder.timer = new Timer();
        holder.timer.scheduleAtFixedRate(new RemindTask(), 200, 3500); // delay*/

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        RecyclerView recycler_shops;
        Timer timer;
        public int position =0;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category);
            recycler_shops = itemView.findViewById(R.id.recycler_shops);
            recycler_shops.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        }
    }
}