package technited.minds.bigbannerindia;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.Arrays;
import java.util.List;

import technited.minds.asaimagelibrary.GallerySettings;
import technited.minds.asaimagelibrary.MediaInfo;
import technited.minds.asaimagelibrary.ScrollGallery;
import technited.minds.asaimagelibrary.glide.GlideImageLoader;
import technited.minds.bigbannerindia.models.Offer;


public class OffersFragment extends Fragment {


    private ScrollGallery scrollGallery;
    private int position;
    private List<Offer> offers;
    private TextView offer_name;

    public OffersFragment() {
    }


    public OffersFragment(List<Offer> offers, int position) {
        this.position = position;
        this.offers = offers;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers,
                container, false);


        offer_name = view.findViewById(R.id.offer_name);
        scrollGallery = ScrollGallery
                .from(view.findViewById(R.id.scroll_gallery))
                .settings(
                        GallerySettings
                                .from(requireActivity().getSupportFragmentManager())
                                .enableZoom(true)
                                .build()
                )
                .onImageClickListener(position -> {

                })
                .onPageChangeListener(new CustomOnPageListener())
                .build();
        if (offers == null)
            offers = Arrays.asList(OffersFragmentArgs.fromBundle(getArguments()).getListOffer());

        String folder = API.OFFER_FOLDER.toString();

        for (Offer offer : offers) {

            scrollGallery.addMedia(MediaInfo.mediaLoader(
                    new GlideImageLoader(folder + offer.getImage()))
            );
        }
        offer_name.setText(offers.get(position).getName());
        scrollGallery.setCurrentItem(position);

        return view;
    }

    private class CustomOnPageListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            offer_name.setText(offers.get(position).getName());
        }
    }
}
