package kr.ac.gachon.sw.petstree;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import kr.ac.gachon.sw.petstree.certreq.CertRequestViewActivity;
import kr.ac.gachon.sw.petstree.model.Write_Info;

public class Home extends Fragment implements PostListAdapter.ClickListener{
    public ArrayList<Write_Info> postArrayList = new ArrayList<>();
    private PostListAdapter postListAdapter = new PostListAdapter(this, postArrayList, this);
    private String TAG = "Home";
    private int board = -1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_home, container, false);

        if(getArguments()!=null){
            board = getArguments().getInt("Type");
        }

        // 초기 화면. navigation bar 선택하지 않고 들어올 때
        if(board == -1) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("posts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + "=>" + document.getData());
                                    postArrayList.add(new Write_Info(
                                            document.getData().get("title").toString(),
                                            (ArrayList<String>) document.getData().get("contents"),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createAt").getTime()),
                                            Integer.parseInt(String.valueOf(document.getData().get("boardType"))),
                                            Integer.parseInt(String.valueOf(document.getData().get("num_comments")))
                                    ));
                                }
                                // 게시글 목록 생성
                                RecyclerView postList = root.findViewById(R.id.recycler_post_list);
                                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                llm.setReverseLayout(true);
                                llm.setStackFromEnd(true); // 먼저 생성된 포스트가 가장 밑으로
                                postList.setLayoutManager(llm);
                                postList.setAdapter(postListAdapter);
                                postListAdapter.setItems(postArrayList);
                            } else {
                                Log.d(TAG, "Error getting documents:", task.getException());
                            }
                        }
                    });
        }
        else{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("posts")
                    .whereEqualTo("boardType", board)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + "=>" + document.getData());
                                    postArrayList.add(new Write_Info(
                                            document.getData().get("title").toString(),
                                            (ArrayList<String>) document.getData().get("contents"),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createAt").getTime()),
                                            Integer.parseInt(String.valueOf(document.getData().get("boardType"))),
                                            Integer.parseInt(String.valueOf(document.getData().get("num_comments")))
                                    ));
                                }
                                // 게시글 목록 생성
                                RecyclerView postList = root.findViewById(R.id.recycler_post_list);
                                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                llm.setReverseLayout(true);
                                llm.setStackFromEnd(true); // 먼저 생성된 포스트가 가장 밑으로
                                postList.setLayoutManager(llm);
                                postList.setAdapter(postListAdapter);
                                postListAdapter.setItems(postArrayList);
                            } else {
                                Log.d(TAG, "Error getting documents:", task.getException());
                            }
                        }
                    });
        }
        // postArrayList에 포스트 정보 추가 후 게시글 목록에 할당
//        postArrayList.add(new Post("Test Title", "Test Author", "Test Body", 3));
//        postArrayList.add(new Post("Test Title 2", "Test Author 2", "Test Body", 2));
//        postListAdapter.setItems(postArrayList);


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

    @Override
    public void onClick(View v,int position) {
        postArrayList.get(position);
        Intent postView = new Intent(getActivity(), Post.class);
        postView.putExtra("post_view", (Parcelable) postArrayList.get(position));
        startActivity(postView);
    }
}
