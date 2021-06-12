package kr.ac.gachon.sw.petstree.post;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.model.CertRequest;
import kr.ac.gachon.sw.petstree.model.Comment;
import kr.ac.gachon.sw.petstree.model.User;
import kr.ac.gachon.sw.petstree.model.Write_Info;
import kr.ac.gachon.sw.petstree.util.Auth;
import kr.ac.gachon.sw.petstree.util.Firestore;
import kr.ac.gachon.sw.petstree.util.LoadingDialog;
import kr.ac.gachon.sw.petstree.util.Storage;
import kr.ac.gachon.sw.petstree.util.Util;

public class Post extends AppCompatActivity {
    private static String LOG_TAG = "Post";
    private CommentAdapter commentAdapter;
    private LoadingDialog loadingDialog;
    private ActionBar actionBar;
    private Write_Info write_info;
    private Comment comment;
    private EditText edTitle;
    private EditText comment_et;
    private TextView tvPublisher;
    private TextView tvTime;
    private Button comment_btn;
    private LinearLayout parent;
    private ArrayList<String> contents = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        edTitle = findViewById(R.id.post_title);
        tvPublisher = findViewById(R.id.publisher);
        tvTime = findViewById(R.id.time);
        parent = findViewById(R.id.contentsLayout);
        comment_btn = findViewById(R.id.comment_btn);
        comment_et = findViewById(R.id.comment_edit);

        loadingDialog = new LoadingDialog(getApplicationContext());

        // 댓글 목록 생성
        RecyclerView commentList = findViewById(R.id.recycler_post_list);
        commentList.setLayoutManager(new LinearLayoutManager(this));

