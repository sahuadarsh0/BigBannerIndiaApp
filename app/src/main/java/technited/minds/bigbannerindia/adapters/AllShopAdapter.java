package technited.minds.bigbannerindia.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.List;

import technited.minds.androidutils.MD;
import technited.minds.androidutils.SharedPrefs;
import technited.minds.bigbannerindia.API;
import technited.minds.bigbannerindia.R;
import technited.minds.bigbannerindia.ShopActivity;
import technited.minds.bigbannerindia.ShopsFragmentDirections;
import technited.minds.bigbannerindia.models.Shop;

public class AllShopAdapter extends RecyclerView.Adapter<AllShopAdapter.ShopViewHolder> {

    private List<Shop> shops;
    private Context context;
    SharedPrefs loginSharedPrefs;

    public AllShopAdapter(Context context, List<Shop> shops) {
        this.context = context;
        this.shops = shops;
        loginSharedPrefs = new SharedPrefs(context, "CUSTOMER");
    }


    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_shop, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {

        Parcelable shopParcelable = Parcels.wrap(shops.get(position));
        Shop s = Parcels.unwrap(shopParcelable);

        holder.srn.setText(s.getSrn());
        if (s.getOffer().size() > 0) {
            holder.total_offer.setText(s.getOffer().size() + "");
        } else {
            holder.offer_layout.setVisibility(View.GONE);
        }
        holder.offer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: OFFERS FRAGMENT
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                activity.getSupportFragmentManager()
//                        .beginTransaction().addToBackStack(null)
//                        .replace(R.id.main_container, new OffersFragment(context, s.getOffer(), 0))
//                        .commit();
            }
        });

        holder.banner.setOnClickListener(v -> {
            if (loginSharedPrefs.get("customer_id") == null) {
                NavDirections action = ShopsFragmentDirections.actionShopsFragmentToLoginFragment();
                MD.alert(v.getContext(), "Login Required", "To see shop details you have to login first", "ok", v, action);

            } else {
                Intent i = new Intent(context, ShopActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("shop", shopParcelable);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });

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

    class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView total_offer;
        TextView srn;
        ImageView banner;
        LinearLayout offer_layout;

        ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            total_offer = itemView.findViewById(R.id.total_offer);
            srn = itemView.findViewById(R.id.srn);
            banner = itemView.findViewById(R.id.banner);
            offer_layout = itemView.findViewById(R.id.offer_layout);
        }
    }
}