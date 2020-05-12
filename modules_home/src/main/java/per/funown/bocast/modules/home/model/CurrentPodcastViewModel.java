package per.funown.bocast.modules.home.model;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssItem;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CurrentPodcastViewModel extends AndroidViewModel implements LifecycleObserver {

  private static final String TAG = CurrentPodcastViewModel.class.getSimpleName();
  private MutableLiveData<RssFeed> currentPodcast = new MutableLiveData<>();
//  private EpisodesViewModel repository;


  public CurrentPodcastViewModel(@NonNull Application application) {
    super(application);
//    repository = new EpisodesViewModel(application.getApplicationContext());
  }

  public void setCurrentPodcast(RssFeed feed) {
    this.currentPodcast.postValue(feed);
  }


  public LiveData<RssFeed> getCurrentPodcast() {
    return currentPodcast;
  }

}
