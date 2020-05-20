package minds.technited.bigbannerindia;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import minds.technited.asautils.SharedPrefs;


public class Account extends Fragment {

    private Context context, c;

    SharedPrefs loginSharedPrefs;

    public Account() {
        // Required empty public constructor
    }


    public Account(Context context) {
        this.context = context;
        loginSharedPrefs = new SharedPrefs(context, "CUSTOMER");
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,
                container, false);



        Button btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(v -> {

            loginSharedPrefs.clearAll();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, new LoginFragment(context))
                            .commit();
                }
        );

        return view;
    }
}
