package per.funown.bocast.library.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/19
 *     desc   : Podcast class
 *     version: 1.0
 * </pre>
 *
 */
@Entity
public class Podcast implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private long id;
  @ColumnInfo
  private String title;
  @ColumnInfo
  private String author;
  @ColumnInfo
  private int episodes;
  @ColumnInfo
  private String rssLink;
  @ColumnInfo
  private String logoLink;

  public Podcast(String title, String author, int episodes, String rssLink, String logoLink) {
    this.title = title;
    this.author = author;
    this.episodes = episodes;
    this.rssLink = rssLink;
    this.logoLink = logoLink;
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

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
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

  @Override
  public String toString() {
    return "Podcast{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", episodes=" + episodes +
        ", rssLink='" + rssLink + '\'' +
        ", logoLink='" + logoLink + '\'' +
        '}';
  }
}
