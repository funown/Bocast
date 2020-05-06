package per.funown.bocast.library.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import java.io.File;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class FileUtil {

  private static final String TAG = FileUtil.class.getSimpleName();

  public static void deleteFile(String filePath) {
    File file = new File(filePath);
    if (file.isFile() && file.exists()) {
      file.delete();
    }
  }

  public static void deleteFileCompletely(Context context, String filePath, String filename) {
    File file = new File(filePath);
    if (file.isFile() && file.exists()) {
      file.delete();
    }
    updateMedia(context, filename);
  }


  private static void updateMedia(Context context, String filename) {
    MediaScannerConnection.scanFile(context, new String[]{filename}, null, new MediaScannerConnection.OnScanCompletedListener() {
      public void onScanCompleted(String path, Uri uri) {
        Log.i(TAG, "Scanned " + path + ":");
        Log.i(TAG, "-> uri=" + uri);
      }
    });
  }





}
