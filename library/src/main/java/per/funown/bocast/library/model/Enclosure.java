package per.funown.bocast.library.model;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/06
 *     desc   :
 *     version:
 * </pre>
 */
@Xml(name = "enclosure")
public class Enclosure {

  @Attribute
  private String url;
  @Attribute
  private String length;
  @Attribute
  private String type;

  public String getLength() {
    return length;
  }

  public String getType() {
    return type;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setLength(String length) {
    this.length = length;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Enclosure{" +
        "url='" + url + '\'' +
        ", length='" + length + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
