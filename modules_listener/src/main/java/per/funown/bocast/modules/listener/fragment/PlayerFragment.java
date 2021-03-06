package per.funown.bocast.modules.listener.fragment;

import android.util.Log;
import android.os.Bundle;
import android.content.res.Resources;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lzx.starrysky.provider.SongInfo;
import com.lzx.starrysky.control.PlayerControl;
import com.lzx.starrysky.utils.TimerTaskManager;
import com.lzx.starrysky.control.OnPlayerEventListener;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;

import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import per.funown.bocast.library.entity.HistoryItem;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssItem;
import per.funown.bocast.library.service.MusicService;
import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.download.BaseDownloadListener;
import per.funown.bocast.library.download.DownloadFactory;
import per.funown.bocast.library.download.DownloadStatus;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.library.utils.RssFetchUtils;
import per.funown.bocast.modules.listener.R;
import per.funown.bocast.modules.listener.viewmodel.PlayerViewModel;
import per.funown.bocast.modules.listener.databinding.FragmentPlayerBinding;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = ArouterConstant.FRAGMENT_LISTENER)
public class PlayerFragment extends Fragment implements OnPlayerEventListener {

  private static final String TAG = PlayerFragment.class.getSimpleName();

  FragmentPlayerBinding binding;
  PlayerViewModel viewModel;
  MusicService service;
  BaseDownloadListener downloadListener;
  DownloadFactory factory;
  private BottomSheetBehavior bottomSheetBehavior;

  public PlayerFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ARouter.getInstance().inject(this);

