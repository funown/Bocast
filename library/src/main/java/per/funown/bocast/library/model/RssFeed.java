package per.funown.bocast.library.model;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

/**
 * <pre>
 *     author : funown
 *     time   : 2020/04/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
//@Root(name = "rss", strict = false)
//@NamespaceList({
//    @Namespace(reference = "http://www.w3.org/2005/Atom", prefix = "atom"),
//    @Namespace(reference = "http://purl.org/rss/1.0/modules/content/", prefix = "content"),
//    @Namespace(reference = "http://www.itunes.com/dtds/podcast-1.0.dtd", prefix = "itunes"),
//    @Namespace(reference = "http://www.google.com/schemas/play-podcasts/1.0", prefix = "googleplay"),
//})
@Xml(writeNamespaces = {"atom=http://www.w3.org/2005/Atom",
    "content=http://purl.org/rss/1.0/modules/content/",
    "itunes=http://www.itunes.com/dtds/podcast-1.0.dtd",
    "googleplay=http://www.google.com/schemas/play-podcasts/1.0"}, name = "rss")
public class RssFeed {

  @Attribute
  private String encoding;

  @Element(name = "channel")
  private RssChannel channel;

  public String getEncoding() {
    return encoding;
  }

  public RssChannel getChannel() {
    return channel;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public void setChannel(RssChannel channel) {
    this.channel = channel;
  }

  @Override
  public String toString() {
    return "RssFeed{" +
        "channel=" + channel +
        '}';
  }
}
