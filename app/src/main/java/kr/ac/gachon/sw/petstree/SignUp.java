package kr.ac.gachon.sw.petstree;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import kr.ac.gachon.sw.petstree.util.Auth;
import kr.ac.gachon.sw.petstree.util.Util;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnSignUp = (Button) findViewById(R.id.sign_up_btn);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterTask();
            }
        });
    }

    //툴바 사용
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    //툴바 선택 (뒤로가기 버튼)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작. login으로 돌아가기
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 회원가입 Task를 실행한다
     *
     * @author Minjae Seon
     */
    private void startRegisterTask() {
        RadioGroup rgUserType = (RadioGroup) findViewById(R.id.radio_usertype);

        EditText etNick = (EditText) findViewById(R.id.sign_up_nickname);
        String nick = etNick.getText().toString();

        EditText etId = (EditText) findViewById(R.id.sign_up_id);
        String id = etId.getText().toString();

        EditText etPw = (EditText) findViewById(R.id.sign_up_password);
        String pw = etPw.getText().toString();

        EditText etPwTest = (EditText) findViewById(R.id.sign_up_password_check);
        String pwTest = etPwTest.getText().toString();

        // 닉네임, 아이디, 비밀번호 항목이 비어있지 않은 경우
        if (!nick.isEmpty() && !id.isEmpty() && !pw.isEmpty()) {
            // 이메일 형태가 맞을 경우
            if (Util.isValidEmail(id)) {
                // 비밀번호가 6자리 이상일 경우
                if (pw.length() >= 6) {
                    // 비밀번호와 비밀번호 확인이 일치할경우
                    if (pw.equals(pwTest)) {
                        // 계정 생성
                        Auth.createUserTask(id, pw)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // 성공시
                                        if (task.isSuccessful()) {
                                            int selectedTypeId = rgUserType.getCheckedRadioButtonId();

                                            // 전문가 선택시
                                            if (selectedTypeId == R.id.sign_up_professor) {
                                                Intent certIntent = new Intent(SignUp.this, ExpectCertActivity.class);
                                                startActivity(certIntent);
                                                finish();
                                            }
                                            // 일반 유저 선택시
                                            else {
                                                // 인증 이메일 전송
                                                Auth.getCurrentUser().sendEmailVerification();

                                                // 인증 이메일 전송되었다고 알림
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this)
                                                        .setTitle(R.string.signup_emailsend_title)
                                                        .setMessage(R.string.signup_emailsend)
                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                finish();
                                                            }
                                                        });
                                                builder.create().show();
                                            }
                                        }
                                        // 실패시
                                        else {
                                            Toast.makeText(SignUp.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(this, R.string.signup_pwtesterror, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.signup_pwlengtherror, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.signup_emailerror, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.signup_notfillerror, Toast.LENGTH_SHORT).show();
        }
    }
}