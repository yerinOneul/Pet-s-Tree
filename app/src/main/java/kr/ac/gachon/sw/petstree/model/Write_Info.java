package kr.ac.gachon.sw.petstree.model;

import java.util.ArrayList;
import java.util.Date;

public class Write_Info {
    private String title;
    private ArrayList<String> contents;
    private String publisher;
    private Date createdAt;

    /**
     * 0 - 공지사항
     * 1 - 반려동물 일상 게시판
     * 2 - 반려동물 관련 질문 게시판
     * 3 - 정보 공유 게시판
     * 4 - 반려동물 관련 질문 게시판
     * 5 - 정보 공유 게시판
     * 6 - 상담 게시판
     * 7 - 유기동물 제보
     * 8 - 오류 제보
     */
    private int boardType;

    public Write_Info(String title, ArrayList<String> contents, String publisher, Date createdAt, int boardType){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.boardType = boardType;
    }

    public String getTitle(){ return this.title; }
    public void setTitle(String title){this.title = title; }
    public ArrayList<String> getContents(){ return this.contents; }
    public void setContents(ArrayList<String> contents){ this.contents = contents;}
    public String getPublisher(){ return this.publisher; }
    public void setPublisher(String publisher){ this.publisher = publisher;}
    public Date getCreateAt(){ return this.createdAt; }
    public void setCreateAt(Date publisher){ this.createdAt = createdAt;}
    public int getBoardType() { return boardType; }
    public void setBoardType(int boardType) { this.boardType = boardType; }
}
