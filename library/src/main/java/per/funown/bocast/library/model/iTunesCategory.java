package per.funown.bocast.library.model;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
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
@Xml(name = "itunes:category")
public class iTunesCategory {

  @Attribute
  private String text;

  @Element(name = "itunes:category")
  private List<iTunesCategory> categorys;

  public String getText() {
    return text;
  }

  public List<iTunesCategory> getCategorys() {
    return categorys;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setCategorys(List<iTunesCategory> categorys) {
    this.categorys = categorys;
  }

  @Override
  public String toString() {
    return "iTunesCategory{" +
        "text='" + text + '\'' +
        ", categorys=" + categorys +
        '}';
  }
}
