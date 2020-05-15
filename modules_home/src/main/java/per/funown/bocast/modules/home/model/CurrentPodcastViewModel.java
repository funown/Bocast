package per.funown.bocast.modules.home.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.Transformations;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import per.funown.bocast.library.AppExecutors;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.RssItem;
import per.funown.bocast.library.net.NetworkState;
import per.funown.bocast.library.utils.LiveDataUtils;
import per.funown.bocast.library.utils.RssFetchUtils;

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
  private MutableLiveData<NetworkState> networkStatus = new MutableLiveData<>();
  private Context context;

  public CurrentPodcastViewModel(@NonNull Application application) {
    super(application);
  }

  public void setCurrentPodcast(RssFeed feed) {
    this.currentPodcast.postValue(feed);
  }

  public void setNetworkStatus(
      NetworkState networkStatus) {
    this.networkStatus.postValue(networkStatus);
  }

  public LiveData<NetworkState> getNetworkStatus() {
    return networkStatus;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public LiveData<RssFeed> getCurrentPodcast() {
    return currentPodcast;
  }

}
