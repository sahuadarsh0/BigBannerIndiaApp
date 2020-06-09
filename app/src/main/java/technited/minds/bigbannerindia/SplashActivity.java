package technited.minds.bigbannerindia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.rezwan.knetworklib.KNetwork;

import technited.minds.androidutils.MD;


public class SplashActivity extends AppCompatActivity implements KNetwork.OnNetWorkConnectivityListener {


    Runnable runnable;
    Handler handler;
    KNetwork.Request knRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        knRequest = KNetwork.INSTANCE.bind(this, getLifecycle())
                .showKNDialog(false)
                .setConnectivityListener(this);


    }

    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onNetConnected() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(runnable);
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }, 3000);
    }

    @Override
    public void onNetDisConnected() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                MD.alert(SplashActivity.this, "No Internet", "Please check your internet connection and try again!!!", "yes");
            }
        };
        handler.postDelayed(runnable, 3000);
    }
}
