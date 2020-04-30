package per.funown.bocast.modules.home.view.adapter;

import android.content.Context;
import android.util.TypedValue;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.List;
import java.util.ArrayList;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.alibaba.android.arouter.launcher.ARouter;

import per.funown.bocast.modules.home.R;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.library.base.BaseRecyclerViewAdapter;
import per.funown.bocast.modules.home.view.adapter.PodcastCellAdapter.PodcastCellViewHolder;

/**
 * <pre>
 *     author : Funown
 *     time   : 2020/02/19
 *     desc   : podcast Cell Adapter
 *     version: 1.0
 * </pre>
 */
public class PodcastCellAdapter extends BaseRecyclerViewAdapter<PodcastCellViewHolder> {

  private static final String TAG = PodcastCellAdapter.class.getSimpleName();
  List<SubscribedPodcast> subscribedPodcasts = new ArrayList<>();
  List<ItunesResponseEntity> entities = new ArrayList<>();

  FragmentManager manager;
  Context context;
  int containerId;

  public void setContext(Context context) {
    this.context = context;
  }

  public void setManager(FragmentManager manager) {
    this.manager = manager;
  }

  public void setContainerId(int containerId) {
    this.containerId = containerId;
  }

  public void setSubscribedPodcasts(
      List<SubscribedPodcast> subscribedPodcasts) {
    this.subscribedPodcasts = subscribedPodcasts;
    this.entities = new ArrayList<>();
  }

  public void setEntities(List<ItunesResponseEntity> entities) {
    this.subscribedPodcasts = new ArrayList<>();
    this.entities = entities;
  }

  @NonNull
  @Override
  public PodcastCellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View itemView = inflater.inflate(R.layout.cell_podcast, parent, false);
    PodcastCellViewHolder podcastCellViewHolder = new PodcastCellViewHolder(itemView);
    return podcastCellViewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull PodcastCellViewHolder holder, int position) {
    Log.i(TAG, "--> Binding podcast cell ...");
    if (subscribedPodcasts.size() != 0) {
      SubscribedPodcast podcast = subscribedPodcasts.get(position);
      holder.podcastLogo.setImageURI(Uri.parse(podcast.getLogoLink()));
      holder.podcastTitle.setText(podcast.getTitle());
      holder.podcastAuthor.setText(podcast.getAuthor());
      holder.rssLink = podcast.getRssLink();
      holder.isSubscribed = true;

    } else if (entities.size() != 0) {
      ItunesResponseEntity entity = entities.get(position);
      holder.podcastLogo.setImageURI(Uri.parse(entity.getArtworkUrl60()));
      holder.podcastTitle.setText(entity.getTrackName());
      holder.podcastAuthor.setText(entity.getArtistName());
      holder.rssLink = entity.getFeedUrl();
      holder.isSubscribed = false;
    }

    if (holder.podcastTitle.getLineCount() >= 2) {
      holder.podcastTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
    }

  }


  @Override
  public int getItemCount() {
    return subscribedPodcasts.size() > 0 ? subscribedPodcasts.size() : entities.size();
  }


  /**
   * <pre>
   *     author : Funown
   *     time   : 2020/02/19
   *     desc   : podcast cell view holder
   *     version: 1.0
   * </pre>
   */
  class PodcastCellViewHolder extends RecyclerView.ViewHolder {

    private SimpleDraweeView podcastLogo;
    private TextView podcastTitle, podcastAuthor;
    private String rssLink;
    private boolean isSubscribed;

    public PodcastCellViewHolder(@NonNull View itemView) {
      super(itemView);
      podcastLogo = itemView.findViewById(R.id.podcast_logo);
      podcastTitle = itemView.findViewById(R.id.podcast_title);
      podcastAuthor = itemView.findViewById(R.id.podcast_author);
      itemView.findViewById(R.id.podcast_card).setOnClickListener(v -> {
        Fragment podcastFragment = (Fragment) ARouter.getInstance()
            .build(ArouterConstant.FRAGMENT_PODCAST_DETAIL)
            .withSerializable("rssLink", rssLink).withBoolean("isSubscribed", isSubscribed)
            .navigation();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, podcastFragment);
        transaction.addToBackStack(null);
        transaction.commit();
      });
    }

    public void bind(final String item, final OnItemClickListener listener) {
      itemView.setOnClickListener(v -> listener.onItemClick(item));
    }
  }
}
