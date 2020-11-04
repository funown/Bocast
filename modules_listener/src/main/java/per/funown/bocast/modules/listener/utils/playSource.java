package per.funown.bocast.modules.listener.utils;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface playSource extends Iterable<MediaMetadataCompat> {

  void load();

  boolean whenReady();

  List<MediaMetadataCompat> search(String query, Bundle bundle);

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({STATE_CREATED, STATE_INITIALIZING, STATE_INITIALIZED, STATE_ERROR})
  @interface State {}
  int STATE_CREATED = 1;
  int STATE_INITIALIZING = 2;
  int STATE_INITIALIZED = 3;
  int STATE_ERROR = 4;

  abstract class PodcastPlaySource implements playSource {

    @Override
    public boolean whenReady() {
      return false;
    }
  }
}
