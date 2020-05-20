package minds.technited.bigbannerindia;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.parceler.Parcels;

import minds.technited.bigbannerindia.adapters.OfferAdapter;
import minds.technited.bigbannerindia.adapters.ProductsAdapter;
import minds.technited.bigbannerindia.models.Shop;

public class ShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_left, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Shop shop = Parcels.unwrap(getIntent().getParcelableExtra("shop"));
        assert shop != null;


        ImageView banner = findViewById(R.id.banner);
        String url = API.BANNER_FOLDER.toString() + shop.getBanner();
        Glide
                .with(this)
                .load(url)
                .placeholder(R.drawable.banner)
                .into(banner);


//        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.accent));
//        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
//        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        RecyclerView recycler_offers_container = findViewById(R.id.recycler_offers_container);
        recycler_offers_container.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recycler_offers_container.setAdapter(new OfferAdapter(this, shop.getOffer()));

        RecyclerView recycler_products_container = findViewById(R.id.recycler_products_container);
        recycler_products_container.setLayoutManager(new LinearLayoutManager(this));
        recycler_products_container.setAdapter(new ProductsAdapter(this, shop.getProduct()));

    }
}
