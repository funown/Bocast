package per.funown.bocast.modules.home.model;

import android.annotation.SuppressLint;
import android.content.Context;

import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssItem;
import per.funown.bocast.library.utils.RssFetchUtils;

import per.funown.bocast.library.net.RssCacheUtil;


/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class EpisodesRepository {

  private static final String TAG = EpisodesRepository.class.getSimpleName();
  private String podcastId;
  private String url;
  //  private MutableLiveData<List<RssItem>> episodes = new MutableLiveData<>();
//  private MutableLiveData<State> state = new MutableLiveData<>(State.NOT_REFRESHED);
  private RssItem currentEpisode;
  private DownloadedEpisodeRepository repository;

  enum State {
    REFRESHING, REFRESHED, NOT_REFRESHED, ERROR
  }

  public EpisodesRepository(Context context) {
    repository = new DownloadedEpisodeRepository(context);
  }
//  public EpisodesRepository(String key) {
//    if (Pattern.matches("^[0-9]+$", key)) {
//      this.podcastId = podcastId;
//    }else {
//      this.url = key;
//    }
//    refresh();
//  }
//
//  public LiveData<List<RssItem>> getEpisodes() {
//    return episodes;
//  }
//
//  public LiveData<State> getState() {
//    return state;
//  }
//
//  public void refresh() {
//    NetManager instance = NetManager.getInstance();
//    ItunesApiService service = instance.getItunesApiService();
//    if (podcastId != null) {
//      Call<iTunesLookupResult> call = service.lookupPodcast(podcastId);
//
//      call.enqueue(new Callback<iTunesLookupResult>() {
//        @Override
//        public void onResponse(Call<iTunesLookupResult> call,
//            Response<iTunesLookupResult> response) {
//          if (response.isSuccessful()) {
//            ItunesResponseEntity responseEntity = response.body().getResults()[0];
//
//            if (responseEntity != null) {
//              if (responseEntity.getTrackCount() == RssCacheUtil.getCacheItuneResponse(podcastId)
//                  .getTrackCount()) {
//                episodes.postValue(
//                    RssCacheUtil.getFeed(responseEntity.getFeedUrl()).getChannel().getItems());
//              } else {
//                setEpisodes(responseEntity);
//                RssCacheUtil.cacheItuneResponse(responseEntity);
//              }
//              state.postValue(State.REFRESHED);
//            } else {
//              ItunesResponseEntity ituneResponse = RssCacheUtil.getCacheItuneResponse(podcastId);
//              state.postValue(State.NOT_REFRESHED);
//            }
//          }
//        }
//
//        @Override
//        public void onFailure(Call<iTunesLookupResult> call, Throwable t) {
//          state.postValue(State.ERROR);
//        }
//      });
//    }
//    else {
//      RssFeed feed = RssFetchUtils.getFeed(url);
//      if (feed != null) {
//        episodes.postValue(feed.getChannel().getItems());
//        state.postValue(State.REFRESHED);
//      }
//      else {
//        episodes.postValue(RssCacheUtil.getFeed(url).getChannel().getItems());
//        state.postValue(State.NOT_REFRESHED);
//      }
//    }
//  }

//  /**
//   * set the episodes
//   *
//   * @param cacheEntity {@link ItunesResponseEntity}
//   */
//  private void setEpisodes(ItunesResponseEntity cacheEntity) {
//    RssFeed feed = RssFetchUtils.getFeed(cacheEntity.getFeedUrl());
//    episodes = new MutableLiveData<>(feed.getChannel().getItems());
//    RssCacheUtil.cacheFeed(cacheEntity.getFeedUrl(), feed);
//  }

  @SuppressLint("CheckResult")
  public RssItem getCurrentEpisode(String Url, String guid) {
    RssFeed rssFeed = RssCacheUtil.getFeed(Url);
    RssFeed feed = rssFeed == null ? RssFetchUtils.fetchRss(Url) : rssFeed;
    if (feed == null && feed.getChannel() == null && feed.getChannel().getItems() == null) {
      return null;
    }
    feed.getChannel().getItems().forEach(item -> {
      if (item.getGuid().getGuid().equals(guid)) {
        currentEpisode = item;
        return;
      }
    });
    return currentEpisode;
  }

  public String isDownloaded(String guid, String url) {
    return repository.isDownloaded(guid, url);
  }
}