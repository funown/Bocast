package per.funown.bocast.library.playback;

import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioFocusRequest.Builder;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.media.AudioManager;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;

import android.support.v4.media.session.PlaybackStateCompat;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;

import okhttp3.OkHttpClient;

import per.funown.bocast.library.model.EpisodeSource;
import per.funown.bocast.library.model.PodcastEpisodeProvider;
import per.funown.bocast.library.service.PlayService;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LocalPlayback implements Playback {

  private static final String TAG = LocalPlayback.class.getSimpleName();

  private Context context;
  private Callback callback;
  private PodcastEpisodeProvider provider;

  private volatile String currentMediaId;
  private SimpleExoPlayer exoPlayer;
  private boolean exoPlayerNullStoped;
  private final ExoPlayerEventListener eventListener = new ExoPlayerEventListener();

  // Wifi
  private final WifiManager.WifiLock wifiLock;

  /**
   * Headset setting field
   */
  private volatile boolean audioNoisyReceiverRegistered;
  private final IntentFilter audioNoisyIntentFilter = new IntentFilter(
      AudioManager.ACTION_AUDIO_BECOMING_NOISY);
  private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
        Log.d(TAG, "Headphones disconnected.");
        //When player is playing, notices service to pause player (solve the command in Service.onStartCommand )
        if (isPlaying()) {
          Intent i = new Intent(context, PlayService.class);
          i.setAction(PlayService.ACTION_CMD);
          i.putExtra(PlayService.CMD_NAME, PlayService.CMD_PAUSE);
          context.startService(i);
        }
      }
    }
  };

  /**
   * Audio Focus Setting field
   */
  // The volume we set the media player to when we lose audio focus, but are
  // allowed to reduce the volume instead of stopping playback.
  public static final float VOLUME_DUCK = 0.2f;
  // The volume we set the media player when we have audio focus.
  public static final float VOLUME_NORMAL = 1.0f;

  // we don't have audio focus, and can't duck (play at a low volume)
  private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
  // we don't have focus, but can duck (play at a low volume)
  private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
  // we have full audio focus
  private static final int AUDIO_FOCUSED = 2;

  private volatile boolean playOnFocusGain;
  private volatile int currentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;

  private final AudioManager audioManager;
  private final AudioFocusRequest audioFocusRequest;
  private final AudioAttributes audioAttributes;
  private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = (focusChange) -> {
    switch (focusChange) {
      case AudioManager.AUDIOFOCUS_GAIN:
        currentAudioFocusState = AUDIO_FOCUSED;
        break;
      case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
        currentAudioFocusState = AUDIO_NO_FOCUS_CAN_DUCK;
        break;
      case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
        currentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
        playOnFocusGain = exoPlayer != null && exoPlayer.getPlayWhenReady();
        break;
      case AudioManager.AUDIOFOCUS_LOSS:
        currentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
        break;
    }
    if (exoPlayer != null) {
      // update player state
      configurePlayState();
    }
  };

  public LocalPlayback(Context context, PodcastEpisodeProvider provider) {
    this.context = context;
    this.provider = provider;

    Context applicationContext = context.getApplicationContext();
    wifiLock = ((WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE))
        .createWifiLock(WifiManager.WIFI_MODE_FULL_LOW_LATENCY, "podcast_lock");

    this.audioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
    audioAttributes = new AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
        .build();
    audioFocusRequest = new Builder(AudioManager.AUDIOFOCUS_GAIN)
        .setOnAudioFocusChangeListener(audioFocusChangeListener)
        .setWillPauseWhenDucked(true)
        .setAudioAttributes(audioAttributes)
        .build();
  }

  @Override
  public void start() {

  }

  @Override
  public void stop(boolean notifyListeners) {
    cancelAudioFocus();
    unregisterAudioNoisyReceiver();
    releaseResource(true);
  }

  @Override
  public void setState(int state) {
    //exoPlayer holds its own state
  }

  @Override
  public int getState() {
    if (exoPlayer == null) {
      return exoPlayerNullStoped
          ? PlaybackStateCompat.STATE_STOPPED
          : PlaybackStateCompat.STATE_NONE;
    }
    switch (exoPlayer.getPlaybackState()) {
      case ExoPlayer.STATE_IDLE:
      case ExoPlayer.STATE_ENDED:
        return PlaybackStateCompat.STATE_PAUSED;

      case ExoPlayer.STATE_BUFFERING:
        return PlaybackStateCompat.STATE_BUFFERING;

      case ExoPlayer.STATE_READY:
        return exoPlayer.getPlayWhenReady()
            ? PlaybackStateCompat.STATE_PLAYING
            : PlaybackStateCompat.STATE_PAUSED;

      default:
        return PlaybackStateCompat.STATE_NONE;
    }
  }

  @Override
  public boolean isConnected() {
    return true;
  }

  @Override
  public boolean isPlaying() {
    return playOnFocusGain || (exoPlayer != null && exoPlayer.getPlayWhenReady());
  }

  @Override
  public long getCurrentStreamPosition() {
    return exoPlayer != null ? exoPlayer.getCurrentPosition() : 0;
  }

  @Override
  public void updateLastKnownStreamPosition() {
      //Position maintained by ExoPlayer.
  }

  @Override
  public void play(QueueItem item) {
    playOnFocusGain = true;
    tryToGetAudioFocus();

    if (exoPlayer == null) {
      exoPlayer = new SimpleExoPlayer.Builder(context)
          .setTrackSelector(new DefaultTrackSelector(context))
          .setLoadControl(new DefaultLoadControl())
          .build();
      exoPlayer.addListener(eventListener);
    }
    MediaMetadataCompat episode = provider
        .getEpisodeSource(item.getDescription().getMediaId());
    String source = episode.getString(EpisodeSource.CUSTOM_METADATA_TRACK_SOURCE);
    if (source != null) {
      source = source.replaceAll(" ", "%20"); // Escape spaces for URLs
    }
    DefaultDataSourceFactory dataSource = new DefaultDataSourceFactory(context,
        new OkHttpDataSourceFactory(new OkHttpClient(),
            Util.getUserAgent(context, "Bocast")));
    ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSource)
        .createMediaSource(Uri.parse(source));
    exoPlayer.prepare(mediaSource);

    // prevent wifi radio from sleep while player is playing
    wifiLock.acquire();

    configurePlayState();
  }

  /**
   * Start or restart the player according to the setting of audio focus. if we have focus, then
   * play the player normally, else if we don't have focus, then pause player or set it to a low
   * volume according to the current focus settings
   */
  private void configurePlayState() {
    if (currentAudioFocusState == AUDIO_NO_FOCUS_NO_DUCK) {
      pause();
    } else {
      registerAudioNoisyReceiver();

      if (currentAudioFocusState == AUDIO_NO_FOCUS_CAN_DUCK) {
        exoPlayer.setVolume(VOLUME_DUCK);
      } else {
        exoPlayer.setVolume(VOLUME_NORMAL);
      }
      // If we were playing when we lost focus, we need to resume playing.
      if (playOnFocusGain) {
        exoPlayer.setPlayWhenReady(true);
        playOnFocusGain = false;
      }
    }
  }

  @Override
  public void pause() {
    if (exoPlayer != null) {
      exoPlayer.setPlayWhenReady(false);
    }
    releaseResource(false);
    unregisterAudioNoisyReceiver();
  }

  /**
   * release the resource and if releasePlayer equals true, release the exoplayer instance
   *
   * @param releasePlayer
   */
  private void releaseResource(boolean releasePlayer) {
    if (releasePlayer && exoPlayer != null) {
      exoPlayer.removeListener(eventListener);
      exoPlayer = null;
      playOnFocusGain = false;
      exoPlayerNullStoped = true;
      exoPlayer.release();
    }

    if (wifiLock.isHeld()) {
      wifiLock.release();
    }
  }

  @Override
  public void seekTo(long position) {
    if (exoPlayer != null) {
      registerAudioNoisyReceiver();
      exoPlayer.seekTo(position);
    }
  }

  @Override
  public void setCurrentMediaId(String mediaId) {
    this.currentMediaId = mediaId;
  }

  @Override
  public String getCurrentMediaId() {
    return currentMediaId;
  }

  @Override
  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  public void setProvider(PodcastEpisodeProvider provider) {
    this.provider = provider;
  }

  private void registerAudioNoisyReceiver() {
    if (!audioNoisyReceiverRegistered) {
      context.registerReceiver(broadcastReceiver, audioNoisyIntentFilter);
      audioNoisyReceiverRegistered = true;
    }
  }

  private void unregisterAudioNoisyReceiver() {
    if (audioNoisyReceiverRegistered) {
      context.unregisterReceiver(broadcastReceiver);
      audioNoisyReceiverRegistered = false;
    }
  }

  private void tryToGetAudioFocus() {
    int result = audioManager.requestAudioFocus(audioFocusRequest);
    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
      currentAudioFocusState = AUDIO_FOCUSED;
    } else {
      currentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
    }
  }

  private void cancelAudioFocus() {
    if (audioManager.abandonAudioFocusRequest(audioFocusRequest)
        == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
      currentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
    }
  }

  private final class ExoPlayerEventListener implements ExoPlayer.EventListener {

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
      switch (playbackState) {
        case ExoPlayer.STATE_IDLE:
        case ExoPlayer.STATE_BUFFERING:
        case ExoPlayer.STATE_READY:
          if (callback != null) {
            callback.onPlayStateChange(getState());
          }
          break;
        case ExoPlayer.STATE_ENDED:
          // The media player finished playing the current song.
          if (callback != null) {
            callback.onComplete();
          }
          break;
      }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
      final String what;
      switch (error.type) {
        case ExoPlaybackException.TYPE_SOURCE:
          what = error.getSourceException().getMessage();
          break;
        case ExoPlaybackException.TYPE_RENDERER:
          what = error.getRendererException().getMessage();
          break;
        case ExoPlaybackException.TYPE_UNEXPECTED:
          what = error.getUnexpectedException().getMessage();
          break;
        default:
          what = "Unknown: " + error;
      }

      Log.e(TAG, "ExoPlayer error: what=" + what);
      if (callback != null) {
        callback.onError("ExoPlayer error " + what);
      }
    }


  }

}
