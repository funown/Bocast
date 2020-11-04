package per.funown.bocast.library.net.service;

import retrofit2.Call;
import retrofit2.http.GET;

import per.funown.bocast.library.model.RssFeed;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface RssService {

  @Headers("Cache-Control: max-age=640000")
  @GET("{endPoint}")
  Call<RssFeed> getFeed(@Path("endPoint") String endPoint);

}
