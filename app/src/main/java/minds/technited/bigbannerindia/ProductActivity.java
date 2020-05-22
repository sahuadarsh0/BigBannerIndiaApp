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

import org.jetbrains.annotations.NotNull;

import java.util.List;

import minds.technited.asautils.ProcessDialog;
import minds.technited.bigbannerindia.adapters.CommentsAdapter;
import minds.technited.bigbannerindia.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    String product_id;
    ProcessDialog processDialog;
    RecyclerView recycler_comments_container;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar, toolbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        processDialog = new ProcessDialog(this);
        processDialog.show();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product_id = extras.getString("product_id");
        }

        recycler_comments_container = findViewById(R.id.recycler_comments_container);
        recycler_comments_container.setLayoutManager(new LinearLayoutManager(this));
        getProductDetails(product_id);


    }


    private void getProductDetails(String product_id) {
        Call<List<Product>> productDetail = HomeApi.getApiService().getProductDetails(product_id);
        productDetail.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                processDialog.dismiss();
                List<Product> products = response.body();

                assert products != null;
                recycler_comments_container.setAdapter(new CommentsAdapter(ProductActivity.this, products.get(0).getComments()));
                initToolbar(products.get(0));
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });


    }

    private void initToolbar(@NotNull Product product) {

        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        toolbar = findViewById(R.id.toolBar);
        toolbar1 = findViewById(R.id.toolBar1);


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


        ImageView product_image = findViewById(R.id.product_image);
        String url = API.PRODUCT_FOLDER.toString() + product.getImage();
        Glide
                .with(ProductActivity.this)
                .load(url)
                .placeholder(R.drawable.product)
                .into(product_image);

        collapsingToolbarLayout.setTitle(product.getName());
        toolbar1.setTitle(product.getName());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


    }
}
