package technited.minds.bigbannerindia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import technited.minds.androidutils.ProcessDialog;
import technited.minds.bigbannerindia.adapters.ServiceAdapter;
import technited.minds.bigbannerindia.models.Service;

public class ServicesFragment extends Fragment {
    ProcessDialog processDialog;

    public ServicesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_servives,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        processDialog = new ProcessDialog(requireContext());
        processDialog.show();

        RecyclerView recycler_services = view.findViewById(R.id.recycler_services);
        recycler_services.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<Service>> services = HomeApi.getApiService().getServices();
        services.enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                List<Service> serviceList;
                serviceList = response.body();
                recycler_services.setAdapter(new ServiceAdapter(getContext(), serviceList));
                processDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                processDialog.dismiss();
            }
        });
    }
}
