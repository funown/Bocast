package per.funown.bocast.modules.user.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.library.repo.SubscribedPodcastRepository;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SubscribedViewModel extends AndroidViewModel {

  private SubscribedPodcastRepository subscribedPodcastRepository;

  public SubscribedViewModel(@NonNull Application application) {
    super(application);
    subscribedPodcastRepository = new SubscribedPodcastRepository(application);
  }

  public LiveData<List<SubscribedPodcast>> getSubscribedPodcastList() {
    return subscribedPodcastRepository.getAllPodcasts();
  }

  public SubscribedPodcastRepository getSubscribedPodcastRepository() {
    return subscribedPodcastRepository;
  }
}
