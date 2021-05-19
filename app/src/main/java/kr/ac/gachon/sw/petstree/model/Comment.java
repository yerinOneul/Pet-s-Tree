package kr.ac.gachon.sw.petstree.model;

import java.sql.Timestamp;

public class Comment {
    private String userId;
    private String content;
    private Timestamp writeTime;

    public Comment(String userId, String content, Timestamp writeTime) {
        this.userId = userId;
        this.content = content;
        this.writeTime = writeTime;
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

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(Timestamp writeTime) {
        this.writeTime = writeTime;
    }
}
