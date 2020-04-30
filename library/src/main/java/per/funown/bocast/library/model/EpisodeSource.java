package per.funown.bocast.library.model;

import java.util.Iterator;
import android.support.v4.media.MediaMetadataCompat;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface EpisodeSource {
  String CUSTOM_METADATA_TRACK_SOURCE = "podcast.episode.media.metadata.source";
  String CUSTOM_METADATA_TRACK_COVER = "podcast.episode.media.metadata.cover";
  String CUSTOM_METADATA_TRACK_PUBDATE = "podcast.episode.media.metadata.pubdate";
  Iterator<MediaMetadataCompat> iterator();
}
