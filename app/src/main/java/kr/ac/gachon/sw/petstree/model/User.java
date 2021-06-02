package kr.ac.gachon.sw.petstree.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

public class User implements Parcelable {
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

    public User() { }

    public User(String userNickName, boolean certOk, int userType) {
        this.userNickName = userNickName;
        this.certOk = certOk;
        this.userType = userType;
        this.admin = false;
    }

    protected User(Parcel in) {
        userId = in.readString();
        userNickName = in.readString();
        certOk = in.readByte() != 0;
        userType = in.readInt();
        admin = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userNickName);
        dest.writeByte((byte) (certOk ? 1 : 0));
        dest.writeInt(userType);
        dest.writeByte((byte) (admin ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
