package kr.ac.gachon.sw.petstree;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class Animal_API extends AsyncTask<Void, Integer, Document> {
    private String url;
    private String key;
    private TextView info;
    private TextView info_code;
    private AlertDialog.Builder search;
    private int mode;


    public Animal_API(TextView info,TextView info_code,AlertDialog.Builder search, String url,String key,int mode){
        this.info = info; //dialog 선택 시 정보가 표시될 textview
        this.info_code = info_code;
        this.search = search; //radio dialog
        this.url = url;
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
            d_url = new URL(url+key);
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

        super.onPostExecute(doc);
    }

    protected void conditionSido(Document doc){
        // xml parsing
        NodeList nodeList = doc.getElementsByTagName("item");
        String[] Cd = new String[nodeList.getLength()];
        String[] Nm = new String[nodeList.getLength()];

        for(int i = 0; i< nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            Element fstElmnt = (Element) node;
            NodeList orgCd = fstElmnt.getElementsByTagName("orgCd");
            Cd[i] = orgCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList orgdownNm = fstElmnt.getElementsByTagName("orgdownNm");
            Nm[i] = orgdownNm.item(0).getChildNodes().item(0).getNodeValue();
        }
        info.setText(Nm[0]);
        info_code.setText(Cd[0]);
        search.setSingleChoiceItems(Nm, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                info.setText(Nm[i]);
                info_code.setText(Cd[i]);
            }
        });

        search.show();
    }

    protected void conditionSigungu(Document doc){
        // xml parsing
        NodeList nodeList = doc.getElementsByTagName("item");
        String[] Cd = new String[nodeList.getLength()];
        String[] Nm = new String[nodeList.getLength()];

        for(int i = 0; i< nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            Element fstElmnt = (Element) node;
            NodeList orgCd = fstElmnt.getElementsByTagName("orgCd");
            Cd[i] = orgCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList orgdownNm = fstElmnt.getElementsByTagName("orgdownNm");
            Nm[i] = orgdownNm.item(0).getChildNodes().item(0).getNodeValue();

        }
        info.setText(Nm[0]);
        info_code.setText(Cd[0]);
        search.setSingleChoiceItems(Nm, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                info.setText(Nm[i]);
                info_code.setText(Cd[i]);
            }
        });

        search.show();
    }


    protected void conditionShelter(Document doc){
        // xml parsing
        NodeList nodeList = doc.getElementsByTagName("item");
        String[] Cd = new String[nodeList.getLength()];
        String[] Nm = new String[nodeList.getLength()];

        for(int i = 0; i< nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            Element fstElmnt = (Element) node;
            NodeList orgCd = fstElmnt.getElementsByTagName("careRegNo");
            Cd[i] = orgCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList orgdownNm = fstElmnt.getElementsByTagName("careNm");
            Nm[i] = orgdownNm.item(0).getChildNodes().item(0).getNodeValue();
        }
        info.setText(Nm[0]);
        info_code.setText(Cd[0]);
        search.setSingleChoiceItems(Nm, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                info.setText(Nm[i]);
                info_code.setText(Cd[i]);
            }
        });

        search.show();
    }

}


