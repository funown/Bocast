package per.funown.bocast.library.net;

import android.util.Log;

import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient.Builder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import per.funown.bocast.library.BuildConfig;
import per.funown.bocast.library.net.service.ItunesApiService;
import retrofit2.Retrofit;
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

  private Retrofit retrofit;
  private Retrofit rssRetrofit;
  private OkHttpClient.Builder builder;

  private volatile static NetworkState networkState;
  private static ItunesApiService itunesApiService;

  private static final int DEFAULT_TIME_OUT = 10;

  private NetManager() {
    builder = new OkHttpClient.Builder()
        .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS).addInterceptor(new NetInterceptor());
    if (BuildConfig.DEBUG) {
      builder.addInterceptor(new HttpLoggingInterceptor(s -> Log.d(TAG, s)));
    }
    retrofit = new Retrofit.Builder()
        .client(builder.build())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(ApiConfig.ITUNES_BASE_URL)
        .build();
    rssRetrofit = new Retrofit.Builder().baseUrl(ApiConfig.ITUNES_BASE_URL).client(builder.build())
        .build();
    itunesApiService = retrofit.create(ItunesApiService.class);
    networkState = NetworkState.LOADED;
  }

  public Builder getBuilder() {
    return builder;
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

  public Retrofit getRssRetrofit() {
    return rssRetrofit;
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
}
