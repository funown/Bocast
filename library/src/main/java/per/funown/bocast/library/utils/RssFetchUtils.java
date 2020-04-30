package per.funown.bocast.library.utils;

import android.os.AsyncTask;
import android.util.Log;
import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import okhttp3.OkHttpClient.Builder;
import per.funown.bocast.library.model.AtomLink;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.net.NetManager;
import per.funown.bocast.library.service.RssService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RssFetchUtils {

  private static final String TAG = RssFetchUtils.class.getSimpleName();

  public synchronized static RssFeed getFeed(String url) {
    RssFeed feed = new RssFeed();
    int i = url.lastIndexOf("/");
    String endpoint = url.substring(i + 1);
    String baseUrl = url.substring(0, i + 1);

    feed = getRssFeed(url, feed, endpoint, baseUrl);
    return feed;
  }

  public static RssFeed fetchRss(String url) {
    try {
      return new RssFetchAsyncTask().execute(url).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static RssFeed getRssFeed(String url, RssFeed feed, String endpoint, String baseUrl) {
    Builder builder = NetManager.getInstance().getBuilder();
    TikXml build = new TikXml.Builder().exceptionOnUnreadXml(false).build();
    Retrofit retrofit = new Retrofit.Builder().client(builder.build()).baseUrl(baseUrl)
        .addConverterFactory(TikXmlConverterFactory.create(build))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
    RssService rssService = retrofit.create(RssService.class);
    Call<RssFeed> call = rssService.getFeed(endpoint);
    try {
      Response<RssFeed> response = call.execute();
      if (response.isSuccessful()) {
        feed = response.body();
        if (feed.getChannel().getAtomLink() == null) {
          AtomLink atomLink = new AtomLink();
          atomLink.setHref(url);
          atomLink.setType("application/rss+xml");
          feed.getChannel().setAtomLink(atomLink);
        }
      } else {
        Log.i(TAG, "Feed fetched error: " + url + " - " + response.message());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return feed;
  }

  static class RssFetchAsyncTask extends AsyncTask<String, Void, RssFeed> {

    @Override
    protected RssFeed doInBackground(String... strings) {
      String url = strings[0];
      RssFeed feed = new RssFeed();
      int i = url.lastIndexOf("/");
      String endpoint = url.substring(i + 1);
      String baseUrl = url.substring(0, i + 1);

      feed = getRssFeed(url, feed, endpoint, baseUrl);

      return feed;
    }

    @Override
    protected void onPostExecute(RssFeed rssFeed) {
      super.onPostExecute(rssFeed);
    }
  }
}
