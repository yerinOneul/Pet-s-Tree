package kr.ac.gachon.sw.petstree.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

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
        return getFirestoreInstance().collection("certreq").document().set(data);
    }

}
