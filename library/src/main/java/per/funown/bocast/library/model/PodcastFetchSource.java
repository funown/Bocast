package per.funown.bocast.library.model;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.MediaMetadataCompat.Builder;

import per.funown.bocast.library.net.NetManager;
import per.funown.bocast.library.utils.RssFetchUtils;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastFetchSource implements EpisodeSource {

  private NetManager netManager;
  private String rssUrl;

  public PodcastFetchSource(String rssUrl) {
    netManager = NetManager.getInstance();
    this.rssUrl = rssUrl;
  }

  @Override
  public Iterator<MediaMetadataCompat> iterator() {
    ArrayList<MediaMetadataCompat> list = new ArrayList<>();
    RssFeed feed = fetchResult();
    List<RssItem> rssItems = feed.getChannel().getItems();
    Iterator<RssItem> iterator = rssItems.iterator();
    while (iterator.hasNext()) {
      RssItem item = iterator.next();
      MediaMetadataCompat compat = new Builder()
          .putLong(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, (long) item.hashCode())
          .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, item.getTitle())
          .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, item.getSubtitle())
          .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, item.getAuthor())
          .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION,
              item.getTitle())
          .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, item.getEnclosure().getUrl())
          .putString(CUSTOM_METADATA_TRACK_COVER, item.getImage().getHref())
          .putString(CUSTOM_METADATA_TRACK_PUBDATE, item.getPubDate())
          .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, Long.getLong(item.getDuration()))
          .build();
      list.add(compat);
    }

    return list.iterator();
  }

  public RssFeed fetchResult() {
    return RssFetchUtils.getFeed(this.rssUrl);
  }
}
