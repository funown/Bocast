package per.funown.bocast.library.download;

import android.content.Context;

import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.breakpoint.BreakpointStoreOnSQLite;
import com.liulishuo.okdownload.core.breakpoint.RemitStoreOnSQLite;
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/26
 *     desc   : 用于获取OkDownload实例
 *     version: 1.0
 * </pre>
 */
public class DownloadUtil {

  private static final String TAG = DownloadUtil.class.getSimpleName();

  private static OkDownload INSTANCE;

  public static void getINSTANCE(Context context) {
    if (INSTANCE == null) {
      INSTANCE = new OkDownload.Builder(context.getApplicationContext()).downloadStore(
          new BreakpointStoreOnSQLite(context).createRemitSelf()).build();
      OkDownload.setSingletonInstance(INSTANCE);
      DownloadDispatcher.setMaxParallelRunningCount(3);
      RemitStoreOnSQLite.setRemitToDBDelayMillis(3000);
    }
  }
}
