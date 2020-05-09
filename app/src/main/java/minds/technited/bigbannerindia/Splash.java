package minds.technited.bigbannerindia;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import minds.technited.asautils.MD;
import minds.technited.asavideoslider.MyService;
import minds.technited.bigbannerindia.models.Slider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {

    ArrayList<String> list = new ArrayList<>();

    Runnable runnable;
    Handler handler1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler1 = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                MD.alert(Splash.this, "No Internet", "Please check your internet connection and try again!!!");
            }
        };
        handler1.postDelayed(runnable, 4500);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();

        connectivityManager.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler1.removeCallbacks(runnable);
                                startActivity(new Intent(Splash.this, HomeActivity.class));
                                finish();
                            }
                        }, 3000);
                    }
                }
        );

        Call<List<Slider>> videoSlider = HomeApi.getApiService().getSliderVideos();
        videoSlider.enqueue(new Callback<List<Slider>>() {
            @Override
            public void onResponse(Call<List<Slider>> call, Response<List<Slider>> response) {

                List<Slider> video = response.body();

                assert video != null;
                for (int i = 0; i < video.size(); i++) {
                    list.add(API.VIDEO_SLIDER_FOLDER.toString() + video.get(i).getVideo());
                }
                Intent serviceIntent;
                serviceIntent = new Intent(Splash.this, MyService.class);
                serviceIntent.putStringArrayListExtra("url", list);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startForegroundService(serviceIntent);
                } else {
//                    startService(serviceIntent);
                }
            }

            @Override
            public void onFailure(Call<List<Slider>> call, Throwable t) {

            }
        });

    }
}
