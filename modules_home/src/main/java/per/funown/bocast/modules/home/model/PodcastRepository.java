package per.funown.bocast.modules.home.model;

import androidx.lifecycle.MutableLiveData;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;

import java.util.Map;
import java.util.Set;
import per.funown.bocast.library.net.NetworkState;
import per.funown.bocast.library.entity.Genre;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.dataSource.TopPodcastSourceFactory;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastRepository {

  private static final String TAG = PodcastRepository.class.getSimpleName();
  private TopPodcastSourceFactory factory;

  private MutableLiveData<Map<Genre, MutableLiveData<List<ItunesResponseEntity>>>> topPodcasts;
  public LiveData<NetworkState> refreshState;//初始化加载状态
  private List<Genre> genreList;

  public NetworkState getNetworkState() {
    return factory.getNetworkState();
  }

  public LiveData<Map<Genre, MutableLiveData<List<ItunesResponseEntity>>>> getTopPodcasts() {
    return topPodcasts;
  }

  public void setGenreList(List<Genre> genreList) {
    this.genreList = genreList;
    factory.setGenreList(genreList);
  }

  public PodcastRepository(List<Genre> genreList) {
    factory = new TopPodcastSourceFactory();
    factory.setGenreList(genreList);
    this.genreList = genreList;
    refreshState = factory.getInitState();
    topPodcasts = new MutableLiveData<>(
        new HashMap<>(genreList == null ? 10 : genreList.size()));
  }

  public void fetchData(int currentPage, int pageSize) {
    Map<Genre, MutableLiveData<List<ItunesResponseEntity>>> podcasts = factory
        .getTopPodcasts(currentPage, pageSize);
    Map<Genre, MutableLiveData<List<ItunesResponseEntity>>> map = topPodcasts.getValue();
    Set<Genre> genres = podcasts.keySet();
    for (Genre genre : genres) {
      if (genre.getWeight() == 0) continue;
      map.put(genre, podcasts.get(genre));
    }
    topPodcasts.setValue(map);
  }
}
