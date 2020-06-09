package technited.minds.bigbannerindia;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import technited.minds.androidutils.MD;
import technited.minds.androidutils.SharedPrefs;
import technited.minds.bigbannerindia.models.Customer;
import technited.minds.bigbannerindia.models.Received;

public class LoginFragment extends Fragment {

    private Context context;
    private SharedPrefs loginSharedPrefs;
    private TextInputEditText etMobile, etPassword;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,
                container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireContext();
        loginSharedPrefs = new SharedPrefs(context, "CUSTOMER");

        etMobile = view.findViewById(R.id.mobile);
        etPassword = view.findViewById(R.id.password);
        TextView register_text = view.findViewById(R.id.register_text);
        TextView register = view.findViewById(R.id.register);

        ConstraintLayout layout = view.findViewById(R.id.request_layout);
        Button btnLogin = view.findViewById(R.id.login);

        if (loginSharedPrefs.get("customer_id") != null) {
            NavDirections action = LoginFragmentDirections.actionLoginFragmentToAccountFragment();
            Navigation.findNavController(view).navigate(action);
        }


        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
        layout.startAnimation(animation);

        register_text.setOnClickListener(v -> {
            NavDirections action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
            Navigation.findNavController(view).navigate(action);
        });
        register.setOnClickListener(v -> {
            NavDirections action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
            Navigation.findNavController(view).navigate(action);
        });
        btnLogin.setOnClickListener(v -> customerLogin());

    }

    private void customerLogin() {

        String mobile = etMobile.getText().toString();
        String password = etPassword.getText().toString();

        boolean flag = false;

        if (mobile.isEmpty()) {
            Toast.makeText(context, "MOBILE IS MISSING", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(context, "PASSWORD IS MISSING", Toast.LENGTH_SHORT).show();
        } else {
            flag = true;
        }

        if (flag) {

            Call<Received> loginCustomerCall = AccountsApi.getApiService().loginCustomer(mobile, password);

            loginCustomerCall.enqueue(new Callback<Received>() {
                @Override
                public void onResponse(@NotNull Call<Received> call, @NotNull Response<Received> response) {

                    Received data = response.body();
                    assert data != null;

                    if (data.getMsg().equals("Successful")) {
                        Customer loggedCustomer = data.getCustomer();

                        NavDirections action = LoginFragmentDirections.actionLoginFragmentToAccountFragment();
                        MD.alert(getActivity(), data.getMsg(), data.getDetails(), "ok", getView(), action);

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
