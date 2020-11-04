package per.funown.bocast.library.model;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.TextContent;
import com.tickaroo.tikxml.annotation.Xml;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/06
 *     desc   :
 *     version:
 * </pre>
 */
@Xml(name = "guid")
public class Guid {

  @Attribute
  public String isPermaLink;

  @TextContent
  private String guid;

  public String getGuid() {
    return guid;
  }

  public String getIsPermaLink() {
    return isPermaLink;
  }

  public void setIsPermaLink(String isPermaLink) {
    this.isPermaLink = isPermaLink;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }
}
