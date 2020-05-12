package per.funown.bocast;

import android.app.Application;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import cn.bmob.v3.Bmob;
import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.liulishuo.okdownload.core.Util;
import com.lzx.starrysky.StarrySkyBuilder;
import com.lzx.starrysky.StarrySkyConfig;
import java.io.File;
import java.util.concurrent.TimeUnit;
import per.funown.bocast.config.PlayConfig;
import per.funown.bocast.library.net.NetManager;
import per.funown.bocast.library.service.MusicService;
import per.funown.bocast.library.service.PollingIntentService;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class App extends Application {

  private final static String APPID_KEY = "Bmob_APP_KEY";
  private static final String TAG = App.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();

    Fresco.initialize(this);
    Util.enableConsoleLog();
    ARouter.openLog();
    ARouter.openDebug();
    ARouter.init(this);
    MusicService service = ARouter.getInstance().navigation(MusicService.class);

    service.initPlayback(this, new PlayConfig());
    service.setApplication("Bocast");

    File cache = new File(getCacheDir(), "cache");
    NetManager.setCache(cache);

    try {
      Bmob.initialize(this, this.getPackageManager()
          .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString(APPID_KEY));
    } catch (NameNotFoundException e) {
      e.printStackTrace();
    }

    Constraints constraints = new Constraints.Builder()
        .setRequiresCharging(true)
        .build();
    PeriodicWorkRequest update =
        new PeriodicWorkRequest.Builder(PollingIntentService.class, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build();
    WorkManager.getInstance(this)
        .enqueue(update);
  }

  @Override
  public void onTerminate() {
    ARouter.getInstance().destroy();
    Fresco.shutDown();
    super.onTerminate();
  }
}
