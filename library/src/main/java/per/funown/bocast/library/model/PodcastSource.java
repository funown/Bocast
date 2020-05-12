package per.funown.bocast.library.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import per.funown.bocast.library.entity.Genre;
import retrofit2.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import androidx.lifecycle.MutableLiveData;

import per.funown.bocast.library.net.NetManager;
import per.funown.bocast.library.net.NetworkState;
import per.funown.bocast.library.net.service.ItunesApiService;
import per.funown.bocast.library.net.service.iTunesRssTopPodcastService;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastSource extends AsyncTask<Genre, Void, List<ItunesResponseEntity>> {

  private static final String TAG = PodcastSource.class.getSimpleName();
  private volatile static NetworkState networkState = NetworkState.LOADED;
  private MutableLiveData<NetworkState> initState = new MutableLiveData<>();
  private Runnable retry = null;

  private int limit = 12;

  public NetworkState getNetworkState() {
    return networkState;
  }

  public MutableLiveData<NetworkState> getInitState() {
    return initState;
  }

  @SuppressLint("WrongThread")
  @Override
  protected List<ItunesResponseEntity> doInBackground(Genre... genres) {

    Log.d(TAG, "Fetch Top Podcast ...");
    networkState = NetworkState.LOADING;
    List<ItunesResponseEntity> podcastList = new ArrayList<>();
    Genre genre = genres[0];
    try {
      ItunesApiService itunesApiService = NetManager.getInstance().getRetrofit()
          .create(ItunesApiService.class);
      iTunesRssTopPodcastService iTunesRssTopPodcastService = NetManager.getInstance()
          .getITunesRssTopPodcastService(limit, genre.getItunesid());
      Response<JsonObject> execute = iTunesRssTopPodcastService.getTopPodcastsInGenre().execute();
      JsonObject body = execute.body();
      if (execute.isSuccessful() && !body.isJsonNull()) {
        JsonArray entry = body.get("feed").getAsJsonObject().get("entry").getAsJsonArray();

        for (JsonElement jsonElement : entry) {
          JsonObject element = jsonElement.getAsJsonObject();
          String id = element.get("id").getAsJsonObject().get("attributes").getAsJsonObject()
              .get("im:id").getAsString();
          Response<ItunesSearchResultList> response = itunesApiService.lookupPodcast(id).execute();
          ItunesSearchResultList result = response.body();
          if (response.isSuccessful() && result != null && result.getResultCount() > 0) {
            podcastList.add(result.getResults().get(0));
          } else {
            retry = () -> doInBackground(genre);
            NetworkState error = new NetworkState(NetworkState.Status.FAILED,
                String.format("ERROR: %i %s", response.code(), response.message()));
            initState.postValue(error);
            networkState = error;
          }
        }

        if (podcastList.size() == entry.size()) {
          retry = null;
          initState.postValue(NetworkState.LOADED);
          networkState = NetworkState.LOADED;
        }
      } else {
        retry = () -> doInBackground(genre);
        NetworkState error = new NetworkState(NetworkState.Status.FAILED,
            String.format("ERROR: %d %s", execute.code(), execute.message()));
        initState.postValue(error);
        networkState = error;
      }
    } catch (IOException e) {
      e.printStackTrace();
      retry = () -> doInBackground(genre);
      NetworkState error = new NetworkState(NetworkState.Status.FAILED,
          String.format("ERROR: %s", e.getMessage()));
      initState.postValue(error);
      networkState = error;
    }
    return podcastList;
  }

  @Override
  protected void onPostExecute(List<ItunesResponseEntity> itunesResponseEntities) {
    super.onPostExecute(itunesResponseEntities);
  }
}