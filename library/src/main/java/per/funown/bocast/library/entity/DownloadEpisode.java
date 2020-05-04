package per.funown.bocast.library.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import per.funown.bocast.library.download.DownloadStatus;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Entity
public class DownloadEpisode implements Serializable {

  private static final long serialVersionUID = -8841776187967078936L;

  @PrimaryKey(autoGenerate = true)
  private long id;

  @ColumnInfo(name = "Episode's key")
  private String guid;

  @ColumnInfo(name = "podcast's name")
  private String podcast;

  private String podcastId;

  private String imageUri;

  private String rssLink;

  private String episodeTitle;

  private int episode;

  private String filename;

  private String status;

  private long total;

  private long offset;

  private String url;

  @Ignore
  public DownloadEpisode() {}

  public DownloadEpisode(String guid, String podcast, String podcastId, String imageUri,
      String rssLink, String episodeTitle, int episode, String filename, String status, long total,
      long offset, String url) {
    this.guid = guid;
    this.podcast = podcast;
    this.podcastId = podcastId;
    this.imageUri = imageUri;
    this.rssLink = rssLink;
    this.episodeTitle = episodeTitle;
    this.episode = episode;
    this.filename = filename;
    this.status = status;
    this.total = total;
    this.offset = offset;
    this.url = url;
  }

  public String getImageUri() {
    return imageUri;
  }

  public void setImageUri(String imageUri) {
    this.imageUri = imageUri;
  }

  public String getRssLink() {
    return rssLink;
  }

  public void setRssLink(String rssLink) {
    this.rssLink = rssLink;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getPodcast() {
    return podcast;
  }

  public void setPodcast(String podcast) {
    this.podcast = podcast;
  }

  public int getEpisode() {
    return episode;
  }

  public void setEpisode(int episode) {
    this.episode = episode;
  }

  public String getPodcastId() {
    return podcastId;
  }

  public void setPodcastId(String podcastId) {
    this.podcastId = podcastId;
  }

  public DownloadStatus getDownloadStatus() {
    return DownloadStatus.valueOf(this.status);
  }

  public void setStatus(DownloadStatus status) {
    this.status = status.name();
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public String getEpisodeTitle() {
    return episodeTitle;
  }

  public void setEpisodeTitle(String episodeTitle) {
    this.episodeTitle = episodeTitle;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public long getOffset() {
    return offset;
  }

  public void setOffset(long offset) {
    this.offset = offset;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
