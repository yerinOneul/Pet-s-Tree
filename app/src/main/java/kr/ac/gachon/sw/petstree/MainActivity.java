package kr.ac.gachon.sw.petstree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ImageView ivMenu;

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



        TextView login = findViewById(R.id.nav_login);

        TextView notice = findViewById(R.id.nav_notice);
        TextView community = findViewById(R.id.nav_community);
        TextView communityQnA = findViewById(R.id.nav_community_qna);
        TextView communityShare = findViewById(R.id.nav_community_share);
        TextView communityCounseling = findViewById(R.id.nav_community_counseling);

        TextView abandoned = findViewById(R.id.nav_abandoned);
        TextView abandonedReport = findViewById(R.id.nav_abandoned_report);

        TextView errorReport = findViewById(R.id.nav_report);

        // 로그인 액티비티 열기
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        // 게시판은 프레그먼트 형태로 메인 액티비티에 표시됩니다.
        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, new Home());
            }
        });

        // 유기동물 검색
        abandoned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(getApplicationContext(), Animal_info.class);
                startActivity(intent);
            }
        });


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
}