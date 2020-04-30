package per.funown.bocast.config;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;
import android.webkit.PermissionRequest;
import android.widget.Toast;
import com.lzx.starrysky.delayaction.Valid;
import com.lzx.starrysky.provider.SongInfo;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import java.security.Permission;
import java.security.Permissions;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PlayValid implements Valid {

  private Context mContext;
  //  private MusicRequest

  PlayValid(Context context) {
    mContext = context;
  }

  @Override
  public void doValid(SongInfo songInfo, ValidCallback callback) {
  }
}
