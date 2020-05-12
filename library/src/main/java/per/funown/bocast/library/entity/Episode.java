package per.funown.bocast.library.entity;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Entity
public class Episode implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private long id;

  private long podcastId;

  private String guid;

  @ColumnInfo
  private String title;

  @ColumnInfo
  private String subtitle;

  @ColumnInfo
  private String pubDate;

  @ColumnInfo
  private String duration;

  @ColumnInfo
  private String link;

  @ColumnInfo
  private String enclosure;

  @ColumnInfo
  private String image;

  @ColumnInfo
  private String showNotes;

  public Episode() {}

  @Ignore
  public Episode(long podcastId, String guid, String title, String subtitle, String pubDate,
      String duration, String link, String enclosure, String image,
      String showNotes) {
    this.podcastId = podcastId;
    this.guid = guid;
    this.title = title;
    this.subtitle = subtitle;
    this.pubDate = pubDate;
    this.duration = duration;
    this.link = link;
    this.enclosure = enclosure;
    this.image = image;
    this.showNotes = showNotes;
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

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public String getPubDate() {
    return pubDate;
  }

  public void setPubDate(String pubDate) {
    this.pubDate = pubDate;
  }


  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getEnclosure() {
    return enclosure;
  }

  public void setEnclosure(String enclosure) {
    this.enclosure = enclosure;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getShowNotes() {
    return showNotes;
  }

  public void setShowNotes(String showNotes) {
    this.showNotes = showNotes;
  }
}
