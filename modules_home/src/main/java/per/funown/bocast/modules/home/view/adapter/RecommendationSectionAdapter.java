package per.funown.bocast.modules.home.view.adapter;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;

import java.util.Map;
import per.funown.bocast.library.model.ItunesResponseEntity;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import per.funown.bocast.modules.home.R;
import per.funown.bocast.library.entity.Genre;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RecommendationSectionAdapter extends
    RecyclerView.Adapter<RecommendationSectionAdapter.RecommendationSectionHolder> {

  private static final String TAG = RecommendationSectionAdapter.class.getSimpleName();
  private Map<Genre, MutableLiveData<List<ItunesResponseEntity>>> topPodcasts;

  private Resources resources;
  private LifecycleOwner owner;
  private Context context;
  private FragmentManager manager;
  private int containerId;

  public void setManager(FragmentManager manager) {
    this.manager = manager;
  }

  public void setContainerId(int containerId) {
    this.containerId = containerId;
  }

  public void setTopPodcasts(
      Map<Genre, MutableLiveData<List<ItunesResponseEntity>>> topPodcasts) {
    this.topPodcasts = topPodcasts;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public void setOwner(LifecycleOwner owner) {
    this.owner = owner;
  }

  public void setResources(Resources resources) {
    this.resources = resources;
  }

  @NonNull
  @Override
  public RecommendationSectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View itemView = inflater.inflate(R.layout.recommendation_section, parent, false);
    return new RecommendationSectionHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull RecommendationSectionHolder holder, int position) {
    Genre[] genres = topPodcasts.keySet().toArray(new Genre[topPodcasts.size()]);
    holder.title.setText("Hot podcasts in " + genres[position].getGenre());
    LinearLayoutManager layoutManager
        = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    holder.panel.setLayoutManager(layoutManager);
    topPodcasts.get(genres[position]).observe(owner, itunesResponseEntities -> {
      holder.adapter.setEntities(itunesResponseEntities);
      holder.adapter.notifyDataSetChanged();
    });
  }

  @Override
  public int getItemCount() {
    return topPodcasts == null ? 0 : topPodcasts.size();
  }

  class RecommendationSectionHolder extends ViewHolder {

    private TextView title;
    private RecyclerView panel;
    private PodcastCellAdapter adapter;

    public RecommendationSectionHolder(@NonNull View itemView) {
      super(itemView);
      title = itemView.findViewById(R.id.section_title);
      panel = itemView.findViewById(R.id.recommendation_podcast_panel);
      adapter = new PodcastCellAdapter();
      adapter.setManager(manager);
      adapter.setContext(context);
      adapter.setContainerId(containerId);
      panel.setAdapter(adapter);
    }
  }
}
