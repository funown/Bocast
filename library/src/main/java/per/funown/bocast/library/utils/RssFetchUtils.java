package per.funown.bocast.library.utils;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import per.funown.bocast.library.model.AtomLink;
import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.net.NetManager;
import per.funown.bocast.library.net.service.RssService;
import retrofit2.Call;
import retrofit2.Response;

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
    return getRssFeed(url);
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

  /**
   * 获取RSS资源
   * @param url RSS链接
   * @return RssFeed
   */
  private static RssFeed getRssFeed(String url) {
    RssFeed feed = null;
    // 将链接拆分为尾部路径和主体网址（如“https://anyway.fm/rss.xml”拆分为“https://anyway.fm/”和“rss.xml"）
    int i = url.lastIndexOf("/");
    String endpoint = url.substring(i + 1);
    String baseUrl = url.substring(0, i + 1);

    // 建立网络连接
    RssService rssService = NetManager.getInstance().getRssService(baseUrl);
    Call<RssFeed> call = rssService.getFeed(endpoint);

    // 发出请求
    try {
      Response<RssFeed> response = call.execute();
      // 请求成功
      if (response.isSuccessful()) {
        feed = response.body();
        // 设置feed中的AtomLink，若为空则将传入参数url赋值给feed
        if (feed.getChannel().getAtomLink() == null) {
          AtomLink atomLink = new AtomLink();
          atomLink.setHref(url);
          atomLink.setType("application/rss+xml");
          feed.getChannel().setAtomLink(atomLink);
        }
      } else {
        Log.i(TAG, "Feed fetched error: " + url + " - " + response.message());
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return feed;
  }

  static class RssFetchAsyncTask extends AsyncTask<String, Void, RssFeed> {

    @Override
    protected RssFeed doInBackground(String... strings) {
      return getRssFeed(strings[0]);
    }

    @Override
    protected void onPostExecute(RssFeed rssFeed) {
      super.onPostExecute(rssFeed);
    }
  }
}
