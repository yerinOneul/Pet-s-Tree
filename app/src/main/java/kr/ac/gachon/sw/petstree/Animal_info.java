package kr.ac.gachon.sw.petstree;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Animal_info extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_info);
        TextView sido = (TextView)findViewById(R.id.sido);
        TextView sigungu = (TextView)findViewById(R.id.sigungu);
        TextView shelter = (TextView)findViewById(R.id.shelter);
        TextView kindUp = (TextView)findViewById(R.id.kindUp);
        TextView kindDown = (TextView)findViewById(R.id.kindDown);
        TextView sido_code = (TextView)findViewById(R.id.sido_code);
        TextView sigungu_code = (TextView)findViewById(R.id.sigungu_code);
        TextView shelter_code = (TextView)findViewById(R.id.shelter_code);
        TextView kindUp_code = (TextView)findViewById(R.id.kindUp_code);
        TextView kindDown_code = (TextView)findViewById(R.id.kindDown_code);
        RecyclerView recycler = (RecyclerView)findViewById(R.id.recycler_abandoned_list);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(linearManager);
        Button btn = (Button)findViewById(R.id.btn);
        String key = getString(R.string.service_key); //api_info.xml에 등록된 service key

        //시도 선택
        sido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigungu.setVisibility(View.INVISIBLE);
                sigungu.setText("시군구 ▼");
                sigungu_code.setText("");
                shelter.setVisibility(View.INVISIBLE);
                shelter.setText("보호소 ▼");
                shelter_code.setText("");
                AlertDialog.Builder search = new AlertDialog.Builder(Animal_info.this);
                search.setTitle("시도를 선택해주세요");
                //시도 조회 url
                String url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sido?ServiceKey=";
                new Animal_API(sido,sido_code,search,url,key,1).execute();
                search.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sigungu.setVisibility(View.VISIBLE);
                    }
                });


            }
        });
        //시군구 선택
        sigungu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shelter.setVisibility(View.INVISIBLE);
                shelter.setText("보호소 ▼");
                shelter_code.setText("");
                AlertDialog.Builder search = new AlertDialog.Builder(Animal_info.this);
                search.setTitle("시군구를 선택해주세요");
                //시군구 조회 url
                String url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sigungu?upr_cd="+sido_code.getText()+"&ServiceKey=";
                new Animal_API(sigungu,sigungu_code,search,url,key,2).execute();
                search.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        shelter.setVisibility(View.VISIBLE);
                        Toast.makeText(Animal_info.this,shelter_code.getText(),Toast.LENGTH_LONG).show();

                    }
                });

            }
        });

        //보호소 선택
        shelter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder search = new AlertDialog.Builder(Animal_info.this);
                search.setTitle("보호소를 선택해주세요");
                //보호소 조회 url
                String url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/shelter?upr_cd="+sido_code.getText()+"&org_cd="+sigungu_code.getText()+"&ServiceKey=";
                search.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                new Animal_API(shelter,shelter_code,search,url,key,3).execute();
            }
        });

        //축종 선택
        kindUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kindDown.setVisibility(View.INVISIBLE);
                kindDown.setText("품종 ▼");
                kindDown_code.setText("");
                AlertDialog.Builder search = new AlertDialog.Builder(Animal_info.this);
                search.setTitle("축종을 선택해주세요");
                final String[] list = new String[] {"개","고양이","기타"};
                final String[] list_code = new String[] {getString(R.string.dog_code),getString(R.string.cat_code),getString(R.string.etc_code)};
                kindUp.setText(list[0]);
                kindUp_code.setText(list_code[0]);
                search.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        kindUp.setText(list[i]);
                        kindUp_code.setText(list_code[i]);
                    }
                });
                search.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        kindDown.setVisibility(View.VISIBLE);
                    }
                });
                search.show();
            }
        });

        //품종 선택
        kindDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder search = new AlertDialog.Builder(Animal_info.this);
                search.setTitle("품종을 선택해주세요");
                //품종 조회 url
                String url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/kind?up_kind_cd="+kindUp_code.getText()+"&ServiceKey=";
                search.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                new Animal_API(kindDown,kindDown_code,search,url,key,4).execute();
            }
        });


        //조회 버튼 클릭 시
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sido_url,sigungu_url,shelter_url,kindUp_url,kindDown_url;
                sido_url = (sido_code.getText()=="") ? "" : "upr_cd="+sido_code.getText()+"&";
                sigungu_url = (sigungu_code.getText()=="") ? "" : "org_cd="+sigungu_code.getText()+"&";
                shelter_url = (shelter_code.getText()=="") ? "" : "care_reg_no="+shelter_code.getText()+"&";
                kindUp_url =  (kindUp_code.getText()=="") ? "" : "upkind="+kindUp_code.getText()+"&";
                kindDown_url =  (kindDown_code.getText()=="") ? "" : "kind="+kindDown_code.getText()+"&";
                String url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?"+
                             //"bgnde=20140301&"+
                             //"endde=20140430&"+
                              kindUp_url+kindDown_url+sido_url+sigungu_url+shelter_url+
                             "pageNo=1&numOfRows=10&ServiceKey=";
                Animal_adapter adapter = new Animal_adapter();
                recycler.setAdapter(adapter);
                new Animal_API(adapter,url,key,5).execute();
            }
        });
    }
}
