package kr.ac.gachon.sw.petstree.util;

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
}
