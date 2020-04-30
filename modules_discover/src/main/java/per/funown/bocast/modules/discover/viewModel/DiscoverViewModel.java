package per.funown.bocast.modules.discover.viewModel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import per.funown.bocast.library.model.ItunesSearchResultList;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.net.NetManager;
import per.funown.bocast.library.net.service.ItunesApiService;
import per.funown.bocast.library.utils.RssFetchUtils;
import retrofit2.Response;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DiscoverViewModel extends AndroidViewModel {

  private static final String TAG = DiscoverViewModel.class.getSimpleName();
  private MutableLiveData<List<ItunesResponseEntity>> itunesResponseEntityList = new MutableLiveData<>();
  private MutableLiveData<RssFeed> rssSearch = new MutableLiveData<>();

  public LiveData<List<ItunesResponseEntity>> getItunesResponseEntityList() {
    return itunesResponseEntityList;
  }

  public LiveData<RssFeed> getRssSearch() {
    return rssSearch;
  }

  public DiscoverViewModel(@NonNull Application application) {
    super(application);
  }

  public void SearchRss(String url) {
    RssFeed feed = null;
    try {
      feed = new FetchRssAsyncTask().execute(url).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (feed != null) {
      rssSearch.setValue(feed);
    }
  }

  public void SearchTerms(String terms) {
    List<ItunesResponseEntity> entities = null;
    try {
      entities = new iTunesSearchAsyncTask().execute(terms).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    itunesResponseEntityList.setValue(entities);
  }

  public void clearSearch() {
    itunesResponseEntityList.setValue(new ArrayList<>());
    rssSearch.setValue(null);
  }

  class FetchRssAsyncTask extends AsyncTask<String, Void, RssFeed> {

    @Override
    protected RssFeed doInBackground(String... strings) {
      RssFeed feed = RssFetchUtils.getFeed(strings[0]);
      return feed;
    }
  }

  class iTunesSearchAsyncTask extends AsyncTask<String, Void, List<ItunesResponseEntity>> {

    @Override
    protected List<ItunesResponseEntity> doInBackground(String... strings) {
      Log.e(TAG, "iTunes Searching...");
      List<ItunesResponseEntity> responseEntityList = new ArrayList<>();
      ItunesApiService itunesApiService = NetManager.getInstance().getItunesApiService();
      try {
        Response<ItunesSearchResultList> execute = itunesApiService
            .SearchPodcast(strings[0].replaceAll(" ", "+")).execute();
        if (execute.isSuccessful() && execute.body() != null) {
          responseEntityList = execute.body().getResults();
          Log.e(TAG, "-->" + execute.body().getResultCount());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

//      itunesApiService.SearchPodcast(strings[0].replaceAll(" ", "+")).enqueue(
//          new Callback<List<ItunesResponseEntity>>() {
//            @Override
//            public void onResponse(Call<List<ItunesResponseEntity>> call,
//                Response<List<ItunesResponseEntity>> response) {
//              if (response.isSuccessful()) {
//                Log.e(TAG, "--> " + response.body());
//                responseEntityList = response.body();
//              }
//            }
//
//            @Override
//            public void onFailure(Call<List<ItunesResponseEntity>> call, Throwable t) {
//              responseEntityList = new ArrayList<>();
//            }
//          });

      return responseEntityList;
    }
  }
}
