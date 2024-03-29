package technited.minds.bigbannerindia.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import technited.minds.bigbannerindia.API;
import technited.minds.bigbannerindia.ProductActivity;
import technited.minds.bigbannerindia.R;
import technited.minds.bigbannerindia.models.Product;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    Context context;
    List<Product> products;

    public ProductsAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_products, parent, false);
        return new ProductsAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.product_name.setText(product.getName());
        holder.product_id.setText(product.getId());

        if (product.getStock().equals("0"))
            holder.product_layout.setOnClickListener(v -> {
                Toast.makeText(context, "Product Out of Stock", Toast.LENGTH_SHORT).show();
            });
        else
            holder.product_layout.setOnClickListener(v -> {
                Intent i = new Intent(context, ProductActivity.class);
                i.putExtra("product_id", holder.product_id.getText().toString());
                context.startActivity(i);
            });

        String url = API.PRODUCT_FOLDER.toString() + product.getImage();
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.product)
                .into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView product_name;
        TextView product_id;
        CardView product_layout;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            product_layout = itemView.findViewById(R.id.product_layout);
            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_id = itemView.findViewById(R.id.product_id);

        }
    }
}
