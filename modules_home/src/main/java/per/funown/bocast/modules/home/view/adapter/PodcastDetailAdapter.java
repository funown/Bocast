package per.funown.bocast.modules.home.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.modules.home.R;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PodcastDetailAdapter extends ListAdapter<RssFeed, PodcastDetailViewHolder> {


  protected PodcastDetailAdapter(
      @NonNull DiffCallBack diffCallback) {
    super(diffCallback);
  }

  @NonNull
  @Override
  public PodcastDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_podcast_detail, parent, false);
    return new PodcastDetailViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull PodcastDetailViewHolder holder, int position) {

  }

  public class DiffCallBack extends DiffUtil.ItemCallback<RssFeed> {

    @Override
    public boolean areItemsTheSame(@NonNull RssFeed oldItem, @NonNull RssFeed newItem) {
      return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull RssFeed oldItem, @NonNull RssFeed newItem) {
      return oldItem.getChannel().getAtomLink().getHref()
          .equals(newItem.getChannel().getAtomLink().getHref());
    }
  }
}

class PodcastDetailViewHolder extends ViewHolder {

  public PodcastDetailViewHolder(@NonNull View itemView) {
    super(itemView);
  }
}
