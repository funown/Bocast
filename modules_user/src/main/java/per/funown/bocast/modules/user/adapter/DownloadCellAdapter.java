package per.funown.bocast.modules.user.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.DownloadTask.Builder;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.lzx.starrysky.provider.SongInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import me.tankery.lib.circularseekbar.CircularSeekBar;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.download.BaseDownloadListener;
import per.funown.bocast.library.download.DownloadFactory;
import per.funown.bocast.library.download.DownloadStatus;
import per.funown.bocast.library.entity.Episode;
import per.funown.bocast.library.entity.Podcast;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssItem;
import per.funown.bocast.library.repo.DownloadedEpisodeRepository;
import per.funown.bocast.library.repo.EpisodeRepository;
import per.funown.bocast.library.repo.PodcastRepository;
import per.funown.bocast.library.service.MusicService;
import per.funown.bocast.library.utils.FileUtil;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.library.utils.ItemTouchHelperAdapter;
import per.funown.bocast.library.utils.ItemTouchHelperViewHolder;
import per.funown.bocast.library.utils.RssFetchUtils;
import per.funown.bocast.modules.user.R;
import per.funown.bocast.modules.user.adapter.DownloadCellAdapter.DownloadCellHolder;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DownloadCellAdapter extends Adapter<DownloadCellHolder> implements
    ItemTouchHelperAdapter {

  private static final String TAG = DownloadCellAdapter.class.getSimpleName();
  private List<DownloadTask> tasks;
  private List<BaseDownloadListener> listeners = new ArrayList<>();
  private DownloadedEpisodeRepository downloadedEpisodeRepository;
  private PodcastRepository podcastRepository;
  private EpisodeRepository episodeRepository;
  boolean isDownloading;
  DownloadFactory instance;
  MusicService service;
  Context context;
  private int containerId;

  public void setContainerId(int containerId) {
    this.containerId = containerId;
  }

  public void setService(MusicService service) {
    this.service = service;
  }

  public DownloadCellAdapter(Context context) {
    this.context = context;
    tasks = new ArrayList<>();
    instance = DownloadFactory.getINSTANCE(context);
    downloadedEpisodeRepository = new DownloadedEpisodeRepository(context);
    episodeRepository = new EpisodeRepository(context);
    podcastRepository = new PodcastRepository(context);
    List<DownloadEpisode> allDownloadEpisodes = downloadedEpisodeRepository
        .getAllDownloadEpisodes();
    for (DownloadEpisode episode : allDownloadEpisodes) {
      DownloadTask task = new Builder(episode.getUrl(), instance.getQueueDir())
          .setFilename(episode.getFilename())
          .setMinIntervalMillisCallbackProcess(150)
          .setPassIfAlreadyCompleted(true)
          .build();
      task.setTag(episode);
      tasks.add(task);
    }
  }

  public void setTasks(List<DownloadTask> tasks) {
    for (int i = 0; i < tasks.size(); i++) {
      boolean exist = false;
      for (DownloadTask task : this.tasks) {
        if (task.getUrl().equals(tasks.get(i).getUrl())) {
          exist = true;
        }
      }
      if (!exist) {
        this.tasks.add(i, tasks.get(i));
      }
    }
  }

  @NonNull
  @Override
  public DownloadCellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.cell_download, parent, false);
    instance = DownloadFactory.getINSTANCE(parent.getContext());
    return new DownloadCellHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull DownloadCellHolder holder, int position) {
    DownloadTask task = tasks.get(position);
    DownloadEpisode downloadEpisode = (DownloadEpisode) task.getTag();
    Log.e(TAG, String.valueOf(downloadEpisode.getEpisodeId()));
    Episode item = episodeRepository.getEpisodeById(downloadEpisode.getEpisodeId());
    Podcast podcast = podcastRepository.getPodcastById(item.getPodcastId());
    holder.podcastLogo.setImageURI(item.getImage());
    holder.episodeTitle.setText(item.getTitle());
    holder.author.setText(podcast.getTitle());
    BreakpointInfo currentInfo = StatusUtil.getCurrentInfo(task);
    BaseDownloadListener baseDownloadListener = new BaseDownloadListener(downloadEpisode, context,
        holder.progressBar, holder.btn_download);
    instance.changeListener(task, baseDownloadListener);
    listeners.add(baseDownloadListener);
    holder.btn_download.setColorFilter(context.getColor(R.color.colorChecked));
    if (currentInfo != null) {
      holder.progressBar.setMax(currentInfo.getTotalLength());
      holder.progressBar.setProgress(currentInfo.getTotalOffset());
    } else {
      holder.progressBar.setMax(downloadEpisode.getTotal());
      holder.progressBar.setProgress(downloadEpisode.getOffset());
    }
    holder.progressBar.setVisibility(View.VISIBLE);
    isDownloading = baseDownloadListener.getItem().getStatus()
        .equals(DownloadStatus.DOWNLOADING.name());
    if (!isDownloading) {
      holder.btn_download
          .setImageDrawable(context.getDrawable(R.drawable.ic_arrow_down));
    } else {
      holder.btn_download
          .setImageDrawable(context.getDrawable(R.drawable.ic_pause));
    }
    if (baseDownloadListener.getItem().getStatus().equals(DownloadStatus.FINISHED.name())) {
      holder.progressBar.setVisibility(View.GONE);
      holder.btn_download.setImageDrawable(context.getDrawable(R.drawable.ic_finish));
      holder.btn_download.setClickable(false);
    }

    holder.btn_download.setOnClickListener(v -> {
      Log.e(TAG, baseDownloadListener.getItem().getStatus());
      if (baseDownloadListener.getItem().getStatus().equals(DownloadStatus.DOWNLOADING.name())) {
        instance.stop(task, baseDownloadListener);
        downloadedEpisodeRepository.addDownload(baseDownloadListener.getItem());
      } else if (baseDownloadListener.getItem().getStatus().equals(DownloadStatus.PAUSE.name())) {
        Log.e(TAG, "restart");
        instance.restart(task.getUrl(), baseDownloadListener);
      }
    });

    holder.episodeTitle.setOnClickListener(v -> {
      RssFeed feed = RssFetchUtils.fetchRss(podcast.getRssLink());
      feed.getChannel().getItems().forEach(new Consumer<RssItem>() {
        @Override
        public void accept(RssItem rssItem) {
          if (rssItem.getGuid().getGuid().equals(item.getGuid())) {
            Fragment fragment = (Fragment) ARouter.getInstance()
                .build(ArouterConstant.FRAGMENT_PODCAST_EPISODE_DETAIL)
                .withObject("item", rssItem).navigation();
            FragmentTransitionUtil.getINSTANCE().transit(fragment, containerId);
            return;
          }
        }
      });

    });

    holder.itemView.setOnClickListener(v -> {
      if (baseDownloadListener.getItem().getStatus().equals(DownloadStatus.FINISHED.name())) {
        RssFeed feed = RssFetchUtils.fetchRss(podcast.getRssLink());
        List<RssItem> items = feed.getChannel().getItems();
        for (RssItem rssItem : items) {
          if (rssItem.getGuid().getGuid().equals(item.getGuid())) {
            SongInfo songInfo = new SongInfo();
            songInfo.setSongId(rssItem.getGuid().getGuid());
            songInfo.setSongName(rssItem.getTitle());
            songInfo.setSongCover(
                rssItem.getImage() == null ? feed.getChannel().getImage().getHref()
                    : rssItem.getImage().getHref());
            songInfo.setArtist(rssItem.getAuthor());
            songInfo.setPublishTime(rssItem.getPubDate());
            songInfo.setSongUrl(task.getFile().getPath());
            songInfo.setAlbumName(feed.getChannel().getTitle());
            songInfo.setAlbumArtist(feed.getChannel().getAuthor());
            songInfo.setAlbumCover(feed.getChannel().getImage().getHref());
            songInfo.setAlbumId(feed.getChannel().getLink());
            songInfo.setDescription(feed.getChannel().getAtomLink().getHref());
          }
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return tasks.size();
  }

  @Override
  public void onItemMove(int adapterPosition, int adapterPosition1) {
  }

  @Override
  public void onItemDismiss(int adapterPosition) {
    DownloadTask task = tasks.get(adapterPosition);
    BaseDownloadListener downloadListener = listeners.get(adapterPosition);
    DownloadEpisode item = downloadListener.getItem();
    notifyItemRemoved(adapterPosition);
    tasks.remove(task);
    listeners.remove(downloadListener);
    instance.stop(task, downloadListener);
    DownloadEpisode downloadEpisode = downloadedEpisodeRepository.getDownloadEpisode(task.getUrl());
    if (downloadEpisode != null) {
      downloadedEpisodeRepository.setUnDownloaded(downloadEpisode);
    }
    AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
        .setMessage(context.getString(R.string.file_delete))
        .setNeutralButton(R.string.cancel, new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            tasks.add(adapterPosition, task);
            listeners.add(adapterPosition, downloadListener);
            notifyItemInserted(adapterPosition);
            if (item.getStatus().equals(DownloadStatus.FINISHED.name())
                || item.getStatus().equals(DownloadStatus.PAUSE.name())) {
              downloadedEpisodeRepository.addDownload(downloadEpisode);
            } else if (item.getStatus().equals(DownloadStatus.DOWNLOADING)) {
              instance.addTask(task, downloadListener);
            }
          }
        })
        .setNegativeButton(R.string.dont_delete, null)
        .setPositiveButton(R.string.YES, new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            FileUtil.deleteFileCompletely(context, task.getFile().getPath(), item.getFilename());
          }
        }).create();
    alertDialog.show();
    notifyItemRangeChanged(0, getItemCount());
  }


  class DownloadCellHolder extends ViewHolder implements ItemTouchHelperViewHolder {

    private SimpleDraweeView podcastLogo;
    private TextView episodeTitle, author;
    private CircularSeekBar progressBar;
    private ImageView btn_download;

    public DownloadCellHolder(@NonNull View itemView) {
      super(itemView);
      podcastLogo = itemView.findViewById(R.id.Podcast_logo);
      episodeTitle = itemView.findViewById(R.id.episode_title);
      author = itemView.findViewById(R.id.author);
      progressBar = itemView.findViewById(R.id.progressBar);
      btn_download = itemView.findViewById(R.id.btn_download);
    }

    @Override
    public void onItemSelected(Context context) {

    }

    @Override
    public void onItemClear(Context context) {

    }
  }

  public interface OnStartDragListener {

    void onStartDrag(RecyclerView.ViewHolder viewHolder);
  }
}
