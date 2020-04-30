package per.funown.bocast.library.service;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.lzx.starrysky.StarrySky;
import com.lzx.starrysky.StarrySkyConfig;
import com.lzx.starrysky.control.OnPlayerEventListener;
import com.lzx.starrysky.control.PlayerControl;
import com.lzx.starrysky.provider.SongInfo;
import com.lzx.starrysky.utils.TimerTaskManager;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Route(path = "/service/play")
public class MusicService implements IProvider {

  private static PlayerControl starrySky;
  private String application;
  private Context context;
  private List<SongInfo> list;
  private static TimerTaskManager manager;

  public String getApplication() {
    return application;
  }

  public void addSong(SongInfo info) {
    list.add(info);
    starrySky.updatePlayList(list);
  }

  public void setApplication(String application) {
    this.application = application;
  }

  public void initPlayback(Application application, @Nullable StarrySkyConfig config) {
    StarrySky.init(application, config);
    starrySky = StarrySky.with();
    starrySky.setVolume(1f);
    list = new ArrayList<>();
    starrySky.updatePlayList(list);
    manager = new TimerTaskManager();

    starrySky.addPlayerEventListener(new OnPlayerEventListener() {
      @Override
      public void onMusicSwitch(SongInfo songInfo) {

      }

      @Override
      public void onPlayerStart() {

      }

      @Override
      public void onPlayerPause() {

      }

      @Override
      public void onPlayerStop() {

      }

      @Override
      public void onPlayCompletion(SongInfo songInfo) {

      }

      @Override
      public void onBuffering() {

      }

      @Override
      public void onError(int errorCode, String errorMsg) {

      }
    });
  }

  public TimerTaskManager getManager() {
    return manager;
  }

  public PlayerControl getINSTANCE() {
    return starrySky;
  }

  @Override
  public void init(Context context) {
    this.context = context;
  }

  //  private MediaSessionCompat mediaSession;
//  private PlaybackStateCompat.Builder builder;
//  private ProgressiveMediaSource.Factory mediaSourcefactory;
//  private SimpleExoPlayer exoPlayer;
//  private AudioAttributes audioAttributes;
//  private Token sessionToken;
//  private Uri oldUri;
//  private MediaNotificationManager mediaNotificationManager;
//  private MediaSessionCompat.Callback sessionCallback = new Callback() {
//    @Override
//    public void onPlayFromUri(Uri uri, Bundle extras) {
//      super.onPlayFromUri(uri, extras);
//
//      if (uri != null) {
//        MediaSource mediaSource = extractMediaSourceFromUri(uri);
//        if (uri != oldUri) {
//          play(mediaSource);
//        } else {
//          play();
//        }
//        oldUri = uri;
//      }
//    }
//
//    @Override
//    public void onPause() {
//      super.onPause();
//      pause();
//    }
//
//    @Override
//    public void onStop() {
//      super.onStop();
//      stop();
//    }
//  };
//
//  @Override
//  public void onCreate() {
//    super.onCreate();
//    initPlayer();
//    initMediaSourcefactory();
//    initAudioAttributes();
//    mediaSession = new MediaSessionCompat(getApplicationContext(), this.getClass().getSimpleName());
//    mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
//        | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
//    builder = new PlaybackStateCompat.Builder()
//        .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE);
//    mediaSession.setPlaybackState(builder.build());
//    mediaSession.setCallback(sessionCallback);
//    sessionToken = mediaSession.getSessionToken();
//    mediaSession.setActive(true);
//  }
//
//  private void initMediaSourcefactory() {
//    String bocast = Util.getUserAgent(getApplication(), "Bocast");
//    mediaSourcefactory = new Factory(new DefaultDataSourceFactory(this, bocast),
//        new DefaultExtractorsFactory());
//  }
//
//  private MediaSource extractMediaSourceFromUri(Uri uri) {
//    return mediaSourcefactory.createMediaSource(uri);
//  }
//
//  private void play(MediaSource mediaSource) {
//    if (exoPlayer == null) {
//      initPlayer();
//    }
//    exoPlayer.prepare(mediaSource);
//    play();
//  }
//
//  private void play() {
//    exoPlayer.setPlayWhenReady(true);
//    updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
//    mediaSession.setActive(true);
//  }
//
//  private void pause() {
//    exoPlayer.setPlayWhenReady(false);
//    updatePlaybackState(PlaybackStateCompat.STATE_PAUSED);
//  }
//
//  private void stop() {
//    exoPlayer.setPlayWhenReady(false);
//    exoPlayer.release();
//    exoPlayer = null;
//    updatePlaybackState(PlaybackStateCompat.STATE_NONE);
//    mediaSession.setActive(false);
//    mediaSession.release();
//  }
//
//  @Override
//  public void onTaskRemoved(Intent rootIntent) {
//    super.onTaskRemoved(rootIntent);
//    stopSelf();
//  }
//
//  @Override
//  public void onDestroy() {
//    super.onDestroy();
//    stop();
//  }
//
//  private void updatePlaybackState(int state) {
//    mediaSession
//        .setPlaybackState(new PlaybackStateCompat.Builder()
//            .setState(state, 0L, 1.0f).build());
//  }
//
//  private void initPlayer() {
//    exoPlayer = new SimpleExoPlayer.Builder(getApplicationContext(),
//        new DefaultRenderersFactory(getApplicationContext()))
//        .setLoadControl(new DefaultLoadControl()).build();
//    if (audioAttributes == null) {
//      exoPlayer.setAudioAttributes(audioAttributes, true);
//    }
//  }
//
//  private void initAudioAttributes() {
//    audioAttributes = new Builder().setUsage(C.USAGE_MEDIA)
//        .setContentType(C.CONTENT_TYPE_MUSIC)
//        .build();
//  }
//
//  @Nullable
//  @Override
//  public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid,
//      @Nullable Bundle rootHints) {
//    return new BrowserRoot("_ROOT_", null);
//  }
//
//  @Override
//  public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaItem>> result) {
//
//  }
}
