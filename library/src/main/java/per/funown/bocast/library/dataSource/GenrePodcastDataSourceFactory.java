package per.funown.bocast.library.dataSource;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
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
public class GenrePodcastDataSourceFactory extends DataSource.Factory<Integer, ItunesResponseEntity> {

  private static final String TAG = GenrePodcastDataSourceFactory.class.getSimpleName();
  private MutableLiveData<GenrePodcastDataSource> genrePodcastDataSource = new MutableLiveData<>();
  String genreId;
  int limit;

  public LiveData<GenrePodcastDataSource> getGenrePodcastDataSource() {
    return genrePodcastDataSource;
  }

  public GenrePodcastDataSourceFactory(String genreId, @Nullable int limit){
    super();
    this.genreId = genreId;
    this.limit = limit;
  }


//  public void setGenreId(String genreId) {
//    this.genreId = genreId;
//    getGenrePodcastDataSource().getValue().setGenreId(genreId);
//  }
//
//  public void setLimit(int limit) {
//    this.limit = limit;
//    getGenrePodcastDataSource().getValue().setLIMIT(limit);
//  }

  @NonNull
  @Override
  public DataSource<Integer, ItunesResponseEntity> create() {
    Log.e(TAG, "onCreate...");
    GenrePodcastDataSource dataSource = new GenrePodcastDataSource();
    dataSource.setGenreId(genreId);
    dataSource.setLIMIT(limit);
    genrePodcastDataSource.postValue(dataSource);
    return dataSource;
  }
}
