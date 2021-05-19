package kr.ac.gachon.sw.petstree;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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

public class Animal_detail extends Fragment {
    private View root;
    private int pos;
    private String url;
    private DrawerLayout mDrawerLayout;
    private ImageView ivMenu,image;
    public static AppCompatActivity activity;

    public static Animal_detail getInstance(String url, int position) {
        Animal_detail animal_detail = new Animal_detail();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putInt("position", 0);
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

        new loadData(url, getString(R.string.service_key), pos).execute();
        return root;
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
            String str = popfile.item(0).getChildNodes().item(0).getNodeValue();
            Glide.with(getContext()).load(str).into(image);
            super.onPostExecute(doc);
        }
    }
}
