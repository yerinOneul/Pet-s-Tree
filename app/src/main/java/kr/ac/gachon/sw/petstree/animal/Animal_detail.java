package kr.ac.gachon.sw.petstree.animal;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import kr.ac.gachon.sw.petstree.R;

public class Animal_detail extends Fragment {
    private View root;
    private int pos;
    private String url;
    private TextView stateView,kindView,sexView,ageView,weightView,colorView,detailView;
    private ImageView image;
    private Button care_call,call;

    public static Animal_detail getInstance(String url, int position) {
        Animal_detail animal_detail = new Animal_detail();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putInt("position",position);
        animal_detail.setArguments(bundle);
        return animal_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            url = getArguments().getString("url");
            pos = getArguments().getInt("position");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_animal_detail, container, false);
        image = (ImageView) root.findViewById(R.id.detail_image);
        stateView = (TextView)root.findViewById(R.id.detail_state);
        kindView = (TextView)root.findViewById(R.id.detail_kind);
        sexView = (TextView)root.findViewById(R.id.detail_sex);
        ageView  = (TextView)root.findViewById(R.id.detail_age);
        weightView  = (TextView)root.findViewById(R.id.detail_weight);
        colorView  = (TextView)root.findViewById(R.id.detail_color);
        detailView  = (TextView)root.findViewById(R.id.detail);
        care_call = (Button)root.findViewById(R.id.detail_care_call);
        call = (Button)root.findViewById(R.id.detail_call);


        new loadData(url, getString(R.string.service_key), pos).execute();
        return root;
    }

    private class  loadData extends AsyncTask<Void, Integer, Document> {
        private String s_url;
        private String key;
        private int position;

        //?????? ?????? ???
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
        protected Document doInBackground(Void... params) { //url??? ?????? ????????? ????????????
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
        protected void onPostExecute(Document doc) { //????????? ????????? ?????? ??????
            // xml parsing
            NodeList nodeList = doc.getElementsByTagName("item");
            Node node = nodeList.item(this.position);
            Element fstElmnt = (Element) node;
            NodeList popfile = fstElmnt.getElementsByTagName("popfile");
            String image_url = popfile.item(0).getChildNodes().item(0).getNodeValue();
            Glide.with(getContext()).load(image_url).into(image);
            NodeList happenD = fstElmnt.getElementsByTagName("happenDt");                    //?????????
            String happenDt = happenD.item(0).getChildNodes().item(0).getNodeValue();
            NodeList happenP = fstElmnt.getElementsByTagName("happenPlace");                //????????????
            String happenPlace = happenP.item(0).getChildNodes().item(0).getNodeValue();
            NodeList kindCd = fstElmnt.getElementsByTagName("kindCd");                      //??????
            String kind = kindCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList colorCd = fstElmnt.getElementsByTagName("colorCd");                    //??????
            String color = colorCd.item(0).getChildNodes().item(0).getNodeValue();
            NodeList ageN = fstElmnt.getElementsByTagName("age");                           //??????
            String age = ageN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList weightN = fstElmnt.getElementsByTagName("weight");                     //??????
            String weight = weightN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList noticeNoN = fstElmnt.getElementsByTagName("noticeNo");                 //????????????
            String noticeNo = noticeNoN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList noticeSdtN = fstElmnt.getElementsByTagName("noticeSdt");               //???????????????
            String noticeSdt = noticeSdtN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList noticeEdtN = fstElmnt.getElementsByTagName("noticeEdt");               //???????????????
            String noticeEdt = noticeEdtN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList processStateN = fstElmnt.getElementsByTagName("processState");        // ??????
            String processState = processStateN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList sexCdN = fstElmnt.getElementsByTagName("sexCd");                       //??????
            String sexCd = sexCdN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList neuterYnN = fstElmnt.getElementsByTagName("neuterYn");               //????????? ??????
            String neuterYn = neuterYnN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList specialMarkN = fstElmnt.getElementsByTagName("specialMark");           //??????
            String specialMark = specialMarkN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList careNmN = fstElmnt.getElementsByTagName("careNm");                     //???????????????
            String careNm = careNmN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList careTelN = fstElmnt.getElementsByTagName("careTel");                   //?????????????????????
            String careTel = careTelN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList careAddrN = fstElmnt.getElementsByTagName("careAddr");                 //????????????
            String careAddr = careAddrN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList orgNmN = fstElmnt.getElementsByTagName("orgNm");                       // ????????????
            String orgNm = orgNmN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList chargeNmN = fstElmnt.getElementsByTagName("chargeNm");                 //?????????
            String chargeNm = chargeNmN.item(0).getChildNodes().item(0).getNodeValue();
            NodeList officetelN = fstElmnt.getElementsByTagName("officetel");               //????????? ?????????
            String officetel = officetelN.item(0).getChildNodes().item(0).getNodeValue();
            
            //????????? ???????????? ?????? ?????? ????????? ??????????????? ??????
            stateView.setText(processState);
            if (!processState.equals("?????????")) {
                stateView.setBackgroundColor(Color.RED);
                stateView.setTextColor(Color.WHITE);
            }
            kindView.setText(kind);
            if (sexCd == "F")
                sexView.setText("?????? : ??????");
            else if (sexCd == "M")
                sexView.setText("?????? : ??????");
            else
                sexView.setText("?????? : ??????");
            colorView.setText("?????? : "+color);
            ageView.setText(age);
            weightView.setText("?????? : "+weight);
            if (neuterYn == "Y")
                neuterYn = "???";
            else if (neuterYn == "N")
                neuterYn = "X";
            else
                neuterYn = "??????";


            detailView.setText("???????????? : " + happenDt + "\n" +
                    "??????????????? : " + happenPlace + "\n" +
                    "????????? ?????? : " + noticeNo + "\n" +
                    "??????????????? : " + noticeSdt +  " ~ " + noticeEdt +"\n" +
                    "?????????????????? : " + neuterYn + "\n" +
                    "????????? : " + specialMark + "\n" +
                    "???????????? ?????? : " + careNm + "\n" +
                    "????????? ?????? : " + careAddr + "\n" +
                    "????????? ?????? : " + orgNm + "\n" +
                    "???????????? : " + chargeNm + "\n"
            );

            care_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+careTel));
                    startActivity(intent);
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+officetel));
                    startActivity(intent);
                }
            });


            String str = popfile.item(0).getChildNodes().item(0).getNodeValue();
            Glide.with(getContext()).load(str).into(image);
            super.onPostExecute(doc);
        }

    }
}
