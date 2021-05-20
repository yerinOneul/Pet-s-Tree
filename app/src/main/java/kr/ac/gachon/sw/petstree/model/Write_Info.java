package kr.ac.gachon.sw.petstree.model;

import java.util.ArrayList;
import java.util.Date;

public class Write_Info {
    private String title;
    private ArrayList<String> contents;
    private String publisher;
    private Date createdAt;
    private int num_comments;

    /**
     * 0 - 공지사항
     * 1 - 반려동물 일상 게시판
     * 2 - 반려동물 관련 질문 게시판
     * 3 - 정보 공유 게시판
     * 4 - 상담 게시판
     * 5 - 유기동물 제보
     * 6 - 오류 제보
     */
    private int boardType;

    public Write_Info(String title, ArrayList<String> contents, String publisher, Date createdAt, int boardType, int num_comments){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.boardType = boardType;
        this.num_comments = num_comments;
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
    public int getNum_comments()  { return num_comments; }
    public void setNum_comments(int boardType) { this.boardType = boardType; }

}
