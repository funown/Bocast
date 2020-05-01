package per.funown.bocast.modules.listener.fragment;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import com.liulishuo.okdownload.DownloadTask;
import com.lzx.starrysky.control.OnPlayerEventListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lzx.starrysky.control.PlayerControl;
import com.lzx.starrysky.provider.SongInfo;
import com.lzx.starrysky.utils.TimerTaskManager;

import android.view.View;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;

import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import per.funown.bocast.library.service.MusicService;
import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.download.BaseDownloadListener;
import per.funown.bocast.library.download.DownloadFactory;
import per.funown.bocast.library.download.DownloadStatus;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.modules.listener.R;
import per.funown.bocast.modules.listener.viewmodel.PlayerViewModel;
import per.funown.bocast.modules.listener.databinding.FragmentPlayerBinding;

/**
 * A simple {@link Fragment} subclass.
 */
@Route(path = ArouterConstant.FRAGMENT_LISTENER)
public class PlayerFragment extends Fragment {

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

  @Override
  public void onResume() {
    super.onResume();
    ViewGroup viewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
    if (binding.getRoot().getParent() != null) {
      ((ViewGroup) binding.getRoot().getParent()).removeView(binding.getRoot());
    }
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    layoutParams.bottomMargin = getNavigationBarHeight();
    viewGroup.addView(binding.getRoot(), layoutParams);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
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
    instance.addPlayerEventListener(new OnPlayerEventListener() {
      @Override
      public void onMusicSwitch(SongInfo songInfo) {
        binding.PodcastAuthor.setText(songInfo.getArtist());
        binding.EpisodeTitle.setText(songInfo.getSongName());
        binding.podcastTitle.setText(songInfo.getSongName());
        binding.EpisodeCover.setImageURI(songInfo.getSongCover());
        initDownloadButton(songInfo);
      }

      @Override
      public void onPlayerStart() {
        instance.setVolume(1f);
        binding.barBtnPlay.setImageDrawable(getActivity().getDrawable(R.drawable.ic_pause));
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
          binding.btnPlayPause.setFrame(63);
        }
      }

      @Override
      public void onPlayerPause() {
        binding.barBtnPlay.setImageDrawable(getActivity().getDrawable(R.drawable.ic_play));
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
          binding.btnPlayPause.setProgress(0f);
        }
        SongInfo songInfo = instance.getNowPlayingSongInfo();
        viewModel.addHistory(songInfo, binding.trackbar);
      }

      @Override
      public void onPlayerStop() {
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
    });

    if (instance.getNowPlayingSongInfo() != null) {
      SongInfo songInfo = instance.getNowPlayingSongInfo();
      binding.EpisodeTitle.setText(songInfo.getSongName());
      binding.EpisodeCover.setImageURI(songInfo.getSongCover());
      String time = convertTimeToString(songInfo.getDuration());
      binding.trackDuration.setText(time);
      binding.PodcastAuthor.setText(songInfo.getArtist());
      binding.podcastTitle.setText(songInfo.getSongName());
      binding.progressBar.setMax((int) songInfo.getDuration());
      if (instance.isPlaying()) {
        binding.btnPlayPause.setProgress(0.5f);
      }
    } else {
      binding.progressBar.setProgress(0);
      binding.trackbar.setProgress(0);
    }

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
    binding.EpisodeTitle.setOnClickListener(v -> {
      SongInfo songInfo = instance.getNowPlayingSongInfo();
      if (songInfo != null) {
        Fragment todetail = (Fragment) ARouter.getInstance()
            .build(ArouterConstant.FRAGMENT_PODCAST_EPISODE_DETAIL)
            .withString("feed", songInfo.getDescription())
            .withString("guid", songInfo.getSongId()).navigation();
        FragmentTransitionUtil.getINSTANCE().setManager(getActivity().getSupportFragmentManager());
        FragmentTransitionUtil.getINSTANCE().transit(todetail);
      }
    });
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

    initDownloadButton(instance.getNowPlayingSongInfo());

    binding.playlist.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        EpisodeListDialogFragment.newInstance(getContext())
            .show(requireActivity().getSupportFragmentManager(), "playlist");
      }
    });

    bottomSheetBehavior.setSaveFlags(BottomSheetBehavior.SAVE_ALL);
    if (binding.getRoot().getParent() != null) {
      ((ViewGroup) binding.getRoot().getParent()).removeView(binding.getRoot());
    }
    return binding.getRoot();
  }

  private void initDownloadButton(SongInfo songInfo) {
    Log.e(TAG, "initButton");
    if (songInfo != null) {
      Log.i(TAG, songInfo.getSongName());
      DownloadTask task = factory.getTask(songInfo.getSongUrl());
      DownloadEpisode downloadEpisode = viewModel
          .getDownloadEpisode(songInfo.getSongId(), songInfo.getSongUrl());
      if (downloadEpisode != null) {
        if (downloadEpisode.getStatus().equals(DownloadStatus.FINISHED.name())) {
          binding.btnDownload.setClickable(false);
          binding.btnDownload.setColorFilter(getContext().getColor(R.color.grey));
          binding.btnDownload.setImageDrawable(getContext().getDrawable(R.drawable.ic_finish));
        } else {

          if (task != null) {
            DownloadEpisode item = (DownloadEpisode) task.getTag();
            downloadListener = new BaseDownloadListener(item, getContext(),
                binding.downloadProgress,
                binding.btnDownload);
            factory.changeListener(task, downloadListener);

          } else {
            binding.btnDownload.setImageDrawable(getContext().getDrawable(R.drawable.ic_download));
            binding.downloadProgress.setVisibility(View.GONE);
            downloadListener = null;
          }
        }
      }
    }

    binding.btnDownload.setOnClickListener(v -> {
      Log.e(TAG, "-->" + (downloadListener != null));
      if (downloadListener != null) {
        String status = downloadListener.getItem().getStatus();
        Log.e(TAG, "-->" + status);
        if (status.equals(DownloadStatus.DOWNLOADING.name())) {
          DownloadTask task = factory.getTask(songInfo.getSongUrl());
          factory.stop(task);
          binding.btnDownload.setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_down));
        } else if (!status.equals(DownloadStatus.FINISHED.name())) {
          factory.restart(songInfo.getSongUrl(), downloadListener);
        }
      } else {
        int i = songInfo.getSongUrl().lastIndexOf("/");
        String filename = songInfo.getSongUrl().substring(i + 1);
        DownloadEpisode episode = new DownloadEpisode();
        episode.setEpisode(songInfo.getTrackNumber());
        episode.setFilename(filename);
        episode.setEpisodeTitle(songInfo.getSongName());
        episode.setPodcast(songInfo.getAlbumName());
        episode.setPodcastId(songInfo.getAlbumId());
        episode.setImageUri(songInfo.getAlbumCover());
        episode.setGuid(songInfo.getSongId());
        episode.setStatus(DownloadStatus.DOWNLOADING);
        episode.setRssLink(songInfo.getDescription());
        downloadListener = new BaseDownloadListener(episode, getContext(), binding.downloadProgress,
            binding.btnDownload);
        factory.addTask(songInfo.getSongUrl(), filename, episode, downloadListener);
        binding.downloadProgress.setVisibility(View.VISIBLE);
      }
    });
  }

  public static String convertTimeToString(Long time) {
    DateTimeFormatter ftf = DateTimeFormatter.ofPattern("mm:ss");
    return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
  }

  private int getNavigationBarHeight() {
    Resources resources = getActivity().getResources();
    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
    int height = resources.getDimensionPixelSize(resourceId);
    return height;
  }

}
