package kr.ac.gachon.sw.petstree;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import kr.ac.gachon.sw.petstree.model.User;
import kr.ac.gachon.sw.petstree.util.Auth;
import kr.ac.gachon.sw.petstree.util.Firestore;

public class Animal_detail extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private TextView test;
    private ImageView ivMenu,image;
    public static AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);
        test = (TextView)findViewById(R.id.detail_test);
        image = (ImageView)findViewById(R.id.detail_image);
        activity = Animal_detail.this;
        Intent intent = getIntent();
        int pos = intent.getIntExtra("position",0);
        String url = intent.getStringExtra("url");
        String key = getString(R.string.service_key);
        //정보 불러오기
        new loadData(url,key,pos).execute();


        // 상단 툴바 추가
        Toolbar toolbar = findViewById(R.id.main);
        setSupportActionBar(toolbar);

        // 메뉴 레이아웃 불러오기
        mDrawerLayout = (DrawerLayout) findViewById(R.id.detail_drawer_layout);

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

    }

    private class  loadData extends AsyncTask<Void, Integer, Document> {
        private String s_url;
        private String key;
        private int position;

        //조건 조회 시
        public loadData(String s_url, String key, int position){
            this.s_url = s_url;
            this.key = key;
            this.position = position;
        }

        @Override
        protected void  onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(Void... params) { //url을 통해 데이터 가져오기
            URL d_url;
            Document doc = null;
            //xml data load
            try {
                d_url = new URL(s_url+key);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(d_url.openStream()));
                doc.getDocumentElement().normalize();
            } catch (Exception e) {
                Log.e("doInBackground ERROR : ", String.valueOf(e));
            }
            return doc;
        }


        @Override
        protected void onProgressUpdate(Integer... params) {

        }


        @Override
        protected void onPostExecute(Document doc) { //입력된 모드에 따라 처리
            // xml parsing
            NodeList nodeList = doc.getElementsByTagName("item");
            Node node = nodeList.item(this.position);
            Element fstElmnt = (Element) node;
            NodeList popfile = fstElmnt.getElementsByTagName("popfile");
            String image_url = popfile.item(0).getChildNodes().item(0).getNodeValue();
            Glide.with(Animal_detail.this.getApplicationContext()).load(image_url).into(image);
            NodeList happenD = fstElmnt.getElementsByTagName("happenDt");                    //접수일
            String happenDt= happenD.item(0).getChildNodes().item(0).getNodeValue();
            NodeList happenP = fstElmnt.getElementsByTagName("happenPlace");                //발견장소
            String happenPlace= happenP.item(0).getChildNodes().item(0).getNodeValue();
            NodeList kindCd = fstElmnt.getElementsByTagName("kindCd");                      //품종
            String  kind = kindCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList colorCd = fstElmnt.getElementsByTagName("colorCd");                    //색상
            String color = colorCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList ageN = fstElmnt.getElementsByTagName("age");                           //나이
            String  age= ageN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList weightN = fstElmnt.getElementsByTagName("weight");                     //체중
            String weight = weightN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList noticeNoN = fstElmnt.getElementsByTagName("noticeNo");                 //공고번호
            String  noticeNo= noticeNoN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList  noticeSdtN= fstElmnt.getElementsByTagName("noticeSdt");               //공고시작일
            String  noticeSdt= noticeSdtN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList noticeEdtN = fstElmnt.getElementsByTagName("noticeEdt");               //공고종료일
            String  noticeEdt= noticeEdtN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList  processStateN = fstElmnt.getElementsByTagName("processState");        // 상태
            String  processState= processStateN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList sexCdN = fstElmnt.getElementsByTagName("sexCd");                       //성별
            String  sexCd= sexCdN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList neuterYnN= fstElmnt.getElementsByTagName("neuterYn");               //중성화 여부
            String  neuterYn =  neuterYnN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList specialMarkN = fstElmnt.getElementsByTagName("specialMark");           //특징
            String  specialMark= specialMarkN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList  careNmN= fstElmnt.getElementsByTagName("careNm");                     //보호소이름
            String careNm = careNmN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList careTelN = fstElmnt.getElementsByTagName("careTel");                   //보호소전화번호
            String  careTel= careTelN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList  careAddrN= fstElmnt.getElementsByTagName("careAddr");                 //보호장소
            String  careAddr= careAddrN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList  orgNmN= fstElmnt.getElementsByTagName("orgNm");                       // 관할기관
            String orgNm = orgNmN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList chargeNmN = fstElmnt.getElementsByTagName("chargeNm");                 //담당자
            String chargeNm = chargeNmN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList officetelN = fstElmnt.getElementsByTagName("officetel");               //담당자 연락처
            String  officetel= officetelN.item(0).getChildNodes().item(0).getNodeValue();


            test.setText("접수일 : "+happenDt+"\n"+
                         "발견장소 : "+happenPlace+"\n"+
                         "품종 : "+kind+"\n"+
                         "색상 : "+color+"\n"+
                         "나이 : "+age+"\n"+
                         "체충 : "+weight+"\n"+
                         "공고 번호 : " +noticeNo+"\n"+
                         "공고시작일 : " +noticeSdt+"\n"+
                         "공고종료일 : "+noticeEdt+"\n"+
                         "상태 : "+processState+"\n"+
                         "성별 : "+sexCd+"\n"+
                         "중성화여부 : "+neuterYn+"\n"+
                         "특징 : " +specialMark+"\n"+
                         "보호소 이름 : "+careNm+"\n"+
                         "보호소 전화번호 : " +careTel+"\n"+
                         "보호 장소 : "+careAddr+"\n"+
                         "관할 기관 : "+orgNm+"\n"+
                         "담당자 : "+chargeNm+"\n"+
                         "담당자 연락처 : " +officetel+"\n"
                    );


            super.onPostExecute(doc);
        }


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
                //유기동물 검색을 다시 클릭 시 activity 종료가 안되는 걸 방지
                Animal_info.activity.finish();
                Intent intent = new Intent(getApplicationContext(), Animal_info.class);
                startActivity(intent);
                finish();
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
                                Toast.makeText(getApplicationContext(), R.string.error_server, Toast.LENGTH_SHORT).show();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
                            Toast.makeText(getApplicationContext(), R.string.main_logout_toast, Toast.LENGTH_SHORT).show();
                        }
                    });
                    // No는 아무 동작 안함
                    builder.setNegativeButton(android.R.string.no, null);
                    builder.create().show();
                }
                // 로그인 상태 아니면 로그인으로
                else {
                    Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                    startActivity(loginIntent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Drawer 열려있다면
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            // 닫음
            mDrawerLayout.closeDrawers();
        }
        // 닫혀있다면 뒤로 가기
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUserInfo();
    }


}
