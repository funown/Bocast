package per.funown.bocast.library.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.Date;

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
  @ColumnInfo(name = "Podcast's name")
  private String title;
  @ColumnInfo(name = "author")
  private String author;
  @ColumnInfo(name = "the number of the podcast")
  private int episodes;
  @ColumnInfo(name = "the rss link of the podcast")
  private String rssLink;
  @ColumnInfo(name = "the link of the podcast's logo")
  private String logoLink;

  private Date updateTime;

  private Date subscribedTime;

  public SubscribedPodcast(String title, String author, int episodes, String rssLink,
      String logoLink, Date updateTime, Date subscribedTime) {
    this.title = title;
    this.author = author;
    this.episodes = episodes;
    this.rssLink = rssLink;
    this.logoLink = logoLink;
    this.updateTime = updateTime;
    this.subscribedTime = subscribedTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getEpisodes() {
    return episodes;
  }

  public void setEpisodes(int episodes) {
    this.episodes = episodes;
  }

  public String getRssLink() {
    return rssLink;
  }

  public void setRssLink(String rssLink) {
    this.rssLink = rssLink;
  }

  public String getLogoLink() {
    return logoLink;
  }

  public void setLogoLink(String logoLink) {
    this.logoLink = logoLink;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
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
  public String toString() {
    return "SubscribedPodcast{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", episodes=" + episodes +
        ", rssLink='" + rssLink + '\'' +
        ", logoLink='" + logoLink + '\'' +
        ", updateTime=" + updateTime +
        ", subscribedTime=" + subscribedTime +
        '}';
  }
}
