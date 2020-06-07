package technited.minds.bigbannerindia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import technited.minds.bigbannerindia.API;
import technited.minds.bigbannerindia.OffersFragment;
import technited.minds.bigbannerindia.R;
import technited.minds.bigbannerindia.models.Offer;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    private Context context;
    private List<Offer> offers;

    public OfferAdapter(Context context, List<Offer> offers) {
        this.context = context;
        this.offers = offers;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_offer, parent, false);
        return new OfferAdapter.OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offers.get(position);
        holder.offer_name.setText(offer.getName());
        holder.offer_id.setText(offer.getId());


        holder.offer_layout.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction().addToBackStack(null)
                    .replace(R.id.shop_container, new OffersFragment(context, offers, position))
                    .commit();
        });

        String url = API.OFFER_FOLDER.toString() + offer.getImage();
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.offer)
                .into(holder.offer_image);

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
        return offers.size();
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView offer_name, offer_id;
        ImageView offer_image;
        CardView offer_layout;

        OfferViewHolder(@NonNull View itemView) {
            super(itemView);

            offer_id = itemView.findViewById(R.id.offer_id);
            offer_name = itemView.findViewById(R.id.offer_name);
            offer_image = itemView.findViewById(R.id.offer);
            offer_layout = itemView.findViewById(R.id.offer_layout);

        }
    }
}
