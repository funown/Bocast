package per.funown.bocast.library.utils;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/20
 *     desc   : a tool for ARouter
 *     version: 1.0
 * </pre>
 */
public class ArouterUtil {

  /**
   * forward to specified module
   * @param path specified path
   */
  public static void navigation(String path) {
    if (path == null || path == "") return;
    ARouter.getInstance().build(path).navigation();
  }

}