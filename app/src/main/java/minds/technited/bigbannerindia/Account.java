package minds.technited.bigbannerindia;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


public class Account extends Fragment {

    private Context context, c;
    private static final String PREFS = "PREFS";

    public Account() {
        // Required empty public constructor
    }


    public Account(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,
                container, false);


        c = getActivity().getApplicationContext();
        SharedPreferences.Editor edit;
        SharedPreferences sp = c.getSharedPreferences(PREFS, MODE_PRIVATE);
        edit = sp.edit();

        Button btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(v -> {
                    edit.clear().apply();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, new Login(context))
                            .commit();
                }
        );

        return view;
    }
}