        commentAdapter = new CommentAdapter();
        commentList.setAdapter(commentAdapter);
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("comments")
                .orderBy("writeTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                 loadingDialog.dismiss();
                            }
                            else setCommentData(task);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.error_server, Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Error getting documents:", task.getException());
                                loadingDialog.dismiss();
                            }
                        }
                    });


        if (getIntent().hasExtra("post_view")) {
            write_info = getIntent().getParcelableExtra("post_view");

            actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(write_info.getTitle());
            }

            edTitle.setText(write_info.getTitle());
            tvPublisher.setText(write_info.getPublisherNick());

            Log.d("dd", "dd " + write_info.getCreateAt());
            //날짜 형식 변환
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String date = simpleDateFormat.format(write_info.getCreateAt());
            date = date.substring(5,date.length()-3);
            tvTime.setText(date.replace('.', '/'));

            contents = write_info.getContents();
            Log.w(LOG_TAG, String.valueOf(contents.size()));

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //contents 내용이 firebase 주소로 시작하면 이미지, 아니면 text
            for (int i = 0; i < contents.size(); i++) {
                if (contents.get(i).startsWith("https://firebasestorage")) {
                    ImageView imageView = new ImageView(Post.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setPadding(0,25,0,25);
                    parent.addView(imageView);
                    Glide.with(this).load(contents.get(i)).override(1000).into(imageView);
                }
                //제보자 정보 숨기기
                else if (contents.get(i).startsWith("제보자명 :") || contents.get(i).startsWith("제보자 연락처 :")) {
                    String text = contents.get(i);
                    // 사용자가 Admin이면 제보자 정보 보이기
                    if(Auth.getCurrentUser() != null) {
                        Firestore.getUserData(Auth.getCurrentUser().getUid())
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult() != null) {
                                                User user = task.getResult().toObject(User.class);
                                                if (user != null && (user.isAdmin() ||write_info.getPublisher().equals(Auth.getCurrentUser().getUid()))) {
                                                    TextView editText = new TextView(Post.this);
                                                    editText.setLayoutParams(layoutParams);
                                                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                                                    parent.addView(editText);
                                                    editText.setText(text);
                                                }
                                                else{
                                                    TextView editText = new TextView(Post.this);
                                                    editText.setLayoutParams(layoutParams);
                                                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                                                    parent.addView(editText);
                                                    if (text.startsWith("제보자명 :")){
                                                        editText.setText("※제보자명은 관리자와 제보자만 확인 가능합니다.");
                                                    }
                                                    else{
                                                        editText.setText("※제보자 연락처는 관리자와 제보자만 확인 가능합니다.");
                                                    }

                                                }
                                            }
                                        }
                                    }
                                });
                    }

                }
                else{
                    TextView editText = new TextView(Post.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    parent.addView(editText);
                    editText.setText(contents.get(i));

                }

            }
        }

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        try {
            // 작성자 아이디가 현재 로그인 ID와 같으면
            if(write_info.getPublisher().equals(Auth.getCurrentUser().getUid())) {
                // Menu 보이기
                inflater.inflate(R.menu.post_menu, menu);
            }
            else {
                if(Auth.getCurrentUser() != null) {
                    Firestore.getUserData(Auth.getCurrentUser().getUid())
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(task.getResult() != null) {
                                            // 사용자가 Admin이면 Menu 추가
                                            User user = task.getResult().toObject(User.class);
                                            if (user != null && user.isAdmin()) {
                                                // Menu 보이기
                                                inflater.inflate(R.menu.post_menu, menu);
                                            }
                                        }
                                    }
                                }
                            });
                }
            }
        } catch (NullPointerException e) {
            // 비로그인 사용자
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Google 정책에 따라 MenuItem에 Switch 사용하지 않고 if문 사용
        int itemId = item.getItemId();

        // Actionbar 뒤로가기 버튼
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        // 삭제 버튼
        else if(itemId == R.id.post_delete) {
            confirmDelete();
        }

        return super.onOptionsItemSelected(item);
    }

    public void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.post_deletewarning_msg))
                .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost();
                        finish();
                    }
                })
                .setNegativeButton(getString(android.R.string.no), null)
                .create().show();
    }

    private void deletePost() {
        loadingDialog.show();

        Firestore.removePost(write_info.getDocId())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            ArrayList<String> contents = write_info.getContents();
                            for(String content : contents) {
                                if(content.contains("https://firebasestorage.googleapis.com/v0/b/petstree-c5e85.appspot.com/o/posts")) {
                                    Storage.deleteStorageFile( Storage.getStorageInstance().getReferenceFromUrl(content))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> storageTask) {
                                                    loadingDialog.dismiss();
                                                    if (storageTask.isSuccessful()) {
                                                        Toast.makeText(Post.this, R.string.post_deletecomplete, Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Log.e(LOG_TAG, "Delete Post Image Error", storageTask.getException());
                                                        Toast.makeText(Post.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                        else {
                            Log.e(LOG_TAG, "Delete Post Error", task.getException());
                            Toast.makeText(Post.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    }
                });
    }

    private void saveComment() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String content = comment_et.getText().toString();

        comment = new Comment(user.getUid(), content, new Date(), write_info.getDocId());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("comments").document(String.valueOf(new Date())).set(comment)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            //댓글수 늘리기
                            db.collection("posts").document(write_info.getDocId()).update("num_comments", FieldValue.increment(1));
                            //댓글창에 넣은 내용 clear
                            comment_et.setText("");
                        }
                        else {
                            Log.w(LOG_TAG, "Error adding document", task.getException());
                        }
                    }
                });
    }

    private void setCommentData(Task<QuerySnapshot> task) {
        ArrayList<Comment> comments = new ArrayList<>();

        for(int i = 0; i < task.getResult().getDocuments().size(); i++) {
            DocumentSnapshot doc = task.getResult().getDocuments().get(i);
            Log.d(LOG_TAG, doc.getId() + "=>" + doc.getData());
            Comment comment = doc.toObject(Comment.class);

            // post ID가 일치하는 경우만 가져오기
            if(comment.getPostId().equals(write_info.getDocId())) {
                comments.add(comment);
                commentAdapter.setItems(comments);
            }
        }
        loadingDialog.dismiss();
    }
}



