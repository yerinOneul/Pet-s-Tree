package kr.ac.gachon.sw.petstree.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {
    /**
     * FirebaseAuth Instance를 가져온다
     * @author Minjae Seon
     * @return FirebaseAuth
     */
    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    /**
     * 현재 로그인 된 사용자의 정보를 불러온다
     * @author Minjae Seon
     * @return FirebaseUser
     */
    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }

    /**
     * 사용자 계정을 생성하는 Firebase Task를 실행한다
     * @param email 사용자 E-mail
     * @param password 사용자 Password
     * @return Task<AuthResult>
     */
    public static Task<AuthResult> createUserTask(String email, String password) {
        return getFirebaseAuth().createUserWithEmailAndPassword(email, password);
    }

    /**
     * 사용자의 이메일 인증 여부를 확인한다
     * @author Minjae Seon
     * @return 이메일 인증 완료 여부
     */
    public static boolean isUserEmailVerified() {
        return getCurrentUser().isEmailVerified();
    }

    /**
     * 로그인 Task를 실행해서 가져온다
     * @param email Email
     * @param password Password
     * @return Task<AuthResult>
     */
    public static Task<AuthResult> getLoginTask(String email, String password) {
        return getFirebaseAuth().signInWithEmailAndPassword(email, password);
    }

    /**
     * 현재 사용자 계정에서 로그아웃한다
     */
    public static void signOut() {
        getFirebaseAuth().signOut();
    }
}
