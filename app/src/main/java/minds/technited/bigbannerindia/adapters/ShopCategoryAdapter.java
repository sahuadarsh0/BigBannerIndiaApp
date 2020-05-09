package minds.technited.bigbannerindia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import minds.technited.bigbannerindia.API;
import minds.technited.bigbannerindia.R;
import minds.technited.bigbannerindia.models.Shop;

public class ShopCategoryAdapter extends RecyclerView.Adapter<ShopCategoryAdapter.CategoryViewHolder> {

    private List<Shop> shops;
    private Context context;

    public ShopCategoryAdapter(Context context, List<Shop> shops) {
        this.context = context;
        this.shops = shops;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_shop_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Shop s = shops.get(position);
//        holder.recycler_shops.setAdapter(new CategoryAdapter(c.getShops(),context));
//        holder.banner

        if (s.getOffer() != null || s.getOffer().size() > 0) {
//            holder.total_offer.setText(s.getOffer().size() + 1);
        }

        String url = API.BANNER_FOLDER.toString() + s.getBanner();
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.banner)
                .into(holder.banner);
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView total_offer;
        ImageView banner;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            total_offer = itemView.findViewById(R.id.total_offer);
            banner = itemView.findViewById(R.id.banner);
        }
    }
}