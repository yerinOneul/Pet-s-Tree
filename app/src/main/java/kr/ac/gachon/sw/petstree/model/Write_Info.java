package kr.ac.gachon.sw.petstree.model;

import java.util.ArrayList;
import java.util.Date;

public class Write_Info {
    private String title;
    private ArrayList<String> contents;
    private String publisher;
    private Date createdAt;

    public Write_Info(String title, ArrayList<String> contents, String publisher, Date createdAt){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }

    public String getTitle(){ return this.title; }
    public void setTitle(String title){this.title = title; }
    public ArrayList<String> getContents(){ return this.contents; }
    public void setContents(ArrayList<String> contents){ this.contents = contents;}
    public String getPublisher(){ return this.publisher; }
    public void setPublisher(String publisher){ this.publisher = publisher;}
    public Date getCreateAt(){ return this.createdAt; }
    public void setCreateAt(Date publisher){ this.createdAt = createdAt;}

}
