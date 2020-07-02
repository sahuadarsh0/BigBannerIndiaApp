package technited.minds.bigbannerindia;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.parceler.Parcels;

import technited.minds.androidutils.MD;
import technited.minds.bigbannerindia.adapters.OfferAdapter;
import technited.minds.bigbannerindia.adapters.ProductsAdapter;
import technited.minds.bigbannerindia.models.Shop;

public class ShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Shop shop = Parcels.unwrap(getIntent().getParcelableExtra("shop"));
        assert shop != null;

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        Toolbar toolbar = findViewById(R.id.toolBar);
        Toolbar toolbar1 = findViewById(R.id.toolBar1);


        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_left, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar1.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_left, null));
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView offers_text = findViewById(R.id.offers_text);
        TextView address = findViewById(R.id.address);
        ImageView banner = findViewById(R.id.banner);
        String url = API.BANNER_FOLDER.toString() + shop.getBanner();
        Glide
                .with(this)
                .load(url)
                .placeholder(R.drawable.banner)
                .into(banner);

        String add = shop.getAddress().concat(", ").concat(shop.getLocality())
                .concat(", ").concat(shop.getCity())
                .concat(", ").concat(shop.getPostalCode())
                .concat(", ").concat(shop.getState())
                .concat(",\nMobile : ").concat(shop.getMobile());
        if (!shop.getMobile2().isEmpty()) {
            add = add.concat(", ").concat(shop.getMobile2());
            Log.d("asa", "onCreate: " + shop.getMobile2());
        }
        if (!shop.getMobile3().isEmpty()) {
            add = add.concat(", ").concat(shop.getMobile3());
            Log.d("asa", "onCreate: " + shop.getMobile3());
        }

        address.setText(add);

        collapsingToolbarLayout.setTitle(shop.getName());
        toolbar1.setTitle(shop.getName());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        if (shop.getOffer().size() == 0)
            offers_text.setVisibility(View.INVISIBLE);
        RecyclerView recycler_offers_container = findViewById(R.id.recycler_offers_container);
        recycler_offers_container.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recycler_offers_container.setAdapter(new OfferAdapter(this, shop.getOffer()));

        RecyclerView recycler_products_container = findViewById(R.id.recycler_products_container);
        recycler_products_container.setLayoutManager(new LinearLayoutManager(this));
        recycler_products_container.setAdapter(new ProductsAdapter(this, shop.getProduct()));


        FloatingActionButton info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MD.alert(ShopActivity.this, "About", Html.fromHtml(shop.getAbout()).toString(), "OK");
            }
        });
    }
}
