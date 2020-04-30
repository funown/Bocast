package per.funown.bocast.modules.user.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.liulishuo.okdownload.DownloadTask;
import java.util.List;
import per.funown.bocast.library.entity.DownloadEpisode;
import per.funown.bocast.library.download.BaseDownloadListener;
import per.funown.bocast.library.download.DownloadFactory;
import per.funown.bocast.library.download.DownloadStatus;
import per.funown.bocast.modules.user.data.DownloadEpisodeRepository;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DownloadViewModel extends AndroidViewModel {

  private DownloadFactory factory;
  private DownloadEpisodeRepository repository;
  private MutableLiveData<List<DownloadTask>> tasks;
  private LiveData<List<DownloadEpisode>> downloadEpisodes;

  public MutableLiveData<List<DownloadTask>> getTasks() {
    return tasks;
  }

  public DownloadViewModel(@NonNull Application application) {
    super(application);
    factory = DownloadFactory.getINSTANCE(application);
    repository = new DownloadEpisodeRepository(application);
    downloadEpisodes = repository.getEpisodes();

    if (downloadEpisodes != null && downloadEpisodes.getValue() != null) {
      for (DownloadEpisode episode : downloadEpisodes.getValue()) {
        if (episode.getStatus().equals(DownloadStatus.PAUSE) || episode.getStatus()
            .equals(DownloadStatus.DOWNLOADING)) {
          factory.addTask(episode.getUrl(), episode.getFilename(), episode,
              new BaseDownloadListener());
        }
      }
    }
    tasks = new MutableLiveData<>(factory.getTasks());
  }

}
