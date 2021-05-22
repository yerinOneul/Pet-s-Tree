package kr.ac.gachon.sw.petstree;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import kr.ac.gachon.sw.petstree.model.Write_Info;

public class Home extends Fragment {
    public ArrayList<Write_Info> postArrayList = new ArrayList<>();
    private PostListAdapter postListAdapter = new PostListAdapter(this, postArrayList);
    private String TAG = "Home";
    private int board = -1;
    private int btn_state = 0;
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
        FloatingActionButton fab_write = root.findViewById(R.id.write_btn);
        FloatingActionButton fab_report = root.findViewById(R.id.report_btn);
        //+모양을 x모양으로 회전
        Animation rotate = AnimationUtils.loadAnimation(getContext(),R.anim.anim_rotate);
        //x모양을 +모양으로
        Animation reverse = AnimationUtils.loadAnimation(getContext(),R.anim.anim_reverse);
        //버튼 fade in , fade out animation
        Animation fade_in = AnimationUtils.loadAnimation(getContext(),R.anim.anim_fade_in);
        Animation fade_out = AnimationUtils.loadAnimation(getContext(),R.anim.anim_fade_out);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( btn_state == 0){
                    //버튼 나타나기
                    fab.startAnimation(rotate);
                    fab_write.setVisibility(View.VISIBLE);
                    fab_write.startAnimation(fade_in);
                    fab_report.setVisibility(View.VISIBLE);
                    fab_report.startAnimation(fade_in);
                    btn_state = 1;
                }
                else{
                    //사라지기
                    fab.startAnimation(reverse);
                    fab_write.startAnimation(fade_out);
                    fab_write.setVisibility(View.INVISIBLE);
                    fab_report.startAnimation(fade_out);
                    fab_report.setVisibility(View.INVISIBLE);
                    btn_state = 0;
                }




            }
        });

        fab_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Write.class);
                startActivity(intent);
            }
        });


        // ! 유기동물 제보 게시글 작성이랑 일반 글 작성은 처리해줘야 하는 방식이 다른 거 같아서 아예 글 작성이랑 분리해봤습니다 !
        //  ex ) 제보 시 필수 입력 사항 : 제보자 이름 ,연락처 ,사진 등 -- > 관리자만 확인 가능하도록 처리
        fab_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Animal_report.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
