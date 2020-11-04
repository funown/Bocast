package per.funown.bocast.library.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Entity
public class SubscribedPodcast implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private long id;
  @ColumnInfo
  private long podcastId;
  private Date updateTime;
  private Date subscribedTime;

  public SubscribedPodcast(long podcastId, Date updateTime, Date subscribedTime) {
    this.podcastId = podcastId;
    this.updateTime = updateTime;
    this.subscribedTime = subscribedTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getPodcastId() {
    return podcastId;
  }

  public void setPodcastId(long podcastId) {
    this.podcastId = podcastId;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Date getSubscribedTime() {
    return subscribedTime;
  }

  public void setSubscribedTime(Date subscribedTime) {
    this.subscribedTime = subscribedTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubscribedPodcast that = (SubscribedPodcast) o;
    return id == that.id &&
        podcastId == that.podcastId &&
        Objects.equals(updateTime, that.updateTime) &&
        Objects.equals(subscribedTime, that.subscribedTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, podcastId, updateTime, subscribedTime);
  }
}
