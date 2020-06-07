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
import technited.minds.bigbannerindia.models.Customer;
import technited.minds.bigbannerindia.models.Masters;
import technited.minds.bigbannerindia.models.Received;

class AccountsApi {


    private static final String base_url = API.BASE_URL.toString();

    private static ApiService apiService = null;

    static ApiService getApiService() {


        if (apiService == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(s -> Log.d("ASA", s));
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);

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

        @GET("state/1")
        Call<List<Masters>> getStates();

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

