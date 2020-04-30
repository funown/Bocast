package per.funown.bocast.library.model;

import java.io.Serializable;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class iTunesLookupResult implements Serializable {

  private static final long serialVersionUID = -5651460372885052921L;
  private int resultCount;
  private ItunesResponseEntity[] results;

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public int getResultCount() {
    return resultCount;
  }

  public void setResultCount(int resultCount) {
    this.resultCount = resultCount;
  }

  public ItunesResponseEntity[] getResults() {
    return results;
  }

  public void setResults(ItunesResponseEntity[] results) {
    this.results = results;
  }
}
