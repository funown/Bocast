package per.funown.bocast.modules.discover.fragment.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import java.util.List;
import per.funown.bocast.library.constant.ArouterConstant;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.utils.FragmentTransitionUtil;
import per.funown.bocast.modules.discover.R;
import per.funown.bocast.modules.discover.fragment.adapter.SearchResultAdapter.SearchResultViewHolder;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {

  private static final String TAG = SearchResultAdapter.class.getSimpleName();
  List<ItunesResponseEntity> itunesRespones = new ArrayList<>();
  RssFeed response;
  private int containerId;

  public void setItunesRespones(
      List<ItunesResponseEntity> itunesRespones) {
    this.itunesRespones = itunesRespones;
    response = null;
  }

  public void setResponse(RssFeed response) {
    this.response = response;
    this.itunesRespones.clear();
  }

  public void setContainerId(int containerId) {
    this.containerId = containerId;
  }

  @NonNull
  @Override
  public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View itemView = inflater.inflate(R.layout.cell_search_result, parent, false);
    return new SearchResultViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {

    if (itunesRespones.size() != 0) {
      ItunesResponseEntity entity = itunesRespones.get(position);
      holder.podcastTitle.setText(entity.getTrackName());
      holder.podcastAuthor.setText(entity.getArtistName());
      holder.logo.setImageURI(Uri.parse(entity.getArtworkUrl60()));

      holder.itemView.setOnClickListener(v -> {
        Fragment toDetail = (Fragment) ARouter.getInstance()
            .build(ArouterConstant.FRAGMENT_PODCAST_DETAIL)
            .withString("rssLink", entity.getFeedUrl()).navigation();
        FragmentTransitionUtil.getINSTANCE().transit(toDetail);
      });
    } else if (response != null) {
      holder.logo.setImageURI(Uri.parse(response.getChannel().getImage().getHref()));
      holder.podcastTitle.setText(response.getChannel().getTitle());
      holder.podcastAuthor.setText(response.getChannel().getOwner().getName());
      holder.itemView.setOnClickListener(v -> {
        Fragment toDetail = (Fragment) ARouter.getInstance()
            .build(ArouterConstant.FRAGMENT_PODCAST_DETAIL)
            .withObject("feed", response).navigation();
        FragmentTransitionUtil.getINSTANCE().transit(toDetail);
      });
    }
  }

  @Override
  public int getItemCount() {
    return response == null ? itunesRespones.size() : 1;
  }

  class SearchResultViewHolder extends ViewHolder {

    private SimpleDraweeView logo;
    private TextView podcastTitle, podcastAuthor, description;

    public SearchResultViewHolder(@NonNull View itemView) {
      super(itemView);
      logo = itemView.findViewById(R.id.Podcat_Logo);
      podcastTitle = itemView.findViewById(R.id.podcast_title);
      podcastAuthor = itemView.findViewById(R.id.Podcast_author);
    }
  }

}
