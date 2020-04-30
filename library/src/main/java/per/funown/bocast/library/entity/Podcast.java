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
  private int id;

  @ColumnInfo(name = "Podcast_title")
  private String title;

  @ColumnInfo(name = "Podcast_author")
  private String author;

  @ColumnInfo(name = "Podcast_logo_src")
  private String imgLogoSrc;

  @ColumnInfo(name = "Podcast_rss_link")
  private String rssLink;

  @Ignore
  public Podcast() {}
  public Podcast(String title, String author, String imgLogoSrc, String rssLink) {
    this.title = title;
    this.author = author;
    this.imgLogoSrc = imgLogoSrc;
    this.rssLink = rssLink;
  }
  @Ignore
  public Podcast(int id, String title, String author, String imgLogoSrc, String rssLink) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.imgLogoSrc = imgLogoSrc;
    this.rssLink = rssLink;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
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

  public String getImgLogoSrc() {
    return imgLogoSrc;
  }

  public void setImgLogoSrc(String imgLogoSrc) {
    this.imgLogoSrc = imgLogoSrc;
  }

  public String getRssLink() {
    return rssLink;
  }

  public void setRssLink(String rssLink) {
    this.rssLink = rssLink;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Podcast podcast = (Podcast) o;
    return id == podcast.id &&
        Objects.equals(title, podcast.title) &&
        Objects.equals(author, podcast.author) &&
        Objects.equals(imgLogoSrc, podcast.imgLogoSrc) &&
        Objects.equals(rssLink, podcast.rssLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, author, imgLogoSrc, rssLink);
  }

  /**
   * clone the podcast object
   * @return a new podcast object
   */
  public Podcast clone() {
    return new Podcast(this.title, this.title, this.imgLogoSrc, this.rssLink);
  }
}
