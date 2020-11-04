package per.funown.bocast.library.net;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class NetworkState {

  public enum Status {
    INITIAL_LOADING,
    LOADING,
    SUCCESS,
    FAILED,
  }

  public static final NetworkState INITIAL_LOADING = new NetworkState(Status.INITIAL_LOADING, "Initial");
  public static final NetworkState LOADED = new NetworkState(Status.SUCCESS, "Success");
  public static final NetworkState LOADING = new NetworkState(Status.LOADING, "Loading");
  public static final NetworkState FAILED = new NetworkState(Status.FAILED, "Loading");

  private Status status;
  private String msg;

  public NetworkState(Status status, String msg) {
    this.status = status;
    this.msg = msg;
  }

  public Status getStatus() {
    return status;
  }

  public String getMsg() {
    return msg;
  }

}
