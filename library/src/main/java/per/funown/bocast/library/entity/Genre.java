package per.funown.bocast.library.entity;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/03/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Entity
public class Genre {

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo
  private String genre;

  @ColumnInfo
  private String itunesid;

  @ColumnInfo
  private int weight;

  public Genre(String genre, int weight, String itunesid) {
    this.genre = genre;
    this.weight = weight;
    this.itunesid = itunesid;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public String getItunesid() {
    return itunesid;
  }

  public void setItunesid(String itunesid) {
    this.itunesid = itunesid;
  }

  @Override
  public String toString() {
    return "Genre{" +
        "id=" + id +
        ", genre='" + genre + '\'' +
        ", iTunesId='" + itunesid + '\'' +
        ", weight=" + weight +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Genre genre1 = (Genre) o;
    return id == genre1.id &&
        weight == genre1.weight &&
        Objects.equals(genre, genre1.genre) &&
        Objects.equals(itunesid, genre1.itunesid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, genre, itunesid, weight);
  }
}
