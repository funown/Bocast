package per.funown.bocast.modules.user.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import per.funown.bocast.library.entity.Episode;
import per.funown.bocast.library.entity.HistoryItem;
import per.funown.bocast.library.entity.Podcast;
import per.funown.bocast.library.repo.EpisodeRepository;
import per.funown.bocast.library.repo.PodcastRepository;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class HistoryViewModel extends AndroidViewModel {

  private HistoryItemRepository historyItemRepository;
  private EpisodeRepository episodeRepository;
  private PodcastRepository podcastRepository;
  private LiveData<List<HistoryItem>> items;

  public HistoryViewModel(@NonNull Application application) {
    super(application);
    historyItemRepository = new HistoryItemRepository(application);
    episodeRepository = new EpisodeRepository(application);
    podcastRepository = new PodcastRepository(application);
    items = historyItemRepository.getItems();
  }

  public LiveData<List<HistoryItem>> getItems() {
    return items;
  }

  public void addHistory(HistoryItem... items) {
    historyItemRepository.addHistory(items);
  }

  public void updateHistory(HistoryItem... items) {
    historyItemRepository.updateHistory(items);
  }

  public void deleteHistory(HistoryItem... items) {
    historyItemRepository.deleteHistory(items);
  }

  public void deleteAll() {
    historyItemRepository.deleteAllHistory();
  }

  public Episode getEpisode(long episodeId) {
    return episodeRepository.getEpisodeById(episodeId);
  }

  public Podcast getPodcast(long podcastId) {
    return podcastRepository.getPodcastById(podcastId);
  }
}
