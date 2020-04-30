package per.funown.bocast.library.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
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
public class HistoryItem {

  @PrimaryKey(autoGenerate = true)
  private long id;
  private String podcast;
  private String episodeId;
  private String episode;
  private String kind;
  private String rssLink;
  private float percent;
  private float total;
  private String imageUrl;
  private Date date;

  public HistoryItem() {
  }

  public float getTotal() {
    return total;
  }

  public void setTotal(float total) {
    this.total = total;
  }

  public String getPodcast() {
    return podcast;
  }

  public void setPodcast(String podcast) {
    this.podcast = podcast;
  }

  public String getEpisodeId() {
    return episodeId;
  }

  public void setEpisodeId(String episodeId) {
    this.episodeId = episodeId;
  }

  public String getEpisode() {
    return episode;
  }

  public void setEpisode(String episode) {
    this.episode = episode;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
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

  public String getRssLink() {
    return rssLink;
  }

  public void setRssLink(String rssLink) {
    this.rssLink = rssLink;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
