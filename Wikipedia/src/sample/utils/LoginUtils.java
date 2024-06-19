package sample.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The type Login utils.
 */
public class LoginUtils {
    /**
     * Convert in md 5 string.
     *
     * @param text the text
     * @return the string
     */
    public static String convertInMD5(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(text.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : bytes) {
                sb.append(Integer.toHexString((int) (b & 0xff)));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }
}
