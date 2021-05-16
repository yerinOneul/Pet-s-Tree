package kr.ac.gachon.sw.petstree.util;

import android.graphics.Bitmap;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Storage {
    /**
     * 전문가 인증 이미지 저장 폴더 Reference
     * expectCert/[User Id] 내에 저장되어야 함
     */
    public static StorageReference expectCertRef = getStorageInstance().getReference().child("expectCert");

    /**
     * Storage Instance를 가져온다
     * @author Minjae Seon
     * @return FirebaseStorage Instance
     */
    public static FirebaseStorage getStorageInstance() {
        return FirebaseStorage.getInstance();
    }

    /**
     * 전문가 인증 이미지를 업로드한다
     * @author Minjae Seon
     * @param userId 사용자 Firebase ID
     * @param bitmap Image Bitmap
     * @return Task<UploadTask.TaskSnapshot> (업로드 Task)
     */
    public static Task<UploadTask.TaskSnapshot> uploadExpectCertImg(String userId, Bitmap bitmap) {
        StorageReference userCertRef = expectCertRef.child(userId + "/cert.jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        return userCertRef.putBytes(data);
    }
}
