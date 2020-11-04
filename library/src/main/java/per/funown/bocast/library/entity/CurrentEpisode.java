package per.funown.bocast.library.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/07
 *     desc   : Current playing episode
 *     version: 1.0
 * </pre>
 */
@Entity
public class CurrentEpisode implements Serializable {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private String podcastTitle;
  private long podcastId;
  private String authors;
  private String rssLink;
  /* the number of the episode */
  private int episodeNo;
  /* the uri of the podcast's logo */
  private String logo;
  private String EpisodeTitle;
  /* the uri of the episode's cover */
  private String cover;
  /* the uri of the episode's track */
  private String enclosure;
  private Long duration;
  private Long currentTime;
  private String shownotes;
  private String guid;

  public CurrentEpisode() {
  }

  public CurrentEpisode(int id, String podcastTitle, long podcastId, String authors, String rssLink, int episodeNo,
      String logo, String episodeTitle, String cover, String enclosure, Long duration,
      Long currentTime, String shownotes, String guid) {
    this.id = id;
    this.podcastTitle = podcastTitle;
    this.podcastId = podcastId;
    this.authors = authors;
    this.rssLink = rssLink;
    this.episodeNo = episodeNo;
    this.logo = logo;
    EpisodeTitle = episodeTitle;
    this.cover = cover;
    this.enclosure = enclosure;
    this.duration = duration;
    this.currentTime = currentTime;
    this.shownotes = shownotes;
    this.guid = guid;
  }

  public CurrentEpisode(String podcastTitle, long podcastId, String authors, String rssLink, int episodeNo,
      String logo, String episodeTitle, String cover, String enclosure, Long duration,
      Long currentTime, String shownotes, String guid) {
    this.podcastTitle = podcastTitle;
    this.podcastId = podcastId;
    this.authors = authors;
    this.rssLink = rssLink;
    this.episodeNo = episodeNo;
    this.logo = logo;
    EpisodeTitle = episodeTitle;
    this.cover = cover;
    this.enclosure = enclosure;
    this.duration = duration;
    this.currentTime = currentTime;
    this.shownotes = shownotes;
    this.guid = guid;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPodcastTitle() {
    return podcastTitle;
  }

  public void setPodcastTitle(String podcastTitle) {
    this.podcastTitle = podcastTitle;
  }

  public long getPodcastId() {
    return podcastId;
  }

  public void setPodcastId(long podcastId) {
    this.podcastId = podcastId;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getRssLink() {
    return rssLink;
  }

  public void setRssLink(String rssLink) {
    this.rssLink = rssLink;
  }

  public int getEpisodeNo() {
    return episodeNo;
  }

  public void setEpisodeNo(int episodeNo) {
    this.episodeNo = episodeNo;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getEpisodeTitle() {
    return EpisodeTitle;
  }

  public void setEpisodeTitle(String episodeTitle) {
    EpisodeTitle = episodeTitle;
  }

  public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
    this.cover = cover;
  }

  public String getEnclosure() {
    return enclosure;
  }

  public void setEnclosure(String enclosure) {
    this.enclosure = enclosure;
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public Long getCurrentTime() {
    return currentTime;
  }

  public void setCurrentTime(Long currentTime) {
    this.currentTime = currentTime;
  }

  public String getShownotes() {
    return shownotes;
  }

  public void setShownotes(String shownotes) {
    this.shownotes = shownotes;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }
}
