package per.funown.bocast.library.playback;

import android.util.Log;
import android.os.Bundle;
import android.os.SystemClock;
import android.content.res.Resources;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.media.session.PlaybackStateCompat.Builder;

import per.funown.bocast.library.model.PodcastEpisodeProvider;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PlaybackManager implements Playback.Callback {

  private static final String TAG = PlaybackManager.class.getSimpleName();

  private Resources resources;
  private Playback playback;
  private PodcastEpisodeProvider provider;
  private QueueManager queueManager;
  private MediaSessionCallback mediaSessionCallback;
  private PlaybackServiceCallback playbackServiceCallback;

  public PlaybackManager(PlaybackServiceCallback playbackServiceCallback, Resources resources,
      PodcastEpisodeProvider provider, QueueManager queueManager, Playback playback) {
    this.playback = playback;
    this.provider = provider;
    this.resources = resources;
    this.queueManager = queueManager;
    this.playbackServiceCallback = playbackServiceCallback;

    mediaSessionCallback = new MediaSessionCallback();
  }

  public MediaSessionCallback getMediaSessionCallback() {
    return mediaSessionCallback;
  }

  public PlaybackServiceCallback getPlaybackServiceCallback() {
    return playbackServiceCallback;
  }

  @Override
  public void onComplete() {
    int currentIndex = queueManager.getCurrentIndex();
    if (currentIndex == queueManager.getPlayQueue().size() - 1) {
      playback.stop(false);
      handleStopRequest(null);
      queueManager.updateMetadata();
      provider.setFinished(queueManager.getCurrentMusic().getDescription().getMediaId());
    }
  }

  private void handleStopRequest(String withError) {
    playback.stop(true);
    playbackServiceCallback.onPlaybackStop();
    updatePlayStateChange(withError);
  }

  @Override
  public void onPlayStateChange(int state) {

  }

  public void updatePlayStateChange(String error) {
    long position = PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN;
    if (playback != null && playback.isConnected()) {
      position = playback.getCurrentStreamPosition();
    }

    Builder stateBuilder = new Builder().setActions(getAvailableActions());
    setCustomAction(stateBuilder);
    int state = playback.getState();

    if (error != null) {
      stateBuilder.setErrorMessage(PlaybackStateCompat.ERROR_CODE_UNKNOWN_ERROR, error);
      state = PlaybackStateCompat.STATE_ERROR;
    }
    stateBuilder.setState(state, position, 1.0f, SystemClock.elapsedRealtime());

    // Set the active QueueItemId if the current index is valid.
    MediaSessionCompat.QueueItem currentMusic = queueManager.getCurrentMusic();
    if (currentMusic != null) {
      stateBuilder.setActiveQueueItemId(currentMusic.getQueueId());
    }

    playbackServiceCallback.onPlaybackStateUpdated(stateBuilder.build());

    if (state == PlaybackStateCompat.STATE_PLAYING ||
        state == PlaybackStateCompat.STATE_PAUSED) {
      playbackServiceCallback.onNotificationRequired();
    }
  }

  private long getAvailableActions() {
    long actions =
        PlaybackStateCompat.ACTION_PLAY_PAUSE |
            PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
            PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH |
            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
            PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
    if (playback.isPlaying()) {
      actions |= PlaybackStateCompat.ACTION_PAUSE;
    } else {
      actions |= PlaybackStateCompat.ACTION_PLAY;
    }
    return actions;
  }

  private void setCustomAction(Builder stateBuilder) {
    MediaSessionCompat.QueueItem currentMusic = queueManager.getCurrentMusic();
    if (currentMusic == null) {
      return;
    }
    // Set appropriate "Favorite" icon on Custom action:
    String mediaId = currentMusic.getDescription().getMediaId();
    if (mediaId == null) {
      return;
    }
    // TODO
  }

  @Override
  public void onError(String err) {

  }

  @Override
  public void setCurrentMediaId(String MediaId) {

  }

  /**
   * Handle a request to play music
   */
  public void handlePlayRequest() {
    MediaSessionCompat.QueueItem currentMusic = queueManager.getCurrentMusic();
    if (currentMusic != null) {
      playbackServiceCallback.onPlaybackStart();
      playback.play(currentMusic);
    }
  }

  /**
   * Handle a request to pause music
   */
  public void handlePauseRequest() {
    Log.d(TAG, "handlePauseRequest: mState=" + playback.getState());
    if (playback.isPlaying()) {
      playback.pause();
      playbackServiceCallback.onPlaybackStop();
    }
  }

  private class MediaSessionCallback extends MediaSessionCompat.Callback {

    @Override
    public void onPlay() {
      super.onPlay();
      handlePlayRequest();
    }

    @Override
    public void onPause() {
      handlePauseRequest();
    }

    @Override
    public void onSeekTo(long pos) {
      Log.d(TAG, String.format("onSeekTo: %s", pos));
      playback.seekTo(pos);
    }

    @Override
    public void onPlayFromMediaId(String mediaId, Bundle extras) {
      queueManager.setCurrentQueueItem(mediaId);
      handlePlayRequest();
    }

    @Override
    public void onStop() {
      handleStopRequest(null);
    }

    @Override
    public void onSkipToNext() {
      if (queueManager.getCurrentIndex() + 1 >= queueManager.getPlayQueue().size() ) {
        handleStopRequest("No next episode");
      }
      else {
        queueManager.setCurrentIndex(queueManager.getCurrentIndex() + 1);
        handlePlayRequest();
      }
    }

    @Override
    public void onSkipToPrevious() {
      int currentIndex = queueManager.getCurrentIndex();
      if (currentIndex == 0) {
        handleStopRequest("No previous episode");
      }
      else {
        queueManager.setCurrentIndex(currentIndex - 1);
        handlePlayRequest();
      }
    }

    @Override
    public void onSkipToQueueItem(long id) {
      queueManager.setCurrentQueueItem(id);
      handlePlayRequest();
    }

    @Override
    public void onPlayFromSearch(String query, Bundle extras) {
      playback.setState(PlaybackStateCompat.STATE_CONNECTING);
      provider.retrieveMediaAsync(result -> {
        if (!result) {
          updatePlayStateChange("Can not load episode");
        }
        boolean search = queueManager.setQueueFromSearch(query, extras);
        if (search) {
          handlePlayRequest();
          queueManager.updateMetadata();
        }
        else {
          updatePlayStateChange("Can not find the episode");
        }
      });
    }
  }

  public interface PlaybackServiceCallback {

    void onPlaybackStart();

    void onNotificationRequired();

    void onPlaybackStop();

    void onPlaybackStateUpdated(PlaybackStateCompat newState);
  }
}
