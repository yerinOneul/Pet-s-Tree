package kr.ac.gachon.sw.petstree;

//유기동물 api 활용 시 필요한 url을 만드는 class
// bgnde, endde, upkind, kind : 검색 시작일, 검색 종료일, 축종, 품종
// upr_cd, org_cd, care_reg_no, state, neuter_yn : 시도코드, 시군구코드, 보호소코드, 상태 (공고중 ,보호중 ..), 중성화 여부
// pageNo, numOfRows, totalCount : 페이지 수, 한 페이지 내 item 개수, 총 검색된 item들의 개수

public class Animal_url implements Cloneable{
    private String bgnde="",endde="",upkind="",kind="";
    private String upr_cd="",org_cd="",care_reg_no="",state="",neuter_yn="";
    private String pageNo="",numOfRows="", totalCount="";

    public void setBgnde(String bgnde){ this.bgnde = bgnde; }

    public String getBgnde(){ return this.bgnde; }

    public void setEndde(String endde){ this.endde = endde; }

    public String getEndde(){ return this.endde; }

    public void setUpkind(String upkind){ this.upkind = upkind; }

    public String getUpkind(){ return this.upkind; }

    public void setKind(String kind){ this.kind= kind; }

    public String getKind(){ return this.kind; }

    public void setUpr_cd(String upr_cd){ this.upr_cd = upr_cd; }

    public String getUpr_cd(){ return this.upr_cd; }

    public void setOrg_cd(String org_cd){ this.org_cd = org_cd; }

    public String getOrg_cd(){ return this.org_cd; }

    public void setCare_reg_no(String care_reg_no){ this.care_reg_no = care_reg_no ; }

    public String getCare_reg_no(){ return this.care_reg_no; }

    public void setState(String state){ this.state = state ; }

    public String getState(){ return this.state; }

    public void setNeuter_yn(String neuter_yn){ this.neuter_yn = neuter_yn; }

    public String getNeuter_yn(){ return this.neuter_yn; }

    public void setPageNo(String pageNo){ this.pageNo = pageNo ; }

    public String getPageNo(){ return this.pageNo; }

    public void setNumOfRows(String numOfRows){ this.numOfRows = numOfRows; }

    public String getNumOfRows(){ return this.numOfRows; }

    public void setTotalCount(String totalCount){ this.totalCount = totalCount; }

    public String getTotalCount(){ return this.totalCount; }

    //설정된 검색 조건에 따른 url 생성
    public String getUrl(){
        String start,end,sido,sigungu,shelter,kindUp,kindDown;
        start = (this.bgnde=="") ? "" : "bgnde="+this.bgnde+"&";
        end = (this.endde=="") ? "" : "endde="+this.endde+"&";
        sido = (this.upr_cd=="") ? "" : "upr_cd="+this.upr_cd+"&";
        sigungu = (this.org_cd=="") ? "" : "org_cd="+this.org_cd+"&";
        shelter = (this.care_reg_no=="") ? "" : "care_reg_no="+this.care_reg_no+"&";
        kindUp =  (this.upkind=="") ? "" : "upkind="+this.upkind+"&";
        kindDown =  (this.kind=="") ? "" : "kind="+this.kind+"&";

        String url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?"+
                start+end+kindUp+kindDown+sido+sigungu+shelter+
                "pageNo="+this.pageNo+"&numOfRows=10&ServiceKey=";
        return url;
    }

    public Object clone() throws CloneNotSupportedException {

        return super.clone();

    }

}
