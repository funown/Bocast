package per.funown.bocast.modules.home.model;

import java.util.List;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;

import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.entity.Podcast;
import per.funown.bocast.library.entity.SubscribedPodcast;
import per.funown.bocast.library.repo.PodcastRepository;
import per.funown.bocast.library.repo.SubscribedPodcastRepository;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SubscribedPodcastViewModel extends AndroidViewModel {

  private SubscribedPodcastRepository repository;
  private PodcastRepository podcastRepository;

  public SubscribedPodcastViewModel(@NonNull Application application) {
    super(application);
    repository = new SubscribedPodcastRepository(application);
    podcastRepository = new PodcastRepository(application);
  }

  public LiveData<List<SubscribedPodcast>> getAllPodcasts() {
    return repository.getAllPodcasts();
  }

  public Podcast getPodcast(long podcastId) {
    return podcastRepository.getPodcastById(podcastId);
  }

  public SubscribedPodcast getSubscribedPodcast(long podcastId) {
    return repository.getPodcast(podcastId);
  }

  public void subscribe(Podcast podcast) {
    long podcastId = podcastRepository.addPodcast(podcast);
    podcast.setId(podcastId);
    repository.subscribe(podcast);
  }

  public void unsubscribe(SubscribedPodcast podcast) {
    podcastRepository.deletePodcast(podcastRepository.getPodcastById(podcast.getPodcastId()));
    repository.unsubscribe(podcast);
  }

  public long isSubscribed(RssFeed item) {
    Podcast podcast = podcastRepository
        .getPodcastByRss(item.getChannel().getAtomLink().getHref());
    if (podcast != null) {
      SubscribedPodcast subscribedPodcast = repository.getPodcast(podcast.getId());
      if (subscribedPodcast != null) {
        return subscribedPodcast.getPodcastId();
      }
    }
    return 0;
  }

  public long addPodcast(Podcast podcast) {
    return podcastRepository.addPodcast(podcast);
  }
}
