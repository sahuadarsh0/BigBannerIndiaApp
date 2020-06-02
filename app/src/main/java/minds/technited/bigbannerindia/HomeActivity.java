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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rezwan.knetworklib.KNetwork;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import minds.technited.asautils.ProcessDialog;
import minds.technited.bigbannerindia.models.Category;
import minds.technited.bigbannerindia.models.Received;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, KNetwork.OnNetWorkConnectivityListener {

    private BottomNavigationView bottomNavigationView;

    Context context;

    DrawerLayout drawer;
    NavigationView navigationView;


    KNetwork.Request knRequest;
    String version;
    ProcessDialog processDialog;
    NavHostFragment navHostFragment;
    NavController navController;
    HomeActivityViewModel homeActivityViewModel;

    Boolean backPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FloatingActionButton home = findViewById(R.id.home);


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
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });


        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        Navigation.setViewNavController(home, navController);

        home.setOnClickListener(v -> {
            navController.navigate(R.id.homeFragment);
        });


        processDialog = new ProcessDialog(this);
        processDialog.show();
        bottomNavigationView.setVisibility(View.GONE);

//         Get All Categories
        getCategories();


        setBottomNavMenu();


//         Check For Update
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        checkUpdate();


        homeActivityViewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawer);
    }

    private void setBottomNavMenu() {

        NavigationUI.setupWithNavController(bottomNavigationView,
                navController);
    }

    private void getCategories() {

        Call<List<Category>> getCategory = HomeApi.getApiService().getCategories();
        getCategory.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> category;
                category = response.body();

                bottomNavigationView.setVisibility(View.VISIBLE);
                processDialog.dismiss();
                homeActivityViewModel.setCategory(category);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        drawer.closeDrawer(GravityCompat.END);

        if (id == R.id.our_services) {
            // Handle the camera action
        } else if (id == R.id.local_jobs) {

        } else if (id == R.id.contact) {

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (navController.getGraph().getStartDestination() == navController.getCurrentDestination().getId()) {

            if (backPressedOnce) {
                super.onBackPressed();
            }

            backPressedOnce = true;
            Toast.makeText(context, "Please Back again to exit", Toast.LENGTH_SHORT).show();
            new Handler();
            Handler handler;
            handler = new Handler();
            handler.postDelayed(() -> backPressedOnce = false, 2000);
        } else {
            super.onBackPressed();
        }
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