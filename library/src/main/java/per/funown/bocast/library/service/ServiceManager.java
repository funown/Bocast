package per.funown.bocast.library.service;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ServiceManager {
  private static ServiceManager INSTANCE;

  @Autowired
  MusicService service;

  public static ServiceManager getINSTANCE() {
    if (INSTANCE == null) {
      INSTANCE = new ServiceManager();
    }
    return INSTANCE;
  }

  private ServiceManager() {
    ARouter.getInstance().inject(this);
  }

}
