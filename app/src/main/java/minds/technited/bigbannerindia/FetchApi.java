package minds.technited.bigbannerindia;

import android.util.Log;

import java.util.List;

import minds.technited.bigbannerindia.models.Customer;
import minds.technited.bigbannerindia.models.Masters;
import minds.technited.bigbannerindia.models.Received;
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

class FetchApi {

     static final String base_url = "http://bigbannerindia.com/admin/api/";
//    private static final String base_url = "http://192.168.43.102/Bigbannerindia.com/admin/api/";


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

        @GET("district/{id}")
        Call<List<Masters>> getDistricts(@Path("id") String id);

        @GET("city/{id}")
        Call<List<Masters>> getCities(@Path("id") String id);

        @GET("locality/{id}")
        Call<List<Masters>> getLocalities(@Path("id") String id);

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

