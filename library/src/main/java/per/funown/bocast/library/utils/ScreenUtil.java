package per.funown.bocast.library.utils;

import android.content.res.Resources;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/26
 *     desc   :
 *     version:
 * </pre>
 */
public class ScreenUtil {

  public static float dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }
}
