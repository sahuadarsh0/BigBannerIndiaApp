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
import technited.minds.bigbannerindia.adapters.LocalJobAdapter;
import technited.minds.bigbannerindia.models.LocalJob;


public class LocalJobsFragment extends Fragment {

    public LocalJobsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_jobs,
                container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recycler_local_jobs = view.findViewById(R.id.recycler_local_jobs);
        recycler_local_jobs.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<LocalJob>> jobs = HomeApi.getApiService().getLocalJobs();
        jobs.enqueue(new Callback<List<LocalJob>>() {
            @Override
            public void onResponse(Call<List<LocalJob>> call, Response<List<LocalJob>> response) {
                List<LocalJob> localJobs;
                localJobs = response.body();
                recycler_local_jobs.setAdapter(new LocalJobAdapter(getContext(), localJobs));

            }

            @Override
            public void onFailure(Call<List<LocalJob>> call, Throwable t) {

            }
        });
    }
}
