package per.funown.bocast.modules.discover.viewModel;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import per.funown.bocast.library.model.ItunesSearchResultList;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.ItunesResponseEntity;
import per.funown.bocast.library.net.NetManager;
import per.funown.bocast.library.net.service.ItunesApiService;
import per.funown.bocast.library.net.service.iTunesSearchService;
import per.funown.bocast.library.utils.RssFetchUtils;
import retrofit2.Response;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DiscoverViewModel extends AndroidViewModel {

  private static final String TAG = DiscoverViewModel.class.getSimpleName();
  private MutableLiveData<List<ItunesResponseEntity>> itunesResponseEntityList = new MutableLiveData<>();
  private MutableLiveData<RssFeed> rssSearch = new MutableLiveData<>();
  Context context;

  public LiveData<List<ItunesResponseEntity>> getItunesResponseEntityList() {
    return itunesResponseEntityList;
  }

  public LiveData<RssFeed> getRssSearch() {
    return rssSearch;
  }

  public DiscoverViewModel(@NonNull Application application) {
    super(application);
    context = application.getApplicationContext();
  }

  public void SearchRss(String url) {
    RssFeed feed = null;
    feed = RssFetchUtils.fetchRss(url);
    if (feed != null) {
      rssSearch.setValue(feed);
    } else {
      Toast.makeText(context, "Search fail :" + url, Toast.LENGTH_LONG);
    }
  }

  public void SearchTerms(String terms) {
    List<ItunesResponseEntity> entities = iTunesSearchService.SearchTerms(terms);
    if (entities != null) {
      itunesResponseEntityList.setValue(entities);
    }
  }

  public void clearSearch() {
    itunesResponseEntityList.setValue(new ArrayList<>());
    rssSearch.setValue(null);
  }

}
