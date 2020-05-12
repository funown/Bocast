package per.funown.bocast.library.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Entity
public class HistoryItem implements Serializable {

  private static final long serialVersionUID = -917402937706644024L;
  @PrimaryKey(autoGenerate = true)
  private long id;
  private long episodeId;
  private float percent;
  private float total;
  private Date date;

  public HistoryItem() {
  }

  public float getTotal() {
    return total;
  }

  public void setTotal(float total) {
    this.total = total;
  }

  public long getEpisodeId() {
    return episodeId;
  }

  public void setEpisodeId(long episodeId) {
    this.episodeId = episodeId;
  }



  public float getPercent() {
    return percent;
  }

  public void setPercent(float percent) {
    this.percent = percent;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

}
