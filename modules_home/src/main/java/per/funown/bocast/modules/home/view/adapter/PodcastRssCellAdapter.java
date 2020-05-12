package per.funown.bocast.modules.home.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.android.arouter.launcher.ARouter;
import java.util.ArrayList;
import java.util.List;
import per.funown.bocast.library.base.BaseRecyclerViewAdapter;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.entity.Podcast;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.modules.home.R;
import per.funown.bocast.modules.home.model.SubscribedPodcastViewModel;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastRssCellAdapter extends BaseRecyclerViewAdapter<PodcastCellViewHolder> {

  List<SubscribedPodcast> subscribedPodcasts = new ArrayList<>();
  SubscribedPodcastViewModel subscribedPodcastViewModel;
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
      List<SubscribedPodcast> subscribedPodcasts, SubscribedPodcastViewModel subscribedPodcastViewModel) {
    this.subscribedPodcasts = subscribedPodcasts;
    this.subscribedPodcastViewModel = subscribedPodcastViewModel;
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
    SubscribedPodcast subscribedPodcast = subscribedPodcasts.get(position);
    Podcast podcast = subscribedPodcastViewModel.getPodcast(subscribedPodcast.getPodcastId());
    if (podcast != null) {
      holder.getPodcastLogo().setImageURI(Uri.parse(podcast.getLogoLink()));
      holder.getPodcastTitle().setText(podcast.getTitle());
      holder.getPodcastAuthor().setText(podcast.getAuthor());
      holder.getPodcastCard().setOnClickListener(v -> {
        Fragment podcastFragment = (Fragment) ARouter.getInstance()
            .build(ArouterConstant.FRAGMENT_PODCAST_DETAIL)
            .withSerializable("rssLink", podcast.getRssLink()).withBoolean("isSubscribed", true)
            .navigation();
        FragmentTransitionUtil.getINSTANCE().transit(podcastFragment, containerId);
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(containerId, podcastFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
      });
    }

    if (holder.getPodcastTitle().getLineCount() >= 2) {
      holder.getPodcastTitle().setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
    }
  }

  @Override
  public int getItemCount() {
    return subscribedPodcasts.size();
  }
}
