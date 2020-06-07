package technited.minds.bigbannerindia;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import technited.minds.asaimagelibrary.GallerySettings;
import technited.minds.asaimagelibrary.MediaInfo;
import technited.minds.asaimagelibrary.ScrollGallery;
import technited.minds.asaimagelibrary.glide.GlideImageLoader;
import technited.minds.bigbannerindia.models.Offer;


public class OffersFragment extends Fragment {

    private Context context, c;

    private ScrollGallery scrollGallery;
    private int position;
    private List<Offer> offers;
    private TextView offer_name;

    public OffersFragment() {
        // Required empty public constructor
    }


    public OffersFragment(Context context, List<Offer> offers, int position) {
        this.context = context;
        this.position = position;
        this.offers = offers;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers,
                container, false);

        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        offer_name = view.findViewById(R.id.offer_name);
        scrollGallery = ScrollGallery
                .from(view.findViewById(R.id.scroll_gallery))
                .settings(
                        GallerySettings
                                .from(activity.getSupportFragmentManager())
                                .enableZoom(true)
                                .build()
                )
                .onImageClickListener(position -> {
//                        Toast.makeText(GridViewItems.this, "image position = " + position, Toast.LENGTH_SHORT).show();
                })
                .onPageChangeListener(new CustomOnPageListener())
                .build();

        if (offers != null) {
            String folder = API.OFFER_FOLDER.toString();

            for (Offer offer : offers) {

                scrollGallery.addMedia(MediaInfo.mediaLoader(
                        new GlideImageLoader(folder + offer.getImage()))
                );
            }
        }
        scrollGallery.setCurrentItem(position);
        offer_name.setText(offers.get(position).getName());


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class CustomOnPageListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            offer_name.setText(offers.get(position).getName());
        }
    }
}
