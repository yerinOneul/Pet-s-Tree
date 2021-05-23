package kr.ac.gachon.sw.petstree;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import kr.ac.gachon.sw.petstree.model.Write_Info;

public class Post extends AppCompatActivity {
    private static String LOG_TAG = "Post";
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
            tvPublisher.setText(write_info.getPublisher());
            //날짜 형식 변환
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String date = simpleDateFormat.format(write_info.getCreateAt());
            tvTime.setText(date);

            contents = write_info.getContents();
            Log.w(LOG_TAG, String.valueOf(contents.size()));

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // array의 index가 짝수이면 text, 홀수이면 image
            //  ---> contents 내용이 firebase 주소로 시작하면 이미지, 아니면 text
            for (int i = 0; i < contents.size(); i++) {
                Log.e("내용",contents.get(i));
                if (contents.get(i).startsWith("https://firebasestorage")) {
                    ImageView imageView = new ImageView(Post.this);
                    imageView.setLayoutParams(layoutParams);
                    parent.addView(imageView);
                    Glide.with(this).load(contents.get(i)).override(1000).into(imageView);
                }
                else{
                    //custom view 만들어서 수정해볼게요
                    TextView text = new TextView(Post.this);
                    text.setLayoutParams(layoutParams);
                    parent.addView(text);
                    text.setText(contents.get(i));

                }
            }
        }
    }
}



