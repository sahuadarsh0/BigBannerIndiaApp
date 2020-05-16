package minds.technited.bigbannerindia;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import minds.technited.asautils.MD;
import minds.technited.asautils.SharedPrefs;
import minds.technited.bigbannerindia.models.Customer;
import minds.technited.bigbannerindia.models.Received;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment {


    private Context context, c;


    private SharedPrefs loginSharedPrefs;

    private TextInputEditText etMobile, etPassword;

    Login(Context context) {
        this.context = context;
        loginSharedPrefs = new SharedPrefs(context, "CUSTOMER");
    }

    public Login() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,
                container, false);

        etMobile = view.findViewById(R.id.mobile);
        etPassword = view.findViewById(R.id.password);
        TextView register_text = view.findViewById(R.id.register_text);
        TextView register = view.findViewById(R.id.register);

        ConstraintLayout layout = view.findViewById(R.id.request_layout);
        Button btnLogin = view.findViewById(R.id.login);
        c = getActivity().getApplicationContext();


        if (loginSharedPrefs.get("customer_id") != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new Account(context))
                    .commit();
        }


        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
        layout.startAnimation(animation);

        register_text.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new Register(context))
                    .commit();
        });
        register.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new Register(context))
                    .commit();
        });
        btnLogin.setOnClickListener(v -> customerLogin());

        return view;
    }

    private void customerLogin() {

        String mobile = etMobile.getText().toString();
        String password = etPassword.getText().toString();

        boolean flag = false;

        if (mobile.isEmpty()) {
            Toast.makeText(c, "MOBILE IS MISSING", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(c, "PASSWORD IS MISSING", Toast.LENGTH_SHORT).show();
        } else {
            flag = true;
        }

        if (flag) {

            Call<Received> loginCustomerCall = AccountsApi.getApiService().loginCustomer(mobile, password);

            loginCustomerCall.enqueue(new Callback<Received>() {
                @Override
                public void onResponse(@NotNull Call<Received> call, @NotNull Response<Received> response) {

                    Received data = response.body();
                    Fragment accountFrag = new Account(getActivity());

                    assert data != null;

                    if (data.getMsg().equals("Successful")) {
                        Customer loggedCustomer = data.getCustomer();

                        MD.alert(getActivity(), data.getMsg(), data.getDetails(), "ok", R.id.main_container, accountFrag);

                        assert loggedCustomer != null;
                        loginSharedPrefs.set("customer_id", loggedCustomer.getId());
                        loginSharedPrefs.set("name", loggedCustomer.getName());
                        loginSharedPrefs.set("gender", loggedCustomer.getGender());
                        loginSharedPrefs.set("state", loggedCustomer.getState());
                        loginSharedPrefs.set("district", loggedCustomer.getDistrict());
                        loginSharedPrefs.set("city", loggedCustomer.getCity());
                        loginSharedPrefs.set("locality", loggedCustomer.getLocality());
                        loginSharedPrefs.set("postalCode", loggedCustomer.getPostalCode());
                        loginSharedPrefs.set("mobile", loggedCustomer.getMobile());
                        loginSharedPrefs.set("email", loggedCustomer.getEmail());
                        loginSharedPrefs.set("address", loggedCustomer.getAddress());
                        loginSharedPrefs.apply();

                    } else
                        MD.alert(context, data.getMsg(), data.getDetails());

                }

                @Override
                public void onFailure(@NotNull Call<Received> call, @NotNull Throwable t) {
                    Log.e("ASA", "onFailure: " + t.getMessage(), t);
                }
            });

        }

    }
}
