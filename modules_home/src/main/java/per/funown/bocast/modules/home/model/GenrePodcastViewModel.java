package per.funown.bocast.modules.home.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PagedList.Config;
import per.funown.bocast.library.dataSource.GenrePodcastDataSource;
import per.funown.bocast.library.dataSource.GenrePodcastDataSourceFactory;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.net.NetworkState;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GenrePodcastViewModel extends AndroidViewModel {

  private GenrePodcastDataSourceFactory genrePodcastDataSourceFactory;
  private LiveData<PagedList<ItunesResponseEntity>> podcastList;
  private LiveData<NetworkState> networkStatus;
  public GenrePodcastViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<NetworkState> getNetworkStatus() {
    return networkStatus;
  }

  /**
   * 设置播客列表以及加载状态
   * @param genreId
   */
  public void setPodcastList(
      String genreId) {
    genrePodcastDataSourceFactory = new GenrePodcastDataSourceFactory(
        genreId, 0);
    this.podcastList = new LivePagedListBuilder<>(genrePodcastDataSourceFactory, 50).build();
    networkStatus = Transformations
        .switchMap(genrePodcastDataSourceFactory.getGenrePodcastDataSource(),
            GenrePodcastDataSource::get_networkState);

  }

  public void requestQuery() {
    genrePodcastDataSourceFactory.getGenrePodcastDataSource().getValue().invalidate();
  }

  public void retry() {
    Runnable retry = genrePodcastDataSourceFactory.getGenrePodcastDataSource().getValue()
        .getRetry();
    if (retry != null) {
      retry.run();
    }
  }

  public LiveData<PagedList<ItunesResponseEntity>> getPodcastList() {
    return podcastList;
  }
}
