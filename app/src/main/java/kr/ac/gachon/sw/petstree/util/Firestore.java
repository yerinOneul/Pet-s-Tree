package kr.ac.gachon.sw.petstree.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;

import kr.ac.gachon.sw.petstree.model.Comment;
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

    /**
     * 지정된 Type의 게시물 정보들을 가져온다
     * @param boardType Board Type
     * 0 - 공지사항
     * 1 - 반려동물 일상 게시판
     * 2 - 반려동물 관련 질문 게시판
     * 3 - 정보 공유 게시판
     * 4 - 반려동물 관련 질문 게시판
     * 5 - 정보 공유 게시판
     * 6 - 상담 게시판
     * 상담 게시판에 있는 본인 작성 글 확인은 getUserCounselingPostData 사용 요망 - 전문가가 글 불러올때만 이 명령어 사용
     * 7 - 유기동물 제보
     * 8 - 오류 제보
     * @return Task<QuerySnapshot>
     */
    public static Task<QuerySnapshot> getBoardData(int boardType) {
        return getFirestoreInstance().collection("posts").whereEqualTo("boardType", boardType).get();
    }

    /**
     * 해당하는 User ID로 작성된 작성한 상담 게시판 글을 가져온다
     * @param userId Firebase User ID
     * @return Task<QuerySnapshot>
     */
    public static Task<QuerySnapshot> getUserCounselingPostData(String userId) {
        return getFirestoreInstance().collection("posts").whereEqualTo("boardType", 6).whereEqualTo("publisher", userId).get();
    }

    /**
     * 게시글의 댓글을 가져온다
     * @param postId 게시글 ID
     * @return Task<QuerySnapshot>
     */
    public static Task<QuerySnapshot> getComment(String postId) {
        return getFirestoreInstance().collection("posts").document(postId).collection("comment").get();
    }

    /**
     * 댓글을 작성한다
     * @param postId 게시글 ID
     * @param comment Comment Object
     * @return Task<DocumentReference>
     */
    public static Task<DocumentReference> writeComment(String postId, Comment comment) {
        return getFirestoreInstance().collection("posts").document(postId).collection("comment").add(comment);
    }

}
