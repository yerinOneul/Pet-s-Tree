package kr.ac.gachon.sw.petstree.animal;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//유기동물 api를 통하여 검색 정보 및 유기동물 정보 불러오기
//s_url, key : api 접속 주소, 서비스키
//info : 불러온 정보가 화면에 표시될 textview
//url : 검색 조건 (ex ) 시도 코드, 시군구 코드 ..)을 저장하는 animal url object
//adapter : recylerview adapter
//search : radioDialog
//mode : mode에 따라 시도 조회 ~ 유기동물 조회 구분

public class Animal_API extends AsyncTask<Void, Integer, Document> {
    private String s_url;
    private String key;
    private TextView info;
    private Animal_url url;
    private Animal_adapter adapter;
    private AlertDialog.Builder search;
    private int mode;

    //조건 조회 시
    public Animal_API(TextView info,Animal_url url,AlertDialog.Builder search, String s_url,String key,int mode){
        this.info = info; //dialog 선택 시 정보가 표시될 textview
        this.url = url;
        this.search = search; //radio dialog
        this.s_url = s_url;
        this.key = key;
        this.mode= mode; // mode == 1 -> 시도 조회 , mode == 2 -> 시군구 조회 ...
    }

    // 최종 조회 시
    public Animal_API(Animal_adapter adapter,Animal_url url, String s_url,String key, int mode){
        this.adapter = adapter;
        this.url = url;
        this.s_url = s_url;
        this.key = key;
        this.mode= mode; // mode == 1 -> 시도 조회 , mode == 2 -> 시군구 조회 ...
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
        if (mode == 1){//시도 조회
            conditionSido(doc);
        }
        else if (mode == 2){//시군구 조회
            conditionSigungu(doc);
        }
        else if (mode == 3){//보호소 조회
            conditionShelter(doc);
        }
        else if (mode == 4){//품종 조회
            conditionKind(doc);
        }
        else if (mode == 5){// 유기동물 조회
            animalSearch(doc);
        }
        super.onPostExecute(doc);
    }

