package per.funown.bocast.library.service;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.lzx.starrysky.StarrySky;
import com.lzx.starrysky.StarrySkyConfig;
import com.lzx.starrysky.control.OnPlayerEventListener;
import com.lzx.starrysky.control.PlayerControl;
import com.lzx.starrysky.provider.SongInfo;
import com.lzx.starrysky.utils.TimerTaskManager;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Route(path = "/service/play")
public class MusicService implements IProvider {

  private static PlayerControl starrySky;
  private String application;
  private Context context;
  private List<SongInfo> list;
  private static TimerTaskManager manager;

  public String getApplication() {
    return application;
  }

  public void addSong(SongInfo info) {
    list.add(info);
    starrySky.updatePlayList(list);
  }

  public void removeSong(SongInfo info) {
    list.remove(info);
    starrySky.updatePlayList(list);
  }

  public void setApplication(String application) {
    this.application = application;
  }

  public void initPlayback(Application application, @Nullable StarrySkyConfig config) {
    StarrySky.init(application, config);
    starrySky = StarrySky.with();
    starrySky.setVolume(1f);
    list = new ArrayList<>();
    starrySky.updatePlayList(list);
    manager = new TimerTaskManager();
  }

  public TimerTaskManager getManager() {
    return manager;
  }

  public PlayerControl getINSTANCE() {
    return starrySky;
  }

  @Override
  public void init(Context context) {
    this.context = context;
  }
}
