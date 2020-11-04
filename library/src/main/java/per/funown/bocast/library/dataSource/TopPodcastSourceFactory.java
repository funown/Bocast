package per.funown.bocast.library.dataSource;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import per.funown.bocast.library.entity.Genre;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.model.PodcastSource;
import per.funown.bocast.library.net.NetworkState;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TopPodcastSourceFactory{

  private static final String TAG = TopPodcastSourceFactory.class.getSimpleName();
  private PodcastSource source;
  private List<Genre> genreList;

  public TopPodcastSourceFactory(){
    source = new PodcastSource();
  }

  public void setGenreList(
      List<Genre> genreList) {
    this.genreList = genreList;
  }

  public NetworkState getNetworkState() {
    return source.getNetworkState();
  }

  public LiveData<NetworkState> getInitState() {
    return source.getInitState();
  }

  public Map<Genre, MutableLiveData<List<ItunesResponseEntity>>> getTopPodcasts(int currentPage, int pageSize) {
    Map<Genre, MutableLiveData<List<ItunesResponseEntity>>> podcastMap = new HashMap<>(pageSize);

    int currentIndex = (currentPage - 1) * pageSize;
    for (int i = 0; i < pageSize; i++) {
      int searchIndex = currentIndex + i;
      if (searchIndex >= genreList.size()) break;
      List<ItunesResponseEntity> entities = new ArrayList<>();
      PodcastSource podcastSource = new PodcastSource();
      try {
        entities = podcastSource
            .execute(genreList.get(searchIndex)).get();
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      MutableLiveData<List<ItunesResponseEntity>> mutableLiveData = new MutableLiveData<>();
      mutableLiveData.setValue(entities);
      podcastMap.put(genreList.get(searchIndex), mutableLiveData);
    }
    return podcastMap;
  }
}
