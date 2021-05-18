package kr.ac.gachon.sw.petstree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

import kr.ac.gachon.sw.petstree.model.User;
import kr.ac.gachon.sw.petstree.util.Auth;
import kr.ac.gachon.sw.petstree.util.Firestore;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ImageView ivMenu;

    private long lastPressedTime = 0;
    private long backPressedTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 상단 툴바 추가
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // 메뉴 레이아웃 불러오기
        mDrawerLayout = (DrawerLayout)findViewById(R.id.main_drawer_layout);

        // 메뉴 아이콘 지정, 오른쪽에서 메뉴가 나타남
        ivMenu = findViewById(R.id.iv_menu);
        ivMenu.setVisibility(View.VISIBLE);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // 유저 정보 설정
        setUserInfo();

        // Navigation Drawer Action 설정
        setNavAction();

        // 메뉴 이쁘게 만들려고 메뉴 리소스 안 쓰고 커스텀했습니다
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                item.setCheckable(false);
//                mDrawerLayout.closeDrawers();
//
//                int id = item.getItemId();
//                if (id == R.id.nav_login) {
//                    Intent intent = new Intent(getApplicationContext(), Login.class);
//                    startActivity(intent);
//                }
//                else if (id == R.id.nav_community) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, new Home());
//                }
//                else if (id == R.id.nav_abandoned) {
//                    Intent intent = new Intent(getApplicationContext(), Animal_info.class);
//                    startActivity(intent);
//                }
//                return true;
//            }
//        });

    }

    /**
     * Navigation Drawer Action 설정
     */
    private void setNavAction() {
        TextView notice = findViewById(R.id.nav_notice);
        TextView communityQnA = findViewById(R.id.nav_community_qna);
        TextView communityShare = findViewById(R.id.nav_community_share);
        TextView communityCounseling = findViewById(R.id.nav_community_counseling);
        TextView abandonedReport = findViewById(R.id.nav_abandoned_report);
        TextView errorReport = findViewById(R.id.nav_report);

        // 게시판은 프레그먼트 형태로 메인 액티비티에 표시됩니다.
        TextView community = findViewById(R.id.nav_community);
        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, new Home());
            }
        });

        // 유기동물 검색
        TextView abandoned = findViewById(R.id.nav_abandoned);
        abandoned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(getApplicationContext(), Animal_info.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Navigation Drawer에 사용자 정보를 작성한다
     * @author Minjae Seon
     */
    private void setUserInfo() {
        TextView tvLogin = findViewById(R.id.nav_login);
        TextView tvNickName = findViewById(R.id.navi_header_user_nickname);
        TextView tvClass = findViewById(R.id.navi_header_user_type);

        // 로그인 되어있다면
        if(Auth.getCurrentUser() != null) {
            // 유저 데이터 로드
            Firestore.getUserData(Auth.getCurrentUser().getUid())
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            // 성공했다면
                            if (task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                tvNickName.setText(user.getUserNickName());
                                tvClass.setText(getResources().getStringArray(R.array.userclass)[user.getUserType()]);
                                tvLogin.setText(getString(R.string.logout));
                            }
                            // 실패시
                            else {
                                // 에러 메시지 띄우고 로그아웃 후 다시 setUserInfo() 실행
                                Toast.makeText(MainActivity.this, R.string.error_server, Toast.LENGTH_SHORT).show();
                                Auth.signOut();
                                setUserInfo();
                            }
                        }
                    });
        }
        // 비로그인 상태라면
        else {
            tvNickName.setText(getString(R.string.nologin));
            tvClass.setText("");
            tvLogin.setText(getString(R.string.login));
        }

        // 로그인 버튼 OnClickListener
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 상태라면
                if(Auth.getCurrentUser() != null) {
                    // 로그아웃 여부 묻는 Dialog 띄우기
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.main_logout_dialog_title);
                    builder.setMessage(R.string.main_logout_dialog_msg);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        // Yes 눌렀을 때 처리
                        public void onClick(DialogInterface dialog, int which) {
                            // 로그아웃 처리
                            Auth.signOut();
                            // 다시 setUserInfo 실행
                            setUserInfo();
                            // 로그아웃 Toast
                            Toast.makeText(MainActivity.this, R.string.main_logout_toast, Toast.LENGTH_SHORT).show();
                        }
                    });
                    // No는 아무 동작 안함
                    builder.setNegativeButton(android.R.string.no, null);
                    builder.create().show();
                }
                // 로그인 상태 아니면 로그인으로
                else {
                    Intent loginIntent = new Intent(MainActivity.this, Login.class);
                    startActivity(loginIntent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Drawer 열려있다면
        if(mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            // 닫음
            mDrawerLayout.closeDrawers();
        }
        // 닫혀있다면
        else {
            // 두 번 눌러서 종료할 수 있도록 함
            if (System.currentTimeMillis() > lastPressedTime + backPressedTime) {
                lastPressedTime = System.currentTimeMillis();
                Toast.makeText(this, getString(R.string.backpressed), Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUserInfo();
    }
}