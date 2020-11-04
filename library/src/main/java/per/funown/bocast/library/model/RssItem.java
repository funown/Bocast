package per.funown.bocast.library.model;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/06
 *     desc   :
 *     version:
 * </pre>
 */
@Xml(name = "item")
public class RssItem {
  @PropertyElement
  private String title;
  @PropertyElement(name = "itunes:episode")
  private String episode;
  @PropertyElement
  private String link;
  @Element
  private Guid guid;
  @PropertyElement
  private String pubDate;
  @PropertyElement
  private String author;
  @Element
  private Enclosure enclosure;
  @PropertyElement(name = "itunes:subtitle")
  private String subtitle;
  @PropertyElement(name = "itunes:duration")
  private String duration;
  @Element
  private iTunesImage image;
  @PropertyElement(name = "description", writeAsCData = true)
  private String description;
  @PropertyElement(name = "content:encoded", writeAsCData = true)
  private String encodedContent;
  @PropertyElement(name = "itunes:summary", writeAsCData = true)
  private String summary;

  public String getTitle() {
    return title;
  }

  public String getEpisode() {
    return episode;
  }

  public String getLink() {
    return link;
  }

  public Guid getGuid() {
    return guid;
  }

  public String getPubDate() {
    return pubDate;
  }

  public String getAuthor() {
    return author;
  }

  public Enclosure getEnclosure() {
    return enclosure;
  }

//  public String getItunesTitle() {
//    return itunesTitle;
//  }

  public String getSubtitle() {
    return subtitle;
  }

  public String getDuration() {
    return duration;
  }

  public iTunesImage getImage() {
    return image;
  }

  public String getDescription() {
    return description;
  }

  public String getEncodedContent() {
    return encodedContent;
  }

  public String getSummary() {
    return summary;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setEpisode(String episode) {
    this.episode = episode;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public void setGuid(Guid guid) {
    this.guid = guid;
  }

  public void setPubDate(String pubDate) {
    this.pubDate = pubDate;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setEnclosure(Enclosure enclosure) {
    this.enclosure = enclosure;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public void setImage(iTunesImage image) {
    this.image = image;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setEncodedContent(String encodedContent) {
    this.encodedContent = encodedContent;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  @Override
  public String toString() {
    return "RssItem{" +
        "title='" + title + '\'' +
        ", episode='" + episode + '\'' +
        ", link='" + link + '\'' +
        ", guid=" + guid +
        ", pubDate='" + pubDate + '\'' +
        ", author='" + author + '\'' +
        ", enclosure=" + enclosure +
//        ", itunesTitle='" + itunesTitle + '\'' +
        ", subtitle='" + subtitle + '\'' +
        ", duration='" + duration + '\'' +
        ", image=" + image +
        ", description='" + description + '\'' +
        ", encodedContent='" + encodedContent + '\'' +
        ", summary='" + summary + '\'' +
        '}';
  }
}
