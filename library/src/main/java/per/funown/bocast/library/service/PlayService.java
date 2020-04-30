package per.funown.bocast.library.service;

import java.util.List;
import java.util.ArrayList;

import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;
import androidx.media.session.MediaButtonReceiver;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.MediaNotificationManager;

import per.funown.bocast.library.R;
import per.funown.bocast.library.utils.MediaIdHelper;
import per.funown.bocast.library.model.PodcastEpisodeProvider;
import per.funown.bocast.library.playback.QueueManager;
import per.funown.bocast.library.playback.LocalPlayback;
import per.funown.bocast.library.playback.PlaybackManager;
import per.funown.bocast.library.playback.QueueManager.MetadataUpdateListener;

//@Route(path = "/service/play")
public class PlayService extends MediaBrowserServiceCompat implements
    PlaybackManager.PlaybackServiceCallback {

  private static final String TAG = PlayService.class.getSimpleName();

//  private SimpleExoPlayer player;
  private QueueManager queueManager;
  private static Bundle sessionExtras;
  private static MediaSessionCompat session;
  private static PlaybackManager playbackManager;
  private static PlaybackStateCompat playbackState;
  private static PodcastEpisodeProvider provider;
  private MediaNotificationManager mediaNotificationManager;


  // The action of the incoming Intent indicating that it contains a command
  // to be executed (see {@link #onStartCommand})
  public static final String ACTION_CMD = "per.funown.bocast.listener.ACTION_CMD";
  // The key in the extras of the incoming Intent indicating the command that
  // should be executed (see {@link #onStartCommand})
  public static final String CMD_NAME = "CMD_NAME";
  // A value of a CMD_NAME key in the extras of the incoming Intent that
  // indicates that the music playback should be paused (see {@link #onStartCommand})
  public static final String CMD_PAUSE = "CMD_PAUSE";
  // A value of a CMD_NAME key that indicates that the music playback should switch
  // to local playback from cast playback.
  public static final String CMD_STOP_CASTING = "CMD_STOP_CASTING";

  private ExoPlayer.EventListener onPrepareListener = new EventListener() {
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
      if (playWhenReady == true) {
        PlayService.playbackState = new PlaybackStateCompat.Builder()
            .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
            .build();
        session.setPlaybackState(PlayService.playbackState);
      } else {
        PlayService.playbackState = new PlaybackStateCompat.Builder()
            .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
            .build();
        session.setPlaybackState(PlayService.playbackState);
      }
      session.setPlaybackState(PlayService.playbackState);
    }

  };


  @Override
  public void onCreate() {
    super.onCreate();
    provider = new PodcastEpisodeProvider(null);
    provider.retrieveMediaAsync(null);

    queueManager = new QueueManager(getResources(), provider,
        new MetadataUpdateListener() {
          @Override
          public void onMetadataChanged(MediaMetadataCompat metadata) {
            session.setMetadata(metadata);
          }

          @Override
          public void onMetadataRetrieveError() {
            playbackManager.updatePlayStateChange(getString(R.string.error_no_metadata));
          }

          @Override
          public void onCurrentQueueIndexUpdated(int queueIndex) {
            playbackManager.handlePlayRequest();
          }

          @Override
          public void onQueueUpdated(String title, List<QueueItem> newQueue) {
            session.setQueue(newQueue);
            session.setQueueTitle(title);
          }
        });

    LocalPlayback playback = new LocalPlayback(this, provider);
    playbackManager = new PlaybackManager(this, getResources(), provider, queueManager, playback);

    playbackState = new PlaybackStateCompat.Builder()
        .setState(PlaybackStateCompat.STATE_NONE, 0, 1.0f)
        .build();

    session = new MediaSessionCompat(this, TAG);
    session.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
    session.setPlaybackState(playbackState);
    sessionExtras = new Bundle();
    session.setExtras(sessionExtras);
    setSessionToken(session.getSessionToken());

    playbackManager.updatePlayStateChange(null);
  }

  @Nullable
  @Override
  public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid,
      @Nullable Bundle rootHints) {
    Log.e(TAG, "onGetRoot");
    return new BrowserRoot("_ROOT_", null);
  }

  @Override
  public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaItem>> result) {
    Log.e(TAG, "onLoadChildren");
    // app doesn't have the authority to fetch data, so return a empty list
    if (MediaIdHelper.MEDIA_ID_EMPTY_ROOT.equals(parentId)) {
      result.sendResult(new ArrayList<>());
    }
    // initialize the PodcastEpisodeProvider and return it
    else if (provider.isInitialized()) {
      result.sendResult(provider.getChildren(parentId, getResources()));
    }
    // finishing data retrieving
    else {
      result.detach();
      provider.retrieveMediaAsync(new PodcastEpisodeProvider.Callback() {
        @Override
        public void onEpisodesReady(boolean success) {
          result.sendResult(provider.getChildren(parentId, getResources()));
        }
      });
    }
  }

  @Override
  public void onPlaybackStart() {
    session.setActive(true);
    startService(new Intent(getApplicationContext(), PlayService.class));
  }

  @Override
  public void onNotificationRequired() {

  }

  @Override
  public void onPlaybackStop() {
    session.setActive(false);
    stopForeground(true);
  }

  @Override
  public void onPlaybackStateUpdated(PlaybackStateCompat newState) {
    session.setPlaybackState(newState);
  }

  /**
   * (non-Javadoc)
   *
   * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
   */
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent != null) {
      String action = intent.getAction();
      String cmd = intent.getStringExtra(CMD_NAME);

      if (ACTION_CMD.equals(action)) {
        if (CMD_PAUSE.equals(cmd)) {
          playbackManager.handlePlayRequest();
        } else if (CMD_STOP_CASTING.equals(cmd)) {
          CastContext.getSharedInstance(this).getSessionManager().endCurrentSession(true);
        }
      } else {
        MediaButtonReceiver.handleIntent(session, intent);
      }
    }
    return super.onStartCommand(intent, flags, startId);
  }
}
