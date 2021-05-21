package kr.ac.gachon.sw.petstree.model;

import com.google.firebase.firestore.Exclude;

public class User {
    // User ID
    private String userId;

    // User NickName
    private String userNickName;

    // 인증 여부 (일반 유저는 가입부터 자동으로 1)
    private boolean certOk;

    // User Type (0 - Normal, 1 - Expect)
    private int userType;

    // Admin 여부
    private boolean admin;

    public User() {

    }

    public User(String userNickName, boolean certOk, int userType) {
        this.userNickName = userNickName;
        this.certOk = certOk;
        this.userType = userType;
        this.admin = false;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public boolean isCertOk() {
        return certOk;
    }

    public void setCertOk(boolean certOk) {
        this.certOk = certOk;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Exclude
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
