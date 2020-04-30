
package per.funown.bocast.library.utils;

import android.content.Context;

import android.net.Network;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

/**
 * Generic reusable network methods.
 */
public class NetworkHelper {

  /**
   * @param context to use to check for network connectivity.
   * @return true if connected, false otherwise.
   */
  public static boolean isOnline(Context context) {
    ConnectivityManager connMgr = (ConnectivityManager)
        context.getSystemService(Context.CONNECTIVITY_SERVICE);
    Network activeNetwork = connMgr.getActiveNetwork();
    NetworkCapabilities capabilities = connMgr.getNetworkCapabilities(activeNetwork);
    return (capabilities != null && capabilities
        .hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities
        .hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED));
  }
}