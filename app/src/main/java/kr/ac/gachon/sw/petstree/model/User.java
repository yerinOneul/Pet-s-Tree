package kr.ac.gachon.sw.petstree.model;

public class User {
    // User NickName
    private String userNickName;

    // 인증 여부 (일반 유저는 가입부터 자동으로 1)
    private boolean certOk;

    // User Type (0 - Normal, 1 - Expect)
    private int userType;

    public User() {

    }

    public User(String userNickName, boolean certOk, int userType) {
        this.userNickName = userNickName;
        this.certOk = certOk;
        this.userType = userType;
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
}
