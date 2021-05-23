package kr.ac.gachon.sw.petstree.animal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.model.Write_Info;

public class Animal_report extends AppCompatActivity {
    private ActionBar actionBar;
    private Button save_btn,cancel_btn;
    private ImageButton myLoc_btn,image_btn;
    private RelativeLayout loaderLayout;
    private static final String TAG = "Report";
    private FirebaseUser user;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    private int pathCount, successCount;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_report);
        myLoc_btn = findViewById(R.id.report_my_location);
        save_btn = findViewById(R.id.report_save_btn);
        cancel_btn = findViewById(R.id.report_cancel_btn);
        myLoc_btn = findViewById(R.id.report_my_location);
        image_btn = findViewById(R.id.report_image_btn);
        parent = findViewById(R.id.report_contentsLayout);
        loaderLayout = findViewById(R.id.loaderLayout);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("유기동물 제보");
        }

        myLoc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사용자의 현재 위치 주소 불러오기
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageUpload();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToast("유기동물 제보 작성을 취소했습니다.");
                finish();
            }
        });

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Animal_report.this)
                        .crop()
                        .galleryMimeTypes(new String[]{"image/png", "image/jpg", "image/jpeg"})
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Google 정책에 따라 MenuItem에 Switch 사용하지 않고 if문 사용
        int itemId = item.getItemId();

        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 사진 선택 시 이미지를 view에 띄워준다.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ImagePicker.REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK) {
                    pathList.add(ImagePicker.Companion.getFilePath(data));

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );

                    ImageView imageView = new ImageView(Animal_report.this);
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(this).load(ImagePicker.Companion.getFilePath(data)).override(1000).into(imageView);
                    parent.addView(imageView);

                    EditText editText = new EditText(Animal_report.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    parent.addView(editText);

                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Log.w(this.getClass().getSimpleName(), "ImagePicker Error : " + ImagePicker.Companion.getError(data));
                    Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void storageUpload(){
        final String title = ((EditText)findViewById(R.id.report_title)).getText().toString();
        final String sex = ((EditText)findViewById(R.id.report_sex)).getText().toString();
        final String age = ((EditText)findViewById(R.id.report_age)).getText().toString();
        final String comment = ((EditText)findViewById(R.id.report_comment)).getText().toString();
        final String time = ((EditText)findViewById(R.id.report_time)).getText().toString();
        final String location = ((EditText)findViewById(R.id.report_location)).getText().toString();
        final String name = ((EditText)findViewById(R.id.report_name)).getText().toString();
        final String phone = ((EditText)findViewById(R.id.report_phone)).getText().toString();

        // 이미지 넣지 않아도 게시글 저장되게 하기  ---> 유기동물 제보 시 사진 필수
        if(pathList.size() == 0){
            startToast("해당 동물의 사진을 업로드 해주세요.");
        }
        else if(title.length()>0 && time.length() > 0 && location.length() >0 && name.length() > 0 && phone.length() >0 ){
            loaderLayout.setVisibility(View.VISIBLE);
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
                }
                else if (view instanceof ImageView){
                    contentsList.add(pathList.get(pathCount));
                    final StorageReference imageRef = storageRef.child("posts/" + user.getUid() + "/" + pathCount + ".jpg");
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
                                            // 업로드 완료
                                            // BoardType은 Write_Info 참조 (0 ~ 6)
                                            Write_Info write_Info = new Write_Info(title, contentsList, user.getUid(), new Date(), 5, 0);
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
        }
        else {
            startToast("필수 기입 항목을 입력해주세요.");
        }
    }

    private void storeUpload(Write_Info write_info){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(write_info)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference){
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        loaderLayout.setVisibility(View.GONE);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Log.w(TAG, "Error adding document", e);
                        loaderLayout.setVisibility(View.GONE);
                    }
                });

    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
