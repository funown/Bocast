package per.funown.bocast.library.model;

import android.annotation.SuppressLint;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;
import android.os.AsyncTask;
import android.content.res.Resources;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.MediaDescriptionCompat;

import per.funown.bocast.library.utils.MediaIdHelper;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */
// TODO
public class PodcastEpisodeProvider {

  private static final String TAG = PodcastEpisodeProvider.class.getSimpleName();

  private EpisodeSource source;
  private Map<String, MutableMediaMetadata> mediaListById;
  private Set<String> finishedTracks;

  enum State {
    NOT_INITIALIZED, INITIALIZED, INITIALIZING
  }

  private volatile State providerCurrentState = State.NOT_INITIALIZED;

  public PodcastEpisodeProvider(EpisodeSource source) {
    this.source = source;
    mediaListById = new ConcurrentHashMap<>();
    finishedTracks = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
  }

  public boolean isInitialized() {
    return providerCurrentState == State.INITIALIZED;
  }

  public boolean isFinished(String mediaId) {
    return finishedTracks.contains(mediaId);
  }

  public boolean setFinished(String mediaId) {
    finishedTracks.add(mediaId);
    return isFinished(mediaId);
  }

  public List<MediaItem> getChildren(String parentId, Resources resources) {
    List<MediaItem> mediaItems = new ArrayList<>();
    if (MediaIdHelper.MEDIA_ID_ROOT.equals(parentId)) {
      mediaItems.add(createBrowsableMediaItemForRoot(resources));
    } else if (MediaIdHelper.MEDIA_ID_MUSICS_BY_ID.equals(parentId)) {
      Collection<MutableMediaMetadata> values = mediaListById.values();
      for (MutableMediaMetadata data : values) {
        mediaItems.add(new MediaItem(data.metadata.getDescription(), MediaItem.FLAG_PLAYABLE));
      }
    }
    return mediaItems;
  }

  private MediaBrowserCompat.MediaItem createBrowsableMediaItemForRoot(Resources resources) {
    MediaDescriptionCompat description = new MediaDescriptionCompat.Builder()
        .setMediaId(MediaIdHelper.MEDIA_ID_MUSICS_BY_ID)
        .build();

    return new MediaBrowserCompat.MediaItem(description,
        MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
  }

  /**
   * When provider{@link PodcastEpisodeProvider} is not initialized, retrieve data from data server
   *
   * @param callback {@link PodcastEpisodeProvider.Callback}
   */
  @SuppressLint("StaticFieldLeak")
  public void retrieveMediaAsync(Callback callback) {
    Log.e(TAG, "RetrieveMediaAsync call");
    if (providerCurrentState == State.NOT_INITIALIZED) {
      if (callback != null) {
        callback.onEpisodesReady(true);
      }
      return;
    }

    // load the episodes
    new AsyncTask<Void, Void, State>() {
      @Override
      protected State doInBackground(Void... voids) {
        fetchMedia();
        return providerCurrentState;
      }

      @Override
      protected void onPostExecute(State state) {
        if (callback != null) {
          callback.onEpisodesReady(state == State.INITIALIZED);
        }
      }
    }.execute();
  }

  private synchronized void fetchMedia() {
    if (providerCurrentState == State.NOT_INITIALIZED) {
      providerCurrentState = State.INITIALIZING;

      Iterator<MediaMetadataCompat> iterator = source.iterator();
      if (iterator != null) {
        while (iterator.hasNext()) {
          MediaMetadataCompat item = iterator.next();
          String mediaId = item.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
          mediaListById.put(mediaId, new MutableMediaMetadata(mediaId, item));
        }
        providerCurrentState = State.INITIALIZED;
      }
    }

    // fetch fail
    if (providerCurrentState != State.INITIALIZED) {
      providerCurrentState = State.NOT_INITIALIZED;
    }
  }


  public interface Callback {

    void onEpisodesReady(boolean result);
  }

  /**
   * obtain episode
   *
   * @param episodeId
   * @return
   */
  public MediaMetadataCompat getEpisodeSource(String episodeId) {
    return mediaListById.containsKey(episodeId) ? mediaListById.get(episodeId).metadata : null;
  }
}
