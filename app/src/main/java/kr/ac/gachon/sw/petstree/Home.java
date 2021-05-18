package kr.ac.gachon.sw.petstree;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home extends Fragment {

    private PostListAdapter postListAdapter = new PostListAdapter();
    public ArrayList<Post> postArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_home, container, false);

        // 게시글 목록 생성
        RecyclerView postList = root.findViewById(R.id.recycler_post_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true); // 먼저 생성된 포스트가 가장 밑으로
        postList.setLayoutManager(llm);
        postList.setAdapter(postListAdapter);

        // postArrayList에 포스트 정보 추가 후 게시글 목록에 할당
        postArrayList.add(new Post("Test Title", "Test Author", "Test Body", 3));
        postArrayList.add(new Post("Test Title 2", "Test Author 2", "Test Body", 2));
        postListAdapter.setItems(postArrayList);


        FloatingActionButton fab = root.findViewById(R.id.plus_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Write.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
