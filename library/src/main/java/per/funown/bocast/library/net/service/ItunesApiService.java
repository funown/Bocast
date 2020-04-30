package per.funown.bocast.library.net.service;

import per.funown.bocast.library.model.ItunesSearchResultList;
import per.funown.bocast.library.model.iTunesLookupResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/28
 *     desc   : api service
 *     version: 1.0
 *
 * </pre>
 */
public interface ItunesApiService {

  /**
   * eg. https://itunes.apple.com/search?term=jack+johnson
   * @param terms
   * @return
   */
  @GET("search?entity=podcast")
  Call<ItunesSearchResultList> SearchPodcast(@Query("term") String terms);

  @GET("lookup")
  Call<iTunesLookupResult> lookupPodcast(@Query("id") String id);
}
