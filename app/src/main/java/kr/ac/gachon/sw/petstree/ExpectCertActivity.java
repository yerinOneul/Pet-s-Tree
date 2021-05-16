package kr.ac.gachon.sw.petstree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import kr.ac.gachon.sw.petstree.util.Auth;
import kr.ac.gachon.sw.petstree.util.Firestore;
import kr.ac.gachon.sw.petstree.util.Storage;

public class ExpectCertActivity extends AppCompatActivity {
    ImageView ivCertImg;
    Button btnUpload;
    Bitmap currentImg = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expectcert);

        ivCertImg = findViewById(R.id.iv_certimg);
        btnUpload = findViewById(R.id.btn_certupload);

        ivCertImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(ExpectCertActivity.this)
                        .crop()
                        .galleryMimeTypes(new String[]{"image/png", "image/jpg", "image/jpeg"})
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 null 아니면
                if(currentImg != null) {
                    // 이미지 Storage에 업로드
                    Storage.uploadExpectCertImg(Auth.getCurrentUser().getUid(), currentImg)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        StorageReference ref = Storage.expectCertRef.child(Auth.getCurrentUser().getUid() + "/cert.jpg");
                                        Log.d(ExpectCertActivity.this.getClass().getSimpleName(), "Ref : " + ref.getPath());

                                        // 이미지 URL얻기
                                        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task1) {
                                                if(task1.isSuccessful()) {
                                                    Firestore.writeCertRequestData(Auth.getCurrentUser().getUid(), task1.getResult().toString())
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task2) {
                                                                    if(task2.isSuccessful()) {
                                                                        // 인증 이메일 전송
                                                                        Auth.getCurrentUser().sendEmailVerification();

                                                                        // 완료 알림
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(ExpectCertActivity.this)
                                                                                .setTitle(R.string.expectcert_success_title)
                                                                                .setMessage(R.string.expectcert_success_msg)
                                                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        finish();
                                                                                    }
                                                                                });
                                                                        builder.create().show();
                                                                    }
                                                                    else {
                                                                        Log.e(ExpectCertActivity.this.getClass().getSimpleName(), "Request Data Write Failed", task2.getException());
                                                                        Toast.makeText(ExpectCertActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                                else {
                                                    Log.e(ExpectCertActivity.this.getClass().getSimpleName(), "Get Image URL Failed", task1.getException());
                                                    Toast.makeText(ExpectCertActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        Log.e(ExpectCertActivity.this.getClass().getSimpleName(), "Image Upload Failed", task.getException());
                                        Toast.makeText(ExpectCertActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ImagePicker.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap fileBitmap = BitmapFactory.decodeFile(ImagePicker.Companion.getFilePath(data));
                currentImg = fileBitmap;
                ivCertImg.setImageBitmap(fileBitmap);
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Log.w(this.getClass().getSimpleName(), "ImagePicker Error : " + ImagePicker.Companion.getError(data));
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 막음
    }
}
