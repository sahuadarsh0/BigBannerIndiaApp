package minds.technited.bigbannerindia;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Offers extends Fragment {

    private static final String PREFS = "PREFS";
    private Context context, c;

    public Offers() {
        // Required empty public constructor
    }


    public Offers(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,
                container, false);


        return view;
    }
}
