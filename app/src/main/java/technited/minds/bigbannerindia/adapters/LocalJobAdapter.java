package technited.minds.bigbannerindia.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import technited.minds.bigbannerindia.R;
import technited.minds.bigbannerindia.models.LocalJob;

public class LocalJobAdapter extends RecyclerView.Adapter<LocalJobAdapter.CategoryViewHolder> {

    private List<LocalJob> localJobs;
    private Context context;

    public LocalJobAdapter(Context context, List<LocalJob> localJobs) {
        this.context = context;
        this.localJobs = localJobs;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_local_job, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        LocalJob localJob = localJobs.get(position);
        holder.id.setText("BBI" + localJob.getId());
        holder.type.setText(localJob.getType());
        holder.salary.setText("Rs. " + localJob.getSalary());
        holder.description.setText(localJob.getDescription());
        holder.job_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+919131869705"));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localJobs.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView id, type, salary, description;
        CardView job_layout;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.job_id);
            salary = itemView.findViewById(R.id.job_salary);
            type = itemView.findViewById(R.id.job_type);
            description = itemView.findViewById(R.id.job_description);
            job_layout = itemView.findViewById(R.id.job_layout);

        }
    }
}