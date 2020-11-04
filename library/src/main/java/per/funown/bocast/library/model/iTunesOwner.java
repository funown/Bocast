package per.funown.bocast.library.model;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/06
 *     desc   :
 *     version:
 * </pre>
 */
@Xml(name = "itunes:owner")
public class iTunesOwner {
  @PropertyElement(name = "itunes:name")
  private String name;
  @PropertyElement(name = "itunes:email")
  private String email;

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "iTunesOwner{" +
        "name='" + name + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
