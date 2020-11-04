package per.funown.bocast.library.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DigestUtils {

  public static String encode(String password) {
    try {

      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.reset();
      messageDigest.update(password.getBytes("UTF-8"));
      byte[] result = messageDigest.digest();
      StringBuffer sb = new StringBuffer();
      for (byte b : result) {
        int num = b & 0xff;
        String hex = Integer.toHexString(num);
        if (hex.length() == 1) {
          sb.append(0);
        }
        sb.append(hex);
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
