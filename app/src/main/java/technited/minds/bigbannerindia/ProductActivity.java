package technited.minds.bigbannerindia;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import technited.minds.androidutils.MD;
import technited.minds.androidutils.ProcessDialog;
import technited.minds.androidutils.SharedPrefs;
import technited.minds.bigbannerindia.adapters.CommentsAdapter;
import technited.minds.bigbannerindia.models.Comment;
import technited.minds.bigbannerindia.models.Like;
import technited.minds.bigbannerindia.models.Product;
import technited.minds.bigbannerindia.models.Received;
import technited.minds.bigbannerindia.models.Request;

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
    Button request_item, cancel_item;
    private Dialog address_order_qty_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        loginSharedPrefs = new SharedPrefs(this, "CUSTOMER");

        processDialog = new ProcessDialog(this);
        processDialog.show();


        address_order_qty_dialog = new Dialog(this);
        address_order_qty_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        address_order_qty_dialog.setContentView(R.layout.address_order_qty_dialog);
        Window window1 = address_order_qty_dialog.getWindow();
        window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window1.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window1.setBackgroundDrawableResource(R.color.semi_transparent);
        address_order_qty_dialog.setCancelable(true);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product_id = extras.getString("product_id");

            recycler_comments_container = findViewById(R.id.recycler_comments_container);
            recycler_comments_container.setLayoutManager(new LinearLayoutManager(this));
            getProductDetails(product_id);
        }

        like = findViewById(R.id.like);
        request_item = findViewById(R.id.request_item);
        cancel_item = findViewById(R.id.cancel_item);
        comment_edit = findViewById(R.id.comment_edit);

        String curAddress = loginSharedPrefs.get("name").trim() + ", " +
                loginSharedPrefs.get("address").trim() + ", " +
                loginSharedPrefs.get("city").trim() + ", " +
                loginSharedPrefs.get("locality").trim() + ", " +
                loginSharedPrefs.get("postalCode").trim() + ", " +
                loginSharedPrefs.get("mobile").trim();
        TextView address_text = address_order_qty_dialog.findViewById(R.id.text2);
        TextView order_qty_text = address_order_qty_dialog.findViewById(R.id.text);
        TextView submit = address_order_qty_dialog.findViewById(R.id.submit);
        TextView cancel = address_order_qty_dialog.findViewById(R.id.cancel);
        address_text.setText(curAddress);
        order_qty_text.setText("1");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String add = address_text.getText().toString();
                String qty = order_qty_text.getText().toString();
                if (!add.equals("") && !qty.equals(""))
                    requestProduct(product_id, loginSharedPrefs.get("customer_id"), add, qty);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address_order_qty_dialog.dismiss();
            }
        });

        request_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_order_qty_dialog.show();
            }
        });
        cancel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestProduct(product_id, loginSharedPrefs.get("customer_id"), "0", "0");
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

                Log.e("ASA", "onFailure: " + t.getMessage(), t);
            }
        });
    }

    private void requestProduct(String product_id, String customer_id, String address, String qty) {
        processDialog.show();
        Call<Received> checkRequest = HomeApi.getApiService().requestProduct(product_id, customer_id, address, qty);
        checkRequest.enqueue(new Callback<Received>() {
            @Override
            public void onResponse(Call<Received> call, Response<Received> response) {
                Received data = response.body();
                processDialog.dismiss();

                MD.alert(ProductActivity.this, data.getMsg(), data.getDetails(), "OK");

                //Refreshing page to update button
                Handler handler1;
                handler1 = new Handler();
                Runnable runnable = () -> {
                    finish();
                    startActivity(getIntent());
                };
                handler1.postDelayed(runnable, 4000);

            }

            @Override
            public void onFailure(Call<Received> call, Throwable t) {

                Log.e("ASA", "onFailure: " + t.getMessage(), t);
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

                Log.e("ASA", "onFailure: " + t.getMessage(), t);
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
                Product product = products.get(0);

//                request visibility
                if (product.getRequestStatus().equals("0")) {
                    request_item.setVisibility(View.INVISIBLE);
                    cancel_item.setVisibility(View.INVISIBLE);
                }

//                check request state for current customer
                for (Request temp : product.getRequests()) {
                    if (temp.getCustomerId().equals(loginSharedPrefs.get("customer_id"))) {
                        if (temp.getStatus().equals("ACTIVE")) {
                            request_item.setVisibility(View.INVISIBLE);
                            cancel_item.setVisibility(View.VISIBLE);
                        }
                    }
                }

//                comment
                if (product.getCommentStatus().equals("0")) {
                    comment_edit.setVisibility(View.INVISIBLE);
                    recycler_comments_container.setVisibility(View.INVISIBLE);
                } else {
                    recycler_comments_container.setAdapter(new CommentsAdapter(ProductActivity.this, product.getComments()));
                }

//                likes
                List<Like> likes = product.getLikes();
                if (likes != null) {
                    boolean likeFlag = false;
                    for (Like oneLike : likes) {
                        if (oneLike.getCustomerId().equals(loginSharedPrefs.get("customer_id")) && oneLike.getLike().equals("1")) {
                            likeFlag = true;
                            like.setImageResource(R.drawable.ic_like);
                        }
                        if (!likeFlag) {
                            like.setImageResource(R.drawable.ic_white_like);
                        }
                    }
                }
                initToolbar(product);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

                Log.e("ASA", "onFailure: " + t.getMessage(), t);
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
