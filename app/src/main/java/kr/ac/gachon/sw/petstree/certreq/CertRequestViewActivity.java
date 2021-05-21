package kr.ac.gachon.sw.petstree.certreq;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.model.CertRequest;
import kr.ac.gachon.sw.petstree.model.User;
import kr.ac.gachon.sw.petstree.util.Firestore;
import kr.ac.gachon.sw.petstree.util.Storage;
import kr.ac.gachon.sw.petstree.util.Util;

public class CertRequestViewActivity extends AppCompatActivity {
    private static String LOG_TAG = "CertRequestViewActivity";

    private ActionBar actionBar;
    private CertRequest certRequest;
    private User reqUser;
    private ImageView ivReqCertPhoto;
    private TextView tvReqNick;
    private TextView tvReqTime;
    private Button btnAccept;
    private Button btnDecline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certrequestview);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ivReqCertPhoto = findViewById(R.id.iv_reqcertphoto);
        tvReqNick = findViewById(R.id.tv_reqnick);
        tvReqTime = findViewById(R.id.tv_reqtime);
        btnAccept = findViewById(R.id.btn_reqaccept);
        btnDecline = findViewById(R.id.btn_reqdecline);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            certRequest = bundle.getParcelable("certrequest");
            setData();
            setButton();
        }
        else {
            Log.w(LOG_TAG, "Null Bundle!");
            Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setData() {
        tvReqTime.setText(getString(R.string.certreqview_requesttime, Util.timeStamptoString(certRequest.getTime())));

        Firestore.getUserData(certRequest.getUserId())
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            reqUser = task.getResult().toObject(User.class);
                            reqUser.setUserId(task.getResult().getId());
                            tvReqNick.setText(reqUser.getUserNickName());
                            actionBar.setTitle(getString(R.string.certreqview_title, reqUser.getUserNickName()));
                        }
                    }
                });

        Storage.getImageFromURL(certRequest.getImgUrl())
                .addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        if(task.isSuccessful()) {
                            ivReqCertPhoto.setImageBitmap(Util.byteArrayToBitmap(task.getResult()));
                        }
                    }
                });
    }


    private void setButton() {
        // 허용 버튼
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사진 삭제
                Storage.deleteStorageFile(Storage.expectCertRef.child(certRequest.getDocId() + "/cert.jpg"))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> photoDeleteTask) {
                                // 성공시
                                if(photoDeleteTask.isSuccessful()) {
                                    // 요청 데이터 삭제
                                    Firestore.deleteExpectCertReq(certRequest.getDocId())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> deleteTask) {
                                                    // 성공시
                                                    if(deleteTask.isSuccessful()) {
                                                        // 유저 정보 업데이트
                                                        Firestore.updateExpectCert(reqUser.getUserId(), true)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()) {
                                                                            Toast.makeText(CertRequestViewActivity.this, R.string.certreqview_success, Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        }
                                                                        else {
                                                                            Log.e(LOG_TAG, "updateExpectCert Failed", task.getException());
                                                                            Toast.makeText(CertRequestViewActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                    // 실패시
                                                    else {
                                                        Log.e(LOG_TAG, "deleteTask Failed", deleteTask.getException());
                                                        Toast.makeText(CertRequestViewActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                                // 실패시
                                else {
                                    Log.e(LOG_TAG, "photoDeleteTask Failed", photoDeleteTask.getException());
                                    Toast.makeText(CertRequestViewActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // 거부 버튼
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사진 삭제
                Storage.deleteStorageFile(Storage.expectCertRef.child(certRequest.getDocId() + "/cert.jpg"))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> photoDeleteTask) {
                                // 성공시
                                if(photoDeleteTask.isSuccessful()) {
                                    // 요청 데이터 삭제
                                    Firestore.deleteExpectCertReq(certRequest.getDocId())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> deleteTask) {
                                                    // 성공시
                                                    if(deleteTask.isSuccessful()) {
                                                        // 유저 정보 업데이트
                                                        Firestore.updateExpectCert(reqUser.getUserId(), false)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()) {
                                                                            Toast.makeText(CertRequestViewActivity.this, R.string.certreqview_success, Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        }
                                                                        else {
                                                                            Log.e(LOG_TAG, "updateExpectCert Failed", task.getException());
                                                                            Toast.makeText(CertRequestViewActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                    // 실패시
                                                    else {
                                                        Log.e(LOG_TAG, "deleteTask Failed", deleteTask.getException());
                                                        Toast.makeText(CertRequestViewActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                                // 실패시
                                else {
                                    Log.e(LOG_TAG, "photoDeleteTask Failed", photoDeleteTask.getException());
                                    Toast.makeText(CertRequestViewActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
