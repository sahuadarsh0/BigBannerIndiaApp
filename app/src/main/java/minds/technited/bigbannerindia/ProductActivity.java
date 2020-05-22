package minds.technited.bigbannerindia;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import minds.technited.bigbannerindia.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    String product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product_id = extras.getString("product_id");
        }
        getProductDetails(product_id);


    }


    private void getProductDetails(String product_id) {


        Call<List<Product>> productDetail = HomeApi.getApiService().getProductDetails(product_id);
        productDetail.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> products = response.body();
                Log.d("asa", "onResponse: " + products);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });


    }
}
