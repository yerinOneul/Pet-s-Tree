package kr.ac.gachon.sw.petstree;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
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
    private ActionBar actionBar;
    private Button save_btn;
    private Button cancel_btn;
    private ImageButton image_btn;
    private Spinner spinner;
    private RelativeLayout loaderLayout;
    private static final String TAG = "Write";
    private FirebaseUser user;
    private ArrayList<String> pathList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
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
        spinner = findViewById(R.id.write_spinner);
        loaderLayout = findViewById(R.id.loaderLayout);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.write);
        }

        String[] items = getResources().getStringArray(R.array.boardType);

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), items[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "게시판을 선택해주세요.", Toast.LENGTH_SHORT).show();
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
                startToast("게시글 작성을 취소했습니다.");
                finish();
            }
        });

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Write.this)
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

                    ImageView imageView = new ImageView(Write.this);
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(this).load(ImagePicker.Companion.getFilePath(data)).override(1000).into(imageView);
                    parent.addView(imageView);

                    EditText editText = new EditText(Write.this);
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
        final String title = ((EditText)findViewById(R.id.title)).getText().toString();
        //final String contents = ((EditText)findViewById(R.id.contents)).getText().toString();

        if(title.length()>0){
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
                else {
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
                                            // BoardType은 Write_Info 참조 (0 ~ 8)
                                            Write_Info write_Info = new Write_Info(title, contentsList, user.getUid(), new Date(), spinner.getSelectedItemPosition());
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
            // 이미지 넣지 않아도 게시글 저장되게 하기
            if(pathList.size() == 0){
                Write_Info write_Info = new Write_Info(title, contentsList, user.getUid(), new Date(), spinner.getSelectedItemPosition());
                storeUpload(write_Info);
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

    private void myStartActivity(Class c, String media){
        Intent intent = new Intent(this, c);
        intent.putExtra("media", media);
        startActivityForResult(intent, 0);
    }

}
