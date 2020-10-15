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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.DefaultSliderView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import technited.minds.androidutils.MD;
import technited.minds.androidutils.SharedPrefs;
import technited.minds.bigbannerindia.API;
import technited.minds.bigbannerindia.HomeFragmentDirections;
import technited.minds.bigbannerindia.R;
import technited.minds.bigbannerindia.ShopActivity;
import technited.minds.bigbannerindia.models.HomeItems;
import technited.minds.bigbannerindia.models.Offer;
import technited.minds.bigbannerindia.models.Shop;

public class AllShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> imo = new ArrayList<>();
    private Context context;
    private List<HomeItems> items;
    SharedPrefs loginSharedPrefs;

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType() == 0) {
            return position;
        } else {
            return -1;//indicates general type, if you have more types other than video, you can use -1,-2,-3 and so on.
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public AllShopAdapter(Context context, List<HomeItems> items) {
        this.context = context;
        this.items = items;
        loginSharedPrefs = new SharedPrefs(context, "CUSTOMER");
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == -1) {
            View view = inflater.inflate(R.layout.item_image_slider, parent, false);
            return new ImageSliderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_shop, parent, false);
            return new ShopViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        if (items.get(position).getType() == 0) {
            Parcelable shopParcelable = Parcels.wrap(items.get(position).getObject1());
            Shop s = Parcels.unwrap(shopParcelable);

            ((ShopViewHolder) holder).srn.setText(s.getSrn());
            if (s.getOffer().size() > 0) {
                ((ShopViewHolder) holder).total_offer.setText(s.getOffer().size() + "");
            } else {
                ((ShopViewHolder) holder).offer_layout.setVisibility(View.GONE);
            }
            ((ShopViewHolder) holder).offer_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Offer[] o = s.getOffer().toArray(new Offer[]{});
//                NavDirections action = ShopsFragmentDirections.actionShopsFragmentToOffersFragment(o);
                    NavDirections action = HomeFragmentDirections.actionHomeFragmentToOffersFragment(o);
                    Navigation.findNavController(v).navigate(action);
                }
            });

            ((ShopViewHolder) holder).banner.setOnClickListener(v -> {
                if (loginSharedPrefs.get("customer_id") == null) {
//                NavDirections action = ShopsFragmentDirections.actionShopsFragmentToLoginFragment();
                    NavDirections action = HomeFragmentDirections.actionHomeFragmentToLoginFragment();
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
                    .into(((ShopViewHolder) holder).banner);

        } else {
            imo = items.get(position).getList1();
            RequestOptions requestOptions = new RequestOptions().fitCenter().placeholder(R.drawable.product);
            for (int i = 0; i < imo.size(); i++) {
                DefaultSliderView sliderView = new DefaultSliderView(context);
                sliderView
                        .image(imo.get(i))
                        .setRequestOption(requestOptions)
                        .setProgressBarVisible(true);
                //add your extra information
                ((ImageSliderViewHolder) holder).imageSlider.addSlider(sliderView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ShopViewHolder extends RecyclerView.ViewHolder {
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

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder {

        SliderLayout imageSlider;

        public ImageSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSlider = itemView.findViewById(R.id.image_slider);
            imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            imageSlider.setCustomAnimation(new DescriptionAnimation());
            imageSlider.moveNextPosition(true);
            imageSlider.setDuration(3000);
            imageSlider.stopCyclingWhenTouch(true);
        }
    }
}