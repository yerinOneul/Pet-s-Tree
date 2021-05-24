package kr.ac.gachon.sw.petstree.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Date;

public class Write_Info implements Parcelable, Comparable {
    private String title;
    private ArrayList<String> contents;
    private String publisher;
    // 유저 닉네임 (DB엔 업로드 X)
    private String publisherNick;
    private Date createAt;
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

    public Write_Info() { }

    public Write_Info(String title, ArrayList<String> contents, String publisher, Date createAt, int boardType, int num_comments){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.createAt = createAt;
        this.boardType = boardType;
        this.num_comments = num_comments;
    }

    protected Write_Info(Parcel in) {
        title = in.readString();
        contents = in.createStringArrayList();
        publisher = in.readString();
        publisherNick = in.readString();
        createAt = (Date) in.readSerializable();
        num_comments = in.readInt();
        boardType = in.readInt();
    }

    public static final Creator<Write_Info> CREATOR = new Creator<Write_Info>() {
        @Override
        public Write_Info createFromParcel(Parcel in) {
            return new Write_Info(in);
        }

        @Override
        public Write_Info[] newArray(int size) {
            return new Write_Info[size];
        }
    };

    public String getTitle(){ return this.title; }
    public void setTitle(String title){this.title = title; }
    public ArrayList<String> getContents(){ return this.contents; }
    public void setContents(ArrayList<String> contents){ this.contents = contents;}
    public String getPublisher(){ return this.publisher; }
    public void setPublisher(String publisher){ this.publisher = publisher;}
    public Date getCreateAt(){ return this.createAt; }
    public void setCreateAt(Date createAt){ this.createAt = createAt;}
    public int getBoardType() { return boardType; }
    public void setBoardType(int boardType) { this.boardType = boardType; }
    public int getNum_comments()  { return num_comments; }
    public void setNum_comments(int boardType) { this.boardType = boardType; }

    @Exclude
    public String getPublisherNick() {
        return publisherNick;
    }
    public void setPublisherNick(String publisherNick) {
        this.publisherNick = publisherNick;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringList(contents);
        dest.writeString(publisher);
        dest.writeString(publisherNick);
        dest.writeSerializable(createAt);
        dest.writeInt(num_comments);
        dest.writeInt(boardType);
    }

    @Override
    public int compareTo(Object o) {
        return -this.getCreateAt().compareTo(((Write_Info) o).getCreateAt());
    }
}
