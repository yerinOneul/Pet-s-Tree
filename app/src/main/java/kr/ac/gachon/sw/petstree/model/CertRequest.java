package kr.ac.gachon.sw.petstree.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class CertRequest implements Parcelable {
    private String docId;
    private String userId;
    private Timestamp time;
    private String imgUrl;

    public CertRequest() {

    }

    public CertRequest(String userId, Timestamp time, String imgUrl) {
        this.userId = userId;
        this.time = time;
        this.imgUrl = imgUrl;
    }

    protected CertRequest(Parcel in) {
        this.docId = in.readString();
        this.userId = in.readString();
        this.time = in.readParcelable(Timestamp.class.getClassLoader());
        this.imgUrl = in.readString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Exclude
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.docId);
        dest.writeString(this.userId);
        dest.writeParcelable(this.time, 0);
        dest.writeString(this.imgUrl);
    }

    public static final Parcelable.Creator<CertRequest> CREATOR = new Parcelable.Creator<CertRequest>() {
        @Override
        public CertRequest createFromParcel(Parcel source) {
            return new CertRequest(source);
        }

        @Override
        public CertRequest[] newArray(int size) {
            return new CertRequest[size];
        }
    };
}
