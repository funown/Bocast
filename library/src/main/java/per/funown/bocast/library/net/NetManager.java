package per.funown.bocast.library.net;

import android.os.Environment;
import android.util.Log;

import androidx.core.os.EnvironmentCompat;
import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;
import java.io.File;
import okhttp3.Cache;
import okhttp3.OkHttpClient.Builder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.simpleframework.xml.filter.EnvironmentFilter;
import per.funown.bocast.library.BuildConfig;
import per.funown.bocast.library.net.service.ItunesApiService;
import per.funown.bocast.library.net.service.iTunesRssTopPodcastService;
import per.funown.bocast.library.net.service.RssService;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class NetManager {

  public static final String TAG = NetManager.class.getSimpleName();
  private static NetManager Instance;
  private static Cache cache;

  private Retrofit retrofit;
  private OkHttpClient.Builder builder;

  private volatile static NetworkState networkState;
  private static ItunesApiService itunesApiService;

  private static final int DEFAULT_TIME_OUT = 10;

  public static void setCache(File file) {
    cache =  new Cache(file, 1024 * 1024 * 10); //10Mb;
  }

  public void setNetworkState(NetworkState networkState) {
    NetManager.networkState = networkState;
  }

  public NetworkState getNetworkState() {
    return networkState;
  }

  public ItunesApiService getItunesApiService() {
    return itunesApiService;
  }

  public Builder getBuilder() {
    return builder;
  }

  private NetManager() {

    builder = new OkHttpClient.Builder()
        .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
        .cache(cache)
        .addInterceptor(new NetInterceptor());
    if (BuildConfig.DEBUG) {
      builder.addInterceptor(new HttpLoggingInterceptor(s -> Log.d(TAG, s)));
    }
    retrofit = new Retrofit.Builder()
        .client(builder.build())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(ApiConfig.ITUNES_BASE_URL)
        .build();
    itunesApiService = retrofit.create(ItunesApiService.class);
    networkState = NetworkState.LOADED;
  }

  public static synchronized NetManager getInstance() {
    if (Instance == null) {
      Instance = new NetManager();
    }
    return Instance;
  }

  public Retrofit getRetrofit() {
    return retrofit;
  }

  public Retrofit getRssRetrofit(String baseUrl, Converter.Factory factory) {
    Retrofit rssRetrofit = new Retrofit.Builder().client(builder.build()).baseUrl(baseUrl)
        .addConverterFactory(factory)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
    return rssRetrofit;
  }

  public RssService getRssService(String baseUrl) {
    TikXml build = new TikXml.Builder().exceptionOnUnreadXml(false).build();
    Retrofit rssRetrofit = getRssRetrofit(baseUrl, TikXmlConverterFactory.create(build));
    RssService rssService = rssRetrofit.create(RssService.class);
    return rssService;
  }

  public iTunesRssTopPodcastService getITunesRssTopPodcastService(int limit, String genre) {
    Retrofit rssRetrofit = getRssRetrofit(URLConstructUtil.buildTopUrl("cn", limit, genre),
        GsonConverterFactory.create());
    return rssRetrofit.create(iTunesRssTopPodcastService.class);
  }
}
