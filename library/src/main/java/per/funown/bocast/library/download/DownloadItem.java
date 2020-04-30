package per.funown.bocast.library.download;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DownloadItem {

  String url;
  String filename;
  DownloadStatus status;

  public enum DownloadStatus {
    DOWNLOADING, ERROR, FINISHED, PAUSE
  }

  public DownloadItem(String url, String filename) {
    this.url = url;
    this.filename = filename;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public DownloadStatus getStatus() {
    return status;
  }

  public void setStatus(DownloadStatus status) {
    this.status = status;
  }
}
