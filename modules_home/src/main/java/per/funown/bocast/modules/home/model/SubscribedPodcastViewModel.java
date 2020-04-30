package per.funown.bocast.modules.home.model;

import java.util.List;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;

import per.funown.bocast.library.model.AtomLink;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.entity.SubscribedPodcast;
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

  public SubscribedPodcastViewModel(@NonNull Application application) {
    super(application);
    repository = new SubscribedPodcastRepository(application);
  }

  public LiveData<List<SubscribedPodcast>> getAllPodcasts() {
    return repository.getAllPodcasts();
  }

  public void subscribe(SubscribedPodcast podcast) {
    repository.subscribe(podcast);
  }

  public void unsubscribe(SubscribedPodcast podcast) {
    repository.unsubscribe(podcast);
  }

  public boolean isSubscribed(RssFeed item) {
    LiveData<List<SubscribedPodcast>> allPodcasts = getAllPodcasts();
    List<SubscribedPodcast> value = allPodcasts.getValue();
    if (value != null && value.size() > 0) {
      for (SubscribedPodcast podcast : value) {
        AtomLink atomLink = item.getChannel().getAtomLink();
        if (atomLink != null) {
          if (podcast.getRssLink().equals(atomLink.getHref())) {
            return true;
          }
        }
        else if (podcast.getTitle().equals(item.getChannel().getTitle())) {
          return true;
        }
      }
    }
    return false;
  }
}
