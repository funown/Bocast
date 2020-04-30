package per.funown.bocast.library.model;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ItunesSearchResultList implements Serializable {

  private static final long serialVersionUID = -5305716660462829096L;
  private int resultCount;
  private List<ItunesResponseEntity> results;

  public int getResultCount() {
    return resultCount;
  }

  public void setResultCount(int resultCount) {
    this.resultCount = resultCount;
  }

  public List<ItunesResponseEntity> getResults() {
    return results;
  }

  public void setResults(List<ItunesResponseEntity> results) {
    this.results = results;
  }
}