    binding = FragmentPlayerBinding.inflate(getLayoutInflater());
    service = ARouter.getInstance().navigation(MusicService.class);
    viewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);
    factory = DownloadFactory.getINSTANCE(getContext());

  }

  /**
   * 使播放器固定于界面底部
   */
  @Override
  public void onResume() {
    super.onResume();
    Log.i(TAG, "on Resume...");
    ViewGroup viewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
    if (binding.getRoot().getParent() != null) {
      ((ViewGroup) binding.getRoot().getParent()).removeView(binding.getRoot());
    }
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    layoutParams.bottomMargin = getNavigationBarHeight();
    viewGroup.addView(binding.getRoot(), layoutParams);
    // 设置播放按钮状态
    if (service.getINSTANCE().getNowPlayingSongInfo() != null && service.getINSTANCE().isPlaying()) {
      Log.e(TAG, "playing 0.5");
      binding.btnPlayPause.setProgress(0.5f);
      Log.e(TAG, "playing " + binding.btnPlayPause.getProgress());
    }

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    Log.e(TAG, "on Create view...");
    // 设置播放器的收缩扩展功能
    binding.playerContent.setVisibility(View.INVISIBLE);
    bottomSheetBehavior = BottomSheetBehavior.from(binding.mainContent);
    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    bottomSheetBehavior.setHideable(false);
    bottomSheetBehavior.setFitToContents(true);
    bottomSheetBehavior.addBottomSheetCallback(new BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
          binding.playerContent.setVisibility(View.VISIBLE);
          binding.playerBar.setVisibility(View.GONE);
        } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
          binding.playerContent.setVisibility(View.INVISIBLE);
          binding.playerBar.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onSlide(@NonNull View bottomSheet, float slideOffset) {
      }
    });

    PlayerControl instance = service.getINSTANCE();
    instance.addPlayerEventListener(this);

    // 若已存在播放项目则初始化界面信息
    if (instance.getNowPlayingSongInfo() != null) {
      SongInfo songInfo = instance.getNowPlayingSongInfo();
      binding.EpisodeTitle.setText(songInfo.getSongName());
      binding.EpisodeCover.setImageURI(songInfo.getSongCover());
      String time = convertTimeToString(songInfo.getDuration());
      binding.trackDuration.setText(time);
      binding.PodcastAuthor.setText(songInfo.getArtist());
      binding.podcastTitle.setText(songInfo.getSongName());
      binding.progressBar.setMax((int) songInfo.getDuration());
    } else {
      binding.progressBar.setProgress(0);
      binding.trackbar.setProgress(0);
    }

    // 创建播放进度监听
    TimerTaskManager timerTaskManager = service.getManager();
    timerTaskManager.setUpdateProgressTask(() -> {
      long position = instance.getPlayingPosition();
      long track = instance.getDuration();
      long buffered = instance.getBufferedPosition();
      if (binding.trackbar.getMax() != track) {
        binding.trackbar.setMax((int) track);
        binding.progressBar.setMax((int) track);
      }
      binding.trackbar.setProgress((int) position);
      binding.progressBar.setProgress((int) position);
      binding.trackbar.setSecondaryProgress((int) buffered);
      binding.progressBar.setSecondaryProgress((int) buffered);

      String time = convertTimeToString(position);
      binding.TrackStart.setText(time);
    });

    // 设置播放按钮状态以及功能
    binding.btnPlayPause.setScaleType(ScaleType.CENTER_INSIDE);
    binding.barBtnPlay.setOnClickListener(v -> {
      if (instance.isPlaying()) {
        timerTaskManager.stopToUpdateProgress();
        instance.pauseMusic();
      } else {
        timerTaskManager.startToUpdateProgress();
        instance.playMusic();
      }
    });

    binding.btnPlayPause.setOnClickListener(v -> {
      if (instance.isPlaying()) {
        binding.btnPlayPause.setMinProgress(0.5f);
        binding.btnPlayPause.setMaxProgress(1f);
        timerTaskManager.stopToUpdateProgress();
        instance.pauseMusic();
      } else {
        binding.btnPlayPause.setMinProgress(0f);
        binding.btnPlayPause.setMaxProgress(0.5f);
        timerTaskManager.startToUpdateProgress();
        instance.playMusic();
      }
      binding.btnPlayPause.playAnimation();
    });

    // 设置下一首功能
    binding.lavNext.setOnClickListener(v -> {
      PlayerControl instance12 = service.getINSTANCE();
      if (instance12.getPlayList().size() > 1) {
        int nowPlayingIndex = instance12.getNowPlayingIndex();
        if (nowPlayingIndex == instance12.getPlayList().size() - 1) {
          instance12.playMusicByIndex(0);
        } else {
          instance12.playMusicByIndex(nowPlayingIndex + 1);
        }
      }
    });

    // 设置上一首按钮功能
    binding.lavPrev.setOnClickListener(v -> {
      PlayerControl instance1 = service.getINSTANCE();
      if (instance1.getPlayList().size() > 1) {
        int nowPlayingIndex = instance1.getNowPlayingIndex();
        if (nowPlayingIndex != 0) {
          instance1.playMusicByIndex(nowPlayingIndex - 1);
        } else {
          Toast.makeText(requireContext(), "There are no more song", Toast.LENGTH_SHORT);
        }
      }
    });

    // 标题点击进入该分集详细信息
    binding.EpisodeTitle.setOnClickListener(v -> {
      SongInfo songInfo = instance.getNowPlayingSongInfo();
      if (songInfo != null) {
        RssFeed feed = RssFetchUtils.fetchRss(songInfo.getDescription());
        RssItem item = null;
        for (RssItem rssItem : feed.getChannel().getItems()) {
          if (rssItem.getEnclosure().getUrl().equals(songInfo.getSongUrl())) {
            item = rssItem;
            break;
          }
        }
        Fragment todetail = (Fragment) ARouter.getInstance()
            .build(ArouterConstant.FRAGMENT_PODCAST_EPISODE_DETAIL)
            .withObject("item", item)
            .withString("feed", songInfo.getDescription()).navigation();
        FragmentTransitionUtil.getINSTANCE().setManager(getActivity().getSupportFragmentManager());
        FragmentTransitionUtil.getINSTANCE().transit(todetail);
      }
    });

    // 设置进度条拖动功能，使播放时间随拖动而变化
    binding.trackbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
          instance.seekTo(progress);
        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    // 初始化下载按钮
    initDownloadButton(instance.getNowPlayingSongInfo());

    // 设置播放列表
    binding.playlist.setOnClickListener(v -> EpisodeListDialogFragment.newInstance(getContext())
        .show(getActivity().getSupportFragmentManager(), "playlist"));

    bottomSheetBehavior.setSaveFlags(BottomSheetBehavior.SAVE_ALL);
    if (binding.getRoot().getParent() != null) {
      ((ViewGroup) binding.getRoot().getParent()).removeView(binding.getRoot());
    }
    return binding.getRoot();
  }

  /**
   * 初始化下载按钮
   * @param songInfo
   */
  private void initDownloadButton(SongInfo songInfo) {
    Log.e(TAG, "initButton");
    // 初始化状态
    binding.downloadProgress.setProgress(0);
    binding.downloadProgress.setVisibility(View.GONE);
    binding.btnDownload.setColorFilter(getContext().getColor(R.color.grey));
    binding.btnDownload.setImageDrawable(getContext().getDrawable(R.drawable.ic_download));
    // 存在正在播放的项目则查询是否正在下载
    if (songInfo != null) {
      DownloadTask task = factory.getTask(songInfo.getSongUrl());
      DownloadEpisode downloadEpisode = viewModel
          .getDownloadEpisode(Long.valueOf(songInfo.getSongId()));
      // 该分集处于下载状态
      if (downloadEpisode != null) {
        // 下载已完成
        if (downloadEpisode.getStatus().equals(DownloadStatus.FINISHED.name())) {
          binding.btnDownload.setClickable(false);
          binding.btnDownload.setColorFilter(getContext().getColor(R.color.colorChecked));
          binding.btnDownload.setImageDrawable(getContext().getDrawable(R.drawable.ic_finish));
        }
        // 下载未完成
        else {
          // 存在该下载任务
          if (task != null) {
            binding.downloadProgress.setVisibility(View.VISIBLE);
            BreakpointInfo breakpointInfo = factory
                .getBreakpointInfo(downloadEpisode.getUrl(), downloadEpisode.getFilename());
            DownloadEpisode item = (DownloadEpisode) task.getTag();
            binding.btnDownload.setColorFilter(getContext().getColor(R.color.colorChecked));

            // 下载中
            if (item.getStatus().equals(DownloadStatus.DOWNLOADING)) {
              Log.e(TAG, "downloading -->" + downloadEpisode);
              binding.btnDownload.setImageDrawable(getContext().getDrawable(R.drawable.ic_pause));
            }
            // 下载暂停中
            else {
              Log.e(TAG, "pause -->" + downloadEpisode);
              binding.btnDownload
                  .setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_down));
              binding.downloadProgress.setMax(breakpointInfo.getTotalLength());
              binding.downloadProgress.setProgress(breakpointInfo.getTotalOffset());
            }
            // 创建新下载监听
            downloadListener = new BaseDownloadListener(item, getContext(),
                binding.downloadProgress,
                binding.btnDownload);
            factory.changeListener(task, downloadListener);
          }
          //未有对应下载任务则查找下载断点信息
          else {
            Log.e(TAG, "history -->" + downloadEpisode);
            BreakpointInfo breakpointInfo = factory
                .getBreakpointInfo(downloadEpisode.getUrl(), downloadEpisode.getFilename());
            // 存在该断点
            if (breakpointInfo != null) {
              downloadEpisode.setOffset(breakpointInfo.getTotalOffset());
              downloadEpisode.setTotal(breakpointInfo.getTotalLength());
              binding.btnDownload.setColorFilter(getContext().getColor(R.color.colorChecked));
              binding.btnDownload
                  .setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_down));
              binding.downloadProgress.setVisibility(View.VISIBLE);
              binding.downloadProgress.setMax(breakpointInfo.getTotalLength());
              binding.downloadProgress.setProgress(breakpointInfo.getTotalOffset());
            }
          }
        }
      }
    }

    // 实现下载
    binding.btnDownload.setOnClickListener(v -> {
      if (downloadListener != null) {
        String status = downloadListener.getItem().getStatus();
        if (status.equals(DownloadStatus.DOWNLOADING.name())) {
          DownloadTask task = factory.getTask(songInfo.getSongUrl());
          factory.stop(task, downloadListener);
          binding.btnDownload.setColorFilter(getContext().getColor(R.color.colorChecked));
          binding.btnDownload.setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_down));
          viewModel.addDownloadEpisode(downloadListener.getItem());
        } else if (!status.equals(DownloadStatus.FINISHED.name())) {
          factory.restart(songInfo.getSongUrl(), downloadListener);
        }
      } else {
        int i = songInfo.getSongUrl().lastIndexOf("/");

        String filename = songInfo.getSongUrl().substring(i + 1);
        DownloadEpisode episode = new DownloadEpisode();
        episode.setFilename(filename);
        episode.setEpisodeId(Long.valueOf(songInfo.getSongId()));
        episode.setStatus(DownloadStatus.DOWNLOADING);
        episode.setUrl(songInfo.getSongUrl());
        downloadListener = new BaseDownloadListener(episode, requireContext(),
            binding.downloadProgress,
            binding.btnDownload);
        factory.addTask(songInfo.getSongUrl(), filename, episode, downloadListener);
        binding.downloadProgress.setVisibility(View.VISIBLE);
      }
    });
  }

  // 转换时间
  public static String convertTimeToString(Long time) {
    DateTimeFormatter ftf = DateTimeFormatter.ofPattern("mm:ss");
    return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
  }

  // 获取导航栏高度
  private int getNavigationBarHeight() {
    Resources resources = getActivity().getResources();
    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
    int height = resources.getDimensionPixelSize(resourceId);
    return height;
  }

  /**
   * 刷新播放器界面信息
   * @param songInfo
   */
  @Override
  public void onMusicSwitch(SongInfo songInfo) {
    binding.PodcastAuthor.setText(songInfo.getArtist());
    binding.EpisodeTitle.setText(songInfo.getSongName());
    binding.podcastTitle.setText(songInfo.getSongName());
    binding.EpisodeCover.setImageURI(songInfo.getSongCover());
    binding.trackDuration.setText(convertTimeToString(songInfo.getDuration()));
    downloadListener = null;
    initDownloadButton(songInfo);
    HistoryItem history = viewModel.getHistory(Long.valueOf(songInfo.getSongId()));
    if (history != null) {
     service.getINSTANCE().seekTo((long) history.getPercent());
    }
  }

  @Override
  public void onPlayerStart() {
    binding.barBtnPlay.setImageDrawable(getActivity().getDrawable(R.drawable.ic_pause));
    binding.btnPlayPause.setProgress(0.5f);
  }

  @Override
  public void onPlayerPause() {
    PlayerControl instance = service.getINSTANCE();
    binding.barBtnPlay.setImageDrawable(getActivity().getDrawable(R.drawable.ic_play));
    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
      binding.btnPlayPause.setProgress(0f);
    }
    SongInfo songInfo = instance.getNowPlayingSongInfo();
    viewModel.addHistory(songInfo, binding.trackbar);
  }

  @Override
  public void onPlayerStop() {
    PlayerControl instance = service.getINSTANCE();
    SongInfo songInfo = instance.getNowPlayingSongInfo();
    viewModel.addHistory(songInfo, binding.trackbar);
  }

  @Override
  public void onPlayCompletion(SongInfo songInfo) {
    viewModel.addHistory(songInfo, binding.trackbar);
  }

  @Override
  public void onBuffering() {
  }

  @Override
  public void onError(int errorCode, String errorMsg) {
    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
  }
}
