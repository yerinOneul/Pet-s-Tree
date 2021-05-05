package kr.ac.gachon.sw.petstree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Animal_info extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_info);

        TextView info = (TextView)findViewById(R.id.test);
        Button btn = (Button)findViewById(R.id.btn);

        //조회 버튼 클릭 시
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test용 시도 조회 url
                String url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sido?ServiceKey=";
                //api_info.xml에 등록된 service key
                String key = getString(R.string.service_key);
                new Animal_API(info,url+key).execute();
            }
        });
    }
}
