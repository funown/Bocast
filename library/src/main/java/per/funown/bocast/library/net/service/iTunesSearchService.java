package per.funown.bocast.library.net.service;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.model.ItunesSearchResultList;
import per.funown.bocast.library.net.NetManager;
import retrofit2.Response;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class iTunesSearchService {

  private static final String TAG = iTunesSearchService.class.getSimpleName();

  public static List<ItunesResponseEntity> SearchTerms(String terms) {
    List<ItunesResponseEntity> entities = null;
    try {
      entities = new iTunesSearchAsyncTask().execute(terms).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return entities;
  }

  static class iTunesSearchAsyncTask extends AsyncTask<String, Void, List<ItunesResponseEntity>> {

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

      return responseEntityList;
    }
  }

}
