package per.funown.bocast.library.entity;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
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
  private int id;

  private String guid;

  @ColumnInfo(name = "Episode_title")
  private String title;

  @ColumnInfo(name = "Subtitle")
  private String subtitle;

  @ColumnInfo(name = "Publish_date")
  private Date pubDate;

  @ColumnInfo(name = "Author")
  private String author;

  @ColumnInfo(name = "Duration")
  private Double duration;

  @ColumnInfo(name = "link")
  private String link;

  @ColumnInfo(name = "Music_source_url")
  private String enclosure;

  @ColumnInfo(name = "Image_source_url")
  private String image;

  @ColumnInfo(name = "ShowNotes")
  private String showNotes;

  @ColumnInfo(name = "tags")
  private List<String> tags;

  public Episode() {}

  public Episode(String guid, String title, String subtitle, Date pubDate, String author,
      Double duration, String link, String enclosure, String image, String showNotes,
      List<String> tags) {
    this.guid = guid;
    this.title = title;
    this.subtitle = subtitle;
    this.pubDate = pubDate;
    this.author = author;
    this.duration = duration;
    this.link = link;
    this.enclosure = enclosure;
    this.image = image;
    this.showNotes = showNotes;
    this.tags = tags;
  }

  public int getId() {
    return id;
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

  public void setSubtitle(String subTitle) {
    this.subtitle = subTitle;
  }

  public Date getPubDate() {
    return pubDate;
  }

  public void setPubDate(Date pubDate) {
    this.pubDate = pubDate;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Double getDuration() {
    return duration;
  }

  public void setDuration(Double duration) {
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

  public void setEnclosure(String musicSrcUrl) {
    this.enclosure = musicSrcUrl;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String imgSrcUrl) {
    this.image = imgSrcUrl;
  }

  public String getShowNotes() {
    return showNotes;
  }

  public void setShowNotes(String showNotes) {
    this.showNotes = showNotes;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public Episode clone() {
    Episode episode = new Episode();
    episode.setGuid(this.guid);
    episode.setTags(this.tags);
    episode.setLink(this.link);
    episode.setTitle(this.title);
    episode.setAuthor(this.author);
    episode.setPubDate(this.pubDate);
    episode.setDuration(this.duration);
    episode.setShowNotes(this.showNotes);
    episode.setImage(this.image);
    episode.setEnclosure(this.enclosure);
    return episode;
  }
}
