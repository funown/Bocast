package per.funown.bocast.library.dataSource;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.net.NetworkState;
import per.funown.bocast.library.utils.RssFetchUtils;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/05/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RssDataSource {

  private static final String TAG = RssDataSource.class.getSimpleName();
  private Context context;

  private MutableLiveData<RssFeed> currentPodcast = new MutableLiveData<>();
  private MutableLiveData<NetworkState> networkStatus = new MutableLiveData<>();

  public RssDataSource(Context context) {
    this.context = context;
  }

  public LiveData<RssFeed> getCurrentPodcast() {
    return currentPodcast;
  }

  public LiveData<NetworkState> getNetworkStatus() {
    return networkStatus;
  }

  @SuppressLint("CheckResult")
  public void loadRss(String rss) {
    networkStatus.postValue(NetworkState.LOADING);
    Observable.timer(3, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(aLong -> {
          RssFeed feed = RssFetchUtils.getFeed(rss);
          Log.e(TAG, "feed: " + (feed == null));
          if (feed != null) {
            this.currentPodcast.postValue(feed);
            networkStatus.postValue(NetworkState.LOADED);
          } else {
            networkStatus.postValue(NetworkState.FAILED);
          }
        }, throwable -> {
          Log.e(TAG, "Throwable " + throwable.getMessage());
          networkStatus.postValue(NetworkState.FAILED);
        });
  }
}
