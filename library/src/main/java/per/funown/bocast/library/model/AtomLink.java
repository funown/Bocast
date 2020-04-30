package per.funown.bocast.library.model;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Xml(name = "atom:link")
public class AtomLink {

  @Attribute
  private String href;

  @Attribute
  private String rel;

  @Attribute
  private String type;

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getRel() {
    return rel;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "AtomLink{" +
        "href='" + href + '\'' +
        ", rel='" + rel + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
