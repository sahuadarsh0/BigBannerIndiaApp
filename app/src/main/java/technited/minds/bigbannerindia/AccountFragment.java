package technited.minds.bigbannerindia;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import technited.minds.androidutils.SharedPrefs;


public class AccountFragment extends Fragment {

    public AccountFragment() {
    }

    TextView likes, requests;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();
        SharedPrefs loginSharedPrefs = new SharedPrefs(context, "CUSTOMER");

        Button btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(v -> {

                    loginSharedPrefs.clearAll();
                    NavDirections action = AccountFragmentDirections.actionAccountFragmentToHomeFragment();
                    Navigation.findNavController(view).navigate(action);
                }
        );

        likes = view.findViewById(R.id.likes);
        requests = view.findViewById(R.id.requests);

    }
}
