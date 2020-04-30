package per.funown.bocast.modules.home.model;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

import android.app.Application;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssItem;
import per.funown.bocast.library.utils.LiveDataUtils;
import per.funown.bocast.library.utils.RssFetchUtils;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class EpisodeViewModel extends AndroidViewModel implements LifecycleObserver {

  private static final String TAG = EpisodeViewModel.class.getSimpleName();
  private String mediaId;
  private MutableLiveData<RssFeed> currentPodcast = new MutableLiveData<>();
  private List<MediaItem> itemLiveData;
  private EpisodesRepository repository;


  public EpisodeViewModel(@NonNull Application application) {
    super(application);
    repository = new EpisodesRepository(application.getApplicationContext());
  }

  public List<MediaItem> getItemLiveData() {
    return itemLiveData;
  }

  public void setItemLiveData(
      List<MediaItem> itemLiveData) {
    this.itemLiveData = itemLiveData;
  }

  public void setCurrentPodcast(RssFeed feed) {
    this.currentPodcast.postValue(feed);
  }

  public void setCurrentPodcast(String rss) {
    LiveDataUtils.setValue(currentPodcast, RssFetchUtils.getFeed(rss));
  }

  public LiveData<RssFeed> getCurrentPodcast() {
    return currentPodcast;
  }

  public String getMediaId() {
    return mediaId;
  }

  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
  }

  public RssItem getCurrentEpisode(String url, String guid) {
    return repository.getCurrentEpisode(url, guid);
  }
}
