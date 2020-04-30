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

  @ColumnInfo(name = "the name of genre")
  private String genre;

  @ColumnInfo(name = "the id of genre in iTunes")
  public String iTunesId;

  private int weight;

  public Genre(String genre, int weight, String iTunesId) {
    this.genre = genre;
    this.weight = weight;
    this.iTunesId = iTunesId;
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

  public String getiTunesId() {
    return iTunesId;
  }

  public void setiTunesId(String iTunesId) {
    this.iTunesId = iTunesId;
  }

  @Override
  public String toString() {
    return "Genre{" +
        "id=" + id +
        ", genre='" + genre + '\'' +
        ", iTunesId='" + iTunesId + '\'' +
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
        Objects.equals(iTunesId, genre1.iTunesId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, genre, iTunesId, weight);
  }
}
