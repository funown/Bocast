package per.funown.bocast.library.playback;

import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.res.Resources;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;

import per.funown.bocast.library.model.PodcastEpisodeProvider;
import per.funown.bocast.library.utils.MediaIdHelper;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class QueueManager {

  private Resources resources;
  private PodcastEpisodeProvider provider;
  private MetadataUpdateListener metadataUpdateListener;

  private List<MediaSessionCompat.QueueItem> playQueue;
  private int currentIndex;

  public QueueManager(@NonNull Resources resources,
      @NonNull PodcastEpisodeProvider provider,
      @NonNull MetadataUpdateListener metadataUpdateListener) {
    this.resources = resources;
    this.provider = provider;
    this.metadataUpdateListener = metadataUpdateListener;

    playQueue = Collections.synchronizedList(new ArrayList<>());
    currentIndex = 0;
  }

  public List<QueueItem> getPlayQueue() {
    return playQueue;
  }

  public void setPlayQueue(String podcastTitle, List<QueueItem> playQueue, String initialMediaId ) {
    this.playQueue = playQueue;
    int index = 0;
    if (initialMediaId != null) {
      for (MediaSessionCompat.QueueItem item : playQueue) {
        if (initialMediaId.equals(item.getDescription().getMediaId())) {
          currentIndex = Math.max(index, 0);
        }
        index++;
      }
    }
    metadataUpdateListener.onQueueUpdated(podcastTitle, playQueue);
  }

  public int getCurrentIndex() {
    return currentIndex;
  }

  public void setCurrentIndex(int currentIndex) {
    if (currentIndex >= 0 && currentIndex < playQueue.size()) {
      this.currentIndex = currentIndex;
      metadataUpdateListener.onCurrentQueueIndexUpdated(currentIndex);
    }
  }

  public QueueItem getCurrentMusic() {
    if (playQueue != null && currentIndex >= 0 && currentIndex < playQueue.size()) {
      return playQueue.get(currentIndex);
    }
    return null;
  }

  public boolean setCurrentQueueItem(String mediaId) {
    int index = 0;
    for (MediaSessionCompat.QueueItem item : playQueue) {
      if (mediaId.equals(item.getDescription().getMediaId())) {
        break;
      }
      index++;
    }
    setCurrentIndex(index);
    return index <= playQueue.size();
  }

  public boolean setCurrentQueueItem(long queueId) {
    int index = 0;
    for (MediaSessionCompat.QueueItem item : playQueue) {
      if (queueId == item.getQueueId()) {
        break;
      }
      index++;
    }
    setCurrentIndex(index);
    return index <= playQueue.size();
  }

  // TODO
  public boolean setQueueFromSearch(String query, Bundle extras) {
    return true;
  }

  public void updateMetadata() {
    QueueItem currentMusic = getCurrentMusic();
    if (currentMusic == null) {
      metadataUpdateListener.onMetadataRetrieveError();
      return;
    }
    String musicId = MediaIdHelper.extractMusicIDFromMediaID(currentMusic.getDescription().getMediaId());
    MediaMetadataCompat episodeSource = provider.getEpisodeSource(musicId);
    if (episodeSource == null) {
      throw new IllegalArgumentException("Invaild MusicId:" + musicId);
    }

    metadataUpdateListener.onMetadataChanged(episodeSource);
  }

  public interface MetadataUpdateListener {

    /**
     * call when Metadata change
     *
     * @param metadata
     */
    void onMetadataChanged(MediaMetadataCompat metadata);

    /**
     * invoke when metadata search fail
     */
    void onMetadataRetrieveError();

    /**
     * invoke when current play index change
     *
     * @param queueIndex
     */
    void onCurrentQueueIndexUpdated(int queueIndex);

    /**
     * invoke when playlist change
     *
     * @param title
     * @param newQueue
     */
    void onQueueUpdated(String title, List<QueueItem> newQueue);

  }
}
