package minds.technited.bigbannerindia;

import android.util.Log;

import java.util.List;

import minds.technited.bigbannerindia.models.Category;
import minds.technited.bigbannerindia.models.Customer;
import minds.technited.bigbannerindia.models.Masters;
import minds.technited.bigbannerindia.models.Product;
import minds.technited.bigbannerindia.models.Received;
import minds.technited.bigbannerindia.models.Slider;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

class HomeApi {

    public static final String base_url = API.BASE_URL.toString();


    private static ApiService apiService = null;

    static ApiService getApiService() {


        if (apiService == null) {
            //create Ok HttP Client
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(s -> Log.d("ASA", s));
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);

//            create
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;

    }

    public interface ApiService {

        @GET("version/")
        Call<Received> checkVersion();

        @GET("slider_videos/")
        Call<List<Slider>> getSliderVideos();

        @GET("categories/")
        Call<List<Category>> getCategories();

        @GET("getProductDetails/{id}")
        Call<List<Product>> getProductDetails(@Path("id") String id);

        @GET("postal_code/{id}")
        Call<List<Masters>> getPostalCodes(@Path("id") String id);


        @Headers("Content-Type: application/json")
        @POST("customer_register")
        Call<Received> registerCustomer(@Body Customer customer);


        @FormUrlEncoded
        @POST("customer_login")
        Call<Received> loginCustomer(
                @Field("mobile") String mobile,
                @Field("password") String password
        );

    }


}

