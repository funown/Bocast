package per.funown.bocast.modules.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.snackbar.Snackbar;
import com.lzx.starrysky.control.PlayerControl;
import com.lzx.starrysky.provider.SongInfo;
import java.util.Collections;
import java.util.List;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.entity.Episode;
import per.funown.bocast.library.entity.Podcast;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssItem;
import per.funown.bocast.library.entity.HistoryItem;
import per.funown.bocast.library.net.RssCacheUtil;
import per.funown.bocast.library.service.MusicService;
import per.funown.bocast.library.utils.DateUtils;
import per.funown.bocast.library.utils.DateUtils.DatePattern;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.library.utils.ItemTouchHelperAdapter;
import per.funown.bocast.library.utils.ItemTouchHelperViewHolder;
import per.funown.bocast.library.utils.RssFetchUtils;
import per.funown.bocast.library.utils.ScreenUtil;
import per.funown.bocast.modules.user.R;
import per.funown.bocast.modules.user.adapter.HistoryCellAdapter.HistoryItemViewHolder;
import per.funown.bocast.modules.user.viewmodel.HistoryViewModel;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class HistoryCellAdapter extends Adapter<HistoryItemViewHolder> implements
    ItemTouchHelperAdapter {

  private static final String TAG = HistoryCellAdapter.class.getSimpleName();
  private List<HistoryItem> items;
  private int containerId;
  MusicService service;
  PlayerControl instance;
  Activity activity;
  private final Context context;
  private HistoryViewModel viewModel;
  private View panel;

  private final OnStartDragListener dragStartListener;
  boolean isPlaying;

  public HistoryCellAdapter(Context context, OnStartDragListener onStartDragListener, View panel, HistoryViewModel viewModel) {
    this.context = context;
    this.dragStartListener = onStartDragListener;
    this.panel = panel;
    this.viewModel = viewModel;
  }

  public void setItems(List<HistoryItem> items) {
    this.items = items;
  }

  public void setContainerId(int containerId) {
    this.containerId = containerId;
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
  }

  @NonNull
  @Override
  public HistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View itemView = inflater.inflate(R.layout.cell_history, parent, false);
    service = ARouter.getInstance().navigation(MusicService.class);
    instance = service.getINSTANCE();
    return new HistoryItemViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull HistoryItemViewHolder holder, int position) {
    HistoryItem historyItem = items.get(position);
    Episode episode = viewModel.getEpisode(historyItem.getEpisodeId());
    Podcast podcast = viewModel.getPodcast(episode.getPodcastId());
    isPlaying = instance.isCurrMusicIsPlaying(String.valueOf(historyItem.getEpisodeId()));

    holder.episodeTitle.setText(episode.getTitle());
    holder.date.setText(DateUtils.dateToString(historyItem.getDate(), DatePattern.RSS_DATE));
    float percent = (historyItem.getPercent() / historyItem.getTotal()) * 100;
    holder.percent.setText(String.format("%.1f", percent) + "%");
    holder.logo.setImageURI(Uri.parse(episode.getImage()));
    if (isPlaying) {
      holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_pause));
    }
    holder.itemView.findViewById(R.id.to_detail).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = (Fragment) ARouter.getInstance()
            .build(ArouterConstant.FRAGMENT_PODCAST_EPISODE_DETAIL)
            .withString("feed", podcast.getRssLink())
            .withString("guid", episode.getGuid()).navigation();
        FragmentTransitionUtil.getINSTANCE().transit(fragment, containerId);
      }
    });
    holder.btn_play.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!isPlaying) {
          List<SongInfo> playList = instance.getPlayList();
          for (SongInfo info : playList) {
            if (info.getSongId().equals(String.valueOf(historyItem.getEpisodeId()))) {
              instance.playMusicById(info.getSongId());
              holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_pause));
              notifyDataSetChanged();
              return;
            }
          }

          RssFeed feed = RssCacheUtil.getFeed(podcast.getRssLink());
          if (feed == null) {
            feed = RssFetchUtils.fetchRss(podcast.getRssLink());
          }
          if (feed == null) {
            Toast.makeText(activity.getApplicationContext(), "Data fetch error", Toast.LENGTH_LONG)
                .show();
            return;
          }
          List<RssItem> items = feed.getChannel().getItems();
          for (RssItem item : items) {
            if (item.getGuid().getGuid().equals(historyItem.getEpisodeId())) {
              SongInfo songInfo = new SongInfo();
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
              service.addSong(songInfo);
              instance.playMusicByInfo(songInfo);
              holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_pause));
              notifyDataSetChanged();
              return;
            }
          }
        } else {
          holder.btn_play.setImageDrawable(activity.getDrawable(R.drawable.ic_play));
          instance.pauseMusic();
          notifyDataSetChanged();
        }
      }
    });

    holder.frame.setOnTouchListener((v, event) -> {
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
        dragStartListener.onStartDrag(holder);
      }
      return false;
    });
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public void addItem(int position, HistoryItem item) {
    items.add(position, item);
    notifyItemInserted(position);
  }

  @Override
  public void onItemMove(int fromPosition, int toPosition) {
    if (fromPosition < toPosition) {
      for (int i = fromPosition; i < toPosition; i++) {
        Collections.swap(items, i, i + 1);
      }
    } else {
      for (int i = fromPosition; i > toPosition; i--) {
        Collections.swap(items, i, i - 1);
      }
    }
    notifyItemMoved(fromPosition, toPosition);
  }

  @Override
  public void onItemDismiss(int adapterPosition) {
    final HistoryItem item = new HistoryItem();
    HistoryItem historyItem = items.get(adapterPosition);
    item.setId(historyItem.getId());
    item.setTotal(historyItem.getTotal());
    item.setPercent(historyItem.getPercent());
    item.setDate(historyItem.getDate());
    item.setEpisodeId(historyItem.getEpisodeId());

    notifyItemRemoved(adapterPosition);
    items.remove(adapterPosition);
    viewModel.deleteHistory(historyItem);
    notifyItemRangeChanged(0, getItemCount());
    final Snackbar snackbar = Snackbar
        .make(panel, context.getResources().getString(R.string.item_deleted),
            Snackbar.LENGTH_LONG)
        .setActionTextColor(ContextCompat.getColor(context, R.color.white))
        .setAction(context.getResources().getString(R.string.item_undo), new OnClickListener() {
          @Override
          public void onClick(View v) {
            items.add(adapterPosition, item);
            notifyItemInserted(adapterPosition);
            viewModel.addHistory(historyItem);

          }
        });
    View snackbarView = snackbar.getView();
    snackbarView.offsetTopAndBottom(- (int) ScreenUtil.dpToPx(50));
    snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
    TextView snackBarText = (TextView) snackbarView.findViewById(R.id.snackbar_text);
    TextView snackBarAction = (TextView) snackbar.getView().findViewById(R.id.snackbar_action);
    snackBarText.setTextColor(Color.WHITE);
    snackbar.show();

    Runnable runnableUndo = new Runnable() {

      @Override
      public void run() {
        snackbar.dismiss();
      }
    };
    Handler handlerUndo = new Handler();
    handlerUndo.postDelayed(runnableUndo, 2500);
  }

  public class HistoryItemViewHolder extends ViewHolder implements ItemTouchHelperViewHolder{

    private SimpleDraweeView logo;
    private TextView episodeTitle, date, percent;
    private ImageView btn_play;
    private ConstraintLayout frame;

    public HistoryItemViewHolder(@NonNull View itemView) {
      super(itemView);
      logo = itemView.findViewById(R.id.logo);
      episodeTitle = itemView.findViewById(R.id.episode);
      date = itemView.findViewById(R.id.date);
      percent = itemView.findViewById(R.id.percent);
      btn_play = itemView.findViewById(R.id.btn_play);
      frame = itemView.findViewById(R.id.cell_history);
    }

    @Override
    public void onItemSelected(Context context) {
      itemView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
    }

    @Override
    public void onItemClear(Context context) {

    }
  }

  public interface OnStartDragListener {

    void onStartDrag(RecyclerView.ViewHolder viewHolder);
  }

}
