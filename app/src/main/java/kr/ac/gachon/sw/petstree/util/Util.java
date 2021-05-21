package kr.ac.gachon.sw.petstree.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    /**
     * 올바른 이메일 형태가 맞는지 반환
     * @param text 확인할 Text
     * @return 유효 여부
     */
    public static boolean isValidEmail(String text) {
        Pattern p = Pattern.compile("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$");
        Matcher m = p.matcher(text);
        return m.matches();
    }

    /**
     * Firebase Timestamp 정보를 Format에 맞춰 String 형태로 반환
     * @author Minjae Seon
     * @param timestamp com.google.firebase.Timestamp
     * @return Timestamp 정보 기반의 시간/날짜 정보 String
     */
    public static String timeStamptoString(Timestamp timestamp) {
        SimpleDateFormat detailDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);
        return detailDateFormat.format(timestamp.toDate());
    }


    /**
     * ByteArray로 된 Image를 Bitmap으로 변환한다
     * @author Minjae Seon
     * @param bytesImage Image 정보가 담긴 Byte Array
     * @return bytesImage로 넘어온 Image를 Bitmap으로 변환한 객체
     */
    public static Bitmap byteArrayToBitmap(byte[] bytesImage) {
        return BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length);
    }
}
