package per.funown.bocast.modules.user.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.facebook.drawee.view.SimpleDraweeView;
import com.liulishuo.okdownload.DownloadTask;
import java.util.List;
import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.download.BaseDownloadListener;
import per.funown.bocast.library.download.DownloadFactory;
import per.funown.bocast.library.download.DownloadStatus;
import per.funown.bocast.library.repo.DownloadedEpisodeRepository;
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
public class DownloadCellAdapter extends Adapter<DownloadCellHolder> {

  private static final String TAG = DownloadCellAdapter.class.getSimpleName();
  private List<DownloadTask> tasks;
  private DownloadedEpisodeRepository repository;
  boolean isDownloading;
  DownloadFactory instance;
  Context context;

  public DownloadCellAdapter(Context context) {
    this.context = context;
    repository = new DownloadedEpisodeRepository(context);
  }

  public void setTasks(List<DownloadTask> tasks) {
    this.tasks = tasks;
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
    DownloadEpisode item = (DownloadEpisode) task.getTag();

    isDownloading = item.getDownloadStatus().equals(DownloadStatus.DOWNLOADING);
    holder.podcastLogo.setImageURI(item.getImageUri());
    holder.episodeTitle.setText(item.getEpisodeTitle());
    holder.author.setText(item.getPodcast());

    BaseDownloadListener baseDownloadListener = new BaseDownloadListener(item, context,
        holder.progressBar, holder.btn_download);
    instance.changeListener(task, baseDownloadListener);
    holder.progressBar.setOnClickListener(v -> {
      Log.e(TAG, baseDownloadListener.getItem().getStatus());
      if (baseDownloadListener.getItem().getStatus().equals(DownloadStatus.DOWNLOADING)) {
        instance.stop(task);
      } else if (baseDownloadListener.getItem().getStatus().equals(DownloadStatus.PAUSE)) {
        instance.restart(task.getUrl(), baseDownloadListener);
      }
    });
  }

  @Override
  public int getItemCount() {
    return tasks.size();
  }


  class DownloadCellHolder extends ViewHolder {

    private SimpleDraweeView podcastLogo;
    private TextView episodeTitle, author;
    private ProgressBar progressBar;
    private ImageView btn_download;

    public DownloadCellHolder(@NonNull View itemView) {
      super(itemView);
      podcastLogo = itemView.findViewById(R.id.Podcast_logo);
      episodeTitle = itemView.findViewById(R.id.episode_title);
      author = itemView.findViewById(R.id.author);
      progressBar = itemView.findViewById(R.id.progressBar);
      btn_download = itemView.findViewById(R.id.btn_download);
    }
  }
}
