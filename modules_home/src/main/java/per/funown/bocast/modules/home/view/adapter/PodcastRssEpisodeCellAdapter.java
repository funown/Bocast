package per.funown.bocast.modules.home.view.adapter;

import android.content.Context;
import android.media.session.PlaybackState;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lzx.starrysky.common.PlaybackStage;
import com.lzx.starrysky.control.PlayerControl;
import com.lzx.starrysky.provider.SongInfo;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.NotNull;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.entity.Episode;
import per.funown.bocast.library.entity.Podcast;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssItem;
import per.funown.bocast.library.download.DownloadFactory;
import per.funown.bocast.library.service.MusicService;
import per.funown.bocast.library.utils.DateUtils;
import per.funown.bocast.library.utils.DateUtils.DatePattern;
import per.funown.bocast.modules.home.R;
import per.funown.bocast.modules.home.model.EpisodesViewModel;
import per.funown.bocast.modules.home.model.SubscribedPodcastViewModel;
import per.funown.bocast.modules.home.view.adapter.PodcastRssEpisodeCellAdapter.PodcastEpisodeCellViewHolder;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastRssEpisodeCellAdapter extends Adapter<PodcastEpisodeCellViewHolder> {

  private static final String TAG = PodcastRssEpisodeCellAdapter.class.getSimpleName();
  private List<RssItem> items;

  private FragmentActivity activity;
  private RssFeed feed;
  private int containerId;
  private long podcastId;
  MusicService service;
  PlayerControl instance;
  Context context;
  EpisodesViewModel episodesViewModel;
  SubscribedPodcastViewModel viewModel;

  public void setPodcastId(long podcastId) {
    this.podcastId = podcastId;
  }

  public void setItems(List<RssItem> items) {
    this.items = items;
  }

  public void setContainerId(int containerId) {
    this.containerId = containerId;
  }

  public void setActivity(FragmentActivity activity) {
    this.activity = activity;
  }

  public void setFeed(RssFeed feed) {
    this.feed = feed;
  }

  public PodcastRssEpisodeCellAdapter(Context context, EpisodesViewModel episodesViewModel,
      SubscribedPodcastViewModel viewModel) {
    this.context = context;
    this.viewModel = viewModel;
    this.episodesViewModel = episodesViewModel;
  }

  @NonNull
  @Override
  public PodcastEpisodeCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.cell_podcast_episode, parent, false);
    return new PodcastEpisodeCellViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull PodcastEpisodeCellViewHolder holder, int position) {
    service = ARouter.getInstance().navigation(MusicService.class);
    instance = service.getINSTANCE();
    RssItem item = items.get(position);
    Podcast podcast = viewModel.getPodcast(podcastId);
    AtomicBoolean isPlaying = new AtomicBoolean(false);

    holder.episodeTitle.setText(item.getTitle());
    Date date = DateUtils
        .stringToDate(item.getPubDate().trim(), DatePattern.RSS_DATE, Locale.ENGLISH);
    String pubDate = DateUtils
        .dateToString(date, DatePattern.ONLY_MINUTE);
    if (item.getDuration() != null) {
      if (item.getDuration().trim().contains(":")) {
        holder.updateTime_durationTime.setText(pubDate + " " + item.getDuration());
      } else {
        int duration = Integer.parseInt(item.getDuration().trim());
        int mins = duration / 60;
        int seconds = duration % 60;
        holder.updateTime_durationTime.setText(pubDate + " " + mins + ":" + seconds);
      }
    }
    holder.toDetail.setOnClickListener(v -> {
      Fragment fragment = (Fragment) ARouter.getInstance()
          .build(ArouterConstant.FRAGMENT_PODCAST_EPISODE_DETAIL).withObject("item", item)
          .withString("feed", feed.getChannel().getAtomLink().getHref())
          .navigation();
      FragmentManager manager = activity.getSupportFragmentManager();
      FragmentTransaction transaction = manager.beginTransaction();
      transaction.replace(containerId, fragment);
      transaction.addToBackStack(null);
      transaction.commit();
    });
    holder.btn_play.setOnClickListener(v -> {
      if (!isPlaying.get()) {
        List<SongInfo> playList = instance.getPlayList();
        SongInfo songInfo = null;
        for (int i = 0; i < playList.size(); i++) {
          SongInfo info = playList.get(i);
          if (info.getSongUrl().trim().equals(item.getEnclosure().getUrl().trim())) {
            songInfo = info;
            instance.playMusicByIndex(i);
            holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_pause));
            break;
          }
        }
        if (songInfo == null) {
          Podcast newPodcast = podcast;
          if (newPodcast == null) {
            newPodcast = new Podcast(feed.getChannel().getTitle(),
                feed.getChannel().getOwner().getName() == null ? feed.getChannel().getOwner()
                    .getName() : feed.getChannel().getAuthor(),
                feed.getChannel().getItems().size(),
                feed.getChannel().getAtomLink().getHref(),
                feed.getChannel().getImage().getHref());
            long id = viewModel.addPodcast(newPodcast);
            newPodcast.setId(id);
            Log.e(TAG, String.valueOf(id));
          }

          Episode episode = episodesViewModel.getEpisode(item.getTitle());
          if (episode == null) {
            episode = new Episode(newPodcast.getId(),
                item.getGuid().getGuid(),
                item.getTitle(),
                item.getSubtitle(),
                item.getPubDate(),
                item.getDuration(),
                item.getLink(),
                item.getEnclosure().getUrl(),
                item.getImage() == null ? feed.getChannel().getImage().getHref()
                    : item.getImage().getHref(),
                item.getDescription());
            episode.setId(episodesViewModel.insertEpisode(episode));
          }
          long episodeId = episode.getId();

          songInfo = buildSongInfo(item, newPodcast, episodeId);
          service.addSong(songInfo);
          instance.playMusicByInfo(songInfo);
          instance.playMusic();
        }
        isPlaying.set(true);
        service.getManager().startToUpdateProgress();
      } else {
        instance.pauseMusic();
        isPlaying.set(false);
        service.getManager().stopToUpdateProgress();
      }
    });

    instance.playbackState().observe(activity, playbackStage -> {
      if (playbackStage == null) {
        return;
      }
      Log.e(TAG, playbackStage.getStage());
      if (playbackStage.getStage() != PlaybackStage.NONE) {
        SongInfo songInfo = playbackStage.getSongInfo();
        if (songInfo.getSongUrl().equals(item.getEnclosure().getUrl())) {

          if (instance.getState() == PlaybackStateCompat.STATE_PLAYING) {
            holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_pause));
            isPlaying.set(true);
            holder.btn_addToList
                .setImageDrawable(activity.getDrawable(R.drawable.ic_playlist_add_check));
          } else {
            holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_play));
            isPlaying.set(false);
          }
        } else {
          holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_play));
          isPlaying.set(false);
        }
      }
    });

    Episode episode = episodesViewModel.getEpisode(item.getTitle());
    AtomicBoolean inList = new AtomicBoolean(false);
    instance.getPlayList().forEach(info -> {
      if (episode != null && info.getSongId().equals(String.valueOf(episode.getId()))) {
        holder.btn_addToList
            .setImageDrawable(activity.getDrawable(R.drawable.ic_playlist_add_check));
        inList.set(true);
      }
    });
    holder.btn_addToList.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (inList.get()) {
          List<SongInfo> playList = service.getINSTANCE().getPlayList();
          for (int i = 0; i < playList.size(); i++) {
            if (playList.get(i).getSongId().equals(item.getGuid().getGuid())) {
              service.removeSong(playList.get(i));
              holder.btn_addToList
                  .setImageDrawable(activity.getDrawable(R.drawable.ic_playlist_plus));
              inList.set(false);
            }
          }
        } else {
          Podcast newPodcast = podcast;
          if (newPodcast == null) {
            newPodcast = new Podcast(feed.getChannel().getTitle(),
                feed.getChannel().getOwner().getName() == null ? feed.getChannel().getOwner()
                    .getName() : feed.getChannel().getAuthor(),
                feed.getChannel().getItems().size(),
                feed.getChannel().getAtomLink().getHref(),
                feed.getChannel().getImage().getHref());
            long id = viewModel.addPodcast(newPodcast);
            newPodcast.setId(id);
            Log.e(TAG, String.valueOf(id));
          }
          Episode episode = new Episode(newPodcast.getId(),
              item.getGuid().getGuid(),
              item.getTitle(),
              item.getSubtitle(),
              item.getPubDate(),
              item.getDuration(),
              item.getLink(),
              item.getEnclosure().getUrl(),
              item.getImage().getHref(),
              item.getDescription());
          long episodeId = episodesViewModel.insertEpisode(episode);

          SongInfo songInfo = PodcastRssEpisodeCellAdapter.this
              .buildSongInfo(item, podcast, episodeId);
          List<SongInfo> list = service.getINSTANCE().getPlayList();
          list.add(songInfo);
          service.getINSTANCE().updatePlayList(list);
          service.addSong(songInfo);
          inList.set(true);
          holder.btn_addToList
              .setImageDrawable(activity.getDrawable(R.drawable.ic_playlist_add_check));
          Toast.makeText(activity.getApplicationContext(),
              songInfo.getSongName() + "had been added to playlist" + service.getINSTANCE()
                  .getPlayList().size(), Toast.LENGTH_LONG).show();
        }
      }
    });
  }

  @NotNull
  private SongInfo buildSongInfo(RssItem item, Podcast podcast, long episodeId) {
    SongInfo songInfo = new SongInfo();
    Log.i(TAG, item.toString());
    songInfo.setSongId(String.valueOf(episodeId));
    songInfo.setSongName(item.getTitle());
    songInfo.setSongCover(
        item.getImage() == null ? feed.getChannel().getImage().getHref()
            : item.getImage().getHref());
    songInfo.setArtist(item.getAuthor());
    songInfo.setPublishTime(item.getPubDate());
    songInfo.setSongUrl(item.getEnclosure().getUrl());
    songInfo.setAlbumName(feed.getChannel().getTitle());
    songInfo.setAlbumArtist(feed.getChannel().getAuthor());
    songInfo.setAlbumCover(feed.getChannel().getImage().getHref());
    songInfo.setAlbumId(feed.getChannel().getLink());
    songInfo.setDescription(feed.getChannel().getAtomLink().getHref());
    if (podcast != null) {
      String filename = episodesViewModel.isDownloaded(episodeId);
      if (filename != null && !filename.equals("")) {
        File queueDir = DownloadFactory.getINSTANCE(context).getQueueDir();
        songInfo.setSongUrl(queueDir.getAbsolutePath() + "/" + filename);
        Log.e(TAG, songInfo.getSongUrl());
      }
    }
    return songInfo;
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  class PodcastEpisodeCellViewHolder extends RecyclerView.ViewHolder {

    private ConstraintLayout toDetail, play;
    private ImageView btn_play, btn_addToList;
    private TextView episodeTitle, updateTime_durationTime;

    public PodcastEpisodeCellViewHolder(@NonNull View itemView) {
      super(itemView);
      play = itemView.findViewById(R.id.play_bar);
      btn_play = itemView.findViewById(R.id.btn_play);
      toDetail = itemView.findViewById(R.id.title_bar);
      episodeTitle = itemView.findViewById(R.id.Episode_Title);
      btn_addToList = itemView.findViewById(R.id.btn_addToList);
      updateTime_durationTime = itemView.findViewById(R.id.Update_time);
    }
  }
}
