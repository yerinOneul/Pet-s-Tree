package kr.ac.gachon.sw.petstree;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import kr.ac.gachon.sw.petstree.model.Write_Info;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private ArrayList<Write_Info> items;
    private Home activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, publisher, num_comments;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.post_title);
            publisher = itemView.findViewById(R.id.post_author);
            num_comments = itemView.findViewById(R.id.num_comments);
        }
    }

    public PostListAdapter(Home activity, ArrayList<Write_Info> mitems){
        items = mitems;
        this.activity = activity;

    }
    @NonNull
    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.ViewHolder viewHolder, int position) {

        Write_Info item = items.get(position);

        viewHolder.title.setText(item.getTitle());
        viewHolder.publisher.setText(item.getPublisher());
        viewHolder.num_comments.setText(String.valueOf(item.getNum_comments()));

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Write_Info> items) {
        this.items = items;
    }


}
