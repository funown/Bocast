package per.funown.bocast.library.net;

import java.io.File;

import android.util.Log;

import androidx.collection.LruCache;

import per.funown.bocast.library.model.RssFeed;
import per.funown.bocast.library.model.ItunesResponseEntity;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/25
 *     desc   : Cache Tool
 *     version: 1.0
 * </pre>
 */
public class RssCacheUtil {
  private static final String TAG = RssCacheUtil.class.getSimpleName();

  private static LruCache<String, ItunesResponseEntity> itunesResponseEntityLruCache;
  private static LruCache<String, RssFeed> feedLruCache;
  private static int ituneCacheSize;

  private static File cacheFile;

  static  {
    int maxMemory = (int) Runtime.getRuntime().maxMemory();
    ituneCacheSize = maxMemory / 8;
    if (itunesResponseEntityLruCache == null) {
      itunesResponseEntityLruCache = new LruCache<>(ituneCacheSize);
    }
    if (feedLruCache == null) {
      feedLruCache = new LruCache<>(ituneCacheSize);
    }
  }

  public static void cacheItuneResponse(ItunesResponseEntity entity) {
    if (entity != null && itunesResponseEntityLruCache.get(entity.getTrackName()) == null) {
      itunesResponseEntityLruCache.put(entity.getCollectionId(), entity);
    }
  }

  public static ItunesResponseEntity getCacheItuneResponse(String key) {
    Log.d(TAG, String.format("Rss Cache Size: %i", itunesResponseEntityLruCache.size()));
    return itunesResponseEntityLruCache.get(key);
  }

  public static void cacheFeed(String url, RssFeed feed) {
    if (feed != null ) {
      feedLruCache.put(url, feed);
    }
  }

  public static RssFeed getFeed(String url) {
    return feedLruCache.get(url);
  }

  public static void removeCacheItuneResponse(String key) {
    itunesResponseEntityLruCache.remove(key);
  }

  public static void initCacheFile(File file) {
    if (!file.exists()) {
      file.mkdirs();
    }
    cacheFile = file;
  }
}
