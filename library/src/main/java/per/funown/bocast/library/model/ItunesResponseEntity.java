package per.funown.bocast.library.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/02/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ItunesResponseEntity implements Serializable {

  private static final long serialVersionUID = 6213525121345646450L;
  /**
   * "wrapperType":"track",
   * "kind":"podcast",
   * "collectionId":1053786200,
   * "trackId":1053786200,
   * "artistName":"JJ Ying & Leon Gao",
   * "collectionName":"Anyway.FM 设计杂谈",
   * "trackName":"Anyway.FM 设计杂谈",
   * "collectionCensoredName":"Anyway.FM 设计杂谈",
   * "trackCensoredName":"Anyway.FM 设计杂谈",
   * "collectionViewUrl":"https://podcasts.apple.com/us/podcast/anyway-fm-%E8%AE%BE%E8%AE%A1%E6%9D%82%E8%B0%88/id1053786200?uo=4",
   * "feedUrl":"https://Anyway.FM/rss.xml",
   * "trackViewUrl":"https://podcasts.apple.com/us/podcast/anyway-fm-%E8%AE%BE%E8%AE%A1%E6%9D%82%E8%B0%88/id1053786200?uo=4",
   * "artworkUrl30":"https://is5-ssl.mzstatic.com/image/thumb/Podcasts113/v4/d3/72/25/d37225d8-f656-21eb-f223-e35c9aad8513/mza_9527851597791836093.png/30x30bb.jpg",
   * "artworkUrl60":"https://is5-ssl.mzstatic.com/image/thumb/Podcasts113/v4/d3/72/25/d37225d8-f656-21eb-f223-e35c9aad8513/mza_9527851597791836093.png/60x60bb.jpg",
   * "artworkUrl100":"https://is5-ssl.mzstatic.com/image/thumb/Podcasts113/v4/d3/72/25/d37225d8-f656-21eb-f223-e35c9aad8513/mza_9527851597791836093.png/100x100bb.jpg",
   * "collectionPrice":0.00,
   * "trackPrice":0.00,
   * "trackRentalPrice":0,
   * "collectionHdPrice":0,
   * "trackHdPrice":0,
   * "trackHdRentalPrice":0,
   * "releaseDate":"2020-02-20T16:15:00Z",
   * "collectionExplicitness":"cleaned",
   * "trackExplicitness":"cleaned",
   * "trackCount":103,
   * "country":"USA",
   * "currency":"USD",
   * "primaryGenreName":"Design",
   * "contentAdvisoryRating":"Clean",
   * "artworkUrl600":"https://is5-ssl.mzstatic.com/image/thumb/Podcasts113/v4/d3/72/25/d37225d8-f656-21eb-f223-e35c9aad8513/mza_9527851597791836093.png/600x600bb.jpg",
   * "genreIds":["1402", "26", "1301", "1406"],
   * "genres":["Design", "Podcasts", "Arts", "Visual Arts"]
   */
  private int id;

  private String wrapperType;
  private String kind;
  private String collectionId;
  private String trackId;
  private String artistName;
  private String trackName;
  private String collectionCensoredName;
  private String trackCensoredName;
  private String collectionViewUrl;
  private String feedUrl;
  private String trackViewUrl;
  private String artworkUrl30;
  private String artworkUrl60;
  private String artworkUrl100;
  private String artworkUrl600;
  private double collectionPrice;
  private double trackPrice;
  private int trackRentalPrice;
  private int collectionHdPrice;
  private int trackHdPrice;
  private int trackHdRentalPrice;
  private String releaseDate;
  private String collectionExplicitness;
  private int trackCount;
  private String country;
  private String currency;
  private String primaryGenreName;
  private String contentAdvisoryRating;
  private String[] genreIds;
  private String[] genres;

  public ItunesResponseEntity() {}
  public ItunesResponseEntity(String wrapperType, String kind, String collectionId, String trackId,
                              String artistName, String trackName, String collectionCensoredName,
                              String trackCensoredName, String collectionViewUrl, String feedUrl,
                              String trackViewUrl, String artworkUrl30, String artworkUrl60,
                              String artworkUrl100, String artworkUrl600, double collectionPrice,
                              double trackPrice, int trackRentalPrice, int collectionHdPrice,
                              int trackHdPrice, int trackHdRentalPrice, String releaseDate,
                              String collectionExplicitness, int trackCount, String country,
                              String currency, String primaryGenreName, String contentAdvisoryRating,
                              String[] genreIds, String[] genres) {
    this.wrapperType = wrapperType;
    this.kind = kind;
    this.collectionId = collectionId;
    this.trackId = trackId;
    this.artistName = artistName;
    this.trackName = trackName;
    this.collectionCensoredName = collectionCensoredName;
    this.trackCensoredName = trackCensoredName;
    this.collectionViewUrl = collectionViewUrl;
    this.feedUrl = feedUrl;
    this.trackViewUrl = trackViewUrl;
    this.artworkUrl30 = artworkUrl30;
    this.artworkUrl60 = artworkUrl60;
    this.artworkUrl100 = artworkUrl100;
    this.artworkUrl600 = artworkUrl600;
    this.collectionPrice = collectionPrice;
    this.trackPrice = trackPrice;
    this.trackRentalPrice = trackRentalPrice;
    this.collectionHdPrice = collectionHdPrice;
    this.trackHdPrice = trackHdPrice;
    this.trackHdRentalPrice = trackHdRentalPrice;
    this.releaseDate = releaseDate;
    this.collectionExplicitness = collectionExplicitness;
    this.trackCount = trackCount;
    this.country = country;
    this.currency = currency;
    this.primaryGenreName = primaryGenreName;
    this.contentAdvisoryRating = contentAdvisoryRating;
    this.genreIds = genreIds;
    this.genres = genres;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getWrapperType() {
    return wrapperType;
  }

  public void setWrapperType(String wrapperType) {
    this.wrapperType = wrapperType;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public String getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  public String getTrackId() {
    return trackId;
  }

  public void setTrackId(String trackId) {
    this.trackId = trackId;
  }

  public String getArtistName() {
    return artistName;
  }

  public void setArtistName(String artistName) {
    this.artistName = artistName;
  }

  public String getTrackName() {
    return trackName;
  }

  public void setTrackName(String trackName) {
    this.trackName = trackName;
  }

  public String getCollectionCensoredName() {
    return collectionCensoredName;
  }

  public void setCollectionCensoredName(String collectionCensoredName) {
    this.collectionCensoredName = collectionCensoredName;
  }

  public String getTrackCensoredName() {
    return trackCensoredName;
  }

  public void setTrackCensoredName(String trackCensoredName) {
    this.trackCensoredName = trackCensoredName;
  }

  public String getCollectionViewUrl() {
    return collectionViewUrl;
  }

  public void setCollectionViewUrl(String collectionViewUrl) {
    this.collectionViewUrl = collectionViewUrl;
  }

  public String getFeedUrl() {
    return feedUrl;
  }

  public void setFeedUrl(String feedUrl) {
    this.feedUrl = feedUrl;
  }

  public String getTrackViewUrl() {
    return trackViewUrl;
  }

  public void setTrackViewUrl(String trackViewUrl) {
    this.trackViewUrl = trackViewUrl;
  }

  public String getArtworkUrl30() {
    return artworkUrl30;
  }

  public void setArtworkUrl30(String artworkUrl30) {
    this.artworkUrl30 = artworkUrl30;
  }

  public String getArtworkUrl60() {
    return artworkUrl60;
  }

  public void setArtworkUrl60(String artworkUrl60) {
    this.artworkUrl60 = artworkUrl60;
  }

  public String getArtworkUrl100() {
    return artworkUrl100;
  }

  public void setArtworkUrl100(String artworkUrl100) {
    this.artworkUrl100 = artworkUrl100;
  }

  public String getArtworkUrl600() {
    return artworkUrl600;
  }

  public void setArtworkUrl600(String artworkUrl600) {
    this.artworkUrl600 = artworkUrl600;
  }

  public double getCollectionPrice() {
    return collectionPrice;
  }

  public void setCollectionPrice(double collectionPrice) {
    this.collectionPrice = collectionPrice;
  }

  public double getTrackPrice() {
    return trackPrice;
  }

  public void setTrackPrice(double trackPrice) {
    this.trackPrice = trackPrice;
  }

  public int getTrackRentalPrice() {
    return trackRentalPrice;
  }

  public void setTrackRentalPrice(int trackRentalPrice) {
    this.trackRentalPrice = trackRentalPrice;
  }

  public int getCollectionHdPrice() {
    return collectionHdPrice;
  }

  public void setCollectionHdPrice(int collectionHdPrice) {
    this.collectionHdPrice = collectionHdPrice;
  }

  public int getTrackHdPrice() {
    return trackHdPrice;
  }

  public void setTrackHdPrice(int trackHdPrice) {
    this.trackHdPrice = trackHdPrice;
  }

  public int getTrackHdRentalPrice() {
    return trackHdRentalPrice;
  }

  public void setTrackHdRentalPrice(int trackHdRentalPrice) {
    this.trackHdRentalPrice = trackHdRentalPrice;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getCollectionExplicitness() {
    return collectionExplicitness;
  }

  public void setCollectionExplicitness(String collectionExplicitness) {
    this.collectionExplicitness = collectionExplicitness;
  }

  public int getTrackCount() {
    return trackCount;
  }

  public void setTrackCount(int trackCount) {
    this.trackCount = trackCount;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getPrimaryGenreName() {
    return primaryGenreName;
  }

  public void setPrimaryGenreName(String primaryGenreName) {
    this.primaryGenreName = primaryGenreName;
  }

  public String getContentAdvisoryRating() {
    return contentAdvisoryRating;
  }

  public void setContentAdvisoryRating(String contentAdvisoryRating) {
    this.contentAdvisoryRating = contentAdvisoryRating;
  }

  public String[] getGenreIds() {
    return genreIds;
  }

  public void setGenreIds(String[] genreIds) {
    this.genreIds = genreIds;
  }

  public String[] getGenres() {
    return genres;
  }

  public void setGenres(String[] genres) {
    this.genres = genres;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ItunesResponseEntity that = (ItunesResponseEntity) o;
    return id == that.id &&
        Double.compare(that.collectionPrice, collectionPrice) == 0 &&
        Double.compare(that.trackPrice, trackPrice) == 0 &&
        trackRentalPrice == that.trackRentalPrice &&
        collectionHdPrice == that.collectionHdPrice &&
        trackHdPrice == that.trackHdPrice &&
        trackHdRentalPrice == that.trackHdRentalPrice &&
        trackCount == that.trackCount &&
        Objects.equals(wrapperType, that.wrapperType) &&
        Objects.equals(kind, that.kind) &&
        Objects.equals(collectionId, that.collectionId) &&
        Objects.equals(trackId, that.trackId) &&
        Objects.equals(artistName, that.artistName) &&
        Objects.equals(trackName, that.trackName) &&
        Objects.equals(collectionCensoredName, that.collectionCensoredName) &&
        Objects.equals(trackCensoredName, that.trackCensoredName) &&
        Objects.equals(collectionViewUrl, that.collectionViewUrl) &&
        Objects.equals(feedUrl, that.feedUrl) &&
        Objects.equals(trackViewUrl, that.trackViewUrl) &&
        Objects.equals(artworkUrl30, that.artworkUrl30) &&
        Objects.equals(artworkUrl60, that.artworkUrl60) &&
        Objects.equals(artworkUrl100, that.artworkUrl100) &&
        Objects.equals(artworkUrl600, that.artworkUrl600) &&
        Objects.equals(releaseDate, that.releaseDate) &&
        Objects.equals(collectionExplicitness, that.collectionExplicitness) &&
        Objects.equals(country, that.country) &&
        Objects.equals(currency, that.currency) &&
        Objects.equals(primaryGenreName, that.primaryGenreName) &&
        Objects.equals(contentAdvisoryRating, that.contentAdvisoryRating) &&
        Arrays.equals(genreIds, that.genreIds) &&
        Arrays.equals(genres, that.genres);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, wrapperType, kind, collectionId, trackId, artistName, trackName,
        collectionCensoredName, trackCensoredName, collectionViewUrl, feedUrl, trackViewUrl, artworkUrl30,
        artworkUrl60, artworkUrl100, artworkUrl600, collectionPrice, trackPrice, trackRentalPrice,
        collectionHdPrice, trackHdPrice, trackHdRentalPrice, releaseDate, collectionExplicitness,
        trackCount, country, currency, primaryGenreName, contentAdvisoryRating);
    result = 31 * result + Arrays.hashCode(genreIds);
    result = 31 * result + Arrays.hashCode(genres);
    return result;
  }

  @Override
  public String toString() {
    return "ITunesResponseEntity{" +
        "id=" + id +
        ", wrapperType='" + wrapperType + '\'' +
        ", kind='" + kind + '\'' +
        ", collectionId='" + collectionId + '\'' +
        ", trackId='" + trackId + '\'' +
        ", artistName='" + artistName + '\'' +
        ", trackName='" + trackName + '\'' +
        ", collectionCensoredName='" + collectionCensoredName + '\'' +
        ", trackCensoredName='" + trackCensoredName + '\'' +
        ", collectionViewUrl='" + collectionViewUrl + '\'' +
        ", feedUrl='" + feedUrl + '\'' +
        ", trackViewUrl='" + trackViewUrl + '\'' +
        ", artworkUrl30='" + artworkUrl30 + '\'' +
        ", artworkUrl60='" + artworkUrl60 + '\'' +
        ", artworkUrl100='" + artworkUrl100 + '\'' +
        ", artworkUrl600='" + artworkUrl600 + '\'' +
        ", collectionPrice=" + collectionPrice +
        ", trackPrice=" + trackPrice +
        ", trackRentalPrice=" + trackRentalPrice +
        ", collectionHdPrice=" + collectionHdPrice +
        ", trackHdPrice=" + trackHdPrice +
        ", trackHdRentalPrice=" + trackHdRentalPrice +
        ", releaseDate='" + releaseDate + '\'' +
        ", collectionExplicitness='" + collectionExplicitness + '\'' +
        ", trackCount=" + trackCount +
        ", country='" + country + '\'' +
        ", currency='" + currency + '\'' +
        ", primaryGenreName='" + primaryGenreName + '\'' +
        ", contentAdvisoryRating='" + contentAdvisoryRating + '\'' +
        ", genreIds=" + Arrays.toString(genreIds) +
        ", genres=" + Arrays.toString(genres) +
        '}';
  }


}
