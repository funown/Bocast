package per.funown.bocast.modules.home.model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import per.funown.bocast.library.entity.Episode;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssItem;
import per.funown.bocast.library.repo.DownloadedEpisodeRepository;
import per.funown.bocast.library.repo.EpisodeRepository;
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
public class EpisodesViewModel extends AndroidViewModel {

  private static final String TAG = EpisodesViewModel.class.getSimpleName();
  private RssItem currentEpisode;
  private DownloadedEpisodeRepository downloadedEpisodeRepository;
  private EpisodeRepository episodeRepository;

  public EpisodesViewModel(@NonNull Application application) {
    super(application);
    downloadedEpisodeRepository = new DownloadedEpisodeRepository(application);
    episodeRepository = new EpisodeRepository(application);
  }


  public long insertEpisode(Episode episode) {
    return episodeRepository.insertEpisode(episode);
  }

  public Episode getEpisode(String title) {
    return episodeRepository.getEpisodeByTitle(title);
  }

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

  public String isDownloaded(long episodeId) {
    return downloadedEpisodeRepository.isDownloaded(episodeId);
  }
}