package minds.technited.bigbannerindia;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rezwan.knetworklib.KNetwork;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import minds.technited.asautils.ProcessDialog;
import minds.technited.asavideoslider.VideoSlider;
import minds.technited.bigbannerindia.models.Category;
import minds.technited.bigbannerindia.models.Received;
import minds.technited.bigbannerindia.models.Slider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, KNetwork.OnNetWorkConnectivityListener {

    private BottomNavigationView bottomNavigationView;

    private FloatingActionButton home;
    Context context;
    FragmentManager fm = getSupportFragmentManager();

    DrawerLayout drawer;
    NavigationView navigationView;

    Fragment homeFrag;
    Fragment loginFrag = new LoginFragment(this);
    Fragment registerFrag = new RegisterFragment(this);
    Fragment shopsFrag;
    Fragment videoSliderFrag;
    FrameLayout video_container;
    KNetwork.Request knRequest;
    String version;
    ProcessDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        video_container = findViewById(R.id.video_container);
        home = findViewById(R.id.home);

        // Network Connection
        knRequest = KNetwork.INSTANCE.bind(this, getLifecycle())
                .showKNDialog(false)
                .setConnectivityListener(this);
        knRequest.setInAnimation(R.anim.bottom_in)
                .setOutAnimation(R.anim.bottom_out)
                .setViewGroupResId(R.id.crouton_top);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.getBackground().setAlpha(0);
        bottomNavigationView.setBackgroundResource(R.drawable.transparent_background);
        bottomNavigationView.setItemBackground(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    Fragment active = null;

                    video_container.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .remove(videoSliderFrag)
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
                            active = shopsFrag;
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
                                .replace(R.id.video_container, videoSliderFrag)
                                .commit();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_container, homeFrag)
                                .addToBackStack(null)
                                .commit();
                    }

                    return false;
                });

        home.setOnClickListener(v -> {
            video_container.setVisibility(View.VISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.video_container, videoSliderFrag)
                    .commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, homeFrag)
                    .addToBackStack(null)
                    .commit();

        });


        processDialog = new ProcessDialog(this);
        processDialog.show();
        bottomNavigationView.setVisibility(View.GONE);

//
////         Get All Slider Videos
//        getSliderVideos();
//         Get All Categories
        getCategories();


//         Check For Update
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        checkUpdate();

    }

    private void getSliderVideos() {
        Call<List<Slider>> videoSlider = HomeApi.getApiService().getSliderVideos();
        videoSlider.enqueue(new Callback<List<Slider>>() {
            @Override
            public void onResponse(Call<List<Slider>> call, Response<List<Slider>> response) {

                List<Slider> video = response.body();
                ArrayList<String> list = new ArrayList<>();
                assert video != null;
                for (int i = 0; i < video.size(); i++) {
                    list.add(API.VIDEO_SLIDER_FOLDER.toString() + video.get(i).getVideo());
                }
                videoSliderFrag = new VideoSlider(context, list);
                showSlider();
            }

            @Override
            public void onFailure(Call<List<Slider>> call, Throwable t) {

            }
        });
    }

    private void getCategories() {

        Call<List<Category>> getCategory = HomeApi.getApiService().getCategories();
        getCategory.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> category;
                category = response.body();
                homeFrag = new HomeFragment(HomeActivity.this, category);
                shopsFrag = new ShopsFragment(HomeActivity.this, category);
                bottomNavigationView.setVisibility(View.VISIBLE);
                processDialog.dismiss();


//         Get All Slider Videos
                getSliderVideos();
//                recycler_categories.setAdapter(new CategoryAdapter(context, category));
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }


    private void showSlider() {
        video_container.setVisibility(View.VISIBLE);
        fm.beginTransaction().add(R.id.main_container, homeFrag, "1").commit();
        fm.beginTransaction()
                .add(R.id.video_container, videoSliderFrag)
                .commit();
    }


    private void checkUpdate() {

        Call<Received> versionCheck = HomeApi.getApiService().checkVersion();
        versionCheck.enqueue(new Callback<Received>() {
            @Override
            public void onResponse(@NotNull Call<Received> call, @NotNull Response<Received> response) {

                Received data = response.body();

                assert data != null;
                if (!data.getMsg().equals(version)) {

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_update_app);
                    Button btn = dialog.findViewById(R.id.btn_ok);

                    new Handler().postDelayed(dialog::show, 4000);

                    dialog.setCancelable(true);
                    btn.setOnClickListener(v -> {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(API.BASE_URL.toString() + "app"));
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

    @Override
    public void onNetConnected() {
        Log.d("asa-network", "onNetConnected: Net is Connected");
    }

    @Override
    public void onNetDisConnected() {
        Log.d("asa-network", "onNetDisConnected: Net is disconnected");

    }
}