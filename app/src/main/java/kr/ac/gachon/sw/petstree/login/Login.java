package kr.ac.gachon.sw.petstree.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;

import kr.ac.gachon.sw.petstree.MainActivity;
import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.model.User;
import kr.ac.gachon.sw.petstree.util.Auth;
import kr.ac.gachon.sw.petstree.util.Firestore;
import kr.ac.gachon.sw.petstree.util.LoadingDialog;
import kr.ac.gachon.sw.petstree.util.Util;

public class Login extends AppCompatActivity {
    EditText tvId;
    EditText tvPw;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingDialog = new LoadingDialog(this);

        TextView signUp = (TextView)findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        tvId = findViewById(R.id.login_id);
        tvPw = findViewById(R.id.login_password);

        Button btnLogin = findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 진행
                login();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // 이미 로그인 된 사용자이고 이메일 인증이 완료됐다면
        if(Auth.getFirebaseAuth().getCurrentUser() != null && Auth.isUserEmailVerified()) {
            // 전문가 인증이 완료되었는지 확인하기 위해 사용자 정보 읽어오기
            Firestore.getUserData(Auth.getCurrentUser().getUid())
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> getDataTask) {
                            // 정보 읽기 성공시
                            if(getDataTask.isSuccessful()) {
                                // 받아온 값을 User Object로 변환
                                User loginUser = getDataTask.getResult().toObject(User.class);

                                // 인증 완료라면
                                if (loginUser.isCertOk()) {
                                    // Main으로 가고 현재 Activity Finish
                                    Intent mainActIntent = new Intent(Login.this, MainActivity.class);
                                    startActivity(mainActIntent);
                                    finish();
                                }
                                // 인증이 안됐다면 오류 토스트
                                else {
                                    Log.w(Login.this.getClass().getSimpleName(), "Login Error! - Not Cert Ok");
                                    Toast.makeText(Login.this, R.string.login_notcertok, Toast.LENGTH_SHORT).show();
                                    Auth.signOut();
                                }
                            }
                            // 정보 읽기 실패했으므로 서버 에러 토스트
                            else {
                                Log.w(Login.this.getClass().getSimpleName(), "Login Error! - Get Data Failed", getDataTask.getException());
                                Toast.makeText(Login.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                Auth.signOut();
                            }
                        }
                    });
        }
        // 사용자가 null은 아니지만 이메일 인증이 안 된 경우
        else if(Auth.getFirebaseAuth().getCurrentUser() != null && !Auth.isUserEmailVerified()) {
            // 로그아웃
            Auth.signOut();
        }
    }

    private void login() {
        String id = tvId.getText().toString();
        String pw = tvPw.getText().toString();

        // 이메일이 올바르면
        if(Util.isValidEmail(id)) {
            // 비밀번호가 6글자 이상이면
            if(pw.length() >= 6) {
                // 로딩 다이얼로그 Show
                loadingDialog.show();

                // 로그인 진행
                Auth.getLoginTask(id, pw)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // 성공시
                                if (task.isSuccessful()) {
                                    // 이메일 인증이 되어있다면
                                    if(Auth.isUserEmailVerified()) {
                                        // 사용자 정보 읽어오기
                                        Firestore.getUserData(Auth.getCurrentUser().getUid())
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> getDataTask) {
                                                        // 정보 읽기 성공시
                                                        if(getDataTask.isSuccessful()) {
                                                            // 받아온 값을 User Object로 변환
                                                            User loginUser = getDataTask.getResult().toObject(User.class);

                                                            // 인증 완료라면
                                                            if (loginUser.isCertOk()) {
                                                                Toast.makeText(Login.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                                                                // Finish
                                                                finish();
                                                            }
                                                            // 인증이 안됐다면 오류 토스트
                                                            else {
                                                                Log.w(Login.this.getClass().getSimpleName(), "Login Error! - Not Cert Ok");
                                                                Toast.makeText(Login.this, R.string.login_notcertok, Toast.LENGTH_SHORT).show();
                                                                Auth.signOut();
                                                            }
                                                        }
                                                        // 정보 읽기 실패했으므로 서버 에러 토스트
                                                        else {
                                                            Log.w(Login.this.getClass().getSimpleName(), "Login Error! - Get Data Failed", getDataTask.getException());
                                                            Toast.makeText(Login.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                                            Auth.signOut();
                                                        }
                                                    }
                                                });
                                    }
                                    // 이메일 인증 안되어있다면
                                    else {
                                        // 이메일 인증해달라는 토스트 띄움
                                        Toast.makeText(Login.this, R.string.login_emailnotverify, Toast.LENGTH_SHORT).show();
                                        Auth.signOut();
                                    }
                                }
                                // 실패라면
                                else {
                                    // 에러 사유에 따라 분류
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException | FirebaseAuthInvalidCredentialsException e) {
                                        // 비밀번호나 아이디가 틀렸을 떄
                                        Log.w(Login.this.getClass().getSimpleName(), "Login Error! - Wrong Infomation", e);
                                        Toast.makeText(Login.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        // 그 이외에
                                        Log.w(Login.this.getClass().getSimpleName(), "Login Error! - Invalid User", e);
                                        Toast.makeText(Login.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                // LoadingDialog 닫음
                                loadingDialog.dismiss();
                            }
                        });
            }
            // 패스워드 에러 토스트 출력
            else {
                Toast.makeText(this, R.string.login_pwerror, Toast.LENGTH_SHORT).show();
            }
        }
        // 아이디 에러 토스트 출력
        else {
            Toast.makeText(this, R.string.login_iderror, Toast.LENGTH_SHORT).show();
        }
    }
}
