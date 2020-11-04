package per.funown.bocast.library.dataSource;

import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.model.ItunesSearchResultList;
import per.funown.bocast.library.net.NetManager;
import per.funown.bocast.library.net.NetworkState;
import per.funown.bocast.library.net.service.ItunesApiService;
import retrofit2.Call;
import retrofit2.Response;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GenrePodcastDataSource extends PageKeyedDataSource<Integer, ItunesResponseEntity> {

  private static final String TAG = GenrePodcastDataSource.class.getSimpleName();
  private int offset;
  private String genreId;
  private static int LIMIT = 30;

  private MutableLiveData<NetworkState> _networkState = new MutableLiveData<>();
  private Runnable retry = null;

  public LiveData<NetworkState> get_networkState() {
    return _networkState;
  }

  public Runnable getRetry() {
    return retry;
  }

  public void setGenreId(String genreId) {
    this.genreId = genreId;
  }

  public static void setLIMIT(int limit) {
    if (limit != 0) {
      GenrePodcastDataSource.LIMIT = limit;
    }
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getOffset() {
    return offset;
  }

  @Override
  public void loadInitial(@NonNull LoadInitialParams<Integer> params,
      @NonNull LoadInitialCallback<Integer, ItunesResponseEntity> callback) {
    _networkState.postValue(NetworkState.INITIAL_LOADING);
    retry = null;
    ItunesApiService itunesApiService = NetManager.getInstance().getItunesApiService();
    Call<ItunesSearchResultList> call = itunesApiService
        .SearchPodcastByGenre(genreId, 0, LIMIT);
    ItunesSearchResultList itunesSearchResultList = null;
    try {
      itunesSearchResultList = new PodcastDataSourceAsyncTask().execute(call)
          .get();
      offset = itunesSearchResultList.getResultCount();
      if (itunesSearchResultList == null) {
        retry = () -> loadInitial(params, callback);
      }
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    callback.onResult(itunesSearchResultList.getResults(), null, 2);
  }

  @Override
  public void loadBefore(@NonNull LoadParams<Integer> params,
      @NonNull LoadCallback<Integer, ItunesResponseEntity> callback) {
  }

  @Override
  public void loadAfter(@NonNull LoadParams<Integer> params,
      @NonNull LoadCallback<Integer, ItunesResponseEntity> callback) {
    _networkState.postValue(NetworkState.LOADING);
    retry = null;
    ItunesApiService itunesApiService = NetManager.getInstance().getItunesApiService();
    Call<ItunesSearchResultList> call = itunesApiService
        .SearchPodcastByGenre(genreId, offset, LIMIT);
    ItunesSearchResultList itunesSearchResultList = null;
    try {
      itunesSearchResultList = new PodcastDataSourceAsyncTask().execute(call)
          .get();
      if (itunesSearchResultList == null) {
        retry = () -> loadAfter(params, callback);
        return;
      }
      if (itunesSearchResultList.getResultCount() == 0) {
        _networkState.postValue(NetworkState.LOADED);
        return;
      }
      offset = offset + itunesSearchResultList.getResultCount();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    callback.onResult(itunesSearchResultList.getResults(), params.key + 1);
  }

  class PodcastDataSourceAsyncTask extends AsyncTask<Call<ItunesSearchResultList>, Void, ItunesSearchResultList> {

    @Override
    protected ItunesSearchResultList doInBackground(Call<ItunesSearchResultList>... calls) {
      Response<ItunesSearchResultList> execute = null;
      try {
        execute = calls[0].execute();
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (execute.isSuccessful()) {
        return execute.body();
      }
      else {
        _networkState.postValue(NetworkState.FAILED);
        Log.e(TAG, "loading error: " + execute.message());
      }
      return null;
    }

    @Override
    protected void onPostExecute(ItunesSearchResultList itunesSearchResultList) {
      super.onPostExecute(itunesSearchResultList);
    }
  }
}
