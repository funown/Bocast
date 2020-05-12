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
   *
   * @param terms
   * @return
   */
  @GET("search?entity=podcast")
  Call<ItunesSearchResultList> SearchPodcast(@Query("term") String terms);


  /**
   * eg. https://itunes.apple.com/search?term=podcast&genreId=1324&offset=200&limit=200
   *
   * @param genreId
   * @param limit
   * @return
   */
  @GET("search?term=podcast&country=cn")
  Call<ItunesSearchResultList> SearchPodcastByGenre(@Query("genreId") String genreId,
      @Query("offset") int offset,
      @Query("limit") int limit);

  /**
   * eg. https://itunes.apple.com/lookup?id=1053786200
   * @param id
   * @return
   */
  @GET("lookup")
  Call<ItunesSearchResultList> lookupPodcast(@Query("id") String id);
}
