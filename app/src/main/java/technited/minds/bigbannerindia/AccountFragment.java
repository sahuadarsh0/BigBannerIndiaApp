package technited.minds.bigbannerindia;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import technited.minds.androidutils.ProcessDialog;
import technited.minds.androidutils.SharedPrefs;
import technited.minds.bigbannerindia.models.Account;


public class AccountFragment extends Fragment {
    SharedPrefs loginSharedPrefs;

    TextView likes, requests;
    ImageView gender;
    ProcessDialog processDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,
                container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        loginSharedPrefs = new SharedPrefs(context, "CUSTOMER");
        processDialog = new ProcessDialog(context);
        Button btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(v -> {

                    loginSharedPrefs.clearAll();
                    NavDirections action = AccountFragmentDirections.actionAccountFragmentToHomeFragment();
                    Navigation.findNavController(view).navigate(action);
                }
        );

        likes = view.findViewById(R.id.likes);
        requests = view.findViewById(R.id.requests);
        gender = view.findViewById(R.id.gender);

        getAccount();
    }

    private void getAccount() {
        processDialog.show();
        Call<Account> accountCall = AccountsApi.getApiService().account(loginSharedPrefs.get("mobile"));
        accountCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                processDialog.dismiss();
                likes.setText(account.getLikes() + "");
                requests.setText(account.getRequests() + "");
                if (account.getGender().equals("Male")) {
                    gender.setImageResource(R.drawable.ic_boy);
                } else {
                    gender.setImageResource(R.drawable.ic_girl);
                }


            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                processDialog.dismiss();
            }
        });
    }


}
