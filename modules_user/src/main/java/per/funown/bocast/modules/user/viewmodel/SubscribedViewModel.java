package per.funown.bocast.modules.user.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import per.funown.bocast.library.entity.Podcast;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.library.repo.PodcastRepository;
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
  private PodcastRepository podcastRepository;

  public SubscribedViewModel(@NonNull Application application) {
    super(application);
    subscribedPodcastRepository = new SubscribedPodcastRepository(application);
    podcastRepository = new PodcastRepository(application);
  }

  public LiveData<List<SubscribedPodcast>> getSubscribedPodcastList() {
    return subscribedPodcastRepository.getAllPodcasts();
  }

  public SubscribedPodcastRepository getSubscribedPodcastRepository() {
    return subscribedPodcastRepository;
  }

  public Podcast getPodcast(long podcastId) {
    return podcastRepository.getPodcastById(podcastId);
  }

  public SubscribedPodcast getSubscribedPodcast(long podcastId) {
    return subscribedPodcastRepository.getPodcast(podcastId);
  }

  public void subscribe(Podcast podcast) {
    podcastRepository.addPodcast(podcast);
    subscribedPodcastRepository.subscribe(podcast);
  }

  public void unsubscribe(SubscribedPodcast podcast) {
    podcastRepository.deletePodcast(podcastRepository.getPodcastById(podcast.getPodcastId()));
    subscribedPodcastRepository.unsubscribe(podcast);
  }
}
