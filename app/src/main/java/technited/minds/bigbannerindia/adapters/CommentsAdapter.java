package technited.minds.bigbannerindia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import technited.minds.bigbannerindia.R;
import technited.minds.bigbannerindia.models.Comment;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    Context context;
    List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        setHasStableIds(true);
        this.context = context;
        this.comments = comments;
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_comments, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        Comment comment = comments.get(position);


        holder.customer_name.setText(comment.getCustomerName());
        holder.customer_comment.setText(comment.getComment());

        if (comment.getCustomerGender().equals("Male")) {
            holder.gender.setImageResource(R.drawable.ic_boy);
        } else {
            holder.gender.setImageResource(R.drawable.ic_girl);
        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView customer_name;
        TextView customer_comment;
        ImageView gender;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            customer_name = itemView.findViewById(R.id.customer_name);
            customer_comment = itemView.findViewById(R.id.comment);
            gender = itemView.findViewById(R.id.image);
        }
    }

}


