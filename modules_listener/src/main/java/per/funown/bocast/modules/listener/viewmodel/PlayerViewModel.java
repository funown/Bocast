package per.funown.bocast.modules.listener.viewmodel;

import java.util.Date;
import java.util.List;

import android.widget.SeekBar;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;

import com.lzx.starrysky.provider.SongInfo;

import per.funown.bocast.library.entity.HistoryItem;
import per.funown.bocast.library.entity.CurrentEpisode;
import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.repo.DownloadedEpisodeRepository;
import per.funown.bocast.library.repo.PodcastRepository;
import per.funown.bocast.modules.listener.utils.PlaySpeed;
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
  private PodcastRepository podcastRepository;
  private volatile boolean isPlaying = false;

  public PlayerViewModel(@NonNull Application application) {
    super(application);
    playerViewModelRepository = new PlayerViewModelRepository(application);
    downloadedEpisodeRepository = new DownloadedEpisodeRepository(
        application);
    historyItemRepository = new HistoryItemRepository(application);
    podcastRepository = new PodcastRepository(application);
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

  public DownloadEpisode getDownloadEpisode(long episodeId) {
    return downloadedEpisodeRepository.getEpisode(episodeId);
  }

  public void addDownloadEpisode(DownloadEpisode episode) {
    downloadedEpisodeRepository.addDownload(episode);
  }

  public void addHistory(SongInfo info, SeekBar progressbar) {
    HistoryItem historyItem = new HistoryItem();
    historyItem.setDate(new Date());
    historyItem.setEpisodeId(Long.valueOf(info.getSongId()));
    historyItem.setPercent(progressbar.getProgress());
    historyItem.setTotal(progressbar.getMax());
    historyItemRepository.addHistory(historyItem);
  }

}
