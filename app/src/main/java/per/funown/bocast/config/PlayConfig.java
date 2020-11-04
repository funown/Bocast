package per.funown.bocast.config;

import android.content.Context;
import androidx.annotation.NonNull;
import com.lzx.starrysky.StarrySkyBuilder;
import com.lzx.starrysky.StarrySkyConfig;
import com.lzx.starrysky.notification.StarrySkyNotificationManager;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PlayConfig extends StarrySkyConfig {

  @Override
  public void applyOptions(@NonNull Context context, @NonNull StarrySkyBuilder builder) {
    super.applyOptions(context, builder);
    builder.setOpenNotification(true);
  }
}
