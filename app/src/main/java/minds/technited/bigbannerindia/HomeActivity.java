package minds.technited.bigbannerindia;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import minds.technited.asavideoslider.VideoSlider;
import minds.technited.bigbannerindia.models.Received;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private FloatingActionButton home;
    Context context;
    FragmentManager fm = getSupportFragmentManager();

    DrawerLayout drawer;
    NavigationView navigationView;

    Fragment homeFrag = new Home(this);
    Fragment loginFrag = new Login(this);
    Fragment registerFrag = new Register(this);
    Fragment videoSlider;
    FrameLayout video_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        video_container = findViewById(R.id.video_container);
        home = findViewById(R.id.home);

        ArrayList<String> list = new ArrayList<>();

        final String base_url = "http://bigbannerindia.com/admin/assets/uploads/slider/";

        list.add(base_url + "53e36-cee433db3e9a3c26cf33bee4d71f2b3b.mp4");
        list.add(base_url + "videoplayback.mp4");
        list.add(base_url + "53e36-cee433db3e9a3c26cf33bee4d71f2b3b.mp4");
        list.add(base_url + "13fba-vid-20200310-wa0002.mp4");
        videoSlider = new VideoSlider(context, list);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        fm.beginTransaction().add(R.id.main_container, homeFrag, "1").commit();
        fm.beginTransaction()
                .add(R.id.video_container, videoSlider)
                .commit();
        bottomNavigationView.getBackground().setAlpha(0);
        bottomNavigationView.setBackgroundResource(R.drawable.transparent_background);
        bottomNavigationView.setItemBackground(null);
        video_container.setVisibility(View.VISIBLE);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    Fragment active = null;

                    video_container.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .remove(videoSlider)
                            .commit();
                    switch (item.getItemId()) {

                        case R.id.menu:
                            drawer.openDrawer(navigationView);
                            break;


                        case R.id.accounts:
                            active = loginFrag;
                            break;


                        case R.id.search:
                            active = homeFrag;
                            break;

                        case R.id.shops:
                            active = homeFrag;
                            break;


                    }
                    if (active != null) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_container, active)
                                .addToBackStack(null)
                                .commit();
                    }
                    if (item.getItemId() == R.id.invisible || item.getItemId() == R.id.home) {

                        video_container.setVisibility(View.VISIBLE);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.video_container, videoSlider)
                                .commit();

                    }

                    return false;
                });

        home.setOnClickListener(v -> {
            Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();

            video_container.setVisibility(View.VISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.video_container, videoSlider)
                    .commit();


        });


        // Check For Update
        Call<Received> versionCheck = FetchApi.getApiService().checkVersion();
        versionCheck.enqueue(new Callback<Received>() {
            @Override
            public void onResponse(@NotNull Call<Received> call, @NotNull Response<Received> response) {

                Received data = response.body();

                assert data != null;
                if (!data.getMsg().equals("0.2.0")) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_update_app);
                    Button btn = dialog.findViewById(R.id.btn_ok);

                    new Handler().postDelayed(dialog::show, 4000);

                    dialog.setCancelable(true);
                    btn.setOnClickListener(v -> {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FetchApi.base_url + "app"));
                        startActivity(browserIntent);

                    });
                }
            }

            @Override
            public void onFailure(Call<Received> call, Throwable t) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        switch (id) {

//            case android.R.id.home:

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.our_services) {
            // Handle the camera action
        } else if (id == R.id.local_jobs) {

        } else if (id == R.id.contact) {
            drawer.closeDrawer(GravityCompat.END);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFrag).commit();

        }
        return true;
    }
}