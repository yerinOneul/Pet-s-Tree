package kr.ac.gachon.sw.petstree.post;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.model.CertRequest;
import kr.ac.gachon.sw.petstree.model.User;
import kr.ac.gachon.sw.petstree.model.Write_Info;
import kr.ac.gachon.sw.petstree.util.Firestore;
import kr.ac.gachon.sw.petstree.util.Storage;
import kr.ac.gachon.sw.petstree.util.Util;

public class Post extends AppCompatActivity {
    private static String LOG_TAG = "Post";

    private ActionBar actionBar;
    private Write_Info write_info;


    private EditText edTitle;
    private TextView tvPublisher;
    private TextView tvTime;
    private LinearLayout parent;
    private ArrayList<String> contents = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        edTitle = findViewById(R.id.post_title);
        tvPublisher = findViewById(R.id.publisher);
        tvTime = findViewById(R.id.time);
        parent = findViewById(R.id.contentsLayout);

        if (getIntent().hasExtra("post_view")) {
            write_info = getIntent().getParcelableExtra("post_view");
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
            // array의 index가 짝수이면 text, 홀수이면 image
            //  ---> contents 내용이 firebase 주소로 시작하면 이미지, 아니면 text
            for (int i = 0; i < contents.size(); i++) {
                if (contents.get(i).startsWith("https://firebasestorage")) {
                    ImageView imageView = new ImageView(Post.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setPadding(0,25,0,25);
                    parent.addView(imageView);
                    Glide.with(this).load(contents.get(i)).override(1000).into(imageView);
                }
                else{
                    //custom view 만들어서 수정해볼게요
                    TextView editText = new TextView(Post.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    parent.addView(editText);
                    editText.setText(contents.get(i));

                }

            }
        }
    }
}



