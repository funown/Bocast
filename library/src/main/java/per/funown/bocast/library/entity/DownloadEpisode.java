package per.funown.bocast.library.entity;

import androidx.annotation.NonNull;
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

  private long EpisodeId;

  private String filename;

  private String status;

  private long total;

  private long offset;

  private String url;

  public DownloadEpisode() {}

  @Ignore
  public DownloadEpisode(long episodeId, String filename, String status, long total, long offset,
      String url) {
    EpisodeId = episodeId;
    this.filename = filename;
    this.status = status;
    this.total = total;
    this.offset = offset;
    this.url = url;
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

  public void setEpisodeId(long episodeId) {
    EpisodeId = episodeId;
  }

  public long getEpisodeId() {
    return EpisodeId;
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

  @NonNull
  @Override
  public String toString() {
    return super.toString();
  }
}
