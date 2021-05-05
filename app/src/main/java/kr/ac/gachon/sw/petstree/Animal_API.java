package kr.ac.gachon.sw.petstree;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class Animal_API extends AsyncTask<Void, Integer, Document> {
    private String s_url;
    private TextView info;

    public Animal_API(TextView info,String url){
        this.info = info;
        this.s_url = url;
    }

    @Override
    protected void  onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Document doInBackground(Void... params) {
        URL url;
        Document doc = null;
        //xml data load
        try {
            url = new URL(s_url);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(url.openStream()));
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
    protected void onPostExecute(Document doc) {
        // xml parsing
        String s = "";
        NodeList nodeList = doc.getElementsByTagName("item");

        for(int i = 0; i< nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            Element fstElmnt = (Element) node;

            NodeList orgCd = fstElmnt.getElementsByTagName("orgCd");
            s += "orgCd = "+  orgCd.item(0).getChildNodes().item(0).getNodeValue() +"\n";

            NodeList orgdownNm = fstElmnt.getElementsByTagName("orgdownNm");
            s += "orgdownNm = "+  orgdownNm.item(0).getChildNodes().item(0).getNodeValue() +"\n";

        }

        info.setText(s);

        super.onPostExecute(doc);
    }

}


