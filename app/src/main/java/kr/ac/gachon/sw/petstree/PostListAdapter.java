package kr.ac.gachon.sw.petstree;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private ArrayList<Post> items = new ArrayList<>();

    @NonNull
    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.ViewHolder viewHolder, int position) {

        Post item = items.get(position);

        viewHolder.title.setText(item.getTitle());
        viewHolder.author.setText(item.getAuthor());
        viewHolder.num_comments.setText(item.getNum_comments());

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Post> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, author, num_comments;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.post_title);
            author = itemView.findViewById(R.id.post_author);
            num_comments = itemView.findViewById(R.id.num_comments);
        }
    }
}
