package technited.minds.bigbannerindia;

import android.util.Log;

import java.util.List;

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
import technited.minds.bigbannerindia.models.AboutTheApp;
import technited.minds.bigbannerindia.models.Category;
import technited.minds.bigbannerindia.models.Comment;
import technited.minds.bigbannerindia.models.LocalJob;
import technited.minds.bigbannerindia.models.Product;
import technited.minds.bigbannerindia.models.Received;
import technited.minds.bigbannerindia.models.Service;
import technited.minds.bigbannerindia.models.Slider;

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

        @GET("getAboutApp/")
        Call<AboutTheApp> getAboutApp();

        @GET("slider/")
        Call<List<Slider>> getSliders();

        @GET("categories/")
        Call<List<Category>> getCategories();

        @GET("getProductDetails/{id}")
        Call<List<Product>> getProductDetails(@Path("id") String id);

        @FormUrlEncoded
        @POST("likeProduct")
        Call<Received> likeProduct(
                @Field("product_id") String product_id,
                @Field("customer_id") String customer_id
        );

        @FormUrlEncoded
        @POST("requestProduct")
        Call<Received> requestProduct(
                @Field("product_id") String product_id,
                @Field("customer_id") String customer_id,
                @Field("address") String address,
                @Field("qty") String qty
        );

        @Headers("Content-Type: application/json")
        @POST("commentOnProduct")
        Call<Received> commentOnProduct(@Body Comment comment);


        @GET("getLocalJobs/")
        Call<List<LocalJob>> getLocalJobs();


        @GET("getServices/")
        Call<List<Service>> getServices();

    }


}

