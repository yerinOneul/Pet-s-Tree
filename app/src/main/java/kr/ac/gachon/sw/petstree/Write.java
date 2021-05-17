package kr.ac.gachon.sw.petstree;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import kr.ac.gachon.sw.petstree.model.Write_Info;

public class Write extends AppCompatActivity {
    private Button save_btn;
    private Button cancel_btn;
    private ImageButton image_btn;
    private static final String TAG = "Write";
    private FirebaseUser user;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    private int pathCount, successCount;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        save_btn = findViewById(R.id.save_btn);
        cancel_btn = findViewById(R.id.cancel_btn);
        image_btn = findViewById(R.id.image_btn);
        parent = findViewById(R.id.contentsLayout);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageUpload();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToast("게시글 작성을 취소했습니다.");
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(GalleryActivity.class, "image");
            }
        });
    }

    // 사진 선택 시 이미지를 view에 띄워준다.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode == Activity.RESULT_OK){
                   String imagePath = data.getStringExtra("imagePath");
                   pathList.add(imagePath);

                   ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );

                   ImageView imageView = new ImageView(Write.this);
                   imageView.setLayoutParams(layoutParams);
                   Glide.with(this).load(imagePath).override(1000).into(imageView);
                   parent.addView(imageView);

                   EditText editText = new EditText(Write.this);
                   editText.setLayoutParams(layoutParams);
                   editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                   parent.addView(editText);
                }
                break;
        }
    }

    private void storageUpload(){
        final String title = ((EditText)findViewById(R.id.title)).getText().toString();
        //final String contents = ((EditText)findViewById(R.id.contents)).getText().toString();

        if(title.length()>0){
            ArrayList<String> contentsList = new ArrayList<>();
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            for(int i = 0; i < parent.getChildCount(); i++){
                View view = parent.getChildAt(i);
                if(view instanceof EditText){
                    String text = ((EditText)view).getText().toString();
                    if(text.length() > 0){
                        contentsList.add(text);
                    }
                }else{
                    contentsList.add(pathList.get(pathCount));
                    final StorageReference imageRef = storageRef.child("users/" + user.getUid() + "/" + pathCount + ".jpg");
                    try{
                        InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));

                        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", ""+ (contentsList.size()-1)).build();
                        UploadTask uploadTask = imageRef.putStream(stream, metadata);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        contentsList.set(index, uri.toString());
                                        successCount++;
                                        if(pathList.size() == successCount){
                                            //업로드 완료
                                            Write_Info write_Info = new Write_Info(title, contentsList, user.getUid(), new Date());
                                            storeUpload(write_Info);

                                        }
                                    }
                                });

                            }
                        });
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    pathCount++;
                }
            }
        }else {
            startToast("내용을 입력해주세요.");
        }
    }

    private void storeUpload(Write_Info write_info){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(write_info)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference){
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                        //finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c, String media){
        Intent intent = new Intent(this, c);
        intent.putExtra("media", media);
        startActivityForResult(intent, 0);
    }

}
