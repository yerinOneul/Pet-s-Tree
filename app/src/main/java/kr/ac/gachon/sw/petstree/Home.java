package kr.ac.gachon.sw.petstree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private PostListAdapter postListAdapter = new PostListAdapter();
    public ArrayList<Post> postArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 게시글 목록 생성
        RecyclerView postList = findViewById(R.id.recycler_post_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true); // 먼저 생성된 포스트가 가장 밑으로
        postList.setLayoutManager(llm);
        postList.setAdapter(postListAdapter);

        // postArrayList에 포스트 정보 추가 후 게시글 목록에 할당
        postArrayList.add(new Post("Test Title", "Test Author", "Test Body", 3));
        postArrayList.add(new Post("Test Title 2", "Test Author 2", "Test Body", 2));
        postListAdapter.setItems(postArrayList);


        FloatingActionButton fab = findViewById(R.id.plus_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Write.class);
                startActivity(intent);
            }
        });


    }
}
