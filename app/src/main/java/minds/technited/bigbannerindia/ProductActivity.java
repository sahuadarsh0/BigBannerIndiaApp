package minds.technited.bigbannerindia;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import minds.technited.asautils.MD;
import minds.technited.asautils.ProcessDialog;
import minds.technited.asautils.SharedPrefs;
import minds.technited.bigbannerindia.adapters.CommentsAdapter;
import minds.technited.bigbannerindia.models.Comment;
import minds.technited.bigbannerindia.models.Like;
import minds.technited.bigbannerindia.models.Product;
import minds.technited.bigbannerindia.models.Received;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    String product_id;
    ProcessDialog processDialog;
    RecyclerView recycler_comments_container;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar, toolbar1;
    FloatingActionButton like;
    private SharedPrefs loginSharedPrefs;
    EditText comment_edit;
    Comment comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        loginSharedPrefs = new SharedPrefs(this, "CUSTOMER");

        processDialog = new ProcessDialog(this);
        processDialog.show();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product_id = extras.getString("product_id");

            recycler_comments_container = findViewById(R.id.recycler_comments_container);
            recycler_comments_container.setLayoutManager(new LinearLayoutManager(this));
            getProductDetails(product_id);
        }

        like = findViewById(R.id.like);
        Button request_item = findViewById(R.id.request_item);
        comment_edit = findViewById(R.id.comment_edit);


        request_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processDialog.show();
                requestProduct(product_id, loginSharedPrefs.get("customer_id"));
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processDialog.show();
                likeProduct(product_id, loginSharedPrefs.get("customer_id"));
            }
        });

        comment_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (!comment_edit.getText().toString().equals("")) {
                        comment = new Comment("", "", product_id, comment_edit.getText().toString(), "", "", "", loginSharedPrefs.get("customer_id"));
                        processDialog.show();
                        sendComment(comment);
                    } else {
                        Toast.makeText(ProductActivity.this, "Write Comments", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });


    }

    private void sendComment(Comment comment) {

        Call<Received> commentOnProduct = HomeApi.getApiService().commentOnProduct(comment);
        commentOnProduct.enqueue(new Callback<Received>() {
            @Override
            public void onResponse(Call<Received> call, Response<Received> response) {

                processDialog.dismiss();

                Received data = response.body();
                assert data != null;
                Log.d("asa", "onResponse: " + data);
                if (data.getMsg().equals("Successful")) {
                    Toast.makeText(ProductActivity.this, data.getDetails(), Toast.LENGTH_SHORT).show();
                }
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onFailure(Call<Received> call, Throwable t) {

            }
        });
    }

    private void requestProduct(String product_id, String customer_id) {

        Call<Received> checkRequest = HomeApi.getApiService().requestProduct(product_id, customer_id);
        checkRequest.enqueue(new Callback<Received>() {
            @Override
            public void onResponse(Call<Received> call, Response<Received> response) {
                Received data = response.body();
                processDialog.dismiss();

                if (data.getMsg().equals("Successful"))
                    MD.alert(ProductActivity.this, data.getMsg(), data.getDetails(), "OK");

                else
                    MD.alert(ProductActivity.this, data.getMsg(), data.getDetails());

            }

            @Override
            public void onFailure(Call<Received> call, Throwable t) {

            }
        });

    }

    private void likeProduct(String product_id, String customer_id) {

        Call<Received> checkLike = HomeApi.getApiService().likeProduct(product_id, customer_id);
        checkLike.enqueue(new Callback<Received>() {
            @Override
            public void onResponse(Call<Received> call, Response<Received> response) {
                Received data = response.body();
                processDialog.dismiss();

                if (data.getMsg().equals("Successful")) {
                    MD.alert(ProductActivity.this, data.getMsg(), data.getDetails(), "OK");
                    if (data.getExtra().equals("1")) {
                        like.setImageResource(R.drawable.ic_like);
                    } else {
                        like.setImageResource(R.drawable.ic_white_like);
                    }
                } else
                    MD.alert(ProductActivity.this, data.getMsg(), data.getDetails());

            }

            @Override
            public void onFailure(Call<Received> call, Throwable t) {

            }
        });
    }


    private void getProductDetails(String product_id) {
        Call<List<Product>> productDetail = HomeApi.getApiService().getProductDetails(product_id);
        productDetail.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                processDialog.dismiss();
                List<Product> products = response.body();

                assert products != null;
                List<Like> likes = products.get(0).getLikes();
                if (likes != null) {
                    for (Like oneLike : likes) {
                        if (oneLike.getCustomerId().equals(loginSharedPrefs.get("customer_id")) && oneLike.getLike().equals("1")) {
                            like.setImageResource(R.drawable.ic_like);
                        } else {
                            like.setImageResource(R.drawable.ic_white_like);
                        }
                    }
                }
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
