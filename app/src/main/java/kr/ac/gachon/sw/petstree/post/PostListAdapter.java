package kr.ac.gachon.sw.petstree.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;

import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.model.User;
import kr.ac.gachon.sw.petstree.model.Write_Info;
import kr.ac.gachon.sw.petstree.util.Firestore;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {
    private ArrayList<Write_Info> items;
    private ClickListener mlistener;

    public PostListAdapter(ClickListener listener){
        items = new ArrayList<>();
        this.mlistener = listener;
    }

    @NonNull
    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView, mlistener);
        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title, publisher, num_comments;
        ClickListener listener;
        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.post_title);
            publisher = itemView.findViewById(R.id.post_author);
            num_comments = itemView.findViewById(R.id.num_comments);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }


    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.ViewHolder viewHolder, int position) {
        Write_Info item = items.get(position);
        viewHolder.title.setText(item.getTitle());

        Firestore.getUserData(item.getPublisher())
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            User userData = task.getResult().toObject(User.class);
                            item.setPublisherNick(userData.getUserNickName());
                            viewHolder.publisher.setText(item.getPublisherNick());
                            viewHolder.num_comments.setText(String.valueOf(item.getNum_comments()));
                        }
                        else {
                            item.setPublisherNick("Unknown");
                            viewHolder.publisher.setText(item.getPublisherNick());
                            viewHolder.num_comments.setText(String.valueOf(item.getNum_comments()));
                        }
                    }
                });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Write_Info> list) {
        items = list;
        notifyDataSetChanged();
    }

    public void addItem(Write_Info item) {
        items.add(item);
    }

    public Write_Info getItem(int position) {
        return items.get(position);
    }

    public interface ClickListener{
        void onClick(View v, int position);
    }
}
