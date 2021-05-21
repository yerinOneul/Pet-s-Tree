package kr.ac.gachon.sw.petstree.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
     *
     * @return FirebaseStorage Instance
     * @author Minjae Seon
     */
    public static FirebaseStorage getStorageInstance() {
        return FirebaseStorage.getInstance();
    }

    /**
     * 전문가 인증 이미지를 업로드한다
     *
     * @param userId 사용자 Firebase ID
     * @param bitmap Image Bitmap
     * @return Task<UploadTask.TaskSnapshot> (업로드 Task)
     * @author Minjae Seon
     */
    public static Task<UploadTask.TaskSnapshot> uploadExpectCertImg(String userId, Bitmap bitmap) {
        StorageReference userCertRef = expectCertRef.child(userId + "/cert.jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        return userCertRef.putBytes(data);
    }

    /**
     * URL을 통해 이미지를 불러온다
     *
     * @param URL 이미지 URL
     * @return Task<byte []>
     */
    public static Task<byte[]> getImageFromURL(String URL) {
        StorageReference profileReference = getStorageInstance().getReferenceFromUrl(URL);
        return profileReference.getBytes(1500000);
    }

    /**
     * 파일을 삭제한다
     * @param ref 삭제할 위치의 StorageReference
     * @return Task<Void>
     */
    public static Task<Void> deleteStorageFile(StorageReference ref) {
        return ref.delete();
    }
}
