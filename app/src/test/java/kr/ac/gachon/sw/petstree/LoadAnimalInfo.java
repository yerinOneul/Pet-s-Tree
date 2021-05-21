package kr.ac.gachon.sw.petstree;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@RunWith(JUnit4.class)
public class LoadAnimalInfo {
    private String s_url;
    private String key;
    private int position;

    @Before
    public void init() {
        s_url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?pageNo=1&numOfRows=10&ServiceKey=";
        key = "ydYadYPMlmcE4TE8CY1c1z4aytuofqvw3Hh3PSShyU9kbpgk42L00hmlIUGNCA9zExyznSUVKWoovCKKXw9boA%3D%3D";
        position = 0;
    }

    @Test
    public void execute() {
        URL d_url;
        Document doc = null;
        //xml data load
        try {
            d_url = new URL(s_url + key);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(d_url.openStream()));
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            System.out.println("doInBackground ERROR : " + e);
        }

        // xml parsing
        NodeList nodeList = doc.getElementsByTagName("item");
        Node node = nodeList.item(position);
        Element fstElmnt = (Element) node;
        NodeList popfile = fstElmnt.getElementsByTagName("popfile");
        String image_url = popfile.item(0).getChildNodes().item(0).getNodeValue();
        NodeList happenD = fstElmnt.getElementsByTagName("happenDt");                    //접수일
        String happenDt = happenD.item(0).getChildNodes().item(0).getNodeValue();
        NodeList happenP = fstElmnt.getElementsByTagName("happenPlace");                //발견장소
        String happenPlace = happenP.item(0).getChildNodes().item(0).getNodeValue();
        NodeList kindCd = fstElmnt.getElementsByTagName("kindCd");                      //품종
        String kind = kindCd.item(0).getChildNodes().item(0).getNodeValue();
        NodeList colorCd = fstElmnt.getElementsByTagName("colorCd");                    //색상
        String color = colorCd.item(0).getChildNodes().item(0).getNodeValue();
        NodeList ageN = fstElmnt.getElementsByTagName("age");                           //나이
        String age = ageN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList weightN = fstElmnt.getElementsByTagName("weight");                     //체중
        String weight = weightN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList noticeNoN = fstElmnt.getElementsByTagName("noticeNo");                 //공고번호
        String noticeNo = noticeNoN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList noticeSdtN = fstElmnt.getElementsByTagName("noticeSdt");               //공고시작일
        String noticeSdt = noticeSdtN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList noticeEdtN = fstElmnt.getElementsByTagName("noticeEdt");               //공고종료일
        String noticeEdt = noticeEdtN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList processStateN = fstElmnt.getElementsByTagName("processState");        // 상태
        String processState = processStateN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList sexCdN = fstElmnt.getElementsByTagName("sexCd");                       //성별
        String sexCd = sexCdN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList neuterYnN = fstElmnt.getElementsByTagName("neuterYn");               //중성화 여부
        String neuterYn = neuterYnN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList specialMarkN = fstElmnt.getElementsByTagName("specialMark");           //특징
        String specialMark = specialMarkN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList careNmN = fstElmnt.getElementsByTagName("careNm");                     //보호소이름
        String careNm = careNmN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList careTelN = fstElmnt.getElementsByTagName("careTel");                   //보호소전화번호
        String careTel = careTelN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList careAddrN = fstElmnt.getElementsByTagName("careAddr");                 //보호장소
        String careAddr = careAddrN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList orgNmN = fstElmnt.getElementsByTagName("orgNm");                       // 관할기관
        String orgNm = orgNmN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList chargeNmN = fstElmnt.getElementsByTagName("chargeNm");                 //담당자
        String chargeNm = chargeNmN.item(0).getChildNodes().item(0).getNodeValue();
        NodeList officetelN = fstElmnt.getElementsByTagName("officetel");               //담당자 연락처
        String officetel = officetelN.item(0).getChildNodes().item(0).getNodeValue();


        System.out.println("이미지 주소 : " + image_url + "\n" +
                "접수일 : " + happenDt + "\n" +
                "발견장소 : " + happenPlace + "\n" +
                "품종 : " + kind + "\n" +
                "색상 : " + color + "\n" +
                "나이 : " + age + "\n" +
                "체충 : " + weight + "\n" +
                "공고 번호 : " + noticeNo + "\n" +
                "공고시작일 : " + noticeSdt + "\n" +
                "공고종료일 : " + noticeEdt + "\n" +
                "상태 : " + processState + "\n" +
                "성별 : " + sexCd + "\n" +
                "중성화여부 : " + neuterYn + "\n" +
                "특징 : " + specialMark + "\n" +
                "보호소 이름 : " + careNm + "\n" +
                "보호소 전화번호 : " + careTel + "\n" +
                "보호 장소 : " + careAddr + "\n" +
                "관할 기관 : " + orgNm + "\n" +
                "담당자 : " + chargeNm + "\n" +
                "담당자 연락처 : " + officetel + "\n"
        );
    }
}
