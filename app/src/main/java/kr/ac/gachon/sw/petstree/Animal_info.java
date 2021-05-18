package kr.ac.gachon.sw.petstree;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        RecyclerView recycler = (RecyclerView)findViewById(R.id.recycler_abandoned_list);
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(linearManager);
        Button btn = (Button)findViewById(R.id.btn);
        Animal_url url = new Animal_url();
        String key = getString(R.string.service_key); //api_info.xml에 등록된 service key


        //마지막 item에서 scroll 할 시 다음 페이지 갱신
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recycler.getLayoutManager());
                int total = layoutManager.getItemCount();
                int last = layoutManager.findLastCompletelyVisibleItemPosition();

                if (last >= total - 1) { //
                    Log.e("!!!!!!!!!!!",url.getTotalCount());
                    int nor = Integer.parseInt(url.getNumOfRows());
                    int pn = Integer.parseInt(url.getPageNo());
                    int tc = Integer.parseInt(url.getTotalCount());
                    if ( (nor*pn) < tc ){
                        url.setPageNo(String.valueOf(++pn));
                        String s_url = url.getUrl();
                        new Animal_API((Animal_adapter) recycler.getAdapter(),url,s_url,key,5).execute();
                    }
                    else
                        Toast.makeText(Animal_info.this,"마지막 페이지입니다.",Toast.LENGTH_LONG).show();

                }
            }
        };

        //시도 선택
        sido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigungu.setVisibility(View.INVISIBLE);
                sigungu.setText("시군구 ▼");
                url.setOrg_cd("");
                shelter.setVisibility(View.INVISIBLE);
                shelter.setText("보호소 ▼");
                url.setCare_reg_no("");
                AlertDialog.Builder search = new AlertDialog.Builder(Animal_info.this);
                search.setTitle("시도를 선택해주세요");
                //시도 조회 url
                String sido_url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sido?ServiceKey=";
                new Animal_API(sido,url,search,sido_url,key,1).execute();
                search.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(url.getUpr_cd()!="")
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
                url.setCare_reg_no("");
                AlertDialog.Builder search = new AlertDialog.Builder(Animal_info.this);
                search.setTitle("시군구를 선택해주세요");
                //시군구 조회 url
                String sigungu_url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sigungu?upr_cd="+url.getUpr_cd()+"&ServiceKey=";
                new Animal_API(sigungu,url,search,sigungu_url,key,2).execute();
                search.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(url.getOrg_cd()!="")
                            shelter.setVisibility(View.VISIBLE);
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
                String shelter_url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/shelter?upr_cd="+url.getUpr_cd()+"&org_cd="+url.getOrg_cd()+"&ServiceKey=";
                search.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                new Animal_API(shelter,url,search,shelter_url,key,3).execute();
            }
        });

        //축종 선택
        kindUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kindDown.setVisibility(View.INVISIBLE);
                kindDown.setText("품종 ▼");
                url.setUpkind("");
                AlertDialog.Builder search = new AlertDialog.Builder(Animal_info.this);
                search.setTitle("축종을 선택해주세요");
                final String[] list = new String[] {"전체","개","고양이","기타"};
                final String[] list_code = new String[] {"",getString(R.string.dog_code),getString(R.string.cat_code),getString(R.string.etc_code)};
                kindUp.setText(list[0]);
                url.setUpkind(list_code[0]);
                search.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        kindUp.setText(list[i]);
                        url.setUpkind(list_code[i]);
                    }
                });
                search.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(url.getUpkind()!="")
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
                String kind_url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/kind?up_kind_cd="+url.getUpkind()+"&ServiceKey=";
                search.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                new Animal_API(kindDown,url,search,kind_url,key,4).execute();
            }
        });


        //조회 버튼 클릭 시
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_url = url.getUrl();
                Animal_adapter adapter = new Animal_adapter();
                recycler.setAdapter(adapter);
                recycler.addOnScrollListener(onScrollListener);
                new Animal_API(adapter,url,s_url,key,5).execute();
            }
        });



    }
}
