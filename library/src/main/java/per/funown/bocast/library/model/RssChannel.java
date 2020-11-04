package per.funown.bocast.library.model;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;
import java.util.List;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/06
 *     desc   :
 *     version:
 * </pre>
 */
@Xml(name = "channel")
public class RssChannel {

  @PropertyElement
  private String copyright;

  @PropertyElement
  private String title;

  @PropertyElement(name = "itunes:subtitle")
  private String subtitle;

  @PropertyElement
  private String link;

  @Element(name = "atom:link")
  private AtomLink atomLink;


  @PropertyElement
  private String description;

  @PropertyElement
  private String language;

  @Element(name = "itunes:category")
  private List<iTunesCategory> categorys;

  @Element(name = "itunes:image")
  private iTunesImage image;

  @PropertyElement(name = "itunes:keywords")
  private String keywords;

  @Element(name = "itunes:owner")
  private iTunesOwner owner;

  @PropertyElement(name = "itunes:author")
  private String author;

  @Element(name = "item")
  private List<RssItem> items;

  public String getTitle() {
    return title;
  }

  public String getLink() {
    return link;
  }

  public String getDescription() {
    return description;
  }

  public String getLanguage() {
    return language;
  }

  public List<iTunesCategory> getCategorys() {
    return categorys;
  }

  public iTunesImage getImage() {
    return image;
  }

  public String getKeywords() {
    return keywords;
  }

  public iTunesOwner getOwner() {
    return owner;
  }

  public List<RssItem> getItems() {
    return items;
  }

  public String getCopyright() {
    return copyright;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public AtomLink getAtomLink() {
    return atomLink;
  }

  public void setAtomLink(AtomLink atomLink) {
    this.atomLink = atomLink;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setCategorys(List<iTunesCategory> categorys) {
    this.categorys = categorys;
  }

  public void setImage(iTunesImage image) {
    this.image = image;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public void setOwner(iTunesOwner owner) {
    this.owner = owner;
  }

  public void setItems(List<RssItem> items) {
    this.items = items;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Override
  public String toString() {
    return "RssChannel{" +
        "copyright='" + copyright + '\'' +
        ", title='" + title + '\'' +
        ", subtitle='" + subtitle + '\'' +
        ", link='" + link + '\'' +
        ", atomLink=" + atomLink +
        ", description='" + description + '\'' +
        ", language='" + language + '\'' +
        ", categorys=" + categorys +
        ", image=" + image +
        ", keywords='" + keywords + '\'' +
        ", owner=" + owner +
        ", author='" + author + '\'' +
        ", items=" + items +
        '}';
  }
}
