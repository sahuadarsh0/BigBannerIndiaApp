package minds.technited.bigbannerindia;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;

        home = findViewById(R.id.home);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setItemIconTintList(null);
        fm.beginTransaction().add(R.id.main_container, homeFrag, "1").commit();
        bottomNavigationView.getBackground().setAlpha(0);
        bottomNavigationView.setBackgroundResource(R.drawable.transparent_background);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    Fragment active = null;

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

                        case R.id.invisible:
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

                    return false;
                });

        home.setOnClickListener(v -> getSupportFragmentManager().beginTransaction().replace(R.id.main_container, homeFrag).commit());


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        switch (id) {

            case android.R.id.home:

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