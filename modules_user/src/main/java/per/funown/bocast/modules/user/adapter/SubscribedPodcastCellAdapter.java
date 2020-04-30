package per.funown.bocast.modules.user.adapter;

import androidx.fragment.app.Fragment;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.alibaba.android.arouter.launcher.ARouter;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.library.repo.SubscribedPodcastRepository;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.modules.user.R;
import per.funown.bocast.modules.user.adapter.SubscribedPodcastCellAdapter.SubscribedPodcastCellHolder;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SubscribedPodcastCellAdapter extends Adapter<SubscribedPodcastCellHolder> {

  private static final String TAG = SubscribedPodcastCellAdapter.class.getSimpleName();
  private SubscribedPodcastRepository repository;
  private List<SubscribedPodcast> podcasts;
  private int containerId;

  public void setPodcasts(List<SubscribedPodcast> podcasts) {
    this.podcasts = podcasts;
  }

  public void setRepository(SubscribedPodcastRepository repository) {
    this.repository = repository;
  }

  public void setContainerId(int containerId) {
    this.containerId = containerId;
  }

  @NonNull
  @Override
  public SubscribedPodcastCellHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View itemView = inflater.inflate(R.layout.cell_subscribed_podcast, parent, false);
    return new SubscribedPodcastCellHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull SubscribedPodcastCellHolder holder, int position) {
    SubscribedPodcast podcast = podcasts.get(position);
    holder.author.setText(podcast.getAuthor());
    holder.podcastTitle.setText(podcast.getTitle());
    holder.logo.setImageURI(podcast.getLogoLink());
    holder.itemView.findViewById(R.id.to_detail).setOnClickListener(v -> {
      Fragment podcastFragment = (Fragment) ARouter.getInstance()
          .build(ArouterConstant.FRAGMENT_PODCAST_DETAIL)
          .withBoolean("isSubscribed", true)
          .withString("rssLink", podcast.getRssLink())
          .navigation();
      FragmentTransitionUtil.getINSTANCE().transit(podcastFragment, containerId);
    });
    holder.itemView.findViewById(R.id.delete).setOnClickListener(v -> {
      repository.unsubscribe(podcast);
    });
  }

  @Override
  public int getItemCount() {
    return podcasts.size();
  }

  public class SubscribedPodcastCellHolder extends ViewHolder {

    private SimpleDraweeView logo;
    private TextView podcastTitle, author;

    public SubscribedPodcastCellHolder(@NonNull View itemView) {
      super(itemView);
      logo = itemView.findViewById(R.id.Podcast_logo);
      podcastTitle = itemView.findViewById(R.id.podcast);
      author = itemView.findViewById(R.id.author);
    }
  }

}