    //시도 조회
    //Cd : 시도 코드 ex) 610000..
    //Nm : 시도명 ex) 서울..
    protected void conditionSido(Document doc){
        // xml parsing
        NodeList nodeList = doc.getElementsByTagName("item");
        String[] Cd = new String[nodeList.getLength()+1];
        String[] Nm = new String[nodeList.getLength()+1];
        Cd[0] = "";
        Nm[0] = "전체";
        for(int i = 1; i< nodeList.getLength()+1; i++){
            Node node = nodeList.item(i-1);
            Element fstElmnt = (Element) node;
            NodeList orgCd = fstElmnt.getElementsByTagName("orgCd");
            Cd[i] = orgCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList orgdownNm = fstElmnt.getElementsByTagName("orgdownNm");
            Nm[i] = orgdownNm.item(0).getChildNodes().item(0).getNodeValue();
        }
        info.setText(Nm[0]);
        url.setUpr_cd(Cd[0]);
        search.setSingleChoiceItems(Nm, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                info.setText(Nm[i]);
                url.setUpr_cd(Cd[i]);
            }
        });

        search.show();
    }

    //시군구 조회
    //Cd : 시군구 코드 ex) 3240000..
    //Nm : 시군구명 ex) 강동구..
    protected void conditionSigungu(Document doc){
        // xml parsing
        NodeList nodeList = doc.getElementsByTagName("item");
        String[] Cd = new String[nodeList.getLength()+1];
        String[] Nm = new String[nodeList.getLength()+1];
        Cd[0] = "";
        Nm[0] = "전체";
        for(int i = 1; i< nodeList.getLength()+1; i++){
            Node node = nodeList.item(i-1);
            Element fstElmnt = (Element) node;
            NodeList orgCd = fstElmnt.getElementsByTagName("orgCd");
            Cd[i] = orgCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList orgdownNm = fstElmnt.getElementsByTagName("orgdownNm");
            Nm[i] = orgdownNm.item(0).getChildNodes().item(0).getNodeValue();

        }
        info.setText(Nm[0]);
        url.setOrg_cd(Cd[0]);
        search.setSingleChoiceItems(Nm, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                info.setText(Nm[i]);
                url.setOrg_cd(Cd[i]);
            }
        });

        search.show();
    }

    //보호소 조회
    //Cd : 보호소 코드 ex) 284200..
    //Nm : 보호소명 ex) 유기동물보호소..
    protected void conditionShelter(Document doc){
        // xml parsing
        NodeList nodeList = doc.getElementsByTagName("item");
        String[] Cd = new String[nodeList.getLength()+1];
        String[] Nm = new String[nodeList.getLength()+1];
        Cd[0] = "";
        Nm[0] = "전체";
        for(int i = 1; i< nodeList.getLength()+1; i++){
            Node node = nodeList.item(i-1);
            Element fstElmnt = (Element) node;
            NodeList orgCd = fstElmnt.getElementsByTagName("careRegNo");
            Cd[i] = orgCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList orgdownNm = fstElmnt.getElementsByTagName("careNm");
            Nm[i] = orgdownNm.item(0).getChildNodes().item(0).getNodeValue();
        }
        info.setText(Nm[0]);
        url.setOrg_cd(Cd[0]);
        search.setSingleChoiceItems(Nm, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                info.setText(Nm[i]);
                url.setCare_reg_no(Cd[i]);
            }
        });

        search.show();
    }

    //품종 조회
    //Cd : 품종 코드 ex) 000054..
    //Nm : 품종명 ex) 골든 리트리버..
    protected void conditionKind(Document doc){
        // xml parsing
        NodeList nodeList = doc.getElementsByTagName("item");
        String[] Cd = new String[nodeList.getLength()+1];
        String[] Nm = new String[nodeList.getLength()+1];
        Cd[0] = "";
        Nm[0] = "전체";
        for(int i = 1; i< nodeList.getLength()+1; i++){
            Node node = nodeList.item(i-1);
            Element fstElmnt = (Element) node;
            NodeList orgCd = fstElmnt.getElementsByTagName("kindCd");
            Cd[i] = orgCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList orgdownNm = fstElmnt.getElementsByTagName("KNm");
            Nm[i] = orgdownNm.item(0).getChildNodes().item(0).getNodeValue();
        }
        info.setText(Nm[0]);
        url.setOrg_cd(Cd[0]);
        search.setSingleChoiceItems(Nm, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                info.setText(Nm[i]);
                url.setKind(Cd[i]);
            }
        });

        search.show();
    }


    //검색 조건에 맞는 유기동물 정보 검색
    protected void animalSearch(Document doc){
        // xml parsing
        NodeList nodeList = doc.getElementsByTagName("item");
        String nor = doc.getElementsByTagName("numOfRows").item(0).getFirstChild().getNodeValue();
        String pn = doc.getElementsByTagName("pageNo").item(0).getFirstChild().getNodeValue();
        String total = doc.getElementsByTagName("totalCount").item(0).getFirstChild().getNodeValue();
        url.setNumOfRows(nor);
        url.setPageNo(pn);
        url.setTotalCount(total);

        for(int i = 0; i< nodeList.getLength(); i++){
            Animal animal = new Animal();
            Node node = nodeList.item(i);
            Element fstElmnt = (Element) node;
            NodeList popfile = fstElmnt.getElementsByTagName("popfile");
            animal.setImage(popfile.item(0).getChildNodes().item(0).getNodeValue());
            NodeList age = fstElmnt.getElementsByTagName("age");
            animal.setAge(age.item(0).getChildNodes().item(0).getNodeValue());
            NodeList sex = fstElmnt.getElementsByTagName("sexCd");
            animal.setSex(sex.item(0).getChildNodes().item(0).getNodeValue());
            NodeList weight = fstElmnt.getElementsByTagName("weight");
            animal.setWeight(weight.item(0).getChildNodes().item(0).getNodeValue());
            NodeList kind = fstElmnt.getElementsByTagName("kindCd");
            animal.setKind(kind.item(0).getChildNodes().item(0).getNodeValue());
            NodeList place = fstElmnt.getElementsByTagName("happenPlace");
            animal.setPlace(place.item(0).getChildNodes().item(0).getNodeValue());
            NodeList shelter = fstElmnt.getElementsByTagName("careNm");
            animal.setShelter(shelter.item(0).getChildNodes().item(0).getNodeValue());


            adapter.addItem(animal);
        }

        adapter.notifyDataSetChanged();

    }

}


