package per.funown.bocast.library.net.service;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface iTunesRssTopPodcastService {
  @GET("json")
  Call<JsonObject> getTopPodcastsInGenre();
}
