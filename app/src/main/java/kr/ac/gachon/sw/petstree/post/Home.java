package kr.ac.gachon.sw.petstree.post;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import kr.ac.gachon.sw.petstree.post.Post;
import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.animal.Animal_report;
import kr.ac.gachon.sw.petstree.model.User;
import kr.ac.gachon.sw.petstree.model.Write_Info;
import kr.ac.gachon.sw.petstree.util.Firestore;
import kr.ac.gachon.sw.petstree.util.LoadingDialog;

public class Home extends Fragment implements PostListAdapter.ClickListener{
    private PostListAdapter postListAdapter;
    private String TAG = "Home";
    private LoadingDialog loadingDialog;
    private int board = -1;
    private int btn_state = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        loadingDialog = new LoadingDialog(getActivity());

        if(getArguments()!=null){
            board = getArguments().getInt("Type");
        }

        // 게시글 목록 생성
        RecyclerView postList = root.findViewById(R.id.recycler_post_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true); // 먼저 생성된 포스트가 가장 밑으로
        postList.setLayoutManager(llm);
        postListAdapter = new PostListAdapter(this);
        postList.setAdapter(postListAdapter);

        loadingDialog.show();

        // 초기 화면. navigation bar 선택하지 않고 들어올 때
        if(board == -1) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("posts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                setPostData(task);
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
                                setPostData(task);
                            } else {
                                Log.d(TAG, "Error getting documents:", task.getException());
                            }
                        }
                    });
        }

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

    @Override
    public void onClick(View v,int position) {
        Intent postView = new Intent(getActivity(), Post.class);
        postView.putExtra("post_view", (Parcelable) postListAdapter.getItem(position));
        startActivity(postView);
    }

    private void setPostData(Task<QuerySnapshot> task) {
        ArrayList<Write_Info> writeInfos = new ArrayList<>();

        for(int i = 0; i < task.getResult().getDocuments().size(); i++) {
            DocumentSnapshot doc = task.getResult().getDocuments().get(i);

            Log.d(TAG, doc.getId() + "=>" + doc.getData());
            Write_Info writeInfo = doc.toObject(Write_Info.class);

            Log.d(TAG, "date " + writeInfo.getCreateAt());

            int currentI = i;
            Firestore.getUserData(writeInfo.getPublisher())
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                            if(userTask.isSuccessful()) {
                                Log.d("Home", "UserTask Successful");
                                User user = userTask.getResult().toObject(User.class);
                                writeInfo.setPublisherNick(user.getUserNickName());
                                writeInfos.add(writeInfo);
                            }
                            else {
                                Log.e("Home", "Get Userdata Error!", userTask.getException());
                            }

                            if(currentI == task.getResult().getDocuments().size() - 1) {
                                postListAdapter.setItems(writeInfos);
                                postListAdapter.notifyDataSetChanged();
                                loadingDialog.dismiss();
                            }
                        }
                    });
        }
    }
}
