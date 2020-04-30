package per.funown.bocast.modules.listener.viewmodel;

import android.app.Application;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.lzx.starrysky.provider.SongInfo;
import java.util.Date;
import java.util.List;
import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.entity.HistoryItem;
import per.funown.bocast.library.entity.CurrentEpisode;
import per.funown.bocast.modules.listener.utils.PlaySpeed;
import per.funown.bocast.modules.listener.viewmodel.repository.DownloadedEpisodeRepository;
import per.funown.bocast.modules.listener.viewmodel.repository.HistoryItemRepository;
import per.funown.bocast.modules.listener.viewmodel.repository.PlayerViewModelRepository;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PlayerViewModel extends AndroidViewModel {

  private static final String TAG = PlayerViewModel.class.getSimpleName();
  private PlayerViewModelRepository playerViewModelRepository;
  private DownloadedEpisodeRepository downloadedEpisodeRepository;
  private HistoryItemRepository historyItemRepository;
  private volatile boolean isPlaying = false;
  private volatile int playSpeed = 0;

  public PlayerViewModel(@NonNull Application application) {
    super(application);
    playerViewModelRepository = new PlayerViewModelRepository(application);
    downloadedEpisodeRepository = new DownloadedEpisodeRepository(application);
    historyItemRepository = new HistoryItemRepository(application);
  }

  public int getPlaySpeed() {
    return playSpeed;
  }

  public void setPlaySpeed(int playSpeed) {
    this.playSpeed = playSpeed;
  }

  public void addPlaySpeed() {
    if (playSpeed + 1 >= PlaySpeed.SPEEDS.length) {
      playSpeed = 0;
    }
    else {
      playSpeed += 1;
    }
  }

  public LiveData<List<CurrentEpisode>> getCurrentPodcast() {
    return playerViewModelRepository.getCurrentEpisode();
  }

  public void addCurrentEpisode(CurrentEpisode episode) {
    playerViewModelRepository.addCurrentEpisode(episode);
  }

  public void updateCurrentEpisode(CurrentEpisode episode) {
    playerViewModelRepository.updateCurrentEpisode(episode);
  }

  public void deleteCurrentEpisode(CurrentEpisode episode) {
    playerViewModelRepository.deleteCurrentEpisode();
  }

  public synchronized void setPlaying(boolean playing) {
    isPlaying = playing;
  }

  public boolean isPlaying() {
    return isPlaying;
  }

  public DownloadEpisode getDownloadEpisode(String guid, String podcastId) {
    return downloadedEpisodeRepository.getEpisode(guid, podcastId);
  }

  public void addHistory(SongInfo info, SeekBar progressbar) {
    HistoryItem historyItem = new HistoryItem();
    historyItem.setDate(new Date());
    historyItem.setEpisode(info.getSongName());
    historyItem.setEpisodeId(info.getSongId());
    historyItem.setPodcast(info.getAlbumName());
    historyItem.setImageUrl(info.getSongCover());
    historyItem.setRssLink(info.getDescription());
    historyItem.setPercent(progressbar.getProgress());
    historyItem.setTotal(progressbar.getMax());
    historyItemRepository.addHistory(historyItem);
  }
}
