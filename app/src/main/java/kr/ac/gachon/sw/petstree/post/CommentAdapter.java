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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.model.User;
import kr.ac.gachon.sw.petstree.model.Comment;
import kr.ac.gachon.sw.petstree.util.Firestore;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comment> items;

    public CommentAdapter(){
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder viewHolder, int position) {
        Comment item = items.get(position);

        Firestore.getUserData(item.getUserId())
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            User userData = task.getResult().toObject(User.class);
                            item.setPublisherNick(userData.getUserNickName());
                        }
                        else {
                            item.setPublisherNick("Unknown");
                        }
                        viewHolder.publisher.setText(item.getPublisherNick());
                        viewHolder.contents.setText(item.getContent());

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                        String date = simpleDateFormat.format(item.getWriteTime());
                        date = date.substring(5,date.length()-3);
                        viewHolder.time.setText(date.replace('.', '/'));

                    }
                });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView publisher, contents, time;
        public ViewHolder(View itemView) {
            super(itemView);
            publisher = itemView.findViewById(R.id.comment_id);
            contents = itemView.findViewById(R.id.comment_contents);
            time = itemView.findViewById(R.id.comment_time);
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Comment> list) {
        items = list;
        notifyDataSetChanged();
    }

    public void addItem(Comment item) {
        items.add(item);
    }

    public Comment getItem(int position) {
        return items.get(position);
    }

}
