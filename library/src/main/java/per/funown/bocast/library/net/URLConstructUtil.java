package per.funown.bocast.library.net;

import android.net.Uri;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class URLConstructUtil {

  public static String buildTopUrl(String region, int limit, String genre) {
    // eg. https://itunes.apple.com/cn/rss/toppodcasts/limit=10/genre=1402/json
    return String
        .format("%s/%s/rss/toppodcasts/limit=%d/genre=%s/", ApiConfig.ITUNES_TOP_URL, region,
            limit, genre);
  }
}
