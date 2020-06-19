package technited.minds.bigbannerindia.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import technited.minds.bigbannerindia.API;
import technited.minds.bigbannerindia.R;
import technited.minds.bigbannerindia.models.Service;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    Context context;
    List<Service> serviceList;

    public ServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);

        String url = API.SERVICE_FOLDER.toString() + service.getImage();
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.banner)
                .into(holder.service);

        Toast toast = Toast.makeText(context, service.getName(), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 150);
        holder.service_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toast.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        CardView service_card;
        ImageView service;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            service_card = itemView.findViewById(R.id.service_card);
            service = itemView.findViewById(R.id.service);
        }
    }
}
