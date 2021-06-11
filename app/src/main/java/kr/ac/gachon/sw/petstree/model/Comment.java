package kr.ac.gachon.sw.petstree.model;

import java.sql.Timestamp;
import java.util.Date;

import com.google.firebase.firestore.Exclude;

public class Comment {
    private String userId;
    private String content;
    private Date writeTime;
    private String publisherNick;
    private String postId;

    public Comment() { }

    public Comment(String userId, String content, Date writeTime, String postId) {
        this.userId = userId;
        this.content = content;
        this.writeTime = writeTime;
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) { this.content = content; }

    public Date getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(Date writeTime) {
        this.writeTime = writeTime;
    }

    public String getPostId(){ return postId;}

    public void setPostId(String postId){ this.postId = postId; }

    @Exclude
    public String getPublisherNick() {
        return publisherNick;
    }
    public void setPublisherNick(String publisherNick) {
        this.publisherNick = publisherNick;
    }
}
