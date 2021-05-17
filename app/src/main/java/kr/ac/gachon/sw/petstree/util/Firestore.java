package kr.ac.gachon.sw.petstree.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

import kr.ac.gachon.sw.petstree.model.User;

public class Firestore {
    /**
     * Firestore의 Instance를 반환한다
     * @author Minjae Seon
     * @return FirebaseFirestore Instance
     */
    public static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    /**
     * 전문가 인증 요청 데이터를 DB에 쓴다
     * @author Minjae Seon
     * @param userId 사용자 Firebase UID
     * @param imgUrl 이미지 URL
     * @return Task<Void>
     */
    public static Task<Void> writeCertRequestData(String userId, String imgUrl) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("time", new Timestamp(new Date()));
        data.put("imgUrl", imgUrl);
        return getFirestoreInstance().collection("certreq").document(userId).set(data);
    }

    /**
     * 새로운 사용자 데이터를 DB에 작성한다
     * @param userId User Firebase UID
     * @param nickName User Nickname
     * @param certOk 전문가 인증 여부 (일반 유저는 바로 true)
     * @param userType User Type (0 - Normal, 1 - Expect)
     * @return Task<Void>
     */
    public static Task<Void> writeNewUserData(String userId, String nickName, boolean certOk, int userType) {
        User newUser = new User(nickName, certOk, userType);
        return getFirestoreInstance().collection("users").document(userId).set(newUser);
    }

    /**
     * 전문가 사용자의 인증 여부를 True로 변경한다
     * @param userId User Firebase UID
     * @return Task<Void>
     */
    public static Task<Void> updateExpectCertSuccess(String userId) {
        return getFirestoreInstance().collection("users").document(userId).update("certOk", true);
    }

    /**
     * 지정된 ID의 사용자 정보를 얻는다
     * @param userId 사용자 Firebase UID
     * @return Task<DocumentSnapshop>
     */
    public static Task<DocumentSnapshot> getUserData(String userId) {
        return getFirestoreInstance().collection("users").document(userId).get();
    }

}
