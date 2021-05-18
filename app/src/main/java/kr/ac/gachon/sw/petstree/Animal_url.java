package kr.ac.gachon.sw.petstree;

public class Animal_url {
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

    public String getUrl(){
        String sido,sigungu,shelter,kindUp,kindDown;
        sido = (this.upr_cd=="") ? "" : "upr_cd="+this.upr_cd+"&";
        sigungu = (this.org_cd=="") ? "" : "org_cd="+this.org_cd+"&";
        shelter = (this.care_reg_no=="") ? "" : "care_reg_no="+this.care_reg_no+"&";
        kindUp =  (this.upr_cd=="") ? "" : "upkind="+this.upkind+"&";
        kindDown =  (this.kind=="") ? "" : "kind="+this.kind+"&";

        String url = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?"+
                //"bgnde=20140301&"+
                //"endde=20140430&"+
                kindUp+kindDown+sido+sigungu+shelter+
                "pageNo="+this.pageNo+"&numOfRows=10&ServiceKey=";
        return url;
    }



}
