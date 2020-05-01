package per.funown.bocast.modules.home.view.adapter;

import android.content.Context;
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
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssItem;
import per.funown.bocast.library.download.DownloadFactory;
import per.funown.bocast.library.net.NetManager;
import per.funown.bocast.library.net.service.ItunesApiService;
import per.funown.bocast.library.service.MusicService;
import per.funown.bocast.library.utils.DateUtils;
import per.funown.bocast.library.utils.DateUtils.DatePattern;
import per.funown.bocast.modules.home.R;
import per.funown.bocast.modules.home.model.EpisodesRepository;
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
  MusicService service;
  PlayerControl instance;
  Context context;
  EpisodesRepository repository;

  private boolean isPlaying;

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

  public PodcastRssEpisodeCellAdapter(Context context) {
    this.context = context;
  }

  @NonNull
  @Override
  public PodcastEpisodeCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    repository = new EpisodesRepository(context);
    View view = inflater.inflate(R.layout.cell_podcast_episode, parent, false);
    return new PodcastEpisodeCellViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull PodcastEpisodeCellViewHolder holder, int position) {
    service = ARouter.getInstance().navigation(MusicService.class);
    instance = service.getINSTANCE();
    RssItem item = items.get(position);
    isPlaying = instance.isCurrMusicIsPlaying(item.getGuid().getGuid());

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
        holder.updateTime_durationTime.setText(item.getPubDate() + " " + mins + ":" + seconds);
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
    holder.btn_play.setImageDrawable(
        activity.getDrawable(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play));
    holder.btn_play.setOnClickListener(v -> {
      if (!isPlaying) {
        List<SongInfo> playList = instance.getPlayList();
        SongInfo songInfo = null;
        for (int i = 0; i < playList.size(); i++) {
          SongInfo info = playList.get(i);
          if (info.getSongId().equals(item.getGuid().getGuid())) {
            songInfo = info;
            instance.playMusicByIndex(i);
            break;
          }
        }
        if (songInfo == null) {
          songInfo = new SongInfo();
          songInfo.setSongId(item.getGuid().getGuid());
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
          songInfo.setTrackNumber(position);
          songInfo.setDescription(feed.getChannel().getAtomLink().getHref());
          String filename = repository.isDownloaded(songInfo.getSongId(), songInfo.getSongUrl());
          if (filename != null && !filename.equals("")) {
            File queueDir = DownloadFactory.getINSTANCE(context).getQueueDir();
            songInfo.setSongUrl(queueDir.getAbsolutePath() + "/" + filename);
          }
          service.addSong(songInfo);
          instance.playMusicByInfo(songInfo);
        }
        isPlaying = true;
        service.getManager().startToUpdateProgress();
        holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_pause));
      } else {
        instance.pauseMusic();
        isPlaying = false;
        service.getManager().stopToUpdateProgress();
        holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_play));
      }

      instance.playbackState().observe(activity, new Observer<PlaybackStage>() {
        @Override
        public void onChanged(PlaybackStage playbackStage) {
          if (playbackStage == null) {
            return;
          }
          if (playbackStage.getStage() != PlaybackStage.NONE) {
            SongInfo songInfo = playbackStage.getSongInfo();
            if (songInfo.getSongId().equals(item.getGuid().getGuid())) {
              switch (playbackStage.getStage()) {
                case PlaybackStage.SWITCH:
                case PlaybackStage.START :
                  holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_pause));
                  break;
                case PlaybackStage.PAUSE:
                  holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_play));
                  break;
              }
            }
          }
        }
      });
    });

    AtomicBoolean inList = new AtomicBoolean(false);
    instance.getPlayList().forEach(info -> {
      if (info.getSongId().equals(item.getGuid().getGuid())) {
        holder.btn_addToList
            .setImageDrawable(activity.getDrawable(R.drawable.ic_playlist_add_check));
        inList.set(true);
      }
    });
    holder.btn_addToList.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.e(TAG, "add to list..." + inList.get());
        if (inList.get()) {
          List<SongInfo> playList = service.getINSTANCE().getPlayList();
          for (int i = 0; i < playList.size(); i++) {
            if (playList.get(i).getSongId().equals(item.getGuid().getGuid())) {
              playList.remove(i);
              service.getINSTANCE().updatePlayList(playList);
              holder.btn_addToList
                  .setImageDrawable(activity.getDrawable(R.drawable.ic_playlist_plus));
              inList.set(false);
            }
          }
        } else {
          SongInfo songInfo = new SongInfo();
          Log.i(TAG, item.toString());
          songInfo.setSongId(item.getGuid().getGuid());
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
          String filename = repository.isDownloaded(songInfo.getSongId(), songInfo.getSongUrl());
          if (filename != null && !filename.equals("")) {
            File queueDir = DownloadFactory.getINSTANCE(context).getQueueDir();
            songInfo.setSongUrl(queueDir.getAbsolutePath() + "/" + filename);
          }
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
